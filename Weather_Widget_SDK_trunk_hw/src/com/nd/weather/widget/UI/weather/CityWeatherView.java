package com.nd.weather.widget.UI.weather;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.calendar.CommData.CityWeatherInfo;
import com.calendar.CommData.DayWeatherInfo.DayInfo;
import com.calendar.CommData.PMIndex;
import com.calendar.CommData.PMIndex.PMIndexInfo;
import com.calendar.CommData.RealTimeWeatherInfo;
import com.calendar.CommData.WarningInfo;
import com.nd.calendar.Control.GetWeatherControler;
import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.communication.http.HttpToolKit;
import com.nd.calendar.module.WarningMoudle;
import com.nd.calendar.module.WeatherModule;
import com.nd.calendar.util.ComfunHelp;
import com.nd.hilauncherdev.analysis.ActualTimeAnalysis;
import com.nd.hilauncherdev.analysis.cvanalysis.CvAnalysis;
import com.nd.hilauncherdev.analysis.cvanalysis.CvAnalysisConstant;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.DateUtil;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.weather.widget.R;
import com.nd.weather.widget.WidgetUtils;
import com.nd.weather.widget.UI.CommonUI;
import com.nd.weather.widget.UI.UICalendarGuideAty;
import com.nd.weather.widget.UI.weather.ad.AdImageView;
import com.nd.weather.widget.UI.weather.ad.AdImageView.OnShowCallBack;
import com.nd.weather.widget.UI.weather.ad.AdImageView.OnclickCallBack;
import com.nd.weather.widget.UI.weather.twentyfour.HoursWeatherEntity;
import com.nd.weather.widget.UI.weather.twentyfour.HoursWeatherEntity.TemperatureRange;
import com.nd.weather.widget.UI.weather.twentyfour.MyHorizontalScrollView;
import com.nd.weather.widget.UI.weather.twentyfour.TwentyFourthWeatherCurve;

public class CityWeatherView implements View.OnClickListener {
	// final static String TAG = "CityWeatherView";

	private Context mContext;
	private Resources mRes;

	private ConfigHelper mCfgHelper;
	private ViewGroup parentPager;
	
	private ImageView[] imDayIco;
	private TextView[] tvWeekDate;
	private TextView[] tvWeather;
	private TextView[] tvDayTemp;

	private Drawable windPowerImg;

	// /////////////////////////////////////////////////////////////////////////
	// private TextView tvWarningInfo;
	// private TextView tvCity;
	private RelativeLayout cityWeatherNow;
	private TextView tvNowTemp;
	private TextView tvNowTempIcon;
	// private TextView tvCityCount;
	private TextView tvNowWeath;
	private TextView tvNowWeathTemp;
//	private ImageView ivNowWeathImage;
	// private TextView tvRefTime;
	private TextView tvWindDirection;
	private TextView tvHumidily;
	private TextView tvAirQuality;
	// 今天天气 以及生活指数
	// private TextView tvWeatherIndex;

	// private ImageView imageNow;

	private View mView;

	// /////////////////////////////////////////////////////////////////////////

//	private int m_iCityNameSize = 0;

	private Object mLockData = new Object();
	private DataPackage mDataPackage = null;

	private int mCurrPos = 0;
	private int mTotalCount = 0;

	private CityWeatherInfo mCityWeatherInfo = null;

//	private boolean bProcessWarning = false;

	private StringBuilder mTempBuffer = new StringBuilder();

	public final int MSG_WARNING_ICO_FINISH = 4;

	private TextView tvNowWindpower;
	private int windpowerIconSize;
	
	// 6小时天气曲线图
	private LinearLayout twentyFourthWeather;
	private RelativeLayout mTemp;
	private TwentyFourthWeatherCurve mTempCurve;
	private MyHorizontalScrollView llCurve;
	
	private AdImageView adsImageView;
	
	private WarningInfo warningInfo;
	
	private PopupWindow warningDetailPopupWindow;
	private TextView warningPopTitle;
	private TextView warningPopContent;
	private TextView warningPopPubTime;

	// /////////////////////////////////////////////////////////////////////////

	// 通知处理，对于每一个城市独立处理，但共享一个UI
	//
	private class DataPackage extends Handler {
		public boolean bAvailable = true;

		public DataPackage() {
		}

		@Override
		public void handleMessage(Message msg) {

			// 判断当前视图是否与 handler的数据匹配
			if (!bAvailable) {
				return;
			}

			synchronized (mLockData) {
				switch (msg.what) {
				case MSG_WARNING_ICO_FINISH:
					Drawable warnIco = null;
					if (msg.arg1 == WarningMoudle.MSG_WARNING_FINISH_SUCCESS) {
						if (msg.obj != null) {
							String sIcoPath = msg.obj.toString();
							try {
								warnIco = Drawable.createFromPath(sIcoPath);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (warnIco == null) {
								// 无效图片要删除
								File f = new File(sIcoPath);
								if (f.exists()) {
									f.delete();
								}
							}
						}
					}

					if (warnIco == null) {
						warnIco = mRes.getDrawable(R.drawable.warn_any);
					}
					break;
				}
			}

		}
	}

	public CityWeatherView(Context context) {
		mContext = context;
		mRes = context.getResources();

		// 初始化视图控件
		initView();
	}

	public View getView() {
		return mView;
	}

	// 设置数据
	public void setData(CityWeatherInfo info, int nPos, int Count) {
		// 锁定数据
		synchronized (mLockData) {
			mCurrPos = nPos;
			setData(info);
			mTotalCount = Count;

			// 重置数据
//			bProcessWarning = false;

			// 已有的 数据包 与当前已不配对，将其关闭
			if (mDataPackage != null) {
				mDataPackage.bAvailable = false;
				mDataPackage = null;
			}

			// 为当前的城市创建一个独立的 数据包
			mDataPackage = new DataPackage();
			refreshView();
		}
	}

	/**
	 * @Title: getCityId
	 * @Description: TODO(获取城市代码)
	 * @author yanyy
	 * @date 2012-4-16 下午01:36:19
	 * @return
	 * @return int
	 * @throws
	 */
	public int getCityId() {
		return mCityWeatherInfo.getId();
	}

	/**
	 * @brief 【初始化页面】
	 * @n<b>函数名称</b> :initView
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-3-31上午11:12:43
	 */
	private void initView() {
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = mInflater.inflate(R.layout.weather_city_weather_view, null,
				false);
		
		cityWeatherNow = (RelativeLayout) mView.findViewById(R.id.city_weather_now);
		cityWeatherNow.getLayoutParams().height = ScreenUtil.getCurrentScreenHeight(mContext) - ScreenUtil.dip2px(mContext, 26+122);
		tvNowTemp = (TextView) mView.findViewById(R.id.tempTextId);
		tvNowTempIcon = (TextView) mView.findViewById(R.id.tempIconId);
		tvNowWeath = (TextView) mView.findViewById(R.id.nowWeathTextId);
		tvNowWeathTemp = (TextView) mView.findViewById(R.id.nowWeathTempTextId);
//		ivNowWeathImage = (ImageView) mView.findViewById(R.id.nowWeathImageId);

		tvWindDirection = (TextView) mView
				.findViewById(R.id.weather_wind_scale);
		tvHumidily = (TextView) mView.findViewById(R.id.weather_humidity);
		tvAirQuality = (TextView) mView.findViewById(R.id.weather_air_quality);
		// 为温度字符设置字体样式
		Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
				"font/temp_typeface.ttf");
		tvNowTemp.setTypeface(tf);
		tvNowTempIcon.setTypeface(tf);

		tvNowWindpower = (TextView) mView
				.findViewById(R.id.nowWeatherWindPower);

		imDayIco = new ImageView[3];
		tvWeekDate = new TextView[3];
		tvWeather = new TextView[3];
		tvDayTemp = new TextView[3];

		imDayIco[0] = ((ImageView) mView.findViewById(R.id.imgOne));
		tvWeekDate[0] = (TextView) mView.findViewById(R.id.weatherWeek1);
		tvWeather[0] = (TextView) mView.findViewById(R.id.weatherOneId);
		tvDayTemp[0] = (TextView) mView.findViewById(R.id.weatherOneTemp);

		imDayIco[1] = ((ImageView) mView.findViewById(R.id.imgTwo));
		tvWeekDate[1] = (TextView) mView.findViewById(R.id.weatherWeek2);
		tvWeather[1] = (TextView) mView.findViewById(R.id.weatherTwoId);
		tvDayTemp[1] = (TextView) mView.findViewById(R.id.weatherTwoTemp);

		imDayIco[2] = ((ImageView) mView.findViewById(R.id.imgThree));
		tvWeekDate[2] = (TextView) mView.findViewById(R.id.weatherWeek3);
		tvWeather[2] = (TextView) mView.findViewById(R.id.weatherThreeId);
		tvDayTemp[2] = (TextView) mView.findViewById(R.id.weatherThreeTemp);

		mView.findViewById(R.id.rl_temp).setOnClickListener(this);
		tvNowWindpower.setOnClickListener(this);
		tvNowWeath.setOnClickListener(this);
		tvNowWeathTemp.setOnClickListener(this);
		tvNowTemp.setOnClickListener(this);
		tvHumidily.setOnClickListener(this);
		tvWindDirection.setOnClickListener(this);
		tvAirQuality.setOnClickListener(this);
		mView.findViewById(R.id.day_weather_info_titile).setOnClickListener(this);
		// 点击未来3天天气图标不跳转 caizp 2016-05-27
//		mView.findViewById(R.id.weatherOne_layout).setOnClickListener(this);
//		mView.findViewById(R.id.weatherTwo_layout).setOnClickListener(this);
//		mView.findViewById(R.id.weatherThree_layout).setOnClickListener(this);

		Drawable drawable;
		windpowerIconSize = ComfunHelp.dip2px(mContext, 18);
		int iPadding = ComfunHelp.dip2px(mContext, 3);

		int cSize = ComfunHelp.dip2px(mContext, 20);
		int cPadding = ComfunHelp.dip2px(mContext, 10);
		drawable = mRes.getDrawable(R.drawable.weather_wind_direction);
		drawable.setBounds(0, 0, cSize, cSize);
		tvWindDirection.setCompoundDrawables(drawable, null, null, null);
		tvWindDirection.setCompoundDrawablePadding(cPadding);

		drawable = mRes.getDrawable(R.drawable.weather_humidity);
		drawable.setBounds(0, 0, cSize, cSize);
		tvHumidily.setCompoundDrawables(drawable, null, null, null);
		tvHumidily.setCompoundDrawablePadding(cPadding);

		drawable = mRes.getDrawable(R.drawable.weather_air);
		drawable.setBounds(0, 0, cSize, cSize);
		tvAirQuality.setCompoundDrawables(drawable, null, null, null);
		tvAirQuality.setCompoundDrawablePadding(cPadding);
		tvNowWindpower.setCompoundDrawablePadding(iPadding);

		mCfgHelper = ConfigHelper.getInstance(mContext);
		
		// 24小时天气
		twentyFourthWeather = (LinearLayout) mView.findViewById(R.id.twentyFourthWeather);
		twentyFourthWeather.setOnClickListener(this);
		mTemp = (RelativeLayout) mView.findViewById(R.id.temp);
		llCurve= (MyHorizontalScrollView) mView.findViewById(R.id.llCurve);
		llCurve.setHorizontalScrollBarEnabled(false);
		llCurve.setParentPager(parentPager);
		llCurve.setOnClickListener(this);
		twentyFourthWeather.setVisibility(View.GONE);
		
		adsImageView = (AdImageView) mView.findViewById(R.id.ad_icon);
		adsImageView.setAutoAdjustHeight(false);
		adsImageView.setVisibility(View.GONE);
//		adsImageView.loadPosAd(AdImageView.TYPE_WEATHER_DETAIL_ICON);
		adsImageView.setOnclickCallBack(new OnclickCallBack() {
			@Override
			public void callback() {
				int	functionId = ActualTimeAnalysis.TENCENT_VALID_HITS;
				HiAnalytics.submitEvent(mContext, AnalyticsConstant.LAUNCHER_THEME_MENU, "gg");
				ActualTimeAnalysis.sendActualTimeAnalysis(mContext, functionId, "ztc");
				CvAnalysis.submitClickEvent(mContext, WidgetUtils.getAnalyticsId(mContext, R.string.weather_detail_ad_cv_page_id), WidgetUtils.getAnalyticsId(mContext, R.string.weather_detail_ad_cv_pos_id), adsImageView.resId, CvAnalysisConstant.RESTYPE_ADS);
			}
		});
		adsImageView.setOnShowCallBack(new OnShowCallBack() {
			@Override
			public void callback() {
				CvAnalysis.submitShowEvent(mContext, WidgetUtils.getAnalyticsId(mContext, R.string.weather_detail_ad_cv_page_id), WidgetUtils.getAnalyticsId(mContext, R.string.weather_detail_ad_cv_pos_id), adsImageView.resId, CvAnalysisConstant.RESTYPE_ADS);
			}
		});
	}
	
	/**
	 * <br>Description: 设置24小时天气数据
	 * <br>Author:caizp
	 * <br>Date:2016年5月5日下午2:20:05
	 * @param info
	 * @param cityCode
	 */
	public void setTwentyFourthData(String cityCode){
		String json = mCfgHelper.loadKey(cityCode);
		if(TextUtils.isEmpty(json)) {
			twentyFourthWeather.setVisibility(View.GONE);
			return;
		}
		JSONObject obj;
		HoursWeatherEntity entity = null;
		try {
			obj = new JSONObject(json);
            int type = obj.optInt("type");
            if (type == 300) {
            	entity = new HoursWeatherEntity();
            	entity.gmt = obj.optInt("gmt");
            	entity.title = obj.optString("title");
            	JSONObject tempObj = obj.optJSONObject("temp");
            	entity.temp = new TemperatureRange();
            	entity.temp.height = tempObj.optInt("height");
            	entity.temp.low = tempObj.optInt("low");
            	entity.items = new ArrayList<HoursWeatherEntity.HourWecther>();
            	JSONArray hourArray = new JSONArray(obj.optString("items"));
            	Time time = new Time();
        		time.setToNow();
				String nowTime = DateUtil.getStringDateShort() + " " + (time.hour < 10 ? "0"+Integer.toString(time.hour) : Integer.toString(time.hour)) + ":00:00";
				boolean beginShow = false;
				for (int k = 0; k < hourArray.length(); k++) {
					JSONObject hourObj = hourArray.optJSONObject(k);
					if(null == hourObj) continue;
					HoursWeatherEntity.HourWecther hourwecther = new HoursWeatherEntity.HourWecther();
					hourwecther.time = hourObj.optString("time");
					if(nowTime.equals(hourwecther.time)) {//取当前时间之后的数据
						beginShow = true;
					}
					if(!beginShow) continue;
					hourwecther.climate = hourObj.optInt("climate");
					hourwecther.dayType = hourObj.optInt("dayType");
					hourwecther.temp = hourObj.optInt("temp");
					hourwecther.time = hourObj.optString("time");
					hourwecther.name = hourObj.optString("name");
					hourwecther.type = hourObj.optInt("type");
					entity.items.add(hourwecther);
					if(entity.items.size() >= TwentyFourthWeatherCurve.SHOW_MAX_DAYS + 1) break;
				}
				if(entity.items.isEmpty()) {
					for (int k = 0; k < hourArray.length(); k++) {
						JSONObject hourObj = hourArray.optJSONObject(k);
						if(null == hourObj) continue;
						HoursWeatherEntity.HourWecther hourwecther = new HoursWeatherEntity.HourWecther();
						hourwecther.time = hourObj.optString("time");
						hourwecther.climate = hourObj.optInt("climate");
						hourwecther.dayType = hourObj.optInt("dayType");
						hourwecther.temp = hourObj.optInt("temp");
						hourwecther.time = hourObj.optString("time");
						hourwecther.name = hourObj.optString("name");
						hourwecther.type = hourObj.optInt("type");
						entity.items.add(hourwecther);
						if(entity.items.size() >= TwentyFourthWeatherCurve.SHOW_MAX_DAYS + 1) break;
					}
				}
            }
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(null == entity) {
			twentyFourthWeather.setVisibility(View.VISIBLE);
			return;
		}
		twentyFourthWeather.setVisibility(View.VISIBLE);
		//对24小时的折线图进行传入
		int[] wh = ScreenUtil.getScreenWH();
		int interval = (wh[0] - ScreenUtil.dip2px(mContext, 32)) / TwentyFourthWeatherCurve.SHOW_MAX_DAYS;
		int totalWidth = interval * (entity.items.size() -1);
		mTempCurve = new TwentyFourthWeatherCurve(mContext, entity, totalWidth);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(totalWidth,(int) (0.3 * wh[1]));
		mTemp.removeAllViews();
		mTempCurve.setLayoutParams(params);
		mTemp.addView(mTempCurve);
		// 点击6小时天气线图不跳转 caizp 2016-05-27
//		mTempCurve.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				WidgetUtils.startGuide(mContext,
//						UICalendarGuideAty.CALENDAR_2015_GUIDE, null,
//						WidgetUtils.DOWNLOAD_CLICK_FROM_SIX_HOUR_WEATHER);
//				HiAnalytics.submitEvent(mContext, WidgetUtils.getAnalyticsId(mContext, R.string.analytics_weather_enter_recommend_huangli), WidgetUtils.ENTER_CLICK_FROM_SIX_HOUR);
//			}
//		});
	}

	/**
	 * @brief 【设置天气信息】
	 * @n<b>函数名称</b> :setWeatherInfo
	 * @<b>作者</b> :
	 * @<b>修改</b> : chenx
	 * @<b>创建时间</b> : 2013-8-5下午3:12:05
	 * @<b>修改时间</b> :
	 */
	private void setWeatherInfo() {
		ArrayList<DayInfo> days = mCityWeatherInfo.getWeatherInfo().getDays();

		if (!days.isEmpty() && days != null) {
			int len = tvWeekDate.length;
			// boolean bNet = HttpToolKit.isNetworkAvailable(mContext);

			for (int i = 0; i < len; i++) {
				try {
					// 从明天开始
					final DayInfo day = days.get(i + 2);

					imDayIco[i].setImageResource(WeatherModule
							.GetFinalWeath64ResId(day.info));
					tvWeather[i].setText(day.info);
					/*int todayId = WeatherModule.GetFinalWeath64ResId(days
							.get(1).info);
					ivNowWeathImage.setVisibility(View.VISIBLE);
					if (todayId == R.drawable.wip_na_64) {
						ivNowWeathImage.setVisibility(View.GONE);
					} else {
						ivNowWeathImage
								.setImageDrawable(mRes
										.getDrawable(WeatherModule
												.GetFinalWeath64ResId(days
														.get(1).info)));
					}*/

					// 天气信息
					tvWeekDate[i].setText(((i == 0) ? mRes
							.getString(R.string.tomorrow) : day.week) + "  " + day.date);

					tvDayTemp[i]
							.setText(TextUtils.isEmpty(day.temperature) ? "-- / --"
									: day.temperature);
				} catch (Exception e) {
					imDayIco[i].setImageDrawable(null);
					tvWeekDate[i].setText("");
				} finally {
					imDayIco[i].setVisibility(View.VISIBLE);
				}
			}
		}
	}

	/**
	 * @brief 【刷新状态】
	 * @n<b>函数名称</b> :refreshState
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-9下午03:33:35
	 */
	public void refreshState() {
		// 更新时间
		// if (mCityWeatherInfo.isUpdating()) {
		//
		// tvRefTime.setText("正在更新.....");
		// } else {
		// if (mCityWeatherInfo.isNetSuccess()) {
		// tvRefTime.setText(mCityWeatherInfo.getSaveTime());
		// } else {
		// tvRefTime.setText("更新失败");
		// }
		// }
	}

	/**
	 * @brief 【刷新天气】
	 * @n<b>函数名称</b> :refreshWeather
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2013-8-9下午2:56:08
	 * @<b>修改时间</b> :
	 */
	public void refreshWeather() {
		if (mCityWeatherInfo == null
				|| TextUtils.isEmpty(mCityWeatherInfo.getCityCode())) {
			return;
		}

		// if no network just return
		if (!checkNetWork()) {
			return;
		}

		if (mCityWeatherInfo.isUpdating()) {
			return;
		} else {
			Toast.makeText(mContext, "正在更新中...", Toast.LENGTH_SHORT).show();
		}
		mCityWeatherInfo.setUpdating(true);
		// tvRefTime.setText("正在更新.....");
		// vProcess.setVisibility(View.VISIBLE);

		// 强制更新
		mCityWeatherInfo.setIsForceUpdate(true);
		// 添加更新任务
		GetWeatherControler.getInstance(mContext).addTopTask(mCityWeatherInfo);
	}

	public void refreshWarning() {
		LoadWarning();
	}

	/**
	 * @brief 【刷新PM参数】
	 * @n<b>函数名称</b> :refreshPMParam
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2012-6-11下午02:03:27
	 * @<b>修改时间</b> :
	 */
	public void refreshPMParam() {
		// 清理缓存
		mCityWeatherInfo.setWeatherIndex(null);
		LoadWeatherAndPMParam();
	}

	public void refreshWeatherImgWithSun() {
		if (mCityWeatherInfo.dayNightChange()) {
			refreshNowWeatherImg();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////
	/**
	 * @brief 【刷新视图】
	 * @n<b>函数名称</b> :refreshView
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-1-18下午06:38:06
	 */
	private void refreshView() {
		try {
			// 常规数据失败捕获
			try {
				// 总数
				mTempBuffer.delete(0, mTempBuffer.length());
				mTempBuffer.append(mCurrPos + 1).append("/")
						.append(mTotalCount);
				refreshState();
			} catch (Exception e) {
				// tvRefTime.setText("更新异常[城市信息]");
				// vProcess.setVisibility(View.GONE);
			}

			// 预警
			LoadWarning();

			// 实时信息
			try {
				tvNowTempIcon.setVisibility(View.VISIBLE);
				// 实时温度tempTextId
				String tempText = mCityWeatherInfo.getTemperatureValue();
				if(tempText == null || tempText.equals("N/A")){
					tvNowTempIcon.setVisibility(View.GONE);
				}
				tvNowTemp.setText(tempText);

				// 当天天气和当天温度
				DayInfo day = mCityWeatherInfo.getWeatherInfo().getDays()
						.get(1);
				if (day != null) {
					tvNowWeath.setText(day.info + " ");
					tvNowWeathTemp.setText(day.temperature);
				}
			} catch (Exception e) {
				// tvRefTime.setText("更新异常[实时数据]");
				// vProcess.setVisibility(View.GONE);
			}

			// 天气图标
			try {
				refreshNowWeatherImg();
			} catch (Exception e) {
				// tvRefTime.setText("更新异常[天气图标]");
				// vProcess.setVisibility(View.GONE);
			}

			// 指数
			try {
				LoadWeatherAndPMParam();
			} catch (Exception e) {
				// tvRefTime.setText("更新异常[天气指数]");
				// vProcess.setVisibility(View.GONE);
			}
			// 24小时天气
			setTwentyFourthData(mCityWeatherInfo.getCityCode());
			
			// 天气预报
			setWeatherInfo();
		} catch (Exception e) {
			e.printStackTrace();
			// tvRefTime.setText("更新异常[未知错误]");
			// vProcess.setVisibility(View.GONE);
		}
		mCityWeatherInfo.setIsRefreshToView(true);
		// Log.v("refreshView", (System.currentTimeMillis() - aa) + "");
	}

	/**
	 * @brief 【加载预警信息】
	 * @n<b>函数名称</b> :LoadWarning
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-1-18下午06:38:12
	 */
	private void LoadWarning() {
		// 先不显示
		tvNowWindpower.setVisibility(View.INVISIBLE);

		warningInfo = mCityWeatherInfo.getWarningInfo();
		if (null == warningInfo) {
			return;
		}
		String w = warningInfo.getWeather();
		if (!TextUtils.isEmpty(w.trim())) {
			tvNowWindpower.setText(warningInfo.getWeather());
			int ic_id = mRes
					.getIdentifier(mContext.getPackageName() + ":drawable/ws_"
							+ warningInfo.getsWeatherNo(), null, null);
			if(ic_id <= 0) {
				ic_id = R.drawable.ws_15;
			}
			windPowerImg = mRes.getDrawable(ic_id);
			windPowerImg.setBounds(0, 0, windpowerIconSize, windpowerIconSize);
			tvNowWindpower.setCompoundDrawables(windPowerImg, null, null, null);
			tvNowWindpower.setVisibility(View.VISIBLE);
			refreshWarningPopupWindow();
		} else {
			tvNowWindpower.setVisibility(View.INVISIBLE);
			if(null != warningDetailPopupWindow && warningDetailPopupWindow.isShowing()) {
				warningDetailPopupWindow.dismiss();
			}
		}
	}
	
	private void refreshWarningPopupWindow() {
		if(null == warningDetailPopupWindow) {
			ViewGroup waringPopLayout = (ViewGroup)View.inflate(mContext, R.layout.weather_warning_detail_view , null);
			warningPopTitle = (TextView) waringPopLayout.findViewById(R.id.warning_title);
			warningPopContent = (TextView) waringPopLayout.findViewById(R.id.warning_content);
			warningPopPubTime = (TextView) waringPopLayout.findViewById(R.id.warning_pub_time);
			warningDetailPopupWindow = new PopupWindow(waringPopLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			warningDetailPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			warningDetailPopupWindow.setAnimationStyle(R.style.PopupWarningAni);
			warningDetailPopupWindow.setOutsideTouchable(true);
			warningDetailPopupWindow.setFocusable(true);
		}
		if(null != warningInfo) {
			warningPopTitle.setText(warningInfo.getWeather() + warningInfo.getColor() + mContext.getResources().getString(R.string.text_warning));
			warningPopContent.setText(warningInfo.getContent());
			warningPopPubTime.setText(warningInfo.getTime());
		}
	}

	/**
	 * @brief 【加载天气和PM参数】
	 * @n<b>函数名称</b> :LoadWeatherAndPMParam
	 * @param sHum
	 * @param sUv
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>修改</b> :
	 * @<b>创建时间</b> : 2012-6-8上午10:40:49
	 * @<b>修改时间</b> :
	 */
	private void LoadWeatherAndPMParam() {

		if (mCityWeatherInfo == null) {
			return;
		}
		String weatherIndex = mCityWeatherInfo.getWeatherIndex();
		if (!TextUtils.isEmpty(weatherIndex)) {
			// 有缓存
			// tvWeatherIndex.setText(weatherIndex);
			return;
		}
		RealTimeWeatherInfo weatherNowInfo = mCityWeatherInfo
				.getRealTimeWeather();

		// PM 指数
		PMIndex pmIndex = mCityWeatherInfo.getPmIndex();
		if (pmIndex != null) {
			PMIndexInfo pmIndexInfo = null;

			int iInfo = pmIndex.getSourceInfo();
			if (iInfo != PMIndex.PM_SOURCE_NULL) {

				// 选择适合的源
				if (iInfo == PMIndex.PM_SOURCE_ALL) {
					// 美使馆源优先
					String sUS = mCfgHelper
							.loadKey(ConfigsetData.CONFIG_NAME_KEY_PM_SOURCE);
					if (TextUtils.isEmpty(sUS)
							|| ConfigsetData.CONFIG_DEFAULT_PM_SOURCE
									.equalsIgnoreCase(sUS)) {
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

				// PM 指数格式化
				if (pmIndexInfo != null) {
					StringBuilder sb = new StringBuilder();
					sb.append(pmIndexInfo.getHint() + " "
							+ pmIndexInfo.getAirGrd());
					tvAirQuality.setText(sb);
				}
			}
		}

		// 紫外线
		String uv = weatherNowInfo.getUv();
		if (!TextUtils.isEmpty(uv)) {
			// sBuilder.append("紫外线 ").append(uv).append("\n");
		}

		// 风向
		String sWind = weatherNowInfo.getWind();
		if (!TextUtils.isEmpty(sWind)) {
			if (sWind.indexOf("风") == -1) {
				tvWindDirection.setText("风向" + sWind);
			} else {
				tvWindDirection.setText(sWind);
			}
		}

		// 湿度
		String hum = weatherNowInfo.getHumidity();
		if (!TextUtils.isEmpty(hum)) {
			if (hum.contains("%")) {
				tvHumidily.setText(hum);
			} else {
				tvHumidily.setText(hum + "%");
			}
		}
	}
	
	/**
	 * @brief 【刷新主天气图标】
	 * @n<b>函数名称</b> :refreshNowWeatherImg
	 * @param dateInfo
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-12上午10:54:44
	 */
	private void refreshNowWeatherImg() {
		// long a = System.currentTimeMillis();
		// 图片显示
//		final String info = mCityWeatherInfo.getRealTimeWeather()
//				.getNowWeather();
//		boolean bNet = HttpToolKit.isNetworkAvailable(mContext);

		// if (TextUtils.isEmpty(info)) {
		// imageNow.setImageDrawable(WeatherModule.getWipDrawable(mContext,
		// "wip_na", bNet));
		// } else {
		// boolean bNight = mCityWeatherInfo.isNight();
		// String resName = WeatherModule.GetFinalWeathResId(info, bNight,
		// true);
		// clearBitmap();
		// Drawable drawable = WeatherModule.getWipDrawable(mContext, resName,
		// bNet);
		//
		// imageNow.setImageDrawable(drawable);
		// imageNow.setVisibility(View.VISIBLE);
		// // imageNow.setImageResource(WeatherModule.GetFinalWeathResId(info,
		// // bNight));
		// }

		// Log.v("refreshNowWeatherImg", (System.currentTimeMillis() - a) + "");
	}

	/**
	 * @brief 【检查网络】
	 * @n<b>函数名称</b> :checkNetWork
	 * @return
	 * @return boolean
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-10下午03:55:04
	 */
	private boolean checkNetWork() {
		if (/*HttpToolKit.getActiveNetWorkName(mContext) == null*/!TelephoneUtil.isNetworkAvailable(mContext)) {
			CommonUI.ShowNetworkSet(mContext);
			return false;

		} else {
			return true;

		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.tempTextId || id == R.id.nowWeathTempTextId
				|| id == R.id.nowWeathTextId || id == R.id.rl_temp) {
			refreshWeather();
		} else if (id == R.id.weather_air_quality
				|| id == R.id.weather_humidity || id == R.id.weather_wind_scale) {
			WidgetUtils.startGuide(mContext,
					UICalendarGuideAty.CALENDAR_2015_GUIDE, null,
					WidgetUtils.DOWNLOAD_CLICK_FROM_ZHISHU);
			HiAnalytics.submitEvent(mContext, WidgetUtils.getAnalyticsId(mContext, R.string.analytics_weather_enter_recommend_huangli), WidgetUtils.ENTER_CLICK_FROM_ZHISHU);
		} else if (id == R.id.weatherOne_layout || id == R.id.weatherTwo_layout
				|| id == R.id.weatherThree_layout || id == R.id.day_weather_info_titile) {
			WidgetUtils.startGuide(mContext,
					UICalendarGuideAty.CALENDAR_2015_GUIDE, null,
					WidgetUtils.DOWNLOAD_CLICK_FROM_WEATHER);
			HiAnalytics.submitEvent(mContext, WidgetUtils.getAnalyticsId(mContext, R.string.analytics_weather_enter_recommend_huangli), WidgetUtils.ENTER_CLICK_FROM_WEATHER);
		} else if (id == R.id.twentyFourthWeather || id == R.id.llCurve) {
			WidgetUtils.startGuide(mContext,
					UICalendarGuideAty.CALENDAR_2015_GUIDE, null,
					WidgetUtils.DOWNLOAD_CLICK_FROM_SIX_HOUR_WEATHER);
			HiAnalytics.submitEvent(mContext, WidgetUtils.getAnalyticsId(mContext, R.string.analytics_weather_enter_recommend_huangli), WidgetUtils.ENTER_CLICK_FROM_SIX_HOUR);
		} else if (id == R.id.nowWeatherWindPower) {// 天气预警，点击查看详情
			if(null == warningInfo || null == warningDetailPopupWindow) return;
			if(warningDetailPopupWindow.isShowing()) {
				warningDetailPopupWindow.dismiss();
			} else {
				int[] tvWarningXY = new int[2];
				tvNowWindpower.getLocationInWindow(tvWarningXY);
				int x = tvWarningXY[0];
				int y = tvWarningXY[1] + tvNowWindpower.getHeight() + ScreenUtil.dip2px(mContext, 10);
				warningDetailPopupWindow.showAtLocation(tvNowWindpower, Gravity.NO_GRAVITY, x, y);
			}
		}
	}

	/**
	 * @brief 【显示预警】
	 * @n<b>函数名称</b> :ShowWarning
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-2-3上午10:18:37
	 */
	// private void ShowWarning() {
	/*
	 * if (TextUtils.isEmpty(mCityWeatherInfo.getCityCode())){ return; } Intent
	 * intent = new Intent(mContext, UIWarningAty.class);
	 * intent.setClassName(mContext.getPackageName(),
	 * UIWarningAty.class.getName()); if (ComfunHelp.checkActivity(mContext,
	 * intent)) { return; } intent.putExtra("id", mCityWeatherInfo.getId());
	 * mContext.startActivity(intent);
	 */
	// }

	private void setData(CityWeatherInfo c) {
		mCityWeatherInfo = c;
	}

	public CityWeatherInfo getCityWeatherInfo() {
		return mCityWeatherInfo;
	}

	public int getViewIndex() {
		return mCurrPos;
	}

	public String getCity() {
		return mCityWeatherInfo.getCityName();
	}
	
	public void setParentPager(ViewGroup parentPager) {
		this.parentPager = parentPager;
	}

	public void refreshData() {
		if (!mCityWeatherInfo.getIsRefreshToView()) {
			// 刷新界面
			refreshView();
		}
	}

	/**
	 * @brief 【显示城市管理】
	 * @n<b>函数名称</b> :DoShowSetAty
	 * @return void
	 * @<b>作者</b> : 陈希
	 * @<b>创建时间</b> : 2012-4-5下午07:23:28
	 */
	void DoShowSetAty() {
		Intent intent = new Intent(mContext, UIWeatherSetAty.class);
		intent.setClassName(mContext.getPackageName(),
				UIWeatherSetAty.class.getName());

		if (!ComfunHelp.checkActivity(mContext, intent)) {
			// 把更新控制器停了(任务清理),为了防止删除了，又添加进去
			GetWeatherControler.getInstance(mContext).clearTask();
			mContext.startActivity(intent);
		}
	}

	public void clearBitmap() {
		// try {
		// // BitmapDrawable db = (BitmapDrawable) imageNow.getDrawable();
		// if (db != null) { // && db != mDrawableNA
		// Bitmap bmp = db.getBitmap();
		// if (!bmp.isRecycled()) {
		// // imageNow.setImageDrawable(null);
		// // imageNow.setVisibility(View.GONE);
		// db.setCallback(null);
		// bmp.recycle();
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public AdImageView getAdsImageView() {
		return adsImageView;
	}

}
