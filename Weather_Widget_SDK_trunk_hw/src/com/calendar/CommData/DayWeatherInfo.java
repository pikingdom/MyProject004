package com.calendar.CommData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

import com.nd.calendar.util.CalendarInfo;
import com.nd.calendar.util.ComfunHelp;
import com.nd.calendar.util.StringHelp;

/**
 * @ClassName: DayWeatherBase
 * @Description: TODO(日天气基类)
 * @author yanyy
 * @date 2012-4-28 下午05:53:06
 * 
 */
public class DayWeatherInfo {

    static final SimpleDateFormat DATE_FORMAT_MD = new SimpleDateFormat("MM-dd");
    static final SimpleDateFormat DATE_FORMAT_MD2 = new SimpleDateFormat("MM月dd日");

    // 整天温度情况
    public class TempInfo {
        // 白天温度
        public String strDayTemp = "";
        // 晚上温度
        public String strNightTemp = "";
        // 白天天气
        public String strDayWeather = "";
        // 晚上天气
        public String strNightWeather = "";
    }

    // 每天天气信息
    public class DayInfo {

        // 周天
        public String week = "";

        public String date = "";
        public String date2 = "";

        // 天气图标ID
//        public String imgResID;

        // 天气信息
        public String info = "";

        // 温度
        public String temperature = "";

        // 原始温度
        public String tempOrg = "";

        // 整天温度情况
        public TempInfo tempInfo;
    }

    private int mId;

    // 城市代码号
    private String mCityCode = "";

    // 时间
    private String mStrTime = "";

    // 发布时间
    //private Date mDateWeatherUpdate;

    // 时实天气描述(旧的数据和天气网数据在这里取)现在的逻辑，界面统一从实时天气取
    private String mNowWeatherInfo = "";

    // 紫外线(旧的数据和天气网数据在这里取)现在的逻辑，界面统一从实时天气取
    private String mUv = "";
    private String mUvValue = "";

    // 未来几天天气
    private ArrayList<DayInfo> mDays = new ArrayList<DayInfo>();
    
    // 是否是旧数据(天气网取的数据)
    private boolean mIsOldData = false;
    
    // 时区
    private String mGMT = null;

    public DayWeatherInfo() {
        initDay();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getCityCode() {
        return mCityCode;
    }

    public void setCityCode(String code) {
        mCityCode = code;
    }

    /* 旧的数据和天气网取数据的时候 */
    public String getNowUv() {
        return mUv;
    }

    /* 旧的数据和天气网取数据的时候 */
    public String getNowWeather() {
        return mNowWeatherInfo;
    }

    public ArrayList<DayInfo> getDays() {
        return mDays;
    }

    public void setDays(ArrayList<DayInfo> days) {
        mDays = days;
    }

//    public Date getWeatherTime() {
//        return mDateWeatherUpdate;
//    }

    public String getWeatherTimeString() {
        return mStrTime;
    }

    /**
     * @brief 【数据有效性检查】
     * 
     * @n<b>函数名称</b> :checkData
     * @param strJson
     * @return
     * @return boolean
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-4-26下午04:23:10
     */
    public static boolean checkData(String sJson) {        
        try {
            JSONObject jo = StringHelp.getJSONObject(sJson);
            if (jo == null) {
                return false;
            }

            if (isNewJson(jo)) {
                // 新接口数据检测
                if ((jo.isNull("sysdate")) || (jo.isNull("weather"))
                        || (jo.getJSONArray("weather").length() == 0)) {
                    return false;
                }
            } else {
                // 旧数据检测
                if (jo.isNull("date_y") || jo.isNull("cityid") || jo.isNull("index_uv")) {
                    return false;
                }

                for (int i = 1; i < 13; i++) {
                    if ((i < 7 && jo.isNull("temp" + i)) || jo.isNull("img_title" + i)) {
                        return false;
                    }
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* 判断是否是新接口的数据 */
    private static boolean isNewJson(JSONObject json) {
        try {
            return (json.has("weather") && json.getJSONArray("weather").length() > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Title: oldJsonAnalysis
     * @Description: TODO(旧数据的解析函数)
     * @author yanyy
     * @date 2012-7-27 上午09:47:21
     * 
     * @param jsonObject
     * @param sNowTemp
     * @return
     * @return boolean
     * @throws
     */
    private boolean oldJsonAnalysis(JSONObject jsonObject, String sNowTemp) {
        boolean breturn = false;
        DATE_FORMAT_MD.getCalendar().setTimeZone(TimeZone.getDefault());
        DATE_FORMAT_MD2.getCalendar().setTimeZone(TimeZone.getDefault());
        try {
            mStrTime = jsonObject.getString("date_y");
            if (mStrTime == null) {
                return false;
            }

            mCityCode = jsonObject.getString("cityid");
            mUvValue = jsonObject.getString("index_uv");
            if (mUvValue == null) {
                return false;
            }

            mUv = mUvValue;

            // 判断是否白天晚上
            boolean bNight = false;
            String sTemp1 = jsonObject.getString("temp1"); // 今天天气
            String[] sTempValue1 = sTemp1.split("~");

            // 正常为 高温~低温，否则是晚上的数据
            int nNumHigh = Integer.parseInt(getTempValue(sTempValue1[0]));
            int nNumLow = Integer.parseInt(getTempValue(sTempValue1[1]));
            if (nNumHigh < nNumLow) {
                bNight = true;
            }

            // 拆分温度
            String[] sTempArr = new String[15];
            // 没有摄氏度的
            String[] sTempNoCArr = new String[15];

            int nTempCount = 1;
            if (bNight) {
                // 往后移动一格
                nTempCount = 2;
            }

            for (int i = 0; i < 6; i++) {
                String sTemp = jsonObject.getString("temp" + (i + 1));
                String[] sTempValue = sTemp.split("~");

                if (sTempValue != null && sTempValue.length == 2) {
                    for (int j = 0; j < 2; j++) {
                        sTempArr[nTempCount] = sTempValue[j];
                        sTempNoCArr[nTempCount] = getTempValue(sTempValue[j]);
                        nTempCount++;
                    }
				}
            }

            // 拆分天气描述
            int nWeatherCount = 1;
            if (bNight) {
                // 往后移动一格
                nWeatherCount = 2;
            }

            String arrStrWeather[] = new String[15];
            try {
                for (int i = 1; i < 13; i++) {
                    arrStrWeather[nWeatherCount] = jsonObject.getString("img_title" + i);
                    nWeatherCount++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 服务器更新时间
            Date mDateWeatherUpdate = ComfunHelp.GetDate(mStrTime, "yyyy年MM月dd", false);
            // dateWeatherUpdate.setMonth(dateWeatherUpdate.getMonth() -1);
            // 本地时间
            Date dateSys = new Date(System.currentTimeMillis());
            dateSys.setHours(0);
            dateSys.setMinutes(0);
            dateSys.setSeconds(0);

            // 根据时间差转换成当天的时间
            long nDayDiff = (mDateWeatherUpdate.getTime() - dateSys.getTime()) / (1000 * 24 * 3600);
            if (nDayDiff > 0) {
                // strNowWeather = "";
                for (int i = 12; i >= 1; i--) {
                    if (i >= nDayDiff * 2 + 1) {
                        sTempArr[i] = sTempArr[(int) (i - nDayDiff * 2)];
                        sTempNoCArr[i] = sTempNoCArr[(int) (i - nDayDiff * 2)];
                        arrStrWeather[i] = arrStrWeather[(int) (i - nDayDiff * 2)];
                    } else {
                        sTempArr[i] = "";
                        sTempNoCArr[i] = "";
                        arrStrWeather[i] = "";
                    }
                }
            } else if (nDayDiff < 0) {
                // strNowWeather = "";
                for (int i = 1; i <= 12; i++) {
                    if (i <= 12 + nDayDiff * 2) {
                        sTempArr[i] = sTempArr[(int) (i - nDayDiff * 2)];
                        sTempNoCArr[i] = sTempNoCArr[(int) (i - nDayDiff * 2)];
                        arrStrWeather[i] = arrStrWeather[(int) (i - nDayDiff * 2)];
                    } else {
                        sTempArr[i] = "";
                        sTempNoCArr[i] = "";
                        sTempArr[i] = "";
                    }
                }
            }

            // 温度调整
            try {
                if (!TextUtils.isEmpty(sNowTemp) && TextUtils.isDigitsOnly(sNowTemp)) {
                    int iNowTemp = Integer.parseInt(sNowTemp);

                    if (bNight) {
                        nNumHigh = Integer.parseInt(sTempNoCArr[2]);
                        if (nNumHigh > iNowTemp) { // nNumHigh 为今日低温
                            sTempArr[2] = sNowTemp + "℃";
                            sTempNoCArr[2] = new String(sNowTemp);
                        }
                    } else {
                        nNumHigh = Integer.parseInt(sTempNoCArr[1]);
                        if (nNumHigh < iNowTemp) {
                            sTempArr[1] = sNowTemp + "℃";
                            sTempNoCArr[1] = new String(sNowTemp);
                        }

                        nNumLow = Integer.parseInt(sTempNoCArr[2]);
                        if (nNumLow > iNowTemp) {
                            sTempArr[2] = sNowTemp + "℃";
                            sTempNoCArr[2] = new String(sNowTemp);
                        }
                    }
                }

            } catch (Exception e) {
            	e.printStackTrace();
            }

            Date date = new Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            // 温度(22~16℃)
            for (int i = 1; i <= 6; i++) {
                // DayInfo day = mDays.get(i - 1);
                // 旧接口没有昨天数据就空着
                DayInfo day = mDays.get(i);
                if (i == 1) {
                    day.week = "今天";
                } else {
                    day.week = CalendarInfo.getWeekOfDate(date);
                }
                day.date = DATE_FORMAT_MD.format(date);// 月日
                day.date2 = DATE_FORMAT_MD2.format(date);
                
                String strDay = sTempNoCArr[i * 2 - 1];
                String StrNigh = sTempArr[i * 2];
                String strDayWeahter = arrStrWeather[i * 2 - 1];
                String strNightWeahter = arrStrWeather[i * 2];

                TempInfo temp1 = new TempInfo();

                // 该天没有数据
                if ((strDay == null || strDay.equalsIgnoreCase(""))
                        && (StrNigh == null || StrNigh.equalsIgnoreCase(""))) {
                    temp1.strDayTemp = "";
                    temp1.strNightTemp = "";
                    temp1.strDayWeather = "";
                    temp1.strNightWeather = "";

                }// 只有白天没有数据，说明是晚上
                else if (strDay == null || strDay.equalsIgnoreCase("")) {
                    day.temperature = StrNigh;
                    day.tempOrg = StrNigh;

                    day.info = strNightWeahter;
                    temp1.strDayTemp = "";
                    temp1.strNightTemp = sTempNoCArr[i * 2];
                    temp1.strDayWeather = "";
                    temp1.strNightWeather = strNightWeahter;
                } else {
                    day.temperature = strDay + " / " + StrNigh;
                    day.tempOrg = strDay + " ~ " + StrNigh;

                    if (strNightWeahter.equalsIgnoreCase(strDayWeahter)) {
                        day.info = strDayWeahter;
                    } else {
                        day.info = strDayWeahter + "转" + strNightWeahter;
                    }

                    temp1.strDayTemp = sTempNoCArr[i * 2 - 1];
                    temp1.strNightTemp = sTempNoCArr[i * 2];
                    temp1.strDayWeather = strDayWeahter;
                    temp1.strNightWeather = strNightWeahter;
                }

                try {
                    if (TextUtils.isEmpty(day.info)) { // !isEmpty
//                        day.imgResID = WeatherModule.GetFinalWeathResId(day.info, false);
//                    } else {
                        day.info = "暂无";
                        //day.imgResID = WeatherModule.WEATHER_UNKNOWN_RES;
                    }

                    if (TextUtils.isEmpty(day.temperature)) {
                        day.temperature = "暂无温度";
                        day.tempOrg = "暂无温度";
                    }

                } catch (Exception e) {
                    day.info = "";
//                    day.imgResID = null;
                    day.temperature = "";
                    day.tempOrg = "";
                }

                day.tempInfo = temp1;

                // 加一天
                cal.setTime(date);
                cal.add(Calendar.DAY_OF_MONTH, +1);
                date = cal.getTime();

            }
            // 时实天气描述
            if ((mDays.size() > 2) && (mDays.get(1).tempInfo != null)) {
                mNowWeatherInfo = mDays.get(1).tempInfo.strDayWeather;
            }

            breturn = true;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return breturn;
    }

    private void initDay() {
        DATE_FORMAT_MD.getCalendar().setTimeZone(TimeZone.getDefault());
        DATE_FORMAT_MD2.getCalendar().setTimeZone(TimeZone.getDefault());
        if ((mDays.size()) <= 0) {
            for (int i = 0; i < 7; i++) {
                DayInfo day = new DayInfo();
                mDays.add(day);
            }
        }
        Date date = ComfunHelp.getLocalCityDate(mGMT);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 从昨天开始
        cal.add(Calendar.DAY_OF_MONTH, -1);
        date = cal.getTime();

        // 温度(22~16℃)(包括昨天的数据，第一个是昨天)
        int ilen = mDays.size();
        for (int i = 0; i < ilen; i++) {
            DayInfo day = mDays.get(i);
            if (i == 0) {
                day.week = "昨天";
            } else if (i == 1) {
                day.week = "今天";
            } else {
                day.week = CalendarInfo.getWeekOfDate(date);
            }
            day.date = DATE_FORMAT_MD.format(date);// 月日
            day.date2 = DATE_FORMAT_MD2.format(date);
            day.info = "";
//            day.imgResID = WeatherModule.WEATHER_UNKNOWN_RES;
            day.temperature = "";
            day.tempOrg = "";
            day.tempInfo = null;

            // 加一天
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, +1);
            date = cal.getTime();
        }
    }

    private long dayDiff(Date d1, Date d2) {
        d1.setHours(0);
        d1.setMinutes(0);
        d1.setSeconds(0);
        d2.setHours(0);
        d2.setMinutes(0);
        d2.setSeconds(0);
        return (((d2.getTime() / 1000 - d1.getTime() / 1000)) / (24 * 3600));
    }

    /* 新接口数据解析 */
    private boolean newJsonAnalysis(JSONObject jsonObject, String sNowTemp) {
        boolean breturn = false;
        try {
            // 初始化日期
            initDay();
            // 发布时间
            mStrTime = jsonObject.getString("sysdate");
            if (TextUtils.isEmpty(mStrTime)) {
                return false;
            }

            // 服务器更新时间
            //mDateWeatherUpdate = ComfunHelp.GetDate(mStrTime, "yyyy-MM-dd HH:mm:ss", false);
            String time = mStrTime.substring(10);

            JSONArray arrWeather = jsonObject.getJSONArray("weather");
            
            // 换算成城市时区的时间当前时间
            Date yesterday = ComfunHelp.getLocalCityDate(mGMT);//new Date(System.currentTimeMillis());

            //更新时间(转化成当地时间)
            Date updateTime = ComfunHelp.getCityServerDate(ComfunHelp.getTimeDate(mStrTime), mGMT);
            
            //判断是否是今天的数据
            boolean bIsToday = (yesterday.getMonth() == updateTime.getMonth()) && (yesterday.getDay() == updateTime.getDay());
            
            // 从昨天开始
            Calendar cal = Calendar.getInstance();
            cal.setTime(yesterday);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            yesterday = cal.getTime();
            int ilen = arrWeather.length();
            int iday = mDays.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ilen; i++) {
                JSONObject jWeather = arrWeather.getJSONObject(i);
                sb.delete(0, sb.length());
                // 加上 时间 然后在换算成城市时区的时间
                String strDate = sb.append(jWeather.getString("ddate")).append(time).toString();
                Date dt = ComfunHelp.GetDate(strDate, "yyyy-MM-dd HH:mm:ss", false);
                dt = ComfunHelp.getCityServerDate(dt, mGMT);
                int diff = (int) dayDiff(yesterday, dt);
                if ((diff < iday) && (diff >= 0)) {
                    DayInfo d = mDays.get(diff);
                    TempInfo t = new TempInfo();
                    t.strDayTemp = jWeather.getString("hightemp");
                    t.strNightTemp = jWeather.getString("lowtemp");
                    
                    if (bIsToday) {
                        try {
                            if ((dt.getMonth() == updateTime.getMonth())
                                    && (dt.getDay() == updateTime.getDay())) {
                                // 判断实时温度是否有在 最高温和最低温度这个范围内
                                if (Integer.parseInt(sNowTemp) < Integer.parseInt(t.strNightTemp)) {
                                    t.strNightTemp = sNowTemp;
                                } else if (Integer.parseInt(sNowTemp) > Integer
                                        .parseInt(t.strDayTemp)) {
                                    t.strDayTemp = sNowTemp;
                                }
                            }
                        } catch (Exception e) {
                        	e.printStackTrace();
                        }
                    }
                    
                    if ((TextUtils.isEmpty(t.strNightTemp)) || (t.strNightTemp.equals("null"))){
                        t.strNightTemp = "暂无温度"; 
                    }
                    t.strDayWeather = jWeather.getString("dayweather");
                    if ((TextUtils.isEmpty(t.strDayWeather)) || (t.strDayWeather.equals("null"))){
                        t.strDayWeather = "暂无"; 
                    }
                    t.strNightWeather = jWeather.getString("nightweather");
                    if ((TextUtils.isEmpty(t.strNightWeather)) || (t.strNightWeather.equals("null"))){
                        t.strNightWeather = "暂无"; 
                    }
                    d.tempInfo = t;
                    sb.delete(0, sb.length());
                    if ((t.strDayWeather.equalsIgnoreCase(t.strNightWeather)) || (t.strNightWeather.equals("暂无天气"))) {
                        d.info = t.strDayWeather;
                    } else {
                        d.info = sb.append(t.strDayWeather).append("转").append(t.strNightWeather)
                                .toString();
                    }

                    if (!t.strNightTemp.equals("暂无温度")) {
                        sb.delete(0, sb.length());
                        d.temperature = sb.append(t.strDayTemp).append(" / ").append(
                                t.strNightTemp).append("℃").toString();
                        sb.delete(0, sb.length());
                        d.tempOrg = sb.append(t.strDayTemp).append(" ~ ").append(t.strNightTemp)
                                .toString();
                    } else {
                        sb.delete(0, sb.length());
                        d.temperature = sb.append(t.strDayTemp).append("℃").toString();
                        d.tempOrg = d.temperature;
                    }

                    // 解析图标
                    try {
                        if (TextUtils.isEmpty(d.info)) { // !isEmpty
//                            d.imgResID = WeatherModule.GetFinalWeathResId(d.info, false);
//                        } else {
                            d.info = "暂无";
//                            d.imgResID = WeatherModule.WEATHER_UNKNOWN_RES;
                        }

                        if (TextUtils.isEmpty(d.temperature)) {
                            d.temperature = "暂无温度";
                            d.tempOrg = "暂无温度";
                        }
                    } catch (Exception e) {
                        d.info = "";
//                        d.imgResID = WeatherModule.WEATHER_UNKNOWN_RES;
                        d.temperature = "";
                        d.tempOrg = "";
                    }
                }

            }
            breturn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return breturn;
    }

    /**
     * @brief
     * 
     * @n<b>函数名称</b> :setJsonString
     * @param strJson
     * @return
     * @return boolean
     * @<b>修改</b> : 陈希
     * @<b>创建时间</b> : 2012-4-25下午03:53:04
     */
    public boolean setJsonString(String strJson, String sNowTemp, String gmt) { // 额外增加实时温度
        boolean breturn = false;
        mGMT = gmt;
        JSONObject jsonObject;
        try {
            jsonObject = StringHelp.getJSONObject(strJson);
            if (jsonObject != null) {
            	mIsOldData = !isNewJson(jsonObject);
                if (!mIsOldData) {
                    // 新接口返回的数据
                    breturn = newJsonAnalysis(jsonObject, sNowTemp);
                } else {
                    // 旧接口数据(或者天气网请求的数据)
                    breturn = oldJsonAnalysis(jsonObject, sNowTemp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return breturn;
    }

    private static String getTempValue(String sTemp) {
        int nPost = sTemp.indexOf("℃", 0);
        if (nPost > -1) {
            return sTemp.substring(0, nPost);
        } else {
            return "";
        }
    }
    
    /*是否是旧数据或者天气网取的数据*/
    public boolean isOldData(){
        return mIsOldData;
    }
}
