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
 * @Description: 下载新版天气图标
 * @author 陈希
 * @date 2012-12-9 上午10:57:56
 * 
 */
public class GetWeatherIcoControler extends DownloadControler {
	private final String DOWN_URL = "http://hltq.91.com/file/desktop/weather_icon_2/";
	private static GetWeatherIcoControler _instance;

	private GetWeatherIcoControler(Context ctx) {
		super(ctx);
		
		mUrl = DOWN_URL;
		mDir = FileHelp.getCalendarNewWipIconDir(mContext);

		// 默认添加顺序
		mStack.add("wip_sunny");
		mStack.add("wip_sunny_n");
		mStack.add("wip_cloudy");
		mStack.add("wip_cloudy_n");
		mStack.add("wip_heavy_rain");
		mStack.add("wip_lightrain");
		mStack.add("wip_overcast");
		mStack.add("wip_rain");
		mStack.add("wip_thunderstorm");
		mStack.add("wip_showers");
		mStack.add("wip_showers_n");
		mStack.add("wip_sleet");
		mStack.add("wip_snow");
		mStack.add("wip_heavy_snow");
		mStack.add("wip_icy");
		mStack.add("wip_snow_rain");
		mStack.add("wip_storm");
		mStack.add("wip_chance_of_snow");
		mStack.add("wip_chance_of_snow_n");
		mStack.add("wip_na");
		mStack.add("wip_dust");
		mStack.add("wip_dust_n");
		mStack.add("wip_fog");
		mStack.add("wip_fog_n");
	}

	public synchronized static GetWeatherIcoControler getInstance(Context ctx) {
		if (_instance == null) {
			_instance = new GetWeatherIcoControler(ctx);
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
