package com.nd.hilauncherdev.observer;

import android.database.ContentObserver;
import android.os.Handler;

public class BrightContentObserver extends ContentObserver {

	private Handler mHandler; // 此Handler用来更新UI线程

	public BrightContentObserver(Handler handler) {
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
		// 屏幕亮度值改变
		mHandler.obtainMessage(ObserverConstant.BRIGHT_MSG).sendToTarget();
	}
}
