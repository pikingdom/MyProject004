package com.nd.hilauncherdev.framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 延迟加载ViewPager
 */
public abstract class LazyViewPager extends ViewPager {
	static final String TAG = "LazyViewPager";
	private boolean isFirstMeasure = true;
	private boolean isAutoLoadContent = true;
	/**
	 * 确保初始化加载仅执行一次
	 */
	private boolean [] lazyLoaded;
	
	public LazyViewPager(Context context) {
		super(context);
	}

	public LazyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LazyViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (isFirstMeasure) {
			final int N = this.getChildCount();
			lazyLoaded = new boolean[N];
			for (int i = 0; i < N; i++) {
				lazyLoaded[i] = false;
			}
			isFirstMeasure = false;
		}
		
		if (!isAutoLoadContent) {
			return ;
		}
		
		final int init = (getChildCount() - 1) / 2;
		if (!lazyLoaded[init]) {
			lazyLoaded[init] = loadContentData(init);
		}
	}

	/**
	 * 处理延迟加载
	 */
	@Override
	public void snapToScreen(int whichScreen) {
		super.snapToScreen(whichScreen);
		if (whichScreen < 0 || whichScreen >= lazyLoaded.length) {
			Log.e(TAG, "LazyViewPager ERROR!!!!!!!!");
			return;
		}
		if (!lazyLoaded[whichScreen]) {
			lazyLoaded[whichScreen] = loadContentData(whichScreen);
		}
	}

	/**
	 * 设置是否自动加载
	 * @param isAutoLoadContent 是否自动加载
	 */
	public void setAutoLoadContent(boolean isAutoLoadContent){
		this.isAutoLoadContent = isAutoLoadContent;
	}
	/**
	 * 是否自动加载
	 * @return boolean
	 */
	public boolean getAutoLoadContent(){
		return isAutoLoadContent;
	}
	
	/**
	 * 延迟加载指定屏幕
	 * @param screen
	 * @return boolean
	 */
	protected abstract boolean loadContentData(int screen);
	
	/**
	 * 刷新指定屏幕数据
	 * @param screen 屏幕
	 * @param tab 选中的tab
	 */
	public abstract void refreshView(int screen, String tab);
}
