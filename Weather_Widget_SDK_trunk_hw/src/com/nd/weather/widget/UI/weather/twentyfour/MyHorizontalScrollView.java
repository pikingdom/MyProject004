package com.nd.weather.widget.UI.weather.twentyfour;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {

	public static boolean scrollState = false;
	private ViewGroup mParentPager;
	
	public MyHorizontalScrollView(Context context) {
		super(context);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (null != mParentPager) {
//			mParentPager.requestDisallowInterceptTouchEvent(true);
		}
		return super.dispatchTouchEvent(ev);
	}

	public void setParentPager(ViewGroup parentPager) {
		mParentPager = parentPager;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int height = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.measure(widthMeasureSpec,
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			int h = child.getMeasuredHeight();
			if (h > height)
				height = h;
		}

		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
				MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		scrollState = true;
		super.onScrollChanged(l, t, oldl, oldt);
	}


}