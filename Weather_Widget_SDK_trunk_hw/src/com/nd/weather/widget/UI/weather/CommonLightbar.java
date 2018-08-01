package com.nd.weather.widget.UI.weather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 屏幕指示灯
 */
public class CommonLightbar extends LinearLayout {
	
	private static final String TAG = "CommonLightbar";

	private Drawable normal_lighter, selected_lighter;
	
	private int items, lastPos;
	
	private Context context;
	
	public CommonLightbar(Context context) {
		super(context);
		this.context = context;		
	}
	
	public CommonLightbar(Context context, AttributeSet attrs) {		
		super(context, attrs);
		this.context = context;
	}
	
	/**
	 * 刷新指示灯
	 * @param size 总页数
	 * @param current 当前页值
	 */
	public void refresh(int size, int current) {
		if (items == size) {
			return;
		}
		if (lastPos != -1) {
			ImageView imageView = ((ImageView)this.getChildAt(lastPos));
			if (imageView != null) {
				imageView.setImageDrawable(normal_lighter);
			}
		}
		if (items < size) {
			for (int i = items; i < size; i++) {
				ImageView iv = new ImageView(context);
				iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				iv.setImageDrawable(normal_lighter);
				this.addView(iv);
			}
		} else {
			this.removeViews(size, items - size); 
		}
		items = size;
		lastPos = -1;
		update(current);
		requestLayout();
	}
	
	/**
	 * 更新指示灯图片(基于0计算)
	 * @param pos 需更新的指示灯的页值
	 */
	public void update(int pos) {
		if (pos < 0 || pos >= items) {
			Log.e(TAG, "pos out of range!!!");
			return;
		}
		if (pos == lastPos)
			return;
		
		((ImageView)this.getChildAt(pos)).setImageDrawable(selected_lighter);
		if (lastPos != -1) {
			((ImageView)this.getChildAt(lastPos)).setImageDrawable(normal_lighter);
		}
		lastPos = pos;
	}

	/**
	 * 设置普通指示灯图片
	 * @param normal_lighter
	 */
	public void setNormalLighter(Drawable normal_lighter) {
		this.normal_lighter = normal_lighter;
	}

	/**
	 * 设置当前页指示灯图片
	 * @param selected_lighter
	 */
	public void setSelectedLighter(Drawable selected_lighter) {
		this.selected_lighter = selected_lighter;
	}
	/**
	 * 设置指示灯的间距
	 */
	protected int mGap = 0;
	public void setGap(int gap) {
		mGap = gap;
	}
}
