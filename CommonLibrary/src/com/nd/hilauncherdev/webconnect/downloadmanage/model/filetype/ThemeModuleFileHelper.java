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
import com.nd.hilauncherdev.theme.data.ThemeGlobal;
import com.nd.hilauncherdev.theme.module.ModuleConstant;
import com.nd.hilauncherdev.theme.module.ThemeModuleHelper;
import com.nd.hilauncherdev.theme.module.ThemeModuleItem;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;

public class ThemeModuleFileHelper implements IFileTypeHelper{
	
	private static final long serialVersionUID = 1L;

	@Override
	public void onClickWhenFinished(Context context, ViewHolder viewHolder, BaseDownloadInfo downloadInfo) {
		//检测是否安装成功
		HashMap<String, String> redIdMap = downloadInfo.getAdditionInfo();
		if(redIdMap==null){
			//正在安装
			if( ThemeShopV6ThemeInstallAPI.installIngSet.contains(downloadInfo.getIdentification()) ){
				Toast.makeText(context.getApplicationContext(), context.getString(R.string.hint_installing), Toast.LENGTH_SHORT).show();
			}
			return;
		}
		String resId = redIdMap.get(ThemeFileHelper.AdditionKey);
		if (redIdMap!=null && !StringUtil.isEmpty(resId)){
			String moduleId = redIdMap.get(ThemeFileHelper.AdditionKey);
			if ( ThemeModuleHelper.getInstance().isModuleInfoExist(null, FileTypeToModuleType(downloadInfo.getFileType()), moduleId) ){
				//本地主题详情
				Intent intent = new Intent();
				intent.setClassName(context, "com.nd.hilauncherdev.theme.localtheme.LocalModuleDetailActivity");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.putExtra("moduleId", moduleId);
				intent.putExtra("moduleKey", FileTypeToModuleType(downloadInfo.getFileType()));
				SystemUtil.startActivitySafely(context, intent);
			}
		}
	}

	@Override
	public String getItemTextWhenFinished(BaseDownloadInfo downloadInfo) {
		Resources resources = CommonGlobal.getApplicationContext().getResources();
		if (isInUse(downloadInfo)) {
			return resources.getString(R.string.downloadmanager_inuse);
		}
		
		return resources.getString(R.string.downloadmanager_preview);
	}

	private boolean isInUse(BaseDownloadInfo downloadInfo) {
		if (downloadInfo == null) {
			return false;
		}
		HashMap<String, String> redIdMap = downloadInfo.getAdditionInfo();
		if (redIdMap == null) {
			return false;
		}
		String moduleId = redIdMap.get(ThemeFileHelper.AdditionKey);
		if (StringUtil.isEmpty(moduleId)) {
			return false;
		}
		
		return ThemeModuleHelper.getInstance().isModuleUsedInCurTheme(moduleId, ThemeModuleItem.TYPE_MODULE);
	}
	
	@Override
	public void onDownloadCompleted(Context context,
			BaseDownloadInfo downloadInfo, String file) {
		ThemeShopV6ThemeInstallAPI.sendInstallModule(context, file, downloadInfo.getIdentification(), FileTypeToModuleType(downloadInfo.getFileType()), downloadInfo);
	}

	@Override
	public String getItemDefIconPath(BaseDownloadInfo downloadInfo) {
		String path = "drawable:downloadmanager_theme_icon";
		if (downloadInfo != null) {
			FileType fileType = FileType.fromId(downloadInfo.getFileType());
			switch (fileType) {
			
			case FILE_LOCK:
				path = "drawable:downloadmanager_lock_icon";
				break;
			case FILE_ICON:
				path = "drawable:downloadmanager_icon_icon";
				break;
			case FILE_INPUT:
				path = "drawable:downloadmanager_input_icon";
				break;
			case FILE_SMS:
				path = "drawable:downloadmanager_sms_icon";
				break;
			case FILE_WEATHER:
				path = "drawable:downloadmanager_weather_icon";
				break;
			default:
				break;
			}
		}
		return path;
	}

	public static String FileTypeToModuleType(int fileTypeId) {
		FileType fileType = FileType.fromId(fileTypeId);
		String moduleKey = "";
		switch (fileType) {
		case FILE_LOCK:
			moduleKey = ModuleConstant.MODULE_LOCKSCREEN;
			break;
		case FILE_ICON:
			moduleKey = ModuleConstant.MODULE_ICONS;
			break;
		case FILE_INPUT:
			moduleKey = ModuleConstant.MODULE_BAIDU_INPUT;
			break;
		case FILE_INPUT_IFLY:
			moduleKey = ModuleConstant.MODULE_IFLYTEK_INPUT;
			break;			
		case FILE_SMS:
			moduleKey = ModuleConstant.MODULE_SMS;
			break;
		case FILE_SMS_COOTEK:
			moduleKey = ModuleConstant.MODULE_COOTEK_DIALER;
			break;			
		case FILE_WEATHER:
			moduleKey = ModuleConstant.MODULE_WEATHER;
			break;			
		default:
			break;
		}
		return moduleKey;
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
		
		String moduleId = redIdMap.get(ThemeFileHelper.AdditionKey);
		if (redIdMap!=null && !StringUtil.isEmpty(moduleId)){
			return ThemeModuleHelper.getInstance().isModuleInfoExist(null, FileTypeToModuleType(downloadInfo.getFileType()), moduleId)
					&& !ThemeGlobal.isModuleResCleaned(moduleId, FileTypeToModuleType(downloadInfo.getFileType()));
		}
		
		return false;
	}
}
