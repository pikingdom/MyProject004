package com.nd.hilauncherdev.observer;

import android.database.ContentObserver;
import android.os.Handler;

public class ScreenOverTimeContentObserver extends ContentObserver {

	private Handler mHandler; // 此Handler用来更新UI线程

	public ScreenOverTimeContentObserver(Handler handler) {
		super(handler);
		mHandler = handler;
	}

	/**
	 * 当所监听的Uri发生改变时，就会回调此方法
	 * 
	 * @param selfChange
	 * 
	 */
	@Override
	public void onChange(boolean selfChange) {
		// 屏幕超时变化
		mHandler.obtainMessage(ObserverConstant.SCREEN_OVER_TIME_MSG).sendToTarget();
	}
}
