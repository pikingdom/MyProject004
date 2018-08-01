package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.Toast;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.theme.data.ThemeGlobal;
import com.nd.hilauncherdev.theme.data.ThemeType;
import com.nd.hilauncherdev.theme.pref.ThemeSharePref;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;

/**
 * 主题操作
 * @author cfb
 *
 */
public class ThemeFileHelper implements IFileTypeHelper{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String AdditionKey = "RESID";
	
	@Override
	public void onClickWhenFinished(Context context, ViewHolder viewHolder, BaseDownloadInfo downloadInfo) {
		//检测是否安装成功
		HashMap<String, String> redIdMap = downloadInfo.getAdditionInfo();
		if(redIdMap==null){
			//正在安装
			// if( ThemeShopV6ThemeInstallAPI.installIngSet.contains(downloadInfo.getIdentification()) ){
			Toast.makeText(context.getApplicationContext(), context.getString(R.string.hint_installing), Toast.LENGTH_SHORT).show();
			// }
			return;
		}
		
		String resId = redIdMap.get(AdditionKey);
		if (redIdMap!=null && !StringUtil.isEmpty(resId)){
			String themeId = redIdMap.get(AdditionKey);
			if ( ThemeManagerFactory.getInstance().isThemeIdLikeExist(context, themeId) ){
				//本地主题详情
				Intent intent = new Intent();
				intent.setClassName(context, "com.nd.hilauncherdev.theme.localtheme.LocalThemeDetailActivity");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.putExtra("themeId", themeId);
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
		if (isInUse(downloadInfo)) {
			return resources.getString(R.string.downloadmanager_inuse);
		}

		Context context = CommonGlobal.getApplicationContext();
		HashMap<String, String> redIdMap = downloadInfo.getAdditionInfo();
		if (redIdMap == null) {
			return resources.getString(R.string.downloadmanager_unzip);
		}

		String resId = redIdMap.get(AdditionKey);
		if (redIdMap != null && !StringUtil.isEmpty(resId)) {
			String themeId = redIdMap.get(AdditionKey);
			if (ThemeManagerFactory.getInstance().isThemeIdLikeExist(context, themeId)) {
				return resources.getString(R.string.downloadmanager_preview);
			}
		}

		return resources.getString(R.string.downloadmanager_unzip);
	}

	private boolean isInUse(BaseDownloadInfo downloadInfo) {
		if (downloadInfo == null) {
			return false;
		}
		HashMap<String, String> redIdMap = downloadInfo.getAdditionInfo();
		if (redIdMap == null) {
			return false;
		}
		String themeId = redIdMap.get(ThemeFileHelper.AdditionKey);
		if (StringUtil.isEmpty(themeId)) {
			return false;
		}
		
		return ThemeSharePref.getInstance(CommonGlobal.getApplicationContext()).getCurrentThemeId().equals(themeId);
	}
	
	@Override
	public void onDownloadCompleted(Context context,
			BaseDownloadInfo downloadInfo, String file) {
		//下载完成安装
		ThemeShopV6ThemeInstallAPI.sendInstallAPT(context, file, (downloadInfo.getIdentification()+"").replaceAll("theme", ""), downloadInfo);
	}

	@Override
	public String getItemDefIconPath(BaseDownloadInfo downloadInfo) {
		return "drawable:downloadmanager_theme_icon";
	}

	@Override
	public boolean fileExists(BaseDownloadInfo downloadInfo) {
		if (downloadInfo != null && downloadInfo.fileExists()) {
			return true;
		}
		
		HashMap<String, String> redIdMap = downloadInfo.getAdditionInfo();
		if (redIdMap==null){
			return false;
		}
		
		String themeId = redIdMap.get(AdditionKey);
		if (redIdMap!=null && !StringUtil.isEmpty(themeId)){
			return ThemeManagerFactory.getInstance().isThemeIdLikeExist(CommonGlobal.getApplicationContext(), themeId)
					&& !ThemeGlobal.isThemeResCleaned(ThemeType.DEFAULT, themeId, themeId.replace(" ", "_") + "/");
		}
		
		return false;
	}
}
