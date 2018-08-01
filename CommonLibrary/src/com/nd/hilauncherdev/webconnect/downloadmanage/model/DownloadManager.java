package com.nd.hilauncherdev.webconnect.downloadmanage.model;

import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.os.RemoteException;

import com.nd.android.pandahome2.BuildConfig;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.datamodel.db.MyPhoneDB;
import com.nd.hilauncherdev.framework.db.AbstractDataBase;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;

public class DownloadManager extends AbstractDownloadManager {

	/**
	 * apk应用下载广播通知
	 */
	public static final String ACTION_DOWNLOAD_STATE = BuildConfig.APP_PKG_NAME+"_APK_DOWNLOAD_STATE";
	/**
	 * 打开下载管理界面
	 */
	public static final String ACTION_SHOW = BuildConfig.APP_PKG_NAME+".downloadmanager.SHOW";

	private static DownloadManager sInstance = null;

	public static DownloadManager getInstance() {
		if (sInstance == null) {
			sInstance = new DownloadManager(CommonGlobal.getApplicationContext());
		}
		return sInstance;
	}

	private DownloadManager(Context context) {
		super(context);
	}

	@Override
	protected Class<? extends AbstractDataBase> getDownloadDb() {
		return MyPhoneDB.class;
	}

	@Override
	protected Class<? extends AbstractDownloadCallback> getDownloadCallback() {
		return DownloadCallback.class;
	}

	@Override
	protected String getBroadcastAction() {
		return ACTION_DOWNLOAD_STATE;
	}

	void initService(IDownloadManagerService service) {
		if (service == null) {
			return;
		}

		try {
			service.setDownloadCallback(getDownloadCallback().getName());
			service.setDownloadDb(getDownloadDb().getName());
			service.setBroadcastAction(getBroadcastAction());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 有未安装的内容和需继续下载的任务，要显示提醒
	 *
	 * @param downloadTaskS
	 * @return
	 */
	public static boolean isNeedShowRemind(Map<String, BaseDownloadInfo> downloadTaskS) {
		for (Iterator<String> it = downloadTaskS.keySet().iterator(); it.hasNext();) {
			BaseDownloadInfo downloadInfo = downloadTaskS.get(it.next());
			int state = downloadInfo.getState();
			if (state == DownloadState.STATE_PAUSE || state == DownloadState.STATE_WAITING || state == DownloadState.STATE_FAILED) {
				return true;
			}
		}

		return false;
	}

}
