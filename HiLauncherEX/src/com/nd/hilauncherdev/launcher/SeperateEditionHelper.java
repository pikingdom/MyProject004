package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import com.android.newline.launcher.R;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.ChannelUtil;
import com.nd.hilauncherdev.settings.SettingsPreference;
import com.nd.hilauncherdev.theme.data.ThemeData;

/**
 * description: 单行本相关类<br/>
 * author: dingdj<br/>
 * date: 2014/9/23<br/>
 */
public class SeperateEditionHelper {

    static final String BAIDUMAP = "com.baidu.BaiduMap";

    public static String[] Apps = {ThemeData.ICON_EMAIL, ThemeData.ICON_CALENDAR, ThemeData.ICON_GALLERYPICKER, ThemeData.ICON_CAMERA,
            ThemeData.ICON_GOOGLE_PLAY, ThemeData.ICON_ALARMCLOCK, BAIDUMAP, ThemeData.ICON_SETTINGS};

    static int[][] getIOS8AptLocation(Context mContext) {
        int defaultScreenNum = 0;

        return new int[][]{

                LauncherProviderHelper.isThemeAppInstall(mContext.getPackageManager(), Apps[0])?
                        new int[]{LauncherProviderHelper.THEME_APP, 0, defaultScreenNum, 0, 0}: //邮件
                        new int[]{LauncherProviderHelper.MYPHONE_TYPE, R.string.myphone_safecenter, defaultScreenNum, 0, 0}, //安全中心

                LauncherProviderHelper.isThemeAppInstall(mContext.getPackageManager(), Apps[1])?
                        new int[]{LauncherProviderHelper.THEME_APP, 1, defaultScreenNum, 1, 0}: //日历
                        new int[]{LauncherProviderHelper.MYPHONE_TYPE, R.string.anyshare_app_name, defaultScreenNum, 1, 0}, //快传
                LauncherProviderHelper.isThemeAppInstall(mContext.getPackageManager(), Apps[2])?
                        new int[]{LauncherProviderHelper.THEME_APP, 2, defaultScreenNum, 2, 0}: //照片
                        new int[]{LauncherProviderHelper.MYPHONE_TYPE, R.string.myflow_monitoring, defaultScreenNum, 2, 0}, //流量监控

                LauncherProviderHelper.isThemeAppInstall(mContext.getPackageManager(), Apps[3])?
                        new int[]{LauncherProviderHelper.THEME_APP, 3, defaultScreenNum, 3, 0}: //相机
                        new int[]{LauncherProviderHelper.MYPHONE_TYPE, R.string.myphone_backup, defaultScreenNum, 3, 0}, //我的备份


                isInstallGooglePlay(mContext)?new int[]{LauncherProviderHelper.THEME_APP, 4, defaultScreenNum, 0, 1}://play 商店
                        (isSanWU(mContext)?new int[]{LauncherProviderHelper.MYPHONE_TYPE, R.string.appmanager_title, defaultScreenNum, 0, 1}://应用管家
                                new int[]{LauncherProviderHelper.RECOMMEND_FOLDER, 4, defaultScreenNum, 0, 1}),//推荐

                LauncherProviderHelper.isThemeAppInstall(mContext.getPackageManager(), Apps[5])?
                        new int[]{LauncherProviderHelper.THEME_APP, 5, defaultScreenNum, 1, 1}://时钟
                        new int[]{LauncherProviderHelper.MYPHONE_TYPE, R.string.mycleaner_title, defaultScreenNum, 1, 1}, //手机加速

                isInstalledBaiduMap(mContext)?new int[]{LauncherProviderHelper.NORMAL_APP, 6, defaultScreenNum, 2, 1}://百度地图
                        new int[]{LauncherProviderHelper.MYPHONE_TYPE, R.string.myphone_sd, defaultScreenNum, 2, 1},//我的文件
                new int[]{LauncherProviderHelper.MYPHONE_TYPE, R.string.myphone_faq, defaultScreenNum, 3, 1},//帮助手册


                new int[]{LauncherProviderHelper.MYPHONE_TYPE, R.string.theme_shop_v6_app_name, defaultScreenNum, 0, 2},//个性化
                //换壁纸
                isSanWU(mContext)?new int[]{LauncherProviderHelper.MYPHONE_TYPE, R.string.myphone_power, defaultScreenNum, 2, 2}://我的电池
                        new int[]{LauncherProviderHelper.SHORTCUT91_TYPE, R.string.myphone_app_store, defaultScreenNum, 2, 2},//应用商店
                new int[]{LauncherProviderHelper.SHORTCUT91_TYPE, R.string.main_dock_drawer_myphone, defaultScreenNum, 3, 2},//我的手机

                new int[]{LauncherProviderHelper.THEME_APP, 7, defaultScreenNum, 0, 3},//设置
                //一键加速
                new int[]{LauncherProviderHelper.ANYTHING_TYPE, AnythingInfo.FLAG_FOLDER_RECENT, defaultScreenNum, 2, 3},//快速打开
                new int[]{LauncherProviderHelper.SHORTCUT_FOLDER, 0, defaultScreenNum, 3, 3}//工具箱
        };

    }


    static void addRecommendIcon(Context mContext, SQLiteDatabase db) {
        int[] countXY = SettingsPreference.getInstance().getScreenCountXY();
        int xCount = countXY[0];
        int yCount = countXY[1];
        int countInScreen = xCount*yCount;
        int cellCountX = 4;
        int screen = 1;
        int index = 0;
        int cellX = 0;
        int cellY = 0;
        PackageManager mPackageManager = mContext.getPackageManager();
        for (int i = 0; i < LauncherProviderHelper.RECOMMEND_APPS.length; i++) {
            if (addNewScreen(countInScreen, index)) {//增加到新的一屏
                index = 0;
                screen++;
                cellX = 0;
                cellY = 0;
            }

            if (addNewLine(cellCountX, cellX)) {//增加到新的一行
                cellX = 0;
                cellY++;
            }

            Intent appIntent = AndroidPackageUtils.getPackageMainIntent(mContext, LauncherProviderHelper.RECOMMEND_APPS[i]);
            if (appIntent == null) continue;
            ResolveInfo resolveInfo = mPackageManager.resolveActivity(appIntent, 0);
            if (resolveInfo == null) continue;
            String title = resolveInfo.loadLabel(mPackageManager).toString();
            LauncherProviderHelper.saveToDb(db, cellY, cellX, appIntent, title, screen);
            cellX++;
            index++;
        }

    }

    private static boolean addNewLine(int cellCountX, int cellX) {
        return cellX == cellCountX;
    }

    private static boolean addNewScreen(int countInScreen, int index) {
       return countInScreen == index;
    }


    private static boolean isInstalledBaiduMap(Context mContext) {
        return AndroidPackageUtils.isPkgInstalled(mContext, BAIDUMAP);
    }

    private static boolean isInstallGooglePlay(Context mContext) {
        String pkgName = ThemeData.ICON_GOOGLE_PLAY.split("\\|")[0];
        return AndroidPackageUtils.isPkgInstalled(mContext, pkgName);
    }

    public static boolean isSanWU(Context mContext){
        return ChannelUtil.SANWUCHANNEL == ChannelUtil.getChannelType(mContext);
    }
}
