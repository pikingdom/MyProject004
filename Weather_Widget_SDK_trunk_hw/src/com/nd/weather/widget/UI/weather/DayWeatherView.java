package com.nd.weather.widget.UI.weather;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calendar.CommData.DateInfo;
import com.calendar.CommData.DayWeatherInfo;
import com.calendar.CommData.YjcInfo;
import com.nd.calendar.Control.CalendarContext;
import com.nd.calendar.Control.ICalendarContext;
import com.nd.calendar.common.ConfigHelper;
import com.nd.calendar.util.CalendarInfo;
import com.nd.calendar.util.ComfunHelp;
import com.nd.weather.widget.R;

public class DayWeatherView extends LinearLayout implements OnClickListener {
	private Context mContext;

	private TextView m_textYu;
	private TextView m_textji;
	private TextView m_gmtTag;

	private DayWeatherInfo mDayWeatherInfo;

	private boolean bDataChanged = false;

	private int mLastStyle;

	private Resources mRes;

	private String m_gmt = null;
	private DateInfo m_date;

	private View mParent = null;

	private ICalendarContext m_calendarMgr;

	private LinearLayout starLayout;

	private int lightStarId;
	private int blackStarId;

	public DayWeatherView(Context context, AttributeSet atts) {
		super(context, atts);

		mContext = context;
		mRes = mContext.getResources();
		// initView();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initView();
	}

	public void setParentView(View view) {
		mParent = view;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mParent != null) {
			android.view.ViewGroup.LayoutParams lp = mParent.getLayoutParams();
			lp.height = h;
			mParent.setLayoutParams(lp);
		}
	}

	private void initView() {
		// 黄历
		m_textji = (TextView) findViewById(R.id.jiId);
		m_textYu = (TextView) findViewById(R.id.yuId);
		// m_textNongli = (TextView) findViewById(R.id.nongLiId);
		// m_tvJieQi = (TextView) findViewById(R.id.jieQiId);
		m_gmtTag = (TextView) findViewById(R.id.tvGMTtag);

		// m_tvSoftAd = (TextView) findViewById(R.id.tvSoftAd);

		// findViewById(R.id.weather_to_calendar).setOnClickListener(this);
		findViewById(R.id.weather_to_huangli).setOnClickListener(this);
		// 宜忌冲 图片
		// Drawable leftDrawable;
		// int iSize = ComfunHelp.dip2px(mContext, 16);
		// int iPadding = ComfunHelp.dip2px(mContext, 2);
		//
		// leftDrawable = mRes.getDrawable(R.drawable.ji);
		// leftDrawable.setBounds(0, 0, iSize, iSize);
		// m_textji.setCompoundDrawables(leftDrawable, null, null, null);
		// m_textji.setCompoundDrawablePadding(iPadding);
		//
		// leftDrawable = mRes.getDrawable(R.drawable.yu);
		// leftDrawable.setBounds(0, 0, iSize, iSize);
		// m_textYu.setCompoundDrawables(leftDrawable, null, null, null);
		// m_textYu.setCompoundDrawablePadding(iPadding);

		m_calendarMgr = CalendarContext.getInstance(mContext);

		lightStarId = R.drawable.star_orange;
		blackStarId = R.drawable.star_gray;

		starLayout = (LinearLayout) findViewById(R.id.yunshiId);
		composeStarView(mContext, starLayout, 5, lightStarId, blackStarId);
	}

	@Override
	public void onClick(View v) {
		// 2016-5-27 点击宜忌不跳转
//		int id = v.getId();
//		if (id == R.id.weather_to_huangli) {
//			WidgetUtils.startGuide(mContext,
//					UICalendarGuideAty.CALENDAR_2015_GUIDE,
//					WidgetUtils.ACT_SHOW_HULI,
//					WidgetUtils.DOWNLOAD_CLICK_FROM_HUANGLI);
//		}
	}

	public void refreshView() {
		if ((mDayWeatherInfo != null) && (bDataChanged)) {
			// 改变风格
			changeStyle();

			// 设置星期几
			// setWeekText();

			// 设置天气
			// setWeatherInfo();

			bDataChanged = false;
		}
	}

	public void setData(DayWeatherInfo dayWeatherInfo) {
		if (dayWeatherInfo != null) {
			mDayWeatherInfo = dayWeatherInfo;
			bDataChanged = true;
		}
	}

	// 设置运势星星
	private void composeStarView(Context ctx, LinearLayout linear, int n,
			int rid, int nid) {
		linear.removeAllViews();
		for (int i = 0; i < 5; i++) {
			ImageView view = new ImageView(ctx);
			view.setBackgroundResource(nid);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.LEFT;
			lp.rightMargin = ComfunHelp.dip2px(mContext, 5);
			int isSize = ComfunHelp.dip2px(mContext, 16);
			lp.width = isSize;
			lp.height = isSize;
			view.setLayoutParams(lp);

			linear.addView(view);
		}
		for (int i = 0; i < n; i++) {
			linear.getChildAt(i).setBackgroundResource(rid);
		}
	}

	public void changeStyle() {
		int nType = ConfigHelper.VALUE_DEFALUT_STYLE;
		if (mLastStyle == nType) {
			return;
		}

		mLastStyle = nType;

		// try {
		// int nResColor = mRes.getColor(R.color.week_name);
		// for (int i = 0; i < tvWeekName.length; i++) {
		// tvWeekName[i].setShadowLayer(0.8f, 0.8f, 0.8f, 0x8a000000);
		// tvWeekName[i].setTextColor(nResColor);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	/* 设置星期几 */
	// private void setWeekText() {
	// if (mDayWeatherInfo != null) {
	// // 日期变了才设置，这样不要每次都设置
	// if ((TextUtils.isEmpty(mLastSetDate))
	// || (!mDayWeatherInfo.getDays().get(0).date.equals(mLastSetDate))) {
	// int len = tvWeekName.length;
	// ArrayList<DayInfo> days = mDayWeatherInfo.getDays();
	// for (int i = 0; i < len; i++) {
	// // 从明天开始
	//
	// final DayInfo day = days.get(i + 2);
	// if (i == 0) {
	// tvWeekName[i].setText(mRes.getString(R.string.tomorrow));
	// } else {
	// tvWeekName[i].setText(day.week);
	// }
	//
	// }
	// // 记录设置时间
	// mLastSetDate = days.get(0).date;
	// }
	// }
	// }

	/**
	 * @brief 【设置天气信息】
	 * @n<b>函数名称</b> :setWeatherInfo
	 * @<b>作者</b> :
	 * @<b>修改</b> : chenx
	 * @<b>创建时间</b> : 2013-8-5下午3:12:05
	 * @<b>修改时间</b> :
	 */
	// private void setWeatherInfo() {
	// ArrayList<DayInfo> days = mDayWeatherInfo.getDays();
	//
	// if (!days.isEmpty() && days != null) {
	// int len = tvDayWeather.length;
	// // boolean bNet = HttpToolKit.isNetworkAvailable(mContext);
	//
	// for (int i = 0; i < len; i++) {
	// try {
	// // 从明天开始
	// final DayInfo day = days.get(i + 2);
	//
	// // 天气图标
	// // try {
	// // BitmapDrawable db =
	// // (BitmapDrawable)imDayIco[i].getDrawable();
	// // if ((db != null) && (!db.getBitmap().isRecycled())) {
	// // imDayIco[i].setVisibility(View.GONE);
	// // imDayIco[i].setImageDrawable(null);
	// // db.setCallback(null);
	// // db.getBitmap().recycle();
	// // }
	// // } catch (Exception e) {
	// // }
	//
	// // if (!TextUtils.isEmpty(day.imgResID)) {
	// // imDayIco[i].setImageDrawable(WeatherModule.getWipDrawable(mContext,
	// // day.imgResID, bNet));
	// // } else {
	// // imDayIco[i].setImageDrawable(null);
	// // }
	//
	// imDayIco[i].setImageResource(WeatherModule.GetFinalWeath64ResId(day.info));
	//
	// // 天气信息
	// tvDayWeather[i].setText(((i == 0) ? mRes.getString(R.string.tomorrow) :
	// day.week));
	//
	// tvDayTemp[i].setText(day.temperature);
	//
	// } catch (Exception e) {
	// imDayIco[i].setImageDrawable(null);
	// tvDayWeather[i].setText("");
	// } finally {
	// imDayIco[i].setVisibility(View.VISIBLE);
	// }
	// }
	// }
	// }

	/**
	 * @Title: refreshYiJiData
	 * @Description: TODO(刷新)
	 * @author yanyy
	 * @date 2012-11-27 上午09:23:56
	 * 
	 * @param gmt
	 * @param bRefresh
	 *            (是否强制刷新)
	 * @return void
	 * @throws
	 */
	public void refreshYiJiData(String gmt, boolean bRefresh) {
		// 没有强制刷新，时区一样就不需要刷新
		if ((!bRefresh) && (!TextUtils.isEmpty(m_gmt))
				&& (!TextUtils.isEmpty(gmt))) {
			if (m_gmt.equals(gmt)) {
				return;
			}
		}

		m_gmt = gmt;

		// if (!ComfunHelp.isLocalTimeZome(m_gmt)) {
		// // 跟手机当前时区不一样
		// m_gmtTag.setVisibility(View.VISIBLE);
		// } else {
		// m_gmtTag.setVisibility(View.INVISIBLE);
		// }

		YjcInfo yjcInfo = new YjcInfo();

		m_date = CalendarInfo.getSysDateInfo(m_gmt);
		m_calendarMgr.Get_CalendarMdl_Interface().getYJCInfo(m_date, yjcInfo);
		// if (ret != CalendarModule.CM_SUCCESS) {
		// String sMsg = CalendarModule.getYJCState(ret);
		// yjcInfo.setStrYi(sMsg);
		// yjcInfo.setStrJi(sMsg);
		// }

		// LunarInfo lunarInfo = calendarInfo.GetLunarInfoByYanLi(m_date);
		//
		// StringBuilder strDate = new StringBuilder();
		// strDate.append(lunarInfo.getTiangan()).append(lunarInfo.getDizhi()).append("年 ");
		// strDate.append(lunarInfo.isLeepMonth() ? "闰" : "");
		// strDate.append(lunarInfo.getMonthname()).append("月");
		// strDate.append(lunarInfo.getDayname());

		// String strJq =
		// StringHelp.getJieQiFormat(calendarInfo.GetBefOrAferJieQi(m_date),
		// m_date,
		// true);

		if (yjcInfo.getStrJi() != null) {
			m_textji.setText(yjcInfo.getStrJi().trim().replaceAll("\\s+", "、"));
		}

		if (yjcInfo.getStrYi() != null) {
			m_textYu.setText(yjcInfo.getStrYi().trim().replaceAll("\\s+", "、"));
		}

	}

	public DateInfo getDateInfo() {
		return m_date;
	}

	public void clearBitmap() {
		// for (ImageView img : imDayIco) {
		// try {
		// BitmapDrawable db = (BitmapDrawable)img.getDrawable();
		// if ((db != null)) {
		// img.setVisibility(View.GONE);
		// img.setImageDrawable(null);
		// db.setCallback(null);
		// db.getBitmap().recycle();
		// }
		// } catch (Exception e) {
		//
		// }
		// }
	}

}
