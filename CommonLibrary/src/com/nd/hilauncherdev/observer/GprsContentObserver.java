package com.nd.hilauncherdev.observer;

import android.database.ContentObserver;
import android.os.Handler;

public class GprsContentObserver extends ContentObserver {

	private Handler mHandler; // 此Handler用来更新UI线程

	public GprsContentObserver(Handler handler) {
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
		// gprs设置改变
		mHandler.obtainMessage(ObserverConstant.GPRS_MSG).sendToTarget();
	}
}
