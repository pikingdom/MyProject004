package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.app.view.AppDrawerIconMaskTextView;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.view.DrawerSlidingView;
import com.nd.hilauncherdev.folder.model.FolderEncriptHelper;
import com.nd.hilauncherdev.folder.model.FolderSwitchController;
import com.nd.hilauncherdev.folder.view.FolderBoxedViewGroup;
import com.nd.hilauncherdev.folder.view.FolderSlidingView;
import com.nd.hilauncherdev.framework.LauncherDeleteAlertHelper;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.launcher.LauncherSettings.Favorites;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.CellLayoutConfig;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.info.ItemInfo;
import com.nd.hilauncherdev.launcher.info.WidgetInfo;
import com.nd.hilauncherdev.launcher.screens.dockbar.BaseMagicDockbar;
import com.nd.hilauncherdev.launcher.screens.dockbar.DockbarCellLayout;
import com.nd.hilauncherdev.launcher.touch.DragSource;
import com.nd.hilauncherdev.launcher.view.DragView;
import com.nd.hilauncherdev.launcher.view.icon.ui.folder.FolderIconTextView;
import com.nd.hilauncherdev.launcher.view.icon.ui.impl.AppMaskTextView;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.theme.data.ThemeData;


public class MagicDockbar extends BaseMagicDockbar{
	
	public MagicDockbar(Context context, AttributeSet attrs) {
		super(context, attrs);	
	}


	@Override
	public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
		//从匣子或文件夹拖出判断
		if(source instanceof DrawerSlidingView)
			return false;
		if(source instanceof FolderSlidingView){
			View v = dragView.getDragingView();
			if(!(v instanceof AppDrawerIconMaskTextView  || v instanceof FolderBoxedViewGroup 
					|| v instanceof AppMaskTextView || v instanceof FolderIconTextView) 
					|| mDragController.isOnMultiSelectedDrag()){
				Toast.makeText(getContext(), R.string.spring_add_app_from_drawer_reset, Toast.LENGTH_SHORT).show();
				if(mLauncher.isOnSpringMode()){
					mLauncher.getScreenViewGroup().cleanAndRevertReorder();
				}
				return false;
			}
		}
		
		return super.acceptDrop(source, x, y, xOffset, yOffset, dragView, dragInfo);
	}
	
	@Override
	public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
		super.onDrop(source, x, y, xOffset, yOffset, dragView, dragInfo);
		
		//增加打点统计
		DockbarCellLayout dockbar = (DockbarCellLayout) getChildAt(mCurrentScreen);
		int count = dockbar.getChildCount();
		int cellX = 0;
		if(count != 0){
			int cellWidth = getWidth() / count;
			cellX = x / cellWidth;
		}
		HiAnalytics.submitEvent(mLauncher, AnalyticsConstant.DOCK_BAR_ICON_EXCHANGE,""+mCurrentScreen+cellX);
	}
	
	@Override
	public boolean onLongClick(View v) {
		//编辑被锁定Dock上的任何长按事件都不响应
		if (!BaseConfig.allowEdit(getContext())) {
			return true;
		}
		((Launcher)mLauncher).setClickView(v);
		ItemInfo item = (ItemInfo) v.getTag();
		if ((item instanceof ApplicationInfo || item instanceof WidgetInfo || item instanceof FolderInfo || item instanceof AnythingInfo)) {
			((DragController)mDragController).setShowShortcutMenu(true);
			((DragController)mDragController).setMenuTriggerSource(this);
		}
		return super.onLongClick(v);
	}

	/**
	 * 显示长按菜单
	 * @author dingdj
	 * @createtime 2013-7-17
	 */
	public void showShortcutMenu(){
		if(mDragView != null){
			ItemInfo item = (ItemInfo) mDragView.getTag();
//			if(!needShowShortcutMenu(mLauncher, item)) return;
			//底部托盘不显示长按菜单
			if ((item instanceof ApplicationInfo || item instanceof WidgetInfo || item instanceof FolderInfo || item instanceof AnythingInfo) 
					&& (item.itemType != Favorites.ITEM_TYPE_INDEPENDENCE)) {
				mDragController.showShortcutMenu(null, mDragView, this);
			}
		}
	}
	
	/**
	 * 清除新安装程序标记
	 * 
	 * @param isForceClear - 是否强制清除，若否，则根据SharedPreference中记录的标记位决定是否清除
	 */
	public void clearNewMark(boolean isForceClear) {
//		if (!Global.isOnScene() && (DrawerNewInstalledHelper.getInstance().isShowNewMarkInDock() || isForceClear)) {
//			for (int i = 0; i < getChildCount(); i++) {
//				DockbarCellLayout group = (DockbarCellLayout) getChildAt(i);
//				for (int j = 0; j < group.getChildCount(); j++) {
//					View v = group.getChildAt(j);
//					Object tag = v.getTag();
//					if (tag instanceof ApplicationInfo) {
//						final ApplicationInfo appInfo = (ApplicationInfo) tag;
//						final int itemType = appInfo.itemType;
//						if (CustomIntent.ACTION_OPEN_DRAWER.equals(appInfo.intent.getAction())) {
//							final DockbarCell dock = (DockbarCell) v ;
//							if (itemType == Favorites.ITEM_TYPE_INDEPENDENCE) {
//								dock.setIconBitmap(BitmapUtils.drawable2Bitmap(ThemeCompatibleResAssit.getOpenDrawerIcon(getContext())));
//							} /*else {
//								dock.setImageDrawable(ThemeCompatibleResAssit.getOpenDrawerShortcutIcon(mContext));
//							}*/
//							dock.setBackgroundDrawable(null);
//							dock.invalidate();
//						}
//					}
//				}
//			}
//			DrawerNewInstalledHelper.getInstance().clearShowNewMarkInDock();
//		}
	}

	/**
	 * 显示新安装程序标记
	 */
	public void showNewMark() {
//		if (DrawerNewInstalledHelper.getInstance().isShowNewMarkInDock() && !Global.isOnScene()) {
//			for (int i = 0; i < getChildCount(); i++) {
//				DockbarCellLayout group = (DockbarCellLayout) getChildAt(i);
//				for (int j = 0; j < group.getChildCount(); j++) {
//					View v = group.getChildAt(j);
//					Object tag = v.getTag();
//					if (tag instanceof ApplicationInfo) {
//						final ApplicationInfo appInfo = (ApplicationInfo) tag;
//						if (CustomIntent.ACTION_OPEN_DRAWER.equals(appInfo.intent.getAction()) &&
//								appInfo.itemType == Favorites.ITEM_TYPE_INDEPENDENCE) {
//							final DockbarCell dock = (DockbarCell) v ;
//							Drawable d = getResources().getDrawable(R.drawable.drawer_new_mark_close_normal_btn);
//							dock.setIconBitmap(BitmapUtils.drawable2Bitmap(d));
//							dock.setBackgroundDrawable(null);
//							dock.invalidate();
//						}
//					}
//				}
//			}
//		}
	}
	
	
	
	
	/**
	 * <br>Description: 应用主题皮肤
	 * <br>Author:caizp
	 * <br>Date:2012-7-19下午03:04:14
	 * @see com.nd.hilauncherdev.theme.assit.ThemeUIRefreshListener#applyTheme()
	 */
	@Override
	public void applyTheme() {
		// 加载dock栏背景
		ThemeManagerFactory.getInstance().loadThemeResource(ThemeData.PANDAHOME_STYLE_DOCK_BG, this,
				ThemeManagerFactory.THEME_ITEM_BACKGROUND);
	}
	
	@Override
	public boolean isShowDockbarText(){
		return Global.isShowDockbarText();
	}
	
	/**
	 * dock栏图标动画到Workspace时，查找目标位置
	 * @return
	 */
	@Override
	public int[] findCellXYForExchange(){
		if(mLauncher.mWorkspace.getCurrentCellLayout() == null)
			return null;
		
		if(BaseConfig.isOnScene()){
			return CellLayoutHelper.findVacantXYForSceneLayout(mLauncher.mWorkspace.getCurrentCellLayout(), 
					CellLayoutConfig.spanXYMather(1, 1, null));
		}else{			
			return  mLauncher.mWorkspace.getCurrentCellLayout().findFirstVacantCell(1, 1, null, true);
		}
	}
	@Override
	protected boolean notAllowDragOnDockbar(DragSource source, Object dragInfo){
		if(source instanceof DrawerSlidingView)
			return true;
		if(isDragFromDrawerFolder(source))
			return true;
		return super.notAllowDragOnDockbar(source, dragInfo);
	}
	
	/**
	 * 是否dock栏有5个图标并且从匣子拖入
	 * @param source
	 * @param dragInfo
	 * @return
	 */
//	@Override
//	public boolean isFullDockAndDragFromDrawer(DragSource source){
//		DockbarCellLayout cellLayout = (DockbarCellLayout) getChildAt(mCurrentScreen);
//		if(cellLayout.getChildCount() < DEFAULT_SCREEN_ITEM_COUNT)
//			return false;
//		return source instanceof DrawerSlidingView || isDragFromDrawerFolder(source);
//	}
//	
	/**
	 * 是否从匣子的文件夹里拖出
	 * @param source
	 * @return
	 */
	public boolean isDragFromDrawerFolder(DragSource source){
		return source != null && 
				source instanceof FolderSlidingView && ((FolderSlidingView)source).isFolderInDrawer();
	}
	
	/**
	 * 删除时，是否提示
	 * @param tag
	 * @return
	 */
	@Override
	public boolean alertOnDelete(Object tag){
		return (tag instanceof ItemInfo && LauncherDeleteAlertHelper.isNeedAlertView((ItemInfo) tag))
				|| (tag instanceof FolderInfo && ((FolderInfo) tag).isEncript);
	}
	
	@Override
	public void statSnapToScreen(int destToScreen){
		/**
		 * 增加滑屏统计
		 */
//		HiAnalytics.submitEvent(mContext, AnalyticsConstant.DOCK_BAR_SCROLL_SCREEN,String.valueOf(destToScreen));
	}
	
	@Override
	protected void startFolderAnimation(FolderIconTextView iconTextView){
		Workspace wk = (Workspace)mLauncher.mWorkspace;
		wk.initWorkspaceDragAndDropIfNotExsit(null);
		if(wk.getWorkspaceDragAndDrop() instanceof WorkspaceDragAndDropOnDefault){
			((WorkspaceDragAndDropOnDefault)wk.getWorkspaceDragAndDrop()).startFolderAnimation((DragController)mLauncher.getDragController(), false, iconTextView);
		}
	}
	
	@Override
	protected void handleDropDrawerFolderEx(FolderInfo folderInfo, long idFromDrawer){
		String folderEncript = FolderEncriptHelper.getNewInstance().getFolderEncript(idFromDrawer, FolderSwitchController.OPEN_FOLDER_FROM_DRAWER);
		
		if (folderEncript != null) {
			FolderEncriptHelper.getNewInstance().encriptFolderMD5(folderInfo.id, FolderIconTextView.OPEN_FOLDER_FROM_LAUNCHER, folderEncript);
		}
	}
}
