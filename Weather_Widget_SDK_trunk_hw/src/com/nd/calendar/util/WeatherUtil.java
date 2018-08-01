package com.nd.calendar.util;

import com.calendar.CommData.CityWeatherInfo;
import com.calendar.CommData.DayWeatherInfo.DayInfo;
import com.nd.calendar.Control.CalendarContext;
import com.nd.calendar.Control.ICalendarContext;
import com.nd.calendar.module.WeatherModule;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.weather.widget.WeatherLinkTools;

public class WeatherUtil {
	
	/**
	 * <br>Description: 获取当前城市天气数据信息
	 * <br>Author:caizp
	 * <br>Date:2016年5月16日下午7:09:48
	 * @return
	 */
	public static CommonWeatherInfo getCityWeatherInfo() {
		try {
			ICalendarContext mCalCxt = CalendarContext.getInstance(BaseConfig.getApplicationContext());
			WeatherModule weatherModle = mCalCxt.Get_WeatherMdl_Interface();
			WeatherLinkTools wlt = WeatherLinkTools.getInstance(BaseConfig.getApplicationContext());
			CityWeatherInfo cityWeather = new CityWeatherInfo(CityWeatherInfo.TYPE_WIDGET);
			int id = wlt.getFirstCityID();
			if (weatherModle.getCityWeatherWidget(id, cityWeather)) {
				CommonWeatherInfo info = new CommonWeatherInfo();
				// 当前城市
				info.cityName = cityWeather.getCityName();
				// 当天天气和当天温度
				DayInfo day = cityWeather.getWeatherInfo().getDays().get(1);
				if (day != null) {
					info.weather = day.info;
					info.temperatureRange = day.temperature;
				}
				info.weatherIconResId = WeatherModule.GetFinalWeath64ResId(day.info);
				return info;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static class CommonWeatherInfo {
		public String cityName;
		public String weather;
		public String temperatureRange;
		public int weatherIconResId;		
	}
	
}
