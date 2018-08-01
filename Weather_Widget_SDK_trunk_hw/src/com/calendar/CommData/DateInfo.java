package com.calendar.CommData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateInfo
{
    public static final String DATE_FORMAT_YYYYMMDD = "%04d-%02d-%02d";
    public static final String DATE_FORMAT_YYYYMMDDhhmm = "%04d-%02d-%02d %02d:%02d";
    public static final String DATE_FORMAT_YYYYMMDDhhmmss = "%04d-%02d-%02d %02d:%02d:%02d";
	public DateInfo() {
		year = 0;
		month = 0;
		day = 0;
		hour = 0;
		minute = 0;
		second = 0;
		isLeep = false;
	}

	public DateInfo(int iYear, int iMonth, int iDay) {
		year = iYear;
		month = iMonth;
		day = iDay;
		hour = 0;
		minute = 0;
		second = 0;
		isLeep = false;
	}

	public DateInfo(DateInfo dateInfo) {
		this.year = dateInfo.year;
		this.month = dateInfo.month;
		this.day = dateInfo.day;
		this.hour = dateInfo.hour;
		this.minute = dateInfo.minute;
		this.second = dateInfo.second;
		this.isLeep = dateInfo.isLeep;
	}

	public DateInfo(Date date) {
		fromDate(date);
	}

	public void fromDate(Date date) {
		year = date.getYear() + 1900;
		month = date.getMonth() + 1;
		day = date.getDate();
		hour = date.getHours();
		minute = date.getMinutes();
		second = date.getSeconds();
	}
	
	public DateInfo(String strDate, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date;
		try {
			date = df.parse(strDate);
			setYear(date.getYear() + 1900);
			setMonth(date.getMonth() + 1);
			setDay(date.getDate());
			setHour(date.getHours());
			setMinute(date.getMinutes());
			setSecond(date.getSeconds());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public String getDateTime(String format) {
        String sdate = String.format(format, getYear(), getMonth(),
                getDay(), getHour(), getMinute(), getSecond());
        //原来用这个，在有的2.多的系统上，无法支持2038年以后的日期
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            return new SimpleDateFormat(format).format(df.parse(sdate));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return sdate;
    }
    
    /**
     * @Title: isToday  
     * @Description: TODO(判断是否是今天)  
     * @author yanyy
     * @date 2012-10-19 上午09:52:37 
     *
     * @return       
     * @return boolean
     * @throws
     */
    public boolean isToday(){
        Date today = new Date(System.currentTimeMillis());
        int iyear = today.getYear() + 1900;
        int imonth = today.getMonth() + 1;
        int iday = today.getDate();
        return ((year == iyear) && (month == imonth) && (day == iday));
    }

//	@Override
//	public JSONObject ToJsonObject() {
//		return null;
//	}
//
//	@Override
//	public void SetJsonString(String strJson) {
//	}
//
//	@Override
//	public int GetType() {
//		return DataType.DATE_INFO_TYPE;
//	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public void setSecond(int isecond) {
		this.second = isecond;
	}

	public boolean isLeep() {
		return isLeep;
	}

	public void setIsLeep(Boolean isLeap) {
		this.isLeep = isLeap;
	}

	// year:年
	public int year;
	// month:月
	public int month;
	// day:天
	public int day;
	// hour:时
	public int hour;
	// minute:月
	public int minute;
	// second:秒
	public int second;
	// isRunYue:是否是闰年
	public boolean isLeep;

}
