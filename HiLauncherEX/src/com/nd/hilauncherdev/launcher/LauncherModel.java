package com.nd.hilauncherdev.launcher;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.Intent.ShortcutIconResource;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.nd.hilauncherdev.app.AppDataFactory;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.kitset.util.IconUtils;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.LauncherSettings.Favorites;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.info.ItemInfo;
import com.nd.hilauncherdev.launcher.model.BaseLauncherModel;
import com.nd.hilauncherdev.launcher.screens.CellLayout;
import com.nd.hilauncherdev.launcher.support.BaseIconCache;
import com.nd.hilauncherdev.launcher.view.icon.ui.impl.IconMaskTextView;
import com.nd.hilauncherdev.plugin.ThirdApplicationAssit;
import com.nd.hilauncherdev.theme.ThemeManager;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.theme.pref.ThemeSharePref;

public class LauncherModel extends BaseLauncherModel{
	static final String TAG = "LauncherModel";

//	public static enum OperationModel{
//		INSERT,UPDATE,DELETE,QUERY
//	}

	public LauncherModel(LauncherApplication app, BaseIconCache iconCache) {
		super(app, iconCache);
	}

	/**
	 * 获取文件夹内容（只供推荐文件夹调用）
	 * @author Ryan
	 */
/*	public static List<ApplicationInfo> getFolderContentById(Context context, long id) {
		List<ApplicationInfo> result = new ArrayList<ApplicationInfo>();
		
		Cursor c = null;
		
		try {
			final ContentResolver cr = context.getContentResolver();
			c = cr.query(LauncherSettings.Favorites.getContentUri(), null, "container=?", new String[] { String.valueOf(id) }, null);
			final int intentIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.INTENT);
			final int titleIndex = c.getColumnIndexOrThrow(LauncherSettings.BaseLauncherColumns.TITLE);
			final int idIndex = c.getColumnIndexOrThrow(LauncherSettings.BaseLauncherColumns._ID);
			while (c.moveToNext()) {
				String intentDescription = c.getString(intentIndex);
				if (intentDescription == null)
					continue;
				
				Intent intent = null;
				try {
					intent = Intent.parseUri(intentDescription, 0);
				} catch (URISyntaxException e) {
					e.printStackTrace();
					continue;
				}
				
				if (intent.getComponent() == null || intent.getComponent().getPackageName() == null) {
					// 解决“每日新鲜事”等图标在换主题后，从推荐应用文件夹内消失的问题
					int itemType = c.getInt(c.getColumnIndexOrThrow(LauncherSettings.Favorites.ITEM_TYPE));
					if (itemType == LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT) {
						Launcher launcher = Global.getLauncher();
						if (launcher != null && launcher.getLauncherModel() != null) {
							ApplicationInfo info = launcher.getLauncherModel().getShortcutInfo(c, 
									                                                   context, 
									                                                   c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON_TYPE), 
									                                                   c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON_PACKAGE), 
									                                                   c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON_RESOURCE), 
									                                                   c.getColumnIndexOrThrow(LauncherSettings.Favorites.ICON), 
									                                                   titleIndex, 
									                                                   itemType);
							if (info != null) {
								info.intent = intent;
								info.id = c.getInt(idIndex);
								info.container = id;
								result.add(info);
							}
						}
					}
					
					continue;
				}
								
				ApplicationInfo info = new ApplicationInfo();
				info.intent = intent;
				info.componentName= intent.getComponent();
				info.title = c.getString(titleIndex);
				info.id = c.getInt(idIndex);
				info.container = id;
				result.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		
		return result;
	}*/

	/**
	 * 根据应用类型加载应用项
	 * @author wangguomei
	 */
	public static List<ApplicationInfo> loadItemsByTypeForLocale(Context context) {
		final ContentResolver cr = context.getContentResolver();
		Cursor c = cr.query(LauncherSettings.Favorites.getContentUri(), null, "itemType=? or itemType=? or itemType=?", 
				new String[] {String.valueOf(LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT),
				              String.valueOf(LauncherSettings.Favorites.ITEM_TYPE_APPLICATION),
				              String.valueOf(LauncherSettings.Favorites.ITEM_TYPE_INDEPENDENCE)}, null);
		
		List<ApplicationInfo> items = new ArrayList<ApplicationInfo>(20);
		try {
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				ApplicationInfo appInfo = new ApplicationInfo();
				appInfo.id = c.getInt(c.getColumnIndexOrThrow(LauncherSettings.Favorites._ID));
				appInfo.title = c.getString(c.getColumnIndexOrThrow(LauncherSettings.Favorites.TITLE));
				try {
					appInfo.intent = Intent.parseUri(c.getString(c.getColumnIndexOrThrow(LauncherSettings.Favorites.INTENT)),0);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				appInfo.container = c.getInt(c.getColumnIndexOrThrow(LauncherSettings.Favorites.CONTAINER));
				appInfo.itemType = c.getInt(c.getColumnIndexOrThrow(LauncherSettings.Favorites.ITEM_TYPE));
				items.add(appInfo);
			}
		} catch (Exception e) {
			Log.e(TAG, "err in loadItemsByTypeForLocale():" + e.toString());
		} finally {
			c.close();
		}
		
		return items;
	}

	/**
	 * 根据应用类型加载应用项
	 * @author wangguomei
	 */
	public static List<FolderInfo> loadFolderItems(Context context) {
		final ContentResolver cr = context.getContentResolver();
		Cursor c = cr.query(LauncherSettings.Favorites.getContentUri(), null, "itemType=?",
				new String[] {String.valueOf(Favorites.ITEM_TYPE_USER_FOLDER)}, null);

		List<FolderInfo> items = new ArrayList<FolderInfo>(20);
		try {
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				FolderInfo appInfo = new FolderInfo();
				appInfo.id = c.getInt(c.getColumnIndexOrThrow(LauncherSettings.Favorites._ID));
				appInfo.title = c.getString(c.getColumnIndexOrThrow(LauncherSettings.Favorites.TITLE));
				appInfo.container = c.getInt(c.getColumnIndexOrThrow(LauncherSettings.Favorites.CONTAINER));
				appInfo.itemType = c.getInt(c.getColumnIndexOrThrow(LauncherSettings.Favorites.ITEM_TYPE));
				items.add(appInfo);
			}
		} catch (Exception e) {
			Log.e(TAG, "err in loadItemsByTypeForLocale():" + e.toString());
		} finally {
			c.close();
		}

		return items;
	}
	
	/**
	 * 批量更新应用项名称
	 * @author wangguomei
	 */
	public static void batchUpdateItemTitleById(Context context, List<ApplicationInfo> items) {
		
		if (items == null || items.isEmpty())
			return;
		
		ArrayList<ContentProviderOperation> batchOps = new ArrayList<ContentProviderOperation>();

		for (ApplicationInfo applicationInfo : items) {
			Uri uri = LauncherSettings.Favorites.getContentUri(applicationInfo.id, false);
			Builder builder = ContentProviderOperation.newUpdate(uri);
			builder.withValue(LauncherSettings.Favorites.TITLE, applicationInfo.title);
			
			batchOps.add(builder.build());
		}
		
		try {
			context.getContentResolver().applyBatch(Favorites.AUTHORITY, batchOps);
		} catch (RemoteException e) {
			Log.e(TAG, "update database failed",e);
		} catch (OperationApplicationException e) {
			Log.e(TAG, "update database failed",e);
		}
	}

	/**
	 * 批量更新应用项名称
	 * @author wangguomei
	 */
	public static void batchUpdateFolderItemTitleById(Context context, List<FolderInfo> items) {

		if (items == null || items.isEmpty())
			return;

		ArrayList<ContentProviderOperation> batchOps = new ArrayList<ContentProviderOperation>();

		for (FolderInfo applicationInfo : items) {
			Uri uri = LauncherSettings.Favorites.getContentUri(applicationInfo.id, false);
			Builder builder = ContentProviderOperation.newUpdate(uri);
			builder.withValue(LauncherSettings.Favorites.TITLE, applicationInfo.title);

			batchOps.add(builder.build());
		}

		try {
			context.getContentResolver().applyBatch(Favorites.AUTHORITY, batchOps);
		} catch (RemoteException e) {
			Log.e(TAG, "update database failed",e);
		} catch (OperationApplicationException e) {
			Log.e(TAG, "update database failed",e);
		}
	}

	/**
	 * This is called from the code that adds shortcuts from the intent
	 * receiver. This doesn't have a Cursor, but
	 */
//	public ApplicationInfo getApplicationInfo(PackageManager manager, Intent intent, Context context) {
//		return getApplicationInfo(manager, intent, context, null, -1, -1);
//	}

	/**
	 * Make an ShortcutInfo object for a shortcut that isn't an application.
	 */
	@Override
	public ApplicationInfo getShortcutInfo(Cursor c, Context context, int iconTypeIndex, int iconPackageIndex, int iconResourceIndex, int iconIndex, int titleIndex, int itemType) {
		Bitmap icon = null;
		final ApplicationInfo info = new ApplicationInfo();

		info.itemType = itemType;
		info.title = c.getString(titleIndex);
		
		/**
		 * 桌面国际化支持,当加载类型为91快捷时,同我的手机应用一样,实时获取名称
		 * add by wangguomei 2013.11.25
		 */
		if (itemType == LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT) {
			Intent intent = null;
			String intentUri = c.getString(c.getColumnIndexOrThrow(LauncherSettings.Favorites.INTENT));
			try {
				intent = Intent.parseUri(intentUri, 0);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			if (intent != null && intent.getAction() != null) {
				info.title = LauncherMainDockShortcutHelper.get91ShortcutTitleByAction(context,intent.getAction());
			}
			if (StringUtil.isEmpty(info.title)) {
				info.title = c.getString(titleIndex);
			}
		}

		int iconType = c.getInt(iconTypeIndex);
		switch (iconType) {
		case LauncherSettings.Favorites.ICON_TYPE_RESOURCE:
			
			//添加到桌面上时，LauncherIconView.refreshUI会刷新图标（其他应用的快捷除外），因此这里不再获取图标。
			//(情景模式下需要在这里获取自定义图标)
			if (!Global.isOnScene() 
				&& (itemType == LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT || itemType == LauncherSettings.Favorites.ITEM_TYPE_INDEPENDENCE))
				break;
				
			String packageName = c.getString(iconPackageIndex);
			String resourceName = c.getString(iconResourceIndex);
			PackageManager packageManager = context.getPackageManager();
			info.customIcon = false;
			// the resource
			try {
				Resources resources = packageManager.getResourcesForApplication(packageName);
				if (resources != null) {
					final int id = resources.getIdentifier(resourceName, null, null);
					icon = IconUtils.createIconBitmap(resources.getDrawable(id), context);
					if(itemType == LauncherSettings.Favorites.ITEM_TYPE_DRAWER_SHORTCUT){
						info.iconResource = ShortcutIconResource.fromContext(context, id);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//用于情景桌面自定义图标获取
			if(icon == null && Global.isOnScene()){
				Drawable iconDrawable = ThemeManagerFactory.getInstance().getCurrentTheme().getDrawableByKey(resourceName, false, false);
				if(iconDrawable != null){						
					icon = ((BitmapDrawable)iconDrawable).getBitmap();
					info.customIcon = true;
					info.useIconMask = false;
				}
			}
			// the db
			if (icon == null) {
				icon = getIconFromCursor(c, iconIndex);
			}
			// the fallback icon
			if (icon == null) {
				icon = getFallbackIcon();
				info.usingFallbackIcon = true;
			}
			break;
		case LauncherSettings.Favorites.ICON_TYPE_BITMAP:
			icon = getIconFromCursor(c, iconIndex);
			if (icon == null) {
				icon = getFallbackIcon();
				info.customIcon = false;
				info.usingFallbackIcon = true;
			} else {
				info.customIcon = true;
			}
			break;
		default:
			icon = getFallbackIcon();
			info.usingFallbackIcon = true;
			info.customIcon = false;
			break;
		}
		info.iconBitmap = icon;
		return info;
	}


	/**
	 * 是否响应app包更改
	 * @param pkgName
	 * @return
	 */
	@Override
	public boolean isActionOnPkgChanged(String pkgName){
		return !(ThirdApplicationAssit.TTPOD_PACKAGE_NAME.equals(pkgName) || "cn.opda.a.phonoalbumshoushou".equals(pkgName));
	}
	
	@Override
	public void updateOnActionExternalAppAvailable(Context context){
		AppDataFactory.getInstance().fixDrawerApps(context);
	}
	
	@Override
	public void applyThemeNoWallpaperWithWaitDialog(){
		// 去除等待框，解决窗口溢出问题 caizp 2014-9-2
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				ThemeManager.applyThemeWithOutWaitDialog(Global.getApplicationContext(),
						ThemeSharePref.getInstance(Global.getApplicationContext()).getCurrentThemeId(), false, true, false);
			}
		});
	}

	/**
	 * 设置推荐文件夹名称
	 * <p>
	 * Title: setRecommendFolderName
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param folderName
	 * @author maolinnan_350804
	 */
	public static void setRecommendFolderName(final Context context, final String folderName) {
		final Launcher launcher = Global.getLauncher();
		if( launcher == null ){
			return;
		}
		launcher.renameFolder(WorkspaceHelper.getRecommendFolderId(context), folderName);	
		// 刷新桌面的文件夹名称
		launcher.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try{
					int[] info = getRecommendFolderInfo(context);
					if (info == null) {
						return;
					}
					Workspace workspace = launcher.getWorkspace();
					CellLayout cellLayout = workspace.getCellLayoutAt(info[0]);
					IconMaskTextView view = (IconMaskTextView) cellLayout.getChildAt(info[1], info[2]);
					if ( view != null ){
						AnythingInfo anythingInfo = (AnythingInfo) view.getTag();
						if(anythingInfo == null){
							return;
						}
						view.setText(folderName);
						view.invalidate();
						anythingInfo.title = folderName;//改文件名的时候把文件名保存到AnythingInfo中
						view.setTag(anythingInfo);
					}
				}catch (Exception e) {//有异常，就不换名称了。
					e.printStackTrace();
			}
		}});
	}
	
	/**
	 * 获取推荐文件夹的Screen,cellX,cellY
	 * <p>
	 * Title: getRecommendFolderInfo
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param context
	 * @return
	 * @author maolinnan_350804
	 */
	private static int[] getRecommendFolderInfo(Context context) {
		Cursor c = null;
		try {
			ContentResolver cr = context.getContentResolver();
			c = cr.query(
					LauncherSettings.Favorites.getContentUri(),
					null,
					"container=? and itemType=? and iconType=?",
					new String[] { String.valueOf(LauncherSettings.Favorites.CONTAINER_DESKTOP),
							String.valueOf(LauncherSettings.Favorites.ITEM_TYPE_ANYTHING), String.valueOf(AnythingInfo.FLAG_FOLDER_RECOMMEND_APPS) },
					null);
			final int screenIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.SCREEN);
			final int cellXIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CELLX);
			final int cellYIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.CELLY);
			while (c.moveToNext()) {
				int[] result = new int[3];
				result[0] = c.getInt(screenIndex);
				result[1] = c.getInt(cellXIndex);
				result[2] = c.getInt(cellYIndex);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if ( c != null ) {
				c.close();
			}
		}
		return null;
	}
}
