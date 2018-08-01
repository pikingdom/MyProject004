package com.nd.hilauncherdev.launcher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.app.view.AppDrawerIconMaskTextView;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.data.widget.LauncherItemInfo;
import com.nd.hilauncherdev.drawer.view.DrawerSlidingView;
import com.nd.hilauncherdev.drawer.view.WidgetPreviewView;
import com.nd.hilauncherdev.folder.view.FolderBoxedViewGroup;
import com.nd.hilauncherdev.folder.view.FolderSlidingView;
import com.nd.hilauncherdev.framework.view.commonsliding.CommonViewHolder;
import com.nd.hilauncherdev.framework.view.draggersliding.DraggerChooseItem;
import com.nd.hilauncherdev.kitset.GpuControler;
import com.nd.hilauncherdev.kitset.util.HiLauncherEXUtil;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.launcher.config.CellLayoutConfig;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.view.DragView;

/**
 * 
 * Description: DragView扩展，以支持匣子中的多选拖动和小部件拖动
 * Author: guojy
 * Date: 2013-10-23 上午11:23:09
 */
public class DragViewWrapper extends DragView {
//	static final String TAG = "DragViewEx";
	
	public static final int ANI_DURATION = 200;
	
	/**
	 * 拖拽多选项列表
	 */
	private ArrayList<DraggerChooseItem> draggerChooseList;

	/**
	 * 是否正在做聚拢或散开动画
	 */
	private boolean isInAnimation = false;

	/**
	 * 是否在dragView中画多选层叠图标
	 */
	private boolean isDrawDraggerItemsInDragView = false;
	
	private int[] mCoordinatesTemp = new int[2];
	private int[] mCoordinatesTemp2 = new int[2];
	
	//======================处理从匣子中拖出小部件====================//
	private int mDrawerWidgetWidth;
	private int mDrawerWidgetHeight;
	private int mDrawerWidgetScaleWidth;
	private int mDrawerWidgetScaleHeight;
	private WeakReference<Drawable> widgetDrawableWkr = null;
	private Workspace mWorkspace;
	
/*	public DragViewWrapper(Context context, View view, int registrationX, int registrationY, int width, int height) {
		this(context, view, registrationX, registrationY, width, height, null);
	}*/
	
	public DragViewWrapper(Context context, View view, int registrationX, int registrationY, 
			int width, int height, ArrayList<DraggerChooseItem> draggerChooseList) {
		super(context, view, registrationX, registrationY, 
				width, height);
		
		this.draggerChooseList = draggerChooseList;
	}
	
	//==========================多选拖动处理=======================//
	@Override
	public void onDrawMultiSelected(Canvas canvas){
		if (draggerChooseList != null && draggerChooseList.size() > 1 && isDrawDraggerItemsInDragView) {
			int drawCount = 0;
			int end = draggerChooseList.size() >= 3 ? 3 : draggerChooseList.size();
			for (int i = end - 1; i >= 0; i--) {
				if (drawCount >= 2) break;
				DraggerChooseItem item = draggerChooseList.get(i);
				final View v = item.getView();
				if (v == dragingView) continue;
				
				canvas.save();
				canvas.translate(ScreenUtil.dip2px(getContext(), 5) * i, -ScreenUtil.dip2px(getContext(), 5) * i);
				if (v instanceof AppDrawerIconMaskTextView) {
					((AppDrawerIconMaskTextView) v).setEditMode(false);
				} else if (v instanceof FolderBoxedViewGroup) {
					ImageView choosedIcon = (ImageView) v.findViewById(R.id.item_choosed_icon);
					choosedIcon.setVisibility(View.INVISIBLE);
				}
				v.draw(canvas);
				if (v instanceof AppDrawerIconMaskTextView) {
					((AppDrawerIconMaskTextView) v).setEditMode(true);
				} else if (v instanceof FolderBoxedViewGroup) {
					ImageView choosedIcon = (ImageView) v.findViewById(R.id.item_choosed_icon);
					choosedIcon.setVisibility(View.VISIBLE);
				}
				canvas.restore();
				drawCount++;
			}
		}
	}
	
	public void show(final ViewGroup dragSrouce, int touchX, int touchY) {
		isInAnimation = false;
		if (dragSrouce instanceof DrawerSlidingView) {
			((DrawerSlidingView) dragSrouce).setInAnimation(isInAnimation);
		} else if (dragSrouce instanceof FolderSlidingView) {
			((FolderSlidingView) dragSrouce).setInAnimation(isInAnimation);
		}
		show(touchX, touchY);		
		gatherChooseItems(dragSrouce);
	}
	
	/**
	 * @author Anson, 2013-5-15 20:30 <br/>
	 * 拖拽多选项聚拢动画
	 */
	public void gatherChooseItems(final ViewGroup viewGroup) {
		if (draggerChooseList == null || draggerChooseList.size() <= 1)
			return;
		
		/**
		 * @author Anson, 2013-5-15 19:00 <br/>
		 * 处理拖拽多选图标的情况
		 */
		isInAnimation = true;
		if (viewGroup instanceof DrawerSlidingView) {
			((DrawerSlidingView) viewGroup).setInAnimation(isInAnimation);
		} else if (viewGroup instanceof FolderSlidingView) {
			((FolderSlidingView) viewGroup).setInAnimation(isInAnimation);
		}
		
		int currentScreen = 0;
		if (dragingView instanceof AppDrawerIconMaskTextView || dragingView instanceof FolderBoxedViewGroup) {
			/**
			 * 获取当前所在屏
			 */
			currentScreen = ((CommonViewHolder) dragingView.getTag(R.id.common_view_holder)).screen;
		}		
		
		final ViewGroup parent = (ViewGroup) dragingView.getParent();
		for (int i = 0; i < draggerChooseList.size(); i++) {
			DraggerChooseItem item = draggerChooseList.get(i);
			final View v = item.getView();
			ApplicationInfo info = item.getInfo();
			if (v == dragingView) continue;			
			
			if (parent == null) break;
			
			View mtv = null;
			if (v instanceof AppDrawerIconMaskTextView || v instanceof FolderBoxedViewGroup) {
				mtv = (AppDrawerIconMaskTextView) LayoutInflater.from(getContext()).inflate(R.layout.drawer_application_boxed, parent, false);
				((AppDrawerIconMaskTextView) mtv).setText(info.title);
				((AppDrawerIconMaskTextView) mtv).setTag(info);
				((AppDrawerIconMaskTextView) mtv).setIconBitmap(info.iconBitmap);
				((AppDrawerIconMaskTextView) mtv).setLazy(info.usingFallbackIcon);
				((AppDrawerIconMaskTextView) mtv).setEditMode(true);
				((AppDrawerIconMaskTextView) mtv).setChoosed(true);	
				
				int screen = ((CommonViewHolder) v.getTag(R.id.common_view_holder)).screen;
				mtv.layout((screen - currentScreen) * ScreenUtil.getCurrentScreenWidth(getContext()) + v.getLeft(), v.getTop(),
						(screen - currentScreen) * ScreenUtil.getCurrentScreenWidth(getContext()) + v.getRight(), v.getBottom());
				parent.addView(mtv);
			} 
			
			if (mtv == null) break;
			
			final View iv = mtv;			
	        
	        int[] startCoordinate = mCoordinatesTemp;
			int[] dropCoordinate = mCoordinatesTemp2;
			dragingView.getLocationOnScreen(startCoordinate);
			v.getLocationOnScreen(dropCoordinate);
			
			AnimationSet set = new AnimationSet(true);
			TranslateAnimation translateAnimation = new TranslateAnimation(0, startCoordinate[0] - dropCoordinate[0] + ScreenUtil.dip2px(getContext(), 5), 0, startCoordinate[1] - dropCoordinate[1]
					- ScreenUtil.dip2px(getContext(), 5));
			ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, mScale, 1.0f, mScale, Animation.ABSOLUTE, startCoordinate[0] - dropCoordinate[0], Animation.ABSOLUTE, startCoordinate[1]
					- dropCoordinate[1]);
			AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, (float) Global.ALPHA_155 / 255);
			set.addAnimation(translateAnimation);
			set.addAnimation(scaleAnimation);
			set.addAnimation(alphaAnimation);
			set.setDuration(ANI_DURATION);
			set.setFillAfter(true);
			set.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation ani) {
					
				}

				@Override
				public void onAnimationRepeat(Animation ani) {

				}

				@Override
				public void onAnimationEnd(Animation ani) {	
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							isDrawDraggerItemsInDragView = true;
							postInvalidate();
							iv.clearAnimation();
							parent.removeView(iv);
							isInAnimation = false;
							if (viewGroup instanceof DrawerSlidingView) {
								((DrawerSlidingView) viewGroup).setInAnimation(isInAnimation);
							} else if (viewGroup instanceof FolderSlidingView) {
								((FolderSlidingView) viewGroup).setInAnimation(isInAnimation);
							}
						}
					}, 100);			
				}
			});
			iv.startAnimation(set);
		}
	}
	
	/**
	 * @author Anson, 2013-5-15 20:30 <br/>
	 * 拖拽多选项散开动画
	 * 
	 * @param currentScreen
	 * @param viewGroup - 如DrawerSlidingView等横向滑屏组件
	 * @param isIngoreDragingView - 是否忽略dragingView的动画
	 * @param isClearDraggerChooseList - 是否在动画后清除多选拖拽列表
	 */
	public void disperseChooseItems(int currentScreen, final ViewGroup viewGroup, boolean isIngoreDragingView, final boolean isClearDraggerChooseList) {
		isInAnimation = false;
		if (viewGroup instanceof DrawerSlidingView) {
			((DrawerSlidingView) viewGroup).setInAnimation(isInAnimation);
		} else if (viewGroup instanceof FolderSlidingView) {
			((FolderSlidingView) viewGroup).setInAnimation(isInAnimation);
		}
		if (draggerChooseList == null || draggerChooseList.size() <= 1)
			return;
		
		/**
		 * @author Anson, 2013-5-15 19:00 <br/>
		 * 处理拖拽多选图标的情况
		 */
		isInAnimation = true;
		if (viewGroup instanceof DrawerSlidingView) {
			((DrawerSlidingView) viewGroup).setInAnimation(isInAnimation);
		} else if (viewGroup instanceof FolderSlidingView) {
			((FolderSlidingView) viewGroup).setInAnimation(isInAnimation);
		}
					
		final ViewGroup parent = (ViewGroup) viewGroup.getChildAt(currentScreen);
		final View firstViewInParent = parent.getChildAt(0);
		for (int i = 0; i < draggerChooseList.size(); i++) {
			DraggerChooseItem item = draggerChooseList.get(i);
			final View v = item.getView();
			ApplicationInfo info = item.getInfo();
			if (isIngoreDragingView) {
				if (v == dragingView) continue;
			}
			
			if (parent == null) break;
			
			View mtv = null;
			if (v instanceof AppDrawerIconMaskTextView || v instanceof FolderBoxedViewGroup) {
				mtv = (AppDrawerIconMaskTextView) LayoutInflater.from(getContext()).inflate(R.layout.drawer_application_boxed, parent, false);
				((AppDrawerIconMaskTextView) mtv).setText(info.title);
				((AppDrawerIconMaskTextView) mtv).setTag(info);
				((AppDrawerIconMaskTextView) mtv).setIconBitmap(info.iconBitmap);
				((AppDrawerIconMaskTextView) mtv).setLazy(info.usingFallbackIcon);
				((AppDrawerIconMaskTextView) mtv).setEditMode(true);
				((AppDrawerIconMaskTextView) mtv).setChoosed(true);	
				
				this.getLocationOnScreen(mCoordinatesTemp);
				firstViewInParent.getLocationOnScreen(mCoordinatesTemp2);
				mtv.layout(firstViewInParent.getLeft() + (mCoordinatesTemp[0] - mCoordinatesTemp2[0] + ScreenUtil.dip2px(getContext(), 5)), firstViewInParent.getTop()
						+ (mCoordinatesTemp[1] - mCoordinatesTemp2[1] - ScreenUtil.dip2px(getContext(), 5)),
						firstViewInParent.getRight() + (mCoordinatesTemp[0] - mCoordinatesTemp2[0] + ScreenUtil.dip2px(getContext(), 5)), firstViewInParent.getBottom() + (mCoordinatesTemp[1] - mCoordinatesTemp2[1] - ScreenUtil.dip2px(getContext(), 5)));
				parent.addView(mtv);
			}
			
			if (mtv == null) break;
			
			final View iv = mtv;	
			final int index = i;
	        
			int[] startCoordinate = mCoordinatesTemp;
			int[] dropCoordinate = mCoordinatesTemp2;
			iv.getLocationOnScreen(dropCoordinate);
			v.getLocationOnScreen(startCoordinate);
						
			AnimationSet set = new AnimationSet(true);
			TranslateAnimation translateAnimation = new TranslateAnimation(0, startCoordinate[0] - dropCoordinate[0], 0, startCoordinate[1] - dropCoordinate[1]);
			ScaleAnimation scaleAnimation = new ScaleAnimation(mScale, 1.0f, mScale, 1.0f, Animation.ABSOLUTE, startCoordinate[0] - dropCoordinate[0],
					Animation.ABSOLUTE, startCoordinate[1] - dropCoordinate[1]);
			AlphaAnimation alphaAnimation = new AlphaAnimation((float) Global.ALPHA_155 / 255, 1.0f);
			set.addAnimation(translateAnimation);
			set.addAnimation(scaleAnimation);
			set.addAnimation(alphaAnimation);
			set.setDuration(ANI_DURATION);
			set.setFillAfter(true);
			set.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation ani) {
					isDrawDraggerItemsInDragView = false;
					if (index == 1 && isClearDraggerChooseList) {
						/**
						 * 拖拽放手后，清除多选项
						 */
						if (viewGroup instanceof DrawerSlidingView) {
							((DrawerSlidingView) viewGroup).clearDraggerChooseList(dragingView, true, false);
						} else if (viewGroup instanceof FolderSlidingView) {
							((FolderSlidingView) viewGroup).clearDraggerChooseList(dragingView, true, false);
						}
					}
				}

				@Override
				public void onAnimationRepeat(Animation ani) {

				}

				@Override
				public void onAnimationEnd(Animation ani) {	
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							iv.clearAnimation();
							parent.removeView(iv);
							v.setVisibility(VISIBLE);
							isInAnimation = false;
							if (viewGroup instanceof DrawerSlidingView) {
								((DrawerSlidingView) viewGroup).setInAnimation(isInAnimation);
							} else if (viewGroup instanceof FolderSlidingView) {
								((FolderSlidingView) viewGroup).setInAnimation(isInAnimation);
							}
							if (v instanceof AppDrawerIconMaskTextView) {
								((AppDrawerIconMaskTextView) v).setInDrag(false);
								v.postInvalidate();
							} else if (v instanceof FolderBoxedViewGroup) {
								((FolderBoxedViewGroup) v).setInDrag(false);
								v.postInvalidate();
							} 
							if (index == 1 && isClearDraggerChooseList) {
								if (dragingView instanceof AppDrawerIconMaskTextView) {
									((AppDrawerIconMaskTextView) dragingView).setChoosed(false);
									((AppDrawerIconMaskTextView) dragingView).setInDrag(false);
									dragingView.postInvalidate();
								} else if (dragingView instanceof FolderBoxedViewGroup) {
									((FolderBoxedViewGroup) dragingView).setInDrag(false);
									ImageView closeBtn = (ImageView) dragingView.findViewById(R.id.item_close_btn);
									ImageView choosedIcon = (ImageView) dragingView.findViewById(R.id.item_choosed_icon);
									CommonViewHolder viewHolder = (CommonViewHolder) dragingView.getTag(R.id.common_view_holder);
									if (viewHolder == null)
										return;
									boolean isSystem = false;
									if (viewHolder.item instanceof ApplicationInfo) {
										isSystem = ((ApplicationInfo) viewHolder.item).isSystem == 1;
									}
									if (!isSystem && !HiLauncherEXUtil.is91HomeLauncher(((ApplicationInfo) viewHolder.item).componentName)) {
										closeBtn.setVisibility(VISIBLE);
									}
									choosedIcon.setVisibility(INVISIBLE);
									v.postInvalidate();
								}
							}
						}
					}, 100);			
				}
			});
			iv.startAnimation(set);
		}
	}
	

	public void setDraggerChooseList(ArrayList<DraggerChooseItem> draggerChooseList) {
		this.draggerChooseList = draggerChooseList;
	}
	
	/**
	 * Move the window containing this view.
	 * 
	 * @param touchX
	 *            the x coordinate the user touched in screen coordinates
	 * @param touchY
	 *            the y coordinate the user touched in screen coordinates
	 */
	public void move(int touchX, int touchY) {
		if (isInAnimation) return;		
		super.move(touchX, touchY);
	}
	
	public void setWorkspace(Workspace mWorkspace){
		this.mWorkspace = mWorkspace;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (dragingView != null) {
			if(isWidgetDragViewFromDrawer()){//程序匣子中Widget预览拖动做特殊处理
				setMeasuredDimensionForDrawerWidget();
			}else{
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
		} 
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(isWidgetDragViewFromDrawer()){//程序匣子中Widget拖动View处理
			onDrawWidgetDragViewFromDrawer(canvas);
			return;
		}
		super.onDraw(canvas);
	}
	
	@Override
	public void remove() {
		super.remove();
		cleanDrawerWidgetColorFilter();
		cleanWidgetDragViewFromDrawer();
	}
	
	/**
	 * Description: 校正Dragview位置
	 * Author: guojy
	 * Date: 2012-8-31 下午03:57:52
	 */
	@Override
	protected void adjustTouchLocation(int[] touch){
		if(isWidgetDragViewFromDrawer()){//拖动匣子中Widget
			touch[0] -= mDrawerWidgetScaleWidth/2;
			touch[1] -= mDrawerWidgetScaleHeight/2;
		}else{
			super.adjustTouchLocation(touch);
		}
	}
	
	@Override
	public void show(int touchX, int touchY) {
		int width, height;
		if(isWidgetDragViewFromDrawer()){
			initDrawerWidgetSetting();
			width = mDrawerWidgetScaleWidth;
	        height = mDrawerWidgetScaleHeight;
		}else{
			width = dragingView.getWidth();
	        height = dragingView.getHeight();
		}
		int[] touch = {touchX, touchY};
		adjustTouchLocation(touch);
        
		super.show(touch[0], touch[1], width, height);
		GpuControler.enableSoftwareLayers(this);
	}
	
	private void setMeasuredDimensionForDrawerWidget(){
		setMeasuredDimension(mDrawerWidgetScaleWidth, mDrawerWidgetScaleHeight);
	}
	/**
	 * Description: 程序匣子中Widget拖动View处理
	 * Author: guojy
	 * Date: 2012-10-25 下午05:12:36
	 */
	private void onDrawWidgetDragViewFromDrawer(Canvas canvas){
		WidgetPreviewView wpv = (WidgetPreviewView)dragingView;
		int widgetWidth = mDrawerWidgetScaleWidth;// canvas.getWidth();
		int widgetHeight = mDrawerWidgetScaleHeight;// canvas.getHeight();
		if (widgetDrawableWkr == null || widgetDrawableWkr.get() == null) {
			widgetDrawableWkr = new WeakReference<Drawable>(wpv.getDragView(widgetWidth, widgetHeight));
		}
		final Drawable drawable = widgetDrawableWkr.get();
		if (drawable != null) {
			drawable.setBounds(0, 0, widgetWidth, widgetHeight);
			if (mPaint != null) {
				drawable.setColorFilter(mPaint.getColorFilter());
			}
			drawable.draw(canvas);
		}
		
		cleanWidgetDragViewFromDrawer();
	}
	
	//拖放结束后清除widgetDrawableWkr
	private void cleanWidgetDragViewFromDrawer(){
		if(isRemovedDragView && widgetDrawableWkr != null && widgetDrawableWkr.get() != null){
			widgetDrawableWkr.get().setCallback(null);
			widgetDrawableWkr.clear();
			widgetDrawableWkr = null;
		}
	}
	/**
	 * Description: 计算匣子中Widget预览的参数
	 * Author: guojy
	 * Date: 2012-8-31 下午02:53:41
	 */
	private void initDrawerWidgetSetting(){
		WidgetPreviewView wpv = (WidgetPreviewView)dragingView;
		
		//匣子中Widget在拖拽时,若匣子还可见，则拖拽的Widget预览图大小按匣子中布局大小算
		/*if (wpv != null) {
			if (mWorkspace.getLauncher().isAllAppsVisible()) {
				mDrawerWidgetScaleWidth = wpv.getActualWidth();
				mDrawerWidgetScaleHeight = wpv.getActualHeight();
				return;
			}
		}*/
		
		if(mDrawerWidgetWidth == 0 && wpv != null){
			mDrawerWidgetWidth = wpv.previewWidth > 0 ? wpv.previewWidth : 100;
			mDrawerWidgetHeight = wpv.previewHeight > 0 ? wpv.previewHeight : 100;
		}
		
		int clWidth = mWorkspace.getCellLayoutAt(0).getWidth();
		int clHeight = mWorkspace.getCellLayoutAt(0).getHeight();
		float springScale = mWorkspace.getSpringScale();
		float fix = 20;
		float width = clWidth*springScale - fix;
		float height = clHeight*springScale - fix;
		
		LauncherItemInfo item = (LauncherItemInfo)wpv.getTag();
		int spanX = item.spanX;
		int spanY = item.spanY;
		
		float scaleX = (width*spanX/CellLayoutConfig.getCountX()) / mDrawerWidgetWidth;
		float scaleY = (height*spanY/CellLayoutConfig.getCountY()) / mDrawerWidgetHeight;
		float scale = Math.min(scaleX, scaleY);
		
		mDrawerWidgetScaleWidth = (int) (mDrawerWidgetWidth * scale);
		mDrawerWidgetScaleHeight = (int) (mDrawerWidgetHeight * scale);
	}
	/**
	 * Description: 是否拖动程序匣子中Widget预览
	 * Author: guojy
	 * Date: 2012-8-31 下午02:51:41
	 */
	public boolean isWidgetDragViewFromDrawer(){
		return dragingView != null && dragingView instanceof WidgetPreviewView;
	}
//	private WidgetPreviewView getWidgetPreviewView(){
//		return isWidgetDragViewFromDrawer() ? (WidgetPreviewView)dragingView : null;
//		if(mDrawerWidgetView != null) 
//			return mDrawerWidgetView;
//		
//		if(dragingView instanceof LinearLayout){
//			LinearLayout dragView = (LinearLayout)dragingView;
//			int len = dragView.getChildCount();
//			for(int i = 0; i < len; i ++){
//				if(dragView.getChildAt(i) instanceof WidgetPreviewView){
//					mDrawerWidgetView = (WidgetPreviewView) dragView.getChildAt(i);
//					return mDrawerWidgetView;
//				}
//			}
//		}
//		return null;
//	}
	
	//清除拖动匣子小部件的颜色过滤设置
	private void cleanDrawerWidgetColorFilter(){
		if(!isWidgetDragViewFromDrawer())
			return;
		
		WidgetPreviewView wpv = (WidgetPreviewView)dragingView;
		if (widgetDrawableWkr == null || widgetDrawableWkr.get() == null) {
			widgetDrawableWkr = new WeakReference<Drawable>(wpv.getDragView(mDrawerWidgetScaleWidth, mDrawerWidgetScaleHeight));
		}
		final Drawable drawable = widgetDrawableWkr.get();
		drawable.setColorFilter(null);
	}
	/**
	获取widget的示意图的bitamp
	*/
	public Drawable getWidgetDrawBitmap(Point point){
		WidgetPreviewView wpv = (WidgetPreviewView)dragingView;
		int widgetWidth = mDrawerWidgetScaleWidth;// canvas.getWidth();
		int widgetHeight = mDrawerWidgetScaleHeight;// canvas.getHeight();
		if (widgetDrawableWkr == null || widgetDrawableWkr.get() == null) {
			widgetDrawableWkr = new WeakReference<Drawable>(wpv.getDragView(widgetWidth, widgetHeight));
		}
		final Drawable drawable = widgetDrawableWkr.get();
		
		if(mDrawerWidgetWidth == 0 && wpv != null){
			widgetWidth = wpv.previewWidth > 0 ? wpv.previewWidth : 100;
			widgetHeight = wpv.previewHeight > 0 ? wpv.previewHeight : 100;
		}else {
			widgetWidth=mDrawerWidgetWidth;
			widgetHeight=mDrawerWidgetHeight;
		}
		
		int clWidth = mWorkspace.getCellLayoutAt(0).getWidth();
		int clHeight = mWorkspace.getCellLayoutAt(0).getHeight();
		float springScale = mWorkspace.getSpringScale();
		float fix = 20;
		float width = clWidth*springScale - fix;
		float height = clHeight*springScale - fix;
		
		LauncherItemInfo item = (LauncherItemInfo)wpv.getTag();
		int spanX = item.spanX;
		int spanY = item.spanY;
		
		float scaleX = (width*spanX/CellLayoutConfig.getCountX()) / widgetWidth;
		float scaleY = (height*spanY/CellLayoutConfig.getCountY()) / widgetHeight;
		float scale = Math.min(scaleX, scaleY);
		
		point.x = (int) (widgetWidth * scale);
		point.y = (int) (widgetHeight * scale);
		return drawable;
	}
	
	
	/**
	 * 
	 * 匣子中Widget在拖拽到桌面时，拖拽的Widget预览图大小按原来的算法重算
	 * 并重新修改拖拽预览图的高宽
	 */
	public void resetDrawerWidgetScaleWidthAndHeight() {
		initDrawerWidgetSetting();
		LayoutParams lp = this.getLayoutParams();
		lp.width = mDrawerWidgetScaleWidth;
		lp.height = mDrawerWidgetScaleHeight;
	}
	
}
