/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : InstallReceiver
 *@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2012-12-4 下午03:48:51 
 * @n@n<b>文件描述</b>:  
 * @version  
 */
package com.nd.weather.widget.PandaHome.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.provider.CalendarDatas;
import com.nd.calendar.util.ComfunHelp;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.weather.widget.R;
import com.nd.weather.widget.WidgetUtils;
import com.nd.weather.widget.PandaHome.PandaWidgetView;

public class InstallReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(final Context context, Intent intent) {
		String sAction = intent.getAction();

//		Log.d("install", sAction + ": " + intent.getDataString());
		boolean bRemove = false;
		
		if (sAction.equals(Intent.ACTION_PACKAGE_ADDED)
				|| (bRemove = sAction.equals(Intent.ACTION_PACKAGE_REMOVED))) {
			String packageName = intent.getDataString();
			String appIdentifier = ConfigHelper.getInstance(context).getReCommendWeatherAppIdentifier();
			if (packageName != null && packageName.endsWith(appIdentifier)) {
				boolean baok = ComfunHelp.checkApkExist(context, appIdentifier, 0);
				Log.d("widget", sAction + ": " + baok);
				if(sAction.equals(Intent.ACTION_PACKAGE_ADDED)) { // 黄历天气安装成功打点 caizp 2015-02-27
					HiAnalytics.submitEvent(context, WidgetUtils.getAnalyticsId(context, R.string.analytics_weather_install_huangli));
				}
				if(android.os.Build.VERSION.SDK_INT >= 21 || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
					return;
				}
				if (bRemove || baok) {
					 PandaWidgetView.requreWidgetReload(context, bRemove);					
				}
			}
		}
	}
//
//	void restartPanda(Context context) {
//		Intent i = context.getPackageManager().getLaunchIntentForPackage("com.nd.android.pandahome2");
//		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		i.addCategory(Intent.CATEGORY_HOME); 
//		context.startActivity(i);
//	}
}
