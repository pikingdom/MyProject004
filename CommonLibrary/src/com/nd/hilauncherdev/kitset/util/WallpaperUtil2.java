package com.nd.hilauncherdev.kitset.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.preference.BaseSettingsPreference;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class WallpaperUtil2 {
	private static final String m91LiveWpSN="com.baidu.hilivewallpaper.core.LiveWallPaperService";

	protected static int mPaperH=0;
	protected static int sScreenWidth = 0;
	protected static int sScreenHeight = 0;

	public static void setWallPaper(Context context, Bitmap bitmap, InputStream stream, int type, boolean notify) {
		setDesiredDimensions(context);

		boolean bSupport = false;
		// 是否支持动态壁纸
		bSupport = isLiveWallpaperRunning(context, liveWP_ServiceName, liveWP_PackageName);
		// 不支持的话，走原逻辑
		if (!bSupport) {
			try {
				if (type == 0) {
					WallpaperManager.getInstance(context).setBitmap(bitmap);
				} else {
					WallpaperManager.getInstance(context).setStream(stream);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		String file = getLiveWallPaper();
		if (type == 0) {
			BaseBitmapUtils.saveBitmap2file(bitmap, file);
		} else {
			BaseBitmapUtils.saveStream2file(stream, file);
		}
		if(!notify)return;
		try {
			Intent serviceIntent = new Intent();
			serviceIntent.setComponent(new ComponentName("cn.com.nd.s.single.lock.livewallpaper", m91LiveWpSN));
			serviceIntent.putExtra("cmdType", "launcherSetWallpaper");
			serviceIntent.putExtra("launcherWallpaper", file);
			context.startService(serviceIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setResource(Context context, int resID, boolean notify) {
		boolean bSupport = false;
		// 是否支持动态壁纸
		bSupport = isLiveWallpaperRunning(context, liveWP_ServiceName, liveWP_PackageName);
		// 不支持的话，走原逻辑
		if (!bSupport) {
			try {
				WallpaperManager.getInstance(context).setResource(resID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		String file = getLiveWallPaper();
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);
		BaseBitmapUtils.saveBitmap2file(bitmap, file);
		if(!notify)return;
		try {
			Intent serviceIntent = new Intent();
			serviceIntent.setComponent(new ComponentName("cn.com.nd.s.single.lock.livewallpaper", m91LiveWpSN));
			serviceIntent.putExtra("cmdType", "launcherSetWallpaper");
			serviceIntent.putExtra("launcherWallpaper", file);
			context.startService(serviceIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean isLiveWallpaperRunning(Context context, String tagetServiceName, String packageName) {

		boolean support = BaseSettingsPreference.getInstance().isSupportLiveWP();
		if (!support)
			return false;

		WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
		WallpaperInfo wallpaperInfo = wallpaperManager.getWallpaperInfo();
		if (wallpaperInfo != null && packageName != null) {
			String wallPaperPkg = wallpaperInfo.getComponent().getPackageName();
			String currentLiveWallpaperServiceName = wallpaperInfo.getServiceName();
			if (currentLiveWallpaperServiceName.equals(tagetServiceName) && packageName.equals("" + wallPaperPkg)) {
				return true;
			}
		}
		return false;
	}

	public static String liveWP_PackageName = "cn.com.nd.s.single.lock.livewallpaper";
	public static String liveWP_ServiceName = "org.cocos2dx.lib.Cocos2dxGLWallpaperService";

	public static boolean is91LiveWallpaperRunning(WallpaperInfo wallpaperInfo) {
		if (wallpaperInfo == null)
			return false;
		String wallPaperPkg = wallpaperInfo.getComponent().getPackageName();
		String currentLiveWallpaperServiceName = wallpaperInfo.getServiceName();
		if (currentLiveWallpaperServiceName.equals(liveWP_ServiceName) && liveWP_PackageName.equals("" + wallPaperPkg)) {
			return true;
		}
		return false;
	}

	public static String getLiveWallPaper() {
		String file = BaseConfig.getBaseDir() + "/curWallpaper.b";
		return file;
	}

// --Commented out by Inspection START (2015/12/10 14:07):
//	public static String getLiveWPPath() {
//		String file = BaseConfig.getBaseDir();
//		return file;
//	}
// --Commented out by Inspection STOP (2015/12/10 14:07)

// --Commented out by Inspection START (2015/12/10 14:07):
//	public static String getLiveWPName() {
//		String file = "curWallpaper.b";
//		return file;
//	}
// --Commented out by Inspection STOP (2015/12/10 14:07)
	/**
	 * 直接读取文件流应用壁纸
	 * @param ctx
	 * @param path
	 */
	public static void applyWallpaperDirectly(final Context ctx, final String path){
		if (null == path || path.equals(""))
			return;
		if(WallpaperUtil.isNeedSetSingleScreen(ctx)) {
			WallpaperManager.getInstance(ctx).setWallpaperOffsetSteps(0,0);
			WallpaperManager.getInstance(ctx).suggestDesiredDimensions(ScreenUtil.getScreenWH()[0], ScreenUtil.getScreenWH()[1]);
		}else {
			WallpaperManager.getInstance(ctx).suggestDesiredDimensions(ScreenUtil.getScreenWH()[0] * 2, ScreenUtil.getScreenWH()[1]);
		}
		try {
			setWallPaper(ctx, null, new FileInputStream(path), 1, true);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setDesiredDimensions(Context ctx) {
		WallpaperManager wpm = WallpaperManager.getInstance(ctx);
		int wh[]=ScreenUtil.getScreenWH();
		if (WallpaperUtil.isNeedSetSingleScreen(ctx)) {
			wpm.suggestDesiredDimensions(wh[0], wh[1]);
		} else {
			wpm.suggestDesiredDimensions(wh[0]*2, wh[1]);
		}
	}

	/***
	 *
	 * @param context
	 * @param reLoad 是否重新加载
	 * @return
	 */
	public static int getScreenWidth(Context context,boolean reLoad){
		if(sScreenWidth == 0 || reLoad){
			int[] screenwh = getDisplayScreenResolution(context);
			sScreenWidth = screenwh[0];
			sScreenHeight = screenwh[1];
		}
		return sScreenWidth;
	}

	/**
	 *
	 * @param context
	 * @param reLoad 是否重新加载
	 * @return
	 */
	public static int getScreenHeight(Context context,boolean reLoad){
		if(sScreenHeight == 0 || reLoad){
			int[] screenwh = getDisplayScreenResolution(context);
			sScreenWidth = screenwh[0];
			sScreenHeight = screenwh[1];
		}
		return sScreenHeight;
	}

	/**
	 * 获取屏幕真实分辨率
	 *
	 * @param context
	 * @return
	 */
	public static int[] getDisplayScreenResolution(Context context) {
		int result[] = new int[2];
		int ver = Build.VERSION.SDK_INT;

		final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		android.view.Display display = windowManager.getDefaultDisplay();
		display.getMetrics(dm);
		result[0] = dm.widthPixels;
		result[1] = dm.heightPixels;
		if (ver > 16) {
			Point point = new Point();
			try {
				Method method = display.getClass().getDeclaredMethod("getRealSize", Point.class);
				if (method != null) {
					method.invoke(display, point);
					result[0] = point.x;
					result[1] = point.y;

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static Bitmap getCurrentScreenImage(Context context,Bitmap bitmap,int screenSum,int curScreen){
		int screenWidth = getScreenWidth(context, false);
		int screenHeight = getScreenHeight(context, false);
		float ratio = (float) (screenWidth * 1.0 / screenHeight);
		if(bitmap == null || bitmap.getWidth() < bitmap.getHeight()*ratio)
			return bitmap;

		int curentScreen = curScreen;
		float offset = 0;
		if(screenSum > 1){
			offset = (bitmap.getWidth() - bitmap.getHeight()*ratio)/screenSum;
		}else {
			offset = (bitmap.getWidth() - bitmap.getHeight()*ratio)/2;
			curentScreen = 1;
		}
		// 截取相应屏幕的Bitmap
		Bitmap pbm = Bitmap.createBitmap(bitmap, (int) (curentScreen * offset), 0,
				(int)(bitmap.getHeight()*ratio),
				bitmap.getHeight());
		return pbm;
	}

	/**
	 * 调用Launcher 工程中com.nd.hilauncherdev.launcher.LauncherAnimationHelp类下的getBlurWallpaper方法
	 * @param bitmap
	 * @return
	 */
	public static Bitmap invokeGetBlurWallpaperInLauncherAnimationHelp(Bitmap bitmap) {
		Class<?> threadClazz;
		try {
			threadClazz = Class.forName("com.nd.hilauncherdev.launcher.LauncherAnimationHelp");
			Method method = threadClazz.getMethod("getBlurWallpaper", Bitmap.class);
			return (Bitmap) method.invoke(null, bitmap);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 调用Launcher 工程中com.nd.hilauncherdev.launcher.LauncherAnimationHelp类下的getWallpaperByService方法
	 * @param bitmap
	 * @return
	 */
	public static Bitmap invokeGetWallpaperInLauncherAnimationHelp(int inSampleSize) {
		Class<?> threadClazz;
		try {
			threadClazz = Class.forName("com.nd.hilauncherdev.launcher.LauncherAnimationHelp");
			Method method = threadClazz.getMethod("getWallpaperByService", int.class);
			return (Bitmap) method.invoke(null, inSampleSize);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
