package com.nd.hilauncherdev.launcher;

import java.io.File;
import java.lang.reflect.Field;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.app.DynamicIconHelper;
import com.nd.hilauncherdev.app.lisener.font.FontRefreshAssist;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.framework.effect.ParticleEffects;
import com.nd.hilauncherdev.framework.effect.finger.MagicControl;
import com.nd.hilauncherdev.framework.view.SplashWindow;
import com.nd.hilauncherdev.framework.view.bubble.LauncherBubbleManager;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.crop.CropImageActivity;
import com.nd.hilauncherdev.kitset.resolver.CenterControl;
import com.nd.hilauncherdev.kitset.systemtoggler.FuelManagerToggleUtil;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.HiLauncherEXUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.kitset.util.WallpaperUtil2;
import com.nd.hilauncherdev.launcher.broadcast.HiBroadcastReceiver;
import com.nd.hilauncherdev.launcher.broadcast.LauncherBroadcastReceiverManager;
import com.nd.hilauncherdev.launcher.broadcast.LauncherBroadcastReceiverManager.LauncherBroadcastReceiverHandler;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.ConfigFactory;
import com.nd.hilauncherdev.launcher.config.preference.BaseSettingsPreference;
import com.nd.hilauncherdev.launcher.config.preference.SettingsConstants;
import com.nd.hilauncherdev.launcher.po.ThemeTag;
import com.nd.hilauncherdev.scene.SceneManager;
import com.nd.hilauncherdev.scene.shop.SceneGlobal;
import com.nd.hilauncherdev.settings.SettingsPreference;
import com.nd.hilauncherdev.settings.assit.BackupSetting;
import com.nd.hilauncherdev.shop.api6.model.ThemeCampaignInfo;
import com.nd.hilauncherdev.shop.util.ThemeShopV6PreferencesUtils;
import com.nd.hilauncherdev.theme.ThemeAssit;
import com.nd.hilauncherdev.theme.ThemeManager;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.theme.ThemeTrialUtil;
import com.nd.hilauncherdev.theme.assit.ThemeUIRefreshAssit;
import com.nd.hilauncherdev.theme.data.BasePandaTheme;
import com.nd.hilauncherdev.theme.data.PandaTheme;
import com.nd.hilauncherdev.theme.data.ThemeGlobal;
import com.nd.hilauncherdev.theme.pref.ThemeSharePref;
import com.nd.hilauncherdev.theme.redownload.ThemeRedownloadHelper;
import com.nd.hilauncherdev.theme.series.ThemeSeriesAssit;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.ThemeShopV6ThemeInstallAPI;
import com.nd.hilauncherdev.webconnect.upgradhint.BeautyUpgradeService;


/**
 * 桌面广播控制器 <br>
 * Author:ryan <br>
 * Date:2012-12-22下午12:06:59
 */
public class LauncherBrocastReceiverController {
	
	/**
	 * 主题指尖特效切换Action
	 */
	public static final String ACTION_THEME_FINGER_EFFECT_CHANGE = "nd.pandahome.internal.theme.finger.effect.change";
	
	/**
	 * 新手引导添加插件Action
	 */
	public static final String ACTION_README_ADD_PLUGIN = "nd.pandahome.internal.readme.add.plugin";

	/**
	 * 91桌面动态壁纸自定义广播
	 * */
	public static final String ACTION_91_LIVE_WALLPAPER = "com.nd.android.pandahome2.livewallpaper";

	/**
	 * 用来接收其他进程，改变壁纸是否滚动
	 * */
	public static  final  String ACTION_FORCED_SET_ROOL_MODE ="com.nd.android.pandahome2.forceRollMode";
	
	/**
	 * 用来接收桌面自升级包RSA验证失败消息
	 * */
	public static  final  String ACTION_PANDAHOME_UPGRADE_RSA_CHECK_FAIL ="com.nd.android.pandahome2.upgrade.rsa.check.fail";
	
	/**
	 * 用来接收桌面主题应用完成消息
	 * */
	public static  final  String ACTION_THEME_APPLY_FINISHED ="com.nd.android.pandahome2.internal.theme.apply.finished";
	
	public static  final  String EXTRA_PARAM_ISROLLING="extra_param_isrolling";

	/**
	 * 用来接收重置开机引导请求
	 * */
	public static  final  String ACTION_REQUEST_RESET_START_GUI ="com.nd.android.pandahome2.internal.reset.start.gui";

	private Dialog themeTrialDialog;
	
	//private Launcher mLauncher;
	private FontChangeReceiver fontReceiver;
	private LauncherUIRefreshReceiver receiver;
	private LauncherBroadcastReceiverHandler mFlashLightStateChangeReceiver;
	private ApplyOldThemeReceiver mApplyOldThemeReceiver;
	private DynamicIconRefreshReceiver mDynamicIconRefreshReceiver;
	private SdcardListener mSdcardListener;
	private BackupAndRestoreReceiver mBackupAndRestoreReceiver;
	private UninstallDyanmicWidgetReceiver mUninstallDyanmicWidgetReceiver;
	private ThemeFingerEffectChangeReceiver mThemeFingerEffectChangeReceiver;
	private ThemeTrialReceiver themeTrialReceiver;
	private ThemeSeriesReceiver themeSeriesReceiver;
	
	private WallpaperBeforeChangeListener mWallpaperBeforeChangeListener;
	private WallpaperChangeListener mWallpaperChangeListener;
	private boolean isOtherAppWallpaperSetting = true; //是否通过其它第三方app设置壁纸
	private boolean wallpaperRolling = false;//是否允许壁纸滚动

	private SceneTopMenuActionReceiver mSceneTopMenuActionReceiver;
	
	private LocaleChangeReceiver localeChangeReceiver;
	
	//91动态自定义壁纸
	private LauncherBroadcastReceiverHandler mLiveWallpaperListener;
//	private PhoneStateListener mPhoneStateListener;
//	private TelephonyManager mTelephonyManager;
	
	private LauncherBroadcastReceiverManager receiverManager;
	
	private static LauncherBrocastReceiverController mLauncherBrocastReceiverController;

	ForceRollModeReceiver forceRollModeReceiver;
	
	PandaHomeUpgradeRSACheckFailReceiver pandaHomeUpgradeRSACheckFailReceiver;
	
	ThemeApplyFinishedReceiver themeApplyFinishedReceiver;

	ResetStartGuiReceiver resetStartGuiReceiver;

	private LauncherBrocastReceiverController() {
		
		//mLauncher = launcher;
		receiverManager = LauncherBroadcastReceiverManager.getInstance();
		//Log.e("zhou", "LauncherBrocastlauncher="+launcher +" receiverManager="+receiverManager);
	}
	
	public static LauncherBrocastReceiverController getInstance(){
		if(mLauncherBrocastReceiverController == null){
			mLauncherBrocastReceiverController = new LauncherBrocastReceiverController();
		}
		return mLauncherBrocastReceiverController;
	}

//	public static void release() {
//		mLauncherBrocastReceiverController = null;
//	}
	/**
	 * 注册桌面相关监听 
	 * <br>Author:ryan
	 * <br>Date:2012-12-22下午01:56:51
	 */
	public void register() {
		registerFontReveiver();
		registerFlashLight();
		registerLauncherUIRefreshReceiver();
		registerApplyOldThemeReceiver();
		registerSDCardListener();
		registerWallpaperEditListener();
		registerWallpaperChangeListener();
		registerDynamicIconRefreshReceiver();
//		registerCallStateListener();
		registerPrivacyLockScreen();
		registerBackupAndRestoreReceiver();
		registerSceneTopMenuActionReceiver();
		registerLocaleChange();
		registerUninstallListener();
		registerThemeFingerEffectChangeReceiver();
		regsiterLivewallpaper();
		registerThemeTrialReceiver();
		registerThemeSeriesReceiver();
		registerForceRollMode();
		registerPandaHomeUpgradeRSACheckFailReceiver();
		registerThemeApplyFinishedReceiver();
		registerResetStartGuiReceiver();
	}

	private void registerUninstallListener() {
		mUninstallDyanmicWidgetReceiver = new UninstallDyanmicWidgetReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BeautyUpgradeService.ACTION_REFRESH_WIDGETS_UNINSTALL);
		receiverManager.registerReceiver(mUninstallDyanmicWidgetReceiver, intentFilter);
	}
	
	private void registerThemeFingerEffectChangeReceiver() {
		mThemeFingerEffectChangeReceiver = new ThemeFingerEffectChangeReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_THEME_FINGER_EFFECT_CHANGE);
		receiverManager.registerReceiver(mThemeFingerEffectChangeReceiver, intentFilter);
	}

	/**
	 * 注销桌面相关监听 
	 * <br>Author:ryan
	 * <br>Date:2012-12-22下午01:57:12
	 */
	public void unRegister() {
		receiverManager.unregisterReceiver(fontReceiver);
		receiverManager.unregisterReceiver(receiver);
		receiverManager.unregisterReceiver(mApplyOldThemeReceiver);
		receiverManager.unregisterReceiver(mFlashLightStateChangeReceiver);
		receiverManager.unregisterReceiver(mSdcardListener);
		receiverManager.unregisterReceiver(mWallpaperBeforeChangeListener);
		receiverManager.unregisterReceiver(mWallpaperChangeListener);
		receiverManager.unregisterReceiver(mDynamicIconRefreshReceiver);
//		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		unRegisterPrivacyLockScreen();
		
		receiverManager.unregisterReceiver(mBackupAndRestoreReceiver);
		receiverManager.unregisterReceiver( mSceneTopMenuActionReceiver );
		receiverManager.unregisterReceiver(localeChangeReceiver);
		receiverManager.unregisterReceiver(mUninstallDyanmicWidgetReceiver);
		receiverManager.unregisterReceiver(mThemeFingerEffectChangeReceiver);
		receiverManager.unregisterReceiver(mLiveWallpaperListener);
		receiverManager.unregisterReceiver(themeTrialReceiver);
		receiverManager.unregisterReceiver(themeSeriesReceiver);
		receiverManager.unregisterReceiver(forceRollModeReceiver);
		receiverManager.unregisterReceiver(pandaHomeUpgradeRSACheckFailReceiver);
		receiverManager.unregisterReceiver(themeApplyFinishedReceiver);
		receiverManager.unregisterReceiver(resetStartGuiReceiver);
	}
	
	private LauncherBroadcastReceiverHandler mScreenOffReciver;
	
	private void registerPrivacyLockScreen() {
		IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_OFF");
		if (this.mScreenOffReciver == null)
			mScreenOffReciver = new ScreenOffReciver();
		
		receiverManager.registerReceiver(mScreenOffReciver, filter);
	}
	
	private void unRegisterPrivacyLockScreen() {
		try {
			if (mScreenOffReciver != null)
				receiverManager.unregisterReceiver(mScreenOffReciver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class ScreenOffReciver implements LauncherBroadcastReceiverHandler {
		public void onReceive(Context context, Intent intent) {
			try {
				CenterControl.start91Launcher(context);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Launcher mLauncher=Global.getLauncher();
			if (mLauncher == null) {
				return;
			}
			if(-1 != mLauncher.getSharedPreferences("private_zone_config", Context.MODE_PRIVATE).getLong("key_cur_mode_owner_id", -1) && 
					mLauncher.getSharedPreferences("private_zone_config", Context.MODE_PRIVATE).getBoolean("settings_enable_pwd_validate", false)
					){
				Intent i = new Intent();
				i.setClassName(mLauncher, "com.nd.hilauncherdev.privatezone.LockScreenActivity");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				SystemUtil.startActivitySafely(mLauncher, i);
			}
		}
	}
	
	/**
	 * 注册编辑图片后设置为壁纸监听
	 * <br>Author:ryan
	 * <br>Date:2012-12-22下午01:55:13
	 */
	private void registerWallpaperEditListener() {
		mWallpaperBeforeChangeListener = new WallpaperBeforeChangeListener();
		IntentFilter intentFilter = new IntentFilter(CropImageActivity.WALLPAPER_BEFORE_CHANGE_INTENT);
		receiverManager.registerReceiver(mWallpaperBeforeChangeListener, intentFilter);
	}

	/**
	 * 注册壁纸被更换监听
	 */
	private void registerWallpaperChangeListener() {
		mWallpaperChangeListener = new WallpaperChangeListener();
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED);
		receiverManager.registerReceiver(mWallpaperChangeListener, intentFilter);
	}
	
	/**
	 * 注册sdcard装载监听
	 */
	private void registerSDCardListener() {
		mSdcardListener = new SdcardListener();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addDataScheme("file");
		
		receiverManager.registerReceiver(mSdcardListener, intentFilter);
	}
	
	/**
	 * 注册电话状态监听
	 */
//	private void registerCallStateListener() {
//		mPhoneStateListener = new PhoneStateListener(){
//			@Override
//			public void onCallStateChanged(int state, String incomingNumber) {
//				if(mLauncher != null && mLauncher.getNavigationView() != null &&
//					SettingsPreference.getInstance().isShowNavigationView() && state == TelephonyManager.CALL_STATE_IDLE){//挂断时
//					mLauncher.getNavigationView().refreshContactOnToolsGridView();
//				}
//			}
//		};
//		mTelephonyManager = (TelephonyManager)mLauncher.getSystemService(Service.TELEPHONY_SERVICE);   
//		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
//	}
	
	/**
	 * 注册旧主题安装
	 * <br>Author:ryan
	 * <br>Date:2012-12-22下午01:53:03
	 */
	private void registerApplyOldThemeReceiver() {
		mApplyOldThemeReceiver = new ApplyOldThemeReceiver();
		IntentFilter filter = new IntentFilter(HiBroadcastReceiver.APPLY_OLD_THEME_ACTION);
		receiverManager.registerReceiver(mApplyOldThemeReceiver, filter);
	}

	/**
	 * 闪关灯与字体切换
	 * <br>Author:ryan
	 * <br>Date:2012-12-22下午01:46:13
	 */
	private void registerFlashLight() {
		mFlashLightStateChangeReceiver = new FlashLightStateChangeReceiver();
		IntentFilter filter = new IntentFilter(FuelManagerToggleUtil.WIDGET_FLASHLIGHT);
		receiverManager.registerReceiver(mFlashLightStateChangeReceiver, filter);
	}
	
	/**
	 * 注册字体广播
	 * @Author LiuYun
	 * @data: 2013-8-23 下午4:33:21
	 */
	private void registerFontReveiver() {
		fontReceiver = new FontChangeReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(SettingsConstants.ACTION_MODIFY_APP_NAME_FONT_STYLE);
		filter.addAction(SettingsConstants.ACTION_REFRESH_APP_NAME);
		receiverManager.registerReceiver(fontReceiver, filter);
	}

	/**
	 * Description: 注册桌面UI刷新控制器 <br>
	 * Author:caizp <br>
	 * Date:2012-7-14下午03:24:18
	 */
	private void registerLauncherUIRefreshReceiver() {
		receiver = new LauncherUIRefreshReceiver();
		IntentFilter filter = new IntentFilter(ThemeGlobal.LAUNCHER_UI_REFRESH_ACTION);
		receiverManager.registerReceiver(receiver, filter);
	}
	
	/**
	 * 注册动态图标刷新广播
	 */
	private void registerDynamicIconRefreshReceiver() {
		mDynamicIconRefreshReceiver = new DynamicIconRefreshReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_DATE_CHANGED);
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		receiverManager.registerReceiver(mDynamicIconRefreshReceiver, filter);
	}

	
	/**
	 * 注册桌面备份或还原广播
	 */
	private void registerBackupAndRestoreReceiver() {
		mBackupAndRestoreReceiver = new BackupAndRestoreReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_CALL_BACKUP);
		filter.addAction(ACTION_CALL_RESTORE);
		receiverManager.registerReceiver(mBackupAndRestoreReceiver, filter);
	}
	
	
	/**
	 * 注册语言变换监听 
	 * <br>Author:wangguomei
	 * <br>Date:2013-11-25下午
	 */
	public void registerLocaleChange() {
		localeChangeReceiver = new LocaleChangeReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_LOCALE_CHANGED);
		receiverManager.registerReceiver(localeChangeReceiver, filter);
	}
	
	/**
	 * Title: 桌面刷新广播接收器 <br>
	 * Author:caizp <br>
	 * Date:2012-7-14下午03:18:06
	 */
	class LauncherUIRefreshReceiver implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(Context ctx, Intent intent) {
			if (null == intent)
				return;
			Launcher mLauncher=Global.getLauncher();
			if (mLauncher == null) {
				return;
			}
			if(!intent.getBooleanExtra("applyScene", false)){//是否在应用情景
				//刷新DOCK栏
				mLauncher.showOrHideDockBarText();
				//壁纸可滚动
				if (intent.getBooleanExtra("changeRolling", true)) {
					ConfigFactory.setWallpaperRolling(ctx, true);
					CropImageActivity.delCurrentWallpaperTempFile(mLauncher);
				}

			}
			ThemeUIRefreshAssit.getInstance().refreshLauncherThemeUI();
 			mLauncher.getWorkspace().resetWallpaperOffset();//适配4.3以上固件可能存在的壁纸显示不完全情况
 			
 			LauncherBubbleManager.getInstance().dimissAllBubbles();
			// 编辑模式
			mLauncher.delayRefreshWorkspaceSpringScreen(500);
		}
	}

	private class FlashLightStateChangeReceiver implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(FuelManagerToggleUtil.WIDGET_FLASHLIGHT)) {
				FuelManagerToggleUtil.isOpen = intent.getBooleanExtra("flashToggle", false);
			}
		}
	}
	
	/**
	 * 字体修改广播
	 * @Author LiuYun
	 * @data: 2013-8-23 下午4:31:34
	 */
	private class FontChangeReceiver implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (null == intent)
				return;
			Launcher mLauncher=Global.getLauncher();
			if (mLauncher == null) {
				return;
			}
			String action = intent.getAction();
			if (action.equals(SettingsConstants.ACTION_MODIFY_APP_NAME_FONT_STYLE)) {
				//修改默认字体 @liuyun 2013年8月23日16:31:56
				String fontStylePath = intent.getStringExtra("fontStylePath");
				SettingsPreference.getInstance().setFontStyle(fontStylePath);
				//刷新设置的字体@liuyun 2013年8月23日16:32:26
				intent.setAction(SettingsConstants.ACTION_REFRESH_APP_NAME);
				mLauncher.sendBroadcast(intent);
			} else if (intent.getAction().equals(SettingsConstants.ACTION_REFRESH_APP_NAME)) {
				//修改字体@liuyun 2013年8月23日16:32:42
				mLauncher.getMenuWindow().fontChange();
			}
			FontRefreshAssist.getInstance().refresh();
		}
	}

	/**
	 * sdcard装载监听
	 */
	private class SdcardListener implements LauncherBroadcastReceiverHandler {
		private boolean isSdcardExit = true;

		SdcardListener() {
			isSdcardExit = TelephoneUtil.isSdcardExist();
		}

		@Override
		public void onReceive(final Context context, Intent intent) {
			String action = intent.getAction();
			Launcher mLauncher=Global.getLauncher();
			if (mLauncher == null) {
				return;
			}
			if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
				isSdcardExit = false;
				if(Global.isOnScene()){//卸载sd卡时，退出情景桌面
					SceneManager.getInstance(context).applyDefaultScene();
				}
			} else if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
				if (isSdcardExit)
					return;

				isSdcardExit = true;
				Context mContext = Global.getApplicationContext();
				
				if(Global.getLauncher() != null && Global.getLauncher().getNavigationView() != null){//sd卡加载完成尝试刷0屏
					Global.getLauncher().getNavigationView().tryInitViewFromPlugin();
				}
				
				if(!SceneManager.getInstance(mContext).isOnDefaultScene()){//如果是情景桌面，则桌面重启			
					HiLauncherEXUtil.restartDesktop(mContext);
					return;
				}
				
				if (ThemeSharePref.getInstance(mContext).isDefaultTheme())
					return;

				ThemeManager.applyThemeNoWallpaperWithWaitDialog(mLauncher, ThemeSharePref.getInstance(mContext).getCurrentThemeId(), true, false);
			}
		}
	};

	/**
	 * 应用旧主题请求广播接收器 <br>
	 * Author:caizp <br>
	 * Date:2012-7-14下午03:18:06
	 */
	private class ApplyOldThemeReceiver implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(Context ctx, Intent intent) {
			if (null == intent)
				return;
			Launcher launcher=Global.getLauncher();
			if (launcher == null) {
				return;
			}
			launcher.handler.post(new Runnable() {
				@Override
				public void run() {
					Launcher mLauncher=Global.getLauncher();
					if (mLauncher == null) {
						return;
					}
					String oldThemeId = ThemeManager.getOldThemeId(mLauncher);
					ThemeManager.applyThemeWithWaitDialog(mLauncher, oldThemeId);
				}
			});
		}
	}

	/**
	 * 监听编辑图片后设置为壁纸(当用户通过91桌面来设置壁纸)
	 * <br>Author:ryan
	 * <br>Date:2012-12-22下午01:44:00
	 */
	private class WallpaperBeforeChangeListener implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(final Context context, Intent intent) {
			isOtherAppWallpaperSetting = false;
			Launcher mLauncher=Global.getLauncher();
			if (mLauncher == null) {
				return;
			}
			Bundle extras = intent.getExtras();
			if(extras != null){
				wallpaperRolling = extras.getBoolean(CropImageActivity.WALLPAPER_INTENT_EXTRA_ISROLLING, true);
				boolean isLiveWp = extras.getBoolean(CropImageActivity.WALLPAPER_INTENT_EXTRA_ISLIVEWP,false);
				if (isLiveWp) {
					mLauncher.mWorkspace.resetWallpaperOffset();
				}
				ConfigFactory.setWallpaperRolling(context, wallpaperRolling);
				WallpaperUtil2.setDesiredDimensions(context);
				mLauncher.delayRefreshWorkspaceSpringScreen(300);
			}
		}
	};
	
	/**
	 * 监听系统壁纸被更换
	 */
	private class WallpaperChangeListener implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(final Context context, Intent intent) {
			Launcher mLauncher=Global.getLauncher();
			if (mLauncher == null) {
				return;
			}
			//打点统计壁纸被更换时是否正在使用默认主题
			if(ThemeSharePref.getInstance(context).isDefaultTheme()) {
				HiAnalytics.submitEvent(context, AnalyticsConstant.USE_THEME_WHEN_WALLPAPER_CHANGE, "y");
			} else {
				HiAnalytics.submitEvent(context, AnalyticsConstant.USE_THEME_WHEN_WALLPAPER_CHANGE, "n");
			}
			mLauncher.getWallpaperHelper().resetWallPaper();
			if(!isOtherAppWallpaperSetting){//当用户通过91桌面来设置壁纸
				ConfigFactory.setWallpaperRolling(context, wallpaperRolling);
				mLauncher.mWorkspace.resetWallpaperOffset();
				isOtherAppWallpaperSetting = true;
			}else{//当用户使用其它第三方应用来设置壁纸
				CropImageActivity.delCurrentWallpaperTempFile(mLauncher);
			}
			BaseSettingsPreference.getInstance().setNewSingleScreenMode(1);
			LauncherAnimationHelp.onWallPaperChange();
		}
	};
	
	/**
	 * 动态图标刷新广播
	 */
	private class DynamicIconRefreshReceiver implements LauncherBroadcastReceiverHandler {
		private static final String TAG = "DynamicIconRefreshReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e(TAG, "DynamicIconRefreshReceiver.onReceive");
			Launcher mLauncher=Global.getLauncher();
			if (mLauncher == null) {
				return;
			}
			if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED) || intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
				if(!LauncherBranchController.isFanYue()) {
					;
					// 刷新日历动态广播
					DynamicIconHelper.getInstance().updateCalendarDynamicIcon(mLauncher);
				}
				ThemeManager.processThemeSeriesAutoSwitch(mLauncher, mLauncher.handler);
//				Log.e(TAG, intent.getAction());
			} else if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
				// 刷新我的电池动态广播
				DynamicIconHelper.getInstance().updateMyBatteryDynamicIcon(mLauncher, intent);
//				Log.e(TAG, intent.getAction());
				//电池电量改变的时候同时刷新一键清理的内存数值
//				context.sendBroadcast(new Intent(Cleaner2x1.REFRESH_CLEANERS));
			}
		}
	}
	
	private class SceneTopMenuActionReceiver implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(Context context, Intent intent) {
			Launcher mLauncher=Global.getLauncher();
			if (mLauncher == null) {
				return;
			}
			mLauncher.getTopSceneManagerMenu().forceDismiss();
		}
	}
	
	private void registerSceneTopMenuActionReceiver() {
		if ( mSceneTopMenuActionReceiver == null ) {
			mSceneTopMenuActionReceiver = new SceneTopMenuActionReceiver();
		}
		
		IntentFilter filter = new IntentFilter( SceneGlobal.SCENE_TOP_MENU_DISMISS );
		receiverManager.registerReceiver( mSceneTopMenuActionReceiver, filter );
	}
	
	
	//备份广播
	private static final String ACTION_CALL_BACKUP = "action_myphone_mybackup_CALL_BACKUP";
	//备份广播返回  
	private static final String ACTION_CALL_BACKUP_RETURN = "action_myphone_mybackup_CALL_BACKUP_RETURN";
	//还原广播
	private static final String ACTION_CALL_RESTORE = "action_myphone_mybackup_CALL_RESTORE";
	//还原广播返回
	private static final String ACTION_CALL_RESTORE_RETURN = "action_myphone_mybackup_CALL_RESTORE_RETURN";
	/**
	 * 通知桌面备份或还原
	 * @author linqiang(866116)
	 * @Since 2013-7-23
	 */
	private class BackupAndRestoreReceiver implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getExtras() != null){
				String backupPath = intent.getExtras().getString("backupPath");
				if (intent.getAction().equals(ACTION_CALL_BACKUP)) {
					String ret = BackupSetting.backupData(context, backupPath, true);
					Intent retrunIntent = new Intent();
					retrunIntent.setAction(ACTION_CALL_BACKUP_RETURN);
					retrunIntent.putExtra("ret", ret);
		            context.sendBroadcast(retrunIntent);
				} else if (intent.getAction().equals(ACTION_CALL_RESTORE)) {
					String ret = BackupSetting.restoreData(context, backupPath);
					Log.d("BackupAndRestoreReceiver", ret);
					Intent retrunIntent = new Intent();
					retrunIntent.setAction(ACTION_CALL_RESTORE_RETURN);
					retrunIntent.putExtra("ret", ret);
		            context.sendBroadcast(retrunIntent);
					if(context.getString(R.string.settings_backup_restore_success).equals(ret)){
						HiLauncherEXUtil.restartDesktop(context);
					}
				}
			}
		}
	}
	
	
	/**
	 * 注册语言变换广播接收
	 * <br>Author:wangguomei
	 * <br>Date:2013-11-25下午
	 */
	private class LocaleChangeReceiver implements LauncherBroadcastReceiverHandler {

		public void onReceive(final Context context, Intent intent) {
			
			if (!intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
				return;
			}
			ThreadUtil.executeMore(new Runnable() {
				@Override
				public void run() {
					new LauncherInternational(context.getApplicationContext()).localeChange();
				}
			});
		}
	}
	
	
	/**
	 * 卸载动态插件广播监听
	 * 
	 * @ClassName: UninstallDyanmicWidgetReceiver
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author lytjackson@gmail.com
	 * @date 2014-2-18
	 * 
	 */
	private class UninstallDyanmicWidgetReceiver implements LauncherBroadcastReceiverHandler {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			if (!BeautyUpgradeService.ACTION_REFRESH_WIDGETS_UNINSTALL.equals(intent.getAction())) {
				return;
			}
			uninstallDyanmicWidget(ctx, intent);
		}

		private boolean uninstallDyanmicWidget(Context context, Intent data) {
			if (null == data) {
				return false;
			}
			Launcher mLauncher=Global.getLauncher();
			if (mLauncher == null) {
				return false;
			}
			String pkgName = data.getStringExtra("pkg");
			if(StringUtil.isEmpty(pkgName)){
				return false;
			}
			
			if(mLauncher != null){				
				LauncherPandaWidgetHelper.removePandaWidget(mLauncher.pandaWidgets, pkgName, mLauncher.mWorkspace, mLauncher);
			}
			return false;
		}

	}
	
	/**
	 * <br>Description: 主题指尖特效切换监听器
	 * <br>Author:caizp
	 * <br>Date:2014-3-5上午11:06:35
	 */
	private class ThemeFingerEffectChangeReceiver implements LauncherBroadcastReceiverHandler {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			if(MagicControl.getOwn().changeMagicForTheme((PandaTheme)ThemeManagerFactory.getInstance().getCurrentTheme(), false)){
				SettingsPreference.getInstance().setParticleEffects(-1);
				SettingsPreference.getInstance().setParticleEffectsThemeId(ThemeManagerFactory.getInstance().getCurrentTheme().getThemeId());
			}else{//切换为主题指尖特效失败
				// 切换失败则切换至默认特效(无)
				if (!ThemeGlobal.DEFAULT_THEME_ID.equals(SettingsPreference.getInstance().getParticleEffectsThemeId())) {
					MagicControl.getOwn().changeMagic(ParticleEffects.DEFAULT);
					SettingsPreference.getInstance().setParticleEffects(ParticleEffects.DEFAULT);
					SettingsPreference.getInstance().setParticleEffectsThemeId(ThemeGlobal.DEFAULT_THEME_ID);
				}
			}
		}

	}

	private void regsiterLivewallpaper() {
		mLiveWallpaperListener = new LauncherBroadcastReceiverHandler() {
			@Override
			public void onReceive(Context context, Intent intent) {
				LauncherAnimationHelp.onWallPaperChange();
			}
		};
		IntentFilter intentFilter = new IntentFilter(ACTION_91_LIVE_WALLPAPER);
		receiverManager.registerReceiver(mLiveWallpaperListener, intentFilter);
	}
	
	private class ThemeTrialReceiver implements LauncherBroadcastReceiverHandler {

		@Override
		public void onReceive(final Context ctx, Intent intent) {
			SharedPreferences sp = ThemeTrialUtil.getTrialSP(ctx);
			final String newThemeId = sp.getString(ThemeTrialUtil.EXTRA_TRIAL_THEME_NEW_ID, "");

			// 情况一：如果购买成功，取消对话框和闹钟
			String buyThemeID = null;
			if(intent != null && ThemeShopV6ThemeInstallAPI.THEME_APT_INSTALL_RESULT.equals(intent.getAction()))
					buyThemeID = intent.getStringExtra(ThemeShopV6ThemeInstallAPI.THEME_PARAMETER_THEME_ID);
			if(newThemeId.equals(buyThemeID)){
				ThemeTrialUtil.cancelThemeTrialAlarm(ctx);
				sp.edit().remove(ThemeTrialUtil.EXTRA_TRIAL_THEME_PRICE + newThemeId).commit();// 删除试用主题的猫爪
				if (null != themeTrialDialog) {
					reflectDialogShowing(themeTrialDialog, true);
					themeTrialDialog.dismiss();
					themeTrialDialog = null;
				}
				return ;
			}
			// 情况二：重复试用主题，只需要去掉存在的对话框
			if(intent != null && ThemeTrialUtil.ACTION_THEME_TRIAL_EXPIRE.equals(intent.getAction())
					&& ThemeTrialUtil.COMMAND_DISMISS_DIALOG.equals(intent.getStringExtra(ThemeTrialUtil.EXTRA_COMMAND))){
				if (null != themeTrialDialog) {
					reflectDialogShowing(themeTrialDialog, true);
					themeTrialDialog.dismiss();
					themeTrialDialog = null;
				}
				return ;
			}
			
			final Handler handler = new Handler();
			
			final String oldThemeId = sp.getString(ThemeTrialUtil.EXTRA_TRIAL_THEME_OLD_ID, "");
			
			final String currentThemeId = ThemeManagerFactory.getInstance().getCurrentTheme().getThemeId();
			
			if(null != currentThemeId && !currentThemeId.equals(newThemeId)) {
				return;
			}
			String price = sp.getString(ThemeTrialUtil.EXTRA_TRIAL_THEME_PRICE + newThemeId, "");
			String buyBtnText = ctx.getString(R.string.theme_shop_v2_buy_now);
			price = price.trim();
			if(!"".equals(price) && currentThemeId != null && currentThemeId.equals(newThemeId))
				buyBtnText = String.format(ctx.getString(R.string.theme_shop_v6_trial_maozhuas_to_pay), price);
			
			String themeName = ThemeManagerFactory.getInstance().getCurrentTheme().getThemeName();
			
			final BasePandaTheme newTheme = ThemeManagerFactory.getInstance().getThemeById(newThemeId);			
			
			android.content.DialogInterface.OnClickListener buy = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 购买
					new SplashWindow(ctx, ctx.getString(R.string.theme_shop_v6_trial_jumping), 
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									ThemeRedownloadHelper.getInstance().reDownloadSingleTheme(ctx, handler, newThemeId, newTheme.getResType(), newTheme.isSupportV6(), true);
								}
						});
					reflectDialogShowing(dialog, false);
					
					HiAnalytics.submitEvent(ctx, AnalyticsConstant.LAUNCHER_THEME_SHOP_TRYOUT_EXPIRE_DIALOG, "gm");//对话框，购买 
				}
			};
			// 按钮
			android.content.DialogInterface.OnClickListener useOldTheme = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					ThemeTrialUtil.cancelThemeTrialAlarm(ctx);						
					
					// 使用原来主题 oldThemeId
					String themeId = oldThemeId; 
					
					if(null == themeId || !ThemeManagerFactory.getInstance().isThemeIdLikeExist(ctx, themeId))// 为空，或者主题已经被删掉,或是试用主题
						themeId = ThemeGlobal.DEFAULT_THEME_ID;
					else {
						final BasePandaTheme oldTheme = ThemeManagerFactory.getInstance().getThemeById(oldThemeId); 
						if(null == oldTheme || ThemeTrialUtil.isLocalThemeForTrial(oldTheme))
							themeId = ThemeGlobal.DEFAULT_THEME_ID;
					}
					
					ThemeAssit.showThemeApplyWindow(ctx, handler, false, themeId, true);
					
					reflectDialogShowing(dialog, true);
					dialog.dismiss();
					
					HiAnalytics.submitEvent(ctx, AnalyticsConstant.LAUNCHER_THEME_SHOP_TRYOUT_EXPIRE_DIALOG, "yy");//对话框，应用回旧主题
				}
			};
			if(null != themeTrialDialog){
				reflectDialogShowing(themeTrialDialog, true);
				themeTrialDialog.dismiss();
				themeTrialDialog = null;
			}
			themeTrialDialog = ViewFactory.getAlertDialog(ctx, -1, ctx.getString(R.string.theme_shop_v6_trial_expire_title), String.format(ctx.getString(R.string.theme_shop_v6_trial_welcome_buy),themeName) , ctx.getString(R.string.theme_shop_v6_trial_use_old_theme), buyBtnText, 
				useOldTheme, buy);
			themeTrialDialog.setCanceledOnTouchOutside(false);
			themeTrialDialog.setCancelable(false);
			
			themeTrialDialog.show();		
		}
	}
	
	private class ThemeSeriesReceiver implements LauncherBroadcastReceiverHandler {

		@Override
		public void onReceive(final Context ctx, Intent intent) {
			if(null == ctx || null == intent || null == intent.getAction())return;
			if(ThemeSeriesAssit.ACTION_APPLY_THEME_SERIES.equals(intent.getAction())) {
				Launcher launcher = Global.getLauncher();
				if (launcher == null) {
					return;
				}
				ThemeManager.processThemeSeriesApplyHint(launcher, launcher.handler);
			}
		}
	}
	
	/**
	 * 反射,设置dialog是否正在显示。在dismiss之前，要设置为true，才能dismiss
	 * @param dialog
	 * @param showing
	 */
	private void reflectDialogShowing(DialogInterface dialog, boolean showing){
		if(null == dialog)
			return;
		try {			
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			// 将mShowing变量设为false，表示对话框已关闭
			field.set(dialog, showing);
			dialog.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注册主题试用接收器
	 */
	private void registerThemeTrialReceiver(){
		themeTrialReceiver = new ThemeTrialReceiver();
		IntentFilter intentFilter = new IntentFilter(ThemeTrialUtil.ACTION_THEME_TRIAL_EXPIRE);
		intentFilter.addAction(ThemeShopV6ThemeInstallAPI.THEME_APT_INSTALL_RESULT);
		receiverManager.registerReceiver(themeTrialReceiver, intentFilter);
	}	
	
	private void registerThemeSeriesReceiver() {
		themeSeriesReceiver = new ThemeSeriesReceiver();
		IntentFilter intentFilter = new IntentFilter(ThemeSeriesAssit.ACTION_APPLY_THEME_SERIES);
		receiverManager.registerReceiver(themeSeriesReceiver, intentFilter);
	}

	/**
	 * 注册强制设置壁纸滚动的广播
	 *
	 * */
	private void registerForceRollMode() {
		forceRollModeReceiver= new ForceRollModeReceiver();
		IntentFilter intentFilter = new IntentFilter(ACTION_FORCED_SET_ROOL_MODE);
		receiverManager.registerReceiver(forceRollModeReceiver,intentFilter);
	}
	private class ForceRollModeReceiver implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(final Context ctx, Intent intent) {
			if(null == ctx || null == intent || null == intent.getAction())return;
			boolean isRoll=intent.getBooleanExtra(EXTRA_PARAM_ISROLLING,true);
			ConfigFactory.setWallpaperRolling(ctx,isRoll);
		}
	}
	
	/**
	 * 注册桌面自升级包RSA验证失败广播
	 */
	private void registerPandaHomeUpgradeRSACheckFailReceiver() {
		pandaHomeUpgradeRSACheckFailReceiver= new PandaHomeUpgradeRSACheckFailReceiver();
		IntentFilter intentFilter = new IntentFilter(ACTION_PANDAHOME_UPGRADE_RSA_CHECK_FAIL);
		receiverManager.registerReceiver(pandaHomeUpgradeRSACheckFailReceiver,intentFilter);
	}
	
	private class PandaHomeUpgradeRSACheckFailReceiver implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(final Context ctx, Intent intent) {
			if(null == ctx || null == intent || null == intent.getAction())return;
			final String identifier = intent.getStringExtra("identifier");
			if(TextUtils.isEmpty(identifier)) return;
			Toast.makeText(ctx, R.string.soft_update_rsa_check_fail_tip, Toast.LENGTH_LONG).show();
			ThreadUtil.executeMore(new Runnable() {
				@Override
				public void run() {//清除下载记录
					DownloadManager.getInstance().cancelNormalTask(identifier, null);
				}
			});
		}
	}
	
	/**
	 * 注册桌面主题应用成功广播接收
	 */
	private void registerThemeApplyFinishedReceiver() {
		themeApplyFinishedReceiver= new ThemeApplyFinishedReceiver();
		IntentFilter intentFilter = new IntentFilter(ACTION_THEME_APPLY_FINISHED);
		receiverManager.registerReceiver(themeApplyFinishedReceiver,intentFilter);
	}
	
	private class ThemeApplyFinishedReceiver implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(final Context ctx, Intent intent) {
			if(null == ctx || null == intent || null == intent.getAction())return;
			String themeId = intent.getStringExtra("themeId");
			if(TextUtils.isEmpty(themeId)) return;
			handleThemeCampaignPaster(Global.getLauncher(), themeId);
			if(null != Global.getLauncher() && ThemeShopV6PreferencesUtils.isNeedShowThemeMenu(ctx)) {
				Global.getLauncher().showThemeMenu();
				ThemeShopV6PreferencesUtils.setNeedShowThemeMenu(ctx, false);
			}
		}
	}

	/**
	 * 注册重置开机引导广播接收
	 */
	private void registerResetStartGuiReceiver() {
		resetStartGuiReceiver= new ResetStartGuiReceiver();
		IntentFilter intentFilter = new IntentFilter(ACTION_REQUEST_RESET_START_GUI);
		receiverManager.registerReceiver(resetStartGuiReceiver,intentFilter);
	}

	private class ResetStartGuiReceiver implements LauncherBroadcastReceiverHandler {
		@Override
		public void onReceive(final Context ctx, Intent intent) {
			if(null == ctx || null == intent || null == intent.getAction())return;
			CustomChannelController.resetStartGui(ctx);
			Toast.makeText(ctx, R.string.message_reset_start_gui, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * <br>Description: 处理主题活动关联的桌面贴纸
	 * <br>Author:caizp
	 * <br>Date:2016年1月18日下午3:10:56
	 * @param themeId
	 */
	private void handleThemeCampaignPaster(Launcher launcher, String themeId) {
		if(null == launcher) return;
		// 判断当前主题是否活动主题
		String campaignInfoFile = BaseConfig.THEME_DIR + themeId.replace(" ", "_") + "/theme_campaign_info";
		if(new File(campaignInfoFile).exists()) {
			try {
				ThemeCampaignInfo campaignInfo = (ThemeCampaignInfo)FileUtil.readObjectFromFile(campaignInfoFile);
				if(null == campaignInfo || TextUtils.isEmpty(campaignInfo.pasterDownloadUrl)) {
					launcher.getTagViewController().addThemeTag2Launcher(null);
					return;
				}
				ThemeTag themeTag = new ThemeTag();
				themeTag.action = campaignInfo.pasterDetailUrl;
				themeTag.tagKey = "" + campaignInfo.pasterDownloadUrl.hashCode();
				launcher.getTagViewController().addThemeTag2Launcher(themeTag);
				HiAnalytics.submitEvent(launcher, AnalyticsConstant.LAUNCHER_THEME_SHOP_CAMPAIGN_THEME, "tjtz"+ThemeManagerFactory.getInstance().getCurrentTheme().getThemeName());
			} catch (Exception e) {
				e.printStackTrace();
				launcher.getTagViewController().addThemeTag2Launcher(null);
			}
		} else {
			launcher.getTagViewController().addThemeTag2Launcher(null);
		}
	}

	
}
