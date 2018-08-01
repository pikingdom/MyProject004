/**   
*    
* @file
* @brief
*
* @<b>文件名</b>      : WidgetUtils
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2012-7-11 下午05:27:49 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.weather.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.calendar.CommData.CityWeatherInfo;
import com.calendar.CommData.RealTimeWeatherInfo;
import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.module.WeatherModule;
import com.nd.calendar.util.ComfunHelp;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.weather.widget.UI.UICalendarGuideAty;
import com.nd.weather.widget.skin.WidgetLoadedSkinInfo;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WidgetUtils {
	public final static String WIDGET_REFRESH_ACTION = "com.nd.weather.appwidget.refresh";
	public final static String REFRESH_TYPE = "ref_action";
	public final static String HOT_AREA = "hot_area";
	
	public final static int ACTION_INVALIDATE = 0;		// 强制刷新全部
	public final static int ACTION_TIMER = 2;			// 刷新时间
	public final static int ACTION_UPDATE_WEATHER = 3;	// 刷新天气
	public final static int ACTION_NET_CONNECTED = 4;	// 网络连接
	public final static int ACTION_CITY_SWITCH = 5;		// 城市切换
	public final static int ACTION_HOT_AREA = 6;		// 热区变更
	public final static int ACTION_SKIN_CHANGED = 7;	// 皮肤变更
	public final static int ACTION_WIDGET_RELOAD = 8;	// 插件加载
	public final static int ACTION_UPDATE_WEATHER_UPDATING = 10;	// 刷新天气中
	public final static int ACTION_UPDATE_HUANGLI = 11;	// 刷新黄历

	public final static String WIDGET_RELOAD_PARAM = "reload_param";
	
	public final static String WIDGET_PANDAHOME_WEATHER_REQUEST_ACTION = "nd.pandahome.weather.request";
	public final static String WIDGET_NOTIFY_PANDAHOME_ACTION = "com.nd.weather.pandahome.notify";
	public final static String WNP_CITY_NAME = "city_name";	// 城市名
	public final static String WNP_NOW_TEMP = "now_temp";	// 实时温度
	public final static String WNP_WEATHER_NAME = "weahter_name";	// 实时天气名称
	public final static String WNP_WEATHER_KEY = "weahter_key";		// 实时天气key

	private static boolean mbShowTips = false;
	
	public final static String ACT_TAG = "show_";
	//万年历界面
	public final static String ACT_SHOW_CALENDAR = ACT_TAG + "calendar";
	//黄历界面
	public final static String ACT_SHOW_HULI = ACT_TAG + "huli";
	
	/** 黄历客户端下载点击来源 -- 关于界面 */
	public final static String DOWNLOAD_CLICK_FROM_ABOUT = "1";
	/** 黄历客户端下载点击来源 -- 主界面天气指数 */
	public final static String DOWNLOAD_CLICK_FROM_ZHISHU = "2";
	/** 黄历客户端下载点击来源 -- 主界面宜忌 */
	public final static String DOWNLOAD_CLICK_FROM_HUANGLI = "3";
	/** 黄历客户端下载点击来源 -- 主界面农历 */
	public final static String DOWNLOAD_CLICK_FROM_CALENDAR = "4";
	/** 黄历客户端下载点击来源 -- 主界面多日天气图表 */
	public final static String DOWNLOAD_CLICK_FROM_WEATHER = "5";
	/** 黄历客户端下载点击来源 -- 主界面主界面右下角更多图标 */
	public final static String DOWNLOAD_CLICK_FROM_MORE = "6";
	/** 黄历客户端下载点击来源 -- 主界面多日温度图表 */
	public final static String DOWNLOAD_CLICK_FROM_TEMP = "7";
	/** 黄历客户端下载点击来源 -- 主界面主界面右下角更多图标直接下载 */
	public final static String DOWNLOAD_CLICK_FROM_MORE_DOWNLOAD_IMMED = "8";
	/** 黄历客户端下载点击来源 -- 主界面未来6小时天气图标  2016-05-09 */
	public final static String DOWNLOAD_CLICK_FROM_SIX_HOUR_WEATHER = "9";
	
	/** 进入黄历推荐界面点击来源 -- 关于界面 */
	public final static String ENTER_CLICK_FROM_ABOUT = "1";
	/** 进入黄历推荐界面点击来源 -- 主界面天气指数 */
	public final static String ENTER_CLICK_FROM_ZHISHU = "2";
	/** 进入黄历推荐界面点击来源 -- 主界面左上角农历 */
	public final static String ENTER_CLICK_FROM_NONGLI = "3";
	/** 进入黄历推荐界面点击来源 -- 主界面未来6小时天气图表 */
	public final static String ENTER_CLICK_FROM_SIX_HOUR = "4";
	/** 进入黄历推荐界面点击来源 -- 主界面多日天气图表 */
	public final static String ENTER_CLICK_FROM_WEATHER = "5";
	
	public static void notifyWidgetTimeChanged(Context context) {
		sendBroadcast(context, ACTION_TIMER);
	}
	
	/**
     * <br>Description: 跳转至推荐黄历天气客户端页面
     * <br>Author:caizp
     * <br>Date:2015年1月13日下午5:14:14
     * @param param
     * @param action
     * @param from
     */
    public static void startGuide(Context context, int param, String action, String from){
    	Intent intent;
    	String recommendAppIdeitifier = ConfigHelper.getInstance(context).getReCommendWeatherAppIdentifier();
    	if (ComfunHelp.checkApkExist(context, recommendAppIdeitifier, 0)) {
			PackageManager pm = context.getPackageManager();
			intent = pm.getLaunchIntentForPackage(recommendAppIdeitifier);
			if(null == intent) return;
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if(!TextUtils.isEmpty(action)) {
				intent.setAction(action);
			}
		} else {
	        intent = new Intent(context, UICalendarGuideAty.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.putExtra("from", from);
	        intent.putExtra("param", param);
	    }
    	
    	SystemUtil.startActivitySafely(context, intent);
    }
	
	/**
	 * @brief 【全局广播】
	 * @n<b>函数名称</b>     :sendBroadcast
	 * @param context
	 * @param action
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-5-16上午11:03:23
	*/
	public static void sendBroadcast(Context context, int action) {
		Intent intent = new Intent(WIDGET_REFRESH_ACTION);
		intent.addFlags(32);
		intent.putExtra(REFRESH_TYPE, action);
		context.sendBroadcast(intent);
	}

	/**
	 * @brief 【定向广播】
	 * @n<b>函数名称</b>     :sendBroadcast
	 * @param context
	 * @param action
	 * @param cls
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>创建时间</b>      :  2012-5-16上午11:02:45
	*/
	public static void sendBroadcast(Context context, int action, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		intent.addFlags(32);
		intent.setAction(WIDGET_REFRESH_ACTION);
		intent.putExtra(REFRESH_TYPE, action);
		context.sendBroadcast(intent);
	}
	
	public final static SharedPreferences getSetting(Context ctx, String fileName) {
		return ctx.getSharedPreferences(fileName, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE | 4);
	}

	public static void showSkinTips(final Context context, final int iRet) {
		try {
			if ((iRet & WidgetLoadedSkinInfo.WSI_SWITCH_TO_DEFAULT) != 0) {
				if (!mbShowTips) {
					// 去除toast提示
//					Toast.makeText(context, "　　SD卡可能未载入完成。\n皮肤出现异常，切换为默认皮肤！", Toast.LENGTH_SHORT).show();
					mbShowTips = true;
				}
				
			} else if (iRet == WidgetLoadedSkinInfo.WSI_LOAD_FAILURE) {
				Toast.makeText(context, "皮肤载入失败！", Toast.LENGTH_SHORT).show();
			} else {
				mbShowTips = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @brief 【插件统计代码】 *** 桌面进程
	 * @n<b>函数名称</b>     :statWidget
	 * @param context
	 * @param sStatLog
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-7-12上午09:48:00
	 * @<b>修改时间</b>      :
	*/
	public static void statWidget(final Context context, final String sStatLog) {
		SharedPreferences set = getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
		String sKey = "statistics_" + sStatLog;
		
		// 每天统计一次
		Date nowDate = new Date(System.currentTimeMillis());
		String strStatcisDate = set.getString(sKey, "");
		
		// 获取当前的日期
		nowDate.setYear(nowDate.getYear() - 1900);
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strSystemTiem = formatter.format(nowDate);
		
		if (!strStatcisDate.equals(strSystemTiem)
			&& HttpToolKit.isNetworkAvailable(context)) {
			
			set.edit().putString(sKey, strSystemTiem).commit();
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
	final void sendWeatherToPandaHome(Context context, String sCityName, CityWeatherInfo cityWeatherInfo) {
		try {
			Intent intent = new Intent(WIDGET_NOTIFY_PANDAHOME_ACTION);
			
			if (cityWeatherInfo != null) {
				intent.putExtra(WNP_CITY_NAME, cityWeatherInfo.getCityName());
				
				RealTimeWeatherInfo rtw = cityWeatherInfo.getRealTimeWeather();
				if (rtw != null) {
					String sWeather = rtw.getNowWeather();
					String sWeatherKey = WeatherModule.GetFinalWeathResString(sWeather, cityWeatherInfo.isNight());
					intent.putExtra(WNP_NOW_TEMP, rtw.getTempValue());
					intent.putExtra(WNP_WEATHER_NAME, sWeather);
					intent.putExtra(WNP_WEATHER_KEY, sWeatherKey);
				}
			} else {
				intent.putExtra(WidgetUtils.WNP_CITY_NAME, sCityName);
			}

			intent.addFlags(32);
			context.sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <br>Description: 获取功能统计ID
	 * <br>Author:caizp
	 * <br>Date:2015年2月12日下午2:27:41
	 * @param context
	 * @param resId
	 * @return
	 */
	public static int getAnalyticsId(Context context, int resId) {
		try {
			String str = context.getResources().getString(resId);
			return Integer.parseInt(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}
