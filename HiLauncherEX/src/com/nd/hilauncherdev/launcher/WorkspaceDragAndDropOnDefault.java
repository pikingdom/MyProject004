package com.nd.hilauncherdev.launcher;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.analysis.integral.IntegralSubmitUtil;
import com.nd.hilauncherdev.analysis.integral.IntegralTaskIdContent;
import com.nd.hilauncherdev.app.CustomIntent;
import com.nd.hilauncherdev.app.ui.view.icontype.LauncherSystemShortcutIconType;
import com.nd.hilauncherdev.app.view.AppDrawerIconMaskTextView;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.UpgradeFolderInfo;
import com.nd.hilauncherdev.drawer.data.widget.LauncherItemInfo;
import com.nd.hilauncherdev.drawer.data.widget.LauncherWidgetInfo;
import com.nd.hilauncherdev.drawer.view.DrawerMainView;
import com.nd.hilauncherdev.drawer.view.DrawerSlidingView;
import com.nd.hilauncherdev.folder.model.FolderHelper;
import com.nd.hilauncherdev.framework.AnyCallbacks.OnDragEventCallback;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.ControlledAnalyticsUtil;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.SystemUtil;
import com.nd.hilauncherdev.launcher.LauncherSettings.Favorites;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.info.ItemInfo;
import com.nd.hilauncherdev.launcher.info.PandaWidgetInfo;
import com.nd.hilauncherdev.launcher.screens.CellLayout;
import com.nd.hilauncherdev.launcher.touch.BaseDragController;
import com.nd.hilauncherdev.launcher.touch.DragSource;
import com.nd.hilauncherdev.launcher.touch.WorkspaceDragAndDropImpl;
import com.nd.hilauncherdev.launcher.view.DragView;
import com.nd.hilauncherdev.launcher.view.icon.icontype.IconType;
import com.nd.hilauncherdev.launcher.view.icon.ui.LauncherIconView;
import com.nd.hilauncherdev.launcher.view.icon.ui.folder.FolderIconTextView;
import com.nd.hilauncherdev.launcher.view.icon.ui.impl.IconMaskTextView;

/**
 * Description: 处理默认桌面上Workspace的拖放
 * Author: guojy
 * Date: 2013-9-3 下午3:43:54
 */
public class WorkspaceDragAndDropOnDefault extends WorkspaceDragAndDropImpl{
	
	public WorkspaceDragAndDropOnDefault(Workspace mWorkspace, CellLayout.CellInfo mDragInfo){
		super(mWorkspace, mDragInfo);
	}
	
	//=======================================处理workspace上的拖拽响应==============================//
	/**
	 * Description: 处理在Workspace内拖动图标
	 * Author: guojy
	 * Date: 2013-2-1 下午5:44:06
	 */
//	@Override
//	public void onDropInternal(CellLayout cellLayout, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo){
//		super.onDropInternal(cellLayout, x, y, xOffset, yOffset, dragView, dragInfo);
		
		//打点统计
//		int index = mWorkspace.getCurrentDropLayoutIndex();
//		int staticLabel = (dragInfo instanceof WidgetInfo)
//				? AnalyticsConstant.ACTION_WEIGHT_LONGPRESS : AnalyticsConstant.ACTION_ICON_LONGPRESS;
//		if (index != mDragInfo.screen) {//跨屏移动
//			if(dragInfo instanceof WidgetInfo){
//				HiAnalytics.submitEvent(mContext, staticLabel, "4");
//			}else{
//				HiAnalytics.submitEvent(mContext, staticLabel, "5");
//			}
//		}else{
//			if(cellLayout.isItemPlacementDirty()){//交换位置
//				HiAnalytics.submitEvent(mContext, staticLabel, "2");
//			}else{//移动位置
//				HiAnalytics.submitEvent(mContext, staticLabel, "1");
//			}
//		}
//	}
	
	@Override
	public boolean createUserFolderIfNecessary(Object dragInfo, CellLayout cellLayout, View dragOverView, DragView dragView, int[] targetCell, boolean external, ArrayList<ApplicationInfo> appList) {
		if (mWorkspace.isAllAppsIndependence((ItemInfo)dragInfo) || mWorkspace.isAllAppsIndependence(dragOverView)
				|| !willCreateUserFolder((ItemInfo) dragInfo, dragOverView, cellLayout, mTargetCell, false)
				|| isRecommendDownloadApp(dragView.getDragingView()) || isRecommendDownloadApp(dragOverView))
			return false;
		ApplicationInfo appInfo = (ApplicationInfo) dragInfo ;
		
		//如果是从匣子中拖出来的copy一份
		if (((ApplicationInfo) dragInfo).container == ItemInfo.NO_ID) {
			appInfo = appInfo.copy();
			statAddAppFromDrawer(appInfo);
		}

		if (dragOverView instanceof OnDragEventCallback) {
			((OnDragEventCallback) dragOverView).onDropAni(dragView);
		}

		if (!external) {
			mWorkspace.getParentCellLayoutForView(mDragInfo.cell).removeView(mDragInfo.cell);
		}
		CellLayout.LayoutParams lp = (CellLayout.LayoutParams)dragOverView.getLayoutParams();
		if(lp.isOnReorderAnimation){//防止同时进行多个动画，产生多个View或动画不正确情况
			dragOverView.clearAnimation();
		}

		cellLayout.removeView(dragOverView);
		FolderIconTextView fi = ((Launcher)mLauncher).addFolder(cellLayout, LauncherSettings.Favorites.CONTAINER_DESKTOP, 
				mWorkspace.indexOfChild(cellLayout), targetCell[0], targetCell[1], "");
		if(appList != null && appList.size() > 0){
			ArrayList<ApplicationInfo> items = new ArrayList<ApplicationInfo>();
			for (ApplicationInfo applicationInfo : appList) {
				items.add(applicationInfo.copy());
			}
			fi.addItems(items);
		}else{
			fi.addItem(appInfo);
		}
		
		fi.addItem((ApplicationInfo) dragOverView.getTag());
		startFolderAnimation((DragController)mDragController, true, fi);
		/**
		 * 拖动图标合成文件夹统计
		 */
//		HiAnalytics.submitEvent(mContext, AnalyticsConstant.ACTION_ICON_LONGPRESS,"6");
		
		return true;
	}
	
	boolean willCreateUserFolder(ItemInfo info, View dropOverView, CellLayout dragTargetLayout, int[] targetCell, boolean considerTimeout) {
		if (dropOverView == null || !(dropOverView.getTag() instanceof ApplicationInfo))
			return false;

		boolean hasntMoved = false;
		if (mDragInfo != null) {
			hasntMoved = mWorkspace.isHansntMoved(dragTargetLayout, targetCell);
		}

		if (hasntMoved || considerTimeout) {
			return false;
		}

		return info instanceof ApplicationInfo;
	}

	@Override
	public boolean addToExistingFolderIfNecessary(DragView dragView, Object dragObject, CellLayout target, int[] targetCell, boolean external, ArrayList<ApplicationInfo> drItems) {
		if(mWorkspace.isAllAppsIndependence((ItemInfo)dragObject) || isRecommendDownloadApp(dragView.getDragingView()))
			return false;
		
		View dropOverView = target.getChildAt(targetCell[0], targetCell[1]);
		if(isUpgradeFolder(dropOverView))
			return false;
		if (dropOverView instanceof FolderIconTextView) {
			FolderIconTextView fi = (FolderIconTextView) dropOverView;
			if (fi.acceptDrop(dragObject)) {
				if (dropOverView instanceof OnDragEventCallback) {
					((OnDragEventCallback) dropOverView).onDropAni(dragView);
				}
				CellLayout.LayoutParams lp = (CellLayout.LayoutParams)dropOverView.getLayoutParams();
				if(lp.isOnReorderAnimation){//防止同时进行多个动画，产生多个View或动画不正确情况
					dropOverView.clearAnimation();
				}
				
				if (!external)
					mWorkspace.getParentCellLayoutForView(mDragInfo.cell).removeView(mDragInfo.cell);
				
				ApplicationInfo info = (ApplicationInfo) dragObject ;
				
				//如果是从匣子中拖出来的copy一份
				if (((ApplicationInfo) dragObject).container == ItemInfo.NO_ID) {
					info = info.copy();
					statAddAppFromDrawer(info);
				}
				if(drItems != null && drItems.size() > 0){
					ArrayList<ApplicationInfo> items = new ArrayList<ApplicationInfo>();
					for (ApplicationInfo applicationInfo : drItems) {
						items.add(applicationInfo.copy());
					}
					fi.addItems(items);
				}else{
					fi.addItem(info);
				}
				startFolderAnimation((DragController)mDragController,false,fi);
				
				/**
				 * 拖动图标进入文件夹统计
				 */
//				HiAnalytics.submitEvent(mContext, AnalyticsConstant.FOLDER_MERGE_INTO, FolderAnalysisConstants.FOLDER_ANALYSIS_LABEL_LAUNCHER);
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Description: 处理在其他地方(非Workspace)批量拖动图标到桌面
	 * Author: dingdj
	 * Date: 2013-5-24 下午5:03:56
	 */
	@Override
	public void onDropExternal(int x, int y, Object dragInfo, CellLayout cellLayout, DragView dragView, ArrayList<ApplicationInfo> appList) {
		//处理生成或合并文件夹
		if(dragView != null){
			final int[] mDragViewVisualCenter = dragView.getDragCenterPoints();
			mTargetCell = mWorkspace.findNearestArea(mDragViewVisualCenter[0], mDragViewVisualCenter[1], 1, 1, cellLayout, mTargetCell);
			final View dragOverView = cellLayout.getChildAt(mTargetCell[0], mTargetCell[1]);
			if (createUserFolderIfNecessary(dragInfo, cellLayout, dragOverView, dragView, mTargetCell, true, appList))
				return;

			if (addToExistingFolderIfNecessary(dragView, dragInfo, cellLayout, mTargetCell, true, appList)) {
				return;
			}
			
			if(appList != null){
				if(appList.size() == 1){
					ItemInfo info = (ItemInfo) dragInfo;
					int[] targetCell = mWorkspace.estimateDropCell(x, y, info.spanX, info.spanY, null, cellLayout, null);
					if(targetCell == null){
						Toast.makeText(mContext, R.string.spring_add_app_from_drawer_reset, Toast.LENGTH_SHORT).show();
						return;
					}
					dropToScreenExternal(cellLayout, dragInfo, targetCell);
				}else if(appList.size() > 1){
					ItemInfo info = (ItemInfo) appList.get(0);
					int[] targetCell = mWorkspace.estimateDropCell(x, y, info.spanX, info.spanY, null, cellLayout, null);
					if(targetCell == null){
						Toast.makeText(mContext, R.string.spring_add_app_from_drawer_reset, Toast.LENGTH_SHORT).show();
						return;
					}
					dropToScreenExternal(cellLayout, dragInfo, targetCell);
					appList.remove(0);
					ArrayList<int[]> vacantCells = cellLayout.findAllVacantCellFromBottom(1, 1, null);
					CellLayoutHelper.sortVacantCell(vacantCells, targetCell[0], targetCell[1]);
					
					if(vacantCells.size() == 0){//生成文件夹将其它的图标
						createUserFolderIfNecessary(dragInfo, cellLayout, cellLayout.getChildAt(targetCell[0], targetCell[1]), dragView, targetCell, true, appList);
					}else if(vacantCells.size() < appList.size()){//剩下的空cell小于所要添加的图标
						for(int i=0; i< vacantCells.size()-1; i++){
							dropToScreenExternal(cellLayout, appList.get(i), vacantCells.get(i));
						}
						String folderTitle = mContext.getResources().getString(R.string.folder_name);
						FolderIconTextView fi = ((Launcher)mLauncher).addFolder(cellLayout, LauncherSettings.Favorites.CONTAINER_DESKTOP, 
								mWorkspace.getCurrentScreen(), vacantCells.get(vacantCells.size()-1)[0], 
								vacantCells.get(vacantCells.size()-1)[1], folderTitle);
						ArrayList<ApplicationInfo> items = new ArrayList<ApplicationInfo>();
						
						for (int j = vacantCells.size()-1; j< appList.size(); j++ ) {
							items.add(appList.get(j).copy());
						}
						fi.addItems(items);
						fi.refresh();
					}else{
						for(int i=0; i< appList.size(); i++){
							dropToScreenExternal(cellLayout, appList.get(i), vacantCells.get(i));
						}
					}
					HiAnalytics.submitEvent(mContext, AnalyticsConstant.DRAWER_MULTI_SELECT, "zm");
					ControlledAnalyticsUtil.addCommonAnalytics(mContext, AnalyticsConstant.LAUNCHER_DRAWER_BATCH_ADD);
				}
			}
			
		}
		
	}
	
	/**
	 * Description: 将在其他地方(非Workspace)拖动的图标放置到桌面目标位置
	 * Author: guojy
	 * Date: 2013-2-1 下午5:06:48
	 */
	@Override
	public void dropToScreenExternal(CellLayout cellLayout, Object dragInfo, int[] targetCell) {
		dropToScreenExternal(cellLayout, dragInfo, targetCell, mWorkspace.getCurrentScreen());
	}
	
	private void dropToScreenExternal(CellLayout cellLayout, Object dragInfo, int[] targetCell, int screen) {
		if(null == dragInfo || !(dragInfo instanceof ItemInfo))
			return;
		
		ItemInfo info = (ItemInfo) dragInfo;
		info.cellX = targetCell[0];
		info.cellY = targetCell[1];
		
		if (info.itemType == LauncherSettings.Favorites.ITEM_TYPE_PANDA_WIDGET) {//拖动匣子中小部件特殊处理
			if (info instanceof LauncherWidgetInfo 
				&& ((LauncherWidgetInfo) info).getJumpActivity() != null) {
				Intent intent = new Intent(Global.getApplicationContext(), ((LauncherWidgetInfo) info).getJumpActivity());
				SystemUtil.startActivitySafely(mLauncher, intent);
			} else {
				PandaWidgetInfo pandaWidgetInfo = WorkspaceHelper.transformToPandaWidgetInfo(mLauncher,(LauncherWidgetInfo) dragInfo);
				View view = mWorkspace.createViewByItemInfo(pandaWidgetInfo);
				if (view == null)
					return;
				pandaWidgetInfo.screen = screen;
				
				pandaWidgetInfo.cellX = targetCell[0];
				pandaWidgetInfo.cellY = targetCell[1];
				((Launcher)mLauncher).addCustomWidgetToScreen(pandaWidgetInfo, view, screen);
			}
			
			if(info instanceof LauncherWidgetInfo){
				if(((Launcher)mLauncher).isLauncherAddMainViewVisible()){
					HiAnalytics.submitEvent(mContext, AnalyticsConstant.ADD_WIDGET_PATH, "" + 
							((Launcher)mLauncher).getLauncherAddMainView().getFromType());
				}else{					
					HiAnalytics.submitEvent(mContext, AnalyticsConstant.ADD_WIDGET_PATH, "2");
				}
//				HiAnalytics.submitEvent(mContext, AnalyticsConstant.WIDGET_ADD_TO_LAUNCHER_FROM_DRAWER, ((LauncherWidgetInfo)info).getTitle());
				ControlledAnalyticsUtil.addCommonAnalytics(mContext,AnalyticsConstant.LAUNCHER_DRAWER_WIDGET, ((LauncherWidgetInfo)info).title);
				IntegralSubmitUtil.getInstance(mContext).submit(mContext, IntegralTaskIdContent.LAUNCHER_GLORIFY_LONGTOUCH_ADD_WIDGET);
			}
		} else if (info.itemType == LauncherSettings.Favorites.ITEM_TYPE_WIDGET_SHORTCUT) {
			View view = LauncherWidgetHelper.createShortcutByLauncherWidget(mLauncher, (LauncherWidgetInfo) info);
			if (view == null || view.getTag() == null)
				return;
			
			((Workspace)mWorkspace).addViewInCurrentScreenFromOutside(view, cellLayout, (ItemInfo) view.getTag());
			if(info instanceof LauncherWidgetInfo)
//				HiAnalytics.submitEvent(mContext, AnalyticsConstant.WIDGET_ADD_TO_LAUNCHER_FROM_DRAWER, ((LauncherWidgetInfo)info).getTitle());
				IntegralSubmitUtil.getInstance(mContext).submit(mContext, IntegralTaskIdContent.LAUNCHER_GLORIFY_LONGTOUCH_ADD_WIDGET);
				ControlledAnalyticsUtil.addCommonAnalytics(mContext,AnalyticsConstant.LAUNCHER_DRAWER_WIDGET, ((LauncherWidgetInfo)info).title);
		} else {
			//拷贝ItemInfo，防止后面对它的修改影响到原来ItemInfo!
			if (!(dragInfo instanceof LauncherWidgetInfo) && info.container == ItemInfo.NO_ID) {
				info = info.copy();
                //如果是应用升级文件夹拉到桌面变成普通的文件夹类型
                if(info.itemType == LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER) {
                    info.itemType = LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER;
                } else if(!Global.isDrawerVisiable() && info.itemType == Favorites.ITEM_TYPE_CUSTOM_INTENT && info instanceof ApplicationInfo){
                	//无匣子桌面，添加应用升级文件夹
        			ApplicationInfo item = (ApplicationInfo)info;
        			if(item.intent != null && item.intent.toURI().contains(CustomIntent.ACTION_APP_UPGRADE_FOLDER)){
        				if(WorkspaceHelper.isExistUpgradeFolder()){
        					Toast.makeText(mLauncher, "桌面已经存在应用升级文件夹", Toast.LENGTH_LONG).show();
            				return;
        				}else{
        					UpgradeFolderInfo folderInfo = new UpgradeFolderInfo();
            				folderInfo.screen = item.screen;
            				folderInfo.cellX = item.cellX;
            				folderInfo.cellY = item.cellY;
            				folderInfo.title = item.title;
            				folderInfo.itemType = LauncherSettings.Favorites.ITEM_TYPE_UPGRADE_FOLDER;
            				DrawerMainView.initUpgradeFolderContent(mLauncher, folderInfo);
            				info = folderInfo;
        				}
        			}
        		}
                
				info.container = ItemInfo.NO_ID ;
				
				//添加应用到桌面
				if (info instanceof ApplicationInfo){
					IntegralSubmitUtil.getInstance(mContext).submit(mContext, IntegralTaskIdContent.LAUNCHER_GLORIFY_LONGTOUCH_ADD_APP);
				}
			}
			View view = mWorkspace.createViewByItemInfo(info);
			if (view == null)
				return;
			statAddAppFromDrawer(info);
			((Workspace)mWorkspace).addViewInCurrentScreenFromOutside(view, cellLayout, info);
		}
	}

	
	
	
	//=========================================从文件夹中拖拽出控制==================================//
	/**
	 * <p>查找拖拽app时，DragView经过的底部桌面图标</p>
	 * 
	 * <p>date: 2012-8-13 下午02:28:38
	 * @author pdw
	 * @param cellLayout
	 * @param info
	 * @return
	 */
	private View getDragOverView(CellLayout cellLayout,ItemInfo info) {
		final BaseDragController dragController = mLauncher.mDragController;
		final DragView dragView = dragController.getDragView();
		int[] dragViewCenter = dragView.getDragCenterPoints();
		mTargetCellOnFolder = mWorkspace.findNearestArea(dragViewCenter[0], dragViewCenter[1], 1,1, cellLayout, mTargetCellOnFolder);
		return cellLayout.getChildAt(mTargetCellOnFolder[0], mTargetCellOnFolder[1]);
	}
	
	@Override
	public void onDrop(View target, FolderInfo folderInfo, ArrayList<Object> items) {
		FolderInfo folder = (FolderInfo)folderInfo;
		if (items == null) return;
		Object item = items.get(0);
		
		if(folder.getSize() < 2 || (folder.getProxyView() != null && folder.getProxyView().getTag() instanceof AnythingInfo)
				|| !(item instanceof ApplicationInfo))return ;
		
		if (target instanceof Workspace) {
			final CellLayout cellLayout = mWorkspace.getCurrentDropLayout() ;
			
			findCellForFolderDragout(cellLayout, (ApplicationInfo) item);
			
			/**
			 * 屏幕没有空间了
			 */
			if (mTargetCellOnFolder == null) { //桌面没有空间
				final View view = getDragOverView(cellLayout, (ApplicationInfo) item);
				/**
				 * 1、拖拽到应用程序上可合成文件夹
				 * 2、拖拽到文件夹上可放入文件夹
				 */
				if (!(view instanceof IconMaskTextView) && !(view instanceof FolderIconTextView)) {
					//MessageUtils.makeShortToast(mContext, R.string.folder_drag_out_error);
					return ;
				}
			}
		}
		
		FolderHelper.removeDragApp(folder, (ApplicationInfo) item);
		folder.checkFolderState();
		if (folder.getSize() > 1 && folder.mFolderIcon != null)
//			folder.mFolderIcon.refresh();
			folder.mFolderIcon.invalidate();
	}
	
	
	
	
	//=======================================处理屏幕预览上的拖放==============================//
//	@Override
//	public boolean dropItemToScreenFromPreview(int index, int[] targetCell) {
//		View cell = mDragInfo.cell;
//		Object dragInfo = cell.getTag();
//		int staticLabel = (dragInfo instanceof WidgetInfo)
//				? AnalyticsConstant.ACTION_WEIGHT_LONGPRESS : AnalyticsConstant.ACTION_ICON_LONGPRESS;
//		if (index != mDragInfo.screen) {
//			if(dragInfo instanceof WidgetInfo){
//				HiAnalytics.submitEvent(mContext, staticLabel, "4");
//			}else{
//				HiAnalytics.submitEvent(mContext, staticLabel, "5");
//			}
//		}else{//移动位置
//			HiAnalytics.submitEvent(mContext, staticLabel, "1");
//		}
		
//		return super.dropItemToScreenFromPreview(index, targetCell);
//	}
	
	@Override
	public void dropToScreenFromDrawerPreview(int screen, Object mDragInfo, ArrayList<ApplicationInfo> appList){
		if(appList != null && appList.size() > 0){
			 dropToScreenFromMoveToLauncherZone(screen, appList);
			 return;
		}else{
			if(!(mDragInfo instanceof ItemInfo)){
				return;
			}
			ItemInfo itemInfo = (ItemInfo)mDragInfo;
			
			CellLayout cl = mWorkspace.getCellLayoutAt(screen);
			int[] vacantCell = null;
			if(itemInfo instanceof LauncherWidgetInfo){				
				vacantCell = cl.findFirstVacantCell(itemInfo.spanX, itemInfo.spanY, null, true);
			}else{
				vacantCell = cl.findVacantCellFromBottom(itemInfo.spanX, itemInfo.spanY, null);
			}
			
			if(vacantCell == null){
				Toast.makeText(mContext, R.string.spring_add_app_from_drawer_fail, Toast.LENGTH_SHORT).show();
				if(mLauncher.isOnSpringMode()){// 跳转回程序匣子
					((Launcher)mLauncher).exitSpringModeFromDrawer();
				}
			}else{
				mWorkspace.changeToNormalMode();
				dropToScreenExternal(cl, mDragInfo, vacantCell, screen);
//				if(!(mDragInfo instanceof LauncherWidgetInfo)){
//					Toast.makeText(mContext, R.string.spring_add_app_from_drawer_success, Toast.LENGTH_SHORT).show();
//				}
			}
		}
	}
	
	private void statAddAppFromDrawer(ItemInfo itemInfo){
		if(itemInfo != null && itemInfo.itemType != LauncherSettings.Favorites.ITEM_TYPE_HI_APPLICATION
				&& itemInfo.itemType != LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT){
			if(((Launcher)mLauncher).isLauncherAddMainViewVisible()){
				HiAnalytics.submitEvent(mContext, AnalyticsConstant.ADD_APP_PATH, "" + 
						((Launcher)mLauncher).getLauncherAddMainView().getFromType());
			}else{					
				HiAnalytics.submitEvent(mContext, AnalyticsConstant.ADD_APP_PATH, "2");
			}
		}
	}
	
	/**
	 * Description: 在程序匣子中，拖动app并drop到“移动到桌面”区域或屏幕底部时，批量添加app到桌面的当前屏
	 * Author: dingdj
	 * Date: 2013-5-23 下午05:01:03
	 */	
	private void dropToScreenFromMoveToLauncherZone(int screen, ArrayList<ApplicationInfo> appList) {
		CellLayout cl = mWorkspace.getCellLayoutAt(screen);

		ArrayList<int[]> vacantCells = cl.findAllVacantCellFromBottom(1, 1, null);

		if (vacantCells.size() == 0) {
			Toast.makeText(mContext, R.string.spring_add_app_from_drawer_fail, Toast.LENGTH_SHORT).show();
			if (mLauncher.isOnSpringMode()) {// 跳转回程序匣子
				((Launcher)mLauncher).exitSpringModeFromDrawer();
			}
			return;
		} else if (vacantCells.size() < appList.size()) {
			mWorkspace.changeToNormalMode();
			for (int i = 0; i < vacantCells.size() - 1; i++) {
				dropToScreenExternal(cl, appList.get(i), vacantCells.get(i));
			}
			// 余下的图标放置到一个文件夹中
			String folderTitle = mContext.getResources().getString(R.string.folder_name);
			FolderIconTextView fi = ((Launcher)mLauncher).addFolder(cl, LauncherSettings.Favorites.CONTAINER_DESKTOP, screen, vacantCells.get(vacantCells.size() - 1)[0],
					vacantCells.get(vacantCells.size() - 1)[1], folderTitle);
			ArrayList<ApplicationInfo> items = new ArrayList<ApplicationInfo>();
			for (int j = vacantCells.size() - 1; j < appList.size(); j++) {
				items.add(appList.get(j).copy());
			}
			// 批量增加图标
			fi.addItems(items);
			fi.refresh();

		} else {
			mWorkspace.changeToNormalMode();
			for (int i = 0; i < appList.size(); i++) {
				dropToScreenExternal(cl, appList.get(i), vacantCells.get(i));
			}
		}
		Toast.makeText(mContext, R.string.spring_add_app_from_drawer_success, Toast.LENGTH_SHORT).show();
	}
	
	
	
	
	//==========================================其它可复写的方法==========================//
	@Override
	public int[] getDragInfoSpanXY(Object dragInfo){
		int spanX = mDragInfo == null ? 1 : mDragInfo.spanX;
		int spanY = mDragInfo == null ? 1 : mDragInfo.spanY;
		if (dragInfo != null && dragInfo instanceof LauncherItemInfo) {
			/**
			 * 拖拽出的widget大小
			 */
			spanX = ((LauncherItemInfo) dragInfo).spanX;
			spanY = ((LauncherItemInfo) dragInfo).spanY;
		}
		return new int[]{spanX, spanY};
	}
	
	/**
	 * 处理从匣子里拖到Workspace时的onDrop事件
	 * @param source
	 */
	@Override
	public void onDropFromDrawer(DragSource source){
		if(source instanceof DrawerSlidingView){
			DrawerSlidingView view = (DrawerSlidingView)source;
			if(view.isNeedRelayoutAfterDrop()){
				view.setNeedRelayoutAfterDrop(false);
			}
		}
	}

	@Override
	public boolean isOnMultiSelectedDraging(){
		return ((DragController)mLauncher.getDragController()).isOnMultiSelectedDrag();
	}

	@Override
	public boolean isNotAllowToMergeFolder(View dragOverView){
		if(isUpgradeFolder(dragOverView) || isRecommendDownloadApp(dragOverView))
			return true;
		return super.isNotAllowToMergeFolder(dragOverView);
	}
	
	@Override
	public boolean isAllowMerge(DragView dragView, Object dragInfo) {
		if(isRecommendDownloadApp(dragView.getDragingView())) return false;
		return super.isAllowMerge(dragView, dragInfo);
	}

	private boolean isRecommendDownloadApp(View view) {
		if(view != null && view instanceof LauncherIconView){
			IconType iconType = ((LauncherIconView)view).getIconType();
			if(iconType != null && iconType instanceof LauncherSystemShortcutIconType)
				return ((LauncherSystemShortcutIconType)iconType).isRecommendDownload();
		}
		if(view != null && view.getTag() instanceof ItemInfo && LauncherProviderHelper.isLitianRecommendApp((ItemInfo)view.getTag()))
			return true;
		return false;
	}

	private boolean isUpgradeFolder(View dragOverView) {
		if(dragOverView != null){
			Object obj = dragOverView.getTag();
			if(obj != null && obj instanceof UpgradeFolderInfo)
				return true;
		}
		return false;
	}
	
	public void startFolderAnimation(DragController dragController,boolean isNewFolder, FolderIconTextView iconTextView) {
		if (mWorkspace.isOnSpringMode()) {
			return;
		}
		
		((Launcher)mLauncher).getFolderCotroller().reloadOnOpenFolder();
		
		View view = null;
		int wh[];

		view = dragController.getDragView();
		DragView dragView = (DragView) view;
		//dragView.setBackgroundColor(0x5500ff00);
		wh = getIconBottom(dragView.getDragingView());
		FolderAnimation animation = new FolderAnimation(iconTextView, wh[0], wh[1], 0.75f, dragView,dragController,null);
		animation.setDuration(500);
		if (isNewFolder) {
			iconTextView.setNoDrawIndex(0);
		} else {
			iconTextView.setNoDrawIndex(iconTextView.mInfo.contents.size() - 1);
		}
		dragController.setIsFolderAnimation(true);
		
		
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
			@Override
			public void onAnimationEnd(Animation arg0) {
				FolderAnimation animation = (FolderAnimation) arg0;
				animation.getFolderIconView().setNoDrawIndex(-1);
				animation.getFolderIconView().invalidate();
				DragView view = mDragController.getDragView();
				animation.mDragController.setIsFolderAnimation(false);
				if (view != null && view == animation.getDragView()) {
					
					new Handler().post(new Runnable() {	
						@Override
						public void run() {
							mDragController.cleanDragView();
						}
					});
					
				}
			}
		});
		mDragController.getDragView().startAnimation(animation);
	}
	
	
	/**
	 * 用于拖动时底部边框绘制
	 * 对各种不同的View做适配， 得到各个View的图标的底部坐标 目的是把图标底下的文字去除
	 * 
	 * 
	 * @return 为0表示匹配不到，大于0表示匹配成功且结果等于图标的底部的坐标
	 * */

	private int[] getIconBottom(View v) {
		int wh[] = new int[2];

		if (v instanceof LauncherIconView) {
			// 桌面图标以及DOCK栏图标
			final LauncherIconView iconView = (LauncherIconView) v;
			wh[0] = iconView.getIconRect().left;
			wh[1] = iconView.getIconRect().top;
		} else if (v instanceof AppDrawerIconMaskTextView) {
			// 匣子中APP
			AppDrawerIconMaskTextView iconView = (AppDrawerIconMaskTextView) v;
			wh[0] = iconView.getIconRect().left;
			wh[1] = iconView.getIconRect().top;
		}
		return wh;
	}
}
