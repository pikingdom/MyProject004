/**   
*    
* @file
* @brief
*
* @<b>文件名</b>      : WidgetSkinBuilder
*@n@n<b>版权所有</b>: 网龙天晴程序部应用软件开发组
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2012-7-6 下午05:49:51 
* @n@n<b>文件描述</b>:  
* @version  
*/
package com.nd.weather.widget.skin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.calendar.CommData.CityWeatherInfo;
import com.calendar.CommData.DateInfo;
import com.calendar.CommData.DayWeatherInfo;
import com.calendar.CommData.DayWeatherInfo.DayInfo;
import com.calendar.CommData.PMIndex;
import com.calendar.CommData.PMIndex.PMIndexInfo;
import com.calendar.CommData.RealTimeWeatherInfo;
import com.calendar.CommData.WarningInfo;
import com.calendar.CommData.YjcInfo;
import com.nd.calendar.Control.CalendarContext;
import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.module.CalendarModule;
import com.nd.calendar.module.WeatherModule;
import com.nd.calendar.util.CalendarInfo;
import com.nd.calendar.util.ComfunHelp;
import com.nd.hilauncherdev.theme.module.ThemeModuleHelper;
import com.nd.hilauncherdev.theme.module.ThemeModuleItem;
import com.nd.hilauncherdev.theme.pref.ThemeSharePref;
import com.nd.weather.widget.R;
import com.nd.weather.widget.WeatherLinkTools;
import com.nd.weather.widget.WidgetGlobal;
import com.nd.weather.widget.WidgetUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class WidgetSkinBuilder
{
	// 基本属性
    private Context mContext;
	private CalendarContext mCalCxt;
	private AssetManager mAstMgr; // 注意，mAstMgr != null 表示使用
	private Resources mRes;
//    private SharedPreferences set;
    
    private int mWidgetType;		
	private boolean bUpdating = false;
	
	// 皮肤类型
    private WidgetLoadedSkinInfo mWidgetSkinInfo;
	    
	private Canvas canvas;
	// 插件数据
    private CityWeatherInfo m_cityWeather = null;
    
    private WeatherLinkTools mWlt;
    
	static PaintFlagsDrawFilter pfdFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	boolean mbNet = true;
	
	public int[] m_NumberIds = new int[] {
			R.drawable.n0, R.drawable.n1, R.drawable.n2,
			R.drawable.n3, R.drawable.n4, R.drawable.n5,
			R.drawable.n6, R.drawable.n7,
			R.drawable.n8, R.drawable.n9 };
	
	/**
	 * 字体
	 */
	public static Typeface typeface = null;
	public static Boolean isFontChanged = Boolean.valueOf(false);

    public WidgetSkinBuilder(Context context, int nWidgetType) {
    	mContext = context;
        mWidgetType = nWidgetType;
        m_cityWeather = new CityWeatherInfo(CityWeatherInfo.TYPE_WIDGET);
        mbNet = HttpToolKit.isNetworkAvailable(mContext);
        mWlt = WeatherLinkTools.getInstance(context);
    }
    
    public int getWigetType () {
    	return mWidgetType;
    }
    
    public void setUpdating(boolean bState) {
    	bUpdating = bState;
    }
    
    /**
     * @brief 【设置皮肤信息】
     * @n<b>函数名称</b>     :SetSkinInfo
     * @param skinSetting
     * @param bOutSkin
     * @param sLoadedSkinPath
     * @param nType
     * @return    void   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2012-7-6下午06:23:44
     * @<b>修改时间</b>      :
    */
    public void SetSkinInfo(WidgetLoadedSkinInfo wsi) {
    	mWidgetSkinInfo = wsi;
        mCalCxt = CalendarContext.getInstance(mContext);
        mRes = mContext.getResources();
        
        if (!wsi.bUserSkin) {
            mAstMgr = wsi.assetMgr;
        	if (mAstMgr == null) {
        		mAstMgr = mContext.getAssets();
			}
        }
    }
    
    public void setWeatherInfo(CityWeatherInfo cityWeather) {
    	m_cityWeather = cityWeather;
    }
    
    /**
     * @brief 【绘制插件】
     * @n<b>函数名称</b>     :getWidgetBitmap
     * @return
     * @return    Bitmap   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2012-7-6下午06:12:28
     * @<b>修改时间</b>      :
    */
    public Bitmap getWidgetBitmap() {
    	Bitmap widgetBmp = null;

        try {          
            // 绘制
            widgetBmp = Bitmap.createBitmap(
            		mWidgetSkinInfo.skinConfig.getWidgetWidth(),
            		mWidgetSkinInfo.skinConfig.getWidgetHeight(),
            		Config.ARGB_8888);
            
            canvas = new Canvas(widgetBmp);
			canvas.setDrawFilter(pfdFilter); 

            drawBackround();

            // 绘制图片元素
            DrawImage();

            // 填充基本变量信息
            prepateData(); // setData(); // 

            // 绘制文本
            DrawText();
            
            // 保存
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
            
        } catch (Exception e) {
            e.printStackTrace();
            canvas = null;
        } catch (OutOfMemoryError e) {
        	e.printStackTrace();
        	canvas = null;
		}

		return widgetBmp;
    }

    
    final static void safeCloseStream(java.io.Closeable stream) {
		if (stream != null) {
            try {
            	stream.close();
			} catch (IOException e) {
			}
		}
    }
    
    // ////////////////////////////////////////////////////////////////////////
    
    private static final int PM_INDEX_S = 17;
    private static final int PM_INDEX_E = 20;
    private static final int TEMP_INDEX_S = 3;
    private static final int TEMP_INDEX_E = 12;
    private static final int NOW_TEMP_INDEX = 2;
    
    private final static String[] TEXT_KEYS = {
    	// 0
        WidgetRuleId.CITY_NAME_QUOTE,
 
        // 1
        WidgetRuleId.ND_WEATHER_TEMP_DESP_NOW_QUOTE, // 实时天气
        WidgetRuleId.ND_WEATHER_TEMP_NOW_QUOTE,		 // 实时温度

        // 3
        // 晚上温度
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_1_TEMP_LOWEST_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_2_TEMP_LOWEST_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_3_TEMP_LOWEST_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_4_TEMP_LOWEST_QUOTE,
        WidgetRuleId.W_FD5_NIGHT_TEMP,

        // 8
        // 白天温度
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_1_TEMP_HEIGHEST_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_2_TEMP_HEIGHEST_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_3_TEMP_HEIGHEST_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_4_TEMP_HEIGHEST_QUOTE,
        WidgetRuleId.W_FD5_DAY_TEMP,

        // 13
        WidgetRuleId.W_DAY_TEMP,	// 今日温度
        WidgetRuleId.ND_WEATHER_WIND_QUOTE, // 实时风级
        WidgetRuleId.W_CD_UV, // 紫外线
        WidgetRuleId.ND_WEATHER_HUMIDITY_QUOTE, // 湿度
        
        // 17
        WidgetRuleId.ND_WEATHER_PM_SOURCE,		// PM指数源
        WidgetRuleId.ND_WEATHER_PM_AIR_GRD,		// PM指数空气质量
		WidgetRuleId.ND_WEATHER_PM_AIR_TYPE,	// PM指数空气质量类型
		WidgetRuleId.ND_WEATHER_PM_25, 			// PM2.5
		
		// 21
        WidgetRuleId.ND_WEATHER_PUBLISH_TIME_QUOTE, // 发布日期
        WidgetRuleId.ND_WEATHER_UPDATE_TIME_QUOTE, // 发布时间
        WidgetRuleId.ND_WEATHER_PUBLISH_DATE_QUOTE,
         
        // 24
		WidgetRuleId.W_CD_YI_TEXT,
		WidgetRuleId.W_CD_JI_TEXT, // 忌信息
		WidgetRuleId.W_CD_CHONG_TEXT, // 冲信息
		
        // 27
        WidgetRuleId.ND_WEEK_QUOTE, 	// 星期
        WidgetRuleId.ND_DATE_NL_QUOTE,	// 农历
        WidgetRuleId.ND_DATE_YEAR, 		// 年份
        WidgetRuleId.ND_DATE_YEAR_NL,
        
        // 31
        // 日期
        WidgetRuleId.ND_DATE_SHORT_TYPE_3_QUOTE, // (09/27)
        WidgetRuleId.ND_DATE_SHORT_TYPE_2_QUOTE, // 09-27
        WidgetRuleId.ND_DATE_SHORT_TYPE_1_QUOTE, // 09月27日

        // 34
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_1_WEEK_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_2_WEEK_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_3_WEEK_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_4_WEEK_QUOTE,
        WidgetRuleId.W_FD5_WEEK,
        
        // 39
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_1_DESP_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_2_DESP_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_3_DESP_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_4_DESP_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_5_DESP_QUOTE,
        
        // 44
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_1_TYPE_1_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_2_TYPE_1_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_3_TYPE_1_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_4_TYPE_1_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_5_TYPE_1_QUOTE,
        
        // 49
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_1_TYPE_2_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_2_TYPE_2_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_3_TYPE_2_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_4_TYPE_2_QUOTE,
        WidgetRuleId.ND_WEATHER_FORECAST_DATE_5_TYPE_2_QUOTE,

        // 54
        WidgetRuleId.ND_DATE_JQ_QUOTE, // 节气
        
        // 55
        WidgetRuleId.ND_WARN // 天气预警
    };
    
    YjcInfo mYjcInfo = null;
    boolean bHasCity = false;
    DateInfo mDateInfo = null;
    ArrayList<DayInfo> mDays = null;
    String [] lunarInfo = null;
    PMIndexInfo pmIndexInfo = null;
   
    private final void prepateData() {
        DayWeatherInfo dayInfo = m_cityWeather.getWeatherInfo();
        mDays = dayInfo.getDays();
        mDateInfo = CalendarInfo.GetSysDateInfo();
        bHasCity = !TextUtils.isEmpty(m_cityWeather.getCityCode());
        
        mYjcInfo = null;
        lunarInfo = null;
        pmIndexInfo = null;
    }
    
    //////////////////////////////////////////////////////
    /**
     * @brief 【宜忌冲】
     * @n<b>函数名称</b>     :getYiJiChong
     * @return
     * @return    YjcInfo   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2013-3-7下午02:47:42
     * @<b>修改时间</b>      :
    */
    private final YjcInfo getYiJiChong() {
    	if (mYjcInfo == null) {
    		mYjcInfo = new YjcInfo();
            int ret = mCalCxt.Get_CalendarMdl_Interface().getYJCInfo(mDateInfo, mYjcInfo);
            if (ret != CalendarModule.CM_SUCCESS) {
            	mYjcInfo.setStrYi("");
            	mYjcInfo.setStrJi("");
            	mYjcInfo.setStrChong("");
    		}
		}
    	
    	return mYjcInfo;
    }
    
    final String getNightTemp(int day) {
    	return mDays.get(day).tempInfo.strNightTemp;
    }
   
    final String getDayTemp(int day) {
		return mDays.get(day).tempInfo.strDayTemp;
    }
    
    final boolean getLunarInfo() {
    	if (lunarInfo == null) {
    		String sLunarInfo = CalendarInfo.getLunarEx(mDateInfo);
            if (!TextUtils.isEmpty(sLunarInfo)) {
            	lunarInfo = sLunarInfo.split("年 ");
    		}
		}
    	
    	return (lunarInfo != null && lunarInfo.length > 1);
    }
    
    /**
     * @brief 【获取PM指数】
     * @n<b>函数名称</b>     :getPMInfo
     * @return
     * @return    PMIndexInfo   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2013-3-7下午02:23:24
     * @<b>修改时间</b>      :
    */
    final PMIndexInfo getPMInfo() {
    	if (pmIndexInfo != null) {
    		return pmIndexInfo;
		}
    	
        PMIndex pmIndex = m_cityWeather.getPmIndex();
        if (pmIndex == null) {
        	return null;
		}

        int iInfo = pmIndex.getSourceInfo();
        if (iInfo != PMIndex.PM_SOURCE_NULL) {
            // 选择适合的源
            if (iInfo == PMIndex.PM_SOURCE_ALL) {
                // 美使馆源优先
                SharedPreferences st = WidgetUtils.getSetting(mContext, ConfigsetData.CONFIG_NAME);
                String sUS = st.getString(ConfigsetData.CONFIG_NAME_KEY_PM_SOURCE, "");
                if (TextUtils.isEmpty(sUS) || ConfigsetData.CONFIG_DEFAULT_PM_SOURCE.equals(sUS)) {
                    pmIndexInfo = pmIndex.getUSSource();
                } else {
                    pmIndexInfo = pmIndex.getGovSource();
                }
            } else {
                if (iInfo == PMIndex.PM_SOURCE_ONLY_GOV) {
                    pmIndexInfo = pmIndex.getGovSource();
                } else {
                    pmIndexInfo = pmIndex.getUSSource();
                }
            }
        }
        
        return pmIndexInfo;
    }

    /**
     * @brief 【获取文字数据】
     * @n<b>函数名称</b>     :getWidgetData
     * @param iKey
     * @return
     * @return    String   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2013-3-7下午02:23:40
     * @<b>修改时间</b>      :
    */
    private String getWidgetData(int iKey) {
    	
    	try {
    		if (iKey >= 0 && iKey < 24) {
    			if (bHasCity) {
    			    RealTimeWeatherInfo rtInfo = m_cityWeather.getRealTimeWeather();

    				switch (iKey) {
    				case 0: return m_cityWeather.getCityName();
    			    case 1: return rtInfo.getNowWeather();	// 实时天气
    			    case 2:	{String st = rtInfo.getTempValue(); return ("N/A".equals(st)? "": st);}	// 实时温度
    			    
    			    // 晚上温度 3
    			    case 3: return getNightTemp(1);
    			    case 4: return getNightTemp(2);
    			    case 5: return getNightTemp(3);
    			    case 6: return getNightTemp(4);
    			    case 7: return getNightTemp(5);

    			    // 白天温度 8
    			    case 8: return getDayTemp(1);
    			    case 9: return getDayTemp(2);
    			    case 10: return getDayTemp(3);
    			    case 11: return getDayTemp(4);
    			    case 12: return getDayTemp(5);

    			    // 实时数据 13
    			    case 13:  {								// 今日温度
    			    	String sTemp = mDays.get(1).tempOrg;
    		            if (!TextUtils.isEmpty(sTemp) && sTemp.indexOf("暂无") != -1) {
    		            	sTemp = "暂无";
    					}
    		            
    			    	return sTemp;
    			    }
    			    case 14: return rtInfo.getWind();		// 实时风级
    			    case 15: return rtInfo.getUv();			// 紫外线
    			    case 16: return rtInfo.getHumidityValue();// 湿度
    			    
    			    // PM 
    			    case 17: return getPMInfo().getSourceName();
    			    case 18: return getPMInfo().getAirGrd();
    			    case 19: return getPMInfo().getHint();
    			    case 20: return getPMInfo().getPM25();
    			    
    				case 21: return m_cityWeather.getPublishTime();// 发布日期
    				case 22: {
    				    Date date = ComfunHelp.getTimeDate(m_cityWeather.getNowWeatherTime());
    				    return String.format("%d:%d", date.getHours(), date.getMinutes());
    				}
     			    case 23: return m_cityWeather.getPublishDate();// 发布时间
   			    
    			    default:
    					break;
    				}
    			} else if (iKey == 0) {
    				 return "请添加城市";
				}
    			
			} else {
				switch (iKey) {
				case 24: return getYiJiChong().getStrYi();
				case 25: return getYiJiChong().getStrJi();		// 忌信息
				case 26: return getYiJiChong().getStrChong();	// 冲信息
				
				case 27: return CalendarInfo.DayOfWeek(mDateInfo);	// 星期
				case 28: return getLunarInfo()? lunarInfo[1]: null;	// 农历
				case 29: return String.format("%d", mDateInfo.year);	// 年份
				case 30: return getLunarInfo()? lunarInfo[0]: null;		// 农历年份

				case 31: return String.format("%d/%d", mDateInfo.month, mDateInfo.day);// (09/27)
				case 32: return String.format("%d-%d", mDateInfo.month, mDateInfo.day);// 09-27
				case 33: return String.format("%d月%d日", mDateInfo.month, mDateInfo.day);// 09月27日

				// 34 星期
				case 34: return mDays.get(1).week;
				case 35: return mDays.get(2).week;
				case 36: return mDays.get(3).week;
				case 37: return mDays.get(4).week;
				case 38: return mDays.get(5).week;

				
				// 39 天气描述
				case 39: return mDays.get(1).info;
				case 40: return mDays.get(2).info;
				case 41: return mDays.get(3).info;
				case 42: return mDays.get(4).info;
				case 43: return mDays.get(5).info;
				
				// 44
				case 44: return mDays.get(1).date2;
				case 45: return mDays.get(2).date2;
				case 46: return mDays.get(3).date2;
				case 47: return mDays.get(4).date2;
				case 48: return mDays.get(5).date2;
				    
				// 49
				case 49: return mDays.get(1).date;
				case 50: return mDays.get(2).date;
				case 51: return mDays.get(3).date;
				case 52: return mDays.get(4).date;
				case 53: return mDays.get(5).date;
				
				case 54: return CalendarInfo.GetAfterJieQi(mDateInfo);	// 节气
				case 55: return m_cityWeather.getWarningInfo().getWeather(); // 天气预警

				default:
					break;
				}
			}
    		
		} catch (Exception e) {
		}
    	
    	return null;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    /**
     * @brief 【绘制输入流到画布】
     * @n<b>函数名称</b> :drawInputStream
     * @param inStream
     * @param itemInfo
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>修改</b> :
     * @<b>创建时间</b> : 2012-5-2上午11:23:05
     * @<b>修改时间</b> :
     */
    private void drawInputStream(InputStream inStream, DrawImageInfo itemInfo, Canvas canvas) {
    	Bitmap bmpFile = null;
    	
        try {
            bmpFile = BitmapFactory.decodeStream(inStream);
            
			Drawable d = new BitmapDrawable(bmpFile);
			d.setBounds(itemInfo.rect);
			d.draw(canvas);
			
//			canvas.drawRect(itemInfo.rectHot, rcPaint);
			
			if (d != null) {
				d.setCallback(null);
				d = null;
			}
        } catch (Exception e) {
        } finally {
	         if (bmpFile != null) {
				bmpFile.recycle();
				//Log.e("bitmap", bmpFile.toString() + "[recycle] -");
				bmpFile = null;
			}
        }
    }

    // 绘制文件到画布
    private boolean drawPicFile(String strFile, DrawImageInfo itemInfo) {
        InputStream inStream = null;
        try {
            if (mWidgetSkinInfo.bUserSkin) {
                inStream = new FileInputStream(new File(strFile));
            } else {
                inStream = mAstMgr.open(strFile);
            }
            
            drawInputStream(inStream, itemInfo, canvas);
            
            return true;
        } catch (Exception e) {
        } finally {
        	safeCloseStream(inStream);
        	inStream = null;
        }

        return false;
    }

    /**
     * @brief 【绘制数字】
     * @n<b>函数名称</b> :drawNumber
     * @param nNum
     * @param itemInfo
     * @param canvas
     * @param inputTest
     * @param msSkinPath
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-4-24下午04:23:50
     */
    private void drawNumber(int nNum, DrawImageInfo itemInfo) {
        InputStream is = null;
    	String sNum = "n" + nNum;

        try {
            if (mWidgetSkinInfo.skinConfig.isUseOrgNumberIcon()) {
            	if (mWidgetSkinInfo.skinConfig.mNumberIconVer == 1) {
            		is = mRes.openRawResource(m_NumberIds[nNum]);
				} else if (mWlt.canLink()) {
            		is = mWlt.getCalendarRes(sNum);
				}
            } else {
            	String sNumPath = mWidgetSkinInfo.sLoadedSkinPath + sNum + ".png";
                if (mWidgetSkinInfo.bUserSkin) {
                    is = new FileInputStream(sNumPath);
                } else {
                    is = mAstMgr.open(sNumPath);
                }
            }
        } catch (IOException e) {
        	if (mWlt.canLink() && mWidgetSkinInfo.skinConfig.mNumberIconVer == 0) {
        		is = mWlt.getCalendarRes(sNum);
			}
        }
        
        if (is == null) {
        	is = mRes.openRawResource(m_NumberIds[nNum]);
		}
        
        try {
			drawInputStream(is, itemInfo, canvas);
		} catch (Exception e1) {
		} finally {
			safeCloseStream (is);
			is = null;
        }
    }

    /**
     * @brief 【绘制天气图标】
     * @n<b>函数名称</b> :drawWeatherIco
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-4-24下午04:35:27
     */
    private void drawWeatherIco(String sWeather, boolean bCurDay, // bCurDay 当天的天气图标。与多天的不同
            DrawImageInfo itemInfo) {

        InputStream is = null;
        boolean bNight = (bCurDay && m_cityWeather.isNight()); // 当天的天气图标才有早晚之分
         
    	if (bUpdating && TextUtils.isEmpty(sWeather) && mbNet) {
    		is = mRes.openRawResource(R.drawable.wip_na_update);
		} else {
			// 新图标和主版可链接时，使用完整图标系列
			final boolean bAllIco = (mWidgetSkinInfo.skinConfig.mWeahterIconVer == 1 || mWlt.canLink());
	        try {
	            if (mWidgetSkinInfo.skinConfig.isUseOrgWeahterIcon()) {// OrgWeahter(内置)只用于当天的天气图标
	                String resId = WeatherModule.GetFinalWeathResId(sWeather, bNight, bAllIco);
	                is = WeatherModule.getWipInputStream(mContext, resId,
	                		mWidgetSkinInfo.skinConfig.mWeahterIconVer, mbNet);

	            } else {// 使用外置图标

	                String sIcoName = WeatherModule.GetFinalWeathResString(sWeather, bNight);
	                String sIcoPath = mWidgetSkinInfo.sLoadedSkinPath + sIcoName;
	                
	                if (mWidgetSkinInfo.bUserSkin) {
	                    is = new FileInputStream(new File(sIcoPath));
	                } else {
	                    is = mAstMgr.open(sIcoPath);
	                }
	            }
	        } catch (IOException e) {
	            // 无外置，则使用内置图标
	            String resId = WeatherModule.GetFinalWeathResId(sWeather, bNight, bAllIco);
	            is = WeatherModule.getWipInputStream(mContext, resId,
	            		mWidgetSkinInfo.skinConfig.mWeahterIconVer, mbNet);
	        }
		}

        try {
			drawInputStream(is, itemInfo, canvas);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			safeCloseStream (is);
	        is = null;
		}
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @brief 【绘制背景】
     * @n<b>函数名称</b> :drawBackround
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>修改</b> :
     * @<b>创建时间</b> : 2012-5-2上午11:21:02
     * @<b>修改时间</b> :
     */
    void drawBackround() {

        // 绘制图片
        InputStream is = null;

        try {
        	boolean bWidget_4x1 = (mWidgetType == WidgetGlobal.WIDGET_4X1);
        	String widget4x1bgName = "widget_4x1_bk";
        	if(ThemeSharePref.getInstance(mContext).isDefaultTheme()) {
        		widget4x1bgName = WidgetGlobal.getDefaultThemeWidget4x1bgName(mContext);
        	}
        	// 自定义背景
        	if (!mWidgetSkinInfo.skinConfig.isUseOrgBackgroud()) {
            	String sBkPath;
            	if (bWidget_4x1) {
            		sBkPath = mWidgetSkinInfo.sLoadedSkinPath + widget4x1bgName+".png";
    			} else {
    				sBkPath = mWidgetSkinInfo.sLoadedSkinPath + "widget_4x2_bk.png";
    			}
            	
            	try {
                    if (mWidgetSkinInfo.bUserSkin) {
                    	if(!new File(sBkPath).exists()) {
                    		sBkPath = mWidgetSkinInfo.sLoadedSkinPath + (bWidget_4x1 ? "widget_4X1_bk.png": "widget_4X2_bk.png");
                    	}
                   		is = new FileInputStream(new File(sBkPath));
                    } else {
                    	is = mAstMgr.open(sBkPath);
                    }
					if(bWidget_4x1) {// 优先使用手动设置的背景颜色 caizp 2017-03-14
						String bgColor4x1 = WidgetGlobal.getDefaultThemeWidget4x1bgColor(mContext);
						if(!TextUtils.isEmpty(bgColor4x1)) {
							ThemeModuleItem curThemeModuleItem = ThemeModuleHelper.getInstance().getCurrentThemeModuleByKey("weather");
							if(null != curThemeModuleItem && "0".equals(curThemeModuleItem.getId())) {
								int color = Color.parseColor(bgColor4x1);
								Bitmap bitmap = WidgetGlobal.getRoundedCornerBitmap(mContext,
										WidgetGlobal.getColorBitmap(color, mWidgetSkinInfo.skinConfig.mDrawPictureBkInfo.rect.width(),
												mWidgetSkinInfo.skinConfig.mDrawPictureBkInfo.rect.height()), color);
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
								is = new ByteArrayInputStream(baos.toByteArray());
							}
						}
					}
     			} catch (Exception e) {
					e.printStackTrace();
    			}
			}

        	if (is == null) {
        		is = mWlt.getCalendarRes((bWidget_4x1? widget4x1bgName: "widget_4x2_bk"));
			}
        	
        	if (is != null) {
                drawInputStream(is, mWidgetSkinInfo.skinConfig.mDrawPictureBkInfo, canvas);
			}
        	
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	safeCloseStream (is);
        	is = null;
        }
    }

    /**
     * @brief 【绘制图片元素】
     * @n<b>函数名称</b> :DrawImage
     * @return void
     * @<b>修改</b> : 陈希
     * @<b>创建时间</b> : 2012-4-27下午02:58:55
     */
    private void DrawImage() {
        DateInfo dateInfo = CalendarInfo.GetSysDateInfo();

        // 处理 小时制
        int iHour = dateInfo.hour;
        if (!/*Is24Hour(mContext)*/DateFormat.is24HourFormat(mContext)) {
            if (iHour > 12) {
                iHour -= 12;
            } else if (iHour == 0) {
                iHour = 12;
            }
        }

        DayWeatherInfo dayWeatherInfo = m_cityWeather.getWeatherInfo();
        ArrayList<DayInfo> dayInfos = dayWeatherInfo.getDays();
        List<DrawImageInfo> PicItemList = mWidgetSkinInfo.skinConfig.getDrawImageInfoList();
        DayInfo dayInfo;
        
        // 绘制图片元素
        for (int i = 0; i < PicItemList.size(); i++) {
            DrawImageInfo itemInfo = PicItemList.get(i);
            // //////////////////////////////////////////////////////////////////
            // 时钟
            //
            if (itemInfo.Type.equals(WidgetRuleId.ND_TIME_HOUR_POSITION_1_ID)) {
                int nTens = iHour / 10;
                drawNumber(nTens, itemInfo);
            }

            else if (itemInfo.Type.equals(WidgetRuleId.ND_TIME_HOUR_POSITION_2_ID)) {
                int nUits = iHour % 10;
                drawNumber(nUits, itemInfo);
            }

            else if (itemInfo.Type.equals(WidgetRuleId.ND_TIME_MINUTE_POSITION_1_ID)) {
                int nTens = dateInfo.minute / 10;
                drawNumber(nTens, itemInfo);
            }

            else if (itemInfo.Type.equals(WidgetRuleId.ND_TIME_MINUTE_POSITION_2_ID)) {
                int nUits = dateInfo.minute % 10;
                drawNumber(nUits, itemInfo);
            }

            // 时钟，冒号
            else if (itemInfo.Type.equals(WidgetRuleId.ND_TIME_COLON_ID)) {
                String icoPath = mWidgetSkinInfo.sLoadedSkinPath + "colon.png";
                drawPicFile(icoPath, itemInfo);
            }
            
            // 预警图标
            else if (itemInfo.Type.equals(WidgetRuleId.ND_WARNING)) {
            	WarningInfo warningInfo = m_cityWeather.getWarningInfo();
            	if(null != warningInfo) {
	                String icoPath = mWidgetSkinInfo.sLoadedSkinPath + "warning.png";
	                drawPicFile(icoPath, itemInfo);
            	}
            }

            // AM, PM
            else if (itemInfo.Type.equals(WidgetRuleId.ND_TIME_AMPM_ID)) {
            	if (!/*Is24Hour(mContext)*/DateFormat.is24HourFormat(mContext)) {
                    int nAmPm = Calendar.getInstance().get(Calendar.AM_PM);
                    String strAmPmPth = "";

                    if (nAmPm == 0) {
                        strAmPmPth = mWidgetSkinInfo.sLoadedSkinPath + "am.png";
                    } else {
                        strAmPmPth = mWidgetSkinInfo.sLoadedSkinPath + "pm.png";
                    }

                    drawPicFile(strAmPmPth, itemInfo);
				}
            }
            // else if (itemInfo.Type.equals("CT_TIME_COLON_AUTO")) { }
            // else if (itemInfo.Type.equals("CT_BACKGROUND")) { }
            // else if (itemInfo.Type.equals("CT_BACKGROUND_MASK")) { }

            // //////////////////////////////////////////////////////////////////
            // 天气
            //
			else if (itemInfo.Type.equals(WidgetRuleId.ND_WEATHER_ICON_CURRENT_ID)) {

				RealTimeWeatherInfo realTimeWeatherInfo = m_cityWeather.getRealTimeWeather();
				String sWeather = realTimeWeatherInfo.getNowWeather();
				
				drawWeatherIco(sWeather, true, itemInfo);
			}

			else if (itemInfo.Type.equals(WidgetRuleId.ND_WEATHER_ICON_FORECAST_DAY_1_ID)) {
				dayInfo = dayInfos.get(1);
				drawWeatherIco(dayInfo.info, false, itemInfo);
			}

			else if (itemInfo.Type.equals(WidgetRuleId.ND_WEATHER_ICON_FORECAST_DAY_2_ID)) {
				dayInfo = dayInfos.get(2);
				drawWeatherIco(dayInfo.info, false, itemInfo);
			}

			else if (itemInfo.Type.equals(WidgetRuleId.ND_WEATHER_ICON_FORECAST_DAY_3_ID)) {
				dayInfo = dayInfos.get(3);
				drawWeatherIco(dayInfo.info, false, itemInfo);
			}

			else if (itemInfo.Type.equals(WidgetRuleId.ND_WEATHER_ICON_FORECAST_DAY_4_ID)) {
				dayInfo = dayInfos.get(4);
				drawWeatherIco(dayInfo.info, false, itemInfo);
			}

			else if (itemInfo.Type.equals(WidgetRuleId.ND_WEATHER_ICON_FORECAST_DAY_5_ID)) {
				dayInfo = dayInfos.get(5);
				drawWeatherIco(dayInfo.info, false, itemInfo);
			}
            //
            // else if
            // (itemInfo.Type.equals("CT_WEATHER_ICON_FORECAST_00_12")) { }
            // else if
            // (itemInfo.Type.equals("CT_WEATHER_ICON_FORECAST_12_24")) { }
            // else if
            // (itemInfo.Type.equals("CT_WEATHER_ICON_FORECAST_NIGHT_1")) {
            // }
            // else if
            // (itemInfo.Type.equals("CT_WEATHER_ICON_FORECAST_NIGHT_2")) {
            // }
            // else if
            // (itemInfo.Type.equals("CT_WEATHER_ICON_FORECAST_NIGHT_3")) {
            // }
            // else if
            // (itemInfo.Type.equals("CT_WEATHER_ICON_FORECAST_NIGHT_4")) {
            // }
            // else if
            // (itemInfo.Type.equals("CT_WEATHER_ICON_FORECAST_NIGHT_5")) {
            // }

            else if (itemInfo.Type.equals(WidgetRuleId.ND_CUSTOM_ID)) {
                String icoPath = mWidgetSkinInfo.sLoadedSkinPath + itemInfo.FileName;
                drawPicFile(icoPath, itemInfo);
            }
        }
    }

    /**
     * @brief 【绘制文本】
     * @n<b>函数名称</b> :DrawText
     * @return void
     * @<b>作者</b> :
     * @<b>修改</b> : 陈希
     * @<b>创建时间</b> : 2012-5-2上午10:59:26
     * @<b>修改时间</b> :
     */
	private void DrawText() {
		WidgetSkinConfig skinSetting = mWidgetSkinInfo.skinConfig;
		List<DrawTextInfo> listTextInfo = skinSetting.getDrawTextInfoList();
		TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.density = mRes.getDisplayMetrics().density;
		textPaint.setDither(true);
		//textPaint.setFilterBitmap(true);
		//同步91桌面字体 caizp 2014-10-14
		synchronized(isFontChanged) {
			if(null == typeface || isFontChanged.booleanValue()) {
				try {
					isFontChanged = Boolean.valueOf(false);
					SharedPreferences set = WidgetUtils.getSetting(mContext, WidgetGlobal.WIDGET_SKIN_SETTING);
					String fontPath = set.getString(WidgetGlobal.WIDGET_SETTING_PANDA_FONT, "");
					if(TextUtils.isEmpty(fontPath)) {
						typeface = null;
					} else {
						typeface = Typeface.createFromFile(fontPath);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(null != typeface) {
			textPaint.setTypeface(typeface);
		}
		final int length = listTextInfo.size();
		for (int i = 0; i < length; i++) {
			DrawTextInfo textInfo = listTextInfo.get(i);

			if (textInfo.Type.equals(WidgetRuleId.ND_CUSTOM_ID)) {
				String sKey = textInfo.TextKey;
				if (TextUtils.isEmpty(sKey)) {
					continue;
				}
				
				// 格式化文字
				for (int j = 0; j < TEXT_KEYS.length; j++) {					
					if (sKey.contains(TEXT_KEYS[j])) {
						String sValue = getWidgetData(j);
						
						// PM为空时，该元素被隐藏
						if (((j >= PM_INDEX_S && j<= PM_INDEX_E) ||
							(j >= TEMP_INDEX_S && j<= TEMP_INDEX_E) || 
							j == NOW_TEMP_INDEX)
							&& TextUtils.isEmpty(sValue)) {
							sKey = "";
							break;
						}
						
						if (sValue == null) {
							sValue = "";
						}
						
						sKey = sKey.replace(TEXT_KEYS[j], sValue);
					}
				}
				
				if (TextUtils.isEmpty(sKey)) {
					continue;
				}
				
				try {
					if (textInfo.nLenth > 0 && textInfo.nLenth < sKey.length()) {
						sKey = sKey.substring(0, textInfo.nLenth);
					}
				} catch (Exception e) {
				}

				// 字体颜色
				textPaint.setColor(textInfo.Color | 0xff000000);

				// 字体大小
				textPaint.setTextSize(textInfo.Size);

				// 字体阴影
				if (textInfo.UseShadow) {
					int shadowColor = textInfo.ShadowColor;
					if (textInfo.ShadowOffsetX == 0) {
						textInfo.ShadowOffsetX = 1;
					}

					if (textInfo.ShadowOffsetY == 0) {
						textInfo.ShadowOffsetY = 1;
					}

					textPaint.setShadowLayer(textInfo.ShadowBlurSize,
							textInfo.ShadowOffsetX,
							textInfo.ShadowOffsetY, (shadowColor | 0xff000000));
				} else {
					textPaint.clearShadowLayer();
				}
				
				Rect rect = new Rect();
				textPaint.getTextBounds(sKey, 0, sKey.length(), rect);
				int iWidth = rect.width();
				int iHeight = rect.height();
				
				rect.set(textInfo.X, textInfo.Y - iHeight, textInfo.X + iWidth, textInfo.Y);
				
				// 字体对齐
				if (textInfo.Align.equalsIgnoreCase("right")) {
					rect.offset(-iWidth, 0);

					textPaint.setTextAlign(Paint.Align.RIGHT);
				} else if (textInfo.Align.equalsIgnoreCase("left")) {
					textPaint.setTextAlign(Paint.Align.LEFT);
				} else {
					
					rect.offset(-iWidth /2, 0);
					textPaint.setTextAlign(Paint.Align.CENTER);
				}
				
				int extH = 0;
				int extW = 0;
				int maxW = WidgetRuleId.SKIN_HOT_AREA_MAX_SIZE;
				if (textInfo.TextKey.contains(WidgetRuleId.CITY_NAME_QUOTE)) {
					maxW = 80;
				}
				
				if (iHeight < WidgetRuleId.SKIN_HOT_AREA_MAX_SIZE) {
					extH = (WidgetRuleId.SKIN_HOT_AREA_MAX_SIZE - iHeight) / 2;
				}

				if (iWidth < maxW) {
					extW = (maxW - iWidth) / 2;
				}
				
				if (extH != 0 || extW != 0) {
					rect.set(rect.left - extW - 20, rect.top - extH, rect.right + extW + 20, rect.bottom + extH);
				}
				
				textInfo.rect = rect;
				textInfo.rectHot = rect;
				
				canvas.drawText(sKey, textInfo.X, textInfo.Y, textPaint);
				//canvas.drawRect(rect, rcPaint);
			}
		}
	}

    /**
     * @brief 【是否是24小时制】
     * @n<b>函数名称</b>     :Is24Hour
     * @param context
     * @return
     * @return    boolean   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2013-1-28上午10:40:54
     * @<b>修改时间</b>      :
    */
//    private static boolean Is24Hour(Context context) {
//        ContentResolver cv = context.getContentResolver();
//        String strTimeFormat = Settings.System.getString(cv, Settings.System.TIME_12_24);
//
//        if (strTimeFormat != null && strTimeFormat.equals("24")) { // 某些rom12小时制时会返回null
//            return true;
//        } else {
//            return false;
//        }
//    }
}
