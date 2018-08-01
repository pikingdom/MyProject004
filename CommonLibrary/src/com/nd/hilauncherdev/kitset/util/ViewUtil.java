package com.nd.hilauncherdev.kitset.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.Window;

/**
 * View位置工具类
 */
public class ViewUtil {
	private static Rect outRect = new Rect();
	private static int dropCoordinates [] = {-1, -1};
	
// --Commented out by Inspection START (2015/12/10 14:07):
//	/**
//	 * 该桌面是否在View上
//	 * @param x
//	 * @param y
//	 * @param view
//	 * @return boolean
//	 */
//	public static boolean isLocationOnView(int x, int y, View view) {
//		if (view == null)
//			return false;
//		if (x < 0 || y < 0)
//			return false;
//
//		view.getHitRect(outRect);
//		view.getLocationOnScreen(dropCoordinates);
//		outRect.offset(dropCoordinates[0] - view.getLeft(), dropCoordinates[1] - view.getTop());
//		return outRect.contains(x, y);
//	}
// --Commented out by Inspection STOP (2015/12/10 14:07)

	/**
	 * 获取View位置Rect
	 * @param view
	 * @return Rect
	 */
	public static Rect getLocationOnScreen(View view) {
		Rect rsult = new Rect();
		view.getHitRect(rsult);
		view.getLocationOnScreen(dropCoordinates);
		rsult.offset(dropCoordinates[0] - view.getLeft(), dropCoordinates[1] - view.getTop());
		return rsult;
	}
	
	/**
	 * 描述:设置该Activity为全屏
	 * 
	 * @author linqiang(866116)
	 * @Since 2012-5-24
	 * @param $this
	 */
	public static void fullscreen(Activity $this) {
		$this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// $this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
