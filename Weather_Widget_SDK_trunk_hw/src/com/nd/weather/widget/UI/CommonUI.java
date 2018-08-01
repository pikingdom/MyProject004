/**   
 *    
 * @file 【公用 UI 工具类】
 * @brief
 *
 * @<b>文件名</b>      : CommonUI
 *@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2011-12-8 下午03:40:23 
 * @n@n<b>文件描述</b>:  
 * @version  
 */
package com.nd.weather.widget.UI;

import java.io.File;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.widget.Toast;

import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.util.ComfunHelp;
import com.nd.hilauncherdev.kitset.AppDistributeUtil;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.ApkTools;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadServerServiceConnection;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.FileType;
import com.nd.weather.widget.R;
import com.nd.weather.widget.WidgetUtils;

public class CommonUI {

    public interface IOnDownLoad {
        public void onDownLoad();
    }

    public final static int START_YEAR = 1900;
    public final static int END_YEAR = 2049;

    /**
     * @brief 【显示网络设置界面】
     * 
     * @n<b>函数名称</b> :ShowNetworkSet
     * @param activity
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2011-12-12上午09:57:03
     */
    public static void ShowNetworkSet(final Context activity) {
    	CommonDialog.Builder b = new CommonDialog.Builder(activity);
        b.setTitle("没有可用的网络");
        b.setMessage("是否对网络进行设置？");

        b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                Intent mIntent = new Intent("/");
                ComponentName comp = new ComponentName("com.android.settings",
                        "com.android.settings.WirelessSettings");
                mIntent.setComponent(comp);
                mIntent.setAction("android.intent.action.VIEW");

                try {
                    activity.startActivity(mIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    // 直接进入设置界面
                    activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    // Toast.makeText(activity,
                    // "无法打开网络设置，请在系统的设置中进行设置。", Toast.LENGTH_LONG);
                }
                dialog.cancel();
            }

        });
        
        b.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.cancel();
            }
        });
        
       b.create().show();
    }

    /**
     * @Title: showDownLoadDialog  
     * @Description: TODO(下载提示)  
     * @author yanyy
     * @date 2012-12-4 下午07:04:58 
     *
     * @param title
     * @param context
     * @param onDownLoad       
     * @return void
     * @throws
     */
    public static void showDownLoadDialog(String title, final Context context,
            final IOnDownLoad onDownLoad) {
    	CommonDialog.Builder b = new CommonDialog.Builder(context);
        b.setTitle("下载提示").setMessage(
                "未安装“" + title + "”，或者不是最新版本，是否立即下载？");

        b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                if (onDownLoad != null) {
                    onDownLoad.onDownLoad();
                }
                dialog.cancel();
            }

        });
        
        b.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.cancel();
            }

        });
        b.create().show();
    }
    
    /**
     * @Title: downCalendarApk  
     * @Description: 下再黄历天气标准版  
     * @author yanyy
     * @date 2012-12-14 上午10:26:50 
     *
     * @param ctx       
     * @return void
     * @throws
     */
    public static void downCalendarApk(final Context ctx, final String from){
    	final String appName = ConfigHelper.getInstance(ctx).getReCommendWeatherAppName();
    	final String appIdentifier = ConfigHelper.getInstance(ctx).getReCommendWeatherAppIdentifier();
    	final String appUrl = ConfigHelper.getInstance(ctx).getReCommendWeatherAppUrl();
    	File file = new File(BaseConfig.WIFI_DOWNLOAD_PATH + appIdentifier + ".apk");
        if (ComfunHelp.checkApkExist(ctx, appIdentifier, 0)) {
            PackageManager pm = ctx.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(appIdentifier);
            SystemUtil.startActivitySafely(ctx, intent);
        } else if(file.exists() && ApkTools.checkApkIfValidity(ctx, file.getAbsolutePath())) {
    		ApkTools.installApplication(ctx, file);
        } else {
            if (HttpToolKit.isNetworkAvailable(ctx)) {
            	HiAnalytics.submitEvent(ctx, WidgetUtils.getAnalyticsId(ctx, R.string.analytics_weather_click_distribute), "6");
            	HiAnalytics.submitEvent(ctx, WidgetUtils.getAnalyticsId(ctx, R.string.analytics_weather_download_huangli), from);
            	//手动打点
                AppDistributeUtil.logAppDisDownloadStart(ctx, appIdentifier, WidgetUtils.getAnalyticsId(ctx, R.string.sp_widget_recommend_app));
                try {
                	//修改黄历天气客户端下载接口 caizp 2014-10-24
                	ThreadUtil.executeMore(new Runnable() {
						@Override
						public void run() {
							BaseDownloadInfo dlInfo = new BaseDownloadInfo(
									appUrl,
									FileType.FILE_APK.getId(),
									appUrl,
									appName,
									BaseConfig.WIFI_DOWNLOAD_PATH, appIdentifier + ".apk",
									"drawable:downloadmanager_apk_icon");
							DownloadServerServiceConnection mDownloadService = new DownloadServerServiceConnection(
									ctx);
							dlInfo.setDisId(ComDataDef.CALENDAR_PACKAGE);
		                    dlInfo.setDisSp(WidgetUtils.getAnalyticsId(ctx, R.string.sp_widget_recommend_app));
		                    dlInfo.setNeedStat();
							mDownloadService.addDownloadTask(dlInfo);
						}
                	});
                } catch (Exception e) {
                	e.printStackTrace();
                }
            } else {
                Toast.makeText(ctx, "请连接网络后再尝试！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
