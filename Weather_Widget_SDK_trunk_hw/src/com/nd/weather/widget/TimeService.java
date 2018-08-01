package com.nd.weather.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.calendar.CommData.CityInfo;
import com.calendar.CommData.CityStruct;
import com.calendar.CommData.CityWeatherInfo;
import com.calendar.CommData.RealTimeWeatherInfo;
import com.nd.calendar.Control.CalendarContext;
import com.nd.calendar.Control.DownHighPicControler;
import com.nd.calendar.Control.ICalendarContext;
import com.nd.calendar.common.ComDataDef.ConfigSet;
import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.module.GpsSeverModule;
import com.nd.calendar.module.IGpsSeverModule;
import com.nd.calendar.module.WeatherModule;
import com.nd.calendar.module.gps.LocManager.Result;
import com.nd.calendar.module.gps.MyLocation;
import com.nd.calendar.module.gps.MyLocation.LocationResult;

public class TimeService extends WidgetBaseService
{
	private final static String TAG = "TimeService";

	final static String ACTION_TYPE = "action_type";
	final static int AT_UPDATE_FIRST_WEAHTER = 10;
	final static int AT_UPDATE_WEAHTER = 20;
	final static int AT_GET_LOCAL_CITY = 30;
	final static int AT_SET_FIRST_CITY = 40;

	final static String ACTION_PARAM_CITY_ID = "city_id";
	final static String ACTION_PARAM_CITY_CODE = "city_code";
	final static String ACTION_PARAM_FORCE = "city_force";
    final static String ACTION_PARAM_SKIN_PATH = "widget_skin";

	private IGpsSeverModule m_gpsSeverMdl = null;
	private WeatherModule weatherModle = null;
	private int mLastUpdateId = -1;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public final static void autoUpdateWeather(Context c, int id, String sCode, boolean bForce) {
		Intent iService = new Intent(c, TimeService.class);
		iService.putExtra(ACTION_TYPE, AT_UPDATE_WEAHTER);
		iService.putExtra(ACTION_PARAM_CITY_ID, id);
		iService.putExtra(ACTION_PARAM_CITY_CODE, sCode);
		iService.putExtra(ACTION_PARAM_FORCE, bForce);
		try {
			c.startService(iService);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final static void autoUpdateFirstWeather(Context c, boolean bForce) {
		Intent iService = new Intent(c, TimeService.class);
		iService.putExtra(ACTION_TYPE, AT_UPDATE_FIRST_WEAHTER);
		iService.putExtra(ACTION_PARAM_FORCE, bForce);

		try {
			c.startService(iService);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public final static void setFirstCityID(Context c, int id) {
		Intent iService = new Intent(c, TimeService.class);
		iService.putExtra(ACTION_TYPE, AT_SET_FIRST_CITY);
		iService.putExtra(ACTION_PARAM_CITY_ID, id);

		try {
			c.startService(iService);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public final static void localCtiy(Context c) {
		Intent iService = new Intent(c, TimeService.class);
		iService.putExtra(ACTION_TYPE, AT_GET_LOCAL_CITY);

		try {
			c.startService(iService);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	// //////////////////////////////////////////////////////////////////////////
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");

		doForeground();
		
		//下载高清图标
		DownHighPicControler.getInstance(this).startDown();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		doNewCmd(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		doNewCmd(intent);
	}

	void doNewCmd(Intent intent) {
		if (intent != null) {
			final int type = intent.getIntExtra(ACTION_TYPE, -1);
			if (type != -1) {
				switch (type) {
				case AT_UPDATE_FIRST_WEAHTER:
					SharedPreferences mSet = WidgetUtils.getSetting(getApplicationContext(), ConfigsetData.CONFIG_NAME);
					int firstId = mSet.getInt(ConfigSet.CONFIG_KEY_WIDGET_CITY_ID, -1);
					auotUpdateWeather("", firstId, intent.getBooleanExtra(ACTION_PARAM_FORCE, false));
					break;
					
				case AT_UPDATE_WEAHTER: // 天气更新
					int id = intent.getIntExtra(ACTION_PARAM_CITY_ID, -1);
					String code = intent.getStringExtra(ACTION_PARAM_CITY_CODE);
					if (id >= 0) {
						auotUpdateWeather(code, id, intent.getBooleanExtra(ACTION_PARAM_FORCE, false));
					}
				
					break;

				case AT_GET_LOCAL_CITY:
					getCityLocation();
					break;
					
				case AT_SET_FIRST_CITY: {
					int newId = intent.getIntExtra(ACTION_PARAM_CITY_ID, -1);
					if (newId != -1) {
						SharedPreferences cfgSet = WidgetUtils.getSetting(getApplicationContext(),
								ConfigsetData.CONFIG_NAME);
						cfgSet.edit().putInt(ConfigSet.CONFIG_KEY_WIDGET_CITY_ID, newId).commit();
						//切换城市后刷新小插件视图 caizp 2014-10-15
						WidgetUtils.sendBroadcast(getApplicationContext(), WidgetUtils.ACTION_CITY_SWITCH);
					}
				}
					break;
				}
			}
		}
	}

	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		stopForegroundCompat(1);
	}

	// ////////////////////////////////////////////////////////////////////
	class UpdateInfo
	{
		String code = "";
		int id;

		public UpdateInfo(String sCode, int iId) {
			code = sCode;
			id = iId;
		}
	}

	private Thread updateThread = null;
	private UpdateInfo[] mUpdateQueue = new UpdateInfo[2];

	final boolean isEqual(UpdateInfo info, String code, int id) {
		return (info != null && info.code.equals(code) && info.id == id);
	}

	/**
	 * @brief 【更新天气】
	 * @n<b>函数名称</b> :auotUpdateWeather
	 * @param code
	 * @param id
	 * @param bForce
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2012-10-26上午11:47:55
	 * @<b>修改时间</b> :
	 */
	void auotUpdateWeather(String code, final int id, boolean bForce) {
		Context context = getApplicationContext();
		
		if (weatherModle == null) {
			ICalendarContext calCtx = CalendarContext.getInstance(context);
			weatherModle = calCtx.Get_WeatherMdl_Interface();
		}

		boolean bExist = false;

		if (HttpToolKit.isNetworkAvailable(context)) {
			if (TextUtils.isEmpty(code)) {
				code = weatherModle.getCityCodeByID(id);
				if (TextUtils.isEmpty(code)) {
					sendWeatherToPandaHome(null, null);
					return;
				}
			}
			
			mLastUpdateId = id;
			
			synchronized (mUpdateQueue) {
				boolean inRun = isEqual(mUpdateQueue[0], code, id); // 正在运行的
				boolean inQueue = isEqual(mUpdateQueue[1], code, id); // 队列中的，只能有一个城市请求
				bExist = inRun || inQueue;

				if (bForce) {
					if (inQueue) { // 若是在队列中的，撤销
						mUpdateQueue[1] = null;
					}
				} else {
					if (!bExist) {
						// 正在运行的有城市，则排在队列中
						int pos = (mUpdateQueue[0] == null ? 0 : 1);
						mUpdateQueue[pos] = new UpdateInfo(code, id);
					}
				}
			}

			if (bForce) {
				final String sCode = code;
				new Thread() {
					@Override
					public void run() {
						updateWeather(sCode, id, true);
					}
				}.start();

				return;
			}

			if (!bExist && (updateThread == null || !updateThread.isAlive())) {

				updateThread = new Thread() {
					@Override
					public void run() {
						while (!Thread.interrupted()) {
							UpdateInfo item;

							synchronized (mUpdateQueue) {
								item = mUpdateQueue[0];
								if (item == null) {
									break;
								}
							}

							updateWeather(item.code, item.id, false);

							synchronized (mUpdateQueue) {
								mUpdateQueue[0] = mUpdateQueue[1];
								mUpdateQueue[1] = null;
							}
						}

						mUpdateQueue[0] = null;
						mUpdateQueue[1] = null;
						updateThread = null;
					}
				};

				updateThread.start();
			}
		} else {
//			String sName = weatherModle.getCityNameByID(id);
//			if (TextUtils.isEmpty(sName)) {
//				sendWeatherToPandaHome(null, null);
//				return;
//			}
//			mLastUpdateId = id;
//			Log.d(TAG, "send weather no net");
//			// 发送
//			sendWeatherToPandaHome(sName, null);
			CityWeatherInfo cityWeatherInfo = new CityWeatherInfo(CityWeatherInfo.TYPE_PANDAHOME_NOTIFY);
			weatherModle.GetWeatherInfo(code, id, false, cityWeatherInfo);
			Log.d(TAG, "send weather success");
			// 通知91桌面
			sendWeatherToPandaHome(cityWeatherInfo.getCityName(), cityWeatherInfo);
		}
	}

	/**
	 * @brief 【发送天气数据给91桌面】
	 * @n<b>函数名称</b>     :sendWeatherToPandaHome
	 * @param cityWeatherInfo
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-3-8下午06:09:12
	 * @<b>修改时间</b>      :
	*/
	final void sendWeatherToPandaHome(String sCityName, CityWeatherInfo cityWeatherInfo) {
		try {
			Intent intent = new Intent(WidgetUtils.WIDGET_NOTIFY_PANDAHOME_ACTION);
			
			if (cityWeatherInfo != null) {
				intent.putExtra(WidgetUtils.WNP_CITY_NAME, cityWeatherInfo.getCityName());
				
				RealTimeWeatherInfo rtw = cityWeatherInfo.getRealTimeWeather();
				if (rtw != null) {
					String sWeather = rtw.getNowWeather();
					String sWeatherKey = WeatherModule.GetFinalWeathResString(sWeather, cityWeatherInfo.isNight());
					intent.putExtra(WidgetUtils.WNP_NOW_TEMP, rtw.getTempValue());
					intent.putExtra(WidgetUtils.WNP_WEATHER_NAME, sWeather);
					intent.putExtra(WidgetUtils.WNP_WEATHER_KEY, sWeatherKey);
				}
			} else {
				intent.putExtra(WidgetUtils.WNP_CITY_NAME, sCityName);
			}

			intent.addFlags(32);
			sendBroadcast(intent);
		} catch (Exception e) {
		}
	}
	
	final void updateWeather(String code, int id, boolean force) {
		try {
			CityWeatherInfo cityWeatherInfo = new CityWeatherInfo(CityWeatherInfo.TYPE_PANDAHOME_NOTIFY);
			
			weatherModle.GetWeatherInfo(code, id, force, cityWeatherInfo);

			// 仅当数据从网络返回时刷新
//			if (nRes == WeatherModule.WM_SUCCESS || nRes == WeatherModule.WM_WEATHER_INCOMPLETE) {
				WidgetGlobal.updateWidgets(getApplicationContext(),
						WidgetUtils.ACTION_UPDATE_WEATHER);
				
				Log.d(TAG, "send weather success");
				
				// 通知91桌面
				sendWeatherToPandaHome(cityWeatherInfo.getCityName(), cityWeatherInfo);
//			} 
//			else if (nRes != WeatherModule.WM_NO_REFRESH) {
//				
//				Log.d(TAG, "send weather error");
//				
//				// 发送现有的数据
//				//cityWeatherInfo.setRealTimeWeather(null);
//				sendWeatherToPandaHome(cityWeatherInfo.getCityName(), cityWeatherInfo);
//			}
//			else if (nRes != WeatherModule.WM_NO_REFRESH) {
//				Log.d(TAG, "ACTION_UPDATE_WEATHER_FAILURE");
//				WidgetGlobal.updateWidgets(getApplicationContext(),
//						WidgetUtils.ACTION_UPDATE_WEATHER_FAILURE);
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////////////

	private boolean bInGps = false;

	private void getCityLocation() {
		if (bInGps) {
			return;
		}

		if (!HttpToolKit.isNetworkAvailable(getApplicationContext())) {
			return;
		}
		
		if (m_gpsSeverMdl == null) {
			m_gpsSeverMdl = new GpsSeverModule(getApplicationContext());
		}

		bInGps = true;

		if (m_gpsSeverMdl.GetGpsOpenState()) {
			MyLocation l = new MyLocation();
			l.getLocation(getApplicationContext(), new LocationResult() {

				@Override
				public void gotLocation(final Result location) {
					getLocalCity(location);
				}
			});
		} else {
			bInGps = false;
		}
	}

	/**
	 * @brief 【获得城市代码】
	 * @n<b>函数名称</b>     :getLocalCity
	 * @param location
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-1-23下午03:07:00
	 * @<b>修改时间</b>      :
	*/
	void getLocalCity(final Result location) {

		// 去服务器取城市代码
		new Thread() {
			@Override
			public void run() {
				CityStruct cityStruct = new CityStruct();
				boolean result = false;
				
				if (location != null) {
					result = m_gpsSeverMdl.GetGpsInfoFromServer(location.latitude,
							location.longitude, cityStruct);
				}
				 
				// 定位失败，返回北京
				if (!result || TextUtils.isEmpty(cityStruct.getCode())
						|| TextUtils.isEmpty(cityStruct.getName())) {
					cityStruct.setName("北京");
					cityStruct.setCode("101010100");
				}
				
				// 成功
				CityInfo cityInfo = new CityInfo();
				cityInfo.setCode(cityStruct.getCode());
				cityInfo.setName(cityStruct.getName());
				cityInfo.setFromGps(1);

				Context context = getApplicationContext();
				ICalendarContext mCalCxt = CalendarContext.getInstance(context);
				mCalCxt.Get_UserMdl_Interface().SetCityInfo(cityInfo, true);

				WidgetGlobal.updateWidgets(context, WidgetUtils.ACTION_UPDATE_WEATHER);
				auotUpdateWeather(cityInfo.getCode(), cityInfo.getId(), false);
				
				// 通知91桌面
				// ...
				
				m_gpsSeverMdl.SetGpsOpenState(false);
				
				bInGps = false;
			}
		}.start();
	}
}
