package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.List;


/**
 * 不同定制版控制基础类
 */
public class BaseCustomChannelController {
    /**
     * LauncherApplication创建时调用
     * @param application
     */
    public static void onLauncherApplicationCreate(LauncherApplication application){
    }

    /**
     * 手机锁屏解锁回调
     * @param ctx
     */
    public static void onUserPresentOn(Context ctx){
    }


    //===========================开机引导 start  ============================//
    /**
     * 是否显示内置在Launcher里的开机引导
     * @param ctx
     * @return
     */
    public static boolean showInnerCustomGuide(Context ctx){
        return false;
    }

    /**
     * 重置开机引导（目前帆悦定制版使用）
     * @param ctx
     */
    public static void resetStartGui(Context ctx) {
    }
    //===========================开机引导  end  ============================//






    //===========================锁屏相关控制  start  ============================//
    /**
     * 启动锁屏服务
     */
    public static void startLockScreenServer(final Context context,Handler handler){

    }

    /**
     * 禁用系统锁屏的回调
     * @param ctx
     */
    public static void onDisableKeyguard(Context ctx){

    }

    /**
     * 重新启用系统锁屏回调
     * @param ctx
     */
    public static void onReenableKeyguard(Context ctx){

    }
    //===========================锁屏相关控制  end  ============================//





    //===========================力天印尼定制版  start  ============================//
    /**
     * 获取桌面布局文件id，如果返回-1，Launcher里面将用R.layout.drawer_main来代替
     * @return
     */
    public static int getDrawerLayoutId(){
        return -1;
    }

    /**
     * 获取海外推送数据 线程内部调用
     */
    public static String getLitianYinniPushJson(Context ctx){
        return "";
    }
    /**
     * 海外推送图标intent的特殊处理
     * @param ctx
     * @return
     */
    public static boolean onLiantianYinniPushIconIntentAction(Context ctx,Intent intent){
        return false;
    }

    /**
     * 安装双开应用
     * @param context
     */
    public static void installQuickApp(Context context){

    }

    public static void add3UcIcons2Workspace_LITIAN_YINNI(Context ctx) {

    }
    //===========================力天印尼定制版  end  ============================//

    public static View getShenLongAdView(Context context, String folderName) {
        return null;
    }

    public static void addShenLongAdKeywords(Context context, List<String> keywords) {

    }

    public static void checkPushSdk_LiTianYinni(final Context ctx){

    }

    public static void recordFirstNoWifiConnectedTimeForLock(final Context ctx, long noWifiFirstTime){
    }

    public static void startAdWall(final Context ctx){
    }

    //----------------------------------------打点统计--------------------------------//
    public static void eventAppClick(String packageName) {

    }
    public static void eventFolderAppClick(String packageName) {

    }
    public static void eventAppInstall(String packageName) {

    }
    public static void eventAppUninstall(String packageName) {

    }
    public static void eventNavigationApp(String packageName, int position) {

    }
    public static void eventNavigationBrowser(String packageName) {

    }
    public static void eventNavigationWidget(String packageName, int type) {

    }
}
