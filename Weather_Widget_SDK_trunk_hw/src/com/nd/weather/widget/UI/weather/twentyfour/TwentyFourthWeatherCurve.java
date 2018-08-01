package com.nd.weather.widget.UI.weather.twentyfour;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.View;

import com.nd.hilauncherdev.kitset.util.DateUtil;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.weather.widget.R;
import com.nd.weather.widget.UI.weather.twentyfour.HoursWeatherEntity.HourWecther;

import java.util.ArrayList;

public class TwentyFourthWeatherCurve extends View {
	
	/**
	 * 最多显示天数
	 */
	public final static int SHOW_MAX_DAYS = 6;

	//static final String TAG = "TwentyFourthWeatherCurve";
//	private Context mCtx;
	private HoursWeatherEntity mInfo;
	private ArrayList<HourWecther> mItems = new ArrayList<HourWecther>();
	//每一隔得间距是多少
	private int mInterval;
	//这个画布该画多长的线
//	private int mTotalWidth;
	//这个的高度，取决上个传进来的
	private int mCurveHeight;
	//画线的paint
	private Paint mPaintLine;
	//写底部时间的paint
	private Paint mPaintText;
	//写折线对应的温度
	private Paint mPaintWeather;
	//画虚线
	private Paint mPaintDottedLine;
	//最高温度
	private float mHighWeather;
	//最低温度
	private float mLowWeather;
	//最高最低温度的差值
	private float mInterZone;

	private Bitmap mWeatherBitmap;
	
	private String mNowTime;
	
	private  int[][] WEATHER_ICO_RES = { 
		{ R.drawable.wip_app_00_d, R.drawable.wip_app_00_n }, // 晴天
		{ R.drawable.wip_app_01_d, R.drawable.wip_app_01_n }, // 多云
		{ R.drawable.wip_app_02, R.drawable.wip_app_02 }, // 阴
		{ R.drawable.wip_app_18, R.drawable.wip_app_18 }, //雾
		{ R.drawable.wip_app_35, R.drawable.wip_app_35 }, // 大雾
		{ R.drawable.wip_app_36, R.drawable.wip_app_36 }, // 霾
		{ R.drawable.wip_app_07, R.drawable.wip_app_07 }, // 小雨
		{ R.drawable.wip_app_08, R.drawable.wip_app_08 }, // 中雨
		{ R.drawable.wip_app_09, R.drawable.wip_app_09 }, // 大雨
		{ R.drawable.wip_app_10, R.drawable.wip_app_10 }, // 暴雨
		{ R.drawable.wip_app_11, R.drawable.wip_app_11 }, // 大暴雨
		{ R.drawable.wip_app_12, R.drawable.wip_app_12 }, // 特大暴雨
		{ R.drawable.wip_app_03, R.drawable.wip_app_03_n }, // 阵雨
		{ R.drawable.wip_app_19, R.drawable.wip_app_19 }, // 冻雨
		{ R.drawable.wip_app_04, R.drawable.wip_app_04 }, // 雷阵雨
		{ R.drawable.wip_app_05, R.drawable.wip_app_05 }, // 雷阵雨伴有冰雹
		{ R.drawable.wip_app_41, R.drawable.wip_app_41 }, // 冰雹，但是图标还没有，先用NA代替
		{ R.drawable.wip_app_06, R.drawable.wip_app_06 }, // 雨夹雪
		{ R.drawable.wip_app_14, R.drawable.wip_app_14 }, // 小雪
		{ R.drawable.wip_app_15, R.drawable.wip_app_15 }, // 中雪
		{ R.drawable.wip_app_16, R.drawable.wip_app_16 }, // 大雪
		{ R.drawable.wip_app_17, R.drawable.wip_app_17 }, // 暴雪
		{ R.drawable.wip_app_13, R.drawable.wip_app_13 }, // 阵雪
		{ R.drawable.wip_app_32, R.drawable.wip_app_32 }, // 浮尘
		{ R.drawable.wip_app_33, R.drawable.wip_app_33 }, // 扬沙
		{ R.drawable.wip_app_20, R.drawable.wip_app_20 }, // 沙尘暴
		{ R.drawable.wip_app_34, R.drawable.wip_app_34 }, // 强沙尘暴
		{ R.drawable.wip_app_08, R.drawable.wip_app_08 }, // 小到中雨
		{ R.drawable.wip_app_09, R.drawable.wip_app_09 }, // 中到大雨
		{ R.drawable.wip_app_10, R.drawable.wip_app_10 }, // 大到暴雨
		{ R.drawable.wip_app_11, R.drawable.wip_app_11 }, // 暴雨到大暴雨
		{ R.drawable.wip_app_12, R.drawable.wip_app_12 }, // 大暴雨到特大暴雨
		{ R.drawable.wip_app_15, R.drawable.wip_app_15 }, // 小到中雪
		{ R.drawable.wip_app_16, R.drawable.wip_app_16 }, // 中到大雪
		{ R.drawable.wip_app_17, R.drawable.wip_app_17 }, // 大到暴雪
		{ R.drawable.wip_app_37, R.drawable.wip_app_37 }, // 风
		{ R.drawable.wip_app_38, R.drawable.wip_app_38 }, // 大风
		{ R.drawable.wip_app_40, R.drawable.wip_app_40 }, // 旋风
		{ R.drawable.wip_app_na, R.drawable.wip_app_na }, // 热带风暴
		{ R.drawable.wip_app_39, R.drawable.wip_app_39 }, // 龙卷风
		{ R.drawable.wip_app_na, R.drawable.wip_app_na }, // 热
		{ R.drawable.wip_app_na, R.drawable.wip_app_na }, // 冷
		{R.drawable.wip_app_00, R.drawable.wip_app_00_sundown},//日落与日出
};
	
	private PathEffect effect;
	private float avgHeight;
	private float iDownedge;
	private float iDrawHeight;
	private Paint mPaintLaterBg;
	private Paint mPaintBeforeBg;
	private Paint mPaintBeforeTime;
	private Paint mPaintBeforeLine;
	
	public TwentyFourthWeatherCurve (Context context, HoursWeatherEntity info, int totalWidth){
		super(context);
//		this.mCtx= context;
		this.mInfo = info;
		this.mItems = (ArrayList<HourWecther>) info.items;
//		mTotalWidth = totalWidth;
		initData(context);
	}

	private void initData(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels - ScreenUtil.dip2px(context, 32);
		
		mInterval = width / SHOW_MAX_DAYS;
		//初始化画笔
		mPaintLine = new Paint();
		mPaintLine.setColor(getResources().getColor(R.color.weather_curve_line));
		mPaintLine.setShadowLayer(1, 1, 1, Color.BLACK);
		mPaintLine.setStrokeWidth(getResources().getDimension(R.dimen.weather_curve_width));
		mPaintLine.setAntiAlias(true);
		
		mPaintText = new Paint();
		mPaintText.setColor(getResources().getColor(R.color.weather_text_color));
		mPaintText.setShadowLayer(1, 1, 1, Color.BLACK);
		mPaintText.setTextSize(getResources().getDimension(R.dimen.weather_curve_time));
		mPaintText.setAntiAlias(true);
		
		mPaintWeather= new Paint();
		mPaintWeather.setColor(getResources().getColor(R.color.weather_text_color));
		mPaintWeather.setShadowLayer(1, 1, 1, Color.BLACK);
		mPaintWeather.setTextSize(getResources().getDimension(R.dimen.weather_curve_weather));
		mPaintWeather.setAntiAlias(true);
		
		mPaintDottedLine = new Paint();
		mPaintDottedLine.setStyle(Paint.Style.STROKE);
		mPaintDottedLine.setShadowLayer(1, 1, 1, Color.BLACK);
		mPaintDottedLine.setColor(getResources().getColor(R.color.weather_curve_line));
		mPaintDottedLine.setAntiAlias(true);
		
		mPaintLaterBg = new Paint();
		mPaintLaterBg.setColor(getResources().getColor(R.color.paint_later_bg));
		mPaintLaterBg.setShadowLayer(1, 1, 1, Color.BLACK);
		mPaintLaterBg.setStyle(Paint.Style.FILL);
		mPaintLaterBg.setAntiAlias(true);
		
		mPaintBeforeBg = new Paint();
		mPaintBeforeBg.setColor(getResources().getColor(R.color.paint_before_bg));
		mPaintBeforeBg.setShadowLayer(1, 1, 1, Color.BLACK);
		mPaintBeforeBg.setStyle(Paint.Style.FILL);
		mPaintBeforeBg.setAntiAlias(true);
		
		effect = new DashPathEffect(new float[]{5,5,5,5},5);
		mPaintDottedLine.setPathEffect(effect);
		mPaintLine.setStrokeWidth(getResources().getDimension(R.dimen.weather_curve_width));
		mPaintLine.setAntiAlias(true);
		
		mHighWeather = mInfo.temp.height;
		mLowWeather = mInfo.temp.low;
		mInterZone = mHighWeather - mLowWeather;
		
		Time time = new Time();
		time.setToNow();
		mNowTime = DateUtil.getStringDateShort() + " " + (time.hour < 10 ? "0" + Integer.toString(time.hour) : Integer.toString(time.hour)) + ":00:00";
		
		mPaintBeforeTime = new Paint();
		mPaintBeforeTime.setColor(getResources().getColor(R.color.line_time_before));
		mPaintBeforeTime.setShadowLayer(1, 1, 1, Color.BLACK);
		mPaintBeforeTime.setTextSize(getResources().getDimension(R.dimen.weather_curve_time));
		mPaintBeforeTime.setAntiAlias(true);
		
		mPaintBeforeLine = new Paint();
		mPaintBeforeLine.setColor(getResources().getColor(R.color.line_time_before));
		mPaintBeforeLine.setShadowLayer(1, 1, 1, Color.BLACK);
		mPaintBeforeLine.setStrokeWidth(getResources().getDimension(R.dimen.weather_curve_width));
		mPaintBeforeLine.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		mCurveHeight = this.getHeight();
		
		drawTemperature(canvas);
	}
	
	private void drawTemperature(Canvas canvas) {
		
		avgHeight = (mCurveHeight) / 8;
		iDownedge = (float) (avgHeight * 1.5);
		iDrawHeight = avgHeight * 2;
		//最高温度最低温度的差值
		
		float fWidthx = 0;
		float fHighy = 0;
		float lastWidthx = 0;
		float lastHighy = 0;
		//统计有多少个相同的温度
		int iSameWeather = 0;
		//前一个温度是多少
		int lastWeather = 0;
		//上一个climate的值
		int lastClimate = 0;
		//统计有多少个相同的天气图标
		int iSameClimate = 0;
		
		int iTimeIndex = 0;
		
		Path pathBg = new Path();
		
		boolean isNow = false;
		
		float lastLaterX = 0;
		float lastLaterY = 0;
		
		//画刻度尺
		//canvas.drawLine(0, avgHeight * 6, mTotalWidth, avgHeight * 6, mPaintLine);
		for (int i = 0; i < mItems.size(); i++) {
			if (i== 0) {
				//第一点不需要画
			}else {
				if (!isNow) {
//					mPaintLine.setColor(getResources().getColor(R.color.line_time_before));
					mPaintLine.setColor(getResources().getColor(R.color.weather_text_color));
				}else {
					mPaintLine.setColor(getResources().getColor(R.color.weather_text_color));
				}
				if (mItems.get(i).name.equals(mNowTime)) {
					mPaintLine.setColor(getResources().getColor(R.color.weather_text_color));
				}
				canvas.drawLine(mInterval * i, avgHeight * 6, mInterval * i, avgHeight * 6 - 8, mPaintLine);
			}
		}
		
		for (int i = 0; i < mItems.size(); i++) {
			fWidthx = mInterval * i;
			fHighy = (mHighWeather - mItems.get(i).temp) / mInterZone * iDrawHeight;
			fHighy = fHighy + iDownedge;
			
			if (i == 0) {
				iSameWeather += 1;
				lastWeather = mItems.get(i).temp;
				//相同图标的处理
			}else {
				//画出折线
				if (!isNow) {
//					mPaintLine.setColor(getResources().getColor(R.color.line_time_before));
					mPaintLine.setColor(getResources().getColor(R.color.weather_text_color));
				}else {
					mPaintLine.setColor(getResources().getColor(R.color.weather_text_color));
				}
				canvas.drawLine(lastWidthx, lastHighy, fWidthx, fHighy, mPaintLine);
				
				//标注时间出来
				
				if (i != (mItems.size() - 1)) {
					iTimeIndex = i;
					if (!isNow) {
//						mPaintText.setColor(getResources().getColor(R.color.line_time_before));
						mPaintLine.setColor(getResources().getColor(R.color.weather_text_color));
					}else {
						mPaintText.setColor(getResources().getColor(R.color.weather_text_color));
					}
					if (mItems.get(i).type != 1) {
						//日落的时间
						iTimeIndex = iTimeIndex - 1;
						String detailTime = mItems.get(i).time;
						String[] spliteDetailTime = detailTime.split(" ");
						String[] clickTime = spliteDetailTime[1].split(":");
						String time = clickTime[0]+":"+clickTime[1];
						String explain = "日落";
						if (mItems.get(i).type == 3) {
							explain = "日落";
						}else {
							explain = "日出";
						}
						int explainX = getTextWidth(mPaintText, explain);
						int explainY = getTextHeight(mPaintText, explain);
						canvas.drawText(explain, fWidthx - explainX / 2, avgHeight * 6 + 20 + explainY, mPaintText);
						int timeX = getTextWidth(mPaintText, time);
						int timeY = getTextHeight(mPaintText, time);
						canvas.drawText(time, fWidthx - timeX / 2, avgHeight * 6 + 20 + explainY + timeY + 10, mPaintText);
					}else {
						String time = mItems.get(i).name;
						if (mItems.get(i).time.equals(mNowTime)) {
							time = "现在";
							float circleX = i * mInterval;
							float circleY = ((mHighWeather - mItems.get(i).temp) / mInterZone) * iDrawHeight + iDownedge;
							Paint paint = new Paint();
							paint.setStyle(Style.FILL);
							paint.setColor(getResources().getColor(R.color.weather_text_color));
							canvas.drawCircle(circleX, circleY, 5, paint);
						}else if (iTimeIndex == 1) {
							time = mItems.get(i).name;
						}else if ((iTimeIndex - 1) % 3 == 0) {
							time = mItems.get(i).name;
						}else {
							time = mItems.get(i).name;
						}
						if (time.contains("现在")) {
							mPaintText.setColor(getResources().getColor(R.color.weather_text_color));
							mPaintText.setTextSize(getResources().getDimension(R.dimen.weather_curve_now));
							int tX = getTextWidth(mPaintText, time);
							int tY = getTextHeight(mPaintText, time);
							canvas.drawText(time, fWidthx - tX / 2, avgHeight * 6 + 20 + tY, mPaintText);
							mPaintText.setTextSize(getResources().getDimension(R.dimen.weather_curve_time));
						}else {
							if (!time.equals("")) {
								int tX = getTextWidth(mPaintText, time);
								int tY = getTextHeight(mPaintText, time);
								canvas.drawText(time, fWidthx - tX / 2, avgHeight * 6 + 20 + tY, mPaintText);
							}			
						}
						
					}
					//标示温度
					if (lastWeather == mItems.get(i).temp) {
						iSameWeather += 1;
					}else {
						if (!isNow) {
//							mPaintText.setColor(getResources().getColor(R.color.line_time_before));
							mPaintLine.setColor(getResources().getColor(R.color.weather_text_color));
						}else {
							mPaintText.setColor(getResources().getColor(R.color.weather_text_color));
						}
						if ((i - 1) != 0) {
							//不相同的画，那么久必须初始化以及画前一个出来
							if (iSameWeather % 2 == 0) {
								//刚好是2的整数个，那么画在某一个的中间
								int index = i - (iSameWeather /2);
								int width = (int) (index * mInterval - mInterval/2);
								String temp = lastWeather+"°";
								int tX = getTextWidth(mPaintWeather, temp);
								float hight = ((float)(mHighWeather - mItems.get(i - 1).temp) / mInterZone) * iDrawHeight + iDownedge;
								canvas.drawText(temp, width - tX / 2, hight - 10, mPaintText);
							}else {
								//单数个，画在某一个中间
								int index = i - (iSameWeather /2) - 1;
								int width = (int) (index * mInterval);
								String temp = mItems.get(i-1).temp+"°";
								int tX = getTextWidth(mPaintWeather, temp); 
								float hight = ((float)(mHighWeather - mItems.get(i - 1).temp) / mInterZone) * iDrawHeight + iDownedge;
								canvas.drawText(temp, width - tX / 2, hight - 10, mPaintText);
							}
						}
						
						iSameWeather = 1;
						lastWeather = mItems.get(i).temp;
					}
				}else {
					//最后一个标示温度
					if (lastWeather == mItems.get(i).temp) {
						//如果与前一个的温度相同
						iSameWeather += 1;
						
						//不相同的画，那么久必须初始化以及画前一个出来
						if (iSameWeather % 2 == 0) {
							//刚好是2的整数个，那么画在某一个的中间
							int index = i - (iSameWeather /2) + 1;
							int width = (int) (index * mInterval - mInterval/2);
							String temp = lastWeather + "°";
							int tX = getTextWidth(mPaintWeather, temp);
							float hight = ((float)(mHighWeather - mItems.get(i - 1).temp) / mInterZone) * iDrawHeight + iDownedge;
							canvas.drawText(temp, width - tX / 2, hight - 10, mPaintText);
						}else {
							//单数个，画在某一个中间
							int index = i - (iSameWeather /2);
							int width = (int) (index * mInterval);
							String temp = mItems.get(i-1).temp + "°";
							int tX = getTextWidth(mPaintWeather, temp);
							float hight = ((float)(mHighWeather - mItems.get(i - 1).temp) / mInterZone) * iDrawHeight + iDownedge;
							canvas.drawText(temp, width - tX / 2, hight - 10, mPaintText);
						}
					}else {
						if (iSameWeather % 2 == 0) {
							//刚好是2的整数个，那么画在某一个的中间
							int index = i - (iSameWeather /2);
							int width = (int) (index * mInterval - mInterval/2);
							String temp = lastWeather + "°";
							int tX = getTextWidth(mPaintWeather, temp);
							float hight = ((float)(mHighWeather - mItems.get(i - 1).temp) / mInterZone) * iDrawHeight + iDownedge;
							canvas.drawText(temp, width - tX / 2, hight - 10, mPaintText);
						}else {
							//单数个，画在某一个中间
							int index = i - (iSameWeather /2) - 1;
							int width = (int) (index * mInterval);
							String temp = mItems.get(i-1).temp + "°";
							int tX = getTextWidth(mPaintWeather, temp);
							float hight = ((float)(mHighWeather - mItems.get(i - 1).temp) / mInterZone) * iDrawHeight + iDownedge;
							canvas.drawText(temp, width - tX / 2, hight - 10, mPaintText);
						}
					}
				}	
			}
			
			//画背景图
			if (isNow) {
				pathBg.reset();
				float nowX = i * mInterval;
				float nowY = ((mHighWeather - mItems.get(i).temp) / mInterZone) * iDrawHeight + iDownedge;
				pathBg.moveTo(lastLaterX, avgHeight * 6);
				pathBg.lineTo(nowX, avgHeight * 6);
				pathBg.lineTo(nowX, nowY);
				pathBg.lineTo(lastLaterX, lastLaterY);
				canvas.drawPath(pathBg, mPaintLaterBg);
				lastLaterX = nowX;
				lastLaterY = nowY;
				//isNow = false;
			}else {
				if (mItems.get(i).time.equals(mNowTime)) {
					if (mItems.get(i).type != 3) {
						isNow = true;
						pathBg.reset();
						float nowX = i * mInterval;
						float nowY = ((mHighWeather - mItems.get(i).temp) / mInterZone) * iDrawHeight + iDownedge;
						pathBg.moveTo(lastLaterX, avgHeight * 6);
						pathBg.lineTo(nowX, avgHeight * 6);
						pathBg.lineTo(nowX, nowY);
						pathBg.lineTo(lastLaterX, lastLaterY);
						canvas.drawPath(pathBg, mPaintBeforeBg);
						lastLaterX = nowX;
						lastLaterY = nowY;
					}		
				}else {
					if (i == 0) {
						lastLaterX = i * mInterval;
						lastLaterY = ((mHighWeather - mItems.get(i).temp) / mInterZone) * iDrawHeight + iDownedge;
					}else {
						pathBg.reset();
						float nowX = i * mInterval;
						float nowY = ((mHighWeather - mItems.get(i).temp) / mInterZone) * iDrawHeight + iDownedge;
						pathBg.moveTo(lastLaterX, avgHeight * 6);
						pathBg.lineTo(nowX, avgHeight * 6);
						pathBg.lineTo(nowX, nowY);
						pathBg.lineTo(lastLaterX, lastLaterY);
						canvas.drawPath(pathBg, mPaintBeforeBg);
						lastLaterX = nowX;
						lastLaterY = nowY;
					}
				}
				
			}
			//画图标
			if (i == 0) {	
				iSameClimate = 1;
				lastClimate = mItems.get(i).climate;		
			}else {
				//不是最后
				if (i != (mItems.size() - 1)) {
					//遇到了日落或者日出的图标了
					if (mItems.get(i).type != 1) {
						if (lastClimate == mItems.get(i).climate) {
							iSameClimate += 1;
						}
						
						int climateIndex = 0;
						drawBitmap(i, climateIndex, fWidthx, canvas);	
						//先画出之前
						if (iSameClimate % 2 == 0) {
							//刚好是2的整数个，那么画在某一个的中间
							int index = i - (iSameClimate /2);
							int width = (int) (index * mInterval + mInterval /2);
							drawBitmap(index, lastClimate, width, canvas);
						}else {
							//单数个，画在某一个中间
							int index = i - (iSameClimate /2);
							int width = (int) (index * mInterval);
							drawBitmap(index, lastClimate, width, canvas);
						}
						iSameClimate = 1;
					}else {
							if (lastClimate == mItems.get(i).climate) {
								iSameClimate++;
							}else {
								//不相同的画，那么久必须初始化以及画前一个出来
								//画出虚线
								//iSameClimate += 1;
								float topHeight = ((float)(mHighWeather - mItems.get(i).temp) / mInterZone) * iDrawHeight + iDownedge;
								Path path = new Path();
								path.moveTo(i * mInterval, topHeight);
								path.lineTo(i* mInterval, avgHeight * 6 - 8);
								canvas.drawPath(path, mPaintDottedLine);
								
								if (iSameClimate % 2 == 0) {
									//刚好是2的整数个，那么画在某一个的中间
									int index = i - (iSameClimate /2); 
									int width = (int) (index * mInterval);
									drawBitmap(index, lastClimate, width, canvas);
								}else {
									//单数个，画在某一个中间
									int index = i - (iSameClimate /2);
									int width = (int) (index * mInterval - mInterval /2);
									drawBitmap(index, lastClimate, width, canvas);
								}
								iSameClimate = 1;
								lastClimate = mItems.get(i).climate;
							}		
					}	
				}
				else {
					//最后一个标示温度
					if (lastClimate == mItems.get(i).climate) {
						//如果与前一个的温度相同
						if (mItems.get(i - 1).type == 1) {
							iSameClimate += 1;
							if (iSameClimate % 2 == 0) {
								//刚好是2的整数个，那么画在某一个的中间
								int index = i - (iSameClimate /2);
								int width = (int) (index * mInterval + mInterval /2);
						
								drawBitmap(index, mItems.get(i).climate, width, canvas);
							}else {
								//单数个，画在某一个中间
								int index = i - (iSameClimate /2);
								int width = (int) (index * mInterval);
							
								drawBitmap(index, mItems.get(i).climate, width, canvas);
							}
						}else {
							if (iSameClimate % 2 == 0) {
								//刚好是2的整数个，那么画在某一个的中间
								int index = i - (iSameClimate /2); 
								int width = (int) (index * mInterval);
								
								drawBitmap(index, lastClimate, width, canvas);
							}else {
								//单数个，画在某一个中间
								int index = i - (iSameClimate /2);
								int width = (int) (index * mInterval - mInterval /2);
								
								drawBitmap(index, lastClimate, width, canvas);
							}
						}
						
					}else {
						//iSameClimate += 1;
						if (iSameClimate % 2 == 0) {
							//刚好是2的整数个，那么画在某一个的中间
							int index = i - (iSameClimate /2); 
							int width = (int) (index * mInterval);
							
							drawBitmap(index, lastClimate, width, canvas);
						}else {
							//单数个，画在某一个中间
							int index = i - (iSameClimate /2);
							int width = (int) (index * mInterval - mInterval /2);
							
							drawBitmap(index, lastClimate, width, canvas);
						}
					}
				}
			}
			
			lastWidthx = fWidthx;
			lastHighy = fHighy;
			
		}
		
	
	}
	
	//获取字符串的宽度
	public static int getTextWidth(Paint paint, String str) {  
        int iRet = 0;  
        if (str != null && str.length() > 0) {  
            int len = str.length();  
            float[] widths = new float[len];  
            paint.getTextWidths(str, widths);  
            for (int j = 0; j < len; j++) {  
                iRet += (int) Math.ceil(widths[j]);  
            }  
        }  
        return iRet;  
    }  
	
	//获取字符串的高度
	public static int getTextHeight(Paint paint, String str) {  
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, 1, rect);
        return rect.height();
    } 
	
	//获取缩小或者放大后的的图片
	public Bitmap resizedBitmap(Bitmap bitmap, int newWidth, int newHeight){
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeigh = bitmap.getHeight();
		float scaleWidth = ((float) newWidth) / bitmapWidth;
		float scaleHeight = ((float) newHeight) / bitmapHeigh;
		Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth,
        		bitmapHeigh, matrix, true);
	}
	public void drawBitmap(int i, int climateIndex, float fWidthx, Canvas canvas){
		
		int type = 0;
		boolean isSun = false;
		Bitmap resizedBitmap = null;
		if (mItems.get(i).type == 1) {
			isSun = false;
			type = mItems.get(i).dayType;
			mWeatherBitmap = ((BitmapDrawable)getResources().getDrawable(GetTwentyfourWeathResId(climateIndex, type, isSun))).getBitmap();
			resizedBitmap = resizedBitmap(mWeatherBitmap,mInterval - 10,mInterval - 10);
		}else {
			isSun = true;
			type = mItems.get(i).type -1;
			mWeatherBitmap = ((BitmapDrawable)getResources().getDrawable(GetTwentyfourWeathResId(climateIndex, type, isSun))).getBitmap();	
			//resizedBitmap = resizedBitmap(mWeatherBitmap,40,32);
			resizedBitmap = mWeatherBitmap;
		}
		
		int xoffset_weather = resizedBitmap.getWidth() / 2;
		int yoffset_weather = resizedBitmap.getHeight() / 2;
		int bitmapWidthSize = (int) (fWidthx - xoffset_weather);
		int tempHeigh = (int) (((mHighWeather - mItems.get(i).temp) / mInterZone) * iDrawHeight + iDownedge);
		if (mItems.get(i).type == 1) {
			int bitmapHeighSize = (int) ((6 * avgHeight - tempHeigh)/2 + tempHeigh - yoffset_weather);
			canvas.drawBitmap(resizedBitmap, bitmapWidthSize,  bitmapHeighSize, null);
		}else{		
			canvas.drawBitmap(resizedBitmap, bitmapWidthSize,  6 * avgHeight - resizedBitmap.getHeight() - 10, null);
			Path path = new Path();
			path.moveTo(i * mInterval, tempHeigh);
			path.lineTo(i* mInterval, 6 * avgHeight - resizedBitmap.getHeight() - 10);
			canvas.drawPath(path, mPaintDottedLine);
		}	
	}
	
	//获取24小时天气的图标
	public  int  GetTwentyfourWeathResId(int indext, int type, boolean isSun) {
		//climate都是从1开始了，为了对应起来而已
		indext = indext - 1;
		if (isSun) {
			return WEATHER_ICO_RES[WEATHER_ICO_RES.length - 1][type == 2 ? 1:0];
		}else {
			return WEATHER_ICO_RES[indext][type == 2 ? 1:0];
		}	
	}
}
