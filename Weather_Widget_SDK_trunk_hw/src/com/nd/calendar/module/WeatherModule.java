/**   
 *    
 * @file 【天气模块】
 * @brief
 *
 * @<b>文件名</b>      : WeatherModule
 *@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2012-1-19 下午03:11:12 
 * @n@n<b>文件描述</b>:  
 * @version  
 */
package com.nd.calendar.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.calendar.CommData.CityWeatherInfo;
import com.calendar.CommData.CityWeatherJson;
import com.calendar.CommData.DayWeatherInfo;
import com.calendar.CommData.RealTimeWeatherInfo;
import com.calendar.CommData.SunInfo;
import com.calendar.CommData.WarningInfo;
import com.nd.calendar.Control.DownHighPicControler;
import com.nd.calendar.Control.DownloadControler;
import com.nd.calendar.Control.GetWeatherIcoControler;
import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.communication.http.HttpAppFunClient;
import com.nd.calendar.dbrepoist.IDatabaseRef;
import com.nd.calendar.dbrepoist.IUserInfo;
import com.nd.calendar.dbrepoist.UserInfo;
import com.nd.calendar.util.ComfunHelp;
import com.nd.calendar.util.FileHelp;
import com.nd.calendar.util.StringHelp;
import com.nd.weather.widget.R;
import com.nd.weather.widget.WeatherLinkTools;
import com.nd.weather.widget.WidgetUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherModule {
	public final static String TAG = WeatherModule.class.getSimpleName();

	// 默认日出日落时间
	private static Date SUN_BEGIN = new Date(0, 0, 1, 5, 30);

	private static Date SUN_END = new Date(0, 0, 1, 18, 30);

	private static final String[] WEATHER_ICO_KEY = { "晴", "多云", "阴", "阵雨",
			"雷阵雨", "雷阵雨伴有冰雹", "雨夹雪", "小雨", "中雨", "大雨", "暴雨", "大暴雨", "特大暴雨",
			"阵雪", "小雪", "中雪", "大雪", "暴雪", "雾", "冻雨", "沙尘暴", "小到中雨", "中到大雨",
			"大到暴雨", "暴雨到大暴雨", "大暴雨到特大暴雨", "小到中雪", "中到大雪", "大到暴雪", "小雪到中雪",
			"中雪到大雪", "大雪到暴雪", "浮尘", "扬沙", "强沙尘暴", "大雾", "霾" };

	// private static final int WEATHER_UNKNOWN_RES = R.drawable.wip_na;
	public static final String WEATHER_UNKNOWN_RES = "wip_na";
	public static final int WEATHER_UNKNOWN_RES_64 = R.drawable.wip_na_64;
	private static final String WEATHER_UNKNOWN_STRING = "unknown.png";
	private static final int WEATHER_UNKNOWN_COLOR = 0xffbfa3c6;

	// 以下图标是精简版
	
	private static final int [] WEATHER_ICO_RES_64 = {
		R.drawable.wip_sunny_64, 			// 晴天 
		R.drawable.wip_cloudy_64, 			// 多云 
		R.drawable.wip_overcast_64, 		// 阴
		  
		R.drawable.wip_showers_64,			// 阵雨
		R.drawable.wip_thunderstorm_64, 	// 雷阵雨
		R.drawable.wip_sleet_64,			// 雷阵雨伴有冰雹
		R.drawable.wip_snow_rain_64, 		// 雨夹雪
		  
		R.drawable.wip_lightrain_64, 		// 小雨
		R.drawable.wip_rain_64, 			// 中雨
		R.drawable.wip_heavy_rain_64,		// 大雨
		R.drawable.wip_heavy_rain_64, 		// 暴雨
		R.drawable.wip_storm_64, 			// 大暴雨 
		R.drawable.wip_storm_64, 			// 特大暴雨
		  
		R.drawable.wip_chance_of_snow_64, 	// 阵雪 
		R.drawable.wip_snow_64, 			// 小雪 
		R.drawable.wip_snow_64, 			// 中雪
		R.drawable.wip_heavy_snow_64, 		// 大雪
		R.drawable.wip_heavy_snow_64, 		// 暴雪
		  
		R.drawable.wip_fog_64, // 雾
		R.drawable.wip_icy_64, // 冻雨
		R.drawable.wip_dust_64, // 沙尘暴
		  
		R.drawable.wip_rain_64, 			// 小雨到中雨
		R.drawable.wip_heavy_rain_64, 		// 中雨到大雨
		R.drawable.wip_heavy_rain_64, 		// 大雨到暴雨
		R.drawable.wip_storm_64, 			// 暴雨到大暴雨
		R.drawable.wip_storm_64, 			// 大暴雨到特大暴雨
		  
		R.drawable.wip_snow_64,				// 小到中雪
		R.drawable.wip_heavy_snow_64,		// 中到大雪 
		R.drawable.wip_heavy_snow_64,		// 大到暴雪 
		R.drawable.wip_snow_64, 			// 小雪到中雪
		R.drawable.wip_heavy_snow_64, 		// 中雪到大雪
		R.drawable.wip_heavy_snow_64, 		// 大雪到暴雪
		  
		R.drawable.wip_dust_64, // 浮尘
		R.drawable.wip_dust_64, // 扬沙
		R.drawable.wip_dust_64, // 强沙尘暴
		R.drawable.wip_dust_64, // 大雾
		R.drawable.wip_dust_64, // 霾
	};

	private static final String[][] WEATHER_ICO_RES = {
			{ "wip_sunny", "wip_sunny_n" }, // 晴天
			{ "wip_cloudy", "wip_cloudy_n" }, // 多云
			{ "wip_overcast", "wip_overcast" }, // 阴

			{ "wip_showers", "wip_showers_n" }, // 阵雨
			{ "wip_thunderstorm", "wip_thunderstorm" }, // 雷阵雨
			{ "wip_sleet", "wip_sleet" }, // 雷阵雨伴有冰雹
			{ "wip_snow_rain", "wip_snow_rain" }, // 雨夹雪
			{ "wip_rain", "wip_rain" }, // 小雨
			{ "wip_rain", "wip_rain" }, // 中雨
			{ "wip_rain", "wip_rain" }, // 大雨
			{ "wip_rain", "wip_rain" }, // 暴雨
			{ "wip_rain", "wip_rain" }, // 大暴雨
			{ "wip_rain", "wip_rain" }, // 特大暴雨

			{ "wip_snow", "wip_snow" }, // 阵雪
			{ "wip_snow", "wip_snow" }, // 小雪
			{ "wip_snow", "wip_snow" }, // 中雪
			{ "wip_snow", "wip_snow" }, // 大雪
			{ "wip_snow", "wip_snow" }, // 暴雪

			{ "wip_fog", "wip_fog_n" }, // 雾
			{ "wip_icy", "wip_icy" }, 	// 冻雨
			{ "wip_fog", "wip_fog_n" }, // 沙尘暴

			{ "wip_rain", "wip_rain" }, // 小雨到中雨
			{ "wip_rain", "wip_rain" }, // 中雨到大雨
			{ "wip_rain", "wip_rain" }, // 大雨到暴雨
			{ "wip_rain", "wip_rain" }, // 暴雨到大暴雨
			{ "wip_rain", "wip_rain" }, // 大暴雨到特大暴雨

			{ "wip_snow", "wip_snow" }, // 小到中雪
			{ "wip_snow", "wip_snow" }, // 中到大雪
			{ "wip_snow", "wip_snow" }, // 大到暴雪
			{ "wip_snow", "wip_snow" }, // 小雪到中雪
			{ "wip_snow", "wip_snow" }, // 中雪到大雪
			{ "wip_snow", "wip_snow" }, // 大雪到暴雪

			{ "wip_fog", "wip_fog_n" }, // 浮尘
			{ "wip_fog", "wip_fog_n" }, // 扬沙
			{ "wip_fog", "wip_fog_n" }, // 强沙尘暴
			{ "wip_fog", "wip_fog_n" },	// 大雾
			{ "wip_fog", "wip_fog_n" },	// 霾
	};

	private static final String[][] WEATHER_NEW_ICO_RES = {
		{"wip_sunny", "wip_sunny_n"}, 			// 晴天 
		{"wip_cloudy", "wip_cloudy_n"}, 		// 多云 
		{"wip_overcast", "wip_overcast"}, 		// 阴
		  
		{"wip_showers", "wip_showers_n"},		// 阵雨
		{"wip_thunderstorm", "wip_thunderstorm"}, // 雷阵雨
		{"wip_sleet", "wip_sleet"},				// 雷阵雨伴有冰雹
		{"wip_snow_rain", "wip_snow_rain"}, 	// 雨夹雪
		  
		{"wip_lightrain", "wip_lightrain"}, 	// 小雨
		{"wip_rain", "wip_rain"}, 				// 中雨
		{"wip_heavy_rain", "wip_heavy_rain"},	// 大雨
		{"wip_heavy_rain", "wip_heavy_rain"}, 	// 暴雨
		{"wip_storm", "wip_storm"}, 			// 大暴雨 
		{"wip_storm", "wip_storm"}, 			// 特大暴雨
		  
		{"wip_chance_of_snow", "wip_chance_of_snow_n"}, // 阵雪 
		{"wip_snow", "wip_snow"}, 				// 小雪 
		{"wip_snow", "wip_snow"}, 				// 中雪
		{"wip_heavy_snow", "wip_heavy_snow"}, 	// 大雪
		{"wip_heavy_snow", "wip_heavy_snow"}, 	// 暴雪
		
		{"wip_fog", "wip_fog_n"}, 				// 雾
		{"wip_icy", "wip_icy"},					// 冻雨
		{"wip_dust", "wip_dust_n"}, 			// 沙尘暴
		  
		{"wip_rain", "wip_rain"}, 				// 小雨到中雨
		{"wip_heavy_rain", "wip_heavy_rain"}, 	// 中雨到大雨
		{"wip_heavy_rain", "wip_heavy_rain"}, 	// 大雨到暴雨
		{"wip_storm", "wip_storm"}, 			// 暴雨到大暴雨
		{"wip_storm", "wip_storm"}, 			// 大暴雨到特大暴雨
		  
		{"wip_snow", "wip_snow"},				// 小到中雪
		{"wip_heavy_snow", "wip_heavy_snow"},	// 中到大雪 
		{"wip_heavy_snow", "wip_heavy_snow"},	// 大到暴雪 
		{"wip_snow", "wip_snow"}, 				// 小雪到中雪
		{"wip_heavy_snow", "wip_heavy_snow"}, 	// 中雪到大雪
		{"wip_heavy_snow", "wip_heavy_snow"}, 	// 大雪到暴雪
		  
		{"wip_dust", "wip_dust_n"}, // 浮尘
		{"wip_dust", "wip_dust_n"}, // 扬沙
		{"wip_dust", "wip_dust_n"}, // 强沙尘暴
		{"wip_dust", "wip_dust_n"}, // 大雾
		{"wip_dust", "wip_dust_n"}, // 霾
	};
	
	private static final String[][] WEATHER_ICO_STRING = {
			{ "sunny", "sunny_n" }, // 晴天
			{ "cloudy", "cloudy_n" }, // 多云
			{ "overcast", "overcast" }, // 阴

			{ "showers", "showers_n" }, // 阵雨
			{ "thunderstorm", "thunderstorm" }, // 雷阵雨
			{ "sleet", "sleet" }, // 雷阵雨伴有冰雹
			{ "snow_rain", "snow_rain" }, // 雨夹雪

			{ "cn_lightrain", "cn_lightrain" }, // 小雨
			{ "rain", "rain" }, // 中雨
			{ "cn_heavyrain", "cn_heavyrain" }, // 大雨
			{ "cn_heavyrain", "cn_heavyrain" }, // 暴雨
			{ "storm", "storm" }, // 大暴雨
			{ "storm", "storm" }, // 特大暴雨

			{ "chance_of_snow", "chance_of_snow_n" }, // 阵雪
			{ "snow", "snow" }, // 小雪
			{ "snow", "snow" }, // 中雪
			{ "heavysnow", "heavysnow" }, // 大雪
			{ "heavysnow", "heavysnow" }, // 暴雪

			{ "fog", "fog_n" }, // 雾
			{ "icy", "icy" }, // 冻雨
			{ "dust", "dust_n" }, // 沙尘暴

			{ "rain", "rain" }, // 小雨到中雨
			{ "cn_heavyrain", "cn_heavyrain" }, // 中雨到大雨
			{ "cn_heavyrain", "cn_heavyrain" }, // 大雨到暴雨
			{ "storm", "storm" }, // 暴雨到大暴雨
			{ "storm", "storm" }, // 大暴雨到特大暴雨

			{ "snow", "snow" }, // 小到中雪
			{ "heavysnow", "heavysnow" }, // 中到大雪
			{ "heavysnow", "heavysnow" }, // 大到暴雪
			{ "snow", "snow" }, // 小雪到中雪
			{ "heavysnow", "heavysnow" }, // 中雪到大雪
			{ "heavysnow", "heavysnow" }, // 大雪到暴雪

			{ "dust", "dust_n" }, // 浮尘
			{ "dust", "dust_n" }, // 扬沙
			{ "dust", "dust_n" }, // 强沙尘暴
			{ "dust", "dust_n" }, // 大雾
			{ "dust", "dust_n" }, // 霾
	};

	/**
	 * 天气详情界面背景颜色 caizp 2014-10-14
	 */
	private static final int[][] WEATHER_COLORS = {
		{ 0xFF578cbc, 0xFF766bae }, // 晴天
		{ 0xFFe4a058, 0xFF766bae }, // 多云
		{ 0xFFe4a058, 0xFFe4a058 }, // 阴

		{ 0xFFfd9d84, 0xFF766bae }, // 阵雨
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 雷阵雨
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 雷阵雨伴有冰雹
		{ 0xFF46b39e, 0xFF46b39e }, // 雨夹雪

		{ 0xFFfd9d84, 0xFFfd9d84 }, // 小雨
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 中雨
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 大雨
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 暴雨
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 大暴雨
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 特大暴雨

		{ 0xFF46b39e, 0xFF766bae }, // 阵雪
		{ 0xFF46b39e, 0xFF46b39e }, // 小雪
		{ 0xFF46b39e, 0xFF46b39e }, // 中雪
		{ 0xFF46b39e, 0xFF46b39e }, // 大雪
		{ 0xFF46b39e, 0xFF46b39e }, // 暴雪

		{ 0xFF909e9e, 0xFF766bae }, // 雾
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 冻雨
		{ 0xFF909e9e, 0xFF909e9e }, // 沙尘暴

		{ 0xFFfd9d84, 0xFFfd9d84 }, // 小雨到中雨
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 中雨到大雨
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 大雨到暴雨
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 暴雨到大暴雨
		{ 0xFFfd9d84, 0xFFfd9d84 }, // 大暴雨到特大暴雨

		{ 0xFF46b39e, 0xFF46b39e }, // 小到中雪
		{ 0xFF46b39e, 0xFF46b39e }, // 中到大雪
		{ 0xFF46b39e, 0xFF46b39e }, // 大到暴雪
		{ 0xFF46b39e, 0xFF46b39e }, // 小雪到中雪
		{ 0xFF46b39e, 0xFF46b39e }, // 中雪到大雪
		{ 0xFF46b39e, 0xFF46b39e }, // 大雪到暴雪

		{ 0xFF909e9e, 0xFF909e9e }, // 浮尘
		{ 0xFF909e9e, 0xFF909e9e }, // 扬沙
		{ 0xFF909e9e, 0xFF909e9e }, // 强沙尘暴
		{ 0xFF909e9e, 0xFF766bae }, // 大雾
		{ 0xFF909e9e, 0xFF909e9e }, // 霾
	};
	
	// 应用层网络访问对象
	private HttpAppFunClient m_AppFunClient = null;
	// 数据库层接口
	private IUserInfo m_dbUserInfo = null;

	private Context mContext = null;

	public final static int WM_SUCCESS = 1;
	public final static int WM_FAILURE = 0;
	public final static int WM_PARAM_ERROR = -1;
	public final static int WM_UNKNOWN_ERROR = -2;
	public final static int WM_WEATHER_CHECKCODE_ERROR = -3;
	public final static int WM_NO_REFRESH = -5; // this code means no need to
												// refresh weather from
	// net
	public final static int WM_WEATHER_INCOMPLETE = -6;

	public final static int TYPE_ALL_DATA = 0;
	public final static int TYPE_WEATHER = 1;
	public final static int TYPE_NOW = 2;
	public final static int TYPE_INDEX = 3;
	public final static int TYPE_WARNING = 4;
	public final static int TYPE_SUN = 5;
	public final static int TYPE_PM = 6;
	public final static int TYPE_EXT = 7;

	public WeatherModule(Context context, IDatabaseRef dataBaseRef) {
		mContext = context.getApplicationContext();
		if (mContext == null) {
			mContext = context;
		}
		m_AppFunClient = new HttpAppFunClient(mContext);
		m_dbUserInfo = UserInfo.getInstance(mContext, dataBaseRef);
	}

	/**
	 * @brief 【判断成功装态】
	 * 
	 * @n<b>函数名称</b> :isSuccess
	 * @param iRet
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-19下午04:00:43
	 */
	public static boolean isSuccess(int iRet) {
		return (iRet == WM_SUCCESS || iRet == WM_NO_REFRESH || iRet == WM_WEATHER_INCOMPLETE);
	}

	// ////////////////////////////////////////// 新版本
	// ///////////////////////////////////////////////
	// 添加新功能，要在downloadData() 和 isToRefreshWeather() 中注册相关的函数
	//
	/**
	 * @brief 【获取天气数据】
	 * 
	 * @n<b>函数名称</b> :GetWeatherInfo
	 * @param cityWeather
	 * @param bFroceRefresh
	 * @return int
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-12下午02:59:05
	 */
	public int GetWeatherInfo(CityWeatherInfo cityWeather, boolean bFroceRefresh) {
		String code = cityWeather.getCityCode();
		int id = cityWeather.getId();

		return GetWeatherInfo(code, id, bFroceRefresh, cityWeather);
	}

	// cityWeather 可为 null，只更新数据库数据
	public int GetWeatherInfo(String code, int id, boolean bFroceRefresh,
			CityWeatherInfo cityWeather) {
		StringBuffer strResOut = new StringBuffer();
		int iRet = getWeatherData(code, id, TYPE_ALL_DATA, bFroceRefresh,
				strResOut);

		// 记录城市ID对应上一次天气数据请求时间
		Date sysdate = new Date(System.currentTimeMillis());
		String strTime = ComfunHelp.formatDateTime(sysdate);
		SharedPreferences mSet = WidgetUtils.getSetting(mContext, ConfigsetData.CONFIG_NAME);
		mSet.edit().putString("last_update_time_"+id, strTime).commit();

		if (iRet == WM_SUCCESS) {

			// 1.获得网络数据
			boolean bFromOut = true;
			JSONObject jsonObject = StringHelp.getJSONObject(strResOut
					.toString());

			if (jsonObject == null) {
				return WM_FAILURE;
			}

			String sWeather = null;
			String sNowWeather = null;
			String strIndex = null;
			String sWarning = null;
			String strSun = null;
			String sPMIndex = null;

			try {
				sWeather = jsonObject.getString("weatherinfo");
				sNowWeather = jsonObject.getString("now");
				bFromOut = !jsonObject.has("tqzs"); // 从外网下载的数据中没有tqzs字段

				if (!bFromOut) { // 外网地址上无以下数据
					strIndex = jsonObject.getString("tqzs");
					strSun = jsonObject.getString("sun");
					sWarning = jsonObject.getString("vectqyj");
					sPMIndex = jsonObject.getString("pm");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return WM_UNKNOWN_ERROR;
			}

			// 2.从数据库中查询
			CityWeatherJson cityJsonDB = new CityWeatherJson();
			if (!m_dbUserInfo.getCityWeatherJsonById(mContext,
					Integer.toString(id), cityJsonDB)) {
				// 本地数据库没找到，说明被删除了就返回
				return WM_FAILURE;
			}

			// 3.检验新数据，写入数据库
			boolean bDayDataGood = DayWeatherInfo.checkData(sWeather);
			boolean bNowDataGood = RealTimeWeatherInfo.checkData(sNowWeather);

			if (bNowDataGood) {
				cityJsonDB.setNowWeatherTime(strTime);
				cityJsonDB.setNowWeatherJson(sNowWeather);
			}

			if (bDayDataGood) {
				cityJsonDB.setDayWeatherTime(strTime);
				cityJsonDB.setDayWeatherJson(sWeather);
			}

			if (!bFromOut) {
				cityJsonDB.setIndexTime(strTime);
				cityJsonDB.setWarnTime(strTime);
				cityJsonDB.setSunTime(strTime);
				cityJsonDB.setPMTime(strTime);

				cityJsonDB.setIndexJson(strIndex);
				cityJsonDB.setSunJson(strSun);
				cityJsonDB.setWarningJson(sWarning);
				cityJsonDB.setPMJson(sPMIndex);
			}

			// 更新数据
			m_dbUserInfo.updateWeatherInfo(mContext, cityJsonDB);

			if (cityWeather != null) {
				cityWeather.setFromBackup(bFromOut);
				cityWeather.setCityJson(cityJsonDB);
			}

			if (bNowDataGood && bDayDataGood) {
				return bFromOut ? WM_WEATHER_INCOMPLETE : WM_SUCCESS;
			} else {
				return WM_FAILURE;
			}
		} else {// 从数据库中查询
			CityWeatherJson cityJsonDB = new CityWeatherJson();
			if (!m_dbUserInfo.getCityWeatherJsonById(mContext,
					Integer.toString(id), cityJsonDB)) {
				// 本地数据库没找到，说明被删除了就返回
				return WM_FAILURE;
			}
			if (cityWeather != null) {
				cityWeather.setCityJson(cityJsonDB);
				return WM_SUCCESS;
			}
		}

		return iRet;
	}

	/**
	 * @brief 【获取所有天气】
	 * 
	 * @n<b>函数名称</b> :getCityWeatherList
	 * @param listweather
	 * @return
	 * @<b>作者</b> : yyy
	 * @<b>创建时间</b> : 2012-5-11下午02:46:52
	 */

	public boolean getCityWeatherList(final List<CityWeatherInfo> listweather) {
		// 获取城市天气数据，并把数据解析成界面可以直接使用的数据
		listweather.clear();
		final ArrayList<CityWeatherJson> ls = new ArrayList<CityWeatherJson>();
		boolean breturn = m_dbUserInfo.getCityWeatherList(mContext, ls);
		if (breturn) {
			final int size = ls.size();
			for (int i = 0; i < size; i++) {
				final CityWeatherJson l = ls.get(i);
				CityWeatherInfo o = new CityWeatherInfo();
				o.setCityJson(l);
				listweather.add(o);
			}
			ls.clear();
			//System.gc();
			breturn = true;
		}
		return breturn;
	}

	/**
	 * @brief 【返回城市个数】
	 * @n<b>函数名称</b> :getCityCount
	 * @return
	 * @return int
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2012-8-30上午09:57:34
	 * @<b>修改时间</b> :
	 */
	public int getCityCount() {
		return m_dbUserInfo.GetCityCount(mContext);
	}

	/**
	 * @Title: getCityWeatherById
	 * @Description: TODO(根据ID获取城市信息)
	 * @author yanyy
	 * @date 2012-6-12 上午10:51:20
	 * 
	 * @param id
	 * @param cityWeather
	 * @return
	 * @return boolean
	 * @throws
	 */
	public boolean getCityWeatherById(int id, CityWeatherInfo cityWeather) {
		CityWeatherJson city = new CityWeatherJson();
		boolean breturn = m_dbUserInfo.getCityWeatherJsonById(mContext,
				Integer.toString(id), city);
		if (breturn) {
			cityWeather.setCityJson(city);
		}
		return breturn;
	}

	/**
	 * @brief 【桌面插件获取城市数据】
	 * @n<b>函数名称</b> :getCityWeatherWidget
	 * @param id
	 * @param cityWeather
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2012-6-14上午11:24:26
	 * @<b>修改时间</b> :
	 */
	public boolean getCityWeatherWidget(int id, CityWeatherInfo cityWeather) {
		CityWeatherJson city = new CityWeatherJson();
		boolean breturn = m_dbUserInfo.getCityWeatherJsonWidget(mContext, id, city);
		if (breturn) {
			cityWeather.setCityJson(city);
		}
		return breturn;
	}

	public int getNextCityID(int id) {
		return m_dbUserInfo.getNextCityID(mContext, id);
	}

	// /**
	// * @brief 【获取指数扩展数据】
	// * @n<b>函数名称</b> :getExtInfo
	// * @param indexInfos
	// * @param sunInfo
	// * @param pmIndex
	// * @param bFroceRefresh
	// * @return
	// * @return int
	// * @<b>作者</b> : 陈希
	// * @<b>修改</b> :
	// * @<b>创建时间</b> : 2012-6-1上午09:51:15
	// * @<b>修改时间</b> :
	// */
	// public int getExtInfo(IndexInfos indexInfos, SunInfo sunInfo,
	// PMIndex pmIndex, boolean bFroceRefresh) {
	// if (indexInfos == null || sunInfo == null || pmIndex == null) {
	// return WM_PARAM_ERROR;
	// }
	//
	// int id = indexInfos.getId();
	// String sCode = indexInfos.getCode();
	// CityWeatherJson cityJson = new CityWeatherJson();
	// cityJson.setId(id);
	// cityJson.setCode(sCode);
	//
	// sunInfo.setId(id);
	// pmIndex.setId(id);
	// pmIndex.setCode(sCode);
	//
	// StringBuffer sResOut = new StringBuffer();
	// int iRet = getWeatherData(sCode, id, TYPE_EXT, bFroceRefresh, sResOut);
	//
	// if (iRet == WM_SUCCESS) {
	//
	// // 1.获得指数json
	// JSONObject jsonObject = StringHelp.getJSONObject(sResOut.toString());
	//
	// if (jsonObject != null){
	//
	// // 2.写入数据库
	// String sIndex = null;
	// String sSun = null;
	// String sWarning = null;
	// String sPM = null;
	// Date sysdate = new Date(System.currentTimeMillis());
	//
	// try {
	// sIndex = jsonObject.getString("tqzs");
	// sSun = jsonObject.getString("sun");
	// sPM = jsonObject.getString("pm");
	// sWarning = jsonObject.getString("tqyj");
	//
	// String sTime = ComfunHelp.formatDateTime(sysdate);
	// cityJson.setSunTime(sTime);
	// cityJson.setIndexTime(sTime);
	// cityJson.setPMTime(sTime);
	// cityJson.setWarnTime(sTime);
	//
	// cityJson.setIndexJson(sIndex);
	// cityJson.setSunJson(sSun);
	// cityJson.setPMJson(sPM);
	// cityJson.setWarningJson(sWarning);
	//
	// m_dbUserInfo.updateWeatherAuto(cityJson);
	//
	// // 3.保存成功后，返回数据
	// indexInfos.setJsonString(sIndex);
	// sunInfo.setJsonString(sSun);
	// pmIndex.setJsonString(sPM);
	// } catch (Exception e) {
	// e.printStackTrace();
	// iRet = WM_UNKNOWN_ERROR;
	// }
	//
	// } else {
	// iRet = WM_FAILURE;
	// }
	// }
	//
	// // *.出错/无变化，读取保存的数据
	// if (iRet == WM_NO_REFRESH || iRet != WM_SUCCESS) {
	// if (m_dbUserInfo.getCityWeatherJsonById(Integer.toString(id), cityJson))
	// {
	// String indexJson = cityJson.getIndexJson();
	// if (!TextUtils.isEmpty(indexJson)) {
	// indexInfos.setJsonString(indexJson);
	// sunInfo.setJsonString(cityJson.getSunJson());
	// pmIndex.setJsonString(cityJson.getPMJson());
	// iRet = WM_NO_REFRESH;
	// }
	// } else {
	// iRet = WM_FAILURE;
	// }
	// }
	//
	// return iRet;
	// }

	/**
	 * @brief 【从服务器获取预警】
	 * 
	 * @n<b>函数名称</b> :GetWeatherExtInfo
	 * @param listInfo
	 * @param bForceRefresh
	 * @param bReadCache
	 * @return
	 * @return int
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-1-16下午07:56:52
	 */

	public int GetWarningInfo(CityWeatherInfo cityWeather, boolean bFroceRefresh) {
		StringBuffer strResOut = new StringBuffer();
		final String code = cityWeather.getCityCode();
		final int id = cityWeather.getId();

		int iRet = getWeatherData(code, id, TYPE_WARNING, bFroceRefresh,
				strResOut);

		if (iRet == WM_SUCCESS) {
			JSONObject jsonObject = StringHelp.getJSONObject(strResOut
					.toString());
			if (jsonObject == null) {
				return WM_FAILURE;
			}
			String strInfo = null;
			Date sysdate = new Date(System.currentTimeMillis());

			try {
				strInfo = jsonObject.getString("vectqyj");
			} catch (Exception e) {
				e.printStackTrace();
				return WM_UNKNOWN_ERROR;
			}

			CityWeatherJson cityJson = new CityWeatherJson();
			cityJson.setId(id);
			cityJson.setWarnTime(ComfunHelp.formatDateTime(sysdate));
			cityJson.setWarningJson(strInfo);

			m_dbUserInfo.updateWeatherAuto(mContext, cityJson);

			/* 更新到缓存 */
			WarningInfo w = null;
			if (!TextUtils.isEmpty(strInfo)) {
				w = new WarningInfo();
				if (!w.setJsonArrayFirst(strInfo)) {
					w = null;
				} else {
					w.setCity(cityWeather.getCityCode());
				}
			}
			cityWeather.setWarningInfo(w);
		}

		return iRet;
	}

	// /**
	// * @brief 【从服务器获取生活指数】
	// *
	// * @n<b>函数名称</b> :GetWeatherExtInfo
	// * @param listInfo
	// * @param bForceRefresh
	// * @param bReadCache
	// * @return
	// * @return int
	// * @<b>作者</b> : 陈希
	// * @<b>创建时间</b> : 2012-1-16下午07:56:52
	// */
	//
	// public int GetIndexLivingInfo(IndexInfos indexInfos, boolean
	// bForceRefresh) {
	// if (indexInfos == null) {
	// return WM_PARAM_ERROR;
	// }
	//
	// int id = indexInfos.getId();
	// String sCode = indexInfos.getCode();
	// CityWeatherJson cityJson = new CityWeatherJson();
	// cityJson.setId(id);
	// cityJson.setCode(sCode);
	//
	// StringBuffer sResOut = new StringBuffer();
	// int iRet = getWeatherData(sCode, id, TYPE_INDEX, bForceRefresh, sResOut);
	//
	// if (iRet == WM_SUCCESS) {
	//
	// // 1.获得指数json
	// JSONObject jsonObject = StringHelp.getJSONObject(sResOut.toString());
	// if (jsonObject != null){
	//
	// // 2.写入数据库
	// String sIndex = null;
	// String sSun = null;
	// Date sysdate = new Date(System.currentTimeMillis());
	//
	// try {
	// sIndex = jsonObject.getString("tqzs");
	// sSun = jsonObject.getString("sun");
	//
	// String sTime = ComfunHelp.formatDateTime(sysdate);
	// if (sTime == null) {
	// throw new Exception();
	// }
	// cityJson.setSunTime(sTime);
	// cityJson.setIndexTime(sTime);
	//
	// cityJson.setIndexJson(sIndex);
	// cityJson.setSunJson(sSun);
	//
	// m_dbUserInfo.updateWeatherAuto(cityJson);
	//
	// // 3.保存成功后，返回数据
	// indexInfos.setJsonString(sIndex);
	// } catch (Exception e) {
	// e.printStackTrace();
	// iRet = WM_UNKNOWN_ERROR;
	// }
	//
	// } else {
	// iRet = WM_FAILURE;
	// }
	// }
	//
	// // *.出错/无变化，读取保存的数据
	// if (iRet == WM_NO_REFRESH || iRet != WM_SUCCESS) {
	// if (m_dbUserInfo.getCityWeatherJsonById(Integer.toString(id), cityJson))
	// {
	// indexInfos.setJsonString(cityJson.getIndexJson());
	// iRet = WM_NO_REFRESH;
	// } else {
	// iRet = WM_FAILURE;
	// }
	// }
	//
	// return iRet;
	// }

	/**
	 * @brief 【获得太阳数据】
	 * 
	 * @n<b>函数名称</b> :GetSunInfo
	 * @param cityCode
	 * @param id
	 * @param sunInfo
	 * @param bForceRefresh
	 * @return
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-5-11上午10:22:17
	 */
//	public int GetSunInfo(String cityCode, SunInfo sunInfo,
//			boolean bForceRefresh) {
//		StringBuffer strResOut = new StringBuffer();
//		int id = sunInfo.getId();
//		CityWeatherJson cityJson = new CityWeatherJson();
//		cityJson.setId(id);
//		cityJson.setCode(cityCode);
//
//		int iRet = getWeatherData(cityCode, id, TYPE_SUN, bForceRefresh,
//				strResOut);
//
//		if (iRet == WM_SUCCESS) {
//			JSONObject jsonObject = StringHelp.getJSONObject(strResOut
//					.toString());
//
//			if (jsonObject != null) {
//
//				Date sysdate = new Date(System.currentTimeMillis());
//
//				try {
//					String sSun = jsonObject.getString("sun");
//					String sTime = ComfunHelp.formatDateTime(sysdate);
//					if (sTime == null) {
//						throw new Exception();
//					}
//					cityJson.setSunTime(sTime);
//					cityJson.setSunJson(sSun);
//
//					m_dbUserInfo.updateWeatherAuto(mContext, cityJson);
//
//					// 3.保存成功后，返回数据
//					sunInfo.setJsonString(sSun);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//					iRet = WM_UNKNOWN_ERROR;
//				}
//			} else {
//				iRet = WM_FAILURE;
//			}
//		}
//
//		// *.出错/无变化，读取保存的数据
//		if (iRet == WM_NO_REFRESH || iRet != WM_SUCCESS) {
//			if (m_dbUserInfo.getCityWeatherJsonById(mContext,
//					Integer.toString(id), cityJson)) {
//				sunInfo.setJsonString(cityJson.getSunJson());
//				iRet = WM_NO_REFRESH;
//			} else {
//				iRet = WM_FAILURE;
//			}
//		}
//
//		return iRet;
//	}

	/**
	 * @brief 【获取PM指数】
	 * @n<b>函数名称</b> :GetPMIndexInfo
	 * @param pmIndex
	 * @param bForceRefresh
	 * @return
	 * @return int
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2012-5-31下午07:46:36
	 * @<b>修改时间</b> :
	 */
//	public int GetPMIndexInfo(PMIndex pmIndex, boolean bForceRefresh) {
//		StringBuffer strResOut = new StringBuffer();
//		int id = pmIndex.getId();
//		String sCode = pmIndex.getCode();
//
//		CityWeatherJson cityJson = new CityWeatherJson();
//		cityJson.setId(id);
//		cityJson.setCode(sCode);
//
//		int iRet = getWeatherData(sCode, id, TYPE_PM, bForceRefresh, strResOut);
//
//		if (iRet == WM_SUCCESS) {
//			JSONObject jsonObject = StringHelp.getJSONObject(strResOut
//					.toString());
//
//			if (jsonObject != null) {
//
//				Date sysdate = new Date(System.currentTimeMillis());
//
//				try {
//					String sSun = jsonObject.getString("pm");
//					String sTime = ComfunHelp.formatDateTime(sysdate);
//					if (sTime == null) {
//						throw new Exception();
//					}
//
//					cityJson.setPMTime(sTime);
//					cityJson.setPMJson(sSun);
//
//					m_dbUserInfo.updateWeatherAuto(mContext, cityJson);
//
//					// 3.保存成功后，返回数据
//					pmIndex.setJsonString(sSun);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//					iRet = WM_UNKNOWN_ERROR;
//				}
//			} else {
//				iRet = WM_FAILURE;
//			}
//		}
//
//		// *.出错/无变化，读取保存的数据
//		if (iRet == WM_NO_REFRESH || iRet != WM_SUCCESS) {
//			if (m_dbUserInfo.getCityWeatherJsonById(mContext,
//					Integer.toString(id), cityJson)) {
//				pmIndex.setJsonString(cityJson.getPMJson());
//				iRet = WM_NO_REFRESH;
//			} else {
//				iRet = WM_FAILURE;
//			}
//		}
//
//		return iRet;
//	}

	// /**
	// * @brief 【获取指数(数据库)】
	// * @n<b>函数名称</b> :getIndexLivingInfo
	// * @param id
	// * @return
	// * @return IndexLivingInfo[]
	// */
	// public IndexLivingInfo[] getIndexLivingInfo(int id) {
	// // 先从数据库中查询
	// CityWeatherJson cityJson = new CityWeatherJson();
	// m_dbUserInfo.getCityWeatherJsonById(Integer.toString(id), cityJson);
	//
	// // 判断是否是今天的
	// //if (ComfunHelp.isToday(cityJson.getIndexTime())) {
	// final String json = cityJson.getIndexJson();
	// if (!TextUtils.isEmpty(json)) {
	// IndexInfos indexInfos = new IndexInfos();
	// indexInfos.setJsonString(json);
	// return indexInfos.getIndexInfoList();
	// }
	// //}
	// return null;
	// }

	// /////////////////////////// 天气函数 ////////////////////////////////

	/**
	 * 判断是否需要更新天气数据
	 * @param id
	 * @param sysdate
	 * @param iType
	 * @return
	 */
	public boolean isToRefreshWeather(int id, Date sysdate, int iType) {
		// 读取是否开启自动更新,默认是开启自动更新
		SharedPreferences set = mContext.getSharedPreferences(ConfigHelper.PREF_NAME,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		
		boolean bAutoUpdate = set.getBoolean(ConfigsetData.CONFIG_NAME_KEY_AUTOUPDATE, true);
		if (!bAutoUpdate) {
			return false;
		}
		
		return checkRefresh(id, sysdate, iType);
	}

	/**
	 * @brief 【下载数据】
	 * 
	 * @n<b>函数名称</b> :downloadData
	 * @param iType
	 * @param sCode
	 * @param sResOut
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-11下午05:17:44
	 */
	private boolean downloadData(int iType, String sCode, StringBuffer sResOut) {
		switch (iType) {
		case TYPE_ALL_DATA:
			boolean bRet = m_AppFunClient.DownloadWeahterInfo(sCode, sResOut);
			// JSONObject js = StringHelp.getJSONObject(sResOut.toString());
			// try {
			// Log.d(TAG, "weather: " + js.getString("pm"));
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			return bRet;

			// case TYPE_WEATHER:
			// break;
			// case TYPE_NOW:
			// break;
		case TYPE_INDEX:
			return m_AppFunClient.DownloadIndexLiving(sCode, sResOut);
		case TYPE_WARNING:
			return m_AppFunClient.DownloadWarning(sCode, sResOut);
		case TYPE_SUN:
			return m_AppFunClient.DownloadSun(sCode, sResOut);
		case TYPE_PM:
			return m_AppFunClient.DownloadPM(sCode, sResOut);
		case TYPE_EXT:
			return m_AppFunClient.DownloadIndexExt(sCode, sResOut);
		default:
			break;
		}

		return false;
	}

	/**
	 * @brief 【判断指定城市是否要更新】
	 * @n<b>函数名称</b> :checkRefresh
	 * @param id
	 * @param sysdate
	 * @param iType
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2012-5-11上午10:29:22
	 * @<b>修改时间</b> :
	 */
	private boolean checkRefresh(int id, Date sysdate, int iType) {
		String strSaveTime = null;
		float fRefTime = -1.0f; // -1，使用用户设定
		int type = UserInfo.TYPE_NOW;

		switch (iType) {
		case TYPE_WEATHER:
			type = UserInfo.TYPE_WEATHER;
			break;
		// case TYPE_ALL_DATA: // 获取全部数据，时间依照实时
		// case TYPE_NOW:
		// type = UserInfo.TYPE_WEATHER;
		// break;
		case TYPE_EXT:
		case TYPE_INDEX:
			type = UserInfo.TYPE_INDEX;
			break;
		case TYPE_WARNING:
			type = UserInfo.TYPE_WARNING;
//			fRefTime = 0.5f;
			break;
		case TYPE_SUN:
			type = UserInfo.TYPE_SUN;
			break;
		case TYPE_PM:
			type = UserInfo.TYPE_PM;
			break;
		default:
			break;
		}

//		strSaveTime = m_dbUserInfo.getLastTimeByType(mContext,
//				Integer.toString(id), type);
		SharedPreferences mSet = WidgetUtils.getSetting(mContext, ConfigsetData.CONFIG_NAME);
		strSaveTime = mSet.getString("last_update_time_"+id, "");
		if (strSaveTime == null || strSaveTime.length() < 1) {
			return true;
		}

		Date saveTime = ComfunHelp.GetDate(strSaveTime, "yyyy-MM-dd HH:mm:ss",
				false);

		if (fRefTime < 0.0f) {
			// 读取更新间隔
			SharedPreferences set = mContext.getSharedPreferences(ConfigHelper.PREF_NAME,
					Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);

			fRefTime = set.getFloat(ConfigsetData.CONFIG_NAME_KEY_TIEMUPDATE, 1.0f);
		}

		try {
			long diffTime = Math.abs(sysdate.getTime() - saveTime.getTime());

			if ((diffTime > fRefTime * 60 * 60 * 1000)/* && IsInDateTime()*/) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();

			return false;//IsInDateTime();
		}
	}

	public boolean isNeedReadCache(int id, String lastReadTime, int iType) {
		final String strSaveTime = m_dbUserInfo.getLastTimeByType(mContext,
				Integer.toString(id), iType);

		// 如果上次更新的和数据库不一样说明需要重新读取
		return ((TextUtils.isEmpty(strSaveTime)) || (!strSaveTime
				.equals(lastReadTime)));
	}

	/**
	 * @brief 【获取天气数据】
	 * 
	 * @n<b>函数名称</b> :getWeatherData
	 * @param iDataType
	 * @param strResOut
	 * @param listInfo
	 * @param bForceRefresh 是否强制更新天气数据
	 * @param bReadCache
	 * @return
	 * @return int
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-11下午04:37:21
	 */
	private int getWeatherData(String cityCode, int id, int iDataType,
			boolean bForceRefresh, StringBuffer strResOut) {
		if (TextUtils.isEmpty(cityCode) || id == 0) {
			return WM_PARAM_ERROR;
		}

		Date sysdate = new Date(System.currentTimeMillis());

		if (bForceRefresh || isToRefreshWeather(id, sysdate, iDataType)) {

			if (downloadData(iDataType, cityCode, strResOut)) {
				return WM_SUCCESS;
			} else {
				if (strResOut != null && isCheckCodeError(strResOut.toString())) {
					return WM_WEATHER_CHECKCODE_ERROR;
				}
			}
		} else {
			return WM_NO_REFRESH;
		}

		return WM_FAILURE;
	}

	/**
	 * @brief 【检查服务端返回状态】
	 * 
	 * @n<b>函数名称</b> :checkMsg
	 * @param sJson
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-5上午10:36:07
	 */
	boolean isCheckCodeError(String sJson) {
		try {
			JSONObject jsonObject = StringHelp.getJSONObject(sJson);
			String sMsg = jsonObject.getString("msg");

			if (sMsg != null && sMsg.equals("参数错误")) {
				return true;
			}
		} catch (Exception e1) {
			e1.printStackTrace();

			return false;
		}

		return false;
	}

	/**
	 * @brief 【根据时间段去判断是否要更新】
	 * 
	 * 
	 * @n<b>函数名称</b> :IsInDateTime
	 * @see
	 * 
	 * @param @return
	 * @return boolean
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2012-2-7下午04:09:31
	 */
	public boolean IsInDateTime() {
		long nowDate = System.currentTimeMillis();
		Date beginTime = new Date(nowDate);
		SharedPreferences set = mContext.getSharedPreferences(ConfigHelper.PREF_NAME,
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		
		int nBeginHour = set.getInt(
				ConfigsetData.CONFIG_NAME_KEY_BEGINGTIEMEH,
				ConfigsetData.CONFIG_DEFAULT_BEGINGTIEMEH);
		int nBeginMinute = set.getInt(
				ConfigsetData.CONFIG_NAME_KEY_BEGINGTIEMEM,
				ConfigsetData.CONFIG_DEFAULT_BEGINGTIEMEM);
		beginTime.setHours(nBeginHour);
		beginTime.setMinutes(nBeginMinute);
		beginTime.setSeconds(0);

		Date endTime = new Date(nowDate);
		int nEndHour = set.getInt(
				ConfigsetData.CONFIG_NAME_KEY_ENDTIEMEH,
				ConfigsetData.CONFIG_DEFAULT_ENDTIEMEH);
		int nEndMinute = set.getInt(
				ConfigsetData.CONFIG_NAME_KEY_ENDTIEMEM,
				ConfigsetData.CONFIG_DEFAULT_ENDTIEMEM);
		endTime.setHours(nEndHour);
		endTime.setMinutes(nEndMinute);
		endTime.setSeconds(0);

		if (beginTime.getTime() <= nowDate && nowDate <= endTime.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	// /////////////////////////// 天气图标 ////////////////////////////////
	/**
	 * @brief 【根据天气数据，判断是否是日落时间】
	 * 
	 * @n<b>函数名称</b> :isNightTime
	 * @param listInfo
	 * @param nowDateInfo
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-11上午10:41:16
	 */
	// public static boolean isNightTime(ListInfo listInfo, DateInfo
	// nowDateInfo) {
	// return isNightTime(listInfo.getSunInfo());
	// }

	/* 判断是否日落时间 */
	public static boolean isNightTime(SunInfo sunInfo) {
		if (sunInfo == null) {
			return false;
		}

		// final Calendar c = Calendar.getInstance();
		Date sunrise = sunInfo.getSunrise();
		Date sunset = sunInfo.getSunset();
		// 把手机时间换算成 城市的当地时间
		final Date nowDate = ComfunHelp.getLocalCityDate(sunInfo.getGMT());
		nowDate.setYear(0);
		nowDate.setMonth(0);
		nowDate.setDate(1);
		// 增加判断是否是今天的数据(不是今天数据就按默认处理)
		if ((sunrise != null) && (sunset != null) && (sunInfo.isToday())) {
			return !(nowDate.before(sunset) && nowDate.after(sunrise));
		} else {
			return !(nowDate.before(SUN_END) && nowDate.after(SUN_BEGIN));
		}
	}

	/* 判断是否晚上 */
	public static boolean isOnlyNightTime(SunInfo sunInfo) {
		if (sunInfo == null) {
			return false;
		}

		Date sunset = sunInfo.getSunset();
		// 把手机时间换算成 城市的当地时间
		final Date nowDate = ComfunHelp.getLocalCityDate(sunInfo.getGMT());
		nowDate.setYear(0);
		nowDate.setMonth(0);
		nowDate.setDate(1);
		// 判断是否是今天的数据(不是今天数据就按默认处理)
		if ((sunset != null) && (sunInfo.isToday())) {
			return !nowDate.before(sunset);
		} else {
			return !nowDate.before(SUN_END);
		}
	}

	static int getWeatherIcoIndex(String sWeatehr) {
		if (!TextUtils.isEmpty(sWeatehr)) {

			String[] sInfos = sWeatehr.split("转");
			if (sInfos != null && sInfos.length > 1) {
				sWeatehr = sInfos[0];
			}

			int i = 0;
			for (; i < WEATHER_ICO_KEY.length; i++) {
				if (WEATHER_ICO_KEY[i].equals(sWeatehr)) {
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * @brief 【返回天气图标文件名】
	 * @n<b>函数名称</b> :GetFinalWeathResString
	 * @param strIn
	 * @param bNight
	 * @return
	 * @return String
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2012-9-10下午04:24:56
	 * @<b>修改时间</b> :
	 */
	public static String GetFinalWeathResString(String strIn, boolean bNight) {
		// 判断是否有差的到ID
		int nId = getWeatherIcoIndex(strIn);
		if (nId == -1) {
			return WEATHER_UNKNOWN_STRING;
		}
		// / Log.d(TAG, "ico = " + strIn + " id= " + nId);
		return WEATHER_ICO_STRING[nId][bNight ? 1 : 0] + ".png";
	}

	public static String GetFinalWeathResId(String strIn, boolean bNight, boolean bComplete) {
		// 判断是否有差的到ID
		int nId = getWeatherIcoIndex(strIn);
		if (nId == -1) {
			return WEATHER_UNKNOWN_RES;
		}
		// Log.d(TAG, "ico = " + strIn + " id= " + nId);
		if (bComplete) {
			return WEATHER_NEW_ICO_RES[nId][bNight ? 1 : 0];
		} else {
			return WEATHER_ICO_RES[nId][bNight ? 1 : 0];
		}
	}

	public static int GetFinalWeath64ResId(String strIn) {
		// 判断是否有差的到ID
		int nId = getWeatherIcoIndex(strIn);
		if (nId == -1) {
			return WEATHER_UNKNOWN_RES_64;
		}
		
		return WEATHER_ICO_RES_64[nId];
	}
	
	public static int GetFinalWeathColor(String strIn, boolean bNight) {
		// 判断是否有差的到ID
		int nId = getWeatherIcoIndex(strIn);
		if (nId == -1) {
			return WEATHER_UNKNOWN_COLOR;
		}
		// Log.d(TAG, "ico = " + strIn + " id= " + nId);
		return WEATHER_COLORS[nId][bNight ? 1 : 0];
	}
	/**
	 * @Title: getWipDrawable
	 * @Description:获取天气图标
	 * @author yanyy
	 * @date 2012-12-9 上午11:37:06
	 * 
	 * @param ctx
	 * @param resName
	 * @return
	 * @return Drawable
	 * @throws
	 */
	public static Drawable getWipDrawable(Context ctx, String resName, boolean bNetDownloading) {
		Drawable db = null;
		if (!TextUtils.isEmpty(resName)) {
			try {
				// 判断是否有高清的图标
				File f = new File(FileHelp.getCalendarNewWipIconDir(ctx), resName + ".a");
				if (f.exists()) {
					try {
						db = Drawable.createFromPath(f.getAbsolutePath());
					} catch (Exception e) {
						// 不是有效图片资源，删除
						f.delete();
					}
				} else {
					f = new File(FileHelp.getCalendarNewWipIconDir(ctx), resName + ".png");
					if (f.exists()) {
						try {
							db = Drawable.createFromPath(f.getAbsolutePath());
						} catch (Exception e) {
							// 不是有效图片资源，删除
							f.delete();
						}
					} else {
						// 添加到下载队列
						GetWeatherIcoControler.getInstance(ctx).addTopTask(resName);
					}
				}
			} catch (Exception e) {
			}
			
			if (db == null) {
				// 资源取
				int iresId;// = ComfunHelp.getResIdByName(ctx, resName);
//				if (iresId <= 0) {
					if (bNetDownloading) {
						iresId = R.drawable.wip_downing;
					} else {
						iresId = R.drawable.wip_app_na;
					}
//				}
				
				// 获取资源图片
				InputStream is = ctx.getResources().openRawResource(iresId);
				try {
					db = Drawable.createFromStream(is, null);
				} finally {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
		}
		
		return db;
	}

	/**
	 * @Title: getWipInputStream
	 * @Description: 获取天气图标
	 * @author yanyy
	 * @date 2012-12-9 上午11:51:13
	 * 
	 * @param ctx
	 * @param resName
	 * @return
	 * @return InputStream
	 * @throws
	 */
	public static InputStream getWipInputStream(Context ctx, String resName, int icoVer, boolean bDownloading) {
		InputStream is = null;
		if (!TextUtils.isEmpty(resName)) {
			try {
				File fileDir = null;
				DownloadControler downloadControler = null;
				
				if (icoVer == 1) {	// 新版
					fileDir = FileHelp.getCalendarNewWipIconDir(ctx);
					downloadControler = GetWeatherIcoControler.getInstance(ctx);
				} else {
					// 
					WeatherLinkTools wlt = WeatherLinkTools.getInstance(ctx);
					if (wlt.canLink()) {
						is = wlt.getCalendarRes(resName);
					}
					
					if (is == null) {
						fileDir = FileHelp.getCalendarWipIconDir(ctx);
						downloadControler = DownHighPicControler.getInstance(ctx);
					}
				}
				
				if (fileDir != null) {
					// 判断是否有高清的图标
					File f = new File(fileDir, resName + ".a");
					if (f.exists()) {
						is = new FileInputStream(f);
					} else {
						f = new File(fileDir, resName + ".png");
						if (f.exists()) {
							is = new FileInputStream(f);
						} else {
							// 添加到下载队列
							downloadControler.addTopTask(resName);
						}
					}
				}

			} catch (Exception e) {
			}

			if (is == null) {
				// 资源取
				int iresId;// = ComfunHelp.getResIdByName(ctx, resName);
//				if (iresId <= 0) {
					if (bDownloading) {
						iresId = R.drawable.wip_na_update;
					} else {
						iresId = R.drawable.wip_app_na;
					}
//				}
				
				is = ctx.getResources().openRawResource(iresId);
			}
		}
		
		return is;
	}

	public String getCityCodeByID(int id) {
		return m_dbUserInfo.getCityCodeByID(mContext, id);
	}

	public String getCityNameByID(int id) {
		return m_dbUserInfo.getCityNameByID(mContext, id);
	}
	
	// /**
	// * @Title: hasBadData
	// * @Description: TODO(判断是否有坏数据)
	// * @author yanyy
	// * @date 2012-5-24 下午04:12:07
	// *
	// * @param list
	// * @return
	// * @return boolean
	// * @throws
	// */
	// public boolean hasBadData(ArrayList<CityInfo> list){
	// return m_dbUserInfo.hasBadData(list);
	// }
}
