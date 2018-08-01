package com.nd.hilauncherdev.launcher;

import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.app.CustomIntent;
import com.nd.hilauncherdev.app.data.AppDataBase;
import com.nd.hilauncherdev.app.data.AppTable;
import com.nd.hilauncherdev.app.ui.view.icontype.CustomP8IconType;
import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.UpgradeFolderInfo;
import com.nd.hilauncherdev.drawer.view.DrawerMainView;
import com.nd.hilauncherdev.folder.model.FolderEncriptHelper;
import com.nd.hilauncherdev.framework.UIHandlerFactory;
import com.nd.hilauncherdev.framework.effect.EffectConfig;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.config.ConfigPreferences;
import com.nd.hilauncherdev.kitset.invoke.ThirdWidgetAccessor;
import com.nd.hilauncherdev.kitset.resolver.CenterControl;
import com.nd.hilauncherdev.kitset.util.AppCheckUtil;
import com.nd.hilauncherdev.kitset.util.BaseBitmapUtils;
import com.nd.hilauncherdev.kitset.util.HiLauncherEXUtil;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.launcher.LauncherSettings.Favorites;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.CellLayoutConfig;
import com.nd.hilauncherdev.launcher.config.ConfigFactory;
import com.nd.hilauncherdev.launcher.config.preference.BaseConfigPreferences;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.info.ItemInfo;
import com.nd.hilauncherdev.launcher.info.PandaWidgetInfo;
import com.nd.hilauncherdev.launcher.info.WidgetInfo;
import com.nd.hilauncherdev.launcher.model.BaseLauncherModel;
import com.nd.hilauncherdev.launcher.model.load.Callbacks;
import com.nd.hilauncherdev.launcher.model.load.DeferredHandler;
import com.nd.hilauncherdev.launcher.model.load.LauncherLoader;
import com.nd.hilauncherdev.launcher.model.load.LauncherLoaderHelper;
import com.nd.hilauncherdev.launcher.po.TagApplicationInfo;
import com.nd.hilauncherdev.launcher.po.TagViewController;
import com.nd.hilauncherdev.launcher.screens.CellLayout;
import com.nd.hilauncherdev.launcher.screens.ScreenViewGroup;
import com.nd.hilauncherdev.launcher.screens.dockbar.BaseMagicDockbar;
import com.nd.hilauncherdev.launcher.screens.dockbar.DockbarCellLayoutConfig;
import com.nd.hilauncherdev.launcher.support.BaseIconCache;
import com.nd.hilauncherdev.launcher.support.BaseLauncherViewHelper;
import com.nd.hilauncherdev.launcher.touch.DropTarget;
import com.nd.hilauncherdev.launcher.view.PandaWidgetViewContainer;
import com.nd.hilauncherdev.launcher.view.icon.icontype.IconType;
import com.nd.hilauncherdev.launcher.view.icon.icontype.impl.DockDefaultFourIconType;
import com.nd.hilauncherdev.launcher.view.icon.ui.LauncherIconView;
import com.nd.hilauncherdev.launcher.view.icon.ui.LauncherIconViewConfig;
import com.nd.hilauncherdev.launcher.view.icon.ui.folder.FolderIconTextView;
import com.nd.hilauncherdev.launcher.view.icon.ui.impl.IconMaskTextView;
import com.nd.hilauncherdev.myphone.data.MyPhoneDataFactory;
import com.nd.hilauncherdev.push.PushUtil;
import com.nd.hilauncherdev.scene.Scene;
import com.nd.hilauncherdev.scene.SceneCellLayout;
import com.nd.hilauncherdev.scene.SceneManager;
import com.nd.hilauncherdev.scene.data.SceneDatabase;
import com.nd.hilauncherdev.settings.SettingsPreference;
import com.nd.hilauncherdev.settings.assit.BackupSetting;
import com.nd.hilauncherdev.shop.api6.model.GameThemeRecommendAppBean;
import com.nd.hilauncherdev.theme.data.BasePandaTheme;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.util.Log.e;

public class LauncherLoaderHelperImpl extends LauncherLoaderHelper {

	private static LauncherLoaderHelperImpl instance;

	public static ArrayList<String> loadedPkgNames = new ArrayList<String>();
	
	private static HashMap<String, String> uninstallWhiteMap = new HashMap<String, String>();

	private static HashMap<String, Long> systemAppsMapping = new HashMap<String, Long>();

	/**
	 * 双开应用白名单列表
	 */
	public static HashMap<String, String> multiAppWhiteMap = new HashMap<String, String>();

	private LauncherLoaderHelperImpl() {
	};

	public static LauncherLoaderHelperImpl getInstance() {
		if (instance == null) {
			instance = new LauncherLoaderHelperImpl();
		}
		return instance;
	}

	/**
	 * Description: 设置dock栏应用是否显示 Author: guojy Date: 2013-8-19 下午6:50:54
	 */
	@Override
	public void setShowDockbarTitleForNewInstall(Context mContext) {
		int[] wh = ScreenUtil.getScreenWH();
		if (wh[1] < 800) {// 分辨率小于800的不显示文字
			SettingsPreference.getInstance().setShowDockbarText(false);
		}
	}

	/**
	 * 桌面加载时，从数据库读取Favorites数据
	 * 
	 * @param context
	 * @param loader
	 * @param mModel
	 */
	@Override
	public boolean loadFavoritesDataFromDB(Context context, LauncherLoader loader, BaseLauncherModel mModel) {
		final String TAG = "loadData";
		final ContentResolver contentResolver = context.getContentResolver();
		final PackageManager manager = context.getPackageManager();
		Cursor c = null;
		final ArrayList<Long> itemsToRemove = new ArrayList<Long>();
		try {
			if (Global.isOnScene()) {
				c = contentResolver.query(LauncherSettings.Favorites.getContentUri(), null, SceneDatabase.FAVORITES_TABLE_SCENE_ID + "=?",
						new String[] { SceneManager.getInstance(context).getCurrentScene().sceneId }, null);
			} else {
				c = contentResolver.query(LauncherSettings.Favorites.getContentUri(), null, null, null, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//重置桌面
			BackupSetting.toReset(context);
			HiLauncherEXUtil.restartDesktop(context);
			return false;
		}
		if (null == c)
			return false;
		
		try {
			final int idIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites._ID);
			final int intentIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.INTENT);
			final int titleIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.TITLE);
			final int iconTypeIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON_TYPE);
			final int iconIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON);
			final int iconPackageIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON_PACKAGE);
			final int iconResourceIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON_RESOURCE);
			final int containerIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CONTAINER);
			final int itemTypeIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.ITEM_TYPE);
			final int appWidgetIdIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.APPWIDGET_ID);
			final int screenIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.SCREEN);

			final int cellXIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CELLX);
			final int cellYIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CELLY);
			final int spanXIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.SPANX);
			final int spanYIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.SPANY);
			final int uriIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.URI);
			final int displayModeIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.DISPLAY_MODE);

			ApplicationInfo appInfo;
			String intentDescription;
			WidgetInfo appWidgetInfo;
			int container;
			long id;
			Intent intent;

			while (!loader.isStopped() && c.moveToNext()) {
				try {
					int itemType = c.getInt(itemTypeIndex);

					switch (itemType) {
					case LauncherSettings.Favorites.ITEM_TYPE_ANYTHING:
						AnythingInfo anythinginfo = new AnythingInfo();
						anythinginfo.id = c.getLong(idIndex);
						container = c.getInt(containerIndex);
						anythinginfo.container = container;
						anythinginfo.screen = c.getInt(screenIndex);
						anythinginfo.cellX = c.getInt(cellXIndex);
						anythinginfo.cellY = c.getInt(cellYIndex);
						anythinginfo.spanX = c.getInt(spanXIndex);
						anythinginfo.spanY = c.getInt(spanYIndex);
						anythinginfo.flag = c.getInt(iconTypeIndex);
						anythinginfo.title = c.getString(titleIndex);
						anythinginfo.itemType = itemType;
						loader.addToItemsList(anythinginfo);
						break;
					case LauncherSettings.Favorites.ITEM_TYPE_HI_APPLICATION:
					case LauncherSettings.Favorites.ITEM_TYPE_APPLICATION:
					case LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT:
					case LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT:
					case LauncherSettings.Favorites.ITEM_TYPE_INDEPENDENCE:
					case LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT:
						id = c.getLong(idIndex);
						intentDescription = c.getString(intentIndex);
						if (intentDescription == null) {
							contentResolver.delete(LauncherSettings.Favorites.getContentUri(id, false), null, null);
							continue;
						}
						try {
							intent = Intent.parseUri(intentDescription, 0);
						} catch (URISyntaxException e) {
							continue;
						}

						if (itemType == LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT && intent.getComponent() == null
								&& c.getInt(iconTypeIndex) == LauncherSettings.BaseLauncherColumns.ICON_TYPE_BITMAP) {//适配推送图标可能无ComponentName
							appInfo = mModel.getApplicationInfoNotComponent(manager, context, c, iconIndex, titleIndex, iconResourceIndex);
						} else if (itemType == LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT
								|| itemType == LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT
								|| itemType == LauncherSettings.Favorites.ITEM_TYPE_INDEPENDENCE
								|| itemType == LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT) {
							appInfo = mModel.getShortcutInfo(c, context, iconTypeIndex, iconPackageIndex, iconResourceIndex, iconIndex,
									titleIndex, itemType);
							appInfo.uri = c.getString(uriIndex);
							appInfo.displayMode = c.getInt(displayModeIndex);
						} else if (itemType == LauncherSettings.Favorites.ITEM_TYPE_HI_APPLICATION) {
							ApplicationInfo app = MyPhoneDataFactory.getAppFromIntent(context, intent);
							appInfo = mModel.getHiApplicationInfo(app, c, iconIndex, titleIndex);
							if (MyPhoneDataFactory.isPrivateZoneInHideState(appInfo)) {
								appInfo.iconBitmap = null;
								appInfo.title = "";
							}
						} else {
							appInfo = mModel.getApplicationInfo(manager, intent, context, c, iconIndex, titleIndex);
							if(appInfo != null && appInfo.componentName != null){
								loadedPkgNames.add(appInfo.componentName.getPackageName());
							}
						}

						if (appInfo != null) {
							mModel.updateSavedIcon(context, appInfo, c, iconIndex);

							appInfo.intent = intent;
							appInfo.id = c.getLong(idIndex);
							container = c.getInt(containerIndex);
							appInfo.container = container;
							appInfo.screen = c.getInt(screenIndex);

							appInfo.cellX = c.getInt(cellXIndex);
							appInfo.cellY = c.getInt(cellYIndex);
							appInfo.spanX = c.getInt(spanXIndex);
							appInfo.spanY = c.getInt(spanYIndex);
							// check & update map of what's occupied
							// if (!checkItemPlacement(occupied, appInfo)) {
							// break;
							// }

							switch (container) {
							case LauncherSettings.Favorites.CONTAINER_DESKTOP:
								loader.addToItemsList(appInfo);
								break;
							case LauncherSettings.Favorites.CONTAINER_DOCKBAR:
								// mDockItems.add(appInfo);
								if (appInfo.screen == BaseMagicDockbar.DEFAULT_SCREEN)
									loader.getCurrentDockitems().add(appInfo);
								else
									loader.getDockitems().add(appInfo);
								break;
							default:
								// Item is in a user folder
								FolderInfo folderInfo = mModel.findOrMakeUserFolder(loader.getFolders(), container);
								folderInfo.add(appInfo);
								break;
							}
						} else {
							Log.e("Loader", "Error loading shortcut " + id + ", removing it");
							contentResolver.delete(LauncherSettings.Favorites.getContentUri(id, false), null, null);
						}
						break;
					case LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER:
					case LauncherSettings.Favorites.ITEM_TYPE_THEME_APP_FOLDER:
					case LauncherSettings.Favorites.ITEM_TYPE_LAUNCHER_RECOMMEND_FOLDER:
					case LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER:
						id = c.getLong(idIndex);
						FolderInfo folderInfo = null;
						if(itemType == Favorites.ITEM_TYPE_UPGRADE_FOLDER){
							folderInfo = new UpgradeFolderInfo();
							DrawerMainView.initUpgradeFolderContent(context, folderInfo);
						}else{							
							folderInfo = mModel.findOrMakeUserFolder(loader.getFolders(), id);
						}
						folderInfo.itemType = itemType;

						folderInfo.title = c.getString(titleIndex);

						folderInfo.id = id;
						container = c.getInt(containerIndex);
						folderInfo.container = container;
						folderInfo.screen = c.getInt(screenIndex);

						folderInfo.cellX = c.getInt(cellXIndex);
						folderInfo.cellY = c.getInt(cellYIndex);
						folderInfo.spanX = c.getInt(spanXIndex);
						folderInfo.spanY = c.getInt(spanYIndex);

						if (FolderEncriptHelper.getNewInstance().isFolderEncript(id, FolderIconTextView.OPEN_FOLDER_FROM_LAUNCHER)) {
							folderInfo.isEncript = true;
						}
						
						switch (container) {
						case LauncherSettings.Favorites.CONTAINER_DESKTOP:
							loader.addToItemsList(folderInfo);
							break;
						case LauncherSettings.Favorites.CONTAINER_DOCKBAR:
							if (folderInfo.screen == BaseMagicDockbar.DEFAULT_SCREEN)
								loader.getCurrentDockitems().add(folderInfo);
							else
								loader.getDockitems().add(folderInfo);
							break;
						}

						loader.getFolders().put(folderInfo.id, folderInfo);
						break;
					case LauncherSettings.Favorites.ITEM_TYPE_APPWIDGET:
					case LauncherSettings.Favorites.ITEM_TYPE_PANDA_WIDGET:
					case LauncherSettings.Favorites.ITEM_TYPE_PANDA_PREVIEW_WIDGET:
						// Read all Launcher-specific widget details
						appWidgetInfo = mModel.getAppWidgetInfo(itemType, c, appWidgetIdIndex, iconPackageIndex, iconResourceIndex,
								iconTypeIndex, titleIndex);
						appWidgetInfo.id = c.getLong(idIndex);
						appWidgetInfo.screen = c.getInt(screenIndex);
						appWidgetInfo.cellX = c.getInt(cellXIndex);
						appWidgetInfo.cellY = c.getInt(cellYIndex);
						appWidgetInfo.spanX = c.getInt(spanXIndex);
						appWidgetInfo.spanY = c.getInt(spanYIndex);

						container = c.getInt(containerIndex);
						if (container != LauncherSettings.Favorites.CONTAINER_DESKTOP) {
							Log.e(TAG, "Widget found where container " + "!= CONTAINER_DESKTOP -- ignoring!");
							continue;
						}
						appWidgetInfo.container = c.getInt(containerIndex);

						// check & update map of what's occupied
						// if (!checkItemPlacement(occupied, appWidgetInfo)) {
						// break;
						// }

						loader.addToAppWidgetList(appWidgetInfo);
						break;
					case LauncherSettings.Favorites.ITEM_TYPE_GAME_THEME_RECOMMEND_APP://游戏主题图标
						ApplicationInfo info = new ApplicationInfo();
						info.id = c.getLong(idIndex);
						info.itemType = LauncherSettings.Favorites.ITEM_TYPE_GAME_THEME_RECOMMEND_APP;
						info.container = LauncherSettings.Favorites.CONTAINER_DESKTOP;
						info.screen = c.getInt(screenIndex);
						info.cellX = c.getInt(cellXIndex);
						info.cellY = c.getInt(cellYIndex);
						info.spanX = c.getInt(spanXIndex);
						info.spanY = c.getInt(spanYIndex);
						Intent gameThemeAppIntent = null;
						try {
							gameThemeAppIntent = Intent.parseUri(c.getString(intentIndex), 0);
						} catch (URISyntaxException e) {
							continue;
						}
						info.intent = gameThemeAppIntent;
						info.iconBitmap = BaseBitmapUtils.createIconBitmapFor91Icon(GameThemeRecommendAppBean.getNowGameThemeRecommendIcon(), context);
						info.title = c.getString(titleIndex);
						info.useIconMask = true;
						container = c.getInt(containerIndex);
						switch (container) {
						case LauncherSettings.Favorites.CONTAINER_DESKTOP:
							loader.addToItemsList(info);
							break;
						case LauncherSettings.Favorites.CONTAINER_DOCKBAR:
							// mDockItems.add(appInfo);
							if (info.screen == BaseMagicDockbar.DEFAULT_SCREEN)
								loader.getCurrentDockitems().add(info);
							else
								loader.getDockitems().add(info);
							break;
						default:
							// Item is in a user folder
							folderInfo = mModel.findOrMakeUserFolder(loader.getFolders(), container);
							folderInfo.add(info);
							break;
						}
						break;
					case LauncherSettings.Favorites.ITEM_TYPE_TAG_VIEW:
						TagApplicationInfo tagInfo = new TagApplicationInfo();
						tagInfo.id = c.getLong(idIndex);
						tagInfo.container = c.getInt(containerIndex);
						tagInfo.screen = c.getInt(screenIndex);
						tagInfo.title = c.getString(titleIndex);
						tagInfo.defaultUri = c.getString(uriIndex);
						tagInfo.iconKeyValues = c.getString(iconPackageIndex);
						tagInfo.iconPath = c.getString(iconResourceIndex);
						intentDescription = c.getString(intentIndex);
						if (!StringUtil.isEmpty(intentDescription)) {
							tagInfo.intent = Intent.parseUri(intentDescription, 0);
						}
						loader.addToTagItemsList(tagInfo);
						break;
					default:
						// LauncherAppWidgetInfo appwidgetInfo =
						// LauncherWidgetHelper.createWidgetInfo(itemType);
						// if (appwidgetInfo == null)
						// break;
						WidgetInfo appwidgetInfo = new WidgetInfo();
						appwidgetInfo.itemType = itemType;

						container = c.getInt(containerIndex);
						if (container != LauncherSettings.Favorites.CONTAINER_DESKTOP) {
							e(TAG, "Widget found where container " + "!= CONTAINER_DESKTOP  ignoring!");
							continue;
						}
						appwidgetInfo.id = c.getLong(idIndex);
						appwidgetInfo.screen = c.getInt(screenIndex);
						appwidgetInfo.container = container;
						appwidgetInfo.cellX = c.getInt(cellXIndex);
						appwidgetInfo.cellY = c.getInt(cellYIndex);
						appwidgetInfo.spanX = c.getInt(spanXIndex);
						appwidgetInfo.spanY = c.getInt(spanYIndex);
						appwidgetInfo.title = c.getString(titleIndex);
						appwidgetInfo.appWidgetId = c.getInt(appWidgetIdIndex);
						// if (!checkItemPlacement(occupied, appwidgetInfo)) {
						// break;
						// }

						loader.addToAppWidgetList(appwidgetInfo);
						break;
					}
				} catch (Exception e) {
					Log.w(TAG, "Desktop items loading interrupted:", e);
				}
			}
		} finally {
			c.close();
		}

		if (itemsToRemove.size() > 0) {
			ContentProviderClient client = contentResolver.acquireContentProviderClient(LauncherSettings.Favorites.getContentUri());
			// Remove dead items
			for (long id : itemsToRemove) {
				// Don't notify content observers
				try {
					client.delete(LauncherSettings.Favorites.getContentUri(id, false), null, null);
				} catch (RemoteException e) {
					Log.w(TAG, "Could not remove id = " + id);
				}
			}
		}
		
		// 帆悦卸载白名单
		String uninstallWhite = ConfigPreferences.getInstance().getSP().getString("fanyue_unistall_white_list", "");
		if(!TextUtils.isEmpty(uninstallWhite)) {
			uninstallWhiteMap.clear();
			String[] split = uninstallWhite.split(";");
			if(null != split) {
				for(int i=0; i<split.length; i++) {
					String pckInfo = split[i];
					String[] pckInfoArray = pckInfo.split("\\|");
					if(null == pckInfoArray || 2 != pckInfoArray.length) continue;
					uninstallWhiteMap.put(pckInfoArray[0], pckInfoArray[1]);
				}
			}
		}

		//解析System App entry
		String systemApps = SettingsPreference.getInstance().getSystemAppPkgNames();
		if (!TextUtils.isEmpty(systemApps)) {
			String[] split = systemApps.split(";");
			if (split != null) {
				for (int i = 0, len = split.length; i < len; i++) {
					String e = split[i];
					if (!TextUtils.isEmpty(e)) {
						String[] entry = e.split("\\|");// [0] = packageName; [1] = expire(unit : minute)

						String packageName;

						if(entry != null && entry.length > 0){
							packageName = entry[0];
						} else {
							continue;
						}

						Long expire = null;
						if (packageName != null && entry.length > 1) {
							try {
								String expireStr = entry[1];
								if (!TextUtils.isEmpty(expireStr)) {
									expire = Long.valueOf(entry[1]);
								}
							} catch (Exception ex) {
							}
						}

						systemAppsMapping.put(packageName, expire);
					}
				}
			}
		}

		// 帆悦双开应用白名单 caizp 2017-03-21
		String multiAppWhite = ConfigPreferences.getInstance().getSP().getString("fanyue_multi_app_white_list", "");
		if(!TextUtils.isEmpty(multiAppWhite)) {
			multiAppWhiteMap.clear();
			String[] split = multiAppWhite.split(";");
			if (null != split) {
				for (int i = 0; i < split.length; i++) {
					String pck = split[i];
					multiAppWhiteMap.put(pck, "");
				}
			}
		}
		return true;
	}

	/**
	 * 绑定数据
	 * 
	 * @param workspace
	 * @param mDockbar
	 */
	@Override
	public void bindItems(List<ItemInfo> shortcuts, int start, int end, BaseLauncher mLauncher, ScreenViewGroup workspace,
			BaseMagicDockbar mDockbar) {
		boolean bindOnInstall = ConfigPreferences.getInstance().isThemeShopV6Bubble();
		List<String> folderNames = new ArrayList<String>();
		for (int i = start; i < end && i < shortcuts.size(); i++) {
			View view = null;
			final ItemInfo item = shortcuts.get(i);
			switch (item.itemType) {
			case LauncherSettings.Favorites.ITEM_TYPE_APPLICATION:
			case LauncherSettings.Favorites.ITEM_TYPE_HI_APPLICATION:
			case LauncherSettings.Favorites.ITEM_TYPE_GAME_THEME_RECOMMEND_APP:
				if (item.container == LauncherSettings.Favorites.CONTAINER_DESKTOP) {
					if(PushUtil.isPushedIcon(item)){
						view = LauncherViewHelper.createPushIconTextView((ApplicationInfo) item);
					}else{
						view = mLauncher.createCommonAppView((ApplicationInfo) item);
					}
					workspace.addInScreen(view, item.screen, item.cellX, item.cellY, item.spanX, item.spanY);
				} else if (item.container == LauncherSettings.Favorites.CONTAINER_DOCKBAR) {
					view = LauncherViewHelper.createDockShortcut(mLauncher, (ApplicationInfo) item);
					mDockbar.addInDockbar(view, item.screen, item.cellX, item.cellY, item.spanX, item.spanY, false);
				}
				break;
			case LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT:
			case LauncherSettings.Favorites.ITEM_TYPE_INDEPENDENCE:
			case LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT:
			case LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT:
				if (item.container == LauncherSettings.Favorites.CONTAINER_DESKTOP) {
					view = mLauncher.createCommonAppView((ApplicationInfo) item);
					workspace.addInScreen(view, item.screen, item.cellX, item.cellY, item.spanX, item.spanY);
				} else if (item.container == LauncherSettings.Favorites.CONTAINER_DOCKBAR) {
					view = LauncherViewHelper.createDockShortcut(mLauncher, (ApplicationInfo) item);
					mDockbar.addInDockbar(view, item.screen, item.cellX, item.cellY, item.spanX, item.spanY, false);
				}
				break;
			case LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER:
			case LauncherSettings.Favorites.ITEM_TYPE_THEME_APP_FOLDER:
			case LauncherSettings.Favorites.ITEM_TYPE_LAUNCHER_RECOMMEND_FOLDER:
			case LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER:
				if(LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER == item.itemType) {
					CharSequence title = ((FolderInfo) item).title;
					if(!TextUtils.isEmpty(title)) {
						folderNames.add(title.toString());
					}
				}
				if (item.container == LauncherSettings.Favorites.CONTAINER_DESKTOP) {
					view = LauncherViewHelper.createFolderIconTextView(mLauncher, (FolderInfo) item);
					workspace.addInScreen(view, item.screen, item.cellX, item.cellY, item.spanX, item.spanY);
				} else {
					view = BaseLauncherViewHelper.createFolderIconTextViewFromContext(mLauncher, (FolderInfo) item);
					mDockbar.addInDockbar(view, item.screen, item.cellX, item.cellY, item.spanX, item.spanY, false);
				}
				break;
			case LauncherSettings.Favorites.ITEM_TYPE_TAG_VIEW:
				view = TagViewController.createTagViewByApplicationInfo(mLauncher, (TagApplicationInfo) item);
				if(view != null){					
					workspace.getCellLayoutAt(item.screen).addView(view, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				}
				break;
			default:
				view = LauncherViewHelper.createAnythingAppView(mLauncher, item);
				if (view != null)
					workspace.addInScreen(view, item.screen, item.cellX, item.cellY, item.spanX, item.spanY);
			}
			// 新安装绑定冒泡View
			if (bindOnInstall) {
				bindBubbleItem(item, workspace, view);
			}
		}
		// 添加神龙AD文件夹名称列表
		CustomChannelController.addShenLongAdKeywords(mLauncher, folderNames);
	}

	private boolean bindBubbleItem(ItemInfo item, ScreenViewGroup workspace, View view) {
		try {
			// V6用户初次安装时，增加“个性化”view的冒泡
			if (!(item instanceof ApplicationInfo) || view == null)
				return false;
			ApplicationInfo info = (ApplicationInfo) item;
			if (info.itemType == LauncherSettings.Favorites.ITEM_TYPE_HI_APPLICATION
					&& info.title.equals(workspace.getContext().getString(R.string.theme_shop_v6_app_name))) {
//				if(!SemChannelController.isSemChannel()){
//					LauncherBubbleManager.getInstance().addBubble(view,
//							new BubbleItemBuilder(view).buildContent(workspace.getContext().getString(R.string.theme_shop_v6_first_hint))
//									.buildDuration(6000).build());
//				}
				ConfigPreferences.getInstance().setThemeShopV6Bubble(false);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void updateAppsInWorkspace(List<ApplicationInfo> apps, BaseLauncher mLauncher) {
		if (apps == null)
			return;

		try {
			// LauncherApplication application = (LauncherApplication)
			// mLauncher.getApplicationContext();
			// IconCache mIconCache = application.getIconCache();
			BaseIconCache mIconCache = BaseConfig.getIconCache();
			// final Workspace mWorkspace = mLauncher.mWorkspace;
			final ScreenViewGroup mWorkspace = mLauncher.getScreenViewGroup();
			final int count = mWorkspace.getChildCount();
			final ArrayList<View> childrenToRemove = new ArrayList<View>();
			for (int i = 0; i < count; i++) {
				final CellLayout layout = (CellLayout) mWorkspace.getChildAt(i);
				int childCount = layout.getChildCount();
				int updateCount = 0;
				for (int j = 0; j < childCount; j++) {
					final View view = layout.getChildAt(j);
					Object tag = view.getTag();
					if (tag == null)
						continue;

					if (tag instanceof ApplicationInfo) {
						ApplicationInfo info = (ApplicationInfo) tag;
						if(Favorites.ITEM_TYPE_SHORTCUT == info.itemType) continue;
						final Intent intent = info.intent;
						if (intent == null || intent.getComponent() == null)
							continue;

						final ComponentName name = intent.getComponent();
						if (Intent.ACTION_MAIN.equals(intent.getAction()) && name != null) {
							final int appCount = apps.size();
							for (int k = 0; k < appCount; k++) {
								ApplicationInfo app = apps.get(k);
								if (app.componentName.equals(name)) {
									if(!refreshUIForReplaceIconApp(name, view)){//刷新定制图标可能出现的图标缩小问题
										info.iconBitmap = mIconCache.refreshTheCache(info);
										((IconMaskTextView) view).setIconBitmap(info.iconBitmap);
										view.invalidate();
									}
									updateCount++;
								} else if (app.componentName.getPackageName().equals(name.getPackageName())) {// 覆盖安装后应用的入口类名匹配不上
									if (1 == appCount) {// 若应用只匹配出一个入口，则把图标更新为该入口
										info.setActivity(app.componentName);
										info.iconBitmap = mIconCache.getIcon(info);
										((IconMaskTextView) view).setIconBitmap(info.iconBitmap);
										view.invalidate();
										BaseLauncherModel.updateItemInDatabase(mLauncher, info);
										updateCount++;
									} else {// 匹配出多个入口，则删除该图标 caizp 2013-4-2
//										BaseLauncherModel.deleteItemFromDatabase(mLauncher, info);
//										childrenToRemove.add(view);
									}
								}
							}
						}
					} else if (tag instanceof FolderInfo) {
						final FolderInfo info = (FolderInfo) tag;
						final List<ApplicationInfo> contents = info.contents;
						ArrayList<ApplicationInfo> toRemove = null;
						final int contentsCount = contents.size();

						for (int k = 0; k < contentsCount; k++) {
							final ApplicationInfo applicationInfo = contents.get(k);
							if(Favorites.ITEM_TYPE_SHORTCUT == applicationInfo.itemType) continue;
							final Intent intent = applicationInfo.intent;
							if (intent == null || intent.getComponent() == null)
								continue;

							final ComponentName name = intent.getComponent();

							if (Intent.ACTION_MAIN.equals(intent.getAction()) && name != null) {
								final int appCount = apps.size();
								for (int m = 0; m < appCount; m++) {
									ApplicationInfo app = apps.get(m);
									if (app.componentName.equals(name)) {
										applicationInfo.iconBitmap = mIconCache.refreshTheCache(applicationInfo);
										// applicationInfo.iconBitmap =
										// mIconCache.getIcon(applicationInfo);
										view.invalidate();
									} else if (app.componentName.getPackageName().equals(name.getPackageName())) {// 覆盖安装后应用的入口类名匹配不上
										if (1 == appCount) {// 若应用只匹配出一个入口，则把图标更新为该入口
											applicationInfo.setActivity(app.componentName);
											applicationInfo.iconBitmap = mIconCache.getIcon(applicationInfo);
											view.invalidate();
											BaseLauncherModel.updateItemInDatabase(mLauncher, applicationInfo);
										} else {// 匹配出多个入口，则删除该图标 caizp 2013-4-2
											BaseLauncherModel.deleteItemFromDatabase(mLauncher, applicationInfo);
											if (toRemove == null)
												toRemove = new ArrayList<ApplicationInfo>();

											toRemove.add(applicationInfo);
										}
									}
								}
							}
						}

						if (toRemove != null)
							contents.removeAll(toRemove);
					}
				}

				childCount = childrenToRemove.size();
				for (int j = 0; j < childCount; j++) {
					View child = childrenToRemove.get(j);
					layout.removeViewInLayout(child);
					if (child instanceof DropTarget) {
						mLauncher.getDragController().removeDropTarget((DropTarget) child);
					}
				}
				if (childCount > 0 || updateCount > 0) {
					layout.requestLayout();
					layout.invalidate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mLauncher, "Updating workspace is someting wrong:)", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean isMyPhoneItem(ItemInfo item) {
		return item.itemType == LauncherSettings.Favorites.ITEM_TYPE_HI_APPLICATION;
	}

	/**
	 * dockbar是否显示应用的名称
	 */
	@Override
	public boolean isShowDockbarText() {
		return Global.isShowDockbarText();
	}

	/**
	 * 初始化打点统计
	 */
	@Override
	public void initHiAnalytics(Context mContext) {
		CommonGlobal.initDain91SDK(mContext);
	}

	/**
	 * 启动打点统计
	 * 
	 * @param mContext
	 */
	@Override
	public void startUpHiAnalytics(Context mContext) {
		HiAnalytics.startUp(mContext);
	}

	@Override
	public void loadAndBindAllApps(Callbacks callback, DeferredHandler handler, Context mContext) {
		new AppsLoaderAndBinder(callback, handler).loadAndBindOnLauncher(mContext);
	}

	/**
	 * new一个CellLayout的LayoutParams
	 * 
	 * @author dingdj Date:2014-3-26下午3:09:33
	 */
	@Override
	public CellLayout.LayoutParams newCellLayoutLayoutParams(ViewGroup.MarginLayoutParams vm) {
		return !Global.isOnScene() ? new CellLayout.LayoutParams(vm) : new SceneCellLayout.LayoutParams(vm);
	}

	/**
	 * View在workspace的当前屏时候回调，目前支持世界杯插件回调
	 * 
	 * @param view
	 * @return 是否调用成功,若返回false,下次将不会再调用该方法
	 */
	@Override
	public boolean onWorkspaceCurrentScreen(View container) {
		try {
			if (!(container instanceof PandaWidgetViewContainer))
				return false;

			PandaWidgetViewContainer widgetContainer = (PandaWidgetViewContainer) container;

			if (!(widgetContainer.getTag() instanceof PandaWidgetInfo))
				return false;

			View view = widgetContainer.getWidgetView();
			PandaWidgetInfo info = (PandaWidgetInfo) widgetContainer.getTag();
			if (info.layoutResString != null)
				return false;
			Launcher mLauncher = Global.getLauncher();
			if (!mLauncher.isOnSpringMode()) {
				ThirdWidgetAccessor
						.invokeThirdWidgetOnLauncherInvokeMethod(view, info.appWidgetId, mLauncher.mWorkspace.getCurrentScreen());
			}
			return true;
		} catch (Exception e) {
			// Log.e("onWorkspaceCurrentScreen", e.toString());
		}
		return false;
	}

	@Override
	public int[] getScreenEffectsForRandom() {
		return EffectConfig.SCREEN_EFFECT_VALUES;
	}

	@Override
	public int[] getDrawerEffectsForRandom() {
		return EffectConfig.DRAWER_EFFECT_VALUES;
	}

	@Override
	public boolean isDrawerVisiable(){
		return Global.isDrawerVisiable();
	}
	
	@Override
	public String getSaveScreenCountKey(){
		Context mContext = Global.getApplicationContext();
		if(mContext != null){
			String sceneId = SceneManager.curSceneId;
			if(sceneId != null && !LauncherSwitchController.DEFAULT_COMPLEX_SCENE_ID.equals(sceneId)){
				return ConfigFactory.SCREENCOUNT + "_" + sceneId;
			}
		}
		return super.getSaveScreenCountKey();
	}
	
	@Override
	public String getSaveDefualtScreenKey(){
		Context mContext = Global.getApplicationContext();
		if(mContext != null){
			String sceneId = SceneManager.curSceneId;
			if(sceneId != null && !LauncherSwitchController.DEFAULT_COMPLEX_SCENE_ID.equals(sceneId)){
				return BaseConfigPreferences.KEY_DEFAULT_SCREEN + "_" + sceneId;
			}
		}
		return super.getSaveDefualtScreenKey();
	}
	
	@Override
	public boolean isShortcutItem(ItemInfo item){
		if(item.itemType == LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT)
			return true;
		if(item.itemType == LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT && item instanceof ApplicationInfo){
			ApplicationInfo info = (ApplicationInfo) item;
			if(info != null && null != info.intent && (info.intent.getAction().equals(CustomIntent.ACTION_YINNI_SCREENOFF)
			||info.intent.getAction().equals(CustomIntent.ACTION_YINNI_ADWALL))){
				return false;
			}
		}
		// 爱淘宝、双开应用图标可删除 caizp 2017-03-15
		if(item.itemType == LauncherSettings.BaseLauncherColumns.ITEM_TYPE_SHORTCUT && item instanceof ApplicationInfo) {
			Log.e("itemType", "itemType="+item.itemType+",ITEM_TYPE_SHORTCUT="+LauncherSettings.BaseLauncherColumns.ITEM_TYPE_SHORTCUT);
			ApplicationInfo info = (ApplicationInfo) item;
			if(null == info.intent || null == info.intent.getComponent()) return super.isShortcutItem(item);
			if(Global.getApplicationContext().getPackageName().equals(info.intent.getComponent().getPackageName()) &&
					"com.felink.quickhelper.LauncherOpenAppTransformActivity".equals(info.intent.getComponent().getClassName())) {
				return true;
			}
			if(Global.getApplicationContext().getString(R.string.taobao_title).equals(info.title))
				return true;
			if(info.intent != null && info.intent.toUri(0).contains("url.ifjing.com"))
				return true;
			if(1 == info.displayMode) {
				return true;
			}
		}
		//推荐下载图标可删除
		if (LauncherProviderHelper.isLitianRecommendApp(item))
			return true;
		return super.isShortcutItem(item);
	}

	@Override
	public boolean isAllowedDelete(ItemInfo item){
		//配置的轮流打开app不可删除
		if(item.itemType == Favorites.ITEM_TYPE_APPLICATION  && item instanceof ApplicationInfo){
			String allSwitchActionStr = SettingsPreference.getInstance().getSwtichAppActionInfo();
			ApplicationInfo info = (ApplicationInfo) item;
			if(!TextUtils.isEmpty(allSwitchActionStr) && info.componentName != null && info.componentName.getPackageName() != null){
				String[] switchActionArray = allSwitchActionStr.split("##");
				for(String value : switchActionArray){
					if(TextUtils.isEmpty(value))
						continue;
					String[] switchAction = value.split("::");
					if( info.componentName.getPackageName().equals(switchAction[0]))
						return false;
				}
			}
		}
		return super.isAllowedDelete(item);
	}

	// ====================================以下用于情景桌面=============================//
	@Override
	public void initForScene(Context mContext) {
		Scene scene = SceneManager.getInstance(mContext).getCurrentScene();
		CellLayoutConfig.initForScene(mContext, scene.cellLayoutX, scene.cellLayoutY, scene.cellLayoutWidth, scene.cellLayoutHeight);
		DockbarCellLayoutConfig.initForScene(mContext, scene.bottomDockBarX, scene.bottomDockBarY, scene.bottomDockBarWidth,
				scene.bottomDockBarHeight);
	}

	@Override
	public void onAddSceneItemToDatabase(Context context, ContentValues values) {
		Scene scene = SceneManager.getInstance(context).getCurrentScene();
		values.put(SceneDatabase.FAVORITES_TABLE_SCENE_ID, scene.sceneId);
	}

	@Override
	public void onAddSceneItemsToDatabase(Context context, Builder builder) {
		Scene scene = SceneManager.getInstance(context).getCurrentScene();
		builder.withValue(SceneDatabase.FAVORITES_TABLE_SCENE_ID, scene.sceneId);
	}

	@Override
	public int[] spanXYMatherForScene(int spanX, int spanY, int cellWidth, int cellHeight, Object item) {
		if (spanX == 1 && spanY == 1 && Global.getApplicationContext() != null) {// 情景桌面下，app使用默认配置高度
			Scene scene = SceneManager.getInstance(Global.getApplicationContext()).getCurrentScene();
			if (item != null && (item instanceof ApplicationInfo || item instanceof AnythingInfo)) {// 取View的默认高度
				if (item instanceof ApplicationInfo
						&& ((ApplicationInfo) item).itemType == Favorites.ITEM_TYPE_CUSTOM_INTENT
						&& (CustomIntent.ACTION_OPEN_DRAWER.equals(((ApplicationInfo) item).intent.getAction()) || CustomIntent.ACTION_WIDGET_FLASHLIGHT
								.equals(((ApplicationInfo) item).intent.getAction()))) {// 91快捷的程序匣子和手电筒图标特殊处理
					return new int[] { scene.defaultAppWidth, scene.defaultAppHeight };
				}
				return scene.getAppDefaultWH(item);
			}
			if (item instanceof WidgetInfo) {// 非标小部件
				return new int[] { spanX * cellWidth, spanY * cellHeight };
			} else {
				return new int[] { scene.defaultAppWidth, scene.defaultAppHeight };
			}
		}
		if (spanX < 10 || spanY < 10) {
			return new int[] { spanX * cellWidth, spanY * cellHeight };
		} else {
			return new int[] { spanX, spanY };
		}
	}

	@Override
	public void initSceneWH(View view, LauncherIconViewConfig config, boolean calcRect) {
		if (BaseConfig.isOnScene()) {
			Scene scene = SceneManager.getInstance(BaseConfig.getApplicationContext()).getCurrentScene();
			if (scene != null) {
				if (calcRect && scene.isFillContentView(view)) {// 是否icon图标填充整个View
					config.setSceneFillContent(true);
					config.setSceneFillContentFitCenter(scene.isFillContentFitCenter(view));
				}
				if (scene.permission != null && scene.permission.isHideDeskIconText()) {// 是否显示app文字
					config.setShowText(false);
				} else {
					config.setShowText(true);
				}
			}
		}
	}

	/**
	 * 获取holeType
	 * 
	 * @author dingdj Date:2014-3-26下午3:14:59
	 * @param view
	 * @param info
	 * @param wh
	 */
	@Override
	public void getLeftTopXYByHoleType(FolderIconTextView view, ApplicationInfo info, int[] wh) {
		if (BaseConfig.isOnScene()) {// 确保情景桌面下xy位置正确
			int[] xy = CellLayoutHelper.getLeftTopXYByHoleType(view, wh);
			if (xy != null) {
				info.cellX = xy[0];
				info.cellY = xy[1];
			}
		}
	}

	@Override
	public boolean isSceneFillContentView(View view) {
		Scene scene = SceneManager.getInstance(view.getContext()).getCurrentScene();
		if (scene != null) {
			return scene.isFillContentView(view);
		}
		return false;
	}

	@Override
	public boolean isSceneFillContentFitCenter(View view) {
		Scene scene = SceneManager.getInstance(view.getContext()).getCurrentScene();
		if (scene != null) {
			return scene.isFillContentFitCenter(view);
		}
		return false;
	}

	@Override
	public boolean isShowTextOnScene() {
		Scene scene = SceneManager.getInstance(BaseConfig.getApplicationContext()).getCurrentScene();
		if (scene != null) {
			return scene.permission == null || !scene.permission.isHideDeskIconText();
		}
		return true;
	}

	@Override
	public boolean isLockedView(View v) {
		return CellLayoutHelper.isLockedView(v);
	}

	@Override
	public boolean isNewInstallApp(ApplicationInfo info) {
		return info.isNewInstall;
	}

	//循环屏滚动时，需要设置开放屏(zero)要滚动到相应的位置
	public void snapToNavigationChildView(int pageIndex) {
		try {
			Launcher launcher = Global.getLauncher();
			if (launcher != null && launcher.OPEN_PAGE_COUNT > 1) {
				launcher.getNavigationView().scrollToPage(pageIndex);
				launcher.getWorkspaceLayer().setNavigationViewChildIndex(pageIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Rect getDockbarCellLayoutPaddingRect(Context ctx) {
		return LauncherCustomConfig.getDockbarCellLayoutPaddingRect(ctx);
	}

	@Override
	public Rect getWorkspaceCellLayoutPaddingRect(Context ctx) {
		return LauncherCustomConfig.getCellLayoutPaddingRect(ctx);
	}
	
	@Override
	public int getPortMarginTop(Context ctx) {
		 return LauncherCustomConfig.getCellLayoutMarginTop(ctx);
	 }
	
	@Override
	 public int getPortMarginBottom(Context ctx) {
		 return LauncherCustomConfig.getCellLayoutMarginBottom(ctx);
	 }
	 
	@Override
	 public int getLargeIconSize(Context ctx) {
		 return LauncherCustomConfig.getLargeIconSize(ctx);
	 }

	@Override
	public boolean isAllowedUninstall(ItemInfo item) {
		if((LauncherBranchController.isLiTian() || LauncherBranchController.isMingLai() || LauncherBranchController.isXinShiKong()
				|| LauncherBranchController.isLiTian_Yinni() || LauncherBranchController.isDingKai() || LauncherBranchController.isTiapPai_SmartHome())
				&& item instanceof ApplicationInfo){
			ApplicationInfo info = (ApplicationInfo)item;
			String uninstallPkgName = info.componentName.getPackageName();

			if (!systemAppsMapping.isEmpty()) {
				if (systemAppsMapping.containsKey(uninstallPkgName)) {
					Long expire = systemAppsMapping.get(uninstallPkgName);

					if (expire != null && expire > 0) {
						long conTime = SettingsPreference.getInstance().getFirstNonWifiConnectedTime();
						if (conTime > 0) {
							long delta = System.currentTimeMillis() - conTime;
							if (delta >= (expire * 60 * 1000)) {
								return true;
							}
						}
					}
					return false;
				}
			}
		}

		if (TelephoneUtil.isMIMoble()) {
			if (item instanceof ApplicationInfo) {
				ApplicationInfo info = (ApplicationInfo) item;
				//MIUI 9
				//系统自带阅读应用，应不可缷载
				if ("com.duokan.reader".equals(info.componentName.getPackageName())) {
					if ("V9".equalsIgnoreCase(CenterControl.getProp("ro.miui.ui.version.name"))) {
						return false;
					}
				}
			}
		}

		if(null == item) return true;
		if(!(item instanceof ApplicationInfo)) return super.isAllowedUninstall(item);
		if(null == ((ApplicationInfo)item).componentName) return super.isAllowedUninstall(item);
		// 帆悦定制版支持定义卸载白名单APP第一次使用超过指定时长时，变为可卸载 caizp 2017-02-23
		if((LauncherBranchController.isFanYue() || LauncherBranchController.isShenLong()) && item instanceof ApplicationInfo){
			ApplicationInfo info = (ApplicationInfo)item;
			String uninstallPkgName = info.componentName.getPackageName();
			String uninstallClsName = info.componentName.getClassName();
			String uninstallDelayTimeMinutes = uninstallWhiteMap.get(uninstallPkgName);
			// 不在白名单中，可卸载
			if(null == uninstallDelayTimeMinutes) return true;
			//未设置可卸载延迟时长，不可卸载
			if("0".equals(uninstallDelayTimeMinutes) || "".equals(uninstallDelayTimeMinutes)) return false;
			AppDataBase db = null;
			Cursor cursor = null;
			try {
				db = AppDataBase.getInstance(BaseConfig.getApplicationContext());
				// 查询第一次使用时间 caizp 2017-02-23
				cursor = db.query(AppTable.getSelectFirstUsetime(uninstallPkgName, uninstallClsName));
				if(null != cursor && cursor.moveToFirst()) {
					long firstUsetime = cursor.getLong(cursor.getColumnIndexOrThrow("first_use_time"));
					if(0L == firstUsetime) return false;// 应用未打开过，不可卸载
					long curTime = System.currentTimeMillis();
					try {//超过指定时长后，可卸载
						return (curTime - firstUsetime > Long.parseLong(uninstallDelayTimeMinutes) * 60 * 1000);
					} catch (Exception e) {//时长配置错误，不可卸载
						e.printStackTrace();
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(null != cursor) {
					cursor.close();
				}
				if (null != db) {
					db.close();
				}
			}
			super.isAllowedUninstall(item);
		}

		if(null != uninstallWhiteMap.get(((ApplicationInfo)item).componentName.getPackageName())) return false;
		return super.isAllowedUninstall(item);
	}

	@Override
	public boolean isDataApp(ItemInfo item){
		if (item instanceof ApplicationInfo) {
			ApplicationInfo info = (ApplicationInfo) item;
			String uninstallPkgName = info.componentName.getPackageName();
			if (TelephoneUtil.isHuaweiPhone()) {
				try {
					if (LauncherBranchController.isDingKai()) {
						if ("com.huawei.hidisk".equals(uninstallPkgName)) {
							return false;
						}
					}

					/**android版本大于等于7.0时，优先判断是否为原生的系统应用 cxy 2017-10-30*/
					if (Build.VERSION.SDK_INT >= 24 && AppCheckUtil.isOriginalSystemApp(uninstallPkgName)) {
						return false;
					}

					String dataAppPkgs = SettingsPreference.getInstance().getDataApp();
					String[] pkgArray = dataAppPkgs.split(";");
					for (String pkg : pkgArray) {
						if (!TextUtils.isEmpty(pkg) && pkg.equals(uninstallPkgName)) {
							return true;
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
					return false;
				}
			} else if (TelephoneUtil.isGioneePhone()) {
				if (AppCheckUtil.isDataApp(uninstallPkgName)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean needDrawIconBackground(Context ctx){
		return LauncherCustomConfig.hasIconBackground(ctx);
	}

	@Override
	public void setCustomThemeParameters(Context ctx){
		LauncherCustomConfig.setThemeParameters(ctx);
	}


	@Override
	public String getDefaultIconKey(String key){
		return LauncherCustomConfig.getDefaultIconKey(key);
	}

	@Override
	public Drawable getSpringBackgroundBg(){
		return LauncherCustomConfig.getSpringBackgroundBg();
	}

	@Override
	public boolean showCustomGuide(Context ctx){
		return CustomChannelController.showInnerCustomGuide(ctx);
	}

	@Override
	public Bitmap getCustomIcon(ApplicationInfo info, Context context){
		return LauncherProviderHelper.getShortcutReplaceIcon(info, context);
	}

	@Override
	public Bitmap getAppIconMaskBitmap(BasePandaTheme theme){
		return LauncherCustomConfig.getAppIconMaskBitmap(theme);
	}

	@Override
	public boolean isDragOverScreenOnWorkspace(Context ctx){
		return LauncherCustomConfig.isDragOverScreenOnWorkspace(ctx);
	}

	@Override
	public int getDockbarHeight(Context ctx){
		return LauncherCustomConfig.getDockbarHeight(ctx);
	}

	@Override
	public int getLinebarHeight(Context ctx){
		return LauncherCustomConfig.getLineBarHeight(ctx);
	}

	@Override
	public int getIconTextSize(Context ctx){
		return LauncherCustomConfig.getIconTextSize(ctx);
	}

	@Override
	public boolean isDoubleApp(ApplicationInfo info) {
		if(!LauncherCustomConfig.isSupportDoubleApp())
			return false;

		Launcher launcher = Global.getLauncher();
		if(launcher != null && launcher.getDragController() != null && launcher.getDragController() instanceof DragController){
			return ((DragController)launcher.getDragController()).isDoubleApp(info);
		}
		return false;
	}

	@Override
	public boolean isDoubleAppUninstall(ApplicationInfo info, String uninstallPkgName) {
		if(!LauncherCustomConfig.isSupportDoubleApp())
			return false;
		try {
			ComponentName name = info.intent.getComponent();
			if(BaseConfig.PKG_NAME.equals(name.getPackageName())){//删除双开图标
				String packName = info.intent.getStringExtra("packagename");
				if(!TextUtils.isEmpty(packName) && packName.equals(uninstallPkgName))
					return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isUseDefaultIcon(){
		return LauncherCustomConfig.isUseDefaultIcon();
	}

	@Override
	public void setApplicationBitmap(Context ctx, ApplicationInfo info){
		UIHandlerFactory.setApplicationBitmap(ctx, info);
	}

	@Override
	public boolean refreshUIForReplaceIconApp(ComponentName name, View view){
		try{
			if(name != null && view instanceof LauncherIconView &&
					LauncherProviderHelper.isReplacedApp(name.getPackageName(), name.getClassName())){
				IconType iconType = ((LauncherIconView) view).getIconType();
				if(iconType != null && (iconType instanceof CustomP8IconType || iconType instanceof DockDefaultFourIconType)){
					((LauncherIconView) view).refreshUI();
				}
				return true;
			}
		}catch (Throwable e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int getFolderIconStyle(Context ctx){
		if(LauncherCustomConfig.isMeituStyle())
			return FolderIconTextView.ICON_STYLE_MEITU;
		if(LauncherCustomConfig.isSamsungStyle())
			return FolderIconTextView.ICON_STYLE_SUM;
		return -1;
	}

	@Override
	public boolean isAllBitmapRegular() {
		if(LauncherBranchController.isMingLai() ||
				LauncherBranchController.isShenLong() || LauncherBranchController.isDingKai()) {// 铭来或神龙定制版，返回所有图标都是规则的
			return true;
		}
		return false;
	}
}
