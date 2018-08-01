/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : StartupIntentReceiver
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-11-30 下午04:11:14 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.weather.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.nd.calendar.Control.DownHighPicControler;
import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.util.ComfunHelp;

public class StartupReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			return;
		}
		
		String sAction = intent.getAction();
		context = context.getApplicationContext();
		WidgetGlobal.startCalendarService(context);
		
		if (WeatherLinkTools.canLink(context)) {
			return;
		}
		
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(sAction)) {
        	WidgetGlobal.updateWidgets(context);
        	
		    // 网络变化
		    if (HttpToolKit.isNetworkAvailable(context)) {
		    	TimeService.autoUpdateFirstWeather(context, false);
		    	
		        // 下载天气图标
		        DownHighPicControler.getInstance(context).startDown();
		        
		        // 移除宜忌失败标志
		        SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SETTING);
		        set.edit().remove(ConfigsetData.CONFIG_NAME_KEY_HUANGLI_LAST).commit();
		    }
		    
		} else if (WidgetUtils.WIDGET_PANDAHOME_WEATHER_REQUEST_ACTION.equals(sAction)) {
//			if (!ComfunHelp.checkApkExist(context, ComDataDef.CALENDAR_PACKAGE, ComDataDef.CALENDAR_VAR_CODE_2_4_0)) {
			int calendarVersion = ComfunHelp.getAppVersion(context, ComDataDef.CALENDAR_PACKAGE);
				if(calendarVersion < ComDataDef.CALENDAR_VAR_CODE_2_4_0 || (calendarVersion >= ComDataDef.CALENDAR_VAR_CODE_3_3_2
						&& calendarVersion < ComDataDef.CALENDAR_VAR_CODE_3_6_1)) {
				TimeService.autoUpdateFirstWeather(context, false);
			}
		}
	}
}
