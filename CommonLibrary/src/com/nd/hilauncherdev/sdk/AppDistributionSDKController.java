package com.nd.hilauncherdev.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.felink.appdis.AppDistributionManager;
import com.felink.appdis.AppDistributionManager.AppDistributionInfo;
import com.felink.appdis.AppDistributionManager.GetAppListCallBack;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.ChannelUtil;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadBroadcastExtra;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 文件夹底部推荐app控制类
 * Author: guojianyun_dian91 
 * Date: 2016年3月8日 下午4:54:56
 */
public class AppDistributionSDKController {
	
	private static AppDistributionSDKController instance = new AppDistributionSDKController();
	
	private static ConcurrentHashMap<String, AppDistributionInfo> clickedAppMap = new ConcurrentHashMap<String, AppDistributionInfo>();
	public static ConcurrentHashMap<String, AppDistributionInfo> startDownloadedAppMap = new ConcurrentHashMap<String, AppDistributionInfo>();
	public static ConcurrentHashMap<String, AppDistributionInfo> finishDownloadAppMap = new ConcurrentHashMap<String, AppDistributionInfo>();
	public static ConcurrentHashMap<String, Long> downloadCostTimeMap = new ConcurrentHashMap<String, Long>();
	private static BroadcastReceiver mDownloadReceiver;
	private static BroadcastReceiver mInstallReceiver;
	
	public static final String FOLDER_RECOMMEND_APP_POSITION = "1000";

	private static HashMap<String, ArrayList<AppDistributionInfo>> appCategroyMap = new HashMap<String, ArrayList<AppDistributionInfo>>();
	private static HashMap<String, Integer> appListPosMap = new HashMap<String, Integer>();
	
	
	public static AppDistributionSDKController getInstance(){
		return instance;
	}
	
	public void init(Context context) {
		AppDistributionManager.init(context, Integer.valueOf(CommonGlobal.PID), ChannelUtil.getChannel(context));

		if (mDownloadReceiver == null) {
			mDownloadReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String url = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_DOWNLOAD_URL);
					String id = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_IDENTIFICATION);
					int state = intent.getIntExtra(DownloadBroadcastExtra.EXTRA_STATE, DownloadState.STATE_NONE);
					if (state == DownloadState.STATE_START) {//统计开始下载
						for (String key : clickedAppMap.keySet()) {
							if ((id != null && id.contains(key)) || (url != null && url.contains(key))) {
								AppDistributionInfo info = clickedAppMap.get(key);
//								AppDistributionManager.submitStartDownloadEvent(context, info);
								startDownloadedAppMap.put(key, info);
								clickedAppMap.remove(key);
								downloadCostTimeMap.put(key, System.currentTimeMillis());
								//Log.e("submitStartDownloadEvent", info.pkgName);
							}
						}
						
					}else if (state == DownloadState.STATE_FINISHED) {//统计下载完成
						for (String key : startDownloadedAppMap.keySet()) {
							if ((id != null && id.contains(key)) || (url != null && url.contains(key))) {
								AppDistributionInfo info = startDownloadedAppMap.get(key);
								AppDistributionManager.submitFinishDownloadEvent(context, info,
										System.currentTimeMillis() - downloadCostTimeMap.get(key));
								finishDownloadAppMap.put(info.pkgName, info);
								startDownloadedAppMap.remove(key);
								downloadCostTimeMap.remove(key);
								//Log.e("submitFinishDownloadEvent", info.pkgName);
								break;
							}
						}
					}
				}
			};
			IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_STATE);
			context.registerReceiver(mDownloadReceiver, filter);
		}
		
		if (mInstallReceiver == null) {//统计app安装完成
			mInstallReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					final String packageName = intent.getData().getSchemeSpecificPart();
					if(finishDownloadAppMap.containsKey(packageName)){
						AppDistributionManager.submitFinishInstallEvent(context, finishDownloadAppMap.get(packageName));
						finishDownloadAppMap.remove(packageName);
						Log.e("submitActiveAppEvent", packageName);
					}
				}
			};
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.intent.action.PACKAGE_ADDED");
			filter.addDataScheme("package");
			context.registerReceiver(mInstallReceiver, filter);
		}
	}
	
	public static void addToAppMap(AppDistributionInfo info){
		clickedAppMap.put(info.pkgName, info);
	}


	public static void submitClickDownloadEvent(Context context, String key) {
		AppDistributionInfo info = clickedAppMap.get(key);
		AppDistributionManager.submitStartDownloadEvent(context, info);
	}

	public static void cleanCacheFromYYB(){
		AppDistributionManager.clean();
	}
	
	public static void getDistributionAppsFromYYB(final Context ctx, final int categoryId, final int pageSize, final GetAppListCallBack mCallBack) {
		String category = "综合";
		switch (categoryId) {
		case 1:
			category = "社交";
			break;
		case 2:
			category = "摄影";
			break;
		case 3:
			category = "影音";
			break;
		case 4:
			category = "游戏";
			break;
		case 5:
			category = "购物";
			break;
		case 6:
			category = "阅读";
			break;
		case 7:
			category = "办公";
			break;
		case 8:
			category = "出行";
			break;
		case 9:
			category = "学习";
			break;
		case 10:
			category = "生活";
			break;
		case 11:
			category = "理财";
			break;
		case 12:
			category = "健康";
			break;
		case 13:
			category = "美化";
			break;
		case 14:
			category = "工具";
			break;
		}
		final String categoryFinal = category;
		if(appCategroyMap.containsKey(categoryFinal)){
			ArrayList<AppDistributionInfo> appList = appCategroyMap.get(categoryFinal);
			int pos = appListPosMap.get(categoryFinal);
			ArrayList<AppDistributionInfo> resultList = null;
			if(pos < appList.size() - 1){
				resultList = getResultAppList(appList, categoryFinal);
			}
			if(mCallBack != null){
				mCallBack.onCallback(resultList);
			}
		}else {
			AppDistributionManager.getDistributionAppList(ctx, categoryFinal, new GetAppListCallBack(){
				public void onCallback(ArrayList<AppDistributionInfo> appList) {
					ArrayList<AppDistributionInfo> resultList = null;
					if(appList != null && appList.size() > 0){
						appCategroyMap.put(categoryFinal, appList);
						resultList = getResultAppList(appList, categoryFinal);
					}
					if(mCallBack != null){
						mCallBack.onCallback(resultList);
					}

				}
			});
		}
	}

	private static ArrayList<AppDistributionInfo> getResultAppList(ArrayList<AppDistributionInfo> appList, String categoryFinal) {
		ArrayList<AppDistributionInfo> appListResult = null;
		int startPos = 0;
		if(appListPosMap.containsKey(categoryFinal)){
			startPos = appListPosMap.get(categoryFinal);
		}
		int endPos = appList.size() - 1;
		if (appList.size() >= startPos + 8){
            appListResult = new ArrayList<AppDistributionInfo>(appList.subList(startPos, startPos + 8));
            endPos = startPos + 8;
        }else if (appList.size() >= startPos + 4 && appList.size() < startPos + 8){
            appListResult = new ArrayList<AppDistributionInfo>(appList.subList(startPos, startPos + 4));
            endPos = startPos + 4;
        }else if (appList.size() >= startPos && appList.size() < startPos + 4){
			appListResult = new ArrayList<AppDistributionInfo>(appList.subList(startPos, appList.size()));
			endPos = appList.size();
		}
		appListPosMap.put(categoryFinal, endPos);
		return appListResult;
	}

	public static void cleanV8FolderDisAppState(){
		appListPosMap.clear();
		appCategroyMap.clear();
	}
}
