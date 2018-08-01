package com.nd.weather.widget.UI.weather;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.calendar.CommData.DayWeatherInfo;
import com.calendar.CommData.DayWeatherInfo.DayInfo;
import com.calendar.CommData.DayWeatherInfo.TempInfo;
import com.nd.calendar.common.ConfigHelper;
import com.nd.weather.widget.R;

public class TemperatureCurveView extends View {
    final static String TAG = "TemperatureCureView";

    private final static int COUNT = 4;

    private final static int OFF_SET = 2;

    private Context mContext;

    private Resources mRes;

    private int mLastStyle = ConfigHelper.VALUE_WEATHER_NEW;

    private DayWeatherInfo weathInfo = null;

    private TextView tvHint;

    private DrawTempCure mDraw;

    private int mLineColor;

    private int mWeekColor;

    private StringBuilder mTempBuffer = new StringBuilder();

    private int mWidth = 0;
    private int mHeight = 0;

    public TemperatureCurveView(Context context) {
        super(context);
        mContext = context;
        mRes = getResources();
        // 初始化视图控件
        initView();
    }

    // 设置数据
    public void setData(DayWeatherInfo info) {
        if (info != null) {
            weathInfo = info;
            mDraw.parseData(info);
        }
    }

    /**
     * @brief 【初始化页面】
     * @n<b>函数名称</b> :initView
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-3-31上午11:12:43
     */
    private void initView() {
    	mDraw = new DrawTempCure(mContext);
        setWillNotDraw(false);
    }

    /**
     * @brief 【设置页面风格】
     * @n<b>函数名称</b> :changeStyle
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-3-31上午11:12:43
     */
//    public void changeStyle() {
//        int nType = ConfigHelper.VALUE_DEFALUT_STYLE;
//
//        if (mLastStyle == nType) {
//            return;
//        }
//
//        mLastStyle = nType;
//        try {
//        	mWeekColor = mRes.getColor(R.color.week_name);
//            mLineColor = mRes.getColor(R.color.temp_line_color1);
//
//            // tvHint.setTextColor(nResColor3);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // ///////////////////////////////////////////////////////////////////////////
    /**
     * @brief 【刷新视图】
     * @n<b>函数名称</b> :refreshView
     * @return void
     * @<b>作者</b> : 陈希
     * @<b>创建时间</b> : 2012-1-18下午06:38:06
     */
    public void refreshView() {
        //Log.e(TAG, "refreshView width=" + mWidth + ",height=" + mHeight);
        if ((mWidth <= 0) || (mHeight <= 0)) {
            // 没取到长宽 不处理
            return;
        }
        //changeStyle();
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((canvas.getHeight() == 0) || (canvas.getWidth() == 0)){
            return;
        }
        
        if (mDraw != null) {
            mDraw.drawTemperatureCanvas(canvas);
        }
    }

    // 画曲线
    private class DrawTempCure {
        private int[] iHigh;
        private int[] iLow;
        private Paint pLineHeight;
        private Paint pLineLow;
        private Paint pLine;
        private Paint pWeek;
//        private TextPaint pHighText;
//        private TextPaint pLowText;
        private TextPaint pTempText;
        private float factor;
        private float weekY;
        private float tempY;
        private float curvetop;
        private float curvebottom;
//        private float tempSpace;
        private Bitmap nodeHeight;
        private Bitmap nodeLow;
        private float maxH = -10000;
        private float minH = 10000;

        public DrawTempCure(Context context) {
            nodeHeight = ((BitmapDrawable) mRes.getDrawable(R.drawable.templine_high))
                    .getBitmap();
            nodeLow = ((BitmapDrawable) mRes.getDrawable(R.drawable.templine_low))
                    .getBitmap();

            mLineColor = mRes.getColor(R.color.temp_line_color);
            mWeekColor = mRes.getColor(R.color.week_name);

            //changeStyle();

            Resources res = context.getResources();
            float lineWidth = res.getDimension(R.dimen.curve_line_width);
            pLineHeight = new Paint();
            pLineHeight.setAntiAlias(true);
            pLineHeight.setColor(res.getColor(R.color.day_temp_color));
            pLineHeight.setStyle(Paint.Style.STROKE);
            pLineHeight.setStrokeWidth(lineWidth);

            pLineLow = new Paint();
            pLineLow.setAntiAlias(true);
            pLineLow.setColor(res.getColor(R.color.night_temp_color));
            pLineLow.setStyle(Paint.Style.STROKE);
            pLineLow.setStrokeWidth(lineWidth);

            pLine = new Paint();
            pLine.setStyle(Paint.Style.FILL);
            pLine.setStrokeWidth(res.getDimension(R.dimen.curve_splite_line_width));

            pWeek = new Paint();
            pWeek.setAntiAlias(true);
            pWeek.setTextAlign(Align.CENTER);
            pWeek.setTextSize(res.getDimension(R.dimen.weeksize));

//            pHighText = new TextPaint();
//            pHighText.setAntiAlias(true);
//            pHighText.setColor(res.getColor(R.color.day_temp_color));
//            pHighText.setTextSize(res.getDimension(R.dimen.tempinfosize));
//
//            pLowText = new TextPaint();
//            pLowText.setAntiAlias(true);
//            pLowText.setColor(res.getColor(R.color.night_temp_color));
//            pLowText.setTextSize(res.getDimension(R.dimen.tempinfosize));
            
            pTempText = new TextPaint();
            pTempText.setAntiAlias(true);
            pTempText.setColor(res.getColor(R.color.temp_color));
            pTempText.setTextSize(res.getDimension(R.dimen.tempinfosize));
            pTempText.setTextAlign(Align.CENTER);
            
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            factor = metrics.density;

            weekY = pWeek.getTextSize();
            tempY = weekY + 6 * factor + pTempText.getTextSize();
            
            curvetop = tempY + 12 * factor;
            curvebottom = 15 * factor;
//            tempSpace = 1 * factor;
        }
        
        /*解析数据*/
        private void parseData(DayWeatherInfo dayWeather){
            if (iHigh == null) {
                iHigh = new int[COUNT];
            }
            if (iLow == null) {
                iLow = new int[COUNT];
            }
            
            final ArrayList<DayInfo> days = dayWeather.getDays();
            for (int i = 0; i < COUNT; i++) {
                final TempInfo temp = days.get(i + OFF_SET).tempInfo;
                if (temp != null) {
                    try {
                        iHigh[i] = Integer.parseInt(temp.strDayTemp);
                    } catch (Exception e) {
                        e.printStackTrace();
                        iHigh[i] = Integer.MAX_VALUE;
                    }

                    try {
                        iLow[i] = Integer.parseInt(temp.strNightTemp);
                    } catch (Exception e) {
                        e.printStackTrace();
                        iLow[i] = Integer.MAX_VALUE;
                    }
                } else {
                    iHigh[i] = Integer.MAX_VALUE;
                    iLow[i] = Integer.MAX_VALUE;
                }
            }
            
            maxH = -10000;
            minH = 10000;
            int iTotal = iHigh.length;
            for (int i = 0; i < iTotal; i++) {
                if (iHigh[i] != Integer.MAX_VALUE) {
                    // 说明没有温度，不能算
                    maxH = Math.max(iHigh[i], maxH);
                    minH = Math.min(iHigh[i], minH);
                }
                if (iLow[i] != Integer.MAX_VALUE) {
                    maxH = Math.max(iLow[i], maxH);
                    minH = Math.min(iLow[i], minH);
                }
            }
        }
        
        private void drawTemperatureCanvas(Canvas c){
            try {
                // 画星期几和温度
                mDraw.drawWeekAndTempText(c);

                // 画5根线
                mDraw.drawLine(c);
                
                drawTrendLine(c);
//                drawTempText(c);
            } catch (Exception e) {

            }
        }

//        private float getTextWidth(Paint p, String text) {
//            Rect r = new Rect();
//            p.getTextBounds(text, 0, text.length(), r);
//            return r.right - r.left;
//        }

        private void drawLine(Canvas canvas) {
            // 判断风格取颜色
            pLine.setColor(mLineColor);
            float iLeft = 13 * factor;
            float iRight = 10 * factor;
            int iTotalHeight = mHeight;

            float avgheight = (iTotalHeight - curvebottom - curvetop) / 4;
            for (int i = 0; i < 5; i++) {
                int h = (int) (curvetop + avgheight * i);
                canvas.drawLine(iLeft, h, mWidth - iRight, h, pLine);
            }
        }

        private void drawWeekAndTempText(Canvas canvas) {
            // 画星期几和温度
            //final String text = weathInfo.getDays().get(0).week;
//            float iTextW = getTextWidth(pHighText, text);
            pWeek.setColor(mWeekColor);
            float ixavg = mWidth / COUNT;
            float ixStart = ixavg / 2;
            
            ArrayList<DayInfo> days = weathInfo.getDays();
            for (int i = 0; i < COUNT; i++) {
                final DayInfo day = days.get(i + 2);
                final String week;
                if (i == 0) {
                    week = mContext.getResources().getString(R.string.tomorrow);
                } else {
                	week = day.week;
                }

                final float ixCenter = ixStart + ixavg * i;
                canvas.drawText(week, ixCenter, weekY, pWeek);
                
                if (day.tempInfo != null) {
                    mTempBuffer.delete(0, mTempBuffer.length());
                    
                    if (!TextUtils.isEmpty(day.tempInfo.strDayTemp)) {
                        mTempBuffer.append(day.tempInfo.strDayTemp);
                    }
                    
                    if (!TextUtils.isEmpty(day.tempInfo.strNightTemp)) {
                    	if (mTempBuffer.length() > 0) {
                    		mTempBuffer.append('~');
						}
                        mTempBuffer.append(day.tempInfo.strNightTemp);
                    }
                    
                    if (mTempBuffer.length() > 0) {
                        mTempBuffer.append("℃");
                        canvas.drawText(mTempBuffer.toString(), ixCenter, tempY, pTempText);		
					}
                }
            }
        }

//        /* 画温度文字 */
//        private void drawTempText(Canvas canvas) {
//            // 温度
//            int iWidth = mWidth;
//            int iTotalHeight = mHeight;
//
//            float ixavg = iWidth / COUNT;
//
//            // 温度
//            float ibottom = iTotalHeight - tempbottom;
//            ArrayList<DayInfo> days = weathInfo.getDays();
//            for (int i = 0; i < COUNT; i++) {
//                final DayInfo day = days.get(i + 2);
//                
//                if (day.tempInfo != null) {
//                    float tstart = ixavg / 2 + i * ixavg;
//                    mTempBuffer.delete(0, mTempBuffer.length());
//                    
//                    // draw high
//                    if (!TextUtils.isEmpty(day.tempInfo.strDayTemp)) {
//                        mTempBuffer.append(day.tempInfo.strDayTemp);//.append("℃");
////                        float lstart = tstart - getTextWidth(pHighText, mTempBuffer.toString());
////                        canvas.drawText(mTempBuffer.toString(), lstart - tempSpace, ibottom,
////                                pHighText);
//                    }
//
//                    // draw low
//                    if (!TextUtils.isEmpty(day.tempInfo.strNightTemp)) {
//                        //mTempBuffer.delete(0, mTempBuffer.length());
//                    	if (mTempBuffer.length() > 0) {
//                    		mTempBuffer.append('~');
//						}
//                    	
//                        mTempBuffer.append(day.tempInfo.strNightTemp);//.append("℃");
////                        canvas.drawText(mTempBuffer.toString(), tstart + tempSpace, ibottom,
////                                pLowText);
//                    }
//                    
//                    if (mTempBuffer.length() > 0) {
//                        mTempBuffer.append("℃");
//                        float lstart = tstart - getTextWidth(pHighText, mTempBuffer.toString());
//                        canvas.drawText(mTempBuffer.toString(), lstart - tempSpace, ibottom,
//                                pHighText);		
//					}
//                }
//            }
//        }

        private void drawTrendLine(Canvas canvas) {

            if ((iHigh == null) || (iLow == null)) {
                // 没数据就不画
                return;
            }
            
            int iTotalHeight = mHeight;
            float iDrawHeight = iTotalHeight - curvetop - curvebottom;
            int iTotal = iHigh.length;

            float interzone = maxH - minH;
            int offset = nodeHeight.getWidth() / 2;

            float x;
            float Highy;
            float Lowy;
            float lastx = 0;
            float lastHighy = 0;
            float lastLowy = 0;

            // 画曲线
            for (int i = 0; i < iTotal; i++) {
                x = (float) ((mWidth / iTotal) * (i + 0.5));
                // 画最高温度
                if (iHigh[i] != Integer.MAX_VALUE) {
                    Highy = ((maxH - iHigh[i]) / interzone) * iDrawHeight;
                    Highy = Highy + curvetop;
                    // 前一点有才画(第一点不画)
                    if ((i > 0) && (iHigh[i - 1] != Integer.MAX_VALUE)) {
                        canvas.drawLine(lastx, lastHighy, x, Highy, pLineHeight);
                    }
                    lastHighy = Highy;
                }
                // 画最低温度
                if (iLow[i] != Integer.MAX_VALUE) {
                    Lowy = ((maxH - iLow[i]) / interzone) * iDrawHeight;
                    Lowy = Lowy + curvetop;
                    // 前一点有才画(第一点不画)
                    if ((i > 0) && (iLow[i - 1] != Integer.MAX_VALUE)) {
                        canvas.drawLine(lastx, lastLowy, x, Lowy, pLineLow);
                    }
                    lastLowy = Lowy;
                }
                lastx = x;
            }
            lastx = 0;
            lastHighy = 0;
            lastLowy = 0;
            // 画点
            for (int i = 0; i < iTotal; i++) {
                x = (float) ((mWidth / iTotal) * (i + 0.5));

                if (iHigh[i] != Integer.MAX_VALUE) {
                    Highy = ((maxH - iHigh[i]) / interzone) * iDrawHeight;
                    Highy = Highy + curvetop;
                    canvas.drawBitmap(nodeHeight, x - offset, Highy - offset, new Paint());
                    lastHighy = Highy;
                }

                if (iLow[i] != Integer.MAX_VALUE) {
                    Lowy = ((maxH - iLow[i]) / interzone) * iDrawHeight;
                    Lowy = Lowy + curvetop;
                    canvas.drawBitmap(nodeLow, x - offset, Lowy - offset, new Paint());
                    lastLowy = Lowy;
                }
                lastx = x;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        refreshView();
    }

}
