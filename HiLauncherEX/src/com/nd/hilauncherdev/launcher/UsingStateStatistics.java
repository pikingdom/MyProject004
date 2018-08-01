package com.nd.hilauncherdev.launcher;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.analysis.AppDistributeStatistics;
import com.nd.hilauncherdev.app.AppDataFactory;
import com.nd.hilauncherdev.app.CustomIntent;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.framework.httplib.GZipHttpUtil;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.AppsDefaultSwitch;
import com.nd.hilauncherdev.kitset.config.ConfigPreferences;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.config.preference.BaseSettingsPreference;
import com.nd.hilauncherdev.launcher.model.BaseLauncherSettings;
import com.nd.hilauncherdev.launcher.navigation.NavigationHelper;
import com.nd.hilauncherdev.launcher.support.LauncherOnStartDispatcher.OnLauncherStartListener;
import com.nd.hilauncherdev.settings.SettingsPreference;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.theme.data.ThemeData;
import com.nd.hilauncherdev.theme.pref.ThemeSharePref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;

/**
 * 改动备注：
 * V5.3.5
 * 重新开启：是否使用其它主题、是否设置默认桌面
 * 关闭：指尖特效、ABTest、桌面App使用情况、桌面小部件和推荐文件夹使用情况
 * 新增：用户下载主题套数统计
 *
 * V5.5
 * 新增：统计用户安装过多少种桌面
 *
 * V5.6
 * 关闭：用户桌面使用情况、统计用户下载主题套数
 * 新增：世界杯插件统计
 *
 * v5.7
 * 新增：是否安装了百度手机卫士
 *
 * v5.7.1
 * 新增：是否使用天天动听小部件、魔力球
 *
 * V6.0
 * 关闭：是否安装快点、世界杯插件安装情况
 * 新增：是否开启mini通知栏
 *
 * V6.0.1
 * 新增：使用文件夹风格
 *
 * V6.1
 * 新增：app市场安装、自动搬家统计、是否有装手电筒增强插件
 *
 * V6.5
 * 删除：360卫士，百度卫士，文件夹风格；
 * 新增：应用商店，游戏中心
 *
 * Description: 用户使用功能的状态统计
 * Author: guojy
 * Date: 2013-10-15 下午5:33:38
 */
public class UsingStateStatistics implements OnLauncherStartListener{
	private final static String TAG = "UsingStateStatistics";

	private static UsingStateStatistics instance = new UsingStateStatistics();

	public static UsingStateStatistics getInstance(){
		return instance;
	}

	@Override
	public int getType() {
		return OnLauncherStartListener.TYPE_ONCE_A_DAY_NOT_NETWORK;
	}

	@Override
	public void onLauncherStart(final Context ctx){
		ThreadUtil.executeOther(new Runnable() {
			@Override
			public void run() {
				try{
					boolean themeUser = applyOtherTheme();
					if (themeUser) {
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.APPLY_OTHER_THEME);
					}

					if (setAsDefaultLauncher()) {
						if (themeUser)
							HiAnalytics.submitEvent(ctx, AnalyticsConstant.SET_AS_DEFAULT_LAUNCHER, "Y");
						else
							HiAnalytics.submitEvent(ctx, AnalyticsConstant.SET_AS_DEFAULT_LAUNCHER, "N");
					}

					// 统计用户安装过多少种桌面
					statInstalledLauncher(ctx);

//					if(isInstall360Safe()){
//						HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALL_360_SAFE);
//					}

					//统计是否有装百度手机卫士
//					if(AndroidPackageUtils.isPkgInstalled(ctx, "cn.opda.a.phonoalbumshoushou")){
//						HiAnalytics.submitEvent(ctx, AnalyticsConstant.USE_BAIDU_MOBILESAFE);
//					}
					int themesCount = ThemeManagerFactory.getInstance().getThemesCount();
					String countLabel = themesCount > 10 ? ">10" : ""+themesCount;
					HiAnalytics.submitEvent(ctx, AnalyticsConstant.THEME_DOWNLOAD_COUNT, countLabel);
					//是否安装91桌面
					if(Global.isBaiduLauncher()
							&& AndroidPackageUtils.isPkgInstalled(Global.getApplicationContext(), "com.nd.android.pandahome2")){
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.HAS_INSTALL_91LAUNCHER);
					}

					// 统计是否开启魔力球
					SharedPreferences sp = ctx.getSharedPreferences("ShortCutSlotPrefs", Context.MODE_PRIVATE);
					if (sp.getBoolean("is_float_window_show", false)) {
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.USE_MOLIQIU);
					}

					//统计是否开启mini通知栏
					if(SettingsPreference.getInstance().getNotificationBarControl()){
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.USE_ADVANCED_NOTIFICATION);
					}

					//统计文件夹打开风格
//					HiAnalytics.submitEvent(ctx, AnalyticsConstant.USE_FOLDER_STYLE, "" + SettingsPreference.getInstance().getFolderStyle());

					// 统计app市场安装情况
					statInstalledMarket(ctx);

					//统计是否有装手电筒增强插件
					HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_FLASH_LIGHT,
							AndroidPackageUtils.isPkgInstalled(ctx, "com.nd.android.widget.pandahome.flashlight") ? "yes" : "no");
					//匣子里是否存在爱淘宝图标
					if(AppDataFactory.getInstance().hasAiTaobaoShortCut(ctx)){
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.HAS_AITAOBAO_ICON);
					}
					//存在推荐文件夹
					if(WorkspaceHelper.getRecommendFolderId(ctx) != -1){
						AppDistributeStatistics.AppDistributePercentConversionStatistics(ctx, "101");
					}
					//匣子里是否存在升级文件夹
					if(SettingsPreference.getInstance().isShowUpgradeFolder()){
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.HAS_DRAWER_UPDATE_FOLDER);
					}
					//桌面是否存在应用商店
					if (isLauncherExistAppshopIcon(ctx)){
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.HAS_APPSHOP_ICON);
					}
					//桌面是否存在热门游戏
					if (isLauncherExistGameCenterIcon(ctx)){
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.HAS_GAME_CENTER_ICON);
					}
//					//统计是否在使用天天主题
//					if(ThemeSeriesSharePref.getThemeSeriesLastSwitchDate(ctx) != -1){
//						HiAnalytics.submitEvent(ctx, AnalyticsConstant.USE_THEME_SERIES);
//					}
//					//桌面是否存在淘宝插件
//					if (isLauncherExistTaobaoWidget(ctx)){
//						HiAnalytics.submitEvent(ctx, AnalyticsConstant.HAS_TAOBAO_WIDGET);
//					}
					//桌面是否存在默认浏览器图标
					if(isExistDefaultBrowserIcon(ctx)){
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.HAS_LAUNCHER_DEFAULT_BROWSER_ICON);
					}
					//桌面是否存在爱淘宝图标
					if(isExistDefaultAiTaoBaoIcon(ctx)){
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.HAS_LAUNCHER_AITAOBAO_ICON);
					}
					//桌面是否存在百度一下图标
					if(isExistDefaultBaiduIcon(ctx)){
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.HAS_LAUNCHER_BAIDU_ICON);
					}
					//桌面是否存在唯品会图标
					if(isExistDefaultWeiPinHuiIcon(ctx)){
						HiAnalytics.submitEvent(ctx, AnalyticsConstant.HAS_LAUNCHER_WEIPINHUI_ICON);
					}
					
					//统计指定应用安装情况
					trackAppInstall(ctx);
					
					//零屏资讯屏打点统计,相同包名的定制版使用不同的lable
					int navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_LITIAN;

					if(LauncherBranchController.isLiTian()){
						navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_LITIAN;
					}else if(LauncherBranchController.isTianPai()){
						navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_TIANPAI;
					}else if(LauncherBranchController.isFanYue()){
						navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_FANYUE;
					}else if(LauncherBranchController.isJiaoYan()){
						navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_JIAOYAN;
					}else if(LauncherBranchController.isShuaJiJingLing()){
						navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_SHUAJIJINGLING;
					}else if(LauncherBranchController.isXinShiKong()){
						navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_XINSHIKONG;
					}else if(LauncherBranchController.isLiTian_Yinni()){
						navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_LITIAN_YINNI;
					}else if(LauncherBranchController.isDingKai()){
						navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_DINGKAI;
					}else if(LauncherBranchController.isTiapPai_SmartHome()){
						navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_TIANPAI_SMARTHOME;
					}else if(LauncherBranchController.isShenLong()){
						navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_SHENLONG;
					}else if(LauncherBranchController.isMingLai()){
						navigationAnaId = AnalyticsConstant.LAUNCHER_INFORMATION_TYPE_MINGLAI;
					}

					if(!BaseSettingsPreference.getInstance().isShowNavigationView() ||
							!BaseSettingsPreference.getInstance().hasNavigationView()){
						HiAnalytics.submitEvent(ctx, navigationAnaId, "w0p");
					}

					/** 打点功能代码移动到零屏插件中处理 */
					NavigationHelper.handleNavigationUsingStateStastics(ctx);

				}catch(Exception e){
					Log.e(TAG, e.toString());
				}
			}
		});


	}
	
	
	private static void statInstalledMarket(Context ctx){
		if(AndroidPackageUtils.isPkgInstalled(ctx, "com.dragon.android.pandaspace")){
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_MARKET, "91");
		}
		if(AndroidPackageUtils.isPkgInstalled(ctx, "com.hiapk.marketpho")){
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_MARKET, "hi");
		}
		if(AndroidPackageUtils.isPkgInstalled(ctx, "com.baidu.appsearch")){
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_MARKET, "bd");
		}
		if(AndroidPackageUtils.isPkgInstalled(ctx, "com.qihoo.appstore")){
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_MARKET, "360");
		}
		if(AndroidPackageUtils.isPkgInstalled(ctx, "com.tencent.android.qqdownloader")){
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_MARKET, "yyb");
		}
		if(AndroidPackageUtils.isPkgInstalled(ctx, "com.wandoujia.phoenix2")){
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_MARKET, "wdj");
		}
		if(AndroidPackageUtils.isPkgInstalled(ctx, "cn.goapk.market")){
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_MARKET, "anzhi");
		}
	}

	/**
	 * 统计用户安装过多少种桌面
	 * @param ctx
	 */
	private static void statInstalledLauncher(Context ctx){
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) { // 判断sd卡是否存在
			String qihoo360Launcher = Environment.getExternalStorageDirectory().getPath() + "/360launcher";// 360
			String goLauncher = Environment.getExternalStorageDirectory().getPath() + "/GOlauncherEX";// go桌面
			String baiduLauncher = Environment.getExternalStorageDirectory().getPath() + "/BaiduLauncher";// 百度桌面
			String dianxinLauncher = Environment.getExternalStorageDirectory().getPath() + "/dianxin/launcher";// 百度桌面
			String moxiuLauncher = Environment.getExternalStorageDirectory().getPath() + "/moxiu";// 魔秀桌面
			String qubeLauncher = Environment.getExternalStorageDirectory().getPath() + "/Tencent/qube";// Q立方
			String miuiLauncher = Environment.getExternalStorageDirectory().getPath() + "/MIUI";// 小米

			File qihoo = new File(qihoo360Launcher);
			File go = new File(goLauncher);
			File baidu = new File(baiduLauncher);
			File dianxin = new File(dianxinLauncher);
			File moxiu = new File(moxiuLauncher);
			File qube = new File(qubeLauncher);
			File miui = new File(miuiLauncher);
			if (qihoo.exists()) {
				HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_OTHER_LAUNCHER, "360");
			}
			if (go.exists()) {
				HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_OTHER_LAUNCHER, "go");
			}
			if (baidu.exists()) {
				HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_OTHER_LAUNCHER, "bd");
			}
			if (dianxin.exists()) {
				HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_OTHER_LAUNCHER, "dx");
			}
			if (moxiu.exists()) {
				HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_OTHER_LAUNCHER, "mx");
			}
			if (qube.exists()) {
				HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_OTHER_LAUNCHER, "qu");
			}
			if (miui.exists()
					&& !TelephoneUtil.isMIMoble() // 不是小米手机
					&& !TelephoneUtil.isMI2Moble()
					&& !"MI3".equalsIgnoreCase(android.os.Build.DEVICE)
					&& !"HM2013022".equalsIgnoreCase(android.os.Build.DEVICE)// 不是红米手机
					&& !"HM2013021".equalsIgnoreCase(android.os.Build.DEVICE)
					&& !"HM2013023".equalsIgnoreCase(android.os.Build.DEVICE)
					&& !"HM1".equalsIgnoreCase(android.os.Build.DEVICE)
					&& !"HMNOTE".equalsIgnoreCase(android.os.Build.DEVICE)) {// 不是红米note
				HiAnalytics.submitEvent(ctx, AnalyticsConstant.INSTALLED_OTHER_LAUNCHER, "mi");
			}
		}
	}

	/**
	 * 是否安装360手机卫士
	 */
//	private static boolean isInstall360Safe(){
//		String[] PkgName_360Safe = {
//				"com.qihoo360.mobilesafe",
//				"com.qihoo360.mobilesafe_meizu",
//				"com.qihoo360.mobilesafe_lenovo"
//		};
//		for(String pkgName : PkgName_360Safe){
//			if(AndroidPackageUtils.isPkgInstalled(Global.getApplicationContext(), pkgName))
//				return true;
//		}
//		return false;
//	}


	/*
	 * 在应用其它主题
	 */
	private static boolean applyOtherTheme(){
		return !ThemeSharePref.getInstance(Global.getApplicationContext()).isDefaultTheme();
	}


	/*
	 * 设置为默认桌面
	 */
	private static boolean setAsDefaultLauncher(){
		AppsDefaultSwitch homeSwitch = new AppsDefaultSwitch(Global.getApplicationContext());

		return homeSwitch.getIsPandaHomeDefault();
	}

/*	public static boolean isOpennetTraffic(Context ctx, boolean defValue){
		int sdkLevel = Build.VERSION.SDK_INT;
		SharedPreferences sps = ctx.getSharedPreferences(PREFS_FILE, (sdkLevel > Build.VERSION_CODES.FROYO) ? 4 : Context.MODE_WORLD_READABLE);
		return sps.getBoolean(START_NET_LIS, defValue);
	}*/



	/**
	 * 铃声文件过滤器
	 */
	public static FileFilter mp3fileFilter = new FileFilter() {
		public boolean accept(File pathname) {
			String tmp = pathname.getName().toLowerCase();
			if (tmp.endsWith(".mp3")) {
				return true;
			}
			return false;
		}
	};
	/**
	 * 桌面是否存在应用商店图标
	 * <p>Title: isLauncherExistAppshopIcon</p>
	 * <p>Description: </p>
	 * @return
	 * @author maolinnan_350804
	 */
	private static boolean isLauncherExistAppshopIcon(Context context){
		Intent intent = new Intent();
		intent.setAction(CustomIntent.ACTION_APP_STORE);
		return LauncherModel.shortcutExists(context, context.getText(R.string.myphone_app_store).toString(),intent);
	}
	/**
	 * 桌面是否存在游戏中心图标
	 * <p>Title: isLauncherExistGameCenterIcon</p>
	 * <p>Description: </p>
	 * @return
	 * @author maolinnan_350804
	 */
	private static boolean isLauncherExistGameCenterIcon(Context context){
		Intent intent = new Intent();
		intent.setAction(CustomIntent.ACTION_ONE_KEY_PHONE_PLAY);
		return LauncherModel.shortcutExists(context, context.getText(R.string.app_market_one_key_play).toString(),intent);
	}
	/**
	 * 桌面是否存在淘宝插件
	 * <p>Title: isLauncherExistTaobaoWidget</p>
	 * <p>Description: </p>
	 * @param context
	 * @return
	 * @author maolinnan_350804
	 */
//	private static boolean isLauncherExistTaobaoWidget(Context context){
//		boolean result = false;
//		Cursor c = null;
//		try {
//			final ContentResolver cr = context.getContentResolver();
//			c = cr.query(BaseLauncherSettings.Favorites.getContentUri(), new String[] { "itemType"}, 
//					"itemType=?", new String[] {Integer.valueOf(LauncherSettings.Favorites.ITEM_TYPE_WIDGET_TAOBAO).toString()}, null);
//			result = c.moveToFirst();
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally {
//			c.close();
//		}
//		return result;
//	}
	
	private static boolean isExistDefaultBrowserIcon(Context ctx){
		Cursor c = null;
		try {
			final ContentResolver cr = ctx.getContentResolver();
			c = cr.query(BaseLauncherSettings.Favorites.getContentUri(), new String[] {"intent" }, 
					"intent=?", new String[] { ThemeData.INTENT_BROWSER }, null);
			return c.getCount() > 0;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			c.close();
		}
		return false;
	}
	
	private static boolean isExistDefaultAiTaoBaoIcon(Context ctx){
		Cursor c = null;
		try {
			final ContentResolver cr = ctx.getContentResolver();
			c = cr.query(BaseLauncherSettings.Favorites.getContentUri(), new String[] {"intent" }, 
					"intent like '%" + CustomIntent.ACTION_APP_AI_TAOBAO + "%' or intent = '" + LauncherProviderHelper.AITAOBAO_INTENT + "'", null, null);
			return c.getCount() > 0;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			c.close();
		}
		return false;
	}
	
	private static boolean isExistDefaultBaiduIcon(Context ctx){
		Cursor c = null;
		try {
			final ContentResolver cr = ctx.getContentResolver();
			c = cr.query(BaseLauncherSettings.Favorites.getContentUri(), new String[] {"intent" }, 
					"intent like '%" + CustomIntent.ACTION_APP_BAIDUYIXIA  +"%'", null, null);
			return c.getCount() > 0;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			c.close();
		}
		return false;
	}
	
	private static boolean isExistDefaultWeiPinHuiIcon(Context ctx){
		Cursor c = null;
		try {
			final ContentResolver cr = ctx.getContentResolver();
			c = cr.query(BaseLauncherSettings.Favorites.getContentUri(), new String[] {"intent" }, 
					"intent like '%" + CustomIntent.ACTION_APP_WEIPINHUI  +"%'", null, null);
			return c.getCount() > 0;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			c.close();
		}
		return false;
	}
	
	public void trackAppInstall(Context ctx){
		try{
			String url = "http://pandahome.ifjing.com/commonuse/clientconfig.ashx?cname=AppInstallTrack&ver="
							+ ConfigPreferences.getInstance().getTrackAppVersion();
            String resultJson = GZipHttpUtil.get(url);
			JSONObject jsonObject = new JSONObject(resultJson);
			int version = jsonObject.getInt("version");
			JSONArray array = jsonObject.getJSONArray("items");
			if(null == array) 
				return;
			for(int i=0; i<array.length(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				String name = object.optString("name");
				String pkgName = object.optString("pkgName");
				if(AndroidPackageUtils.isPkgInstalled(ctx, pkgName)){
					HiAnalytics.submitEvent(ctx, AnalyticsConstant.TRACK_APP_INSTALL, name);
				}
			}
			ConfigPreferences.getInstance().setTrackAppVersion(version);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
