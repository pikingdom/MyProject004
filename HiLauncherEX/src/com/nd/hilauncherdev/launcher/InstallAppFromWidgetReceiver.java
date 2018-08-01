package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.content.Intent;
import com.nd.hilauncherdev.launcher.broadcast.HiBroadcastStaticReceiver;
import com.nd.hilauncherdev.widget.shop.util.WidgetShopAddedHelper;

/**
 * 功能说明：插件中点击安装客户端时发此广播给91桌面，以便统计由插件带来的app装机量
 * WLH
 */

public class InstallAppFromWidgetReceiver extends HiBroadcastStaticReceiver{
	
	private final static String APP_INSTALL_FROM_WIDGET_ACTION = "nd.panda.custom.widget.install.action";//插件中点击安装客户端时发此广播给91桌面 

	@Override
	public void onReceiveHandler(Context context, Intent data) {
		if(!APP_INSTALL_FROM_WIDGET_ACTION.equals(data.getAction())){
			return;
		}
		String widget_app_name = data.getStringExtra("widget_name");//插件包名
		String application_name = data.getStringExtra("application_name");//APP包名
		if(widget_app_name != null && !widget_app_name.equals("") && application_name != null && !application_name.equals("")){
			WidgetShopAddedHelper.getInstance().setWidgetPackage(widget_app_name, application_name);
		}
		
	}
}
