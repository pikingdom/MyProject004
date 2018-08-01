package com.nd.hilauncherdev.framework.view.commonview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * 滑动视图基础类
 * Cfb
 * 
 */
public class CommonAppView extends FrameLayout {
	
	public static final String TAG = "CommonAppView";

	/**表示需要执行初始化*/
	protected boolean bInitFlag = true;
	
	public CommonAppView(Context paramContext) {
		super(paramContext);
	}

	public CommonAppView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public void addView(int paramInt) {
		LayoutInflater.from(getContext()).inflate(paramInt, this);
	}
	
	public void onNewIntent(Intent intent) {
		
	}
	
	public void onResume() {
		
	}
	
	public void onPause() {
		
	}
	
	public void onDestroy() {
		
	}
	
	public boolean getInitFlag(){
		return bInitFlag;
	}

	/**
	 * 1、返回back会调用次函数 2、再次点击选中的Tab时也会调用次函数,表示刷新View
	 * 
	 * 当点击的是当前在显示的Tab,执行刷新,具体操作在子类总完成 
	 * 
	 * 返回值: 
	 * 当前View没有任何需要更新时返回false
	 * 当前View有进行相关的操做则返回true
	 */
	public boolean flushView() {
		return false;
	}

	@Override
	protected void onAttachedToWindow() {
		
		super.onAttachedToWindow();
		
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.requestFocus();
		this.requestFocusFromTouch();
	};
}
