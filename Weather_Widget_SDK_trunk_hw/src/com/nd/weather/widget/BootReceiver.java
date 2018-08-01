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

public class BootReceiver extends BroadcastReceiver
{
	// 接收广播
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			return;
		}
		
		String sAction = intent.getAction();
		if (Intent.ACTION_USER_PRESENT.equals(sAction)) {
			
			// 更新所有插件
			WidgetGlobal.updateWidgets(context);
			WidgetGlobal.startCalendarService(context);
		}
	}
}
