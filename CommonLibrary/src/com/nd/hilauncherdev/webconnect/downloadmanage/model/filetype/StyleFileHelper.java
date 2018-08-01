package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.TextUtils;
import android.widget.Toast;

import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.shop.api6.ThemeShopV8StyleAssit;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.theme.data.ThemeGlobal;
import com.nd.hilauncherdev.theme.data.ThemeType;
import com.nd.hilauncherdev.theme.db.LauncherThemeDataBase;
import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.state.StateHelper;

import java.util.HashMap;

/**
 * 风格操作
 * Created by linliangbin on 2016/12/17.
 */

public class StyleFileHelper implements IFileTypeHelper {

    private static final long serialVersionUID = 1L;

    public static final String additionThemeKey = "localThemeId";

    @Override
    public void onClickWhenFinished(Context context, DownloadAdapter.ViewHolder viewHolder, BaseDownloadInfo downloadInfo) {
        //检测是否安装成功
        HashMap<String, String> redIdMap = downloadInfo.getAdditionInfo();
        if(redIdMap==null){
            //正在安装
            // if( ThemeShopV6ThemeInstallAPI.installIngSet.contains(downloadInfo.getIdentification()) ){
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.hint_installing), Toast.LENGTH_SHORT).show();
            // }
            return;
        }

        String resId = redIdMap.get(additionThemeKey);
        if (redIdMap!=null && !StringUtil.isEmpty(resId)){
            String themeId = redIdMap.get(additionThemeKey);
            if ( ThemeManagerFactory.getInstance().isThemeIdLikeExist(context, themeId) ){
                //本地风格详情
                Intent intent = new Intent();
                intent.setClassName(context, "com.nd.hilauncherdev.shop.shop6.themestyle.ThemeShopV8LocalStyleDetailActivity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("themeId", themeId);
                SystemUtil.startActivitySafely(context, intent);
            } else {
     			StateHelper.redownload(context, viewHolder, downloadInfo);
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

        String resId = redIdMap.get(additionThemeKey);
        if (redIdMap != null && !StringUtil.isEmpty(resId)) {
            String themeId = redIdMap.get(additionThemeKey);
            if (ThemeManagerFactory.getInstance().isThemeIdLikeExist(context, themeId)) {
                return resources.getString(R.string.downloadmanager_preview);
            } else {
				return resources.getString(R.string.common_button_redownload);
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
        String themeId = redIdMap.get(additionThemeKey);
        if (StringUtil.isEmpty(themeId)) {
            return false;
        }
        
        String sql = "select * from Theme where id=?";
        LauncherThemeDataBase db = null;
        String sceneId = null;
        Cursor cursor = null;
        try {
        	db = LauncherThemeDataBase.getInstance(BaseConfig.getApplicationContext());
	        cursor = db.query(sql, new String[]{themeId});
	        if (cursor != null) {
	            boolean ret = cursor.moveToFirst();
	            if (ret) {
	                sceneId = cursor.getString(cursor.getColumnIndex("scene_id"));
	            }
	            cursor.deactivate();
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        	if(null != db) {
        		db.close();
        		db = null;
        	}
        } finally {
        	if(null != cursor) {
        		cursor.close();
	            cursor = null;
        	}
        	if(null != db) {
        		db.close();
        		db = null;
        	}
        }
        if(TextUtils.isEmpty(sceneId)) return false;
        return ThemeShopV8StyleAssit.isCurrentStyle(BaseConfig.getApplicationContext(), sceneId, themeId);
    }

    @Override
    public void onDownloadCompleted(Context context,
                                    BaseDownloadInfo downloadInfo, String file) {
        //下载完成安装
        ThemeShopV6ThemeInstallAPI.sendInstallStyle(context, file, (downloadInfo.getIdentification()), downloadInfo);
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

        String themeId = redIdMap.get(additionThemeKey);
        if (redIdMap!=null && !StringUtil.isEmpty(themeId)){
            return ThemeManagerFactory.getInstance().isThemeIdLikeExist(CommonGlobal.getApplicationContext(), themeId)
                    && !ThemeGlobal.isThemeResCleaned(ThemeType.DEFAULT, themeId, themeId.replace(" ", "_") + "/");
        }

        return false;
    }
}
