package com.nd.hilauncherdev.launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.app.AppDataFactory;
import com.nd.hilauncherdev.app.data.AppTable;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.recommend.p8custom.CustomDrawerFolderBean;
import com.nd.hilauncherdev.recommend.p8custom.CustomRecommendAppBean;
import com.nd.hilauncherdev.recommend.p8custom.CustomRecommendAppWrapper;
import com.nd.hilauncherdev.recommend.p8custom.CustomRecommendFolderBean;
import com.nd.hilauncherdev.settings.SettingsPreference;
import com.nd.hilauncherdev.theme.adaption.ThemeIconIntentAdaptation;

/**
 * 
 * 桌面国际化处理实现,系统设置中进行语言选项切换时:
 *  1.重新设置桌面应用项表的名称字段数据
 *  2.重新设置匣子应用项表的名称字段数据
 *  
 *  @author wangguomei
 *  
 */
public class LauncherInternational {
	
    public static final String TAG = "LauncherInternational";
	
	private Context context;

	private List<CustomRecommendAppBean> recommendAppList;
	private ArrayList<CustomRecommendFolderBean> mCustomRecommendFolderList = new ArrayList<CustomRecommendFolderBean>();
	private ArrayList<CustomDrawerFolderBean> mDrawerRecommendFolderList = new ArrayList<CustomDrawerFolderBean>();
	private String localLan;
	
	public LauncherInternational(Context context) {
		this.context = context;
		recommendAppList = CustomRecommendAppWrapper.getReplaceList(context);
		localLan = Locale.getDefault().getLanguage();
		initRecommendFolderList();
		initDrawerFolderList();
		Log.e(TAG,"lan:"+localLan);
	}

	private void initDrawerFolderList(){
		String strContent = SettingsPreference.getInstance().getDrawerRecommendFolders();
		if(TextUtils.isEmpty(strContent)){
			return;
		}
		String[] array = strContent.split(";");
		if(array == null){
			return;
		}
		for(int i=0; i<array.length; i++){
			String[] lanArray = array[i].split(",");
			if(lanArray == null || lanArray.length <=0){
				continue;
			} else {
				CustomDrawerFolderBean folderBean = new CustomDrawerFolderBean();
				for(int j=0; j<lanArray.length; j++){
					if(j == 0){
						folderBean.folderName = lanArray[j];
					} else if(j == 1){
						folderBean.folderNameDefault = lanArray[j];
					} else if(j == 2){
						folderBean.folderNameIn = lanArray[j];
					}else if(j == 3){
						folderBean.folderNameEn = lanArray[j];
					}else if(j == 4){
						folderBean.folderNameZh = lanArray[j];
					}
				}
				mDrawerRecommendFolderList.add(folderBean);
			}
		}
	}

	private void initRecommendFolderList(){
		String strContent = SettingsPreference.getInstance().getRecommendFolders();
		if(TextUtils.isEmpty(strContent)){
			return;
		}
		String[] array = strContent.split(";");
		if(array == null){
			return;
		}
		for(int i=0; i<array.length; i++){
			String[] lanArray = array[i].split(",");
			if(lanArray == null || lanArray.length <=0){
				continue;
			} else {
				CustomRecommendFolderBean folderBean = new CustomRecommendFolderBean();
				for(int j=0; j<lanArray.length; j++){
					if(j == 0){
						folderBean.folderName = lanArray[j];
					} else if(j == 1){
						folderBean.folderNameDefault = lanArray[j];
					} else if(j == 2){
						folderBean.folderNameIn = lanArray[j];
					}else if(j == 3){
						folderBean.folderNameEn = lanArray[j];
					}else if(j == 4){
						folderBean.folderNameZh = lanArray[j];
					}
				}
				mCustomRecommendFolderList.add(folderBean);
			}
		}
	}

	
	/**
	 * 
	 * 系统设置中进行语言选项切换时触发本地名称数据处理
	 * 
	 */
    public void localeChange() {
    	try {
//        	// 发广播给海外天气
//    		if (ChannelUtil.isGooglePlayVersion)
//    			context.sendBroadcast(new Intent("com.iweather.language_change"));
			favoretesFoldersHandle();
    	    favoritesHandle();
    	} catch (Throwable t) {
    		Log.e(TAG, "LauncherInternational.favoritesHandle err", t);
    	}

    	try {
			drawerFoldersHandle();
    	    drawerHandle();
    	} catch (Throwable t) {
    		Log.e(TAG, "LauncherInternational.drawerHandle err", t);
    	}
    	restartLauncher();
	}

	private void favoretesFoldersHandle(){
		List<FolderInfo> items = LauncherModel.loadFolderItems(context);
		if (items == null || items.isEmpty()) {
			return;
		}
		for (int i = 0; i < items.size(); i++) {
			FolderInfo app = items.get(i);
			CustomRecommendFolderBean folderBean = new CustomRecommendFolderBean();
			folderBean.folderName = app.title.toString();
			if(mCustomRecommendFolderList.contains(folderBean)){
				int index = mCustomRecommendFolderList.indexOf(folderBean);
				CustomRecommendFolderBean newFolderBean = mCustomRecommendFolderList.get(index);
				if("in".equals(localLan.toLowerCase()) && !TextUtils.isEmpty(newFolderBean.folderNameIn)){
					app.title = newFolderBean.folderNameIn;
				}else if("en".equals(localLan.toLowerCase()) && !TextUtils.isEmpty(newFolderBean.folderNameEn)){
					app.title = newFolderBean.folderNameEn;
				} else if("zh".equals(localLan.toLowerCase()) && !TextUtils.isEmpty(newFolderBean.folderNameZh)){
					app.title = newFolderBean.folderNameZh;
				}else {
					app.title = newFolderBean.folderNameDefault;
				}
			}
		}
		LauncherModel.batchUpdateFolderItemTitleById(context,items);
	}

	private void drawerFoldersHandle(){
		List<FolderInfo> folders = AppDataFactory.getInstance().loadDrawerFolderFromDBForLocale(context);
		if (folders == null || folders.isEmpty())
			return;

		Intent queryIntent = null;
		FolderInfo folder = null;
		String localeTitle = null;
		int count = folders.size();
		String[] resetNameSqls = new String[count];
		for (int i = 0; i < count; i++) {
			folder = folders.get(i);
			CustomDrawerFolderBean folderBean = new CustomDrawerFolderBean();
			folderBean.folderName = folder.title.toString();
			if(mDrawerRecommendFolderList.contains(folderBean)){
				int index = mDrawerRecommendFolderList.indexOf(folderBean);
				CustomDrawerFolderBean newFolderBean = mDrawerRecommendFolderList.get(index);
				if("in".equals(localLan.toLowerCase()) && !TextUtils.isEmpty(newFolderBean.folderNameIn)){
					folder.title = newFolderBean.folderNameIn;
				}else if("en".equals(localLan.toLowerCase()) && !TextUtils.isEmpty(newFolderBean.folderNameEn)){
					folder.title = newFolderBean.folderNameEn;
				} else if("zh".equals(localLan.toLowerCase()) && !TextUtils.isEmpty(newFolderBean.folderNameZh)){
					folder.title = newFolderBean.folderNameZh;
				}else {
					folder.title = newFolderBean.folderNameDefault;
				}
			}
//			queryIntent = new Intent();
//			queryIntent.setComponent(app.componentName);
//			localeTitle = AndroidPackageUtils.getIntentLabel(queryIntent,context.getPackageManager());
//			app.intent = queryIntent;
//			ComponentName cn = app.intent.getComponent();
//			String newName  = changeMaybe(cn);
//			if(!TextUtils.isEmpty(newName)){
//				localeTitle = newName;
//			}
			resetNameSqls[i] = String.format(AppTable.UPDATE_APP_TITLE_BY_ID,folder.title,folder.id);
		}

		AppDataFactory.getInstance().batchUpdateAppsTitleById(context,resetNameSqls);
	}

	/**
	 * 
	 * 桌面应用项表的名称处理
	 * 
	 */
	private void favoritesHandle() {

		Log.i(TAG, "favorites items name reset...");

		List<ApplicationInfo> items = LauncherModel.loadItemsByTypeForLocale(context);
		if (items == null || items.isEmpty()) {
			return;
		}

		String appLable = null;
		for (int i = 0; i < items.size(); i++) {
			ApplicationInfo app = items.get(i);
			if (app.intent == null) {
				continue;
			}
			switch (app.itemType) {
			   case LauncherSettings.Favorites.ITEM_TYPE_APPLICATION :
				   appLable = getAppLabelByIntent(app.intent);
				   break;
			   case LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT :
				    if (ThemeIconIntentAdaptation.isDefaultDockAppByUri(app.intent.toUri(0))) {
				    	appLable = ThemeIconIntentAdaptation.getDefaultDockTitleAppByUri(context,app.intent.toUri(0));
				    } else {
				    	appLable = getAppLabelByIntent(app.intent);
				    }
				    break;
			   case LauncherSettings.Favorites.ITEM_TYPE_INDEPENDENCE :
				    appLable = context.getString(R.string.dockbar_dock_drawer);
				    break;
			}
			ComponentName cn = app.intent.getComponent();
			String newName  = changeMaybe(cn);
			if(!TextUtils.isEmpty(newName)){
				app.title = newName;
			} else {
				if (!StringUtil.isEmpty(appLable)) {
					app.title = appLable;
				}
			}
		}
		LauncherModel.batchUpdateItemTitleById(context,items);
	}

	private String changeMaybe(ComponentName cn){
		if(cn == null || recommendAppList == null || recommendAppList.size() == 0){
			return "";
		}
		String result  = "";
		CustomRecommendAppBean temp = new CustomRecommendAppBean();
		temp.packageName = cn.getPackageName();
		temp.className  = cn.getClassName();
		if(recommendAppList.contains(temp)){
			int index = recommendAppList.indexOf(temp);
			CustomRecommendAppBean info = recommendAppList.get(index);
			return getAppNameCurrentLan(info);
		}
		return result;
	}

	private String getAppNameCurrentLan(CustomRecommendAppBean bean){
		if(bean == null){
			return "";
		}
//		Log.e(TAG,localLan.toLowerCase()+","+bean.appNameIn+","+bean.appNameEn+","+bean.appNameZh+","+bean.appNameDefault);
		if("in".equals(localLan.toLowerCase()) && !TextUtils.isEmpty(bean.appNameIn)){
			return bean.appNameIn;
		} else if("en".equals(localLan.toLowerCase()) && !TextUtils.isEmpty(bean.appNameEn)){
			return bean.appNameEn;
		} else if("zh".equals(localLan.toLowerCase()) && !TextUtils.isEmpty(bean.appNameZh)){
			return bean.appNameZh;
		}
		return bean.appNameDefault;
	}
	/**
	 * 
	 * 匣子应用项表的名称处理
	 * 
	 */
	private void drawerHandle() {
		
		Log.i(TAG, "drawer all app name reset...");
		
		List<ApplicationInfo> apps = AppDataFactory.getInstance().loadDrawerAppFromDBForLocale(context);
		if (apps == null || apps.isEmpty())
		   return;
		
		Intent queryIntent = null;
		ApplicationInfo app = null;
		String localeTitle = null;
		int count = apps.size();
		String[] resetNameSqls = new String[count];
		for (int index = 0; index < count; index++) {
			app = apps.get(index);
			queryIntent = new Intent();
			queryIntent.setComponent(app.componentName);
			localeTitle = AndroidPackageUtils.getIntentLabel(queryIntent,context.getPackageManager());
			app.intent = queryIntent;
			ComponentName cn = app.intent.getComponent();
			String newName  = changeMaybe(cn);
			if(!TextUtils.isEmpty(newName)){
				localeTitle = newName;
			}
			resetNameSqls[index] = String.format(AppTable.UPDATE_APP_TITLE_BY_ID,localeTitle,app.id);
		}
		
		AppDataFactory.getInstance().batchUpdateAppsTitleById(context,resetNameSqls);
	}
	
	
	/**
	 * 
	 * 重启桌面
	 * 
	 */
    private void restartLauncher() {
    	Log.i(TAG, "restart Launcher...");
		NotificationManager nManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
		nManager.cancelAll();
		//不用重启 桌面数据也会自动刷新
//		android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    
	private String getAppLabelByIntent(Intent intent) {
		 try {
			   return AndroidPackageUtils.getIntentLabel(intent,context.getPackageManager());
		 } catch (Exception e) {
			 Log.i(TAG, "getAppLabelByIntent err:" ,e); 
		 }
		 return null;
	}

}
