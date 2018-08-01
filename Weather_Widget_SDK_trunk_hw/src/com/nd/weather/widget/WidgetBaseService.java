/**   
*    
* @file
* @brief
*
* @<b>文件名</b>      : WidgetBaseService
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2012-7-11 上午11:13:15 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.weather.widget;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

public abstract class WidgetBaseService extends Service
{
	// 服务前置函数
	private static final Class<?>[] mSetForegroundSignature = new Class[] { boolean.class };

	private Method mSetForeground;
    private Object[] mSetForegroundArgs = new Object[1];

    
	/**
	 * @brief 【指定服务是否在运行】
	 * @n<b>函数名称</b>     :isWorked
	 * @param context
	 * @param strServer
	 * @return
	 * @return    boolean   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-5-18下午01:51:51
	 * @<b>修改时间</b>      :
	*/
    protected static boolean isWorked(Context context, String strServer) {
		ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(100);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString().equals(strServer)) {
				return true;
			}
		}
		return false;
	}

	
    protected static void sendRefreshBroadCast(){
        Runtime r = Runtime.getRuntime();
        try {
            r.exec("am broadcast -a " + WidgetUtils.WIDGET_REFRESH_ACTION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This is a wrapper around the new startForeground method, using the older
     * APIs if it is not available.
     */
    void startForegroundCompat(int id, Notification notification) {
		if (mSetForeground != null) {
            mSetForegroundArgs[0] = Boolean.TRUE;
            try {
                mSetForeground.invoke(this, mSetForegroundArgs);
            } catch (Exception e) {
            }
            return;
		}
		
		startForeground(1, notification);
    }

    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
    
    /**
     * This is a wrapper around the new stopForeground method, using the older
     * APIs if it is not available.
     */
    protected void stopForegroundCompat(int id) {
		if (mSetForeground != null) {
			mSetForegroundArgs[0] = Boolean.FALSE;
			try {
			    mSetForeground.invoke(this, mSetForegroundArgs);
			} catch (Exception e) {
			}
			return;
		}

		stopForeground(true);
    }

    protected void doForeground() {
		try {
			mSetForeground = getClass().getMethod("setForeground",
					mSetForegroundSignature);
		} catch (NoSuchMethodException e) {
			// Running on an older platform.
		    mSetForeground = null;
		}
        
        Notification notification = new Notification();
        notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
		notification.icon = R.drawable.city_position;
		
        try {
			startForegroundCompat(1, notification);
		} catch (Exception e) {
		}
    }

}
