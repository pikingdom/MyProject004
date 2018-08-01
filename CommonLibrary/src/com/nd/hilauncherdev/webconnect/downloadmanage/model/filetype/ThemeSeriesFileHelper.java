package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.Toast;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;

/**
 * 天天系列下载文件处理
 * 
 * @author chenzhihong_9101910
 *
 */
public class ThemeSeriesFileHelper implements IFileTypeHelper {

	private static final long serialVersionUID = 1L;

	@Override
	public void onClickWhenFinished(Context context, ViewHolder viewHolder, BaseDownloadInfo downloadInfo) {
		//检测是否安装成功
		HashMap<String, String> redIdMap = downloadInfo.getAdditionInfo();
		if (redIdMap == null) {
			Toast.makeText(context.getApplicationContext(), context.getString(R.string.hint_installing), Toast.LENGTH_SHORT).show();
			return;
		}
		
		String seriesId = redIdMap.get("serThemeId");
		if (redIdMap!=null && !TextUtils.isEmpty(seriesId)){
			if ( ThemeManagerFactory.getInstance().isThemeIdLikeExist(context, seriesId) ){
				//本地主题系列详情
				Intent intent = new Intent();
				intent.setClassName(context, "com.nd.hilauncherdev.theme.localtheme.LocalThemeSeriesDetailActivity");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.putExtra("seriesId", seriesId);
				SystemUtil.startActivitySafely(context, intent);
			}
		} else {
			Toast.makeText(context.getApplicationContext(), context.getString(R.string.hint_installing), Toast.LENGTH_SHORT).show();
			return;
		}
	}

	@Override
	public String getItemTextWhenFinished(BaseDownloadInfo downloadInfo) {
		Resources resources = CommonGlobal.getApplicationContext().getResources();
		HashMap<String, String> redIdMap = downloadInfo.getAdditionInfo();
		if (redIdMap == null) {
			return resources.getString(R.string.downloadmanager_unzip);
		}

		String seriesId = redIdMap.get("serThemeId");
		if (redIdMap != null && !StringUtil.isEmpty(seriesId)) {
			if (ThemeManagerFactory.getInstance().isThemeIdLikeExist(CommonGlobal.getApplicationContext(), seriesId)) {
				return resources.getString(R.string.downloadmanager_preview);
			}
		}

		return resources.getString(R.string.downloadmanager_unzip);
	}

	@Override
	public void onDownloadCompleted(final Context context, final BaseDownloadInfo downloadInfo, String file) {
		final String seriesId = downloadInfo.getAdditionInfo().get("serThemeId");
		ThemeShopV6SeriesInstallAssit.installSeriesTheme(context, seriesId, downloadInfo);
	}

	@Override
	public String getItemDefIconPath(BaseDownloadInfo downloadInfo) {
		return "drawable:downloadmanager_theme_icon";
	}

	@Override
	public boolean fileExists(BaseDownloadInfo downloadInfo) {
		if (downloadInfo != null && downloadInfo.isFileListExists()) {
			return true;
		}
		
		HashMap<String, String> redIdMap = downloadInfo.getAdditionInfo();
		if (redIdMap==null){
			return false;
		}
		
		String seriesId = redIdMap.get("serThemeId");
		if (redIdMap!=null && !StringUtil.isEmpty(seriesId)){
			return ThemeManagerFactory.getInstance().isThemeIdLikeExist(CommonGlobal.getApplicationContext(), seriesId);
		}
		
		return false;
	}
}
