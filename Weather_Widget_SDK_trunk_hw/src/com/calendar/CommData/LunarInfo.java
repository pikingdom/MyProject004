/**   
 *    
 * @file【农历信息】
 * @brief
 *
 * @<b>文件名</b>      : LunarInfo
 * @n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 邱堃
 * @n@n<b>创建时间</b>: 2011-4-12 下午12:04:42 
 * @n@n<b>文件描述</b>:  
 * @version  
 */

package com.calendar.CommData;


public class LunarInfo
{
	// 天干
	public String tiangan;
	// 地支
	public String dizhi;
	// 生肖
	public String shenxiao;
	// 日
	public String dayname;
	// 月
	public String monthname;
	// 是否闰月
	public boolean isLeepMonth;

	public String getTiangan() {
		return tiangan;
	}

	public void setTiangan(String tiangan) {
		this.tiangan = tiangan;
	}

	public String getDizhi() {
		return dizhi;
	}

	public void setDizhi(String dizhi) {
		this.dizhi = dizhi;
	}

	public String getShenxiao() {
		return shenxiao;
	}

	public void setShenxiao(String shenxiao) {
		this.shenxiao = shenxiao;
	}

	public String getDayname() {
		return dayname;
	}

	public void setDayname(String dayname) {
		this.dayname = dayname;
	}

	public String getMonthname() {
		return monthname;
	}

	public void setMonthname(String monthname) {
		this.monthname = monthname;
	}

	public boolean isLeepMonth() {
		return isLeepMonth;
	}

	public void setLeepMonth(boolean isLeepMonth) {
		this.isLeepMonth = isLeepMonth;
	}

	// 构造函数
	public LunarInfo() {
		tiangan = "";
		dizhi = "";
		shenxiao = "";
		dayname = "";
		monthname = "";
		isLeepMonth = false;
	}
}
