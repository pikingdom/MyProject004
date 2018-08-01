/**
* @brief 【插件控制】
*
* @<b>文件名</b>    : WidgetGlobal
* @n@n<b>作  者</b>  : 陈希
* @n@n<b>创建时间</b>: 2012-7-16 上午12:24:56 
*/
package com.nd.weather.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.nd.calendar.common.ComDataDef.ConfigsetData;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;
import com.nd.weather.widget.PandaHome.PandaWidgetView;

import java.io.File;

public class WidgetGlobal {
    // 分辨率
    private static String msResolution = null;
    public static final String WIDGET_HOTAREA_RECEIVER = StartupReceiver.class.getName();
    
    public static final String WIDGET_SETTING = ConfigsetData.CONFIG_NAME;
    public static final String WIDGET_SKIN_SETTING = "calendarWidgetSkin";

//    public static final String WIDGET_SETTING_SKIN_PATH = "widgeXmlPath";
//    public static final String WIDGET_SETTING_USER_SKIN = "default";
    public static final String WIDGET_SETTING_PANDA_SKIN = "widget_panda_skin";
    public static final String WIDGET_SETTING_PANDA_USER_SKIN = "widget_panda_user_skin";
    public static final String WIDGET_SETTING_PANDA_SKIN_WITH_THEME = "widget_panda_skin_with_theme";
    public static final String WIDGET_SETTING_PANDA_FONT = "widget_panda_font";
    public static final String WIDGET_SETTING_DEFAULT_THEME_4X1_BG_NAME = "widget_default_theme_4x1_bg_name";
	public static final String WIDGET_SETTING_DEFAULT_THEME_4X1_BG_COLOR = "widget_default_theme_4x1_bg_color";
	public static final String WIDGET_DEFAULT_NO_CITY_SKIN_PATH = "widget_default_no_city_skin_path";
	public static final String WIDGET_DEFAULT_WITH_CITY_SKIN_PATH = "widget_default_with_city_skin_path";
	public static final String WIDGET_WEEK_SHOW_STYLE = "widget_week_show_style";

    public static final String WIDGET_DEFAULT_CITY_SKIN = "weather_skin2/";

	public final static int WIDGET_4X1 = 0;
	public final static int WIDGET_4X2 = 1;

	/**
	 * @brief 【更新插件】
	 * @n<b>函数名称</b>     :updateWidgets
	 * @param context
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2012-7-16上午10:18:24
	 * @<b>修改时间</b>      :
	*/
	public final static boolean updateWidgets(Context context) {
		final boolean bPanda = PandaWidgetView.updateWidgets(context, WidgetUtils.ACTION_INVALIDATE);
    	return bPanda;
	}
	
	public final static boolean updateWidgets(Context context, int action) {
		final boolean bPanda = PandaWidgetView.updateWidgets(context, action);
		return  bPanda;
	}
	
	/**
	 * <br>Description: 获取天气插件默认皮肤路径
	 * <br>Author:caizp
	 * <br>Date:2014年10月23日下午6:03:15
	 * @return
	 */
	public static String getDefaultSkinPath(Context context) {
//		return context.getResources().getString(R.string.default_skin_path);
		return getDefaultWithCitySkinPath(context);
//		SharedPreferences sp = context.getSharedPreferences("configsp", Context.MODE_PRIVATE|4);
//		String path = sp.getString("key_default_weather_skin_assets_path", "");
//		Log.e("launcher", "get default weather path="+path+",+++++++++++++++++");
//		if(!TextUtils.isEmpty(path)) {
//			return path;
//		}
//		return WIDGET_PANDA_DEFAULT_SKIN;
	}
	
	///////////////////////////////////////////////////////////////////////
    public static String GetResolution(Context context) {
    	if (msResolution == null) {
    		// 设置分辨率
    		DisplayMetrics dm = new DisplayMetrics();
    		dm = context.getResources().getDisplayMetrics();

    		int width = Math.min(dm.widthPixels, dm.heightPixels);
    		int height = Math.max(dm.widthPixels, dm.heightPixels);
    		// 获得手机的宽度和高度像素单位为px
    		msResolution = width + "*" + height;
		}
    	
        return msResolution;
    }
    
	/**
	 * @brief 【启动标准版服务】
	 * @n<b>函数名称</b>     :startCalendarService
	 * @param context
	 * @return    void   
	 * @<b>作者</b>          :  陈希
	 * @<b>修改</b>          :
	 * @<b>创建时间</b>      :  2013-3-11下午02:21:04
	 * @<b>修改时间</b>      :
	*/
    public static void startCalendarService(Context context) {
		Intent intent = new Intent();
		intent.setClassName("com.calendar.UI", "com.calendar.Widget.TimeService");
		
		try {
			context.startService(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

   private static String sAskAction = null;
   public static void askPandaTheme(Context context) {
    	if (sAskAction == null) {
    		sAskAction = context.getString(R.string.thenme_ask_action);
		}
    	
    	Intent intent = new Intent(sAskAction);
    	intent.putExtra("packageName", context.getPackageName());//增加包名参数
    	intent.addFlags(32);
    	context.sendBroadcast(intent);	
    }
    
    /**
     * @brief 【设置插件皮肤】
     * @n<b>函数名称</b>     :setWidgetSkin
     * @param context
     * @param sSkinPath
     * @param bUserSkin
     * @return    void   
     * @<b>作者</b>          :  陈希
     * @<b>修改</b>          :
     * @<b>创建时间</b>      :  2012-7-16下午05:28:07
     * @<b>修改时间</b>      :
    */
    public static void setWidgetSkin(Context context, String sSkinPath, boolean bUserSkin) {
        SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
        Editor editor = set.edit();
        
        if (!TextUtils.isEmpty(sSkinPath)&& sSkinPath.lastIndexOf(File.separator) != sSkinPath.length() - 1) {
        	sSkinPath += File.separator;
		}
        
        editor.putBoolean(WIDGET_SETTING_PANDA_USER_SKIN, bUserSkin);
        editor.putString(WIDGET_SETTING_PANDA_SKIN, sSkinPath);
        editor.commit();
        WidgetGlobal.updateWidgets(context, WidgetUtils.ACTION_SKIN_CHANGED);
    }
    
    /**
     * <br>Description: 设置插件字体
     * <br>Author:caizp
     * <br>Date:2014年10月14日下午4:14:06
     * @param context
     * @param sFontPath
     * @return
     */
    public static boolean setWidgetFont(Context context, String sFontPath) {
//    	if(TextUtils.isEmpty(sFontPath))return false;
        SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
        Editor editor = set.edit();
        editor.putString(WIDGET_SETTING_PANDA_FONT, sFontPath);
        editor.commit();
        return true;
    }
    
    /**
     * 设置默认主题4x1天气插件背景图片名称
     * @param context
     * @param bgName
     */
    public static void setDefaultThemeWidget4x1bgName(Context context, String bgName) {
        SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
        Editor editor = set.edit();
        editor.putString(WIDGET_SETTING_DEFAULT_THEME_4X1_BG_NAME, bgName);
        editor.commit();
    }
    
    /**
     * 获取默认主题4x1天气插件背景图片名称
     * @param context
     */
    public static String getDefaultThemeWidget4x1bgName(Context context) {
        SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
        return set.getString(WIDGET_SETTING_DEFAULT_THEME_4X1_BG_NAME, "widget_4x1_bk.png");
    }

	/**
	 * 设置默认主题4x1天气插件背景颜色
	 * @param context
	 * @param bgColor
	 */
	public static void setDefaultThemeWidget4x1bgColor(Context context, String bgColor) {
		SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
		Editor editor = set.edit();
		editor.putString(WIDGET_SETTING_DEFAULT_THEME_4X1_BG_COLOR, bgColor);
		editor.commit();
	}

	/**
	 * 获取默认主题4x1天气插件背景颜色（优先级最高）
	 * @param context
	 */
	public static String getDefaultThemeWidget4x1bgColor(Context context) {
		SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
		return set.getString(WIDGET_SETTING_DEFAULT_THEME_4X1_BG_COLOR, "");
	}

	/**
	 * 设置默认主题天气插件无城市皮肤路径
	 * @param context
	 * @param bgColor
	 */
	public static void setDefaultNoCitySkinPath(Context context, String bgColor) {
		SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
		Editor editor = set.edit();
		editor.putString(WIDGET_DEFAULT_NO_CITY_SKIN_PATH, bgColor);
		editor.commit();
	}

	/**
	 * 获取默认主题天气插件无城市皮肤路径
	 * @param context
	 */
	public static String getDefaultNoCitySkinPath(Context context) {
		SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
		return set.getString(WIDGET_DEFAULT_NO_CITY_SKIN_PATH, WIDGET_DEFAULT_CITY_SKIN);
	}

	/**
	 * 设置默认主题天气插件有城市皮肤路径
	 * @param context
	 * @param path
	 */
	public static void setDefaultWithCitySkinPath(Context context, String path) {
		SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
		Editor editor = set.edit();
		editor.putString(WIDGET_DEFAULT_WITH_CITY_SKIN_PATH, path);
		editor.commit();
	}

	/**
	 * 获取设置默认主题天气插件有城市皮肤路径
	 * @param context
	 */
	public static String getDefaultWithCitySkinPath(Context context) {
		SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
		return set.getString(WIDGET_DEFAULT_WITH_CITY_SKIN_PATH, context.getResources().getString(R.string.default_skin_path));
	}

	/**
	 * 获取星期显示类型
	 * @param context
	 */
	public static int getWeekShowStyle(Context context) {
		SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
		return set.getInt(WIDGET_WEEK_SHOW_STYLE, 0);
	}

	/**
	 * 设置星期显示类型
	 * @param context
	 * @param style  0为周X（默认），1为星期X
	 */
	public static void setWeekShowStyle(Context context, int style) {
		SharedPreferences set = WidgetUtils.getSetting(context, WidgetGlobal.WIDGET_SKIN_SETTING);
		Editor editor = set.edit();
		editor.putInt(WIDGET_WEEK_SHOW_STYLE, style);
		editor.commit();
	}

	/**
	 * 创建指定大小的色值bitmap
	 * @param color
	 * @param width
	 * @param height
     * @return
     */
	public static Bitmap getColorBitmap(int color, int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(color);
		return bitmap;
	}

	/**
	 * 获取圆角图片
	 *
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Context context, Bitmap bitmap, int cornerColor) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = ScreenUtil.dip2px(context, 3);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(cornerColor);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

}
