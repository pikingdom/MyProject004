package com.nd.hilauncherdev.kitset.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import com.nd.hilauncherdev.framework.httplib.HttpCommon;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * 图片处理相关内容
 */
public class BitmapUtils extends BaseBitmapUtils{
	/**
	 * 保存在线图片
	 * @param urlString
	 * @param filePath
	 * @return 成功返回保存的路径，失败返回null
	 */
	public static String saveInternateImage(String urlString, String filePath) {

		InputStream is = null;
		Bitmap bmp = null;
		try {
			// HttpResponse response =
			// WebUtil.getSimpleHttpGetResponse(urlString, null);
			// if (response == null)
			// return null;
			HttpCommon httpCommon = new HttpCommon(urlString);
			HttpEntity entity = httpCommon.getResponseAsEntityGet(null);
			if (entity == null)
				return null;
			Header resHeader = entity.getContentType();
			String contentType = resHeader.getValue();
			CompressFormat format = null;
			if (contentType != null && contentType.equals("image/png")) {
				format = Bitmap.CompressFormat.PNG;
			} else {
				format = Bitmap.CompressFormat.JPEG;
			}
			is = entity.getContent();
			bmp = BitmapFactory.decodeStream(is);
			if (saveBitmap2file(bmp, filePath, format))
				return filePath;
			else
				// 失败，删除文件
				FileUtil.delFile(filePath);
		} catch (Exception e) {
			Log.d(TAG, "function saveInternateImage expose exception:" + e.toString());
			// 失败，删除文件
			FileUtil.delFile(filePath);
		} finally {

			try {
				if (is != null)
					is.close();
				if (bmp != null)
					bmp.recycle();
			} catch (IOException e) {
			}
		}

		return null;
	}
	
	/**
	 * 根据颜色值，产生一个颜色矩阵， 这个矩阵让产生的颜色效果
	 * 目标Rd=(Rs*Rm)/255;Gd=(Rs*Gm)/255;Bd=(Rs*Bm)/255;Ad=As;
	 * */
	private static float[] createColorFilterArray(int color) {
		float r = 1;
		float g = 1;
		float b = 1;
		float a = 1;
		float[] colorArray = new float[20];
		for (int i = 0; i < colorArray.length; i++) {
			colorArray[i] = 0;
		}
		r = Color.red(color) / 255f;
		g = Color.green(color) / 255f;
		b = Color.blue(color) / 255f;
		a = Color.alpha(color) / 255f;

		colorArray[0] = r;
		colorArray[5] = g;
		colorArray[10] = b;
		colorArray[18] = a;

		return colorArray;
	}

	public static Bitmap CreateColorImage(Bitmap srcBitmap, int color) {
		Paint paint = new Paint();
		float colorArrar[] = createColorFilterArray(color);
		paint.setColorFilter(new ColorMatrixColorFilter(colorArrar));
		Bitmap destBitmap = Bitmap.createBitmap(srcBitmap.getWidth(),
				srcBitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(destBitmap);
		canvas.drawBitmap(srcBitmap, 0, 0, paint);
		return destBitmap;
	}

	/**
	 * 生成高斯模糊图片
	 * @param context
	 * @param bmp
	 * @return
	 */
	public static Bitmap createNativeBlur(Context context, Bitmap bmp, int radius, int iterations, int startRow, int endRow) {
		try {
			Class launcherAnimationHelpClass = context.getClassLoader().loadClass("com.nd.hilauncherdev.launcher.LauncherAnimationHelp");
			Method nativeBlurMethod = launcherAnimationHelpClass.getMethod("nativeBlur", Bitmap.class,
					Integer.class, Integer.class, Integer.class, Integer.class);
			nativeBlurMethod.invoke(null, bmp, radius, iterations, startRow, endRow);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return bmp;
	}

}
