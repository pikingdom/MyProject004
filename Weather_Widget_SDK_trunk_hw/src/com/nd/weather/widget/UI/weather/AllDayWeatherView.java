package com.nd.weather.widget.UI.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.calendar.CommData.DayWeatherInfo;
import com.nd.calendar.util.ComfunHelp;
import com.nd.weather.widget.R;

public class AllDayWeatherView extends ViewFlipper implements OnGestureListener {
    // 四天天气回调
    public interface IOnDayWeatherListener {
        public void onDayWeatherFling();
        public void onShowTemperatureCurveDetail();
    }
    
    public final static int VIEW_TYPE_DAY = 0;
    public final static int VIEW_TYPE_TEMP = 1;
    private final int swipe_threshold_veloicty = 200;
    private final int swipe_min_distance = 80;

    private DayWeatherView mWeatherView;
    private TemperatureCurveView mTempCurveView;

    private GestureDetector m_gestureDetector;

    private Context mContext;
    
    private IOnDayWeatherListener mOnDayWeatherListener;
    
    private Animation m_AnimLeft_in = null;
    private Animation m_AnimLeft_out = null;
    private Animation m_AnimRight_in = null;
    private Animation m_AnimRight_out = null;

	private Path mPath;
	private int mPadding;
	
    public AllDayWeatherView(Context context, AttributeSet atts) {
        super(context, atts);
        mContext = context;
            
		mPath = new Path();
		if (Build.VERSION.SDK_INT > 11) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		
		mPadding = ComfunHelp.dip2px(55);
    }

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mPath.reset();
		mPath.moveTo(0, 0);
		mPath.lineTo(w, 0);
		// 去除右上角的三角 caizp 2015-1-28
		mPath.lineTo(w, h/* - mPadding*/);
		mPath.lineTo(w/* - mPadding*/, h);
		mPath.lineTo(0, h);
		mPath.lineTo(0, 0);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save(); 
		canvas.clipPath(mPath, Region.Op.REPLACE);
		canvas.drawColor(0x00ffffff);
		super.dispatchDraw(canvas);
		canvas.restore();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initView();
	}

	private void initView() {
        //mWeatherView = new DayWeatherView(mContext);
        mTempCurveView = new TemperatureCurveView(mContext);
        //setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
        //setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right));
//        addView(mWeatherView.getView());
        
        LayoutParams lp = new LayoutParams(MarginLayoutParams.FILL_PARENT, MarginLayoutParams.WRAP_CONTENT);
        mWeatherView = (DayWeatherView)View.inflate(getContext(), R.layout.weather_day_weather_view, null);
        addView(mWeatherView, lp);
        mWeatherView.setParentView(this);
//        mWeatherView.setVisibility(View.GONE);
        
    	int padding = ComfunHelp.dip2px(13);
    	lp = new LayoutParams(MarginLayoutParams.FILL_PARENT, MarginLayoutParams.FILL_PARENT);
    	lp.setMargins(padding, ComfunHelp.dip2px(8), padding, padding);
    	lp.gravity = Gravity.LEFT | Gravity.TOP;
    	
        addView(mTempCurveView, lp);
        
        m_gestureDetector = new GestureDetector(this);
//        mWeatherView.setOnClickListener(onViewClick);
        mTempCurveView.setOnClickListener(onViewClick);
    }

    public void setDayWeatherInfo(DayWeatherInfo dayWeatherInfo, int viewType) {
        if (dayWeatherInfo != null) {
            mWeatherView.setData(dayWeatherInfo);
            mTempCurveView.setData(dayWeatherInfo);
            switch (viewType) {
            case VIEW_TYPE_DAY:
                mWeatherView.refreshView();
                break;

            case VIEW_TYPE_TEMP:
                mTempCurveView.refreshView();
                break;
            default:
                break;
            }
        }
    }

    public void refreshYiJiData(String gmt, boolean bRefresh) {
        mWeatherView.refreshYiJiData(gmt, bRefresh);
    }

    public void setOnDayWeatherListener(IOnDayWeatherListener listener) {
        if (listener != null) {
            mOnDayWeatherListener = listener;
        }
    }

    public void switchView(int type) {
        // refreshView里面会有重复刷新判断
        switch (type) {
        case VIEW_TYPE_DAY:
            mWeatherView.refreshView();
            break;

        case VIEW_TYPE_TEMP:
            mTempCurveView.refreshView();
            break;
        default:
            break;
        }
        setDisplayedChild(type);
    }

    public void changeStyle() {
        if (mWeatherView != null) {
            mWeatherView.changeStyle();
        }
//        if (mTempCurveView != null) {
//            mTempCurveView.changeStyle();
//        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) {
            return false;
        }

        float fXs = e2.getX() - e1.getX();
        float fys = e2.getY() - e1.getY();

        // 左右划屏
        if (Math.abs(fys) <= Math.abs(fXs)) {

            if (Math.abs(velocityX) > swipe_threshold_veloicty) {
                if (fXs > swipe_min_distance) { // 从左至右
                    //Log.e("onFling", "showPrevious");
                    showPreviousView();
                    return true;

                } else if ((-fXs) > swipe_min_distance) {
                    //Log.e("onFling", "showNext");
                    showNextView();
                    return true;
                }
                
            }
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        m_gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!m_gestureDetector.onTouchEvent(ev)) {
            return super.dispatchTouchEvent(ev);
        } else {
            return true;
        }
    }

    private OnClickListener onViewClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mOnDayWeatherListener != null){
                mOnDayWeatherListener.onShowTemperatureCurveDetail();
            }
        }
    };
    
    private void showNextView(){
        if (m_AnimLeft_in == null) {
            m_AnimLeft_in = AnimationUtils.loadAnimation(mContext, R.anim.left_in);
        }
        if (m_AnimLeft_out == null) {
            m_AnimLeft_out = AnimationUtils.loadAnimation(mContext, R.anim.left_out);
        }
        setInAnimation(m_AnimLeft_in);
        setOutAnimation(m_AnimLeft_out);
        if (mOnDayWeatherListener != null){
            mOnDayWeatherListener.onDayWeatherFling();
        }
    }

    
    private void showPreviousView(){
        if (m_AnimRight_in == null) {
            m_AnimRight_in = AnimationUtils.loadAnimation(mContext, R.anim.right_in);
        }
        if (m_AnimRight_out == null) {
            m_AnimRight_out = AnimationUtils.loadAnimation(mContext, R.anim.right_out);
        }
        setInAnimation(m_AnimRight_in);
        setOutAnimation(m_AnimRight_out);
        if (mOnDayWeatherListener != null){
            mOnDayWeatherListener.onDayWeatherFling();
        }
    }
    
    public void clearBitmap(){
        try{
            mWeatherView.clearBitmap();
        }catch(Exception e){
            
        }
    }
}
