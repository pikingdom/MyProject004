package com.nd.hilauncherdev.launcher;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.data.widget.LauncherWidgetInfo;
import com.nd.hilauncherdev.folder.model.FolderEncriptHelper;
import com.nd.hilauncherdev.framework.view.bubble.LauncherBubbleManager;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.BaseBitmapUtils;
import com.nd.hilauncherdev.kitset.util.BitmapUtils;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.ConfigFactory;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.info.ItemInfo;
import com.nd.hilauncherdev.launcher.info.PandaWidgetInfo;
import com.nd.hilauncherdev.launcher.info.WidgetInfo;
import com.nd.hilauncherdev.launcher.model.BaseLauncherModel;
import com.nd.hilauncherdev.launcher.model.BaseLauncherSettings;
import com.nd.hilauncherdev.launcher.model.load.LauncherBinder;
import com.nd.hilauncherdev.launcher.screens.CellLayout;
import com.nd.hilauncherdev.launcher.screens.ScreenViewGroup;
import com.nd.hilauncherdev.launcher.screens.dockbar.BaseMagicDockbar;
import com.nd.hilauncherdev.launcher.view.icon.ui.folder.FolderIconTextView;
import com.nd.hilauncherdev.launcher.view.icon.ui.impl.IconMaskTextView;
import com.nd.hilauncherdev.push.PushManager;
import com.nd.hilauncherdev.scene.Cushion;
import com.nd.hilauncherdev.scene.Hole;
import com.nd.hilauncherdev.scene.Scene;
import com.nd.hilauncherdev.scene.SceneCellLayout;
import com.nd.hilauncherdev.scene.SceneManager;
import com.nd.hilauncherdev.scene.data.SceneDatabase;
import com.nd.hilauncherdev.shop.api6.model.GameThemeRecommendAppBean;
import com.nd.hilauncherdev.widget.variety.VarietyWidget;

/**
 * <br>
 * Description: Workspace操作 <br>
 * Author:caizp <br>
 * Date:2012-6-26上午11:49:47
 */
public class WorkspaceHelper {

	private Launcher mLauncher;
	private Workspace mWorkspace;

	public WorkspaceHelper(Launcher launcher) {
		mLauncher = launcher;
		if (null != mLauncher) {
			mWorkspace = launcher.getWorkspace();
		}
	}

	/**
	 * <br>
	 * Description: 往Workspace末端添加空白屏 <br>
	 * Author:caizp <br>
	 * Date:2012-6-26上午11:54:01
	 * 
	 * @return
	 */
	public CellLayout createScreenToWorkSpace() {
		CellLayout cell;
		if(!Global.isOnScene()){	
			cell = new CellLayout(mLauncher);
		}else{
			cell = new SceneCellLayout(mLauncher);
		}
		cell.setWorkspace(mWorkspace);
		cell.setCountXY();
		mWorkspace.addView(cell);
		cell.setCellLayoutLocation(mWorkspace.getChildCount() - 1);
		
		cell.setOnLongClickListener(mWorkspace.getOnLongClickListener());
		
		if (null != mWorkspace.getLightBar()) {
			 mWorkspace.getLightBar().setSize(mWorkspace.getChildCount());
			((View)mWorkspace.getLightBar()).postInvalidate();
			// mWorkspace.getCommonLightBar().refresh(mWorkspace.getChildCount(), mWorkspace.getCurrentScreen());
		}
		
		cell.destroyHardwareLayer();
		if(Global.isOnScene()){			
			updateSence(mWorkspace.getChildCount() - 1, true);
			mWorkspace.resetCellLayoutStuffOnScreenChange();
		}else{
			ConfigFactory.saveScreenCount(mLauncher, mWorkspace.getChildCount());
		}
		return cell;
	}

	/**
	 * <br>
	 * Description: 移除指定屏幕同时更新相关屏幕内容 <br>
	 * Author:caizp <br>
	 * Date:2012-6-26下午02:41:29
	 * 
	 * @param screenIndex
	 */
	public void removeScreenFromWorkspace(final int screenIndex) {
		final CellLayout layout = (CellLayout) mWorkspace.getChildAt(screenIndex);
		final boolean isLastScreen = screenIndex == mWorkspace.getChildCount() - 1 ;

		mWorkspace.removeViewAt(screenIndex);
		layout.destroyHardwareLayer();
		
		if (null != mWorkspace.getLightBar()) {
			 mWorkspace.getLightBar().setSize(mWorkspace.getChildCount());
			((View)mWorkspace.getLightBar()).postInvalidate();
			// mWorkspace.getCommonLightBar().refresh(mWorkspace.getChildCount(), mWorkspace.getCurrentScreen());
		}
		if(Global.isOnScene()){	
			updateSence(screenIndex, false);
			mWorkspace.invalidate();
//			mLauncher.setupDeleteZone();
			for (int i = 0; i < layout.getChildCount(); i++) {
				removeItemFromWorkspace(layout.getChildAt(i).getTag(), layout.getChildAt(i));
			}
			if (!isLastScreen) {
				final int count = mWorkspace.getChildCount();
				moveScreenPosition(screenIndex, count);
				resetItemScreen(screenIndex, count,true);
			}
		}else{
			ConfigFactory.saveScreenCount(mLauncher, mWorkspace.getChildCount());
			mWorkspace.invalidate();
//			mLauncher.setupDeleteZone();
			// 异步删除数据
			ThreadUtil.executeMore(new Runnable() {
				@Override
				public void run() {
					int viewCount = layout.getChildCount();
					for (int i = 0; i < viewCount; i++) {
						removeItemFromWorkspace(layout.getChildAt(i).getTag(), layout.getChildAt(i));
						mLauncher.ifNeedClearCache(layout.getChildAt(i));
					}
					if(viewCount > 0){					
						mLauncher.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								layout.removeAllViewsInLayout();
							}
						});
					}
					if (!isLastScreen) {
						final int count = mWorkspace.getChildCount();
						moveScreenPosition(screenIndex, count);
						resetItemScreen(screenIndex, count,true);
					}
				}
			});
		}

	}
	
	/**
	 * 删除Workspace数据
	 * @param dragInfo
	 * @param v
	 */
	public void removeItemFromWorkspace(Object dragInfo, View v){
		Launcher launcher = (Launcher)mLauncher;
		final ItemInfo item = (ItemInfo) dragInfo;
		if (item.container == -1)
			return;
		
		if (item.container == LauncherSettings.Favorites.CONTAINER_DESKTOP) {
			if(item instanceof WidgetInfo) {
				LauncherWidgetHelper.deleteWidgetFromWorkspace(launcher, item, v);
			}
		}
		
		if (item instanceof FolderInfo) {
			final FolderInfo userFolderInfo = (FolderInfo) item;
			LauncherModel.deleteUserFolderContentsFromDatabase(launcher, userFolderInfo);
			FolderEncriptHelper.getNewInstance().clearEncript(userFolderInfo.id,FolderIconTextView.OPEN_FOLDER_FROM_LAUNCHER);
		}
		LauncherModel.deleteItemFromDatabase(launcher, item);
		if (item.itemType == LauncherSettings.Favorites.ITEM_TYPE_WIDGET_VARIETY) {
			VarietyWidget.processDeleted(item);
		}
	}

	/**
	 * Description: 更新情景桌面信息
	 * Author: guojy
	 * Date: 2013-8-3 下午6:43:23
	 */
	private void updateSence(int screen, boolean isAdd){
		if(!Global.isOnScene())
			return;

		Scene scene = SceneManager.getInstance(mLauncher).getCurrentScene();
		scene.screenCount = mWorkspace.getChildCount();
		scene.saveScene();
		
		if(isAdd){			
			List<Hole> list = SceneManager.getInstance(mLauncher).getScreenDefaultHoleList(screen);
			scene.addHoles(list);
			scene.saveHole();
		}else{
			scene.deleteHoles(LauncherSettings.Favorites.CONTAINER_DESKTOP, screen);
			scene.deleteCushion(LauncherSettings.Favorites.CONTAINER_DESKTOP, screen);
		}
		
	}
	
	/**
	 * Description: 将屏幕编号为screenStart之后，screenEnd之前的屏幕往前移一屏 Author: guojy Date:
	 * 2012-7-31 下午04:25:39
	 */
	private void moveScreenPosition(int screenStart, int screenEnd) {
		// 获取数据库对象
		SQLiteDatabase launcherDB = new LauncherProvider.DatabaseHelper(mLauncher).getWritableDatabase();
		final String screenField = LauncherSettings.Favorites.SCREEN;
		final String container = LauncherSettings.Favorites.CONTAINER;
		final int container_desktop = LauncherSettings.Favorites.CONTAINER_DESKTOP;
		String sql = "";
		String tableName = LauncherProvider.TABLE_FAVORITES;
		try {
			if (screenStart < screenEnd) {
				// 起始屏位置小于目的屏位置，从起始屏之后到目的屏都往前移一屏，再将起始屏移到目的屏
				// update favorites set screen=screen-1 where screen>screenStart
				// and screen<=screenEnd
				sql = "update "+tableName+" set " + screenField + "=" + screenField + "-1 where " + screenField + ">" + screenStart + " and " + screenField + "<=" + screenEnd
					+ " and " + container + " =" + container_desktop;
			} else {
				// 起始屏位置大于目的屏位置，从目的屏到起始屏之前的屏都往后移一屏，再将起始屏移到目的屏
				// update favorites set screen=screen+1 where screen>=screenEnd
				// and screen<screenStart
				sql = "update "+tableName+" set " + screenField + "=" + screenField + "+1 where " + screenField + ">=" + screenEnd + " and " + screenField + "<" + screenStart
					+ " and " + container + " =" + container_desktop;
			}
			
			if(Global.isOnScene()){
				Scene scene = SceneManager.getInstance(mLauncher).getCurrentScene();
				sql += " and " + SceneDatabase.FAVORITES_TABLE_SCENE_ID + "='" + scene.sceneId +"'";
			}
			launcherDB.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != launcherDB) {
				launcherDB.close();
			}
		}
		
		if(Global.isOnScene()){
			Scene scene = SceneManager.getInstance(mLauncher).getCurrentScene();
			Map<Integer, List<Cushion>> cushionMap = scene.getCushions(LauncherSettings.Favorites.CONTAINER_DESKTOP);
			Map<Integer, List<Hole>> holeMap = scene.getHoles(LauncherSettings.Favorites.CONTAINER_DESKTOP);
			
			List<Hole> startHoleList = holeMap.get(screenStart);
			List<Cushion> startCushionList = cushionMap.get(screenStart);
			if(screenStart < screenEnd){
				for(int i = screenStart +1; i <= screenEnd; i ++){
					holeMap.put(i - 1, holeMap.get(i));
					cushionMap.put(i - 1, cushionMap.get(i));
				}
			}else{
				for(int i = screenStart -1; i >= screenEnd; i --){
					holeMap.put(i + 1, holeMap.get(i));
					cushionMap.put(i + 1, cushionMap.get(i));
				}
			}
			holeMap.put(screenEnd, startHoleList);
			cushionMap.put(screenEnd, startCushionList);
			
			scene.saveHole();
			scene.saveCushion();
		}
	}

	/**
	 * <br>
	 * Description: 屏幕上的数据项移动 <br>
	 * Author:caizp <br>
	 * Date:2012-6-26下午03:06:50
	 * 
	 * @param screenStart
	 *            开始屏
	 * @param screenEnd
	 *            目的屏
	 */
	public void moveItemPositions(int screenStart, int screenEnd) {
		// 获取屏
		CellLayout startLayout = (CellLayout) mWorkspace.getChildAt(screenStart);

		// 屏上的元素个数
		int childCount = startLayout.getChildCount();
		// 将开始屏数据取出,并将屏幕设置为目的屏，放入集合
		List<ItemInfo> itemList = new ArrayList<ItemInfo>();
		for (int j = 0; j < childCount; j++) {
			final View view = startLayout.getChildAt(j);
			Object tag = view.getTag();
			if (tag == null || !(tag instanceof ItemInfo)) continue ;
			final ItemInfo item = (ItemInfo) tag;
			item.screen = screenEnd;// 屏位置设置成目的屏
			itemList.add(item);
		}
		
		moveScreenPosition(screenStart, screenEnd);
		boolean isAsc ;
		if (screenStart < screenEnd) {
			screenStart++ ;
			isAsc = true ;
		} else {
			screenStart-- ;
			isAsc = false ;
		}

		resetItemScreen(screenStart, screenEnd,isAsc);

		// 将修改到目的屏的数据更新到数据库
		for (ItemInfo item : itemList) {
			if (item != null) {
				item.container = item.container == ItemInfo.NO_ID ? LauncherSettings.Favorites.CONTAINER_DESKTOP : item.container;
				LauncherModel.moveItemInDatabase(mLauncher, item);
			}
		}
		itemList.clear();
	}
	
	/**
	 * <p>桌面删除某一屏后，重置后面屏幕的screen属性</p>
	 * 
	 * <p>date: 2012-9-27 上午11:41:13
	 * @author pdw
	 * @param startScreen
	 * @param screen
	 * @param isAsc 是否从小屏下标拖动到大屏下标
	 */
	private void resetItemScreen(int startScreen,int endScreen,boolean isAsc) {
		int start ;
		int end ;
		if (startScreen < endScreen) {
			start = startScreen ;
			end = endScreen ;
		} else {
			start = endScreen ;
			end = startScreen ;
		}
		int childCount = 0 ;
		for (; start <= end ; start++) {
			final CellLayout cellLayout = (CellLayout) mWorkspace.getChildAt(start);
			if (cellLayout == null) continue ;
			childCount = cellLayout.getChildCount();
			for (int j = 0; j < childCount; j++) {
				final View view = cellLayout.getChildAt(j);
				Object tag = view.getTag();
				if (tag == null || !(tag instanceof ItemInfo)) continue ;
				final ItemInfo item = (ItemInfo) tag;
				if (isAsc) {
					item.screen--;
				} else {
					item.screen++;
				}
			}
		}
	}

	/**
	 * 删除推荐应用文件夹中多余的、未安装推荐应用
	 * @return 删除后文件夹内的图标数量
	 * @throws URISyntaxException 
	 */
	/*
	private static void removeSurplusRecommandAppInFolder(Context context,
			                                                     ContentResolver cr,
			                                                     int folderId,
			                                                     List<ApplicationInfo> newAppInfos) throws URISyntaxException {
		Cursor c = null;
		try {
			c = cr.query(LauncherSettings.Favorites.getContentUri(), null, "container=?", new String[] { String.valueOf(folderId) }, null);
			
			List<Integer> uninstalledRecommandApps = new ArrayList<Integer>();
			String packageName;
			final int intentIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.INTENT);
			final int appIdIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites._ID);
			while (c.moveToNext()) {
				String intentDescription = c.getString(intentIndex);
				if (intentDescription == null)
					continue;
				Intent intent = Intent.parseUri(intentDescription, 0);
				if (intent.getComponent() == null || (packageName = intent.getComponent().getPackageName()) == null)
					continue;
				
				//查找文件夹中是否存在与新推荐应用列表中相同的，且未安装的推荐应用，如果有，则将其从列表中删除
				if ((ThemeRecomendApp.getInstance().isRecommendIncludeDb(context, packageName) || LauncherRecommendApps.getInstance(context).isRecommendApps(packageName))
					&& !AndroidPackageUtils.isPkgInstalled(context, packageName)) {
					int app_id = c.getInt(appIdIndex);
					uninstalledRecommandApps.add(app_id);
					removeDuplicatedRecommandApp(context, packageName, newAppInfos);
				}
			}
			
			//将新推荐应用加入文件夹后，文件夹内未安装推荐应用的总数可能超过限定值，因此，将多余的删除
			int toRemoveSize = uninstalledRecommandApps.size() + newAppInfos.size() - MAX_UNINSTALLED_RECOMMAND_APP_IN_FOLDER;
			if (toRemoveSize > 0 && toRemoveSize <= uninstalledRecommandApps.size()) {
				for (int i=0; i<toRemoveSize; i++) {
					cr.delete(LauncherSettings.Favorites.getContentUri(uninstalledRecommandApps.get(i), false), null, null);
				}
			}
		} finally {
			if (null != c) {
				c.close();
			}
		}
	}*/
	
	/**
	 * 查看新的推荐应用列表中是否含有包名为packageName的应用，如果有，则将其从列表中删除
	 */
	/*
	private static void removeDuplicatedRecommandApp(Context context, String packageName, List<ApplicationInfo> newAppInfos) {
		Iterator<ApplicationInfo> it = newAppInfos.iterator();
		ApplicationInfo info;
		while (it.hasNext()) {
			info = it.next();
			if (info.componentName != null
				&& info.componentName.getPackageName() != null
				&& packageName.equals(info.componentName.getPackageName())) {
				
				ThemeRecomendApp.getInstance().saveRecommendApp(context, packageName, false);
				it.remove();
				return;
			}
		}
	}
	*/
	/**
	 * 添加新推荐app到桌面数据库
	 */
	/*
	private static void addRecommandAppInDB(Context context, 
			                                     List<ApplicationInfo> appInfos,
			                                     int folder_id,
			                                     int[] wh) {
		RecommendAppTable.resetIsLastAddedOfAll(context);
		
		for (ApplicationInfo item : appInfos) {
			item.spanX = wh[0];
			item.spanY = wh[1];
			LauncherModel.addOrMoveItemInDatabase(context, item, folder_id);
						
			String packageName = item.componentName.getPackageName();
			ThemeRecomendApp.getInstance().saveRecommendApp(context, packageName, true);
		}
	}
	*/
	/**
	 * 获取桌面上的主题推荐文件夹
	 * @return 返回文件夹的id，如果不存在，则返回 INVALID_FOLDER_ID
	 */
	/*
	private static int getThemeAppFolderOnWorkspace(ContentResolver cr) {
		return getFolderOnWorkspaceByType(cr, LauncherSettings.Favorites.ITEM_TYPE_THEME_APP_FOLDER);
	}
	*/
	/**
	 * v5.5.1获取推荐文件夹id
	 * <p>
	 * Title: isRecommendFolderExist
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @return
	 * @author maolinnan_350804
	 */
	public static int getRecommendFolderId(Context context) {
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
			int idIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites._ID);
			while (c.moveToNext()) {
				return c.getInt(idIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if ( c != null){
				c.close();
			}
		}
		return -1;
	}
	
	/**
	 * 获取桌面图标数
	 * <p>Title: getLauncherIconCount</p>
	 * <p>Description: </p>
	 * @param context
	 * @return
	 * @author maolinnan_350804
	 */
/*	public static int getLauncherIconCount(Context context){
		Cursor c = null;
		try {
		ContentResolver contentResolver = Global.getApplicationContext().getContentResolver();
		c = contentResolver.query(LauncherSettings.Favorites.getContentUri(),
						new String[]{LauncherSettings.BaseLauncherColumns.ITEM_TYPE, LauncherSettings.Favorites.SCREEN}, 
						LauncherSettings.Favorites.CONTAINER + "=" + LauncherSettings.Favorites.CONTAINER_DESKTOP
						+ " AND (" + LauncherSettings.BaseLauncherColumns.ITEM_TYPE+ "=" + LauncherSettings.BaseLauncherColumns.ITEM_TYPE_APPLICATION
						+ " OR " + LauncherSettings.BaseLauncherColumns.ITEM_TYPE+ "=" + LauncherSettings.BaseLauncherColumns.ITEM_TYPE_SHORTCUT
						+ " OR " + LauncherSettings.BaseLauncherColumns.ITEM_TYPE+ "=" + LauncherSettings.Favorites.ITEM_TYPE_ANYTHING 
						+ " OR " + LauncherSettings.BaseLauncherColumns.ITEM_TYPE+ "=" + LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER
						+ " OR " + LauncherSettings.BaseLauncherColumns.ITEM_TYPE+ "=" + LauncherSettings.Favorites.ITEM_TYPE_HI_APPLICATION
						+ " OR " + LauncherSettings.BaseLauncherColumns.ITEM_TYPE+ "=" + LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT +")", 
						null, null);
		return c.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if ( c != null){
				c.close();
			}
		}
		return -1;
	}*/

	/**
	 * 获取桌面上的默认推荐文件夹
	 * 
	 * @return 返回文件夹的id，如果不存在，则返回 INVALID_FOLDER_ID
	 */
/*	public static int getLauncherRecommendFolderOnWorkspace(ContentResolver cr) {
		return getFolderOnWorkspaceByType(cr, LauncherSettings.Favorites.ITEM_TYPE_LAUNCHER_RECOMMEND_FOLDER);
	}
	*//**
	 * 根据itemType获取桌面上的文件夹的id
	 *//*
	private static int getFolderOnWorkspaceByType(ContentResolver cr, int type) {
		int folderId = INVALID_FOLDER_ID;
		Cursor c = null;
		try {
			c = cr.query(LauncherSettings.Favorites.getContentUri(), null, "itemType=?", new String[] { String.valueOf(type) }, null);
			if (c != null && c.moveToFirst()) {
				folderId = c.getInt(c.getColumnIndexOrThrow(LauncherSettings.Favorites._ID));
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if (null != c) {
				c.close();
			}
		}
		
		return folderId;
	}*/
	/**
	 * 在桌面上增加主题推荐文件夹
	 * @return 新增加的文件夹的id，如果增加失败，则返回 INVALID_FOLDER_ID
	 */
	/*
	private static int addThemeAppFolderOnWorkspace(Context context, String title, List<ApplicationInfo> appInfos) {
		int newFolderId = INVALID_FOLDER_ID;
		try {
			Launcher launcher = Global.getLauncher();
			if (launcher == null)
				return newFolderId;
			
			SQLiteDatabase db = new LauncherProvider.DatabaseHelper(context).getWritableDatabase();
			int[] psInfo = LauncherProviderHelper.getVacantCellFromBottom(db, 1, 1, false);
			db.close();
			if (psInfo == null) {
				Log.w("WorkspaceHelper", "no position for Recommand folder");
				return newFolderId;
			}
		
			FolderInfo folderInfo = new FolderInfo();
			folderInfo.title = StringUtil.isEmpty(title) ? context.getString(R.string.theme_recommand_folder_name) : title;
			folderInfo.itemType = LauncherSettings.Favorites.ITEM_TYPE_THEME_APP_FOLDER;
			folderInfo.container = LauncherSettings.Favorites.CONTAINER_DESKTOP;
			folderInfo.screen = psInfo[0];
			folderInfo.cellX = psInfo[1];
			folderInfo.cellY = psInfo[2];
			folderInfo.spanX = 1;
			folderInfo.spanY = 1;
			LauncherModel.addItemToDatabase(context, folderInfo, false);
			newFolderId = (int) folderInfo.id;
			folderInfo.contents.addAll(appInfos);
			launcher.bindRecommendApps(folderInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newFolderId;
	}
	 */
// --Commented out by Inspection START (2015/12/10 14:33):
//	/**
//	 * 在桌面上增加推荐文件夹
//	 *
//	 * @return 新增加的文件夹的id，如果增加失败，则返回 INVALID_FOLDER_ID
//	 */
////	private static int addRecommendAppFolderOnWorkspace(Context context) {
////		int newFolderId = INVALID_FOLDER_ID;
////		try {
////			Launcher launcher = (Launcher) Global.getLauncher();
////			if (launcher == null)
////				return newFolderId;
////
////			int[] psInfo = getVacantCellFromBottom();
////			if (psInfo == null) {
////				Log.w("WorkspaceHelper", "no position for Recommand folder");
////				return newFolderId;
////			}
////			AnythingInfo info = new AnythingInfo();
////			info.itemType = LauncherSettings.Favorites.ITEM_TYPE_ANYTHING;
////			info.flag = AnythingInfo.FLAG_FOLDER_RECOMMEND_APPS;
////			info.container = LauncherSettings.Favorites.CONTAINER_DESKTOP;
////			info.screen = psInfo[0];
////			info.cellX = psInfo[1];
////			info.cellY = psInfo[2];
////			info.spanX = 1;
////			info.spanY = 1;
////			LauncherModel.addItemToDatabase(context, info, false);
////			newFolderId = (int) info.id;
////			launcher.bindAllRecommendApps(info);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////
////		return newFolderId;
////	}
//
//// --Commented out by Inspection START (2015/12/10 14:32):
////	/**
////	 * 删除文件夹内未安装的应用
////	 *
////	 * @throws URISyntaxException
////	 */
////	/*
////	private static void removeUnInstalledAppInFolder(Context context, ContentResolver cr, int folderId) throws URISyntaxException {
////		Cursor c = null;
////		try {
////			c = cr.query(LauncherSettings.Favorites.getContentUri(), null, "container=?", new String[] { String.valueOf(folderId) }, null);
////			final int intentIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.INTENT);
////			final int appIdIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites._ID);
////			while (c.moveToNext()) {
////				String intentDescription = c.getString(intentIndex);
////				if (intentDescription == null)
////					continue;
////				Intent intent = Intent.parseUri(intentDescription, 0);
////				if (intent.getComponent() == null || intent.getComponent().getPackageName() == null)
////					continue;
////
////				if (!AndroidPackageUtils.isPkgInstalled(context, intent.getComponent().getPackageName())) {
////					int app_id = c.getInt(appIdIndex);
////					cr.delete(LauncherSettings.Favorites.getContentUri(app_id, false), null, null);
////				}
////			}
////		} finally {
////			if (null != c) {
////				c.close();
////			}
////		}
////	}
////	*/
////	private static final int MAX_UNINSTALLED_RECOMMAND_APP_IN_FOLDER = 8;
//// --Commented out by Inspection STOP (2015/12/10 14:32)
//	private static final int INVALID_FOLDER_ID = -1;
// --Commented out by Inspection STOP (2015/12/10 14:33)
	/**
	 * 游戏主题推荐APP
	 * @author maolinnan
	 */
	public static void changeRecommandAppForTheme(final Context context, String themeId) {
		if (context == null || Global.getLauncher() == null)
			return;
		//删除游戏主题推荐的游戏的图标
		deleteGameThemeRecommendApp(context);
		//假如是游戏主题，则添加游戏图标
		String gameInfoPath = Global.THEME_DIR + themeId.replace(" ", "_") + "/gametheme";
		final GameThemeRecommendAppBean gameThemeRecommendAppBean = GameThemeRecommendAppBean.getLocalGameThemeRecommendAppBean(gameInfoPath);
		if(gameThemeRecommendAppBean == null){
			return;
		}
		if(AndroidPackageUtils.isPkgInstalled(context,gameThemeRecommendAppBean.packageName)){
			return;
		}
		int[] psInfo = getVacantCellFromBottom();
		final ApplicationInfo info = new ApplicationInfo();
		info.itemType = LauncherSettings.Favorites.ITEM_TYPE_GAME_THEME_RECOMMEND_APP;
		info.container = LauncherSettings.Favorites.CONTAINER_DESKTOP;
		info.screen = psInfo[0];
		info.cellX = psInfo[1];
		info.cellY = psInfo[2];
		info.spanX = 1;
		info.spanY = 1;
		info.componentName = new ComponentName(gameThemeRecommendAppBean.packageName,gameThemeRecommendAppBean.mainActivity);
		Intent inte = new Intent();
		inte.setComponent(info.componentName);
		inte.addCategory(Intent.CATEGORY_LAUNCHER);
		inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		info.intent = inte;
		info.iconBitmap = BaseBitmapUtils.createIconBitmapFor91Icon(gameThemeRecommendAppBean.cacheIcon,context);
		info.title = gameThemeRecommendAppBean.appName;
		info.useIconMask = true;
		//添加到数据库
		LauncherModel.addItemToDatabase(context, info, false);
		Global.getLauncher().runOnUiThread(new Runnable() {//添加图标操作要放到主线程中
			@Override
			public void run() {
				//添加到界面中
				View view = Global.getLauncher().createCommonAppView(info);
				Global.getLauncher().getWorkspace().addInScreen(view,info.screen,info.cellX,info.cellY,info.spanX,info.spanY);
			}
		});
	}

	/**
	 * 删除游戏主题推荐App
	 * @param context
	 * @return
	 */
	public static void deleteGameThemeRecommendApp(Context context) {
		Cursor c = null;
		try {
			ContentResolver cr = context.getContentResolver();
			c = cr.query(LauncherSettings.Favorites.getContentUri(),null,"itemType=?",
					new String[]{String.valueOf(LauncherSettings.Favorites.ITEM_TYPE_GAME_THEME_RECOMMEND_APP)},null);
			while (c.moveToNext()) {
				int idIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites._ID);
				int intentIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.INTENT);
				Intent intent = Intent.parseUri(c.getString(intentIndex), 0);
				int id = c.getInt(idIndex);
				if(intent.getComponent() != null && AndroidPackageUtils.isPkgInstalled(context,intent.getComponent().getPackageName())){
					//将游戏主题推荐类型升级成应用类型
					ContentValues values = new ContentValues();
					values.put(LauncherSettings.Favorites.ITEM_TYPE,LauncherSettings.Favorites.ITEM_TYPE_APPLICATION);
					cr.update(LauncherSettings.Favorites.getContentUri(),values,"_id=?", new String[]{String.valueOf(id)});
				} else{
					Global.getLauncher().getDockbar().removeInDockbar(intent.getComponent().getPackageName());
					LauncherBinder.removeAppsInWorkspace(intent.getComponent().getPackageName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if ( c != null){
				c.close();
			}
		}
	}

	public static PandaWidgetInfo transformToPandaWidgetInfo(BaseLauncher mLauncher,LauncherWidgetInfo widgetInfo) {
		PandaWidgetInfo pandaWidgetInfo = new PandaWidgetInfo();
		pandaWidgetInfo.appWidgetId = mLauncher.getAppWidgetHost().allocateAppWidgetId();
		pandaWidgetInfo.spanX = widgetInfo.spanX;
		pandaWidgetInfo.spanY = widgetInfo.spanY;
		pandaWidgetInfo.cellX = widgetInfo.cellX;
		pandaWidgetInfo.cellY = widgetInfo.cellY;
		pandaWidgetInfo.itemType = widgetInfo.itemType;
		pandaWidgetInfo.layoutResString = widgetInfo.layoutResName;
		pandaWidgetInfo.pandaWidgetPackage = widgetInfo.packageName;
		return pandaWidgetInfo;
	}
	
	public static int[] getVacantCellFromBottom(){
		SQLiteDatabase db = null;
		try {
			db = new LauncherProvider.DatabaseHelper(Global.getApplicationContext()).getWritableDatabase();
			return LauncherProviderHelper.getVacantCellFromBottom(db, 1, 1, false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
		return null;
	}

	public static int[] getVacantCellFromTop(){
		SQLiteDatabase db = null;
		try {
			db = new LauncherProvider.DatabaseHelper(Global.getApplicationContext()).getWritableDatabase();
			return LauncherProviderHelper.getVacantCellFromTop(db, 1, 1, false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
		return null;
	}

	public static void removeAppsInWorkspaceByIconResource(final String targetIconResource) {
		try{
			LauncherBinder.removeAppsInWorkspace(targetIconResource);
			Global.getLauncher().getDockbar().removeInDockbar(targetIconResource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isExistUpgradeFolder() {
		if (Global.getLauncher() == null)
			return false;
		boolean result = false;
		Cursor c = null;
		try {
			final ContentResolver cr = Global.getLauncher().getContentResolver();
			c = cr.query(BaseLauncherSettings.Favorites.getContentUri(), new String[] { "itemType"}, 
					"itemType=?", new String[] {Integer.valueOf(LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER).toString()}, null);
			result = c.moveToFirst();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			c.close();
		}
		return result;
	}
	
	/**
	 * Description: 返回不在文件夹中的app，用于无匣子桌面文件夹中添加app时使用
	 * Author: guojianyun_dian91 
	 * Date: 2015年11月12日 下午5:22:57
	 * @return
	 */
	public static List<ApplicationInfo> getAllAppsNotInFolder(){
		if (Global.getLauncher() == null)
			return null;
		List<ApplicationInfo> apps = new ArrayList<ApplicationInfo>();
		Workspace wk = Global.getLauncher().getWorkspace();
		int count = wk.getChildCount();
		for (int i = 0; i < count; i++) {
			CellLayout cl = wk.getCellLayoutAt(i);
			int childCount = cl.getChildCount();
			for (int j = 0; j < childCount; j++) {
				View childView = cl.getChildAt(j);
				if (childView instanceof IconMaskTextView && childView.getTag() != null && childView.getTag() instanceof ApplicationInfo) {
					ApplicationInfo info = (ApplicationInfo) childView.getTag();
					if(info.itemType == LauncherSettings.Favorites.ITEM_TYPE_APPLICATION
							|| info.itemType == LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT
							|| info.itemType == LauncherSettings.Favorites.ITEM_TYPE_HI_APPLICATION){
						// 文件夹内点加号添加时，屏蔽快捷方式 caizp 2017-03-22
						if((LauncherBranchController.isFanYue() || LauncherBranchController.isShenLong()) && info.itemType == LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT) {
							continue;
						}
						if(info.itemType == LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT &&
								!TextUtils.isEmpty(info.title) && info.title.toString().startsWith("Ⅱ·"))
							continue;
						apps.add(info);
					}
				}
			}
		}
		BaseMagicDockbar dockbar = Global.getLauncher().getDockbar();
		ViewGroup dockCellLayout = (ViewGroup) dockbar.getChildAt(0);
		count = dockCellLayout.getChildCount();
		for (int i = 0; i < count; i++) {
			View childView = dockCellLayout.getChildAt(i);
			if ( childView.getTag() != null && childView.getTag() instanceof ApplicationInfo) {
				ApplicationInfo info = (ApplicationInfo) childView.getTag();
				if(info.itemType == LauncherSettings.Favorites.ITEM_TYPE_APPLICATION
						|| info.itemType == LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT
						|| info.itemType == LauncherSettings.Favorites.ITEM_TYPE_HI_APPLICATION){
					apps.add(info);
				}
			}
		}
		return apps;
		
	}

	public static void addAiTaobaoInWorkspace(Context ctx) {
		try {
			int[] psInfo = PushManager.getInstance().getPushSDKAdapter().getWorkspaceVacantCellFromTop();
			if (psInfo == null)
				return;
			final ApplicationInfo info = new ApplicationInfo();
			info.title = ctx.getString(R.string.taobao_title);
			info.itemType = BaseLauncherSettings.Favorites.ITEM_TYPE_SHORTCUT;
			String intent = LauncherProviderHelper.AITAOBAO_INTENT;
			info.intent = Intent.parseUri(intent, 0);
			info.customIcon = true;
			info.iconBitmap = BitmapUtils.drawable2Bitmap(ctx.getResources().getDrawable(R.drawable.drawer_custom_itaobao));
			info.container = BaseLauncherSettings.Favorites.CONTAINER_DESKTOP;
			info.screen = psInfo[0];
			info.cellX = psInfo[1];
			info.cellY = psInfo[2];
			info.spanX = 1;
			info.spanY = 1;
			BaseLauncherModel.addItemToDatabase(BaseConfig.getApplicationContext(), info, false);
			final ScreenViewGroup wk = BaseConfig.getBaseLauncher().getScreenViewGroup();
			wk.post(new Runnable() {
				@Override
				public void run() {
					View view = LauncherViewHelper.createPushIconTextView((ApplicationInfo) info);
					wk.addInScreen(view, info.screen, info.cellX, info.cellY, info.spanX, info.spanY);
					wk.getCellLayoutAt(info.screen).invalidate();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
