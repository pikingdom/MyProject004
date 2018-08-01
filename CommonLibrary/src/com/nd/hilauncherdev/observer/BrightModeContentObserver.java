package com.nd.hilauncherdev.observer;

import android.database.ContentObserver;
import android.os.Handler;

public class BrightModeContentObserver extends ContentObserver {

	private Handler mHandler; // 此Handler用来更新UI线程

	public BrightModeContentObserver(Handler handler) {
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
		// 屏幕亮度模式改变
		mHandler.obtainMessage(ObserverConstant.BRIGHT_MODE_MSG).sendToTarget();
	}
}
