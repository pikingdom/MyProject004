package com.nd.hilauncherdev.plugin;

import java.net.URLEncoder;
import java.util.HashMap;

import android.content.Context;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.Analytics.OtherAnalyticsConstants;
import com.nd.hilauncherdev.kitset.util.CUIDUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.url.URLs;

/**
 * <br>
 * Description: 第三方应用程序常量 <br>
 * Author:caizp <br>
 * Date:2012-8-16下午05:29:56
 */
public class ThirdApplicationAssit {

    /**
	 * 91天气秀
	 */
	public static final String CLOCK_WEATHER_PACKAGE_NAME = "com.nd.android.widget.pandahome.clockweather";

	/**
	 * 91黄历天气包名
	 */
	public static final String CALENDAR_WEATHER_PACKAGE_NAME = "com.calendar.UI";

	/**
	 * 91智能锁包名
	 */
	public static final String SCREEN_LOCK_PACKAGE_NAME = "cn.com.nd.s";
	
	/**
	 * 91智能锁主入口类名
	 */
	public static final String SCREEN_LOCK_MAIN_CLASS_NAME = "cn.com.nd.s.OptionActivity";

	/**
	 * 91智能锁设置界面开启Action
	 */
	public static final String SCREEN_LOCK_SETTING_ACTION = "cn.com.nd.zns.action.SETTING";
	
	/**
	 * 安卓锁屏2.0(百变锁屏)包名
	 */
	public static final String SCREEN_HILOCK_PACKAGE_NAME = "com.nd.android.pandalock";
	
	/**
	 * 安卓锁屏2.0(百变锁屏)主入口类名
	 */
	public static final String SCREEN_HILOCK_MAIN_CLASS_NAME = "com.nd.launcher.component.lock.activity.LockMainActivity";

	/**
	 * 安卓锁屏2.0(百变锁屏)主界面开启Action
	 */
	public static final String SCREEN_HILOCK_SETTING_ACTION = "com.nd.android.pandalock.action.main";

	/**
	 * 正点闹钟包名
	 */
	public static final String ZDCLOCK_PACKAGE_NAME = "com.zdworks.android.zdclock";
	
	/**
	 * 天天动听包名
	 */
	public static final String TTPOD_PACKAGE_NAME = "com.sds.android.ttpod";
	
	/**
	 * 杂志gogogo包名
	 */
	public static final String YOKA_MAGAZINE_PACKAGE_NAME = "com.yoka.magazine";
	
	/**
	 * 网易新闻
	 */
	public final static String NETEASE_NEWS = "com.netease.newsreader.activity";
	
	/**
	 * 搜狐新闻
	 */
	public final static String SOHU_NEWS = "com.example.widgetfornine";
	
	/**
	 * 我查查
	 */
	public final static String WCC = "com.wcc";
	
	/**
	 * 豆果美食
	 */
	public final static String DOUGUO = "com.douguo.recipewidget";
	
	/**
	 * 手机电视
	 */
	public final static String DOPOOL = "dopool.player";
	/**
	 * 便签
	 */
	public final static String NOTE = "com.nd.hilauncherdev.widget.note";
	/**
	 * 倒计时
	 */
	public final static String COUNTDOWN = "com.nd.android.pandahome2.countdown";
	
	/**
	 * 万年历
	 */
	public final static String YOULOFT_WNL = "com.youloft.calendar.wnl";
	
	
	public static HashMap<String, Integer> thirdAppMap = new HashMap<String, Integer>();
	static {
		thirdAppMap.put(ZDCLOCK_PACKAGE_NAME, OtherAnalyticsConstants.FUNC_ID_RES_CONTENT_ZD_CLOCK_CHANNEL_URL);
		thirdAppMap.put(TTPOD_PACKAGE_NAME, OtherAnalyticsConstants.FUNC_ID_RES_CONTENT_TTPOD_CHANNEL_URL);
		thirdAppMap.put(YOKA_MAGAZINE_PACKAGE_NAME, OtherAnalyticsConstants.FUNC_ID_RES_CONTENT_YOKA_MAGAZINE_CHANNEL_URL);
		thirdAppMap.put(SOHU_NEWS, OtherAnalyticsConstants.FUNC_ID_RES_CONTENT_SOUHU_NEWS_CHANNEL_URL);
		thirdAppMap.put(DOUGUO, OtherAnalyticsConstants.FUNC_ID_RES_CONTENT_DOUGUO_CHANNEL_URL);
	}

	/**
	 * <br>
	 * Description: 获取应用程序下载地址 收集手机用户数据：
	 * 固件版本、下载apk包名、手机型号、分辨率、imei号、mac地址、软件版本、上网方式、是否root、语言 <br>
	 * Author:caizp <br>
	 * Date:2012-8-16下午05:36:09
	 * 
	 * @param packageName
	 *            应用程序包名
	 * @return
	 */
	public static String getApkDownloadUrl(Context ctx, String packageName) {
		String url = "";
		// 新增小插件渠道统计下载走新接口 add by caizp 2013-1-21
		if (thirdAppMap.containsKey(packageName)) {
			//下载应用功能ID
			int fid = thirdAppMap.get(packageName);
			//产品ID
			int pid=Integer.parseInt(ctx.getString(R.string.app_pid));
			url = OtherAnalyticsConstants.getDownloadUrlResContentAnalyticsUrl(fid,
					fid, OtherAnalyticsConstants.FORMAT_JSON, ctx, pid, "1",
					OtherAnalyticsConstants.EXT_NAME_APK);
			return url;
		}
		String firmwareVersion = TelephoneUtil.getFirmWareVersion();
		String machineName = URLEncoder.encode(TelephoneUtil.getMachineName());
		String screenResolution = TelephoneUtil.getScreenResolution(ctx);
		String imei = TelephoneUtil.getIMEI(ctx);
		String macAddress = TelephoneUtil.getLocalMacAddress(ctx);
		String rootInfo = TelephoneUtil.hasRootPermission() ? "root" : "no_root";
		// TODO linqiang
		// String NdComAccount =
		// SettingsPreference.getInstance().getSP().getString(SettingsConstants.TEL_91_ACCOUNT,
		// "no_login");
		String NdComAccount = "no_login";
		url = String.format(URLs.THIRD_APPLICATION_DOWNLOAD_URL, firmwareVersion, packageName, machineName, screenResolution, imei, macAddress, TelephoneUtil.getVersionName(ctx),
				TelephoneUtil.getNetworkTypeName(ctx), rootInfo, TelephoneUtil.getLanguage(), NdComAccount);
		// 黄历天气和91智能锁下载另外的渠道包 add by caizp 2012-8-27 (还是走之前的接口)
		if (CALENDAR_WEATHER_PACKAGE_NAME.equals(packageName) || SCREEN_LOCK_PACKAGE_NAME.equals(packageName) || ZDCLOCK_PACKAGE_NAME.equals(packageName)) {
			url += "&source=1";
		}
        url = url + CUIDUtil.getCUIDPART();
		return url;
	}

}
