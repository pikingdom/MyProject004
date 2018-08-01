package com.nd.hilauncherdev.launcher;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.felink.quickhelper.LauncherContract;
import com.felink.quickhelper.LauncherOpenAppTransformActivity;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.launcher.model.BaseLauncherSettings;

/**
 * 应用双开
 * 该类被反射调用，切勿删除
 * Created by caizhipeng on 2017/3/13.
 */

public class MultiAppController {

    /**
     * 启用双开功能
     * @param context
     */
    public static void initQuickHelper(Context context) {
        LauncherContract.getInstance().initQuickHelper(context);
    }

    /**
     * 是否可以双开
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isEnableQuickOpenApp(Context context, String packageName) {
        return LauncherContract.getInstance().isEnableQuickOpenApp(context, packageName);
    }

    /**
     * 安装APP（该方法为耗时操作）
     * @param context
     * @param packageName
     */
    public static void installQuickApp(Context context, String packageName) {
        LauncherContract.getInstance().installQuickApp(context, packageName);
    }
    public static void installQuickApp(Context context, String packageName,String apkPath) {
        LauncherContract.getInstance().installQuickApp(context, packageName,apkPath);
    }

    /**
     * 是否在双开系统中安装了该应用
     * @param packageName
     * @return
     */
    public static boolean isInstallQuickApp(String packageName){
        return LauncherContract.getInstance().isInstallQuickApp(packageName);
    }

    /**
     * 卸载APP（该方法为耗时操作）
     * @param packageName
     */
    public static void uninstallQuickApp(String packageName) {
        LauncherContract.getInstance().uninstallQuickApp(packageName);
    }

    /**
     * 打开APP
     * @param context
     * @param packageName
     */
    public static void openQuickApp(Context context, String packageName) {
        LauncherContract.getInstance().openQuickApp(context, packageName);
    }

    /**
     * 获取打开双开APP的Intent
     * @param context
     * @param packageName
     * @return
     */
    public static Intent getOpenAppIntent(Context context, String packageName) {
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(context, LauncherOpenAppTransformActivity.class);
        launcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launcherIntent.putExtra(LauncherOpenAppTransformActivity.PACKAGE_NAME, packageName);
        return launcherIntent;
    }

    /**
     * 双开应用图标是否已存在桌面上
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isMultiAppIconExist(Context context, String packageName) {
        final ContentResolver cr = context.getContentResolver();
        Cursor c = null;
        Intent intent = getOpenAppIntent(context, packageName);
        String intentStr = intent.toUri(0);
        if (StringUtil.isEmpty(intentStr))
            return false;
        boolean result = false;
        try {
            c = cr.query(BaseLauncherSettings.Favorites.getContentUri(), new String[]{"itemType", "intent"},
                    "itemType=? and intent=?", new String[]{String.valueOf(LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT), intentStr}, null);
            result = c.moveToFirst();
        } finally {
            if(null != c) {
                c.close();
            }
        }
        return result;
    }

}
