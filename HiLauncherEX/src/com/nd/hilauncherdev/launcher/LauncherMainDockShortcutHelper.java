package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.content.res.Resources;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.pandahome2.LauncherShortcutHelper;
import com.nd.hilauncherdev.settings.SettingsPreference;

/**
 * 桌面快捷的管理全部整理到LauncherShortcutHelper处理
 * 界面管理快捷方式辅助类
 * 
 * @author Anson
 */
public class LauncherMainDockShortcutHelper {

	public static final int ERROR_TYPE = -1;

	/**
	 * 生成界面管理快捷方式信息
	 * 
	 * @param launcher Launcher
	 * @param cellX  cellX
	 * @param cellY  cellY
	 * @return ApplicationInfo
	 */
	public static ApplicationInfo addMainDockShortcutToWorkspace(Launcher launcher, int cellX, int cellY, String title) {
		return addMainDockShortcutToWorkspace(launcher, cellX, cellY, title, launcher.getWorkspace().getCurrentScreen());
	}

    //<editor-fold desc="Description">
    public static ApplicationInfo addMainDockShortcutToWorkspace(Launcher launcher, int cellX, int cellY, String title, int screen) {
		return LauncherShortcutHelper.getInstance().addMainDockShortcutToWorkspace(launcher,cellX, cellY, title, screen);
	}
    //</editor-fold>

	public static ApplicationInfo createLauncherEditMainDockInfo(Context ctx, String title) {
		return LauncherShortcutHelper.getInstance().createLauncherEditMainDockInfo(ctx, title);
	}

/*	public static int getDrawId(Context ctx, String title) {
		return LauncherShortcutHelper.getInstance().getDrawId(ctx, title);
	}*/
	
	/**
	 * 获取快捷对应的key
	 * author dingdj
	 * Date:2014-1-7下午3:19:58
	 *  @param ctx Context
	 *  @param title 快捷方式名称
	 *  @return key
	 */
/*	public static String getThemeKeyByTitle(Context ctx, String title) {
		return LauncherShortcutHelper.getInstance().getThemeKeyByTitle(ctx, title);
	}*/
	
	/**
	 * 获取快捷对应的key
	 * author dingdj
	 * Date:2014-1-7下午3:19:58
	 *  @return String
	 */
	public static String getThemeKeyByAction(String action) {
		return LauncherShortcutHelper.getInstance().getThemeKeyByAction(action);
	}

	/**
	 * 通过action去获取桌面快捷的相关信息
	 * 
	 * @param ctx Context
	 * @param action action
	 * @return ApplicationInfo
	 */
	public static ApplicationInfo getLauncherEditMainDockInfoByAction(Context ctx, String action) {
		return LauncherShortcutHelper.getInstance().getLauncherEditMainDockInfoByAction(ctx, action);
	}

	
	/**
	 * 通过action获取标题
	 * @author wangguomei
	 * 
	 * @param ctx Context
	 * @param action Action
	 * 
	 * @return 91快捷标题
	 */
	public static String get91ShortcutTitleByAction(Context ctx, String action) {
        return LauncherShortcutHelper.getInstance().get91ShortcutTitleByAction(ctx,action);
	}

	/**
	 * 状态栏开关
	 * 
	 * @param launcher Launcher
	 * @param sp SettingsPreference
	 */
	public static void actionNotificationSwitch(Launcher launcher, SettingsPreference sp) {
        LauncherShortcutHelper.actionNotificationSwitch(launcher, sp);
	}

	/**
	 * 状态栏隐藏开关 @author zhl
	 */
	public static void actionNotification(Launcher launcher, SettingsPreference sp) {
        LauncherShortcutHelper.actionNotification(launcher, sp);
	}

	/**
	 * 壁纸滚动开关
	 * 
	 * @param launcher Launcher
	 * @param sp SettingsPreference
	 */
	public static void actionWallpaper(Launcher launcher, SettingsPreference sp) {
        LauncherShortcutHelper.actionWallpaper(launcher, sp);
	}

/*	public static CharSequence getWallpaperSwitch(Context context, Resources res) {
        return LauncherShortcutHelper.getWallpaperSwitch(context, res);
	}*/

	/**
	 * 蒙板开关
	 * 
	 * @param launcher Launcher
	 * @param sp SettingsPreference
	 */
	public static void actionIconMask(Launcher launcher, SettingsPreference sp) {
        LauncherShortcutHelper.actionIconMask(launcher, sp);
	}

/*	public static CharSequence getIconMaskSwitch(Resources res, SettingsPreference sp) {
		return LauncherShortcutHelper.getIconMaskSwitch(res, sp);
	}*/

	/**
	 * 切换滑屏特效
	 * 
	 * @param launcher Launcher
	 * @param sp SettingsPreference
	 */
	public static void actionTransitonEffect(Launcher launcher, SettingsPreference sp) {
        LauncherShortcutHelper.actionTransitonEffect(launcher, sp);
	}

	/**
	 * 托盘开关
	 * 
	 * @param launcher Launcher
	 * @param sp SettingsPreference
	 */
	public static void actionDockSwitch(Launcher launcher, SettingsPreference sp) {
        LauncherShortcutHelper.actionDockSwitch(launcher, sp);
	}

    /**
     * 是否是应用商店，热门软件，热门游戏，每日新鲜事
     * @return Boolean
     */
    public static boolean isFourSpecialCustomIntent(Context ctx, String action){
        return  LauncherShortcutHelper.getInstance().isFourSpecialCustomIntent(ctx, action);
    }
}
