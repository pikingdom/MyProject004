package com.nd.hilauncherdev.launcher;

import android.app.IWallpaperManager;
import android.app.IWallpaperManagerCallback;
import android.app.WallpaperInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.newline.launcher.R;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.view.DrawerMainView;
import com.nd.hilauncherdev.drawer.view.DrawerSlidingView;
import com.nd.hilauncherdev.framework.view.draggersliding.DrawerLayout;
import com.nd.hilauncherdev.kitset.util.ReflectUtilEx;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.WallpaperUtil;
import com.nd.hilauncherdev.kitset.util.WallpaperUtil2;
import com.nd.hilauncherdev.launcher.addboot.LauncherAddMainView;
import com.nd.hilauncherdev.launcher.config.ConfigFactory;
import com.nd.hilauncherdev.launcher.screens.CellLayout;
import com.nd.hilauncherdev.launcher.screens.DragLayer;
import com.nd.hilauncherdev.launcher.screens.dockbar.MagicDockbarRelativeLayout;
import com.nd.hilauncherdev.launcher.support.WallpaperHelper;
import com.nd.hilauncherdev.settings.SettingsPreference;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public class LauncherAnimationHelp {

	/**
	 * 模糊背景的View
	 * */
	static BlurView mBlurView;
	/**
	 * 阴影的view
	 * */
//	static View mShadowView;
	/**
	 * 保存模糊的后的图像
	 * */
	static Bitmap mBitmap;

	/**
	 * 直接显示一个模糊的壁纸效果,无动画效果
	 * */
	private static BlurView mBlurView2 = null;
	
	/**
	 * 背景缩小的比例
	 * */
	private static final float scale = 0.8f;

	/**
	 * 背景变小的动画时间
	 * */
	private final static int NARROW_TIME = 250;
	/**
	 * 背景变小的动画时间
	 * */
	private final static int RESTORE_TIME = 240;
	
	/** 适配小米手机打开应用关闭文件夹动画时长，修复桌面底部不可见bug  */
	public static boolean MIUI_CLOSE_FOLDER_NO_ANI = false; 
	private final static int MIUI_NO_ANI_RESTORE_TIME = 50;
	
	/**
	 * 背景变暗以及模糊的时间
	 * */
	private final static int BLUR_TIME = 150;
	/**
	 * 背景变清晰的时间
	 * */
	private final static int DISTINCT_TIME = 10;

	private final static float SHADOW_ALPHA = 0.6f;

	private static Object lock = new Object();
	
	private static int mPaperH=0;
	
	
	private static int mState=0;

	/**
	 * 是否只模糊壁纸
	 * */
	private static boolean mIsOnlyBlurWP;
	/**
	 * 缩略图的比例
	 * 
	 * */
	static float thumb_scale = 0.25f;
	
	private static final int Sample_Size=8;
	/**
	 * 当前的动画对象
	 * 
	 * */
	private static AnimatorSet mCurrAnimatorSet;

	private static Bitmap wallPaperBitmap;
	/**
	 * 忽略第三阶段动画，直接进入第个阶段动画
	 * 
	 * */
	private static boolean mIgnoreThirdSection = false;

	/**
	 * 是否需要缩放动画
	 * 
	 * */
	private static boolean mShowScaleAnim = true;
	private final static boolean PARALLEL_ANIMATION = true;
	private static View mCurrenCellLayoutView=null;
	
	private static DecelerateInterpolator mInterpolator = new DecelerateInterpolator();

	
	/**
	 * 壁纸是否是从自绘中取出
	 * */
	private static boolean isDrawWallPaper=false;
	
	public static final String WallPaperColorChange="com.android.pandahome.internal.wallpaper.color";

	/**
	 * so是否加载成功
	 * */
	private static boolean isLoadSoSucceed=false;
	
	/**
	 * 壁纸为动态壁纸时，壁纸背景的颜色
	 * */
	private static final int wallPaperColor = 0xB21E1E1E;
	
	/**
	 * 返回1表示成功，返回1表示已经进入模糊不可重复进入，返回2表示模糊失败
	 * */
	public static int displayAnimation(Launcher launcher, boolean isScreen, boolean showScaleAnim) {
		// long t1=System.currentTimeMillis();
		
		if (mState == 1) {
			Log.e("zhou", "displayAnimation:state error");
			return 1;
		}
		mState=1;
		if (mCurrAnimatorSet != null) {
			mCurrAnimatorSet.cancel();
		}
		/**
		 * 这个必须在打取消动画currAnimatorSet.cancel(); 后面调用，因为取消动画时，会取清除bitmap变量
		 * */
		if (showScaleAnim) {
			mBitmap = LauncherAnimationHelp.getDrawCache(launcher, 0.8f, isScreen);
		} else {
			mBitmap = LauncherAnimationHelp.getDrawCache(launcher, 1, isScreen);
		}
		if(mBitmap==null)return 2;
		if (PARALLEL_ANIMATION) {
			if (mIsOnlyBlurWP) {
				//Log.e("zhou", "displayAnimation: disPlayBlurWallPaper");
				disPlayBlurWallPaper(launcher, isScreen);
			} else {
				//Log.e("zhou", "displayAnimation:narrowAndBlur");
				try {
					narrowAndBlur(launcher, isScreen, showScaleAnim);
				} catch (Exception e) {
					mState=0;
					e.printStackTrace();
					return 2;
				}
				
			}
			
		}
		return 0;
	}

	public static void blankAnimation(Launcher launcher, boolean isScreen, boolean showScaleAnim) {
		if (mState == 0) {
			Log.e("zhou", "blankAnimation: state error");
			return;
		}
		
		if (mCurrAnimatorSet != null) {
			mCurrAnimatorSet.cancel();
		}
		mState=0;
		if (PARALLEL_ANIMATION) {
			if (mIsOnlyBlurWP) {
				//Log.e("zhou", "blankAnimation: missBlurWallPaper");
				missBlurWallPaper(launcher, isScreen);
			}else {
				//Log.e("zhou", "blankAnimation: restoreAndClear");
				try {
					restoreAndClear(launcher, isScreen, showScaleAnim);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		}

	}

	static class LauncherAnimatorListener implements AnimatorListener {
		public LauncherAnimatorListener(Launcher launcher, boolean isScreen, int type, boolean showScaleAnim) {
			mLauncher = launcher;
			mIsScreen = isScreen;
			mType = type;
			mCancelled = false;
			mShowScaleAnim = showScaleAnim;
		}

		Launcher mLauncher;
		boolean mIsScreen;
		int mType = 0;
		boolean mCancelled;
		boolean mShowScaleAnim;

		@Override
		public void onAnimationCancel(Animator arg0) {
			// Log.e("zhou", "onAnimationCancel");
			mCancelled = true;
			switch (mType) {
			case 1:
				// Log.e("zhou", "第一阶段");
				break;
			case 2:
				// Log.e("zhou", "第二阶段");
				break;
			case 3:
				// Log.e("zhou", "第三阶段");
				break;
			case 4:
				// Log.e("zhou", "第四阶段");
				break;
			default:
				break;
			}
		}

		@Override
		public void onAnimationEnd(Animator arg0) {

			// Log.e("zhou", "onAnimationEnd");
			/**
			 * 以下四个clearCurrAnimatorSet函数的调用必须在 每个分支的第一句调动，
			 * 因为后面的动画效果会重新保存新的动画变量，
			 * 
			 * 如果clearCurrAnimatorSet在后面调用就可能把保存的最新的动画 变量给释放了，这样逻辑会出错
			 * 
			 * */

			if (PARALLEL_ANIMATION) {
				onAnimationEndEx(mType, mLauncher, mIsScreen);
			}
		}

		@Override
		public void onAnimationRepeat(Animator arg0) {
		}

		@Override
		public void onAnimationStart(Animator arg0) {
		}

	}

	public static void onAnimationEndEx(int type, Launcher mLauncher, boolean mIsScreen) {

		// Log.e("zhou", "onAnimationEnd");
		/**
		 * 以下四个clearCurrAnimatorSet函数的调用必须在 每个分支的第一句调动， 因为后面的动画效果会重新保存新的动画变量，
		 * 
		 * 如果clearCurrAnimatorSet在后面调用就可能把保存的最新的动画 变量给释放了，这样逻辑会出错
		 * 
		 * */
		clearCurrAnimatorSet();

		switch (type) {
		case 1:
			// Log.e("zhou", "第一阶段");
			break;
		case 2:
			break;
		case 3:
			break;

		case 4:
			// Log.e("zhou", "第四阶段");
			removeBlurBGAndShade(mLauncher);
			mIsOnlyBlurWP=false;
			break;
		default:
			break;
		}

	}

	static void setCurrAnimatorSet(AnimatorSet animator) {
		// Log.e("zhou", "保存动画变量");
		mCurrAnimatorSet = animator;
	}

	static void clearCurrAnimatorSet() {
		// Log.e("zhou", "释放动画变量");
		mCurrAnimatorSet = null;
	}
	
	/**
	 * 获取模糊桌面
	 * <p>Title: getBlurBG</p>
	 * <p>Description: </p>
	 * @param isScreen
	 * @param launcher
	 * @author maolinnan_350804
	 */
	public static Bitmap getBlurBG(boolean isScreen, Launcher launcher){
		return getBlurBG(isScreen,launcher,true);
	}
	public static Bitmap getBlurBG(boolean isScreen, Launcher launcher,boolean haveViewBg){
		return getBlurBitmap(isScreen,launcher,haveViewBg,20,3);
	}

	public static Bitmap getBlurBG(Launcher launcher){
		Bitmap bm=null;
		if (bm == null){
			bm = Bitmap.createBitmap((int) (ScreenUtil.getScreenWH()[0] * thumb_scale), (int) (ScreenUtil.getScreenWH()[1] * thumb_scale), Bitmap.Config.ARGB_8888);
		}
		Canvas canvas = new Canvas();
		canvas.setBitmap(bm);
		drawSingleWallpaper(canvas,(int) (bm.getWidth() / thumb_scale), (int) (bm.getHeight() / thumb_scale), thumb_scale, launcher);
		try {
			native_blur(bm, 5, 2, -1, -1);
		}catch (Throwable e){
			e.printStackTrace();
		}
		canvas.drawColor(Color.parseColor("#4c000000"));
		return bm;
	}
	
	private static void addBlurBGAndShade(boolean isScreen, Launcher launcher) {
		Canvas canvas = new Canvas();
		canvas.setBitmap(mBitmap);
		
		if(!isScreen && !SettingsPreference.getInstance().getDrawerBgTransparent())
		{
			drawDrawMainViewBG(canvas, (int) (mBitmap.getWidth() / thumb_scale), (int) (mBitmap.getHeight() / thumb_scale), thumb_scale, launcher);
		}
		else{
			drawSingleWallpaper(canvas, (int) (mBitmap.getWidth() / thumb_scale), (int) (mBitmap.getHeight() / thumb_scale), thumb_scale, launcher);
		}
		if (isLoadSoSucceed) {
			try {
				native_blur(mBitmap, 5, 2, -1, -1);
			} catch (Error e) {
				e.printStackTrace();
				mBitmap.eraseColor(0);
			}
			
		}
		// Log.e("zhou", "blur time=" + (System.currentTimeMillis() - t));
		mBlurView = new BlurView(launcher.getApplicationContext());
		mBlurView.setBlurBitmap(mBitmap);
		
		if(launcher.isTranslucentNavigationBar()){//导航栏透明，改写背景添加位置
			launcher.addLauncherBgView(mBlurView, 1);
		}else{
			DragLayer mDragLayer = launcher.getDragLayer();
			if (isScreen) {
				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
				mDragLayer.addView(mBlurView, 0, lp);
			} else {
				
				boolean found = false;
				for (int i = mDragLayer.getChildCount(); i > 0; i--) {
					View view = mDragLayer.getChildAt(i);
					if (view instanceof DrawerMainView) {
						mDragLayer.addView(mBlurView, i + 1);
						found = true;
					}
				}
				if (!found) {
					mDragLayer.addView(mBlurView, mDragLayer.getChildCount() - 3);
					found = true;
				}
				
			}
		}
	}

	private static void removeBlurBGAndShade(Launcher launcher) {
		// Log.e("zhou", "removeBlurBGAndShade");
		if (mBlurView != null && mBlurView.getParent() != null) {
			((ViewGroup)mBlurView.getParent()).removeView(mBlurView);
			mBlurView = null;
		}
		mBitmap = null;
	}

	private static Bitmap getDrawCache(Launcher launcher, float scale, boolean isScreen) {
		if(launcher==null) return null;
		int count = 0;
		int h=0;
		DragLayer mDragLayer = launcher.getDragLayer();
		if (mDragLayer != null) {
			h = mDragLayer.getHeight();
		} else {
			h = ScreenUtil.getScreenWH()[1];
		}
		int w = ScreenUtil.getScreenWH()[0];
		if (h <= 0) {
			h = ScreenUtil.getScreenWH()[1];
		}
		
		Bitmap bitmap = Bitmap.createBitmap((int) (w * thumb_scale), (int) (h * thumb_scale), Bitmap.Config.ARGB_8888);
		if (!isLoadSoSucceed) {
			return bitmap;
		}
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);

		if (mIsOnlyBlurWP) {
			return bitmap;
		}
		float offsetX = w * thumb_scale * (1 - scale) / 2;
		float offsetY = h * thumb_scale * (1 - scale) / 2;

		Workspace mWorkspace = launcher.getWorkspace();
		
		if (mDragLayer == null || mWorkspace == null) {
			return bitmap;
		}
		MagicDockbarRelativeLayout bottomContainer = (MagicDockbarRelativeLayout) launcher.getBottomContainer();
		if (isScreen) {
			// 屏幕
			CellLayout cellLayout = mWorkspace.getCurrentCellLayout();
			if (cellLayout != null) {
				count = canvas.save();
				canvas.translate(offsetX, offsetY);
				canvas.scale(scale, scale);
				canvas.scale(thumb_scale, thumb_scale);
				canvas.translate(0, cellLayout.getTop());
				cellLayout.draw(canvas);
				canvas.restoreToCount(count);
			}
			
			// dock栏

			if (bottomContainer != null) {
				// t = System.currentTimeMillis();
				count = canvas.save();
				canvas.translate(offsetX, offsetY);
				canvas.scale(scale, scale);
				canvas.scale(thumb_scale, thumb_scale);
				canvas.translate(0, bottomContainer.getTop());
				bottomContainer.draw(canvas);
				canvas.restoreToCount(count);
				// Log.e("zhou",
				// "dock cache time="+(System.currentTimeMillis()-t));
			}
			// Log.e("zhou", "dock cache time=" + (System.currentTimeMillis() -
			// t));

		} else {
			// 匣子

			int[] location = new int[2];
			// 程序匣子头部模块
			View drawerTopView = (LinearLayout) mDragLayer.findViewById(R.id.drawer_top_layout);
			if (drawerTopView != null) {
				drawerTopView.getLocationOnScreen(location);

				canvas.save();
				canvas.translate(offsetX, offsetY);
				canvas.scale(scale, scale);
				canvas.scale(thumb_scale, thumb_scale);
				canvas.translate(location[0], location[1]);
				drawerTopView.draw(canvas);
				canvas.restore();
			}
			// 程序匣子应用列表模块
			DrawerSlidingView slidingView = (DrawerSlidingView) mDragLayer.findViewById(R.id.drawer_sliding_view);
			DrawerLayout drawerCellLayout=null;
			if (slidingView != null) {
				drawerCellLayout = (DrawerLayout) slidingView.getChildAt(slidingView.getCurrentScreen());
			}
			if (drawerCellLayout != null) {
				drawerCellLayout.getLocationOnScreen(location);
				count = canvas.save();
				canvas.translate(offsetX, offsetY);
				canvas.scale(scale, scale);
				canvas.scale(thumb_scale, thumb_scale);
				canvas.translate(location[0], location[1]);
				drawerCellLayout.draw(canvas);
				canvas.restoreToCount(count);
			}
			// 程序匣子滚动条

			View drawerLightBar = mDragLayer.findViewById(R.id.drawer_lightbar);
			if (drawerLightBar != null) {
				drawerLightBar.getLocationOnScreen(location);
				count = canvas.save();
				canvas.translate(offsetX, offsetY);
				canvas.scale(scale, scale);
				canvas.scale(thumb_scale, thumb_scale);
				canvas.translate(location[0], location[1]);
				drawerLightBar.draw(canvas);
				canvas.restoreToCount(count);
				
			}

			// 程序匣子底部
			View drawerBottomView = (RelativeLayout) mDragLayer.findViewById(R.id.drawer_bottom_layout);
			if (drawerBottomView != null) {
				drawerBottomView.getLocationOnScreen(location);
				count = canvas.save();
				canvas.translate(offsetX, offsetY);
				canvas.scale(scale, scale);
				canvas.scale(thumb_scale, thumb_scale);
				canvas.translate(location[0], location[1]);
				drawerBottomView.draw(canvas);
				canvas.restoreToCount(count);
			}
		}
		return bitmap;
	}

	/**
	 * 是否是HTC One
	 * 
	 * @return boolean
	 */
	public static boolean isHTC_ONE_X() {
		try {
			return TelephoneUtil.getMachineName().contains("HTC One X");
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取壁纸
	 */
	private static void drawSingleWallpaper(Canvas canvas, int screenWidth, int screenHeight, float scale, Launcher lanucher) {
		
		if (!isLoadSoSucceed) {
			return;
		}
		
		Rect srcRect = new Rect();
		Rect destRect = new Rect();
		//Bitmap bitmap = null;
		// long t;
		synchronized (lock) {
			//wallPaperBitmap=null;
			if (wallPaperBitmap == null) {
				wallPaperBitmap=getWallpaperByService(Sample_Size);
					
			}
			//Log.e("zhou", "drawSingleWallpaper: drawSingleWallpaper");
			Workspace workspace = lanucher.getWorkspace();
			// 绘制壁纸
			int paperWidth = screenWidth * 2;
			if(WallpaperUtil.isNewSingleScreen(lanucher)) {
				paperWidth = screenWidth;
			}
			int paperHeight = 0;
			
			if (mPaperH > 0) {
				paperHeight = mPaperH;
			} else {
				paperHeight = screenHeight;
			}
			float bitmapScale = 0;

			if (wallPaperBitmap != null) {
				if (isHTC_ONE_X()) {
					paperHeight = screenHeight + 96;
				}
				if (isNewAPI()) {
					drawWallpaperNewＳtrategy(canvas, screenWidth, screenHeight, lanucher);
					return;
				}
				
				int bitmapW=0;
				int bitmapH=0; 
				if (isDrawWallPaper) {
					bitmapW = wallPaperBitmap.getWidth();
					bitmapH = wallPaperBitmap.getHeight();
				} else {
					bitmapW = wallPaperBitmap.getWidth() * Sample_Size;
					bitmapH = wallPaperBitmap.getHeight() * Sample_Size;
				}
				

				int childCount = workspace.getChildCount();
				int deltaw = paperWidth - bitmapW;
				int deltah = paperHeight - bitmapH;

				if (deltaw > 0 || deltah > 0) {
					// We need to scale up so it covers the entire
					// area.

					if (deltaw > deltah) {
						bitmapScale = paperWidth / (float) bitmapW;
					} else {
						bitmapScale = paperHeight / (float) bitmapH;
					}
					bitmapW = (int) (bitmapW * bitmapScale);
					bitmapH = (int) (bitmapH * bitmapScale);
					deltaw = paperWidth - bitmapW;
					deltah = paperHeight - bitmapH;
				}

				/** 壁纸滚动单位距离 */
				int stepX = 0;
				// int startx = 0;
				boolean isShowNavi = SettingsPreference.getInstance().isShowNavigationView();
				isShowNavi &= SettingsPreference.getInstance().isWallpaperScrollingOnZeroView();
				if (childCount >= 1) {
					if (isShowNavi) {
						stepX = (paperWidth - screenWidth) / (childCount + 1);
					} else {
						stepX = (paperWidth - screenWidth) / (childCount);
					}
				}

				// 壁纸被裁减的区域,不滚动的起始位置
				if (!ConfigFactory.isWallpaperRolling(lanucher)) {
					if (WallpaperUtil.isNewSingleScreen(lanucher)) {
						srcRect.left = 0;
					}else {
						srcRect.left = screenWidth / 2;
					}

				} else if (isShowNavi) {
					srcRect.left = stepX / 2 + stepX * (lanucher.mWorkspace.getCurrentScreen() + 1);

				} else {
					srcRect.left = stepX / 2 + stepX * (lanucher.mWorkspace.getCurrentScreen());
				}
				srcRect.left = srcRect.left - deltaw / 2;

				srcRect.right = srcRect.left + screenWidth;
				srcRect.top = -deltah / 2;
				srcRect.bottom = srcRect.top + screenHeight;

				// HTC ONE 手机的壁纸上下面会有透明条，壁纸区域不同

				// 壁纸将绘制到画布上的区域
				destRect.left = 0;
				destRect.top = 0;
				destRect.right = (int) (screenWidth * scale);
				destRect.bottom = (int) (screenHeight * scale);

				srcRect.left = (int) ((srcRect.left / (float) bitmapW) * wallPaperBitmap.getWidth());
				srcRect.right = (int) ((srcRect.right / (float) bitmapW) * wallPaperBitmap.getWidth());
				srcRect.top = (int) ((srcRect.top / (float) bitmapH) * wallPaperBitmap.getHeight());
				srcRect.bottom = (int) ((srcRect.bottom / (float) bitmapH) * wallPaperBitmap.getHeight());
				canvas.save();
				Paint paint = new Paint();
				paint.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
				canvas.drawBitmap(wallPaperBitmap, srcRect, destRect, paint);
				canvas.restore();

			} else {
				canvas.drawColor(wallPaperColor);// 没有壁纸，绘制一个色值
			}
		}
	}
	public static native void native_blur(Bitmap bitmap, int radius, int iterations, int startRow, int endRow);

	/**
	 * 该方法供插件反射调用，切勿改变路径
	 * <p>Title: nativeBlur</p>
	 * <p>Description: </p>
	 * @param bitmap
	 * @param radius
	 * @param iterations
	 * @param startRow
	 * @param endRow
	 * @author maolinnan_350804
	 */
	public static void nativeBlur(Bitmap bitmap, Integer radius, Integer iterations, Integer startRow, Integer endRow){
		try {
			native_blur(bitmap, radius.intValue(), iterations.intValue(), startRow.intValue(), endRow.intValue());
		}catch (Throwable e){
			e.printStackTrace();
		}
	}
	
	public static Bitmap getWallpaperByService(int inSampleSize) {
	    boolean mIsLiveWP=false;
		mIsLiveWP=false;
		Bitmap bitmap = null;
		if (Global.isDrawWallPaper) {

			bitmap = WallpaperHelper.getInstance().getWallPaperBitmap();
			if (bitmap != null) {
				mPaperH = bitmap.getHeight();
			}
			isDrawWallPaper=true;
			return bitmap;
			
		}
		isDrawWallPaper=false;
		IBinder binder = ServiceManager.getService("wallpaper");

		// 获取壁纸服务失败
		if (binder == null) {
			return null;
		}
		IWallpaperManager wm = IWallpaperManager.Stub.asInterface(binder);
		Bundle params = new Bundle();
		ParcelFileDescriptor fd = null;
		try {
			// 动态壁纸直接返回空
			WallpaperInfo wallpaperInfo=wm.getWallpaperInfo() ;
			if (wallpaperInfo != null) {
				if(WallpaperUtil2.is91LiveWallpaperRunning(wallpaperInfo))
				{
					String fileName=WallpaperUtil2.getLiveWallPaper();
					
					fd=ParcelFileDescriptor.open(new File(fileName),ParcelFileDescriptor.MODE_READ_ONLY);
					mIsLiveWP=true;
				}else {
					return null;
				}
				
			}else {
				IWallpaperManagerCallback.Stub stub = null;
				stub = new Callback();
				if(Build.VERSION.SDK_INT>=24) {
					fd =getWallPaper(wm,stub,params);
				}else {
					fd = wm.getWallpaper(stub, params);
				}
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		// Log.e("zhou", "binder time="+(System.currentTimeMillis()-tt));
		int width = 0;
		int height = 0;
		if (fd != null) {
			width = params.getInt("width", 0);
			height = params.getInt("height", 0);
			if (mIsLiveWP) {
				width = 1;
				height = 1;
			}
			if (width <= 0 || height <= 0) {
				try {
					bitmap=null;
					bitmap = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, null);
					fd.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (bitmap != null) {
					bitmap.setDensity(ReflectUtilEx.getDisplayMetricsStaticValue("DENSITY_DEVICE"));
				}
				return bitmap;
			}

			// Load the bitmap with full color depth, to preserve
			// quality for later processing.
			BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inJustDecodeBounds = false;
			options.inSampleSize = inSampleSize;

			// long t=System.currentTimeMillis();
			try {
				bitmap=null;
				bitmap = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, options);
				fd.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (mIsLiveWP && bitmap != null) {
			int wh[]=ScreenUtil.getScreenWH();
			mIsLiveWP=false;
			bitmap = Bitmap.createScaledBitmap(bitmap, (int) (wh[0]/1f / Sample_Size * 2), (int) (wh[1]/1f/ Sample_Size), false);
			mPaperH=bitmap.getHeight()*Sample_Size;
		}else {
			mPaperH=height;
		}
		return bitmap;

	}

	static class Callback extends IWallpaperManagerCallback.Stub {
		@Override
		public void onWallpaperChanged() throws RemoteException {
		}
		public void  onBlurWallpaperChanged(){
		}
	}

	static int a = 0;

	/**
	 * 屏幕缩小动画
	 */
	public static void narrowAndBlurEx(Launcher launcher, boolean isScreen, boolean isShowScaleAnim) {
		AnimatorSet set = new AnimatorSet();
		ArrayList<Animator> items = new ArrayList<Animator>();

		if(Global.isDrawerVisiable() && !isScreen){
			if(launcher.getDrawer() instanceof DrawerMainView){
				DrawerMainView drawerMainView = (DrawerMainView) launcher.getDrawer();
				drawerMainView.setVisibility(View.INVISIBLE);
			}
		}else {
			CellLayout cellLayout = launcher.getWorkspace().getCurrentCellLayout();
			MagicDockbarRelativeLayout bottomContainer = (MagicDockbarRelativeLayout) launcher.getBottomContainer();
			playTogether(set, items, ObjectAnimator.ofFloat(cellLayout, "alpha", 1, 0), ObjectAnimator.ofFloat(bottomContainer, "alpha", 1f, 0));
		}

		set.playTogether(items);
		LauncherAnimatorListener listener = new LauncherAnimatorListener(launcher, isScreen, 1, false);
		set.addListener(listener);
		set.setDuration(NARROW_TIME);
		set.start();
		// Log.e("zhou", "缩小动画");
		setCurrAnimatorSet(set);
	}

	/**
	 * 屏幕缩小动画
	 */
	public static void restoreAndClearEx(Launcher launcher, boolean isScreen, boolean isShowScaleAnim) {
		AnimatorSet set = new AnimatorSet();
		ArrayList<Animator> items = new ArrayList<Animator>();

		if(Global.isDrawerVisiable() && !isScreen){
			if(launcher.getDrawer() instanceof DrawerMainView){
				DrawerMainView drawerMainView = (DrawerMainView) launcher.getDrawer();
				drawerMainView.setVisibility(View.VISIBLE);
			}
		}else {
			CellLayout cellLayout = launcher.getWorkspace().getCurrentCellLayout();
			MagicDockbarRelativeLayout bottomContainer = (MagicDockbarRelativeLayout) launcher.getBottomContainer();
			playTogether(set, items, ObjectAnimator.ofFloat(cellLayout, "alpha", 0, 1), ObjectAnimator.ofFloat(bottomContainer, "alpha", 0f, 1));
		}

		set.playTogether(items);
		LauncherAnimatorListener listener = new LauncherAnimatorListener(launcher, isScreen, 1, false);
		set.addListener(listener);
		set.setDuration(NARROW_TIME);
		set.start();
		// Log.e("zhou", "缩小动画");
		setCurrAnimatorSet(set);
	}
	
	
	/** 屏幕缩小动画 */
	private static void narrowAndBlur(Launcher launcher, boolean isScreen, boolean isShowScaleAnim) {
		addBlurBGAndShade(isScreen, launcher);
		mBlurView.setShadeColor(0x99000000);

		// 动画
		AnimatorSet set = new AnimatorSet();
		ArrayList<Animator> items=new ArrayList<Animator>();
		if (isScreen) {
			CellLayout cellLayout = launcher.getWorkspace().getCurrentCellLayout();
			MagicDockbarRelativeLayout bottomContainer = (MagicDockbarRelativeLayout) launcher.getBottomContainer();
			if (isShowScaleAnim) {
				// dock 栏移动位置
				int height = cellLayout.getHeight() + bottomContainer.getHeight();
				int transferY = (int) ((height - cellLayout.getHeight()) * (1 - scale) / 2);
				int BottomtransferY = (int) ((height / 2 - bottomContainer.getTop() - bottomContainer.getHeight() / 2) * (1 - scale));
				playTogether(set,items,ObjectAnimator.ofFloat(cellLayout, "scaleX", 1, scale), 
						ObjectAnimator.ofFloat(cellLayout, "scaleY", 1, scale),
						ObjectAnimator.ofFloat(cellLayout, "y", cellLayout.getTop(), cellLayout.getTop() + transferY), 
						ObjectAnimator.ofFloat(cellLayout, "alpha", 1f, 0),
						ObjectAnimator.ofFloat(bottomContainer, "scaleX", 1, scale),
						ObjectAnimator.ofFloat(bottomContainer, "scaleY", 1, scale),
						ObjectAnimator.ofFloat(bottomContainer, "y", bottomContainer.getTop(), bottomContainer.getTop() + BottomtransferY), 
						ObjectAnimator.ofFloat(bottomContainer, "alpha", 1f, 0),
						ObjectAnimator.ofFloat(mBlurView, "alpha", 0.0f, 1));
			} else {
				playTogether(set,items,ObjectAnimator.ofFloat(cellLayout, "alpha", 1f, 0),
						ObjectAnimator.ofFloat(bottomContainer, "alpha", 1f, 0),
						ObjectAnimator.ofFloat(mBlurView, "alpha", 0.0f, 1));
			}
			mCurrenCellLayoutView = cellLayout;
		} else {
			// 匣子
			DragLayer mDragLayer = launcher.getDragLayer();

			// 程序匣子头部模块
			View drawerTopView = (LinearLayout) mDragLayer.findViewById(R.id.drawer_top_layout);
			// 程序匣子应用列表模块
			DrawerSlidingView slidingView = (DrawerSlidingView) mDragLayer.findViewById(R.id.drawer_sliding_view);
			DrawerLayout drawerCellLayout = (DrawerLayout) slidingView.getChildAt(slidingView.getCurrentScreen());
			// 程序匣子滚动条
			View drawerLightBar = mDragLayer.findViewById(R.id.drawer_lightbar);
			// 程序匣子底部
			View drawerBottomView = (RelativeLayout) mDragLayer.findViewById(R.id.drawer_bottom_layout);
			ObjectAnimator blurAnimator = ObjectAnimator.ofFloat(mBlurView, "alpha", 0, 1);
			ObjectAnimator drawerTopViewAni = ObjectAnimator.ofFloat(drawerTopView, "alpha", 0.8f, 0);
			ObjectAnimator drawerCellLayoutAni = ObjectAnimator.ofFloat(drawerCellLayout, "alpha", 0.8f, 0);
			ObjectAnimator drawerLightBarAni = ObjectAnimator.ofFloat(drawerLightBar, "alpha", 0.8f, 0);
			ObjectAnimator drawerBottomViewBAni = ObjectAnimator.ofFloat(drawerBottomView, "alpha", 0.8f, 0);

			if (isShowScaleAnim) {
				int height = drawerTopView.getHeight() + drawerCellLayout.getHeight() + drawerLightBar.getHeight() + drawerBottomView.getHeight();
				int transferY = (int) ((height - drawerTopView.getHeight()) * (1 - scale) / 2);

				// 程序匣子头部模块
				transferY = (int) ((height / 2 - drawerTopView.getTop() - drawerTopView.getHeight() / 2) * (1 - scale));
				PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1, scale);
				PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1, scale);
				PropertyValuesHolder pvhTY = PropertyValuesHolder.ofFloat("y", drawerTopView.getTop(), drawerTopView.getTop() + transferY);
				ObjectAnimator obj1 = ObjectAnimator.ofPropertyValuesHolder(drawerTopView, pvhX, pvhY, pvhTY);

				// 程序匣子应用列表模块
				transferY = (int) ((height / 2 - slidingView.getTop() - drawerCellLayout.getHeight() / 2) * (1 - scale));
				pvhX = PropertyValuesHolder.ofFloat("scaleX", 1, scale);
				pvhY = PropertyValuesHolder.ofFloat("scaleY", 1, scale);
				pvhTY = PropertyValuesHolder.ofFloat("y", drawerCellLayout.getTop(), drawerCellLayout.getTop() + transferY);
				ObjectAnimator.ofPropertyValuesHolder(drawerCellLayout, pvhX, pvhY, pvhTY);
				ObjectAnimator obj2 = ObjectAnimator.ofPropertyValuesHolder(drawerCellLayout, pvhX, pvhY, pvhTY);

				// 程序匣子滚动条
				transferY = (int) ((height / 2 - drawerLightBar.getTop() - drawerLightBar.getHeight() / 2) * (1 - scale));
				pvhX = PropertyValuesHolder.ofFloat("scaleX", 1, scale);
				pvhY = PropertyValuesHolder.ofFloat("scaleY", 1, scale);
				pvhTY = PropertyValuesHolder.ofFloat("y", drawerLightBar.getTop(), drawerLightBar.getTop() + transferY);
				ObjectAnimator.ofPropertyValuesHolder(drawerLightBar, pvhX, pvhY, pvhTY);
				ObjectAnimator obj3 = ObjectAnimator.ofPropertyValuesHolder(drawerLightBar, pvhX, pvhY, pvhTY);

				transferY = (int) ((height / 2 - drawerBottomView.getTop() - drawerBottomView.getHeight() / 2) * (1 - scale));
				pvhX = PropertyValuesHolder.ofFloat("scaleX", 1, scale);
				pvhY = PropertyValuesHolder.ofFloat("scaleY", 1, scale);
				pvhTY = PropertyValuesHolder.ofFloat("y", drawerBottomView.getTop(), drawerBottomView.getTop() + transferY);
				ObjectAnimator.ofPropertyValuesHolder(drawerBottomView, pvhX, pvhY, pvhTY);
				ObjectAnimator obj4 = ObjectAnimator.ofPropertyValuesHolder(drawerBottomView, pvhX, pvhY, pvhTY);

				playTogether(set,items,obj1, obj2, obj3, obj4, blurAnimator, drawerTopViewAni, drawerCellLayoutAni, drawerLightBarAni, drawerBottomViewBAni);
			} else {
				playTogether(set,items,blurAnimator, drawerTopViewAni, drawerCellLayoutAni, drawerLightBarAni, drawerBottomViewBAni);

			}
		}
		// set.setInterpolator(mInterpolator);
		set.playTogether(items);
		LauncherAnimatorListener listener = new LauncherAnimatorListener(launcher, isScreen, 1, false);
		set.addListener(listener);
		set.setDuration(NARROW_TIME);
		set.start();
		// Log.e("zhou", "缩小动画");
		setCurrAnimatorSet(set);
	}
	
	/**
	 * 屏幕放大动画
	 * */
	private static void restoreAndClear(Launcher launcher, boolean isScreen, boolean isShowScaleAnim) {
		AnimatorSet set = new AnimatorSet();
		ArrayList<Animator> items=new ArrayList<Animator>();
		if (isScreen) {
			CellLayout cellLayout ;
			if (mCurrenCellLayoutView != null) {
				cellLayout = (CellLayout) mCurrenCellLayoutView;
				mCurrenCellLayoutView = null;
			} else {
				cellLayout = launcher.getWorkspace().getCurrentCellLayout();
			}
			
			MagicDockbarRelativeLayout bottomContainer = (MagicDockbarRelativeLayout) launcher.getBottomContainer();
			ObjectAnimator blurAnimator = ObjectAnimator.ofFloat(mBlurView, "alpha", 1, 0);
			ObjectAnimator screenAnimator = ObjectAnimator.ofFloat(cellLayout, "alpha", 0, 1);
			ObjectAnimator bottomContainerAnim = ObjectAnimator.ofFloat(bottomContainer, "alpha", 0, 1);
			if (isShowScaleAnim) {
				// dock 栏移动位置
				int height = cellLayout.getHeight() + bottomContainer.getHeight();
				int transferY = (int) ((height - cellLayout.getHeight()) * (1 - scale) / 2);
				int BottomtransferY = (int) ((height / 2 - bottomContainer.getTop() - bottomContainer.getHeight() / 2) * (1 - scale));
				playTogether(set,items,ObjectAnimator.ofFloat(cellLayout, "scaleX", scale, 1), 
						ObjectAnimator.ofFloat(cellLayout, "scaleY", scale, 1),
						ObjectAnimator.ofFloat(cellLayout, "y", cellLayout.getTop() + transferY, cellLayout.getTop()), 
						ObjectAnimator.ofFloat(bottomContainer, "scaleX", scale, 1),
						ObjectAnimator.ofFloat(bottomContainer, "scaleY", scale, 1),
						ObjectAnimator.ofFloat(bottomContainer, "y", bottomContainer.getTop() + BottomtransferY, bottomContainer.getTop()), 
						blurAnimator, bottomContainerAnim, screenAnimator);
			} else {
				playTogether(set,items,blurAnimator, bottomContainerAnim, screenAnimator);
			}

			// int transferX=(int)(cellLayout.getWidth()*(1-scale)/2);
		} else {
			ObjectAnimator blurAnimator = ObjectAnimator.ofFloat(mBlurView, "alpha", 1, 0);
			
			DragLayer mDragLayer = launcher.getDragLayer();

			// 程序匣子头部模块
			View drawerTopView = (LinearLayout) mDragLayer.findViewById(R.id.drawer_top_layout);
			// 程序匣子应用列表模块
			DrawerSlidingView slidingView = (DrawerSlidingView) mDragLayer.findViewById(R.id.drawer_sliding_view);
			DrawerLayout drawerCellLayout = (DrawerLayout) slidingView.getChildAt(slidingView.getCurrentScreen());
			// 程序匣子滚动条
			View drawerLightBar = mDragLayer.findViewById(R.id.drawer_lightbar);
			// 程序匣子底部
			View drawerBottomView = (RelativeLayout) mDragLayer.findViewById(R.id.drawer_bottom_layout);

			int height = drawerTopView.getHeight() + drawerCellLayout.getHeight() + drawerLightBar.getHeight() + drawerBottomView.getHeight();
			int transferY = 0;

			// 程序匣子头部模块
			transferY = (int) ((height / 2 - drawerTopView.getTop() - drawerTopView.getHeight() / 2) * (1 - scale));
			PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", scale, 1);
			PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", scale, 1);
			PropertyValuesHolder pvhTY = PropertyValuesHolder.ofFloat("y", drawerTopView.getTop() + transferY, drawerTopView.getTop());
			ObjectAnimator obj1 = ObjectAnimator.ofPropertyValuesHolder(drawerTopView, pvhX, pvhY, pvhTY);

			if (isShowScaleAnim) {
				// 程序匣子应用列表模块
				transferY = (int) ((height / 2 - slidingView.getTop() - drawerCellLayout.getHeight() / 2) * (1 - scale));
				pvhX = PropertyValuesHolder.ofFloat("scaleX", scale, 1);
				pvhY = PropertyValuesHolder.ofFloat("scaleY", scale, 1);
				pvhTY = PropertyValuesHolder.ofFloat("y", drawerCellLayout.getTop() + transferY, drawerCellLayout.getTop());
				ObjectAnimator obj2 = ObjectAnimator.ofPropertyValuesHolder(drawerCellLayout, pvhX, pvhY, pvhTY);

				// 程序匣子滚动条
				transferY = (int) ((height / 2 - drawerLightBar.getTop() - drawerLightBar.getHeight() / 2) * (1 - scale));
				pvhX = PropertyValuesHolder.ofFloat("scaleX", scale, 1);
				pvhY = PropertyValuesHolder.ofFloat("scaleY", scale, 1);
				pvhTY = PropertyValuesHolder.ofFloat("y", drawerLightBar.getTop() + transferY, drawerLightBar.getTop());
				ObjectAnimator obj3 = ObjectAnimator.ofPropertyValuesHolder(drawerLightBar, pvhX, pvhY, pvhTY);

				transferY = (int) ((height / 2 - drawerBottomView.getTop() - drawerBottomView.getHeight() / 2) * (1 - scale));
				pvhX = PropertyValuesHolder.ofFloat("scaleX", scale, 1);
				pvhY = PropertyValuesHolder.ofFloat("scaleY", scale, 1);
				pvhTY = PropertyValuesHolder.ofFloat("y", drawerBottomView.getTop() + transferY, drawerBottomView.getTop());
				ObjectAnimator obj4 = ObjectAnimator.ofPropertyValuesHolder(drawerBottomView, pvhX, pvhY, pvhTY);
				
				ObjectAnimator drawerTopViewAni = ObjectAnimator.ofFloat(drawerTopView, "alpha", 0, 1);
				ObjectAnimator drawerCellLayoutAni = ObjectAnimator.ofFloat(drawerCellLayout, "alpha", 0, 1);
				ObjectAnimator drawerLightBarAni = ObjectAnimator.ofFloat(drawerLightBar, "alpha", 0, 1);
				ObjectAnimator drawerBottomViewBAni = ObjectAnimator.ofFloat(drawerBottomView, "alpha", 0, 1);
				playTogether(set,items,blurAnimator, obj1, obj2, obj3, obj4,drawerTopViewAni,drawerCellLayoutAni,drawerLightBarAni,drawerBottomViewBAni);
			} else {
				ObjectAnimator drawerTopViewAni = ObjectAnimator.ofFloat(drawerTopView, "alpha", 0, 1);
				ObjectAnimator drawerCellLayoutAni = ObjectAnimator.ofFloat(drawerCellLayout, "alpha", 0, 1);
				ObjectAnimator drawerLightBarAni = ObjectAnimator.ofFloat(drawerLightBar, "alpha", 0, 1);
				ObjectAnimator drawerBottomViewBAni = ObjectAnimator.ofFloat(drawerBottomView, "alpha", 0, 1);
				playTogether(set,items,blurAnimator, drawerTopViewAni, drawerCellLayoutAni, drawerLightBarAni, drawerBottomViewBAni);

			}

		}
		set.playTogether(items);
		LauncherAnimatorListener listener = new LauncherAnimatorListener(launcher, isScreen, 4, false);
		set.addListener(listener);
		set.setDuration(RESTORE_TIME);
		if(MIUI_CLOSE_FOLDER_NO_ANI){//修复bug
			set.setDuration(MIUI_NO_ANI_RESTORE_TIME);
			MIUI_CLOSE_FOLDER_NO_ANI = false;
		}
		// set.setInterpolator(mInterpolator);
		set.start();
		 //Log.e("zhou", "放大动画");
		setCurrAnimatorSet(set);
	}

	public static void retoreBottomContainer(View bottomContainer) {
		try {
			if(bottomContainer != null && bottomContainer.getY() != bottomContainer.getTop()){
				Log.e("xxExitFolderOnClickApp", "" + bottomContainer.getY() +":" + bottomContainer.getTop());
				AnimatorSet set = new AnimatorSet();
				ArrayList<Animator> items=new ArrayList<Animator>();
				items.add(ObjectAnimator.ofFloat(bottomContainer, "scaleX", 1, 1));
				items.add(ObjectAnimator.ofFloat(bottomContainer, "scaleY", 1, 1));
				items.add(ObjectAnimator.ofFloat(bottomContainer, "y", bottomContainer.getTop(), bottomContainer.getTop()));
				set.playTogether(items);
				set.setDuration(50);
				set.start();
			}
		}catch (Throwable e){
			e.printStackTrace();
		}
	}

	/** 显示模糊壁纸 */
	private static void disPlayBlurWallPaper(Launcher launcher, boolean isScreen ) {
		addBlurBGAndShade(isScreen, launcher);
		mBlurView.setShadeColor(0x7a00000);
		// 动画
		AnimatorSet set = new AnimatorSet();
		ArrayList<Animator> items=new ArrayList<Animator>();
		ObjectAnimator blurAnimator = ObjectAnimator.ofFloat(mBlurView, "alpha", 0.0f, 1);
		playTogether(set,items,blurAnimator);
		set.playTogether(items);
		// set.setInterpolator(mInterpolator);
		LauncherAnimatorListener listener = new LauncherAnimatorListener(launcher, isScreen, 1, false);
		set.addListener(listener);
		set.setDuration(300);
		set.start();
		// Log.e("zhou", "缩小动画");
		setCurrAnimatorSet(set);
	}
	
	/** 去除模糊壁纸 */
	private static void missBlurWallPaper(Launcher launcher, boolean isScreen) {
		AnimatorSet set = new AnimatorSet();
		ArrayList<Animator> items=new ArrayList<Animator>();
		ObjectAnimator blurAnimator = null;
		blurAnimator = ObjectAnimator.ofFloat(mBlurView, "alpha", 1, 0);
		LauncherAnimatorListener listener = new LauncherAnimatorListener(launcher, isScreen, 4, false);
		playTogether(set,items,blurAnimator);
		set.playTogether(items);
		set.addListener(listener);
		set.setDuration(290);
		// set.setInterpolator(mInterpolator);
		set.start();
		// Log.e("zhou", "放大动画");
		setCurrAnimatorSet(set);
	}
	
	/**
	 * 壁纸改变时，回调这个函数
	 * **/
	public static void onWallPaperChange() {
		synchronized (lock) {
			wallPaperBitmap = null;
			wallPaperBitmap = getWallpaperByService(Sample_Size);
			//checkWallPaperColor(true);
		}

	}

	/**
	 * 桌面启动时，调用这个函数获取壁纸 新开一个线程获取壁纸
	 * */
	public static void loadWallPaper() {

		new Thread() {
			@Override
			public void run() {
				synchronized (lock) {
					wallPaperBitmap = null;
					wallPaperBitmap = getWallpaperByService(Sample_Size);
				}
				//Log.e("zhou", "loadWallPaper: loadWallPaper");
			}
		}.start();
	}
	
	public static int getBlueTime() {
		return BLUR_TIME;
	}
	
	
/*	public static void setOnlyBlurWallpaper(boolean isOnlyBlurWallpaper) {
		mIsOnlyBlurWP = isOnlyBlurWallpaper;
	}*/
	/**
	 * 获取壁纸
	 */
//	private static void drawPartWallpaper(Canvas canvas, int screenWidth, int screenHeight, int y, Launcher lanucher) {
//		Rect srcRect = new Rect();
//		Rect destRect = new Rect();
//		//Bitmap bitmap = null;
//		// long t;
//		synchronized (lock) {
//
//			if (wallPaperBitmap == null) {
//				wallPaperBitmap=getWallpaperByService(Sample_Size);
//			}
//			//Log.e("zhou", "drawPartWallpaper: drawPartWallpaper");
//			Workspace workspace = lanucher.getWorkspace();
//			// 绘制壁纸
//
//			int paperWidth = screenWidth * 2;
//			int paperHeight = 0;
//
//			if (mPaperH > 0) {
//				paperHeight = mPaperH;
//			} else {
//				paperHeight = screenHeight;
//			}
//			float bitmapScale = 0;
//
//			if (wallPaperBitmap != null) {
//				if (isHTC_ONE_X()) {
//					paperHeight = screenHeight + 96;
//				}
//				int bitmapW = wallPaperBitmap.getWidth() * Sample_Size;
//				int bitmapH = wallPaperBitmap.getHeight() * Sample_Size;
//
//				int childCount = workspace.getChildCount();
//				int deltaw = paperWidth - bitmapW;
//				int deltah = paperHeight - bitmapH;
//
//				if (deltaw > 0 || deltah > 0) {
//					// We need to scale up so it covers the entire
//					// area.
//
//					if (deltaw > deltah) {
//						bitmapScale = paperWidth / (float) bitmapW;
//					} else {
//						bitmapScale = paperHeight / (float) bitmapH;
//					}
//					bitmapW = (int) (bitmapW * bitmapScale);
//					bitmapH = (int) (bitmapH * bitmapScale);
//					deltaw = paperWidth - bitmapW;
//					deltah = paperHeight - bitmapH;
//				}
//
//				/** 壁纸滚动单位距离 */
//				int stepX = 0;
//				// int startx = 0;
//				boolean isShowNavi = SettingsPreference.getInstance().isShowNavigationView();
//				if (childCount >= 1) {
//					if (isShowNavi) {
//						stepX = (paperWidth - screenWidth) / (childCount + 1);
//					} else {
//						stepX = (paperWidth - screenWidth) / (childCount);
//					}
//				}
//
//				// 壁纸被裁减的区域,不滚动的起始位置
//				if (!ConfigFactory.isWallpaperRolling(lanucher)) {
//					srcRect.left = screenWidth / 2;
//				} else if (isShowNavi) {
//					srcRect.left = stepX / 2 + stepX * (lanucher.mWorkspace.getCurrentScreen() + 1);
//
//				} else {
//					srcRect.left = stepX / 2 + stepX * (lanucher.mWorkspace.getCurrentScreen());
//				}
//				srcRect.left = srcRect.left - deltaw / 2;
//
//				srcRect.right = srcRect.left + screenWidth;
//				srcRect.top = -deltah / 2;
//				srcRect.bottom = srcRect.top + screenHeight;
//
//				// HTC ONE 手机的壁纸上下面会有透明条，壁纸区域不同
//
//				// 壁纸将绘制到画布上的区域
//				destRect.left = 0;
//				destRect.top = 0;
//				destRect.right = canvas.getWidth();
//				destRect.bottom = canvas.getHeight();
//
//				//截取一部份壁纸
//				srcRect.top=srcRect.top+y;
//				srcRect.left = (int) ((srcRect.left / (float) bitmapW) * wallPaperBitmap.getWidth());
//				srcRect.right = (int) ((srcRect.right / (float) bitmapW) * wallPaperBitmap.getWidth());
//				srcRect.top = (int) ((srcRect.top / (float) bitmapH) * wallPaperBitmap.getHeight());
//				srcRect.bottom = (int) ((srcRect.bottom / (float) bitmapH) * wallPaperBitmap.getHeight());
//				canvas.save();
//				Paint paint = new Paint();
//				paint.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
//				canvas.drawBitmap(wallPaperBitmap, srcRect, destRect, paint);
//				canvas.restore();
//			}
//		}
//	}
	
	public static void AddBlurWallpaper(Launcher launcher) {
		if (mBlurView2 != null || launcher == null) {
			return;
		}
		
		launcher.getWorkspace().getCurrentCellLayout().setVisibility(View.INVISIBLE);
		DragLayer mDragLayer = launcher.getDragLayer();
		Bitmap bitmap = null;
		int h=0;
		int w = ScreenUtil.getScreenWH()[0];
		if (mDragLayer != null) {
			h = mDragLayer.getHeight();
		} else {
			h = ScreenUtil.getScreenWH()[1];
		}
		
		bitmap = Bitmap.createBitmap((int) (w * thumb_scale), (int) (h * thumb_scale), Bitmap.Config.ARGB_8888);
		if (bitmap == null) {
			return;
		}
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		drawSingleWallpaper(canvas, (int) (bitmap.getWidth() / thumb_scale), (int) (bitmap.getHeight() / thumb_scale), thumb_scale, launcher);
		if (isLoadSoSucceed) {
			try {
				native_blur(bitmap, 5, 2, -1, -1);
			} catch (Error e) {
				e.printStackTrace();
				mBitmap.eraseColor(0);
			}
			
		}
		// Log.e("zhou", "blur time=" + (System.currentTimeMillis() - t));
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		mBlurView2 = new BlurView(launcher.getApplicationContext());
		mBlurView2.setBlurBitmap(bitmap);
		mBlurView2.setShadeColor(0x99000000);
		
		if(launcher.isTranslucentNavigationBar()){//导航栏透明，改写背景添加位置
			launcher.addLauncherBgView(mBlurView2, 1);
		}else{
			boolean found=false;
			for (int i = 0; i < mDragLayer.getChildCount(); i++) {
				View view = mDragLayer.getChildAt(i);
				if (view instanceof LauncherAddMainView) {
					mDragLayer.addView(mBlurView2, i, lp);
					found=true;
					break;
				}

			}
			if (!found) {
				mDragLayer.addView(mBlurView2);
			}
		}
		
		ObjectAnimator animator=ObjectAnimator.ofFloat(mBlurView2, "alpha",0,1);
		animator.setDuration(200);
		animator.start();
	}

	public static void DelBlurWallpaper(Launcher launcher) {
		if (mBlurView2 == null) {
			return;
		}
		launcher.getWorkspace().getCurrentCellLayout().setVisibility(View.VISIBLE);
		ObjectAnimator animator=ObjectAnimator.ofFloat(mBlurView2, "alpha",1,0);
		animator.setDuration(200);
		animator.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {	
			}
			@Override
			public void onAnimationEnd(Animator arg0) {
				if (mBlurView2 != null && mBlurView2.getParent() != null) {
					((ViewGroup)mBlurView2.getParent()).removeView(mBlurView2);
					mBlurView2 = null;
				}
			}
			@Override
			public void onAnimationCancel(Animator arg0) {	
			}
		});
		animator.start();
	}
	
	
	static void drawDrawMainViewBG(Canvas canvas, int screenWidth,
			int screenHeight, float scale, Launcher lanucher) {
		if( !(lanucher.getDrawer() instanceof DrawerMainView)){
			return;
		}
		// 如果主题里没有匣子图片，就用系统壁纸
		DrawerMainView drawer = (DrawerMainView)lanucher.getDrawer();
		Drawable drawable = drawer.getBackground();
//		drawable = ThemeManagerFactory.getInstance().getThemeDrawable(
//				ThemeData.DRAWER);
		
		
		if (drawable == null || drawable instanceof ColorDrawable) {
			drawSingleWallpaper(canvas, screenWidth, screenHeight, scale, lanucher);
			return;
		}
		
		Bitmap bitmap=((BitmapDrawable)(drawable)).getBitmap();
		if (bitmap == null) {
			drawSingleWallpaper(canvas, screenWidth, screenHeight, scale, lanucher);
			return;
		}
		Rect srcRect = new Rect();
		Rect destRect = new Rect();
		
		destRect.left=0;
		destRect.right=canvas.getWidth();
		destRect.top=0;
		destRect.bottom=canvas.getHeight();
		
		srcRect.left=0;
		srcRect.right=bitmap.getWidth();
		srcRect.top=0;
		srcRect.bottom=bitmap.getHeight();
		
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
		canvas.drawBitmap(bitmap, srcRect, destRect, paint);

	}

/*	public static void releaseAll() {
		mBitmap = null;
		mBlurView = null;
		mBlurView2 = null;
		mCurrAnimatorSet = null;
		mCurrenCellLayoutView = null;
		wallPaperBitmap = null;
		//Log.e("zhou", "LauncherAnimationHelp:releaseAll()");
	}*/
	/**
	 * 功能：根据对象是否为空，决定是否添加这个动画
	 * 目的：为了有些异常情况下，View可能为空，但是
	 * ObjectAnimator中又不做处理，这时会导致空指针异常
	 * */
	public static void playTogether(AnimatorSet set, Collection<Animator> list, ObjectAnimator... anis) {
		if (anis != null) {
			for (int i = 0; i < anis.length; ++i) {
				ObjectAnimator objectAnimator = anis[i];
				if (objectAnimator != null && objectAnimator.getTarget() != null) {
					list.add(objectAnimator);
				}
			}
		}

	}

	public static native int native_GetMaincolors(Bitmap srcBitmap, int validValue);

	//android N(7.0)的手机上，通过反射获取壁纸
	public static ParcelFileDescriptor getWallPaper(Object owallpaper, Object callBack, Object param) {
		ParcelFileDescriptor f = null;
		try {
			Class c = Class.forName("android.app.IWallpaperManager");
			Method m = c.getDeclaredMethod("getWallpaper", new Class[]{IWallpaperManagerCallback.class, int.class, Bundle.class, int.class});
			m.setAccessible(true);
			Object file = m.invoke(owallpaper, new Object[]{callBack, 1, param, 0});
			f = (ParcelFileDescriptor) file;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return f;
	}

/*
	public static int checkWallPaperColor(boolean needBrocastResult) {
		Launcher launcher = Global.getLauncher();
		if (launcher == null) {
			return -1;
		}
		if (!ThemeSharePref.getInstance(launcher).isDefaultTheme()) {
			return -1;
		}
		int c = getMainColor(wallPaperBitmap, 0);
		if (c == 0) {
			//Log.e("zhou", "壁纸黑色");
		} else if (c == 1) {
			//Log.e("zhou", "壁纸白色");
		} else {
			//Log.e("zhou", "壁纸彩色");
		}
		Intent intent = new Intent(WallPaperColorChange);
		intent.putExtra("result", c);
		launcher.sendBroadcast(intent);
		return c;
	}
*/

	public static int getMainColor(Bitmap bitmap, int validValue) {

		if (bitmap == null) {
			return 0;
		}
		int color = native_GetMaincolors(bitmap, validValue);
		int R = Color.red(color);
		int G = Color.green(color);
		int B = Color.blue(color);
		//Log.e("zhou", "R=" + R + "G=" + G + "B=" + B);
		float hsv[] = new float[3];
		Color.RGBToHSV(R, G, B, hsv);

		float h = hsv[0];
		float s = hsv[1];
		float v = hsv[2];
		int result = 0;
		if (s < 0.3) {
			// 饱和度很底非黑即白
			if (v < 0.5) {
				// 黑色
				result = 0;

			} else {
				// 白色
				result = 1;
			}

		} else {
			// 有颜色
			if (v < 0.1) {
				result = 0;
			} else {
				result = 2;
			}
		}
		//String HSV = "色相:" + h + "(" + GetH(h) + ") " + "饱和度:" + s + " 亮度:" + v;
		//Log.e("zhou", "HSV=" + HSV);
		return result;
	}

/*	private static String GetH(float H) {
		if ((H < 20.0F) || (H >= 330.0F)) {
			return "红色";
		}
		if (H < 42.0F) {
			return "橙色";
		}
		if (H < 65.0F) {
			return "黄色";
		}
		if (H < 145.0F) {
			return "绿色";
		}
		if (H < 180.0F) {
			return "青色";
		}
		if (H < 245.0F) {
			return "蓝色";
		}
		return "紫色";
	}*/
	static {
		try {
			System.loadLibrary("image");
			isLoadSoSucceed=true;
			//System.loadLibrary("color");
		} catch (Throwable e) {
			isLoadSoSucceed=false;
			e.printStackTrace();
		}
	}
	
	/**
	 * 是否是大于4.4.4的SDK
	 * */
	private static boolean isNewAPI() {
		if (("4.4.4".equalsIgnoreCase(Build.VERSION.RELEASE) || Build.VERSION.SDK_INT > 19) && !isDrawWallPaper) {
			return true;
		}

		return false;
	}
	/**
	 * 4.4.4以上的手机的壁纸绘制策略和谷歌官方保持了一致
	 * 基本原理：　1.先计算壁纸我缩放比例，铺满高或宽，
	 *         2.缩放后计算左右两边的位置移值，让壁纸居中
	 *         3.计算可滚动的范围，注：可滚动范围是根据壁纸原始大上和屏幕分辩计算的，如果原始大小小于屏幕宽则不允许滚动
	 *         4.根据滚动值，最终确实壁纸的当前位置
	 * */
	private static void  drawWallpaperNewＳtrategy(Canvas canvas,int screenW,int screenH,Launcher lanucher) {
		final int dw = screenW;
        final int dh = screenH;
        int BitmapW=wallPaperBitmap.getWidth()*Sample_Size;
        int BitmapH=wallPaperBitmap.getHeight()*Sample_Size;
        
		float mScale = Math.max(1f, Math.max(dw / (float) BitmapW,
                dh / (float) BitmapH));
        final int availw = dw - (int) (BitmapW* mScale);
        final int availh = dh - (int) (BitmapH * mScale);
        int xPixels = availw / 2;
        int yPixels = availh / 2;

        final int availwUnscaled = dw - BitmapW;
        final int availhUnscaled = dh - BitmapH;
        
        float mXOffset=0;
        float mYOffset=0;
        
        Workspace workspace = lanucher.getWorkspace();
		int childCount = workspace.getChildCount();
        boolean isShowNavi = SettingsPreference.getInstance().isShowNavigationView();
		isShowNavi &= SettingsPreference.getInstance().isWallpaperScrollingOnZeroView();
		
		
		float stepX=0;
		if (childCount >= 1) {
			if (isShowNavi) {
				stepX = 1f / (childCount + 1);
			} else {
				stepX = 1f / (childCount);
			}
		}
		
		// 壁纸被裁减的区域,不滚动的起始位置
		if (!ConfigFactory.isWallpaperRolling(lanucher)) {
			mXOffset = 0.5f;
		} else if (isShowNavi) {
			mXOffset = stepX / 2 + stepX * (lanucher.mWorkspace.getCurrentScreen() + 1);

		} else {
			mXOffset = stepX / 2 + stepX * (lanucher.mWorkspace.getCurrentScreen());
		}
        
        
        if (availwUnscaled < 0) xPixels += (int)(availwUnscaled * (mXOffset - .5f) + .5f);
        if (availhUnscaled < 0) yPixels += (int)(availhUnscaled * (mYOffset - .5f) + .5f);

		final float right = xPixels + BitmapW * mScale;
		final float bottom = yPixels + BitmapH * mScale;

		RectF dest = new RectF(xPixels, yPixels, right, bottom);
		dest.left=dest.left*thumb_scale;
		dest.top=dest.top*thumb_scale;
		dest.right=dest.right*thumb_scale;
		dest.bottom=dest.bottom*thumb_scale;
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
		canvas.drawBitmap(wallPaperBitmap, null, dest, paint);
    }
	public static Bitmap getBlurBitmap(boolean isScreen, Launcher launcher,boolean haveViewBg,int radius,int iterations){
		Bitmap bm=null;

		if(haveViewBg) {
			try {
				bm = getDrawCache(launcher, 1, isScreen);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (bm == null){
			bm = Bitmap.createBitmap((int) (ScreenUtil.getScreenWH()[0] * thumb_scale), (int) (ScreenUtil.getScreenWH()[1] * thumb_scale), Bitmap.Config.ARGB_8888);
		}
		Canvas canvas = new Canvas();
		canvas.setBitmap(bm);

		drawSingleWallpaper(canvas, (int) (bm.getWidth() / thumb_scale), (int) (bm.getHeight() / thumb_scale), thumb_scale, launcher);
		try {
			native_blur(bm, radius, iterations, -1, -1);
		}catch (Throwable e){
			e.printStackTrace();
		}
		if (haveViewBg) {
			canvas.drawColor(Color.parseColor("#4c000000"));
		} else {
			canvas.drawColor(Color.parseColor("#77555555"));
		}
		return bm;
	}
	//测试添加
}
