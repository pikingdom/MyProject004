/**   
 *    
 * @file 【日出日落信息】
 * @brief
 *
 * @<b>文件名</b>      : SunInfo
 *@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2012-3-6 下午02:41:55 
 * @n@n<b>文件描述</b>:  
 * @version  
 */
package com.calendar.CommData;

import java.util.Date;

import org.json.JSONObject;

import android.text.TextUtils;

import com.nd.calendar.util.ComfunHelp;
import com.nd.calendar.util.StringHelp;

public class SunInfo {
	private int id;
	private Date sunrise;
    private Date sunset;
    private Date mDate;
    private String mGMT = null;
    private boolean mIsToday;

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public Date getSunrise() {
        return sunrise;
    }

    public Date getSunset() {
        return sunset;
    }

    public String getSunriseString() {

        if (sunset != null) {
            return String.format("%02d:%02d", sunrise.getHours(), sunrise.getMinutes());
        } else {
            return null;
        }
    }

    public String getSunsetString() {
        if (sunset != null) {
            return String.format("%02d:%02d", sunset.getHours(), sunset.getMinutes());
        } else {
            return null;
        }
    }
    
    public boolean isToday(){
        return mIsToday;
    }

	public Date getDate() {
		return mDate;
	}
	
	public String getGMT(){
	    return mGMT;
	}
	
    public void setJsonString(String jsonString) {
        sunrise = null;
        sunset = null;
        mIsToday = false;
        if (jsonString != null) {

            try {
                JSONObject jsonObject = StringHelp.getJSONObject(jsonString);
                
                // 判断是否是今天数据
                if (jsonObject.has("sysdate")) {
                    final String sysdate = jsonObject.getString("sysdate");
                    if (!TextUtils.isEmpty(sysdate)) {
                        try {
                            mDate = ComfunHelp.getTimeDate(sysdate);
                            //把服务器时间转化成本地时间
                            mDate = ComfunHelp.getLocalServerDate(mDate);
                            final Date today = new Date(System.currentTimeMillis());
                            mIsToday = (mDate.getYear() == today.getYear())
                                    && (mDate.getMonth() == today.getMonth())
                                    && (mDate.getDay() == today.getDay());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                final String sunriseStr = jsonObject.getString("sunrise");
                final String sunsetStr = jsonObject.getString("sunset");
                sunrise = ComfunHelp.GetDate(sunriseStr, "HH:mm:ss", false);
                sunrise.setYear(0);
                sunset = ComfunHelp.GetDate(sunsetStr, "HH:mm:ss", false);
                sunset.setYear(0);
                if (jsonObject.has("gmt")){
                    mGMT = jsonObject.getString("gmt");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
