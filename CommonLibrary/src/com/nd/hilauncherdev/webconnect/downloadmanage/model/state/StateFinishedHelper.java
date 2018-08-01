package com.nd.hilauncherdev.webconnect.downloadmanage.model.state;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.FileType;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.IFileTypeHelper;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;

/** 
 * 已下载但未安装状态
 * 
 * @author pdw 
 * @version 
 * @date 2012-9-19 下午04:33:24 
 */
public class StateFinishedHelper implements IDownloadStateHelper {
	
	private final int state=DownloadState.STATE_FINISHED;

	@Override
	public boolean action(Context ctx, 
			                ViewHolder viewHolder, 
			                BaseDownloadInfo downloadInfo) {	
		if (downloadInfo == null) {
			return false;
		}
		
		String btnText = viewHolder.funBtn.getText().toString();
		
		if (null != btnText && btnText.equals(ctx.getResources().getString(R.string.download_notify_finish))) {
			return false;
		}
		
		if (null != btnText && btnText.equals(ctx.getResources().getString(R.string.downloadmanager_inuse))) {
			return false;
		}
		
		if (null != btnText && btnText.equals(ctx.getResources().getString(R.string.common_button_redownload))) {
			StateHelper.redownload(ctx, viewHolder, downloadInfo);
		} else {
			FileType fileType = FileType.fromId(downloadInfo.getFileType());
			IFileTypeHelper helper = fileType.getHelper();
			if (null != helper) {
				helper.onClickWhenFinished(ctx, viewHolder, downloadInfo);
			}
		}
		
		return true;
	}
	
	@Override
	public void initView(ViewHolder viewHolder, BaseDownloadInfo downloadInfo) {
		boolean exists = false;
		FileType fileType = FileType.fromId(downloadInfo.getFileType());
		IFileTypeHelper helper = fileType.getHelper();
		if (helper != null) {
			exists = helper.fileExists(downloadInfo);
		} else {
			exists = downloadInfo.fileExists();
		}
		
		if (!exists) {
			viewHolder.desc.setText("");
			viewHolder.setFunButtonContent(R.string.common_button_redownload);
//			viewHolder.desc.setText(R.string.download_finished);
//			viewHolder.setFunButtonContent(R.string.download_notify_finish);
		} else {
			viewHolder.desc.setText(R.string.download_finished);
			if (helper != null) {
				String funText = helper.getItemTextWhenFinished(downloadInfo);
				Resources resources = viewHolder.funBtn.getResources();
				if (funText != null && funText.equals(resources.getString(R.string.downloadmanager_inuse))) {
					viewHolder.funBtn.setTextColor(0xffd3d3d3);
				}
				viewHolder.setFunButtonContent(helper.getItemTextWhenFinished(downloadInfo));
			} else {
				viewHolder.setFunButtonContent("");
			}
		}
		viewHolder.progress.setVisibility(View.GONE);
		viewHolder.state.setVisibility(View.INVISIBLE);
		
		/*if (ApkTools.hasInstallApk(ctx, apkDownloadInfo.getApkPath())) {
			viewHolder.funBtn.setText(R.string.widget_manage_panda_widget_installed);
		} else {
			viewHolder.funBtn.setText(R.string.common_button_install);
		}*/
	}

	@Override
	public int getState() {
		return state;
	}
	
}
