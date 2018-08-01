package com.nd.hilauncherdev.webconnect.downloadmanage.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.TextUtils;

import com.nd.hilauncherdev.analysis.ThemeDownloadPathAnalysis;
import com.nd.hilauncherdev.analysis.integral.IntegralSubmitUtil;
import com.nd.hilauncherdev.analysis.integral.IntegralTaskIdContent;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.framework.httplib.HttpCommon;
import com.nd.hilauncherdev.framework.httplib.HttpConstants;
import com.nd.hilauncherdev.kitset.AppDistributeUtil;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.MyPhoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadManagerActivity;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.FileType;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.IFileTypeHelper;
import com.nd.hilauncherdev.webconnect.downloadmanage.widget.DownloadNotification;

public class DownloadCallback extends AbstractDownloadCallback {

	@Override
	public void onBeginDownload(BaseDownloadInfo downloadInfo) {
		if (downloadInfo == null) {
			return;
		}
//		Log.d("hjiang", "dcb beging ---> "+downloadInfo.getTitle());

		if (!downloadInfo.getIsSilent() && downloadInfo.getFileType() != FileType.FILE_NONE.getId()) {
			if (downloadInfo.getFileType() == FileType.FILE_THEME_SERIES.getId()) {
				if (downloadInfo.getFinishIndex() == 0) {
					sendBeginNotice(downloadInfo);
				}
			} else {
				if (!downloadInfo.getIsNoNotification()) {
					sendBeginNotice(downloadInfo);
				}
			}
		}
		
		if (downloadInfo.getIsSilent() && !downloadInfo.is23GEnableTask()) {
			Context ctx = CommonGlobal.getApplicationContext();
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.WIFI_AUTO_DOWNLOAD_APK_END2, downloadInfo.getPacakgeName(ctx));
		}

		if(downloadInfo.getFileType() == BaseDownloadInfo.FILE_TYPE_APK){
			//开始下载打点
			Context ctx = CommonGlobal.getApplicationContext();
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.APP_DOWNLOAD_MANAGE_APP_DOWNLOAD, "ks");
		}
	}

	@Override
	public void onDownloadCompleted(final BaseDownloadInfo downloadInfo, boolean fileExist) {
		if (downloadInfo == null) {
			return;
		}
//		Log.i("hjiang", "dcb complete ---> "+downloadInfo.getTitle());
		
		if (!downloadInfo.getIsSilent() && downloadInfo.getFileType() != FileType.FILE_NONE.getId()) {
			if (downloadInfo.getFileType() == FileType.FILE_THEME_SERIES.getId()) {
				if (downloadInfo.getFinishIndex() == downloadInfo.getTotalSubDownloadListSize()) {
					sendSuccessNotice(downloadInfo);
				}
			} else {
				if (!downloadInfo.getIsNoNotification()) {
					sendSuccessNotice(downloadInfo);
				}
			}
			//添加主题下载成功统计
			if ( downloadInfo.getFileType() == FileType.FILE_THEME.getId() ||
					 downloadInfo.getFileType() == FileType.FILE_THEME_SERIES.getId()){
				HashMap<String, String> infoMap = downloadInfo.getAdditionInfo();
				String serThemeId = infoMap.get("serThemeId");
				String themeSp = infoMap.get("themeSp");
				String postionTypeString = infoMap.get("postionType");
				String postionTypeIdString = infoMap.get("postionTypeId");
				String resAttributesString = infoMap.get("ResAttributes");
				int resAttributes = ThemeDownloadPathAnalysis.RES_ATTRIBUTES_FREE;
				if(!TextUtils.isEmpty(resAttributesString)) {
					try {
						resAttributes = Integer.parseInt(resAttributesString);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				int postionType = ThemeDownloadPathAnalysis.POSTION_TYPE_DEFAULT;
				int postionTypeId = ThemeDownloadPathAnalysis.POSTION_TYPE_DEFAULT;
				if(postionTypeString != null && postionTypeIdString != null){
					try{
						postionType = Integer.parseInt(postionTypeString);
						postionTypeId = Integer.parseInt(postionTypeIdString);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if (serThemeId != null && themeSp != null){
					if (downloadInfo.getFileType() == FileType.FILE_THEME_SERIES.getId()){
						String themeServerId = "";
						final List<BaseDownloadInfo> subDownloadInfos = downloadInfo.getInnerSubBaseDownloadInfoS();
						if(null != subDownloadInfos && downloadInfo.getFinishIndex() > 0) {
							BaseDownloadInfo subDownloadInfo = subDownloadInfos.get(downloadInfo.getFinishIndex()-1);
							if(null != subDownloadInfo) {
								themeServerId = subDownloadInfo.getIdentification();
							}
						}
						if(downloadInfo.getFinishIndex() == downloadInfo.getTotalSubDownloadListSize()) {
							//上报主题系列下载成功
							ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(serThemeId, 36, themeSp,ThemeDownloadPathAnalysis.FLAG_DOWNLOAD_SUCCESS,postionType,postionTypeId,resAttributes);
							//上报主题系列中最后一个主题下载成功
							if(!TextUtils.isEmpty(themeServerId)) {
								ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(themeServerId, String.valueOf(ThemeDownloadPathAnalysis.SP_THEME_SERIES),ThemeDownloadPathAnalysis.FLAG_DOWNLOAD_SUCCESS,postionType,postionTypeId,resAttributes);
							}
						} else {
							//上报主题系列中的主题下载成功
							if(!TextUtils.isEmpty(themeServerId)) {
								ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(themeServerId, String.valueOf(ThemeDownloadPathAnalysis.SP_THEME_SERIES),ThemeDownloadPathAnalysis.FLAG_DOWNLOAD_SUCCESS,postionType,postionTypeId,resAttributes);
							}
						}
					} else {
						ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(serThemeId, themeSp,ThemeDownloadPathAnalysis.FLAG_DOWNLOAD_SUCCESS,postionType,postionTypeId,resAttributes);
					}
				}
			}
		}
		
		Context ctx = CommonGlobal.getApplicationContext();
		if(downloadInfo.getFileType() == BaseDownloadInfo.FILE_TYPE_APK){
			//下载完成打点
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.APP_DOWNLOAD_MANAGE_APP_DOWNLOAD, "cg");
		}
		IFileTypeHelper helper = FileType.fromId(downloadInfo.getFileType()).getHelper();
		if (null != helper) {
			helper.onDownloadCompleted(CommonGlobal.getApplicationContext(), downloadInfo, downloadInfo.getFilePath());
		}
		
		if (!fileExist) {
			int sp = downloadInfo.getDisSp();
			if (sp >= 0) {
				AppDistributeUtil.logAppDisDownloadSucc(ctx, downloadInfo.getDisId(), sp);
			}
		}
		
		if (downloadInfo.feedbackUrl != null && downloadInfo.feedbackUrl.startsWith("http")) {
			ThreadUtil.executeMore(new Runnable() {
				@Override
				public void run() {
					new HttpCommon(downloadInfo.feedbackUrl).httpFeedback();
				}
			});
		}
		
		//提交积分
		switch (downloadInfo.getFileType()) {
		case 1://铃声
		case 2://字体
			IntegralSubmitUtil.getInstance(ctx).submit(ctx, IntegralTaskIdContent.THEME_SHOP_RING_FONT_DOWNLOAD_SUCCESS);
			break;
		case 3://壁纸
			IntegralSubmitUtil.getInstance(ctx).submit(ctx, IntegralTaskIdContent.THEME_SHOP_WALLPAPER_DOWNLOAD_SUCCESS);
			break;
		case 6://主题
		case 15://主题系列
			IntegralSubmitUtil.getInstance(ctx).submit(ctx, IntegralTaskIdContent.THEME_SHOP_THEME_DOWNLOAD_SUCCESS);
			break;
		case 7://锁屏
		case 8://图标
		case 9://输入法
		case 10://通讯录
		case 11://天气
			IntegralSubmitUtil.getInstance(ctx).submit(ctx, IntegralTaskIdContent.THEME_SHOP_LOCK_ICON_WEATHER_ADDRESSLIST_INPUTMETHOD_DOWNLOAD_SUCCESS);
			break;
		}
	}

	@Override
	public void onDownloadFailed(BaseDownloadInfo downloadInfo, Exception e) {
		if (downloadInfo == null) {
			return;
		}
		
		HiAnalytics.submitEvent(CommonGlobal.getApplicationContext(), AnalyticsConstant.DOWNLOAD_MANAGER_DOWNLOAD_STATE, "zd");
		if (!downloadInfo.getIsSilent() && downloadInfo.getFileType() != FileType.FILE_NONE.getId()) {
			if (!downloadInfo.getIsNoNotification()) {
				sendFailNotice(downloadInfo);
			}
			//添加主题/主题系列下载失败统计
			if (downloadInfo.getFileType() == FileType.FILE_THEME.getId() || 
					downloadInfo.getFileType() == FileType.FILE_THEME_SERIES.getId()) {
				HashMap<String, String> infoMap = downloadInfo.getAdditionInfo();
				String serThemeId = infoMap.get("serThemeId");
				String themeSp = infoMap.get("themeSp");
				String postionTypeString = infoMap.get("postionType");
				String postionTypeIdString = infoMap.get("postionTypeId");
				String resAttributesString = infoMap.get("ResAttributes");
				int resAttributes = ThemeDownloadPathAnalysis.RES_ATTRIBUTES_FREE;
				if(!TextUtils.isEmpty(resAttributesString)) {
					try {
						resAttributes = Integer.parseInt(resAttributesString);
					}catch(Exception e1){
						e1.printStackTrace();
					}
				}
				int postionType = ThemeDownloadPathAnalysis.POSTION_TYPE_DEFAULT;
				int postionTypeId = ThemeDownloadPathAnalysis.POSTION_TYPE_DEFAULT;
				if(postionTypeString != null && postionTypeIdString != null){
					try{
						postionType = Integer.parseInt(postionTypeString);
						postionTypeId = Integer.parseInt(postionTypeIdString);
					}catch(Exception e1){
						e1.printStackTrace();
					}
				}
                StringBuffer networkState = getNetInfo(CommonGlobal.getApplicationContext(),e);

                if (serThemeId != null && themeSp != null){
                	if(downloadInfo.getFileType() == FileType.FILE_THEME_SERIES.getId()) {
                		ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(serThemeId, 36, themeSp,downloadInfo.getDownloadUrl(),networkState.toString(),postionType,postionTypeId,resAttributes);
                	} else {
                		ThemeDownloadPathAnalysis.sendThemeDownloadPathAnalysisSP(serThemeId, themeSp,downloadInfo.getDownloadUrl(),networkState.toString(),postionType,postionTypeId,resAttributes);
                	}
				}
			}
		}
		
		Context ctx = CommonGlobal.getApplicationContext();
		
		int sp = downloadInfo.getDisSp();
		if (sp >= 0) {
            StringBuffer networkState = getNetInfo(ctx,e);
			AppDistributeUtil.logAppDisDownloadFail(ctx, downloadInfo.getDisId(), sp, downloadInfo.getDownloadUrl(),networkState.toString());
		}

		if(downloadInfo.getFileType() == BaseDownloadInfo.FILE_TYPE_APK){
			//下载失败打点
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.APP_DOWNLOAD_MANAGE_APP_DOWNLOAD, "sb");
		}
	}

    private StringBuffer getNetInfo(Context context,Exception e) {
        //获取当前网络状态
        StringBuffer networkState = new StringBuffer();
        networkState.append(MyPhoneUtil.getTelephoneConcreteNetworkStateString(CommonGlobal.getBaseLauncher()));
//        //TODO 拼接进异常信息
//        try{
//            Writer writer = new StringWriter();
//            PrintWriter printWriter = new PrintWriter(writer);
//            e.printStackTrace(printWriter);
//            Throwable cause = e.getCause();
//            while (cause != null) {
//                cause.printStackTrace(printWriter);
//                cause = cause.getCause();
//            }
//            printWriter.close();
//            networkState.append("@");
//            networkState.append(writer.toString());
//            Map<String, String> infos = collectDeviceInfo(context);
//    		for (Map.Entry<String, String> entry : infos.entrySet()) {
//    			String key = entry.getKey();
//    			String value = entry.getValue();
//    			networkState.append(key + "=" + value + "\n");
//    		}
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
        return networkState;
    }

    /**
	 * 收集设备参数信息
	 */
	public Map<String, String> collectDeviceInfo(Context ctx) {
		Map<String, String> infos = new HashMap<String, String>();
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return infos;
	}
    
    @Override
	public void onDownloadWorking(BaseDownloadInfo downloadInfo) {
		if (downloadInfo == null) {
			return;
		}
		
		if (!downloadInfo.getIsSilent() && downloadInfo.getFileType() != FileType.FILE_NONE.getId()) {
			if (!downloadInfo.getIsNoNotification()) {
				//sendNotice(downloadInfo);
			}
		}
	}

	@Override
	public void onHttpReqeust(BaseDownloadInfo downloadInfo, int requestType) {
		if (downloadInfo == null) {
			return;
		}
//		Log.e("hjiang", "dcb http ---> "+downloadInfo.getTitle());
		
		Context ctx = CommonGlobal.getApplicationContext();
		
		if (!downloadInfo.getIsSilent() 
			&& downloadInfo.getFileType() != FileType.FILE_NONE.getId()
			&& requestType == HttpConstants.HTTP_REQUEST_CANCLE) {
			if (!downloadInfo.getIsNoNotification()) {
				DownloadNotification.downloadCancelledNotification(ctx, Math.abs(downloadInfo.getDownloadUrl().hashCode()));
			}
		}
	}
	
	private Intent createIntent(BaseDownloadInfo downloadInfo) {
		Intent intent = new Intent(DownloadManager.ACTION_SHOW);
		if (downloadInfo != null) {
			intent.putExtra(DownloadServerService.EXTRA_SHOW_TYPE, downloadInfo.getFileType());
			intent.putExtra(DownloadManagerActivity.EXTRA_FROM, DownloadManagerActivity.FROM_TZL);
		}
		
		return intent;
	}
	
/*	private void sendNotice(BaseDownloadInfo downloadInfo) {
//		Context context = CommonGlobal.getApplicationContext();
//		String str = downloadInfo.getTitle() + context.getResources().getString(R.string.common_downloading) ;
//		PendingIntent PIntent = PendingIntent.getActivity(context, 0, createIntent(downloadInfo), PendingIntent.FLAG_UPDATE_CURRENT);
//		int noticePosition = Math.abs(downloadInfo.getDownloadUrl().hashCode());
//		DownloadNotification.downloadRunningNotificationWithProgress(context, noticePosition, str, null, PIntent, downloadInfo.progress);
	}
	*/
	private void sendBeginNotice(BaseDownloadInfo downloadInfo) {
		Context context = CommonGlobal.getApplicationContext();
		String str = downloadInfo.getTitle();
		PendingIntent PIntent = PendingIntent.getActivity(context, 0, createIntent(downloadInfo), PendingIntent.FLAG_UPDATE_CURRENT);
		int noticePosition = Math.abs(downloadInfo.getDownloadUrl().hashCode());
		DownloadNotification.downloadBeganNotification(context, noticePosition, str, null, PIntent, downloadInfo.progress);
	}
	
	private void sendSuccessNotice(BaseDownloadInfo downloadInfo) {
		Context context = CommonGlobal.getApplicationContext();
		String str = downloadInfo.getTitle();
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, createIntent(downloadInfo), PendingIntent.FLAG_UPDATE_CURRENT);
		int noticePosition = Math.abs(downloadInfo.getDownloadUrl().hashCode());
		DownloadNotification.downloadCompletedNotification(context, noticePosition, str, null, pIntent);
	}
	
	private void sendFailNotice(BaseDownloadInfo downloadInfo) {
		Context context = CommonGlobal.getApplicationContext();
		String str = downloadInfo.getTitle();
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, createIntent(downloadInfo), 0);
		int noticePosition = Math.abs(downloadInfo.getDownloadUrl().hashCode());
		DownloadNotification.downloadFailedNotification(context, noticePosition, str, pIntent);
	}

}
