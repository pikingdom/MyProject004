/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : Calendar
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-12 下午04:25:38 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.nd.calendar.util;

import com.calendar.CommData.DateInfo;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.weather.widget.WidgetGlobal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CalendarInfo
{
	final static int	solarMonth[]	= { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	final static String WEEK_NAME []  = {"周日","周一","周二","周三","周四","周五","周六"};

	final static String WEEK_NAME2[]  = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
	
	public final static String TIAN_GAN[] = {"甲","乙","丙","丁","戊","己","庚","辛","壬","癸"};/*天干名称*/
	public final static String sdizhi[] = { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };
	final static String cDayName[] = { /* 农历日期名 */
		"*", "初一", "初二", "初三", "初四", "初五", 
		"初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五",
		"十六", "十七", "十八", "十九", "二十", "廿一","廿二", "廿三", "廿四", "廿五",
		"廿六", "廿七", "廿八", "廿九", "三十" };
	
	final static String cMonName[] = {"*","正","二","三","四","五","六","七","八","九","十","十一","腊"};

	final static long[] lunarInfo = {
		 0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
		 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
		 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
		 0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
		 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
		 0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
		 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
		 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6,
		 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
		 0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
		 0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
		 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
		 0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
		 0x05aa0, 0x076a3, 0x096d0, 0x04bdb, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
		 0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

	final static int NlYearDaysList[] = {
		384,  738,  1093, 1476, 1830, 2185, 2569, 2923, 3278, 3662,
		4016, 4400, 4754, 5108, 5492, 5846, 6201, 6585, 6940, 7324,
		7678, 8032, 8416, 8770, 9124, 9509, 9863, 10218,10602,10956,
		11339,11693,12048,12432,12787,13141,13525,13879,14263,14617,
		14971,15355,15710,16064,16449,16803,17157,17541,17895,18279,
		18633,18988,19372,19726,20081,20465,20819,21202,21557,21911,
		22295,22650,23004,23388,23743,24096,24480,24835,25219,25573,
		25928,26312,26666,27020,27404,27758,28142,28496,28851,29235,
		29590,29944,30328,30682,31066,31420,31774,32158,32513,32868,
		33252,33606,33960,34343,34698,35082,35436,35791,36175,36529,
		36883,37267,37621,37976,38360,38714,39099,39453,39807,40191,
		40545,40899,41283,41638,42022,42376,42731,43115,43469,43823,
		44207,44561,44916,45300,45654,46038,46392,46746,47130,47485,
		47839,48223,48578,48962,49316,49670,50054,50408,50762,51146,
		51501,51856,52240,52594,52978,53332,53686,54070,54424,54479
	};
	
	final static String m_JqTimeArray[][] = {
            {"2012-1-6 6:43","2012-1-21 0:9","2012-2-4 18:22","2012-2-19 14:17","2012-3-5 12:21","2012-3-20 13:14",
            "2012-4-4 17:5","2012-4-20 0:12","2012-5-5 10:19","2012-5-20 23:15","2012-6-5 14:25","2012-6-21 7:8",
            "2012-7-7 0:40","2012-7-22 18:0","2012-8-7 10:30","2012-8-23 1:6","2012-9-7 13:29","2012-9-22 22:48",
            "2012-10-8 5:11","2012-10-23 8:13","2012-11-7 8:25","2012-11-22 5:50","2012-12-7 1:18","2012-12-21 19:11"},
            {"2013-1-5 12:33","2013-1-20 5:51","2013-2-4 0:13","2013-2-18 20:1","2013-3-5 18:14","2013-3-20 19:1",
            "2013-4-4 23:2","2013-4-20 6:3","2013-5-5 16:18","2013-5-21 5:9","2013-6-5 20:23","2013-6-21 13:3",
            "2013-7-7 6:34","2013-7-22 23:55","2013-8-7 16:20","2013-8-23 7:1","2013-9-7 19:16","2013-9-23 4:44",
            "2013-10-8 10:58","2013-10-23 14:9","2013-11-7 14:13","2013-11-22 11:48","2013-12-7 7:8","2013-12-22 1:10"},
            {"2014-1-5 18:24","2014-1-20 11:51","2014-2-4 6:3","2014-2-19 1:59","2014-3-6 0:2","2014-3-21 0:57",
            "2014-4-5 4:46","2014-4-20 11:55","2014-5-5 21:59","2014-5-21 10:59","2014-6-6 2:3","2014-6-21 18:51",
            "2014-7-7 12:14","2014-7-23 5:41","2014-8-7 22:2","2014-8-23 12:45","2014-9-8 1:1","2014-9-23 10:29",
            "2014-10-8 16:47","2014-10-23 19:57","2014-11-7 20:6","2014-11-22 17:38","2014-12-7 13:4","2014-12-22 7:2"},
            {"2015-1-6 0:20","2015-1-20 17:43","2015-2-4 11:58","2015-2-19 7:49","2015-3-6 5:55","2015-3-21 6:45",
            "2015-4-5 10:39","2015-4-20 17:41","2015-5-6 3:52","2015-5-21 16:44","2015-6-6 7:58","2015-6-22 0:37",
            "2015-7-7 18:12","2015-7-23 11:30","2015-8-8 4:1","2015-8-23 18:37","2015-9-8 6:59","2015-9-23 16:20",
            "2015-10-8 22:42","2015-10-24 1:46","2015-11-8 1:58","2015-11-22 23:25","2015-12-7 18:53","2015-12-22 12:47"},
            {"2016-1-6 6:8","2016-1-20 23:27","2016-2-4 17:45","2016-2-19 13:33","2016-3-5 11:43","2016-3-20 12:30",
            "2016-4-4 16:27","2016-4-19 23:29","2016-5-5 9:41","2016-5-20 22:36","2016-6-5 13:48","2016-6-21 6:34",
            "2016-7-7 0:3","2016-7-22 17:30","2016-8-7 9:52","2016-8-23 0:38","2016-9-7 12:51","2016-9-22 22:21",
            "2016-10-8 4:33","2016-10-23 7:45","2016-11-7 7:47","2016-11-22 5:22","2016-12-7 0:41","2016-12-21 18:44"}
        };
    
	public final static String g_JieQi[] = { "小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满",
			"芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };

	/**
	 * @brief 【获取地址的序号】
	 * 
	 * 
	 * @n<b>函数名称</b> :GetDiZhiIndex
	 * @see
	 * 
	 * @param @param sDiZhi
	 * @param @return
	 * @return int
	 * @<b>作者</b> : 邱堃
	 * @<b>创建时间</b> : 2011-4-28上午10:50:22
	 */
	public static int GetDiZhiIndex(String sDiZhi) {
		for (int i = 0; i < 12; ++i) {
			if (sDiZhi.equals(sdizhi[i])) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * @brief【获取天干的序号】
	 *
	 *
	 * @n<b>函数名称</b>     :GetTianGanIndex
	 * @see
	
	 * @param  @param sTianGan
	 * @param  @return
	 * @return int
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-6-23上午11:40:57      
	*/
	public static int GetTianGanIndex(final String sTianGan) {
		for (int i = 0; i < 10; ++i) {
			if (sTianGan.equals(TIAN_GAN[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @brief【传回date与1900年相差的天数】
	 *
	 *
	 * @n<b>函数名称</b>     :LDaysFrom1900
	 * @see
	
	 * @param  @param date
	 * @param  @return
	 * @return long
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-6-23下午03:36:51      
	*/
	public static long LDaysFrom1900(DateInfo date) {
		long days;
		int year = date.getYear();
		int month = date.getMonth();
		int day = date.getDay();
		days = 365 * year + (year - 1) / 4 - (year - 1) / 100 + (year - 1) / 400
				- (365 * 1900 + (1900 - 1) / 4 - (1900 - 1) / 100 + (1900 - 1) / 400);

		for (int i = 0; i < month - 1; i++) {
			days += solarMonth[i];
		}
		days += day;
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			if (month > 2) {
				days++;
			}
		}
		return days;
	}
	
	/**
	 * @brief【传回公历 y年m+1月的天数】
	 *
	 *
	 * @n<b>函数名称</b>     :SolarDays
	 * @see
	
	 * @param  @param y
	 * @param  @param m
	 * @param  @return
	 * @return int
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-6-23下午03:36:21      
	*/
	public static int SolarDays(int y, int m) {
		if (m > 11 || m < 0) {
			return 0;
		}

		if (m == 1)// 2月
		{
			return (((y % 4 == 0) && (y % 100 != 0) || (y % 400 == 0)) ? 29 : 28);
		} else {
			return (solarMonth[m]);
		}
	}

	/**
	 * @brief【根据日期获取时间】
	 *
	 *
	 * @n<b>函数名称</b>     :DayOfWeek
	 * @see
	
	 * @param  @param date
	 * @param  @return
	 * @return String
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-6-23下午12:15:00      
	*/
	public static String DayOfWeek(final DateInfo date) {
		return (WidgetGlobal.getWeekShowStyle(BaseConfig.getApplicationContext())==0) ? WEEK_NAME[DayOfWeekFlag(date)] : WEEK_NAME2[DayOfWeekFlag(date)];
	}

//	public static String DayOfWeek2(final DateInfo date) {
//		return WEEK_NAME_2[DayOfWeekFlag(date)];
//	}

	public static int DayOfWeekFlag(DateInfo dateTemp) {
		int year = dateTemp.year;
		int month = dateTemp.month;
		int day = dateTemp.day;

		if (month == 1 || month == 2) {
			month += 12;
			year--;
		}
		
		int iTemp = (day + 1 + 2 * month + 3 * (month + 1) / 5 + year + year / 4 - year / 100 + year / 400) % 7;
		return iTemp;
	}
	
	public int GetMonthData(int nYear, int nMonth) {
		boolean bIsLeap = false;
		int nDate = 0;
		if (nYear % 400 == 0 || (nYear % 4 == 0 && nYear % 100 != 0)) {
			// 是润年
			bIsLeap = true;
		}
		
		if (0 < nMonth && nMonth < 8) {
			if (nMonth % 2 != 0) {
				nDate = 31;
			} else {
				if (nMonth == 2 && bIsLeap == true) {
					nDate = 29;
				} else if (nMonth == 2 && bIsLeap != true) {
					nDate = 28;
				} else {
					nDate = 30;
				}
			}
		} else if (nMonth >= 8) {
			if (nMonth % 2 == 0) {
				nDate = 31;
			} else {
				nDate = 30;
			}
		}
		
		return nDate;
	}

	//////////////////////////////////////////////////////////////////////
	// ====== 传回农历 y年的总天数
	final private static int LYearDays(int y) {	// LYearDays
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((lunarInfo[y - 1900] & i) != 0)
				sum += 1;
		}
		
		return (sum + leapDays(y));
	}

	// ====== 传回农历 y年闰月的天数
	final private static int leapDays(int y) {
		if (leapMonth(y) != 0) {
			if ((lunarInfo[y - 1900] & 0x10000) != 0)
				return 30;
			else
				return 29;
		} else
			return 0;
	}

	// ====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
	final private static int leapMonth(int y) {
		return (int) (lunarInfo[y - 1900] & 0xf);
	}

	// ====== 传回农历 y年m月的总天数
	final private static int monthDays(int y, int m) {
		if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
			return 29;
		else
			return 30;
	}
	
	/**
	 * @brief 【返回精简的农历信息】
	 * @n<b>函数名称</b>     :getLunarEx
	 * @param glDate
	 * @return
	 * @return    String   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-2-28下午02:31:22
	 * @<b>修改时间</b>      :
	*/
	public static String getLunarEx(DateInfo glDate) {
		int nlYear, nlMonth, nlDay;
		
		// 定位到1900年1月31日（农历1900年1月1日）
		int offset = (int)LDaysFrom1900(glDate) - 30;

		int offsetNew;
		int l_iNewYear = glDate.year;
		if (glDate.year > 1900) {
			offsetNew = NlYearDaysList[l_iNewYear-1900-1] + 1;
			offset = offset - offsetNew + 1;
		}

		int iYear;
		int daysOfYear = LYearDays(l_iNewYear-1);
		for(iYear=l_iNewYear; iYear<2050 && offset>0; iYear++) {
			daysOfYear = LYearDays(iYear);
			offset -= daysOfYear;
		}
		
		if (offset < 0) {
			offset += daysOfYear;
			iYear--;
		}
		
		nlYear = iYear;	//
		
		// 月
		int leap = leapMonth(iYear);
		boolean isLeap = false;
		int iMonth, daysOfMonth = 0;
		
		for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
			if (leap > 0 && iMonth == (leap + 1) && !isLeap) {
				--iMonth;
				isLeap = true;
				daysOfMonth = leapDays(nlYear);
			} else {
				daysOfMonth = monthDays(nlYear, iMonth);
			}

			if (isLeap && iMonth == (leap + 1)) {
				isLeap = false;
			}

			offset -= daysOfMonth;
		}
		
		if (offset <= 0) {
			offset += daysOfMonth;
			--iMonth;
		}
		
		if (iMonth == 0) {
			nlYear--;
			iMonth = 12;
			offset = monthDays(nlYear, 12);
		}
		
		nlMonth = iMonth;
		nlDay = offset;
		
		if (nlMonth < 0 || nlMonth >= cMonName.length
				|| nlDay < 0 || nlDay >= cDayName.length) {
				return null;
		}
		
		//
		int nTianGan = ((nlYear - 4) % 60) % 10;
		if (nTianGan < 0 || nTianGan >= TIAN_GAN.length) {
			return null;
		}
		
		int nDiZhi = ((nlYear - 4) % 60) % 12;
		if (nDiZhi < 0 || nDiZhi >= sdizhi.length) {
			return null;
		}
			
		return TIAN_GAN[nTianGan] + sdizhi[nDiZhi] + "年 " + 
			(isLeap?"闰" : "") + cMonName[nlMonth] + "月" + 
			cDayName[nlDay];
	}

	static final int GetMonthDays(int year, int month) {
		int monthDay[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (((year % 4 == 0) && (year % 100 != 0)) || year % 400 == 0) {
			monthDay[1] += 1;
		}

		return monthDay[(month - 1) % 12];
	}
	
	// 获得某公历年内的第 N 个节气距年初的天数，1-24，对应小寒到冬至
	static final double GetJieQiDayTimeFromYear(int year, int n) {
		double juD, tht, yrD, shuoD;

		// 对没有公元 0 年的调整
		if (year <= 0) year++;

		juD = year * (365.2423112 - 6.4e-14 * (year-100) * (year - 100)
			- 3.047e-8 * (year-100)) + 15.218427 * n + 1721050.71301;
		tht = 3e-4 * year - 0.372781384 - 0.2617913325 * n;
		yrD = (1.945 * Math.sin(tht) - 0.01206 * Math.sin(2*tht)) * (1.048994 - 2.583e-5 * year);
		shuoD = -18e-4 * Math.sin(2.313908653 * year- 0.439822951 - 3.0443 * n);
		
		int ret = (year - 1) * 365 + ((year - 1) / 4) - ((year - 1) / 100) +
			((year - 1) / 400) ;//+ GetDayFromYearBegin(year, 1, 0);

		return juD + yrD + shuoD - ret - 1721425; // 定气
	}
	
	final static SimpleDateFormat JIE_QI_FM = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	static boolean GetJieQiInAYear(int year, int n, DateInfo outDate) {
		if (n < 0 || n > 23) {
			return false;
		}
		
		final int iJQBeginYear = 2012;

		if (year >= iJQBeginYear && year <= 2016) {
			String l_sJqTime = m_JqTimeArray[year - iJQBeginYear][n];
			try {
			    JIE_QI_FM.setTimeZone(TimeZone.getDefault());
				Date date = JIE_QI_FM.parse(l_sJqTime);
				date.setSeconds(0);
				outDate.fromDate(date);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return true;
		}

		double days = GetJieQiDayTimeFromYear(year, n + 1);
		for (int i = 1; i <= 12; ++i) {
			int dayMonth = GetMonthDays(year, i);
			if (days > dayMonth) {
				days -= dayMonth;
			} else {
				outDate.month = i;
				break;
			}
		}

		outDate.day = (int) days;
		days -= outDate.day;
		outDate.hour = (int) (days * 24);

		days = days * 24 - outDate.hour;
		outDate.minute = (int) (days * 60 + 0.5);
		if (outDate.minute >= 60) {
			outDate.minute = 56;
		}

		return true;
	}
	
	/**
	 * @brief 【获取节气】
	 * @n<b>函数名称</b>     :GetAfterJieQi
	 * @param date
	 * @return
	 * @return    String   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-3-1下午02:55:21
	 * @<b>修改时间</b>      :
	*/
	public static String GetAfterJieQi(DateInfo date) {
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        int hour = 0;
        int minute = 0;
        
		int jqYear = year;
		int jqIndex = 0;

		DateInfo jqDate = new DateInfo();
		
		int i = 0;
		for (; i < 24; ++i) {
			if (!GetJieQiInAYear(year, i, jqDate)) {
				continue;
			}

			// 找到当前节气
			if ((jqDate.month > date.month)
				||  (jqDate.month == month && jqDate.day > day)
				|| (jqDate.month == month && jqDate.day == day && jqDate.hour > hour)
				|| (jqDate.month == month && jqDate.day == day && jqDate.hour == hour && jqDate.minute > minute)) {
				jqIndex = i;
				break;
			}
		}
		
		if (i == 24) {
			// 今年均没有找到，则为明年第一个节气
			jqYear++;
			jqIndex = 0;
		}

		GetJieQiInAYear(jqYear, jqIndex, jqDate);
		
		String sJieQi;
		if (jqIndex < 0 || jqIndex >= 24) {
			sJieQi = "";
		} else {
			sJieQi = g_JieQi[jqIndex];
		}
		
        if (year == jqYear && month == jqDate.month && day == jqDate.day) {
        	return String.format("今日 %02d:%02d %s", jqDate.hour, jqDate.minute, sJieQi);
        } else {
            Date dateJieQi = new Date(jqYear, jqDate.month - 1, jqDate.day);
            Date dateNow = new Date(year, month - 1, day);

            long diffTime = Math.abs(dateJieQi.getTime() - dateNow.getTime());
            int nDay = (int) (diffTime / (24 * 60 * 60 * 1000));

            return String.format("%d天后  %s", nDay, sJieQi);
        }
	}
	/**
	 * @brief 【公历转农历】
	 * @n<b>函数名称</b>     :lunar
	 * @param glDate
	 * @return
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-6-3下午04:06:24
	 * @<b>修改时间</b>      :
	*/
	public static DateInfo lunar(DateInfo glDate) {
		int nlYear;
		
		// 定位到1900年1月31日（农历1900年1月1日）
		int offset = (int)LDaysFrom1900(glDate) - 30;

		int offsetNew;
		int l_iNewYear = glDate.year;
		if (glDate.year > 1900) {
			offsetNew = NlYearDaysList[l_iNewYear-1900-1] + 1;
			offset = offset - offsetNew + 1;
		}

		int iYear;
		int daysOfYear = LYearDays(l_iNewYear-1);
		for(iYear=l_iNewYear; iYear<2050 && offset>0; iYear++) {
			daysOfYear = LYearDays(iYear);
			offset -= daysOfYear;
		}
		
		if (offset < 0) {
			offset += daysOfYear;
			iYear--;
		}
		
		nlYear = iYear;	//
		
		// 月
		int leap = leapMonth(iYear);
		boolean isLeap = false;
		int iMonth, daysOfMonth = 0;
		
		for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
			if (leap > 0 && iMonth == (leap + 1) && !isLeap) {
				--iMonth;
				isLeap = true;
				daysOfMonth = leapDays(nlYear);
			} else {
				daysOfMonth = monthDays(nlYear, iMonth);
			}

			if (isLeap && iMonth == (leap + 1)) {
				isLeap = false;
			}

			offset -= daysOfMonth;
		}
		
		if (offset <= 0) {
			offset += daysOfMonth;
			--iMonth;
		}
		
		if (iMonth == 0) {
			nlYear--;
			iMonth = 12;
			offset = monthDays(nlYear, 12);
		}

		DateInfo dateInfo = new DateInfo(nlYear, iMonth, offset);
		dateInfo.isLeep = isLeap;
		return dateInfo;
	}
	////////////////////////////////
	/**
	 * @brief【获取系统时间】
	 *
	 *
	 * @n<b>函数名称</b>     :GetSysDateInfo
	 * @see
	
	 * @param  @return
	 * @return DateInfo
	 * @<b>作者</b>          :  邱堃
	 * @<b>创建时间</b>      :  2011-7-8下午04:45:34      
	*/
	public static DateInfo GetSysDateInfo() {
		return new DateInfo(new Date(System.currentTimeMillis()));
	}
	
    /**
     * @Title: getSysDateInfo  
     * @Description: TODO(获取城市时区的当前时间)  
     * @author yanyy
     * @date 2012-11-26 下午05:57:18 
     *
     * @param gmt
     * @return       
     * @return DateInfo
     * @throws
     */
    public static DateInfo getSysDateInfo(String gmt){
        return new DateInfo(ComfunHelp.getLocalCityDate(gmt));
    }

	public static String getDate(String unixDate) {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long unixLong = 0;
		String date = "";
		try {
			unixLong = Long.parseLong(unixDate) * 1000;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			date = fm.format(unixLong);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return date;
	}
	
	// g_Instance:单体实例对象
//	private static CalendarInfo	g_Instance	= null;
//	private WidgetJni			m_widgetJni	= null;
	
    /**
     * @Title: getWeekOfDate  
     * @Description: TODO(获取星期几)  
     * @author yanyy
     * @date 2012-7-27 下午02:59:36 
     *
     * @param dt
     * @return       
     * @return String
     * @throws
     */
    public static String getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return (WidgetGlobal.getWeekShowStyle(BaseConfig.getApplicationContext())==0) ? WEEK_NAME[w] : WEEK_NAME2[w];
    }
    
//    public String encode(String sContext) {
//    	return sContext;
//    }
//    
//    public String decode(String sEncode) {
//    	return sEncode;
//    }
}
