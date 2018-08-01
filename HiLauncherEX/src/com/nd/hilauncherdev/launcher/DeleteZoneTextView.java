package com.nd.hilauncherdev.launcher;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.view.DrawerSlidingView;
import com.nd.hilauncherdev.folder.view.FolderViewFactory;
import com.nd.hilauncherdev.framework.LauncherDeleteAlertHelper;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.AppUninstallUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.info.FolderInfo;
import com.nd.hilauncherdev.launcher.info.ItemInfo;
import com.nd.hilauncherdev.launcher.info.PandaWidgetInfo;
import com.nd.hilauncherdev.launcher.menu.shortcutmenu.ShortcutMenu;
import com.nd.hilauncherdev.launcher.screens.CellLayout;
import com.nd.hilauncherdev.launcher.screens.DeleteZone;
import com.nd.hilauncherdev.launcher.touch.DragSource;
import com.nd.hilauncherdev.launcher.touch.DropTarget;
import com.nd.hilauncherdev.launcher.view.BaseDeleteZoneTextView;
import com.nd.hilauncherdev.launcher.view.DragView;
import com.nd.hilauncherdev.launcher.view.icon.ui.folder.FolderIconTextView;
import com.nd.hilauncherdev.push.PushUtil;

public class DeleteZoneTextView extends BaseDeleteZoneTextView {
	private CellLayout.CellInfo mDragInfo;
	
	View back;
	TrashView mTrashView;
	Bitmap uninstallBitmap;
	Paint paint=new Paint();
	public DeleteZoneTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		srcColor=0xffffffff;
	}
	
	public DeleteZoneTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		srcColor=0xffffffff;
	}

	@Override
	public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
		/**
		 * 不允许删除【应用列表】快捷方式
		 */
		if(mLauncher.getScreenViewGroup().isAllAppsIndependence((ItemInfo) dragInfo) || mLauncher.isAllAppsVisible())
			return false;


		
		return super.acceptDrop(source, x, y, xOffset, yOffset, dragView, dragInfo);
	}
	
	@Override
	public void onDrop(final DragSource source, final int x, final int y, final int xOffset, final int yOffset, final DragView dragView, final Object dragInfo) {
		if (mType == DeleteZone.HINT_ZONE)
			return;
		Launcher launcher = (Launcher)mLauncher;
		/**
		 * 不处理文件夹的fling拖拽操作
		 */
		/*if (source instanceof FolderSlidingView && mLauncher.isFolderOpened()){
			return;
		}*/
		
		//操作统计
//		int staticLabel = (dragInfo instanceof WidgetInfo)
//				? AnalyticsConstant.ACTION_WEIGHT_LONGPRESS : AnalyticsConstant.ACTION_ICON_LONGPRESS;
//		if(isDeleteZone()){//删除
//			HiAnalytics.submitEvent(mContext, staticLabel, "3");
//		}else{//卸载
//			HiAnalytics.submitEvent(mContext, staticLabel, "4");
//		}
		
		// FIXME 屏幕编辑-删除应用、小部件、快捷方式图标
//		if (mLauncher.isOnSpringMode()){
//			HiAnalytics.submitEvent(mContext, AnalyticsConstant.LAUNCHER_SCREEN_EDIT_REMOVE_APP_ICON);
//		}
		/**
		 * 删除【最近安装】【最近打开】给出提示
		 */
		if (dragInfo instanceof AnythingInfo) {
			removeRecentFolderWithAlert(dragInfo);
			return ;
		}

        /**
         * 删除便签小部件
         */
        if (dragInfo != null && dragInfo instanceof PandaWidgetInfo &&
                "com.nd.hilauncherdev.widget.note".equals(((PandaWidgetInfo)dragInfo).pandaWidgetPackage)) {
            removeNoteWidget(source, dragView, dragInfo, launcher);
            return;
        }

		/**
		 * 删除加密文件夹给出提示
		 */
		if (dragInfo instanceof FolderInfo && ((FolderInfo) dragInfo).isEncript){
			launcher.isDeleteZone = false;
			FolderViewFactory.showAlertDialog(launcher, (FolderInfo) dragInfo, 
					FolderIconTextView.OPEN_FOLDER_FROM_LAUNCHER);
			return;
		} 
		
		if (dragInfo instanceof ItemInfo && LauncherDeleteAlertHelper.isNeedAlertView((ItemInfo) dragInfo)) {
			launcher.isDeleteZone = false ;
			if (source instanceof Workspace) {
				mDragInfo = launcher.mWorkspace.getDragInfo();
                LauncherDeleteAlertHelper.toDelete(((Launcher) mLauncher), mDragInfo, (ApplicationInfo)dragInfo, null);
			} else if (source instanceof MagicDockbar) {
				LauncherDeleteAlertHelper.toDelete(launcher, null, (ApplicationInfo) dragInfo, null);
			}
			return ;
		}
		
		if(dragView != null){
			removeItemFromWorkspace(source, dragInfo, dragView.getDragingView());
		}else{
			removeItemFromWorkspace(source, dragInfo, null);
		}
		
		super.onDrop(source, x, y, xOffset, yOffset, dragView, dragInfo);
	}

    private void removeNoteWidget(final DragSource source, final DragView dragView, final Object dragInfo, Launcher launcher) {
        mDragInfo = launcher.mWorkspace.getDragInfo();
        launcher.isDeleteZone = false ;
        ViewFactory.getAlertDialog(getContext(), getContext().getString(R.string.delete_special_widget), getContext().getString(R.string.delele_note_widget_tip), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (mDragInfo == null)
                    return;
                ViewGroup cellLayout = mLauncher.getScreenViewGroup().getCellLayoutAt(mDragInfo.screen);
                if (mDragInfo.cell != null) {
                    cellLayout.removeView(mDragInfo.cell);
                }

                if (mDragInfo.cell instanceof DropTarget) {
                    mLauncher.getDragController().removeDropTarget((DropTarget) mDragInfo.cell);
                }

                if (dragView != null) {
                    removeItemFromWorkspace(source, dragInfo, dragView.getDragingView());
                } else {
                    removeItemFromWorkspace(source, dragInfo, null);
                }
                mLauncher.refreshWorkspaceSpringScreen();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
	public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
    	if (isDeleteZone()) {
			mTrashView.openTrash();
		}
    	//back.setBackgroundResource(R.drawable.delete_zone_bg_red);
    	back.setBackgroundColor(0xffff3600);;
    	backDisplayAnimation(300);
    	
		if (mLauncher.isAllAppsVisible())
			return;
		if(mLauncher.isOnSpringMode() && source instanceof DrawerSlidingView)
			return;
		if (mLauncher.getScreenViewGroup().isAllAppsIndependence((ItemInfo) dragInfo))
			return;
		super.onDragEnter(source, x, y, xOffset, yOffset, dragView, dragInfo);
	}

	@Override
	public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
		if (isDeleteZone()) {
			mTrashView.closeTrash();
		}
		backMissAnimation(300);
		if (mLauncher.isAllAppsVisible())
			return;
		
		if (mLauncher.getScreenViewGroup().isAllAppsIndependence((ItemInfo) dragInfo))
			return;
		
		super.onDragExit(source, x, y, xOffset, yOffset, dragView, dragInfo);
	}

	private void removeRecentFolderWithAlert(Object dragInfo){
		Launcher launcher = (Launcher)mLauncher;
		launcher.isDeleteZone = false ;
		mDragInfo = launcher.mWorkspace.getDragInfo();
		LauncherDeleteAlertHelper.showAlertIfNecessary((AnythingInfo) dragInfo,mDragInfo,null);
	}
	
	private void removeItemFromWorkspace(DragSource source, Object dragInfo, View v){
		Launcher launcher = (Launcher)mLauncher;
		final ItemInfo item = (ItemInfo) dragInfo;
		if(isDeleteZone()){//删除
			if (item.container == -1)
				return;
			// 删除双应用图标 caizp 2017-03-15
			if(LauncherSettings.BaseLauncherColumns.ITEM_TYPE_SHORTCUT == item.itemType && item instanceof ApplicationInfo) {
				ApplicationInfo info = (ApplicationInfo) item;
				if(null != info.intent) {
					final String multiAppPackageName = info.intent.getStringExtra("packagename");
					if(!TextUtils.isEmpty(multiAppPackageName)) {
						ThreadUtil.executeMore(new Runnable() {
							@Override
							public void run() {
								MultiAppController.uninstallQuickApp(multiAppPackageName);
							}
						});
					}
				}
			}
			launcher.getWorkspace().getWorkspaceHelper().removeItemFromWorkspace(dragInfo, v);
			launcher.isDeleteZone = true;
			if (PushUtil.isPushedIcon(item)) {// 增加推送图标点击打点
				HiAnalytics.submitEvent(Global.getApplicationContext(), AnalyticsConstant.CLICK_PUSH_ICON, 
						PushUtil.getStatIdFromPushIconKeyString(((ApplicationInfo) item).statTag) + "_del");
			}
		}else{
			if (ShortcutMenu.isAppInfo(item))
				return;
			ApplicationInfo applicationInfo = (ApplicationInfo) item;
			AppUninstallUtil.uninstallAppByLauncher(launcher, applicationInfo.componentName.getPackageName());
			launcher.isDeleteZone = false;
		}
	}

	@Override
	public void setDrawable() {
		back=new View(getContext());
		back.setBackgroundColor(0);
		addView(back);
		if (isDeleteZone()) {
			mTrashView=new TrashView(this.getContext());
			addView(mTrashView);
		} else {
			uninstallBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.delete_zone_uninstall);
		}
		//super.setTransitionDrawable(transition);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (isDeleteZone()) {
			canvas.drawText(delText, startX + mTrashView.getMeasuredWidth() + drawpadding, startTY, textPaint);
		} else if (mType == DeleteZone.HINT_ZONE){
			if(!LauncherBranchController.isLiTian_Yinni()){//印尼版不显示
				canvas.drawText(hintText, startX, startTY, textPaint);
			}
		} else{
			canvas.drawText(unText, startX + uninstallBitmap.getWidth() + drawpadding, startTY, textPaint);
			canvas.drawBitmap(uninstallBitmap, startX, startDY,paint );
		}
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		back.layout(0, 0, r-l, b-t);
		if (isDeleteZone()) {
			mTrashView.layout(startX, startDY, startX + mTrashView.getMeasuredWidth(), startDY + mTrashView.getMeasuredHeight());
		}
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int w=MeasureSpec.getSize(widthMeasureSpec);
		int h=MeasureSpec.getSize(heightMeasureSpec);
		paddingBottom=0;
		//int wMode=MeasureSpec.getMode(widthMeasureSpec);
		int hMode=MeasureSpec.getMode(heightMeasureSpec);
		FontMetrics fontMetrics=textPaint.getFontMetrics();
		int fontH=(int)(-fontMetrics.ascent+fontMetrics.descent);

		if (isDeleteZone()) {
			measureChild(mTrashView, widthMeasureSpec, heightMeasureSpec);
			if (hMode == MeasureSpec.AT_MOST) {
				h=mTrashView.getMeasuredHeight();
			}
			startX = (int) ((w - drawpadding - mTrashView.getMeasuredWidth() - textPaint.measureText(delText)) / 2);
			startDY = (h -paddingBottom -mTrashView.getMeasuredHeight()) / 2;
			startTY = (h - fontH-paddingBottom) / 2  - (int)fontMetrics.ascent;
			if (hMode == MeasureSpec.EXACTLY) {
				setMeasuredDimension(w, h < mTrashView.getMeasuredHeight() ? mTrashView.getMeasuredHeight() : h);
			} else {
				setMeasuredDimension(w,  mTrashView.getMeasuredHeight());
			}	
		} else if (mType == DeleteZone.HINT_ZONE){
			startX = (int) ((w - textPaint.measureText(hintText)) / 2);
			startDY = (h -paddingBottom) / 2;
			startTY = (h - fontH-paddingBottom) / 2  - (int)fontMetrics.ascent;
			setMeasuredDimension(w, h);
		}else {
			if (hMode == MeasureSpec.AT_MOST) {
				h=uninstallBitmap.getHeight();
			}
			startX = (int) ((w - drawpadding - uninstallBitmap.getWidth() - textPaint.measureText(unText)) / 2);
			startDY = (h - uninstallBitmap.getHeight()) / 2 - paddingBottom;
			startTY = (h - fontH-paddingBottom) / 2  - (int)fontMetrics.ascent;
			if(hMode == MeasureSpec.EXACTLY)
			{
				setMeasuredDimension(w, h < uninstallBitmap.getHeight() ? uninstallBitmap.getHeight() : h);
			}else {
				setMeasuredDimension(w,  uninstallBitmap.getHeight());
			}
		
		}
		
	}
	/**
	 * 背景由小到大，由透明到不透明的动画
	 * */
	private void backDisplayAnimation(int time) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, getWidth() / 2, getHeight() / 2);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(scaleAnimation);
		set.addAnimation(alphaAnimation);
		set.setDuration(time);
		back.startAnimation(set);
	}
	/**
	 * 背景由大到小，由不透明到透明的动画
	 * */
	private void backMissAnimation(int time) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, getWidth() / 2, getHeight() / 2);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(scaleAnimation);
		set.addAnimation(alphaAnimation);
		set.setDuration(time);
		set.setFillEnabled(true);
		set.setFillAfter(true);
		back.startAnimation(set);
		
	}
	@Override
	public void onShow() {
		if (isDeleteZone() && mTrashView != null) {
			mTrashView.dropTrashHat();
		}
		if (Global.getLauncher() != null && Global.getLauncher().topMenuDrawstringView != null){
			Global.getLauncher().topMenuDrawstringView.hideDrawstring();
		}
	}
}
