package com.nd.calendar.provider;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.provider.BaseColumns;

public class CalendarDatas {
	
	/**
	 * 是否总是使用91桌面天气插件自己的天气数据
	 */
	public static boolean ALWAYS_GET_PANDAHOME_WEATHER_DATA = true;
	
    //ContentProvider的uri 黄历天气
    public static final String AUTHORITY_CALENDAR = "com.nd.calendar.CalendarProvider";
    //91桌面
    public static String AUTHORITY_PANDAHOME = "com.nd.calendar.PandahomeProvider";
    //91智能锁
    public static String AUTHORITY_LOCK = "com.nd.calendar.LockProvider";
    
    static boolean PROVIDER_AUTHED = false;
    
//    public static void setAuthority(String authority){
//        if (authority.equals(AUTHORITY_CALENDAR)){
//            CityDataColumns.CONTENT_URI = CityDataColumns.CONTENT_URI_CALENDAR;
//        }else{
//            CityDataColumns.CONTENT_URI = CityDataColumns.CONTENT_URI_PANDAHOME;
//        }
//    }
    
    public static synchronized void setAuthority(Context context) {
    	if (!PROVIDER_AUTHED) {
        	try {
        		final String sPackName = context.getPackageName();
        		final PackageInfo pl = context.getPackageManager().getPackageInfo(sPackName, PackageManager.GET_PROVIDERS);
    			if (pl.providers != null) {
    				final String sClassName = PandahomeProvider.class.getName();
    				for (ProviderInfo providerInfo : pl.providers) {
    					if (sClassName.equals(providerInfo.name)) {
    						AUTHORITY_PANDAHOME = providerInfo.authority;
    						CityDataColumns.CONTENT_URI_PANDAHOME = CityDataColumns.getURI();
    						CityDataColumns.CONTENT_URI = CityDataColumns.CONTENT_URI_PANDAHOME;
    						PROVIDER_AUTHED = true;
    						break;
    					}
    				}
    			}

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
		}
    }
    
    // 城市列表字段定义
    public static final class CityDataColumns implements BaseColumns{
        /*黄历*/
        public static final Uri CONTENT_URI_CALENDAR = Uri.parse("content://" + AUTHORITY_CALENDAR + "/ListWeathInfos");
        public static final Uri CONTENT_URI_LOCK = Uri.parse("content://" + AUTHORITY_LOCK + "/ListWeathInfos");
        /*91桌面*/
        public static Uri CONTENT_URI_PANDAHOME = getURI();
        
        public static final Uri getURI() {
        	return Uri.parse("content://" + AUTHORITY_PANDAHOME + "/ListWeathInfos");
        }
        
        /*默认从91桌面*/
        public static Uri CONTENT_URI = CONTENT_URI_PANDAHOME;

        public static boolean SWITCH_TO_CALENDAR_READ_URI = false;
        public static boolean SWITCH_TO_CALENDAR_MGR_URI = false;
        
        // 只读用URI，可供在SDK与主版间切换；增删改操作不能使用该URI，防止主版出现数据错误
        public static Uri GET_READ_ONLY_URI() {
        	if(android.os.Build.VERSION.SDK_INT >= 21 || ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
        		return CONTENT_URI_PANDAHOME;
        	}
        	return (SWITCH_TO_CALENDAR_READ_URI? CONTENT_URI_CALENDAR: CONTENT_URI_PANDAHOME);
        }

        // 城市管理使用，对于2.7 以下版本
        public static Uri GET_MGR_URI() {
        	if(android.os.Build.VERSION.SDK_INT >= 21 || ALWAYS_GET_PANDAHOME_WEATHER_DATA) {
        		return CONTENT_URI_PANDAHOME;
        	}
        	return (SWITCH_TO_CALENDAR_MGR_URI? CONTENT_URI_CALENDAR: CONTENT_URI_PANDAHOME);
        }
        
        public static final String  TABLE_NAME = "ListWeathInfo";
        public static final String  DEFAULT_SORT_ORDER = " nSort asc";
        
        // 新的MIME类型-多个
        public static final String  CONTENT_TYPE        = "vnd.android.cursor.dir/vnd.nd.listweatherinfo";

        // 新的MIME类型-单个
        public static final String  CONTENT_ITEM_TYPE   = "vnd.android.cursor.item/vnd.nd.listweatherinfo";
        
        /*id*/
        public static final String _ID = "listInfoId";
        /*城市代码*/
        public static final String CITY_CODE = "strCode";
        /*城市名称*/
        public static final String CITY_NAME = "strText";
        /*顺序ID*/
        public static final String CITY_SORT = "nSort";
        /*多天天气预报*/
        public static final String DAY_WEATHER_JSON = "strweathJson";
        /*实时天气预报*/
        public static final String NOW_WEATHER_JSON = "strNowweathJson";
        /*生活指数*/
        public static final String LIVE_INDEX_JSON = "strIndexJson";
        /*PM数据*/
        public static final String PM_JSON = "strPMJson";
        /*日出日落*/
        public static final String SUN_JSON = "strSunJson";
        /*预警数据*/
        public static final String WARNING_JSON = "strWarningJson";
        /*自动定位标志*/
        public static final String FLAG = "nFlag";
        /*数据保存时间*/
        public static final String DAY_SAVE_TIME = "strSaveTime";
        /*实时天气保存时间*/
        public static final String NOW_REF_SAVE_TIME = "strNowRefTime";
        /*生活指数保存时间*/
        public static final String INDEX_SAVE_TIME = "strIndexTime";
        /*预警数据保存时间*/
        public static final String WARN_SAVE_TIME = "strWarnTime";
        /*日出日落数据保存时间*/
        public static final String SUN_SAVE_TIME = "strSunTime";
        /*PM数据保存时间*/
        public static final String PM_SAVE_TIME = "strPMTime";
    }
}
