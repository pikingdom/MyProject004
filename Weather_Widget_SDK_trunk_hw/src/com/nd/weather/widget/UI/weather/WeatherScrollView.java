package com.nd.weather.widget.UI.weather;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.nd.hilauncherdev.kitset.util.ScreenUtil;

public class WeatherScrollView extends ScrollView {

	private float sX;
	private float eX;
	
	private Context mContext;

	public WeatherScrollView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
	}

	public WeatherScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public WeatherScrollView(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			sX = ev.getRawX();
		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			eX = ev.getRawX();
			if (Math.abs(eX - sX) > ScreenUtil.dip2px(mContext, 10)) {
				return false;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	public interface WeatherScrollViewListener {  
	    void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy);  
	}  
	
	private WeatherScrollViewListener scrollViewListener = null;  
	  
    public void setScrollViewListener(WeatherScrollViewListener scrollViewListener) {  
        this.scrollViewListener = scrollViewListener;  
    }  
  
    @Override  
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {  
        super.onScrollChanged(x, y, oldx, oldy);  
        if (scrollViewListener != null) {  
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);  
        }  
    }  
	
}
