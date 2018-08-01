package com.calendar.CommData;

import java.util.Date;

import org.json.JSONObject;

import android.text.TextUtils;

import com.nd.calendar.util.ComfunHelp;
import com.nd.calendar.util.StringHelp;

/**
 * @ClassName: RealTimeWeatherInfo
 * @Description: TODO(实时天气信息)
 * @author yanyy
 * @date 2012-4-19 上午11:25:02
 * 
 */
public class RealTimeWeatherInfo {
    // 城市代码号
    private String mCityCode = "";
    // 实时温度
    private String mTempString = "N/A";
    // 实时温度
    private String mTempValue = "N/A";
    // 实时风级
    private String mWind = "";
    // 湿度
    private String mHumidity = "";
    private String mHumidityValue = "";
    // 紫外线
    private String mUv = "";

    // 实时天气
    private String mWeather = "";

    // 发布时间
    private String mStrTime;
    
    // 所在时区
    private String mGMT = null;

    public RealTimeWeatherInfo() {
        
    }

    public static boolean checkData(String sJson) {
        try {
            JSONObject jo = StringHelp.getJSONObject(sJson);
            if (jo != null) {
                if (isNewJson(jo)) {
                    return !(jo.isNull("nowweather") || jo.isNull("temp") || jo.isNull("sysdate")
                            || jo.isNull("wd") || jo.isNull("ws") || jo.isNull("sd") || jo
                            .isNull("uv"));
                } else {
                    return !(jo.isNull("cityid") || jo.isNull("temp") || jo.isNull("time")
                            || jo.isNull("WD") || jo.isNull("WS") || jo.isNull("SD"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /* 判断是否是新接口数据 */
    private static boolean isNewJson(JSONObject json) {
        if ((json.has("nowweather")) && (json.has("uv"))) {
            return true;
        }
        return false;
    }

    /**
     * @Title: setJsonString
     * @Description: TODO(解析json)
     * @author yanyy
     * @date 2012-4-19 下午02:08:24
     * 
     * @param strJson
     * @return
     * @return boolean
     * @throws
     */
    public boolean setJsonString(String strJson, String gmt) {
        boolean breturn = false;
        mGMT = gmt;
        JSONObject jsonObject;
        try {
            jsonObject = StringHelp.getJSONObject(strJson);

            if (isNewJson(jsonObject)) {
                breturn = newJsonAnalysis(jsonObject);
            } else {
                // 旧数据或者新接口数据
                breturn = oldJsonAnalysis(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return breturn;
    }

    private boolean newJsonAnalysis(JSONObject jsonObject) {
        boolean breturn = false;
        try {
            mStrTime = jsonObject.getString("sysdate");
            mUv = jsonObject.getString("uv");
            mHumidity = jsonObject.getString("sd");
            mTempValue = jsonObject.getString("temp");
            mWeather = jsonObject.getString("nowweather");
            String wind1 = jsonObject.getString("wd");
            String wind2 = jsonObject.getString("ws");

            if (mTempValue == null || mStrTime == null || wind1 == null || wind2 == null
                    || mHumidity == null) {
                return false;
            }
            // 重组数据
            if ((!TextUtils.isEmpty(mTempValue)) && (!mTempValue.equals("暂无实况"))
                    && (!mTempValue.equals("N/A"))) {
                mTempString = mTempValue + "℃";
            } else {
                mTempString = "N/A";
            }

            if (wind1.equals(wind2) && wind1.equals("暂无实况")) {
                mWind = "暂无实况";
            } else {
                mWind = wind1 + wind2;
            }

            int nPost = mHumidity.indexOf("%", 0);
            if (nPost != -1) {
                mHumidityValue = mHumidity.substring(0, nPost);
            }
            
            //把服务器时间转化成当地时间
            if (mStrTime.length() >= 16){
                Date d = ComfunHelp.getTimeDate(mStrTime);
                mStrTime = ComfunHelp.formatDateTime(ComfunHelp.getCityServerDate(d, mGMT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return breturn;
    }

    private boolean oldJsonAnalysis(JSONObject jsonObject) {
        boolean breturn = false;
        try {
            mCityCode = jsonObject.getString("cityid");
            mTempValue = jsonObject.getString("temp");
            mStrTime = jsonObject.getString("time");
            if (jsonObject.has("weather")){
                mWeather = jsonObject.getString("weather");
            }
            
            if (jsonObject.has("index_uv")){
                mUv = jsonObject.getString("index_uv");
            }
            
            String wind1 = jsonObject.getString("WD");
            String wind2 = jsonObject.getString("WS");
            String sHum = jsonObject.getString("SD");

            if (mTempValue == null || mStrTime == null || wind1 == null || wind2 == null
                    || sHum == null) {
                return false;
            }

            // 重组数据
            if ((!TextUtils.isEmpty(mTempValue)) && (!mTempValue.equals("暂无实况"))
                    && (!mTempValue.equals("N/A"))) {
                mTempString = mTempValue + "℃";
            } else {
                mTempString = "N/A";
            }

            if (wind1.equals(wind2) && wind1.equals("暂无实况")) {
                mWind = "暂无实况";
            } else {
                mWind = wind1 + wind2;
            }

            mHumidity = sHum;
            int nPost = sHum.indexOf("%", 0);
            if (nPost != -1) {
                mHumidityValue = sHum.substring(0, nPost);
            }
            
            //把服务器时间转化成当地时间
            if (mStrTime.length() >= 16){
                Date d = ComfunHelp.getTimeDate(mStrTime);
                mStrTime = ComfunHelp.formatDateTime(ComfunHelp.getCityServerDate(d, mGMT));
            }

            breturn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return breturn;
    }

    public String getCityCode() {
        return mCityCode;
    }

    public void setCityCode(String code) {
        mCityCode = code;
    }

    public String getTemp() {
        return mTempString;
    }

    public String getTempValue() {
        return mTempValue;
    }

    public String getWind() {
        return mWind;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public String getHumidityValue() {
        return mHumidityValue;
    }

    public String getUv() {
        return mUv;
    }

    public void setUv(String uv) {
        mUv = uv;
    }

    public String getNowWeather() {
        return mWeather;
    }

    public void setNowWeather(String weather) {
        mWeather = weather;
    }

    public String getTime() {
        return mStrTime;
    }

    public boolean isNullData() {
        return ((TextUtils.isEmpty(mTempString)) || (mTempString.equals("N/A")));
    }
}
