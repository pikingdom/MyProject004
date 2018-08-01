/**   
*    
* @file 【主版天气链接包】
* @brief 当发现安装主版之后，切换天气数据及设置链接
*
* @<b>文件名</b>      : CalendarWeatherTools
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2013-8-5 下午5:52:22 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.weather.widget;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.nd.calendar.common.ComDataDef;
import com.nd.calendar.common.ComDataDef.ConfigSet;
import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.provider.CalendarDatas;
import com.nd.calendar.provider.CalendarDatas.CityDataColumns;
import com.nd.calendar.util.ComfunHelp;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.weather.widget.UI.weather.UIWeatherFragmentAty;

// 注意:使用WeatherLinkTools要在同一个进程！

public class WeatherLinkTools
{
	private static WeatherLinkTools _instance = null;
	
	public static class WeatherUpdateTime {
		public int nBeginHour;
		public int nBeginMinute;
		
		public int nEndHour;
		public int nEndMinute;
	}

	// Claendar 定义
	private final static String CALENDAR_SERVICE = "com.calendar.Widget.TimeService";
	private final static String ACTION_TYPE = "action_type";
	private final static int AT_GET_LOCAL_CITY = 30;
	private final static int AT_CITY_SWITCH = 50;
	private final static int AT_UPDATE_CITY_WEATHER = 130;
	private final static int AT_WIDGET_STATE = 140;
	
	private final static String ACTION_PARAM_CITY_ID = "city_id";
	private final static String ACTION_PARAM_FORCE = "city_force";
	private final static String ACTION_PARAM_WIDGET_ID = "id";
	private final static String ACTION_PARAM_WIDGET_TYPE = "type";
	private final static String ACTION_PARAM_WIDGET_STATE = "state";
    
	private static final String WIDGET_SETTING = "widgeFileName";
	private static final String WIDGET_SETTING_PANDA_SKIN_WITH_THEME = "widget_panda_skin_with_theme";
	
	//private static final String[] WIDGET_CFG_KEY = { "panda_widget_4x1_ids", "panda_widget_4x2_ids" };
	
	private static final int flags = (Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);

    // 更新广播
    public static final String ACTION_UPDATE_WEATHER = "com.calendar.action.UPDATE_WEATHER";
    public static final String PARAM_ID = "id";
    public static final String PARAM_STATE = "state";
    
	public final static String WIDGET_REFRESH_ACTION = "com.calendar.appwidget.refresh";
	public final static String REFRESH_TYPE = "ref_action";
	

    
	public final static int ACTION_CITY_SWITCH = 5;		// 城市切换

    /*状态*/
    public static final int STATE_NORMAL = 0;
    public static final int STATE_POSTION_ING = 1;
    public static final int STATE_POSTION_FAIL = 2;
    public static final int STATE_UPDATE_ING = 3;
    public static final int STATE_UPDATE_FAIL = 4;
    public static final int STATE_UPDATE_SUCCESS = 5; 
    
    private final static String ACT_TAG = "show_";
	// 万年历界面
    private final static String ACT_SHOW_CALENDAR = ACT_TAG + "calendar";
    private final static String ACT_SHOW_WEATHER = ACT_TAG + "weather";
    
    public static final String WIDGET_CALENDAR_SKIN = "skin1/";
    public static final String WIDGET_PANDA_DEFAULT_SKIN = "skin2/";
    
    //////////////////////////////////////////////////////////////////////////
	private Context mContext = null;
	
	private Context mCtxSetting = null;
	private Context mCtxCalendar = null;
	private String mCfgPrefName = ConfigHelper.PREF_NAME;
	private String mSkinSettingName = WidgetGlobal.WIDGET_SKIN_SETTING;
	private Method mMtdUpdate = null;
	private int mCalVer = -1;
	private boolean bLinked = false;
	
	public final static WeatherLinkTools getInstance(Context context) {
		if (_instance == null) {
			_instance = new WeatherLinkTools();
			if(null != context) {
				_instance.mContext = context.getApplicationContext();
			}
			if(null == _instance.mContext) {
				_instance.mContext = context;
			}
			_instance.mCtxSetting = _instance.mContext;
		}
		
		return _instance;
	}
	
	/**
	 * @brief 【获得主版链接】
	 * @n<b>函数名称</b>     :getCalendarContext
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-8-6下午5:32:44
	 * @<b>修改时间</b>      :
	*/
	public Context getCalendarContext(boolean bCode) {
		if(android.os.Build.VERSION.SDK_INT >= 21 || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
			restoreSelf();
			return mCtxCalendar;
    	}
		// 黄历天气 2.3版本以后才为可替换版本
		if ((mCalVer = ComfunHelp.getAppVersion(mContext, ComDataDef.CALENDAR_PACKAGE))
				>= ComDataDef.CALENDAR_VAR_CODE_2_3_0) {
			
			Context ctxCal = null;
			try {
				ctxCal = mContext.createPackageContext(ComDataDef.CALENDAR_PACKAGE,
							( ((bCode && !canLink_2_7_0())? Context.CONTEXT_INCLUDE_CODE: 0)	// 2.7.0以后不使用代码
							  | Context.CONTEXT_IGNORE_SECURITY) );
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 2.5.0版本后可链接
			if (ctxCal != null && (!bLinked || ctxCal != mCtxCalendar) && canLink()) {
				bLinked = true;
				mCtxCalendar = ctxCal;
				mCtxSetting = mCtxCalendar;
				mCfgPrefName = WIDGET_SETTING;
				mSkinSettingName = WIDGET_SETTING;
				CityDataColumns.SWITCH_TO_CALENDAR_READ_URI = true;
				CityDataColumns.SWITCH_TO_CALENDAR_MGR_URI = (mCalVer < ComDataDef.CALENDAR_VAR_CODE_2_7_0);
			}
		} else if (mCtxCalendar != null) {
			restoreSelf();
		}
		
		return mCtxCalendar;
	}

	/**
	 * @brief 【恢复自身数据接口】
	 * @n<b>函数名称</b>     :restoreSelf
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-8-6下午5:33:04
	 * @<b>修改时间</b>      :
	*/
	public void restoreSelf() {
		if (bLinked) {
			bLinked = false;
			mCtxSetting = mContext;
			mCtxCalendar = null;
			mCalVer = -1;
			
			mCfgPrefName = ConfigHelper.PREF_NAME;
			mSkinSettingName = WidgetGlobal.WIDGET_SKIN_SETTING;
			mMtdUpdate = null;
			
			CityDataColumns.SWITCH_TO_CALENDAR_READ_URI = false;
			CityDataColumns.SWITCH_TO_CALENDAR_MGR_URI = false;
		}
	}

	/**
	 * @brief 【是否可链接】
	 * @n<b>函数名称</b>     :canLink
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-8-16下午2:43:11
	 * @<b>修改时间</b>      :
	*/
	public boolean canLink() {
		if(android.os.Build.VERSION.SDK_INT >= 21 || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
			return false;
		}
		return (mCalVer >= ComDataDef.CALENDAR_VAR_CODE_2_5_0);
	}
	
	public boolean canLink_2_7_0() {
		if(android.os.Build.VERSION.SDK_INT >= 21 || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
			return false;
		}
		return (mCalVer >= ComDataDef.CALENDAR_VAR_CODE_2_7_0);
	}
	
	public AssetManager getLinkAssetManager() {
		if (mCtxCalendar != null && canLink()) {
			return mCtxCalendar.getAssets();
		}
		
		return null;
	}
	
	public static boolean canLink(Context context) {
		if(android.os.Build.VERSION.SDK_INT >= 21 || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
			return false;
		}
		return ComfunHelp.checkApkExist(context, ComDataDef.CALENDAR_PACKAGE, ComDataDef.CALENDAR_VAR_CODE_2_5_0);
	}

	/**
	 * @brief 【获得主版drawable】
	 * @n<b>函数名称</b>     :getCalendarRes
	 * @param sResName
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-8-20下午3:42:42
	 * @<b>修改时间</b>      :
	*/
	public InputStream getCalendarRes(String sResName) {
		if (mCtxCalendar != null) {
			try {
				Resources res = mCtxCalendar.getResources();
				int id = res.getIdentifier(sResName, "drawable", ComDataDef.CALENDAR_PACKAGE);
				if (id != 0) {
					return res.openRawResource(id);
				}
			} catch (Exception e) {
			}
		}

		return null;
	}

	//////////////////////////////////////////////////////////////////////
	/**
	 * @brief 【城市定位】
	 * @n<b>函数名称</b>     :localCity
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-8-6下午7:27:26
	 * @<b>修改时间</b>      :
	*/
	public void localCity() {
		if (mCtxCalendar != null) {
			try {
		        Intent iService = new Intent();
		        iService.setClassName(ComDataDef.CALENDAR_PACKAGE, CALENDAR_SERVICE);
		        iService.putExtra(ACTION_TYPE, AT_GET_LOCAL_CITY);
		        mContext.startService(iService);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			TimeService.localCtiy(mContext);
		}
	}
	
	/**
	 * @brief 【切换城市】
	 * @n<b>函数名称</b>     :switchCity
	 * @param id
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-8-10下午9:19:39
	 * @<b>修改时间</b>      :
	*/
	public void switchCity(int id) {
		try {
	        Intent iService = new Intent();
	        iService.setClassName(ComDataDef.CALENDAR_PACKAGE, CALENDAR_SERVICE);
	        iService.putExtra(ACTION_TYPE, AT_CITY_SWITCH);
	        iService.putExtra(ACTION_PARAM_CITY_ID, id);
	        mContext.startService(iService);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @brief 【更新天气】
	 * @n<b>函数名称</b>     :autoUpdateFirstWeather
	 * @param context
	 * @param bForce
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-8-6上午11:53:36
	 * @<b>修改时间</b>      :
	*/
	public void autoUpdateFirstWeather(boolean bForce) {
		if (mCtxCalendar == null || android.os.Build.VERSION.SDK_INT >= 21 || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
			TimeService.autoUpdateFirstWeather(mContext, bForce);
		} else {
			// 2.7.0 以下版本，调用主版的SWITCH接口更新天气
			// 2.7.0 以上调用TimeServie的更新接口
			if (mCalVer >= ComDataDef.CALENDAR_VAR_CODE_2_7_0) {
				try {
			        Intent iService = new Intent();
			        iService.setClassName(ComDataDef.CALENDAR_PACKAGE, CALENDAR_SERVICE);
			        iService.putExtra(ACTION_TYPE, AT_UPDATE_CITY_WEATHER);
			        iService.putExtra(ACTION_PARAM_FORCE, bForce);
			        mContext.startService(iService);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
//		        Intent iService = new Intent();
//		        iService.setClassName(ComDataDef.CALENDAR_PACKAGE, CALENDAR_SERVICE);
//		        iService.putExtra(ACTION_TYPE, AT_CITY_SWITCH);
//		        iService.putExtra(ACTION_PARAM_CITY_ID, getFirstCityID());
//		        mContext.startService(iService);
				
				ivnokeCalenderUpdate(bForce);
			}
		}
	}
	
	/**
	 * @brief 【调用更新接口】
	 * @n<b>函数名称</b>     :ivnokeCalenderUpdate
	 * @param bForce
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-8-15下午7:06:36
	 * @<b>修改时间</b>      :
	*/
	private void ivnokeCalenderUpdate(boolean bForce) {
		if (mMtdUpdate == null) {
			try {
				final String sClass = "com.calendar.Widget.WidgetUtils";
				Class<?> updateCls = Class.forName(sClass, false, mCtxCalendar.getClassLoader());
				final Class<?> [] args = {Context.class, boolean.class};
				mMtdUpdate = updateCls.getDeclaredMethod("autoUpdateWeather", args);
				mMtdUpdate.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (mMtdUpdate != null && mCtxCalendar != null) {
				try {
	 			final Object [] params = {mCtxCalendar, bForce};
				mMtdUpdate.invoke(null, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	public Intent getWeatherIntent() {
		try {
			if (mCtxCalendar != null) {
	            PackageManager pm = mContext.getPackageManager();
	            Intent intent = pm.getLaunchIntentForPackage(ConfigHelper.getInstance(mContext).getReCommendWeatherAppIdentifier());
	            if(null == intent) return null;
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction(ACT_SHOW_WEATHER + UUID.randomUUID().toString());
				return intent;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Intent getCityMgrIntent() {
		if (mCtxCalendar != null && mCalVer >= ComDataDef.CALENDAR_VAR_CODE_2_7_0) {
			Intent intent = new Intent();
			intent.setClassName(ComDataDef.CALENDAR_PACKAGE, "com.calendar.UI.UIWidgetCityMgr");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			return intent;
		}
		
		return null;
	}
	
	public Intent getCalendarIntent() {
		if (mCtxCalendar != null && canLink()) {
			PackageManager pm = mContext.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(ComDataDef.CALENDAR_PACKAGE);
            if(null == intent) return null;
			intent.setAction(ACT_SHOW_CALENDAR + UUID.randomUUID().toString());
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			return intent;
		}
		
		return null;
	}
	
	public final void startWeatherPage() {
		Intent intent = getWeatherIntent();
		if (intent == null || android.os.Build.VERSION.SDK_INT >= 21 || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
			HiAnalytics.submitEvent(mContext, WidgetUtils.getAnalyticsId(mContext, R.string.analytics_weather_click_distribute), "3");
			intent = new Intent(mContext, UIWeatherFragmentAty.class);
			intent.setAction(UUID.randomUUID().toString());
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		} else {
			HiAnalytics.submitEvent(mContext, WidgetUtils.getAnalyticsId(mContext, R.string.analytics_weather_click_distribute), "4");
		}

		try {
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
	///////////////////////////////////// 天气设置 //////////////////////////////////////////
	public int getFirstCityID() {
        SharedPreferences mSet = getSetting(mCfgPrefName);
        return mSet.getInt(ConfigSet.CONFIG_KEY_WIDGET_CITY_ID, -1);
	}
	
	public boolean setFirstCityID(int newId) {
		if(android.os.Build.VERSION.SDK_INT >= 21 || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
			TimeService.setFirstCityID(mContext, newId);
			return false;
    	}
		if (mCtxCalendar == null) {
			TimeService.setFirstCityID(mContext, newId);
			return false;
		} else {
			switchCity(newId);
			return true;
		}
	}
	
	/**
	 * @brief 【设置插件状态 （2.7.0）】
	 * @n<b>函数名称</b>     :setWidgetState
	 * @param type
	 * @param id
	 * @param bOpen
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-8-20下午4:23:45
	 * @<b>修改时间</b>      :
	*/
	public void setWidgetState(int type, int id, boolean bOpen) {
		try {
			if (mCtxCalendar != null && canLink_2_7_0()) {
		        Intent iService = new Intent();
		        iService.setClassName(ComDataDef.CALENDAR_PACKAGE, CALENDAR_SERVICE);
		        iService.putExtra(ACTION_TYPE, AT_WIDGET_STATE);
		        iService.putExtra(ACTION_PARAM_WIDGET_ID, id);
		        iService.putExtra(ACTION_PARAM_WIDGET_TYPE, type);
		        iService.putExtra(ACTION_PARAM_WIDGET_STATE, bOpen);
		        mContext.startService(iService);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * @brief 【获取熊猫插件皮肤】
     * @n<b>函数名称</b>     :getWidgetPandaSkinPath
     * @param context
     * @return
     * @return    String   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2012-7-18下午04:12:31
     * @<b>修改时间</b>      :
    */
    public String getWidgetPandaSkinPath() {
       /* SharedPreferences set = getSetting(mSkinSettingName);
        String sSkinPath = set.getString(WidgetGlobal.WIDGET_SETTING_PANDA_SKIN, "");
        
        if ("".equals(sSkinPath)) {
//        	set = getSetting(mSkinSettingName);
//        	sSkinPath = set.getString(WidgetGlobal.WIDGET_SETTING_PANDA_SKIN, "");
//        	if ("".equals(sSkinPath)) {
        		sSkinPath = WidgetGlobal.WIDGET_PANDA_DEFAULT_SKIN;
//        	}
        }*/
    	SharedPreferences set;
    	String sSkinPath = null;
		if ((mCtxCalendar == null) || (getSetting(WIDGET_SETTING).getBoolean(WIDGET_SETTING_PANDA_SKIN_WITH_THEME, true))) {
			set = this.mContext.getSharedPreferences(WidgetGlobal.WIDGET_SKIN_SETTING, 4);
		} else {
			set = mCtxCalendar.getSharedPreferences(WIDGET_SETTING, 7);
		}
		sSkinPath = ((SharedPreferences) set).getString(WidgetGlobal.WIDGET_SETTING_PANDA_SKIN, "");
		if (TextUtils.isEmpty(sSkinPath)) {
			sSkinPath = WidgetGlobal.getDefaultSkinPath(mContext);
		}
        
        return sSkinPath;
    }

    /**
     * @brief 【是否跟随91桌面换肤】 (只读多进程)
     * @n<b>函数名称</b>     :isFollowPandaTheme
     * @param context
     * @return
     * @return    boolean   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2012-7-19下午04:25:05
     * @<b>修改时间</b>      :
    */
    /*public boolean isFollowPandaTheme(Context context) {
        SharedPreferences set = getSetting(mSkinSettingName);
        return set.getBoolean(WIDGET_SETTING_PANDA_SKIN_WITH_THEME, true);
    }*/

    
	private final SharedPreferences getSetting(String sPref) {
		// 修复targetSDKversion设置14以上，插件上切换城市不生效的问题 caizp 2014-10-10
		if(android.os.Build.VERSION.SDK_INT >= 21 || CalendarDatas.ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
			return mContext.getSharedPreferences(sPref, flags | Context.MODE_MULTI_PROCESS);
    	}
		return mCtxSetting.getSharedPreferences(sPref, flags | Context.MODE_MULTI_PROCESS);
//				(mCtxCalendar != null) ? (flags | Context.MODE_MULTI_PROCESS): flags);
	}
}
