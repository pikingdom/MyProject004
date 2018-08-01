package com.nd.calendar.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.nd.weather.widget.R;
import com.nd.weather.widget.UI.UICalendarGuideAty;


public class ConfigHelper {
	
	private static final String TAG = "ConfigHelper";

	public static String PREF_NAME = ComDataDef.ConfigsetData.CONFIG_NAME;
	
	private static ConfigHelper mInstance = null;
    private        Context mContext;
    protected      SharedPreferences mSettings;
    protected      SharedPreferences.Editor mEditor;
    
    // key
//    public static final String KEY_WEATHER_BG = ConfigsetData.CONFIG_NAME_KEY_BK_TYPE;
/*    public static final String KEY_CURRENT_PERSON_GUID = "current_person_guid";
    public static final String KEY_CURRENT_PERSONAL_INFO = "current_personal_info";
    public static final String KEY_SERVER_DATE = "server_date";
    public static final String KEY_SERVER_DATE_SAVE_TIME = "server_date_save_time";*/
   
    
    // vlaue
    
    // weather activity background
    public static final int VALUE_WEATHER_OLD = 1;
    public static final int VALUE_WEATHER_NEW = 0;
    public static final int VALUE_DEFALUT_STYLE = VALUE_WEATHER_OLD;
    
    // first start TAB
/*    public static final int START_TAB_WEATHER = 0;
    public static final int START_TAB_CALENDAR = 1;
    public static final int START_TAB_HULI = 2;*/
//    public static final String KEY_START_TAB = ConfigsetData.CONFIG_NAME_KEY_BEGIN;
    
    // show new flag
    public static final String KEY_NEW_FLAG = "has_new_answer";		// true means need to show new flag on TAB
   
    /*    // 周首日
    public static final int WEEK_SUNDAY = 0;
    public static final int WEEK_MONDAY = 1;
    
    // 生肖背景
    public static final int CALENDAR_BK_SHENGXIAO = 0;
    public static final int CALENDAR_BK_NUMBER = 1;
   
    // 节日显示优先级
    public static final int HOILDAY_PRT_JQ = 0;
    public static final int HOILDAY_PRT_NL = 1;
    public static final int HOILDAY_PRT_GL = 2;
    
    // 节日显示
    public static final int HOILDAY_DISPLAY_JQ = 0;
    public static final int HOILDAY_DISPLAY_NL = 1;
    public static final int HOILDAY_DISPLAY_GL = 2;

    // CONFIG_NAME_KEY_SKIN_TAB
    public static final int SKIN_TAB_SORT = 0;
    public static final int SKIN_TAB_SETUPED = 1;
    public static final int SKIN_TAB_LAST = 2;*/
    /**
     * 推荐下载天气APP信息
     */
    public static final String RE_APP_NAME = "re_app_name";
    public static final String RE_APP_IDENTIFIER = "re_app_identifier";
    public static final String RE_APP_RECOMMEND_HTML = "re_app_recommend_html";
    public static final String RE_APP_DOWNLOAD_URL = "re_app_download_url";
    
  
    public static ConfigHelper getInstance(Context context){
    	if (mInstance == null){
    		// this global static method need application context
    		mInstance = new ConfigHelper(context.getApplicationContext());
    	}
    	
    	return mInstance;
    }
    
    public synchronized boolean commit() {
        return mEditor.commit();
    }
    
    public String loadKey(String key) {
        return mSettings.getString(key, "");
    }

    public String loadKey(String key, String defValue) {
        return mSettings.getString(key, defValue);
    }
    
    public void saveKey(String key, String value) {
    	mEditor.putString(key, value);
    }
    
    public void removeKey(String key){
    	mEditor.remove(key);
    }
    
    public boolean loadBooleanKey(String key, boolean defValue) {
    	return mSettings.getBoolean(key, defValue);
    }
    
    public void saveBooleanKey(String key, boolean value) {
    	mEditor.putBoolean(key, value);
    }
    
    public int loadIntKey(String key, int defValue) {
    	return mSettings.getInt(key, defValue);
    }
    
    public void saveIntKey(String key, int value) {
    	mEditor.putInt(key, value);
    }
    
    public long loadLongKey(String key, long defValue) {
    	return mSettings.getLong(key, defValue);
    }
    
    public void saveLongKey(String key, long value) {
    	mEditor.putLong(key, value);
    }
    
    public float loadFloatKey(String key, float defValue) {
    	return mSettings.getFloat(key, defValue);
    }
    
    public void saveFloatKey(String key, float value) {
    	mEditor.putFloat(key, value);
    }
    
    private ConfigHelper(Context c){
    	mContext = c;
    	if (c == null)
    		Log.e(TAG, "the context point is null");
    	mSettings = mContext.getSharedPreferences(PREF_NAME, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE | 4 );
    	mEditor = mSettings.edit();
    }
    
    public void flush() {
    	mSettings = mContext.getSharedPreferences(PREF_NAME, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE | 4);
    	mEditor = mSettings.edit();
    }
    
    /**
     * <br>Description: 获取推荐天气app名称
     * <br>Author:caizp
     * <br>Date:2016年5月13日下午3:09:34
     * @return
     */
    public String getReCommendWeatherAppName() {
    	return mSettings.getString(ConfigHelper.RE_APP_NAME, mContext.getResources().getString(R.string.sdk_app_name));
    }
    /**
     * <br>Description: 获取推荐天气app包名
     * <br>Author:caizp
     * <br>Date:2016年5月13日下午3:09:34
     * @return
     */
    public String getReCommendWeatherAppIdentifier() {
    	return mSettings.getString(ConfigHelper.RE_APP_IDENTIFIER, ComDataDef.CALENDAR_PACKAGE);
    }
    /**
     * <br>Description: 获取推荐天气app推荐下载html
     * <br>Author:caizp
     * <br>Date:2016年5月13日下午3:09:34
     * @return
     */
    public String getReCommendWeatherAppHtml() {
    	return mSettings.getString(ConfigHelper.RE_APP_RECOMMEND_HTML, UICalendarGuideAty.POST_URL);
    }
    /**
     * <br>Description: 获取推荐天气app下载地址
     * <br>Author:caizp
     * <br>Date:2016年5月13日下午3:09:34
     * @return
     */
    public String getReCommendWeatherAppUrl() {
    	return mSettings.getString(ConfigHelper.RE_APP_DOWNLOAD_URL, ComDataDef.CALENDAR_DOWN_URL);
    }
    
}
