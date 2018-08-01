package com.nd.weather.widget.PandaHome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nd.hilauncherdev.analysis.AppAnalysisConstant;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.webconnect.downloadmanage.DownloadUrlManage;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadServerServiceConnection;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.FileType;
import com.nd.weather.widget.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * 黄历天气插件管理
 * Created by maolinnan on 2017/1/11.
 */

public class WeatherPluginManger {
    private static final String DOWNLOAD_LOCAL_PATH = BaseConfig.getBaseDir() + "/WifiDownload/";
    public static final String WEATHER_APP_MAINACTIVITY = "com.calendar.UI.UIWelcome";//天气应用主入口
    public static final String WEATHER_PLUGIN_MAINACTIVITY = "com.calendar.UI.UIWelcome_From_Desk";//天气插件主入口
    public static final String WEATHER_PACKAGE_NAME = "com.calendar.UI"; //黄历天气包名
    /**
     * 获取设置黄历天气动态加载是否启用
     */
    public static final String WEATHER_PLIGIN_SETTING = "weatherPliginSetting";
    public static final String KEY_DROIDPLUGIN_WEATHER = "key_droidplugin_weather";//动态加载黄历天气启用key
    public void setDroidpluginWeather(Context context,boolean type){
        SharedPreferences sp = context.getSharedPreferences(WEATHER_PLIGIN_SETTING, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE | 4);
        sp.edit().putBoolean(KEY_DROIDPLUGIN_WEATHER,type).commit();
    }

    public boolean getDroidpluginWeather(Context context){
        SharedPreferences sp = context.getSharedPreferences(WEATHER_PLIGIN_SETTING, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE | 4);
        return sp.getBoolean(KEY_DROIDPLUGIN_WEATHER,false);
    }



    //单例
    private static WeatherPluginManger weatherPluginManger;
    private WeatherPluginManger(){
    }
    public static WeatherPluginManger getInstance(){
        if (weatherPluginManger == null){
            weatherPluginManger = new WeatherPluginManger();
        }
        return weatherPluginManger;
    }

    //预加载天气
    public void prestrainWeather(final Context context){
        // 铭来定制版不使用黄历天气 caizp 20170615
        if("8.8".equals(TelephoneUtil.getVersionName(context, context.getPackageName()))) return;
        //安装了黄历天气正式版
        if(AndroidPackageUtils.isPkgInstalled(context, WeatherPluginManger.WEATHER_PACKAGE_NAME) || isInstallApp(WEATHER_PACKAGE_NAME)){
            return;
        }
        //判断本地是否已经存在下载包
        File file = new File(DOWNLOAD_LOCAL_PATH + WEATHER_PACKAGE_NAME+".apk");
        if (file.exists()){//存在文件,去安装
            try {
                installPackage(context,file.getPath());
//                PluginManager.getInstance().installPackage(file.getPath(), 0);
                HiAnalytics.submitEvent(context, AnalyticsConstant.CALENDAR_WEATHER_INSTALL);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{//从assets中拷贝并安装
            openAssert(context);
        }
    }

    /**
     * 开启黄历天气插件
     * true 则打开成功
     */
    public long clickTime = 0;
    public boolean openWeatherPlugin(Context context){
        // 铭来定制版不使用黄历天气 caizp 20170615
        if("8.8".equals(TelephoneUtil.getVersionName(context, context.getPackageName()))) return false;
        //防止重复点击
        if (System.currentTimeMillis() - clickTime <= 1000){
            return true;
        }
        if (getDroidpluginWeather(context)){
            return false;
        }
        clickTime = System.currentTimeMillis();
        //安装了黄历天气插件，直接启动黄历天气插件
        if (AndroidPackageUtils.isPkgInstalled(context,WEATHER_PACKAGE_NAME) || isInstallApp(WEATHER_PACKAGE_NAME)){
            return startWeatherApp(context);
        }
        //已存在天气插件安装包，则安装插件，然后再试
        File file = new File(DOWNLOAD_LOCAL_PATH + WEATHER_PACKAGE_NAME+".apk");
        if (file.exists()){
            try {
                installPackage(context,file.getPath());
//                PluginManager.getInstance().installPackage(file.getPath(), 0);
                HiAnalytics.submitEvent(context, AnalyticsConstant.CALENDAR_WEATHER_INSTALL);
                return startWeatherApp(context);//启动黄历天气插件
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 启动天气应用
     * @param context
     * @return
     */
    private boolean startWeatherApp(Context context){
        //尝试启动插件
        try {
            Intent intent = new Intent();
            boolean installed = isInstallApp(WEATHER_PACKAGE_NAME);
            if (installed) {
//                intent.setClassName(WEATHER_PACKAGE_NAME, WEATHER_PLUGIN_MAINACTIVITY);
                openInstallApp(context,WEATHER_PACKAGE_NAME);
                HiAnalytics.submitEvent(context, AnalyticsConstant.CALENDAR_WEATHER_OPEN_PLUGIN);
            }else{
                intent .setClassName(WEATHER_PACKAGE_NAME,WEATHER_APP_MAINACTIVITY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
//        //尝试启动黄历天气
//        try {
//            Intent intent = new Intent();
//            intent .setClassName(WEATHER_PACKAGE_NAME,WEATHER_APP_MAINACTIVITY);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return false;
    }


    /**
     * 拷贝文件
     */
    private void openAssert(Context mContext) {
        try {
            InputStream input = mContext.getResources().getAssets().open(WEATHER_PACKAGE_NAME);
            File apk = new File(DOWNLOAD_LOCAL_PATH + WEATHER_PACKAGE_NAME+".apk");
            if (!apk.getParentFile().exists()){
                apk.getParentFile().mkdirs();
            }
            OutputStream os = new FileOutputStream(apk);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = input.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
            os.close();
            input.close();
            //安装插件
            try {
                installPackage(mContext,apk.getPath());
//                PluginManager.getInstance().installPackage(apk.getPath(), 0);
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试wifi预加载
     */
    public void tryWifiDownloadWeather(Context context){
        if (!TelephoneUtil.isWifiEnable(context)){//WIFI不可用，直接返回
            return;
        }
        if (AndroidPackageUtils.getVersionCode(context,context.getPackageName())  >= 7998){//不是力天的就不预下载
            return;
        }
        File apkFile = new File(DOWNLOAD_LOCAL_PATH + WEATHER_PACKAGE_NAME+".apk");
        if (apkFile.exists()){//APK文件存在直接返回
            return;
        }
        //去下载天气APP
        DownloadServerServiceConnection mDownloadService = new DownloadServerServiceConnection(context);
        String fileUrl = DownloadUrlManage.getDownloadUrlFromPackageName(context, WEATHER_PACKAGE_NAME, "", AppAnalysisConstant.SP_LITIAN_WIFI_DOWNLOAD_APP);
        BaseDownloadInfo dlInfo = new BaseDownloadInfo(WEATHER_PACKAGE_NAME, FileType.FILE_APK.getId(), fileUrl, context.getString(R.string.sdk_app_name), BaseConfig.WIFI_DOWNLOAD_PATH,
                WEATHER_PACKAGE_NAME+".apk", "");
        dlInfo.setDisSp(AppAnalysisConstant.SP_LITIAN_WIFI_DOWNLOAD_APP);
        dlInfo.setDisId(WEATHER_PACKAGE_NAME);
        mDownloadService.addSilentDownloadTask(dlInfo);
    }

    /***************************反射调用双开助手提供的API*********************************/
    public void installPackage(Context context,String apkPath){
        try {
            Class MultiAppController = Class.forName("com.nd.hilauncherdev.launcher.MultiAppController");
            Method installQuickApp = MultiAppController.getMethod("installQuickApp",Context.class,String.class,String.class);
            installQuickApp.invoke(null,context,WEATHER_PACKAGE_NAME,apkPath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean isInstallApp(String packageName){
        try {
            Class MultiAppController = Class.forName("com.nd.hilauncherdev.launcher.MultiAppController");
            Method installQuickApp = MultiAppController.getMethod("isInstallQuickApp",String.class);
            boolean flag = (Boolean)installQuickApp.invoke(null,packageName);
            return flag;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void openInstallApp(Context context,String packageName){
        try {
            Class MultiAppController = Class.forName("com.nd.hilauncherdev.launcher.MultiAppController");
            Method installQuickApp = MultiAppController.getMethod("openQuickApp",Context.class,String.class);
            installQuickApp.invoke(null,context,packageName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
