package com.calendar.CommData;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.text.TextUtils;

import com.calendar.CommData.DayWeatherInfo.DayInfo;
import com.nd.calendar.module.WeatherModule;
import com.nd.calendar.util.ComfunHelp;

/**
 * @ClassName: CityWeatherInfo
 * @Description: TODO(城市天气信息)
 * @author yanyy
 * @date 2012-4-13 上午10:46:03
 */
public class CityWeatherInfo {

    public static final int TYPE_NORMAL = 0;
    
    /*插件天气数据*/
    public static final int TYPE_WIDGET = 1;
    
    /*通知栏天气数据*/
    public static final int TYPE_PANDAHOME_NOTIFY = 2;
    
    private int id;

    // 省份
    private String mStrProvName;
    // 城市名称
    private String mStrText;
    // 城市代码
    private String mStrCode;

    // 实时天气信息
    private RealTimeWeatherInfo mRealTimeWeather;

    // 四天天气信息
    private DayWeatherInfo mWeatherInfo;

    // 预警信息
    private WarningInfo mWarningInfo;

    // 日出日落
    private SunInfo mSunInfo;

    // PM指数
    private PMIndex mPmIndex;

    // 发布时间(实时天气的发布时间)
    private String mPublishTimeText;

    // 更新时间
    private String mNowWeatherTime;

    // 判断是否已经更新到界面
    private boolean mRefreshToView = false;

    // 是否是强制更新
    private boolean mIsForceUpdate = false;

    private boolean mbFromBackup = false;

    // 正在更新数据
    private boolean mUpdating = false;
    // 网络是否出错
    private boolean mNetSuccess = true;

    // 最后一次是否是晚上
    private boolean mLastIsNight;

    // 判断数据是否过期
    // private boolean mIsTimeOut = false;
    
    // 用来缓存天气指数
    private String mWeatherIndex = null;

    // 数据类型
    private int mType = TYPE_NORMAL;

    public CityWeatherInfo() {
    }

    public CityWeatherInfo(int type) {
    	mType = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*获取城市时区*/
    public String getCityGMT() {
        return mSunInfo != null ? mSunInfo.getGMT() : null;
    }
    
    private void ayalysisNowWeather(CityWeatherJson c) {
        if (mRealTimeWeather == null) {
            mRealTimeWeather = new RealTimeWeatherInfo();
        }
        String strNowweath;

        mRealTimeWeather.setCityCode(mStrCode);

        strNowweath = c.getNowWeatherJson();

        // if (TextUtils.isEmpty(strNowweath)) {
        // strNowweath = "{\"cityid\":" + mStrCode + ",\"temp\":\"暂无实况\",\"WD\":\"暂无实况\","
        // + "\"WS\":\"暂无实况\",\"SD\":\"暂无实况\",\"WSE\":\"暂无\",\"time\":\"暂无\"}";
        // }

        if (!TextUtils.isEmpty(strNowweath)) {
            mRealTimeWeather.setJsonString(strNowweath, getCityGMT());
        }
    }

    private void ayalysisWeather(CityWeatherJson c) {
        if (mWeatherInfo == null) {
            mWeatherInfo = new DayWeatherInfo();
        }
        mWeatherInfo.setCityCode(mStrCode);
        mWeatherInfo.setId(id);

        final String strWeathJson = c.getDayWeatherJson();
        if (strWeathJson != null && strWeathJson.length() > 0) {

            // 增加温度调整
            String sNowTemp = null;
            if (mRealTimeWeather != null) {
                // 旧数据和天气网取的数据 才用到这个
                sNowTemp = mRealTimeWeather.getTempValue();
            }

            mWeatherInfo.setJsonString(strWeathJson, sNowTemp, getCityGMT());

            if (mRealTimeWeather != null) {
                // 因为旧数据和天气网取的数据是放这里(mWeatherInfo)
                if (TextUtils.isEmpty(mRealTimeWeather.getNowWeather())) {
                    mRealTimeWeather.setNowWeather(mWeatherInfo.getNowWeather());
                }
                
                // 因为旧数据和天气网取的数据是放这里(mWeatherInfo)
                if (TextUtils.isEmpty(mRealTimeWeather.getUv())) {
                    mRealTimeWeather.setUv(mWeatherInfo.getNowUv());
                }
            }
        }
    }

    private void ayalysisWarning(CityWeatherJson c) {
        String sWarning = c.getWarningJson();
        mWarningInfo = null;
        
        if (!TextUtils.isEmpty(sWarning)) {
            mWarningInfo = new WarningInfo();
            if (mWarningInfo.setJsonArrayFirst(sWarning)){
                mWarningInfo.setCity(c.getCode());
            }else{
                mWarningInfo = null;
            }           
        }
    }

    private void ayalysinsSun(CityWeatherJson c) {
        final String sSunJson = c.getSunJson();
        mSunInfo = null;
        if (!TextUtils.isEmpty(sSunJson)) {
            mSunInfo = new SunInfo();
            mSunInfo.setJsonString(sSunJson);
        }
    }

    /**
     * @brief 【解析PM指数】
     * @n<b>函数名称</b> :ayalysisPMIndex
     * @param c
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>修改</b> :
     * @<b>创建时间</b> : 2012-6-7上午09:56:28
     * @<b>修改时间</b> :
     */
    private void ayalysisPMIndex(CityWeatherJson c) {
        final String sPMJson = c.getPMJson();
        mPmIndex = null;

        if ((!TextUtils.isEmpty(sPMJson))) {// && (!mIsTimeOut)
            mPmIndex = new PMIndex();
            mPmIndex.setJsonString(sPMJson);
        }
    }

    public boolean inavailable() {
    	
    	if (mWeatherInfo == null) { // TextUtils.isEmpty(mStrText) || 
			return true;
		}
    	
    	ArrayList<DayInfo> days = mWeatherInfo.getDays();
    	if (days == null || days.size() < 1) {
    		return true;
		}
    	
    	DayInfo di = days.get(1);
    	if (TextUtils.isEmpty(di.tempOrg)) { // || TextUtils.isEmpty(di.tempInfo.strDayTemp)
    		return true;
		}
    	
    	return false;
    }
    
    public boolean updateFailure() {
    	return (mRealTimeWeather != null && TextUtils.isEmpty(mRealTimeWeather.getNowWeather()));
    }
    
    public void setCityJson(CityWeatherJson city) {
        id = city.getId();
        mStrProvName = city.getProvName();
        // 城市名称
        mStrText = city.getName();
        // 城市代码
        mStrCode = city.getCode();
        
        //实时天气更新时间
        mNowWeatherTime = city.getNowWeatherTime();
        
        mWeatherIndex = null;
        
        // 日出日落
        ayalysinsSun(city);

        // 实时天气
        ayalysisNowWeather(city);

        if (mType == TYPE_PANDAHOME_NOTIFY) {
            return;
        }
        
        // 天气信息
        ayalysisWeather(city);

        // PM指数
        ayalysisPMIndex(city);
        
        // 预警信息
        ayalysisWarning(city);

        if (mType == TYPE_WIDGET) {
            return;
        }

        // 更新时间
        String sTime = mRealTimeWeather.getTime();
        if (TextUtils.isEmpty(sTime) || (sTime.length() < 16)) {
            sTime = city.getNowWeatherTime();
        }
        getPublishTimeText(sTime);

        // 设置为未刷新
        setIsRefreshToView(false);

        // 是否强制更新
        setIsForceUpdate(false);
    }

    public RealTimeWeatherInfo getRealTimeWeather() {
        return mRealTimeWeather;
    }

    public DayWeatherInfo getWeatherInfo() {
        return mWeatherInfo;
    }

    // public Date getDateWeather() {
    // return mDateWeather;
    // }

    public boolean isNight() {
        mLastIsNight = WeatherModule.isNightTime(mSunInfo);
        return mLastIsNight;
    }

    public boolean dayNightChange() {
        // 判断是否有白天晚上切换
        boolean isNight = WeatherModule.isNightTime(mSunInfo);
        return (mLastIsNight != isNight);
    }

    private void getPublishTimeText(String time) {
        try {
            if (mNetSuccess) {
                Format formatter = null;
                Date publishTime = null;

                if (!TextUtils.isEmpty(time)) {
                    publishTime = ComfunHelp.GetDate(time, "yyyy-MM-dd HH:mm", false);
                    Date now = new Date(System.currentTimeMillis());

                    if (now.getYear() != publishTime.getYear()) {
                        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    } else if (now.getMonth() != publishTime.getMonth()
                            || now.getDate() != publishTime.getDate()) {
                        formatter = new SimpleDateFormat("MM-dd HH:mm");
                    } else {
                        formatter = new SimpleDateFormat("HH:mm");
                    }
                    StringBuilder sb = new StringBuilder();
                    if (!ComfunHelp.isLocalTimeZome(getCityGMT())){
                        sb.append("当地时间");
                    }
                    sb.append(formatter.format(publishTime)).append("发布").append(mbFromBackup ? "." : "");
                    mPublishTimeText = sb.toString();
                } else {
                    mPublishTimeText = "等待更新";
                }
            } else {
                mPublishTimeText = "更新失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            mPublishTimeText = "更新失败";
        }
        
        //Log.e("getPublishTime", getPublishTime());
        //Log.e("getPublishDate", getPublishDate());
    }

    public WarningInfo getWarningInfo() {
        return mWarningInfo;
    }

    public PMIndex getPmIndex() {
        return mPmIndex;
    }

    public String getTemperature() {
        return mRealTimeWeather.getTemp();
    }

    public String getTemperatureValue() {
        return mRealTimeWeather.getTempValue();
    }

    public String getSaveTime() {
        return mPublishTimeText;
    }

    public boolean getIsRefreshToView() {
        return mRefreshToView;
    }

    public void setIsRefreshToView(boolean b) {
        mRefreshToView = b;
    }

    public void setIsForceUpdate(boolean b) {
        mIsForceUpdate = b;
    }

    public boolean isForceUpdate() {
        return mIsForceUpdate;
    }

    public void setSunInfo(SunInfo sun) {
        mSunInfo = sun;
    }

    public SunInfo getSunInfo() {
        return mSunInfo;
    }

    public void setProvName(String provname) {
        mStrProvName = provname;
    }

    public String getProvName() {
        return mStrProvName;
    }

    public void setCityName(String cityname) {
        mStrText = cityname;
    }

    public String getCityName() {
        return mStrText;
    }

    public void setCityCode(String citycode) {
        mStrCode = citycode;
    }

    public String getCityCode() {
        return mStrCode;
    }

    public void setFromBackup(boolean b) {
        mbFromBackup = b;
    }

    public boolean isFromBackup() {
        return mbFromBackup;
    }

    public void setUpdating(boolean b) {
        mUpdating = b;
    }

    public boolean isUpdating() {
        return mUpdating;
    }

    public void setNetSuccess(boolean b) {
        mNetSuccess = b;
    }

    public boolean isNetSuccess() {
        return mNetSuccess;
    }

    public String getNowWeatherTime() {
        return mNowWeatherTime;
    }

    public boolean isNullData() {
        return (mNowWeatherTime == null);
    }

    // 实时天气信息
    public void setRealTimeWeather(RealTimeWeatherInfo r) {
        mRealTimeWeather = r;
        mWeatherIndex = null;
    }

    // 四天天气信息
    public void setWeatherInfo(DayWeatherInfo d) {
        mWeatherInfo = d;
        mWeatherIndex = null;
    }

    // 预警信息
    public void setWarningInfo(WarningInfo d) {
        mWarningInfo = d;
        mWeatherIndex = null;
    }

    // PM指数
    public void setPMIndex(PMIndex pmIndex) {
        mPmIndex = pmIndex;
        mWeatherIndex = null;
    }
    
    public void setWeatherIndex(String index){
        mWeatherIndex = index;
    }
    public String getWeatherIndex(){
        return mWeatherIndex;
    }
    
    // 发布时间
    public String getPublishTime(){
        String time = "";
        String sTime = mRealTimeWeather.getTime();
        if (TextUtils.isEmpty(sTime) || (sTime.length() < 16)) {
            sTime = mNowWeatherTime;
        }
        if ((sTime != null) && (sTime.length() >= 16)){
            time = sTime.substring(11, 16);
        }
        return time;
    }
    
    // 发布日期
    public String getPublishDate(){
        String time = "";
        String sTime = mRealTimeWeather.getTime();
        if (TextUtils.isEmpty(sTime) || (sTime.length() < 16)) {
            sTime = mNowWeatherTime;
        }
        if ((sTime != null) && (sTime.length() >= 10)){
            time = sTime.substring(0, 10);
        }
        return time;
    }
}
