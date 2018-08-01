package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import java.io.File;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.HashMap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.dynamic.util.LoaderUtil;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.rsa.RSASignature;
import com.nd.hilauncherdev.kitset.rsa.RSAUtils;
import com.nd.hilauncherdev.kitset.rsa.SHA1Utils;
import com.nd.hilauncherdev.kitset.util.ApkTools;
import com.nd.hilauncherdev.kitset.util.MessageUtils;
import com.nd.hilauncherdev.launcher.config.preference.BaseConfigPreferences;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.state.StateHelper;
import com.nd.hilauncherdev.webconnect.downloadmanage.widget.DownloadNotification;

public class ApkFileHelper implements IFileTypeHelper {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void onClickWhenFinished(Context ctx, ViewHolder viewHolder, BaseDownloadInfo downloadInfo) {
		final String fileStr = downloadInfo.getSavedDir() + downloadInfo.getSavedName();
		final File file = new File(fileStr);
		if (file.exists()) {
			try {
				PackageManager pm = ctx.getPackageManager();
				PackageInfo info = pm.getPackageArchiveInfo(fileStr, 0);
				if (info == null || TextUtils.isEmpty(info.packageName)) {
					aleartErrorPackage(ctx, viewHolder, file, downloadInfo);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ApkTools.installApplication(ctx, file);
			if(downloadInfo.getFileType() == BaseDownloadInfo.FILE_TYPE_APK){
				//安装打点
				HiAnalytics.submitEvent(ctx, AnalyticsConstant.APP_DOWNLOAD_MANAGE_APP_DOWNLOAD, "az");
			}
		} else {
			MessageUtils.makeShortToast(ctx, R.string.download_install_error);
		}
	}

	private void aleartErrorPackage(final Context context, 
			                            final ViewHolder viewHolder, 
			                            final File file,
			                            final BaseDownloadInfo downloadInfo) {
		ViewFactory.getAlertDialog(context, -1, context.getString(R.string.download_error_apk_title), 
								  context.getString(R.string.download_error_apk_content), 
						          context.getString(R.string.common_button_redownload), 
						          context.getString(R.string.download_error_apk_btn_install),
		   new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				StateHelper.redownload(context, viewHolder, downloadInfo);
			}
		}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				ApkTools.installApplication(context, file);
			}
		}).show();
	}
	
	@Override
	public String getItemTextWhenFinished(BaseDownloadInfo downloadInfo) {
		return CommonGlobal.getApplicationContext().getResources().getString(R.string.common_button_install);
	}
	
	@Override
	public void onDownloadCompleted(Context ctx, BaseDownloadInfo downloadInfo, String file) {
		String pkgName = null;
		if (downloadInfo != null) {
			try {
				pkgName = downloadInfo.getPacakgeName(ctx);
				int verCode = downloadInfo.getVersionCode(ctx);
				if (!TextUtils.isEmpty(pkgName)) {
					downloadInfo.putAdditionInfo(BaseDownloadInfo.KEY_PKG_NAME, pkgName);
					if (verCode > 0) {
						downloadInfo.putAdditionInfo(BaseDownloadInfo.KEY_PKG_VER_CODE, String.valueOf(verCode));
					}
					
					DownloadManager.getInstance().modifyAdditionInfo(downloadInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (TextUtils.isEmpty(pkgName)) {
			return;
		}
		
		if (downloadInfo != null && downloadInfo.getIsSilent()) {
			return;
		}
		
		if (ApkTools.checkApkIfValidity(ctx, file) && LoaderUtil.readClientApkInfo(file)==null) {//动态插件不安装
			// 与服务端下发的RSA加密串对比，验证桌面自升级包的合法性 caizp 2016-1-14
			HashMap<String, String> addition = downloadInfo.getAdditionInfo();
			if(ctx.getPackageName().equals(pkgName) && null != addition && "1".equals(addition.get("pandahome_upgrade"))) {
				String rsaSignData = BaseConfigPreferences.getInstance().getPandaHomeVersionSignData();
				if(!TextUtils.isEmpty(rsaSignData)) {
					try {
						BaseConfigPreferences.getInstance().removePandaHomeVersionSignData();
						String apkSHA1 = SHA1Utils.getFileSha1(file);
						// 从文件中得到公钥
						InputStream inPublic = ctx.getResources().getAssets().open("rsa_public_key.pem");
						PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
						boolean result = RSASignature.doCheck(apkSHA1, rsaSignData, publicKey);
						if(!result) {//签名验证失败
							// 清除通知栏下载消息
							DownloadNotification.downloadCancelledNotification(ctx, Math.abs(downloadInfo.getDownloadUrl().hashCode()));
							
							Intent rsaCheckFailIntent = new Intent("com.nd.android.pandahome2.upgrade.rsa.check.fail");
							rsaCheckFailIntent.putExtra("identifier", downloadInfo.getIdentification());
							ctx.sendBroadcast(rsaCheckFailIntent);
							HiAnalytics.submitEvent(ctx, AnalyticsConstant.PANDAHOME_VERSION_UPGRADE_RSA_CHECK_FAIL);
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			ApkTools.installApplication(ctx, new File(file));
			if(downloadInfo.getFileType() == BaseDownloadInfo.FILE_TYPE_APK){
				//安装打点
				HiAnalytics.submitEvent(ctx, AnalyticsConstant.APP_DOWNLOAD_MANAGE_APP_DOWNLOAD, "az");
			}
			if (downloadInfo.isNeedStat()) {
				HiAnalytics.submitEvent(ctx, AnalyticsConstant.DOWNLOAD_APK_SUCCEED, downloadInfo.getTitle());
			}
		}
	}

	@Override
	public String getItemDefIconPath(BaseDownloadInfo downloadInfo) {
		return "drawable:downloadmanager_apk_icon";
	}

	@Override
	public boolean fileExists(BaseDownloadInfo downloadInfo) {
		if (downloadInfo != null) {
			return downloadInfo.fileExists();
		}
		return false;
	}
}
