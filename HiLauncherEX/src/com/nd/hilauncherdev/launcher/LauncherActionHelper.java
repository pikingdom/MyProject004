package com.nd.hilauncherdev.launcher;
import android.content.Intent;
import android.graphics.Rect;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.app.AppInfoIntentCommandAdapter;
import com.nd.hilauncherdev.app.CustomIntentSwitcherController;
import com.nd.hilauncherdev.app.IntentCommand;
import com.nd.hilauncherdev.app.SerializableAppInfo;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.ControlledAnalyticsUtil;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.ActivityActionUtil;
import com.nd.hilauncherdev.kitset.util.StatusBarUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.launcher.config.preference.SettingsConstants;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.navigation.SearchActivity;
import com.nd.hilauncherdev.settings.SettingsPreference;

/**
 * 桌面动作集合 <br>
 * Author:ryan <br>
 * Date:2012-5-31上午10:00:09
 */
public class LauncherActionHelper {

	private static LauncherActionHelper mLauncherActionHelper;

	private LauncherActionHelper() {
	}

	public static LauncherActionHelper getInstance() {
		if (mLauncherActionHelper == null) {
			mLauncherActionHelper = new LauncherActionHelper();
		}

		return mLauncherActionHelper;
	}


	public void actionUp(Launcher launcher) {
		// ConfigFactory.alreadyShowReadmeMenu(mLauncher);
		// mLauncher.menuWindow.updateMenu();
		fireGestureAction(SettingsConstants.SETTING_PERSONAL_GESTURE_UP,launcher);
	}

	public void actionDown(Launcher launcher) {
		fireGestureAction(SettingsConstants.SETTING_PERSONAL_GESTURE_DOWN,launcher);
	}

	/**
	 * 匣子中的下滑操作
	 */
	public void actionDownInDrawer(Launcher mLauncher) {
		/**
		 * maolinnan_350804于14.1.13日添加的统计信息
		 */
//		HiAnalytics.submitEvent(mLauncher,AnalyticsConstant.MENU_DOWN_GLIDE_MENU);
		mLauncher.showTopMenu();
	}

	/**
	 * 匣子中的上滑操作
	 */
//	public void actionUpInDrawer() {
//
//	}

	/**
	 * 绑定手势功能
	 * 
	 * @param actionIndex
	 * @param gestureType
	 */
	private void fireGestureAction(String gestureType,Launcher launcher) {
		int actionIndex = -1;
		if (SettingsConstants.SETTING_PERSONAL_GESTURE_DOWN.equals(gestureType)) {
			actionIndex = SettingsPreference.getInstance().getGestureDownFuction();
		} else if (SettingsConstants.SETTING_PERSONAL_GESTURE_UP.equals(gestureType)) {
			actionIndex = SettingsPreference.getInstance().getGestureUpFuction();
		}
		if (actionIndex == -1)
			return;
		if(null != launcher) {
			launcher.hideWidgetWallpaperDrawer();
		}
		switch (actionIndex) {
		case SettingsPreference.GESTURE_MENU_NONE:
			// 无响应
			break;
		case SettingsPreference.GESTURE_MENU_DEFAULT:
			if (SettingsConstants.SETTING_PERSONAL_GESTURE_DOWN.equals(gestureType)) {
				// 显示顶部快捷菜单
				// mLauncher.getTopMenu().updateMenu(y);
				// /**
				// * maolinnan_350804于14.1.13日添加的统计信息
				// */
				// HiAnalytics.submitEvent(mLauncher,
				// AnalyticsConstant.MENU_DROP_DOWN_MENU,"1");
				launcher.showTopMenu();
//				HiAnalytics.submitEvent(launcher,AnalyticsConstant.MENU_DOWN_GLIDE_MENU);
				ControlledAnalyticsUtil.addCommonAnalytics(launcher,AnalyticsConstant.LAUNCHER_MANIPULATE_GESTURE,"xh");
			} else if (SettingsConstants.SETTING_PERSONAL_GESTURE_UP.equals(gestureType)) {
				// 显示菜单
				// ConfigFactory.alreadyShowReadmeMenu(mLauncher);
//				launcher.getMenuWindow().updateMenu();
				//miui上滑显示搜索
				Intent intent = new Intent(launcher, SearchActivity.class);
				SystemUtil.startActivityForResultSafely(Global.getLauncher(), intent, LauncherActivityResultHelper.REQUEST_SEARCH_ACTIVITY_POSITION);
				
				/**
				 * maolinnan_350804于14.1.13日添加的统计信息
				 */
				 HiAnalytics.submitEvent(launcher,AnalyticsConstant.MENU_LAUNCHER_MENU,"sh");
				 ControlledAnalyticsUtil.addCommonAnalytics(launcher,AnalyticsConstant.LAUNCHER_MANIPULATE_GESTURE,"sh");
			}
			break;
		case SettingsPreference.GESTURE_MENU_APPLICATION:// 打开应用程序
		case SettingsPreference.GESTURE_MENU_SYSTEM_SHORT_CUT:// 打开系统快捷方式
			SerializableAppInfo appinfo1 = null;
			if (SettingsConstants.SETTING_PERSONAL_GESTURE_UP.equals(gestureType)) {
				appinfo1 = SettingsPreference.getInstance().getGestureUpApplicationInfo();
			} else if (SettingsConstants.SETTING_PERSONAL_GESTURE_DOWN.equals(gestureType)) {
				appinfo1 = SettingsPreference.getInstance().getGestureDownApplicationInfo();
			}
			if (appinfo1 == null || appinfo1.intent == null)
				return;
			// mLauncher.startActivitySafely(appinfo1.intent, null);
			appinfo1.intent.setSourceBounds(new Rect(10, 10, 10, 10));
			ActivityActionUtil.startActivitySafelyForRecored(launcher, appinfo1.intent);
			break;
		case SettingsPreference.GESTURE_MENU_LAUNCHER_SHORT_CUT:// 打开桌面快捷方式
			SerializableAppInfo appinfo2 = null;
			if (SettingsConstants.SETTING_PERSONAL_GESTURE_UP.equals(gestureType)) {
				appinfo2 = SettingsPreference.getInstance().getGestureUpApplicationInfo();
			} else if (SettingsConstants.SETTING_PERSONAL_GESTURE_DOWN.equals(gestureType)) {
				appinfo2 = SettingsPreference.getInstance().getGestureDownApplicationInfo();
			}
			if (appinfo2 == null)
				return;
			ApplicationInfo launcherShortcut = LauncherMainDockShortcutHelper.getLauncherEditMainDockInfoByAction(launcher, appinfo2.intent.getAction());
			CustomIntentSwitcherController.getNewInstance().registerCustomIntent(new AppInfoIntentCommandAdapter(launcherShortcut));
			CustomIntentSwitcherController.getNewInstance().onAction(launcher, launcherShortcut, IntentCommand.ACTION_FROM_UNKNOW);
			break;
		case SettingsPreference.GESTURE_MENU_SYSTEM_NOTIFICATION_BAR://下滑系统菜单
			StatusBarUtil.expandStatusBar(launcher);
			break;
		default:
			break;
		}
	}
}
