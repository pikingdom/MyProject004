/**   
 *    
 * @file
 * @brief
 *
 * @<b>文件名</b>      : WarningInfo
 *@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2012-1-6 上午11:15:04 
 * @n@n<b>文件描述</b>:  
 * @version  
 */
package com.calendar.CommData;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

import com.nd.calendar.util.StringHelp;

public class WarningInfo implements Serializable {
	// serialVersionUID:TODO（用一句话描述这个变量表示什么）
	private static final long serialVersionUID = 6085457949462813268L;
	private String sWeather = ""; // 预警天气类型
	private String sColor = ""; // 预警等级颜色
	private String sGrade = ""; // 预警等级
	private String sContent = ""; // 预警内容
	private String sStandard = ""; // 预警标准
	private String sGuide = ""; // 预警指南
	private String sImgUrl = ""; // 预警图标
	private String sTime = ""; // 预警时间
	private String sCity = ""; // 预警省市
	private int id = 0; // 城市id
	private String sWeatherNo = ""; // 预警风力等级

	public String getsWeatherNo() {
		return sWeatherNo;
	}

	public void setsWeatherNo(String sWeatherNo) {
		this.sWeatherNo = sWeatherNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return sCity;
	}

	public void setCity(String sCity) {
		this.sCity = sCity;
	}

	public String getWeather() {
		return sWeather;
	}

	public String getColor() {
		return sColor;
	}

	public String getGrade() {
		return sGrade;
	}

	public String getContent() {
		return sContent;
	}

	public String getStandard() {
		return sStandard;
	}

	public String getGuide() {
		return sGuide;
	}

	public String getImgUrl() {
		return sImgUrl;
	}

	public String getTime() {
		return sTime;
	}

	public void setWeather(String sWeather) {
		this.sWeather = sWeather;
	}

	public void setColor(String sColor) {
		this.sColor = sColor;
	}

	public void setGrade(String sGrade) {
		this.sGrade = sGrade;
	}

	public void setContent(String sContent) {
		this.sContent = sContent;
	}

	public void setStandard(String sStandard) {
		this.sStandard = sStandard;
	}

	public void setGuide(String sGuide) {
		this.sGuide = sGuide;
	}

	public void setImgUrl(String sImgUrl) {
		this.sImgUrl = sImgUrl;
	}

	public void setTime(String sTime) {
		this.sTime = sTime;
	}

	public boolean setJsonString(String jsonString) {
		if (!TextUtils.isEmpty(jsonString)) {
			try {
				JSONObject jsonObjWarn = StringHelp.getJSONObject(jsonString);
				sWeather = jsonObjWarn.getString("weather");
				sColor = jsonObjWarn.getString("color");
				sImgUrl = jsonObjWarn.getString("imgurl");
				sGrade = jsonObjWarn.getString("grade");
				sContent = jsonObjWarn.getString("content");
				sStandard = jsonObjWarn.getString("bz");
				sGuide = jsonObjWarn.getString("fy");
				sTime = jsonObjWarn.getString("fbtime");
				sWeatherNo = jsonObjWarn.getString("weatherno");

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		sWeather = "";
		sColor = "";
		sGrade = "";
		sContent = "";
		sStandard = "";
		sGuide = "";
		sImgUrl = "";
		sTime = "";
		sCity = "";

		return false;
	}

	/**
	 * @Title: setJsonArray
	 * @Description: 输入json数组，解析单个预警
	 * @author yanyy
	 * @date 2012-12-3 下午04:58:26
	 * 
	 * @param jsonArray
	 * @param item
	 * @return
	 * @return boolean
	 * @throws
	 */
	public boolean setJsonArrayFirst(String jsonArray) {
		if (!TextUtils.isEmpty(jsonArray)) {
			String sWarning = null;
			try {
				JSONArray jsonarray = new JSONArray(jsonArray);
				sWarning = jsonarray.getString(0);
			} catch (Exception e) {
				sWarning = jsonArray;
			}

			return setJsonString(sWarning);
		}
		return false;
	}
}
