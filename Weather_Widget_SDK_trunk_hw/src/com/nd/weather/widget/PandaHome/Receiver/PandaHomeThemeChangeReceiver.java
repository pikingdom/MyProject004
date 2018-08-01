/**   
 *    
 * @file 【91桌面端主题更换接收器】
 * @brief
 *
 * @<b>文件名</b>      : PandaHomeThemeChangeReceiver
 *@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2012-7-13 下午05:22:57 
 * @n@n<b>修改时间</b>: 2014-10-14 下午04:18:20
 * @n@n<b>文件描述</b>:  
 * @n@n<b>修改描述</b>: 增加随主题换字体
 * @version  
 */
package com.nd.weather.widget.PandaHome.Receiver;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.nd.weather.widget.R;
import com.nd.weather.widget.WidgetGlobal;
import com.nd.weather.widget.WidgetUtils;
import com.nd.weather.widget.PandaHome.PandaThemeManager;
import com.nd.weather.widget.PandaHome.PandaWidgetView;
import com.nd.weather.widget.skin.WidgetSkinBuilder;

public class PandaHomeThemeChangeReceiver extends BroadcastReceiver
{
	private final static String TAG = "WidgetThemeChangeReceiver";

	@Override
	public void onReceive(final Context context, Intent intent) {
		if(null == intent || null == intent.getAction())return;
		if (intent.hasExtra("fontStylePath")) {
			String str = intent.getStringExtra("fontStylePath");
			try {
				Log.d("WidgetThemeChangeReceiver", "font ok");
				if (WidgetGlobal.setWidgetFont(context, str)) {//保存插件字体，刷新插件
					synchronized (WidgetSkinBuilder.isFontChanged) {
						WidgetSkinBuilder.isFontChanged = Boolean.valueOf(true);
					}
					PandaWidgetView.updateWidgets(context, WidgetUtils.ACTION_SKIN_CHANGED);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String pkg = intent.getStringExtra("packageName");
		if ((context.getString(R.string.thenme_change_action).equals(intent.getAction()))
				&& ((pkg) == null) || (context
						.getPackageName().equals(pkg))) {
			// 优先处理 weatherSkinPath
			final String themePath = intent.getStringExtra("weatherSkinPath");
			if (!TextUtils.isEmpty(themePath)) {
				try {
					File file = new File(themePath);
					if (file.exists()) {
						Log.d(TAG, "theme ok");
						WidgetGlobal.setWidgetSkin(context, themePath, true);
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			final String themeId = intent.getStringExtra("themeid");
			//Log.d(TAG, "change themeid = " + themeId);

			if (!TextUtils.isEmpty(themeId)) {
				new Thread() {
					@Override
					public void run() {

						Context ctx = context.getApplicationContext();
						if(null == ctx) ctx = context;
						String skinPath = PandaThemeManager.getSkinPathFromId(ctx, themeId);

						///Log.d(TAG, "get theme Path = " + skinPath);

						// 默认皮肤
						if (TextUtils.isEmpty(skinPath)) {
							Log.d(TAG, "default theme");
							WidgetGlobal.setWidgetSkin(ctx, WidgetGlobal.getDefaultSkinPath(ctx), false);
						} else {
							Log.d(TAG, "theme ok");
							try {
								File file = new File(skinPath);
								if (file.exists()) {
									WidgetGlobal.setWidgetSkin(ctx, skinPath, true);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				}.start();
			}
		}
		
	}
}
