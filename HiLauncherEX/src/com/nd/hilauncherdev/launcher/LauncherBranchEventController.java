package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.nd.android.pandahome2.manage.shop.ThemeShopMainActivity;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.ActivityActionUtil;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.lieyingbrowser.BrowserPluginManager;

import java.util.List;

/**
 * Description: 定制版桌面事件控制器<br>
 * Author: chenxy
 * Date: 2016/9/2 12:34
 */
public class LauncherBranchEventController {
    private BrowserPluginManager mManager;
    /**
     * 单例
     */
    private static LauncherBranchEventController launcherBranchEventController;
    private LauncherBranchEventController(){
    }
    public static LauncherBranchEventController getInstance(){
        if (launcherBranchEventController == null){
            launcherBranchEventController = new LauncherBranchEventController();
        }
        return launcherBranchEventController;
    }



    public boolean onCellClick(View v, ApplicationInfo appInfo, Context context) {
        if(LauncherBranchController.isXinShiKong() || LauncherBranchController.isDingKai()
                || LauncherBranchController.isTiapPai_SmartHome()){
            if(appInfo.intent != null){
                final String intentStr = appInfo.intent.toURI();
                if(!TextUtils.isEmpty(intentStr) && intentStr.contains(LauncherProviderHelper.LITIAN_DOWANLOAD_APP_SPLIT)){
                    String[] strArray = intentStr.split(LauncherProviderHelper.LITIAN_DOWANLOAD_APP_SPLIT);
                    if(strArray.length >= 2){
                        if(!TelephoneUtil.isNetworkAvailable(context)){
                            Toast.makeText(context, "网络未连接，无法下载", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        String identification = strArray[0] + ".apk";
                        Intent intent = new Intent(context.getPackageName() + ".FORWARD_SERVICE");
                        intent.putExtra("identification", identification);
                        intent.putExtra("fileType", 0);
                        intent.putExtra("downloadUrl", strArray[1]);
                        intent.putExtra("title", appInfo.title);
                        intent.putExtra("savedDir",  BaseConfig.WIFI_DOWNLOAD_PATH);
                        intent.putExtra("savedName", identification);
                        intent.putExtra("iconPath", "");
                        intent.putExtra("totalSize", "");
                        context.startService(intent);
                        return true;
                    }
                }
            }
        }
        //猎鹰定制包浏览器走猎鹰逻辑
        if (LauncherBranchController.isLieYing()){
            if (appInfo != null && appInfo.componentName != null) {
                String pkg = appInfo.componentName.getPackageName();
                if ("com.android.browser".equals(pkg)||
                        BrowserPluginManager.LIEYINGPACKAGENAME.equals(pkg)||
                        "com.htc.sense.browser".equals(pkg)||
                        "com.sec.android.app.sbrowser".equals(pkg)) {//拦截浏览器转换成猎鹰浏览器
                    HiAnalytics.submitEvent(context, AnalyticsConstant.LIEYING_CLICK_BROWSER);

                    if (mManager == null){
                        initBrowserPluginManager(context);
                    }
                    Intent mInstallIntent = new Intent();
                    mInstallIntent.setAction(BrowserPluginManager.INTENT_ACTION);
                    List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(mInstallIntent, 0);
                    if (list.size() > 0) {//猎鹰浏览器已经安装
                        context.startActivity(mInstallIntent);
                        return true;
                    }
                    if (mManager.checkApkValid()) {//猎鹰插件已经可用了，直接打开猎鹰浏览器
                        mManager.openApk(pkg);
                        return true;
                    }else{//没安装去下载
                        ThreadUtil.executeMore(new Runnable() {
                            @Override
                            public void run() {
                                mManager.downloadApp();
                            }
                        });
                    }
                    //接着走本来桌面的浏览器打开逻辑
                }
            }
        }

        //其他定制逻辑
        if (v == null || context == null || !LauncherBranchController.isTianPai())
            return false;
        if (appInfo != null && appInfo.componentName != null) {
            String pkg = appInfo.componentName.getPackageName();
            Intent optIntent = new Intent();
            if (LauncherBranchController.isVivoPhone()) {//VIVO
                if ("com.bbk.calendar".equals(pkg)) {
                    if (AndroidPackageUtils.isPkgInstalled(context, "com.changmi.calendar")) {//天湃日历->万年历
                        optIntent.setClassName("com.changmi.calendar", "com.changmi.calendar.activity.WelcomeActivity");
                    } else if (AndroidPackageUtils.isPkgInstalled(context, "com.youloft.calendar")) {
                        optIntent.setClassName("com.youloft.calendar", "com.youloft.calendar.MainActivity");
                    }
                } else if ("com.android.browser".equals(pkg) || "com.vivo.browser".equals(pkg)) {
                    if (AndroidPackageUtils.isPkgInstalled(context, "com.UCMobile")) {//UC浏览器->qq浏览器
                        optIntent.setClassName("com.UCMobile", "com.UCMobile.main.UCMobile");
                    } else if (AndroidPackageUtils.isPkgInstalled(context, "com.tencent.mtt")) {
                        optIntent.setClassName("com.tencent.mtt", "com.tencent.mtt.SplashActivity");
                    }
                } else if ("com.bbk.theme".equals(pkg)) {//主题商店用系统图标，但调用我们桌面的主题商店
                    optIntent.setClass(context, ThemeShopMainActivity.class);
                } else if ("com.vivo.weather".equals(pkg)) {//本地天气调起中央天气预报
                    if (AndroidPackageUtils.isPkgInstalled(context, "com.nineton.weatherforecast")) {
                        List<ResolveInfo> rs = LauncherProviderHelper.queryMainIntentActivity(context, "com.nineton.weatherforecast");
                        if (rs != null && !rs.isEmpty()) {
                            ResolveInfo first = rs.get(0);
                            optIntent.setClassName(first.activityInfo.packageName, first.activityInfo.name);
                        }
                    }
                }
            } else if (LauncherBranchController.isOppoPhone()) {//OPPO
                if ("com.android.calendar".equals(pkg)) {
                    if (AndroidPackageUtils.isPkgInstalled(context, "com.changmi.calendar")) {//天湃日历->万年历
                        optIntent.setClassName("com.changmi.calendar", "com.changmi.calendar.activity.WelcomeActivity");
                    } else if (AndroidPackageUtils.isPkgInstalled(context, "com.youloft.calendar")) {
                        optIntent.setClassName("com.youloft.calendar", "com.youloft.calendar.MainActivity");
                    }
                } else if ("com.android.browser".equals(pkg)) {//百度浏览器->uc浏览器
                    if (AndroidPackageUtils.isPkgInstalled(context, "com.baidu.browser.apps")) {
                        optIntent.setClassName("com.baidu.browser.apps", "com.baidu.browser.framework.BdBrowserActivity");
                    } else if (AndroidPackageUtils.isPkgInstalled(context, "com.UCMobile")) {
                        optIntent.setClassName("com.UCMobile", "com.UCMobile.main.UCMobile");
                    }
                } else if ("com.nearme.themespace".equals(pkg)) {//主题商店用系统图标，但调用我们桌面的主题商店
                    optIntent.setClass(context, ThemeShopMainActivity.class);
                } else if ("com.coloros.weather".equals(pkg)) {//本地天气调起中央天气预报
                    if (AndroidPackageUtils.isPkgInstalled(context, "com.nineton.weatherforecast")) {
                        List<ResolveInfo> rs = LauncherProviderHelper.queryMainIntentActivity(context, "com.nineton.weatherforecast");
                        if (rs != null && !rs.isEmpty()) {
                            ResolveInfo first = rs.get(0);
                            optIntent.setClassName(first.activityInfo.packageName, first.activityInfo.name);
                        }
                    }
                }
            }
            if (optIntent.getComponent() != null) {
                ActivityActionUtil.startActivitySafelyForRecored(v, context, optIntent);
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化插件管理器
     * @param context
     */
    private void initBrowserPluginManager(final Context context){
        mManager = new BrowserPluginManager(context);
        mManager.setCallback(new BrowserPluginManager.PluginCallback() {
            @Override
            public void onResult(int result) {
                switch (result) {
                    case BrowserPluginManager.PluginCallback.INSTALL_SUCCEEDED:
                        break;
                    case BrowserPluginManager.PluginCallback.INSTALL_FAIL:
                        break;
                    case BrowserPluginManager.PluginCallback.INSTALLED:
                        break;
                    case BrowserPluginManager.PluginCallback.DELETE_SUCCEEDED:
                        break;
                    case BrowserPluginManager.PluginCallback.UNINSTALLED:
                        break;
                    case BrowserPluginManager.PluginCallback.DOWNLOAD_SCCEEDED:
                        try {
                            mManager.install();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        HiAnalytics.submitEvent(context, AnalyticsConstant.LIEYING_DOWNLOAD_BROWSER_PLUGIN,"cg");
                        break;
                    case BrowserPluginManager.PluginCallback.DOWNLOAD_FAIL:
                        HiAnalytics.submitEvent(context, AnalyticsConstant.LIEYING_DOWNLOAD_BROWSER_PLUGIN,"sb");
                        break;
                    case BrowserPluginManager.PluginCallback.DOWNLOADING:
                        break;
                    case CONNECTED:
                        break;
                    case DISCONNECTED:
                        break;
                    case DOWNLOAD:
                        break;
                }
            }
        });
    }
}
