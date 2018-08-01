package com.nd.weather.widget.PandaHome;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.weather.widget.R;
import com.nd.weather.widget.WidgetUtils;

public class WidgetHotAreaEvent
{

	/**
	 * 时钟适配包名和类名
	 */
	private static String[] clockPkgName = new String[] { "com.android.deskclock",
			"com.android.alarmclock", "com.google.android.deskclock", "com.htc.android.worldclock",
			"com.motorola.blur.alarmclock", "com.sec.android.app.clockpackage", "zte.com.cn.alarmclock",
			"com.lenovomobile.deskclock", "com.mstarsemi.clock.alarm.deskclock"};
	
	private static String[] clockClassName = new String[] { "com.android.deskclock.AlarmClock",
			"com.android.alarmclock.AlarmClock", "com.android.deskclock.AlarmClock",
			"com.htc.android.worldclock.WorldClockTabControl",
			"com.motorola.blur.alarmclock.AlarmClock",
			"com.sec.android.app.clockpackage.ClockPackage",
			"zte.com.cn.alarmclock.AlarmClock", "com.lenovomobile.clock.Clock",
			"com.mstarsemi.clock.stopwatch.TimeClockActivity" };
	
	/**
	 * 日历适配包名和类名
	 */
	private static String[] calendarPkgName = new String[] { "com.android.calendar",
			"com.google.android.calendar", "com.htc.calendar", "com.htc.calendar",
			"com.lenovo.app.Calendar" };
	private static String[] calendarClassName = new String[] {
			"com.android.calendar.LaunchActivity", "com.android.calendar.LaunchActivity",
			"com.htc.calendar.MonthActivity", "com.htc.calendar.CalendarActivityMain",
			"com.lenovo.app.Calendar.MonthActivity" };

	/**
	 * @brief 【启动widget闹钟设置】
	 * @n<b>函数名称</b>     :startAlarmClock
	 * @param ctx
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-1-17下午02:14:27
	 * @<b>修改时间</b>      :
	*/
	public static void startAlarmClock(final Context ctx) {
		HiAnalytics.submitEvent(ctx, WidgetUtils.getAnalyticsId(ctx, R.string.analytics_weather_click_distribute), "1");
		PackageManager pm = ctx.getPackageManager();
		clockPkgName = ctx.getResources().getStringArray(R.array.clock_package_name);
		clockClassName = ctx.getResources().getStringArray(R.array.clock_class_name);
		for (int i = 0; i < clockClassName.length; i++) {
			try {
				ComponentName componentName = new ComponentName(clockPkgName[i], clockClassName[i]);
				
				pm.getActivityInfo(componentName, 0);
				
				Intent alarmClockIntent = new Intent();
				alarmClockIntent.setClassName(clockPkgName[i], clockClassName[i]);
				alarmClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				final Intent it = alarmClockIntent;
				
				ctx.startActivity(it);
				return;
			} catch (Exception e) {
				continue;
			}
		}
		
		for (int i = 0; i < clockClassName.length; i++) {
			try {
				Intent alarmClockIntent = pm.getLaunchIntentForPackage(clockPkgName[i]);
				if(null == alarmClockIntent) continue;
				alarmClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				ctx.startActivity(alarmClockIntent);
				return;
			} catch (Exception e) {
				continue;
			}
		}
		
		Log.i("startAlarmClock", "Can not find alarm clock application.");
	}

	/**
	 * @brief 【启动widget日历】
	 * @n<b>函数名称</b>     :startCalendar
	 * @param ctx
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-1-17下午02:15:23
	 * @<b>修改时间</b>      :
	*/
	public static void startCalendar(final Context ctx) {
		PackageManager pm = ctx.getPackageManager();
		calendarPkgName = ctx.getResources().getStringArray(R.array.calendar_package_name);
		calendarClassName = ctx.getResources().getStringArray(R.array.calendar_class_name);
		for (int i = 0; i < calendarClassName.length; i++) {
			try {
				ComponentName componentName = new ComponentName(calendarPkgName[i],
						calendarClassName[i]);
				pm.getActivityInfo(componentName, 0);
				Intent calendarIntent = new Intent();
				calendarIntent.setClassName(calendarPkgName[i], calendarClassName[i]);
				calendarIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				final Intent it = calendarIntent;
				
				ctx.startActivity(it);
				
			} catch (Exception e) {
				continue;
			}
		}
		
		for (int i = 0; i < calendarClassName.length; i++) {
			try {
				Intent alarmClockIntent = pm.getLaunchIntentForPackage(calendarClassName[i]);
				if(null == alarmClockIntent) continue;
				alarmClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				ctx.startActivity(alarmClockIntent);
				return;
			} catch (Exception e) {
				continue;
			}
		}
		
		Log.i("startCalendar", "Can not find calendar application.");
	}

}
