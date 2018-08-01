
package com.calendar.CommData;

/** 
 * @ClassName: CityWeatherJson 
 * @Description: TODO(天气json信息) 
 * @author yanyy
 * @date 2012-4-20 上午11:43:29 
 *
 */
public class CityWeatherJson extends BaseCityInfo {

    /* 四天天气 */
    private String mStrDayWeatherJson;

    /* 实时天气 */
    private String mStrNowWeatherJson;

    /* 指数信息 */
    private String mStrIndexJson;

    /* 日出日落与Index时间同步 */
    private String mStrSunJson;

    /* 预警信息 */
    private String mStrWarningJson;

    /* PM信息 */
    private String mStrPMJson;

    
    /* 天气更新时间 */
    private String mStrDayWeatherTime;

    /* 实时天气更新时间 */
    private String mStrNowWeatherTime;

    /* 指数更新时间 */
    private String mStrIndexTime;

    /* 预警更新时间 */
    private String mStrWarnTime;

    /* 日出日落更新时间 */
    private String mStrSunTime;

    /* PM更新时间 */
    private String mStrPMTime;

    
    public void setDayWeatherJson(String json) {
        mStrDayWeatherJson = json;
    }

    public String getDayWeatherJson() {
        return mStrDayWeatherJson;
    }

    public void setNowWeatherJson(String json) {
        mStrNowWeatherJson = json;
    }

    public String getNowWeatherJson() {
        return mStrNowWeatherJson;
    }

    public void setIndexJson(String json) {
        mStrIndexJson = json;
    }

    public String getIndexJson() {
        return mStrIndexJson;
    }

    public String getSunJson() {
        return mStrSunJson;
    }

    public void setSunJson(String json) {
        mStrSunJson = json;
    }

    public String getWarningJson() {
        return mStrWarningJson;
    }

    public void setWarningJson(String json) {
        mStrWarningJson = json;
    }

    public String getPMJson() {
        return mStrPMJson;
    }

    public void setPMJson(String json) {
    	mStrPMJson = json;
    }
    
    
    public String getDayWeatherTime() {
        return mStrDayWeatherTime;
    }

    public void setDayWeatherTime(String time) {
        mStrDayWeatherTime = time;
    }

    public String getNowWeatherTime() {
        return mStrNowWeatherTime;
    }

    public void setNowWeatherTime(String time) {
        mStrNowWeatherTime = time;
    }

    public String getIndexTime() {
        return mStrIndexTime;
    }

    public void setIndexTime(String time) {
        mStrIndexTime = time;
    }

    public String getWarnTime() {
        return mStrWarnTime;
    }

    public void setWarnTime(String time) {
        mStrWarnTime = time;
    }

    public String getSunTime() {
        return mStrSunTime;
    }

    public void setSunTime(String time) {
        mStrSunTime = time;
    }
    
    public String getPMTime() {
        return mStrPMTime;
    }
    
    public void setPMTime(String time) {
        mStrPMTime = time;
    }
}
