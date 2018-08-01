package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import java.io.File;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.kitset.util.DynamicPluginUtil;
import com.nd.hilauncherdev.kitset.util.MessageUtils;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.state.StateHelper;

public class DynamicApkFileHelper implements IFileTypeHelper {

	private static final String COM_BAIDU_LAUNCHER_PETFLOAT = "com.baidu.launcher.petfloat";
	private static final long serialVersionUID = 1L;
	public static final String PLUGIN_NAME = "plugin_name";
	public static final String EXTRAS_WIDGET_TYPE = "extras_widget_type";
	public static final String EXTRAS_WIDGET_POS_TYPE = "extras_widget_pos_type";
	public static final String WIDGET_TYPE = "widget_type";
	public static final String WIDGET_POS_TYPE = "widget_pos_type";
	public static final String DYNAMIC_WIDGET_ENABLE = "com.baidu.android.action.DYNAMIC_WIDGET_ENABLE";
	public static final String PETFLOAT_UPDATE = "com.baidu.launcher.petfloat.update";

	@Override
	public void onClickWhenFinished(Context ctx, ViewHolder viewHolder, BaseDownloadInfo downloadInfo) {
		String fileStr = downloadInfo.getSavedDir() + downloadInfo.getSavedName();
		File file = new File(fileStr);
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

		installPlugin(ctx, downloadInfo);
	}

	private void aleartErrorPackage(final Context context, final ViewHolder viewHolder, final File file, final BaseDownloadInfo downloadInfo) {
		ViewFactory.getAlertDialog(context, -1, context.getString(R.string.download_error_apk_title), context.getString(R.string.download_error_apk_content), context.getString(R.string.common_button_redownload), context.getString(R.string.download_error_apk_btn_install), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				StateHelper.redownload(context, viewHolder, downloadInfo);
			}
		}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				installPlugin(context, downloadInfo);
			}
		}).show();
	}

	private void installPlugin(Context ctx, BaseDownloadInfo downloadInfo) {
		String fileStr = downloadInfo.getSavedDir() + downloadInfo.getSavedName();
		File file = new File(fileStr);
		if (file.exists()) {
			Intent intent = new Intent(DYNAMIC_WIDGET_ENABLE);
			intent.putExtra(PLUGIN_NAME, downloadInfo.getSavedName());
			intent.putExtra(WIDGET_TYPE, downloadInfo.getAdditionInfo().get(EXTRAS_WIDGET_TYPE));
			intent.putExtra(WIDGET_POS_TYPE, downloadInfo.getAdditionInfo().get(EXTRAS_WIDGET_POS_TYPE));
			ctx.sendBroadcast(intent);
		} else {
			MessageUtils.makeShortToast(ctx, R.string.download_install_error);
		}
	}

	@Override
	public String getItemTextWhenFinished(BaseDownloadInfo downloadInfo) {
		return CommonGlobal.getApplicationContext().getResources().getString(R.string.common_button_use);
	}

	@Override
	public void onDownloadCompleted(Context ctx, BaseDownloadInfo downloadInfo, String file) {
		/**
		 * widget目录存在包名.jar文件,则判断为更新插件下载的情况,直接覆盖升级插件
		 */
		String pkg = downloadInfo.getIdentification();
		if (COM_BAIDU_LAUNCHER_PETFLOAT.equals(pkg)) {
			dealPetFloat(ctx);
		}
		String oldStr = CommonGlobal.WIDGET_PLUGIN_DIR + downloadInfo.getIdentification() + ".jar";

		File f = new File(oldStr);
		if (f.exists()) {
			installPlugin(ctx, downloadInfo);
		}

	}

	private void dealPetFloat(Context ctx) {
		if (DynamicPluginUtil.isPluginEnabled(ctx, CommonGlobal.PLUGIN_DIR, COM_BAIDU_LAUNCHER_PETFLOAT)) {
			Intent intent = new Intent(PETFLOAT_UPDATE);
			ctx.sendBroadcast(intent);
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
