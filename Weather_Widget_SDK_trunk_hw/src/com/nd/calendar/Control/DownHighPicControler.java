package com.nd.calendar.Control;

import java.io.File;

import android.content.Context;
import android.content.Intent;

import com.nd.calendar.util.FileHelp;
import com.nd.weather.widget.WidgetGlobal;
import com.nd.weather.widget.WidgetUtils;
import com.nd.weather.widget.UI.weather.UIWeatherFragmentAty;

/**
 * @ClassName: DownHighPicControler
 * @Description: 下载高清天气图标
 * @author yanyy
 * @date 2012-12-9 上午10:57:56
 * 
 */
public class DownHighPicControler extends DownloadControler {
	private final String DOWN_URL = "http://hltq.91.com/file/desktop/";
	private static DownHighPicControler _instance;

	private DownHighPicControler(Context ctx) {
		super(ctx);
		
		mUrl = DOWN_URL;
		mDir = FileHelp.getCalendarWipIconDir(mContext);
		mUseCalendarIco = true;
		
		// 默认添加顺序
		mStack.add("wip_cloudy");
		mStack.add("wip_cloudy_n");
		mStack.add("wip_fog");
		mStack.add("wip_fog_n");
		mStack.add("wip_sunny");
		mStack.add("wip_sunny_n");
		mStack.add("wip_icy");
		mStack.add("wip_overcast");
		mStack.add("wip_rain");
		mStack.add("wip_showers");
		mStack.add("wip_showers_n");
		mStack.add("wip_sleet");
		mStack.add("wip_snow");
		mStack.add("wip_snow_rain");
		mStack.add("wip_thunderstorm");
		mStack.add("wip_na");
	}

	public synchronized static DownHighPicControler getInstance(Context ctx) {
		if (_instance == null) {
			_instance = new DownHighPicControler(ctx);
		}
		return _instance;
	}

	@Override
	protected void finishDownload(String sPic, File file) {
		// 通知插件和界面刷新
		WidgetGlobal.updateWidgets(mContext, WidgetUtils.ACTION_INVALIDATE);
		Intent intent = new Intent(UIWeatherFragmentAty.ACTION_REFRESH_VIEW);
		mContext.sendBroadcast(intent);
		
		UIWeatherFragmentAty.setRefreshRetrunFromSubAty(true);
	}
}
