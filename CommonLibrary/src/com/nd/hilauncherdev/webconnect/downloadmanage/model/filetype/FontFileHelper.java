package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import java.io.File;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;

public class FontFileHelper implements IFileTypeHelper {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void onClickWhenFinished(Context ctx, ViewHolder viewHolder, BaseDownloadInfo downloadInfo) {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(ctx, "com.nd.hilauncherdev.myphone.myfont.activity.FontMainActivity"));
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//原本打算通过字体id判断，但通过Identification解析出来的id与字体解压后font.json中的id不一致
		intent.putExtra("font_name", downloadInfo.getTitle());
		ctx.startActivity(intent);
	}

	@Override
	public String getItemTextWhenFinished(BaseDownloadInfo downloadInfo) {
		Resources resources = CommonGlobal.getApplicationContext().getResources();
		if (isInUse(downloadInfo)) {
			return resources.getString(R.string.downloadmanager_inuse);
		}
		
		return CommonGlobal.getApplicationContext().getResources().getString(R.string.downloadmanager_preview);
	}
	
	private boolean isInUse(BaseDownloadInfo downloadInfo) {
		return false;
	}
	
	@Override
	public void onDownloadCompleted(Context ctx, BaseDownloadInfo downloadInfo, String file) {
		//判断是否在字体目录
		String downloadFontPath = CommonGlobal.OLD_BASE_DIR + "/Fonts/download";
		File srcFile = new File(file);
		if (!srcFile.getParentFile().getPath().equals(downloadFontPath)){//需要做迁移操作
			File destFile = new File(downloadFontPath + "/"+srcFile.getName());
			//拷贝文件
			if (!destFile.getParentFile().exists()){
				destFile.getParentFile().mkdirs();
			}
			FileUtil.copy(srcFile.getPath(), destFile.getPath());
			FileUtil.delFile(srcFile.getPath());
		}
	}

	@Override
	public String getItemDefIconPath(BaseDownloadInfo downloadInfo) {
		return "drawable:downloadmanager_font_icon";
	}

	@Override
	public boolean fileExists(BaseDownloadInfo downloadInfo) {
		if (downloadInfo != null) {
			if(downloadInfo.fileExists()){
				return true;
			}else{//下载迁移的时候显示存在
				String downloadFontPath = CommonGlobal.OLD_BASE_DIR + "/Fonts/download";
				File srcFile = new File(downloadInfo.getFilePath());
				File destFile = new File(downloadFontPath + "/"+srcFile.getName());
				if (destFile.exists()){
					return true;
				}
			}
		}
		return false;
	}
}
