package com.nd.hilauncherdev.launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nd.hilauncherdev.pandahome2.LauncherShortcutHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;

import com.android.internal.util.XmlUtils;
import com.android.newline.launcher.R;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.kitset.util.AndroidPackageUtils;
import com.nd.hilauncherdev.kitset.util.BitmapUtils;
import com.nd.hilauncherdev.kitset.util.ChannelUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.launcher.LauncherProvider.DatabaseHelper;
import com.nd.hilauncherdev.launcher.LauncherSettings.Favorites;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.config.CellLayoutConfig;
import com.nd.hilauncherdev.launcher.config.IconAndTextSizeConfig;
import com.nd.hilauncherdev.launcher.config.LauncherConfig;
import com.nd.hilauncherdev.launcher.screens.dockbar.DockbarCellLayoutConfig;
import com.nd.hilauncherdev.plugin.ThirdApplicationAssit;
import com.nd.hilauncherdev.recommend.LauncherRecommendApps;
import com.nd.hilauncherdev.settings.SettingsPreference;
import com.nd.hilauncherdev.theme.ThemeManagerFactory;
import com.nd.hilauncherdev.theme.adaption.ThemeIconIntentAdaptation;
import com.nd.hilauncherdev.theme.assit.ThemeImportAssit;
import com.nd.hilauncherdev.theme.data.SimpleTheme;
import com.nd.hilauncherdev.theme.data.ThemeData;
import com.nd.hilauncherdev.theme.db.LauncherThemeDataBase;
import com.nd.hilauncherdev.theme.pref.ThemeSharePref;

public class LauncherProviderUpgrader implements LauncherProvider.ProviderUpgrader{

	private static final String TAG = "LauncherProviderUpgrader";
	private DatabaseHelper mDatabaseHelper;
	private Context mContext;
	
	//以下4个字段用于18 19数据库版本的旧数据升级
	public static final String X_1819 = "x";
	public static final String Y_1819 = "y";
	public static final String WIDTH_1819 = "width";
	public static final String HEIGHT_1819 = "height";
	public static String CREATE_TABLE_FAVORITES_MODEL_1819="CREATE TABLE IF NOT EXISTS %1$s (" 
			 + "_id INTEGER PRIMARY KEY," // 主键 
			 + "title TEXT,"  // 标题
			 + "intent TEXT," // 打开方式
			 + "container INTEGER," // 属于桌面还是文件夹
			 + "screen INTEGER," //屏幕
			 + "x INTEGER," + "y INTEGER," + "width INTEGER," + "height INTEGER," //位置和大小
			 + "itemType INTEGER," // 元素类型
			 + "appWidgetId INTEGER NOT NULL DEFAULT -1," // widgetID 
			 + "isShortcut INTEGER," // 未知
			 + "iconType INTEGER," // 快捷方式图片类型
			 + "iconPackage TEXT," // 快捷方式图片资源包名
			 + "iconResource TEXT," // 快捷方式图片资源ID
			 + "icon BLOB," // 快捷方式图片与换图标图片
			 + "uri TEXT,"  // 实时文件夹
			 + "displayMode INTEGER," // 实时文件夹
			 + "defaultIcon BLOB" // 换图标后记录默认图标使用
			 + ");";
	
	public LauncherProviderUpgrader(Context ctx, DatabaseHelper databaseHelper) {
		this.mContext = ctx;
		this.mDatabaseHelper = databaseHelper;
	}
	
	public void onUpgrade(SQLiteDatabase db, int version){
		Log.e("uuuuuuuuuuuuuuuuu", "uuuuuuuuuuu");
		if(!BaseConfig.is91Launcher)
			return;
		
		if (version < 10) {
			upgrade30(db);
		}
		if (version < 11) {
			upgrade301(db);
		}
		if (version < 12) {
			upgrade31(db, version);
		}
		if (version < 13) {
			upgrade32(db, version);
		}
		if (version < 14) {
			upgrade35(db, version);
		}
		if (version < 15) {
			upgrade352(db, version);
		}
		if (version < 16) {
			upgrade355(db);
		}
		if (version < 17) {
			upgrade356(db);
		}
		if(version < 18){
			upgrade50(db);
		}
		if(version < 19){
			upgrade51(db);
		}
		
		if(version < 20){
			upgrade531(db);
		}
		
		if(version < 21){
			upgrade532(db);
		}
	}
	
	/**
	 * V5.3.2
	 * 修正dock栏CellX数据
	 */
	private void upgrade532(SQLiteDatabase db) {
		Cursor c = null;
		try{
			for(int s = 0; s <= 2; s ++){
				int screen = s;
				c = db.rawQuery("select " + Favorites._ID + "," + Favorites.CELLX + " from favorites where " + Favorites.CONTAINER + " = " + LauncherSettings.Favorites.CONTAINER_DOCKBAR 
						+ " and " + Favorites.SCREEN + " =" + screen + " order by " + Favorites.CELLX, null);
				if(c != null){
					int count = c.getCount();
					int i = 0;
					while (count > 0 && c.moveToNext()) {
						int cellX = c.getInt(1);
						if(i != cellX){
							int id = c.getInt(0);
							db.execSQL("update favorites set cellX = " + i + " where " +  Favorites._ID + " = " + id);
						}
						i ++;
					}
					c.close();
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			//重置favorites表
			db.execSQL("drop table IF EXISTS favorites");
			db.execSQL(LauncherProvider.CREATE_TABLE_FAVORITES);
			mDatabaseHelper.loadFavorites(db);
		} finally{
			if(c != null)
				c.close();
		}
	}
		
	/**
	 * V5.3.1
	 * favorites表字段： x y width height => cellX cellY spanX spanY
	 */
	private void upgrade531(SQLiteDatabase db) {
		try{
			//Log.e("kkkkkkkkkkkkkkkkkkkkkkkk", "531DB");
			String sql = "alter table favorites add column cellX INTEGER";
			db.execSQL(sql);
			sql = "alter table favorites add column cellY INTEGER";
			db.execSQL(sql);
			sql = "alter table favorites add column spanX INTEGER";
			db.execSQL(sql);
			sql = "alter table favorites add column spanY INTEGER";
			db.execSQL(sql);
			
			LauncherConfig.initCellConfigForUpdate(mContext);
			//更改workspace数据
			int width = CellLayoutConfig.getCellWidth();
			int height = CellLayoutConfig.getCellHeight();
			if(width <= 0 || height <= 0){
				throw new Exception();
			}
			sql = "update favorites set cellX = round((x + 0.0) / " + width + ")," +
					" cellY = round((y + 0.0) / " + height + ")," +
					" spanX = round((width + 0.0) / " + width + ")," +
					" spanY = round((height + 0.0) / " + height + ")" +
					" where " + LauncherSettings.Favorites.CONTAINER + " = " + LauncherSettings.Favorites.CONTAINER_DESKTOP;
			db.execSQL(sql);
			//更改dockbar数据
			width = DockbarCellLayoutConfig.getCellWidth();
			if(width <= 0){
				throw new Exception();
			}
			sql = "update favorites set cellX = round((x + 0.0) / " + width + ")," +
					" cellY = 0," +
					" spanX = 1," +
					" spanY = 1" + 
					" where " + LauncherSettings.Favorites.CONTAINER + " = " + LauncherSettings.Favorites.CONTAINER_DOCKBAR;
			db.execSQL(sql);
			//更新文件夹数据
			sql = "update favorites set cellX = 1," +
					" cellY = 1," +
					" spanX = 1," +
					" spanY = 1" + 
					" where " + LauncherSettings.Favorites.CONTAINER + " != " + LauncherSettings.Favorites.CONTAINER_DOCKBAR +
					" and "+ LauncherSettings.Favorites.CONTAINER + " != " + LauncherSettings.Favorites.CONTAINER_DESKTOP;
			db.execSQL(sql);
			//换表
			sql = String.format(LauncherProvider.CREATE_TABLE_FAVORITES_MODEL, "favorites_Tmp", "");
			db.execSQL(sql);
			sql = "insert into favorites_Tmp select _id, title, intent, container, screen, cellX, cellY, spanX, spanY, itemType, " +
					"appWidgetId, isShortcut, iconType, iconPackage, iconResource, icon, uri, displayMode, defaultIcon from favorites";
			db.execSQL(sql);
			sql = "drop table favorites";
			db.execSQL(sql);
			sql = "alter table favorites_Tmp rename to favorites";
			db.execSQL(sql);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			//重置favorites表
			db.execSQL("drop table IF EXISTS favorites");
			db.execSQL(LauncherProvider.CREATE_TABLE_FAVORITES);
			mDatabaseHelper.loadFavorites(db);
		}
	}
	
	/**
	 * V5.1版本
	 * 在桌面上增加“每日新鲜事”快捷方式
	 */
	private void upgrade51(SQLiteDatabase db) {
		Cursor c = null;
		try {
			c = db.rawQuery("select " + Favorites.INTENT + " from favorites where " + Favorites.CONTAINER + " = " + LauncherSettings.Favorites.CONTAINER_DESKTOP + " and " + Favorites.INTENT
					+ " ='" + LauncherShortcutHelper.INTENT_DAILY_FRESH_NEWS + "'", null);
			if(c.getCount() == 0){
				int[] vacantCell = LauncherProviderHelper.getFirstVacantCell_1819(db, 1, 1, false, true);
				if (vacantCell != null) {
					int screen = vacantCell[0];
					int[] xy = CellLayoutConfig.getXY(vacantCell[1], vacantCell[2]);
					int[] wh = new int[]{CellLayoutConfig.getCellWidth(), CellLayoutConfig.getCellHeight()};
					db.execSQL("insert into favorites (" + Favorites.TITLE + ", " + Favorites.INTENT + ", " + Favorites.CONTAINER + ", " + Favorites.SCREEN + ", " + X_1819 + ", "
							+ Y_1819 + ", " + WIDTH_1819 + ", " + HEIGHT_1819 + ", " + Favorites.ITEM_TYPE + ", " + Favorites.ICON_TYPE + ", " + Favorites.ICON_PACKAGE + ", "
							+ Favorites.ICON_RESOURCE + ")" + "values('" + mContext.getString(R.string.personal_compaign) + "', '" + LauncherShortcutHelper.INTENT_DAILY_FRESH_NEWS + "', "
							+ LauncherSettings.Favorites.CONTAINER_DESKTOP + ", " + screen + ", " + xy[0] + ", " + xy[1] + ", " + wh[0] + ", " + wh[1] + ", "
							+ LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT + ", " + LauncherSettings.BaseLauncherColumns.ICON_TYPE_RESOURCE + ", '" + Global.PKG_NAME + "', '"
							+ Global.PKG_NAME + ":drawable/personal_compaign" + "')");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != c){
				c.close();
			}
		}
	}
	
	/**
	 * V5.0版本
	 * 更改favorites表字段：cellX cellY spanX spanY =>x y width height
	 */
	private void upgrade50(SQLiteDatabase db) {
		try{
			//3.*版本升级用户不显示dock栏文字
			SettingsPreference.getInstance().setShowDockbarText(false);
			LauncherConfig.initCellConfigForUpdate(mContext);
			
			//"我的铃声"和"我的壁纸"并入到"美化手机"中
			db.execSQL("update favorites set " + Favorites.TITLE + "='" + mContext.getResources().getString(R.string.beautify_phone_title) + "',intent='#Intent;component="+Global.PKG_NAME+"/com.nd.hilauncherdev.shop.MyThemeCenterMain;end'" 
					+ " where intent='#Intent;component="+Global.PKG_NAME+"/com.nd.hilauncherdev.myphone.mywallpaper.WallPaperActivity;end'");
			db.execSQL("update favorites set " + Favorites.TITLE + "='" + mContext.getResources().getString(R.string.beautify_phone_title) + "',intent='#Intent;component="+Global.PKG_NAME+"/com.nd.hilauncherdev.shop.MyThemeCenterMain;end'" 
					+ " where intent='#Intent;component="+Global.PKG_NAME+"/com.nd.hilauncherdev.myphone.myring.NdRingActivity;end'");
			
			String sql = "alter table favorites add column x INTEGER";
			db.execSQL(sql);
			sql = "alter table favorites add column y INTEGER";
			db.execSQL(sql);
			sql = "alter table favorites add column width INTEGER";
			db.execSQL(sql);
			sql = "alter table favorites add column height INTEGER";
			db.execSQL(sql);
			//更改workspace数据
			int left = CellLayoutConfig.getMarginLeft();
			int top = CellLayoutConfig.getMarginTop();
			int width = CellLayoutConfig.getCellWidth();
			int height = CellLayoutConfig.getCellHeight();
			sql = "update favorites set x = " + left + " + cellX * " + width + "," +
					" y = " + top + " + cellY * " + height + "," +
					" width = spanX * " + width + "," +
					" height = spanY * " + height + 
					" where " + LauncherSettings.Favorites.CONTAINER + " = " + LauncherSettings.Favorites.CONTAINER_DESKTOP;
			db.execSQL(sql);
			//更改dockbar数据
			left = DockbarCellLayoutConfig.getMarginLeft();
			top = DockbarCellLayoutConfig.getMarginTop();
			width = DockbarCellLayoutConfig.getCellWidth();
			height = DockbarCellLayoutConfig.getCellHeight();
			sql = "update favorites set x = " + left + " + cellX * " + width + "," +
					" y = " + top + "," +
					" width = spanX * " + width + "," +
					" height = spanY * " + height + 
					" where " + LauncherSettings.Favorites.CONTAINER + " = " + LauncherSettings.Favorites.CONTAINER_DOCKBAR;
			db.execSQL(sql);
			//换表
			sql = String.format(CREATE_TABLE_FAVORITES_MODEL_1819, "favorites_Tmp");
			db.execSQL(sql);
			sql = "insert into favorites_Tmp select _id, title, intent, container, screen, x, y, width, height, itemType, " +
					"appWidgetId, isShortcut, iconType, iconPackage, iconResource, icon, uri, displayMode, defaultIcon from favorites";
			db.execSQL(sql);
			sql = "drop table favorites";
			db.execSQL(sql);
			sql = "alter table favorites_Tmp rename to favorites";
			db.execSQL(sql);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			//重置favorites表
			db.execSQL("drop table IF EXISTS favorites");
			db.execSQL(LauncherProvider.CREATE_TABLE_FAVORITES);
			mDatabaseHelper.loadFavorites(db);
		}
		
	}
	
	private void upgrade356(SQLiteDatabase db) {
		try {
			Intent appIntent = AndroidPackageUtils.getPackageMainIntent(mContext, LauncherRecommendApps.PANDASPACE_PCK);
			if (appIntent == null)
				return;
			
			String sqlString = "update favorites set intent = '" + appIntent.toUri(0) + "' where intent like '%" + LauncherRecommendApps.PANDASPACE_PCK + "%'";
			db.execSQL(sqlString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * v3.5.5版本 dock栏顺序调整，最左边增加快捷
	 */
	private void upgrade355(SQLiteDatabase db) {
		try {
			db.execSQL("alter table favorites add defaultIcon BLOB");
			String sqlString = "update favorites set screen = -1 where screen = 1 and " + LauncherSettings.Favorites.CONTAINER + " = " + LauncherSettings.Favorites.CONTAINER_DOCKBAR;
			db.execSQL(sqlString);
			sqlString = "update favorites set screen = screen +1 where " + LauncherSettings.Favorites.CONTAINER + " = " + LauncherSettings.Favorites.CONTAINER_DOCKBAR;
			db.execSQL(sqlString);
			LauncherProviderHelper.addDockThirdScreen(mContext, db);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * v3.5版本 将托盘默认四应用在Favorite表中的的Intent字段更新成 可弹出选择框的 格式
	 */
	private void upgrade352(SQLiteDatabase db, int version) {
		try {
			// 更新天气预览图 caizp 2013-01-29
			String sqlString = "update favorites set " + LauncherSettings.Favorites.ICON_PACKAGE + " ='" 
					+ Global.getApplicationContext().getPackageName() + "' where "
					+ LauncherSettings.BaseLauncherColumns.ITEM_TYPE + " = " + Favorites.ITEM_TYPE_PANDA_PREVIEW_WIDGET
					+ " and " + LauncherSettings.Favorites.ICON_PACKAGE + "='" + ThirdApplicationAssit.CALENDAR_WEATHER_PACKAGE_NAME + "'";
			db.execSQL(sqlString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * v3.5版本 将托盘默认四应用在Favorite表中的的Intent字段更新成 可弹出选择框的 格式
	 */
	private void upgrade35(SQLiteDatabase db, int version) {
		LauncherProviderHelper.updateDbIntentColumnForDefaultDockShortcut(mContext, db);
	}

	private void upgrade32(SQLiteDatabase db, int version) {
		try {
			if (version == 12 && Global.isZh()) {
				// 修复单行本主题黄历天气问题 add by caizp 2012-10-9
				mContext.getSharedPreferences("config", 0).edit().putBoolean("has_imoprt_old_theme_weather", false).commit();
				ThemeImportAssit.import91AlmanacSkin(mContext, null);
			}

			// 3.1版本可能存在没有成功添加app市场桌面快捷方式问题，所以再次添加
			if (Global.isZh()) {
				addAppMarketAgain(db);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void upgrade31(SQLiteDatabase db, int version) {
		try {
			// 兼容我的主题图标 add by jimmy 2012-9-20
			String sqlString = "update favorites set " + LauncherSettings.BaseLauncherColumns.ITEM_TYPE + " = " + LauncherSettings.Favorites.ITEM_TYPE_HI_APPLICATION + " where "
					+ Favorites.INTENT + " like '%ThemeShopMainActivity%' ";
			db.execSQL(sqlString);

			if ((version == 10 || version == 11) && Global.isZh()) {
				// 修复部分黄历天气不换肤的问题 add by caizp 2012-9-25
				mContext.getSharedPreferences("config", 0).edit().putBoolean("has_imoprt_old_theme_weather", false).commit();
				ThemeImportAssit.import91AlmanacSkin(mContext, null);
			}

			if (Global.isZh()) {
				addAppMarket(db);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void upgrade301(SQLiteDatabase db) {
		try {
			db.execSQL("update favorites set " + Favorites.SPANX + "=4," + Favorites.SPANY + "=1 where " + Favorites.ITEM_TYPE + "=2020");
			db.execSQL("update favorites set " + Favorites.SPANX + "=2," + Favorites.SPANY + "=1 where " + Favorites.ITEM_TYPE + "=2018");
			// 非默认主题时，判断当前应用的主题是否大图标主题 caizp 2012-9-12
			if (!ThemeSharePref.getInstance(Global.getApplicationContext()).isDefaultTheme()) {
				boolean isLargeIconTheme = false;
				int largeIconSize = IconAndTextSizeConfig.getLargeIconSize(mContext);
				Drawable maskBg = ThemeManagerFactory.getInstance().getThemeAppIcon(ThemeData.PANDA_ICON_BACKGROUND_MASK);
				if (null != maskBg) {
					if (null != maskBg && maskBg.getIntrinsicWidth() == largeIconSize && maskBg.getIntrinsicHeight() == largeIconSize) {
						isLargeIconTheme = true;
					}
				}
				SettingsPreference.getInstance().setLargeIconTheme(isLargeIconTheme);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void upgrade30(SQLiteDatabase db) {
		try {
			// 兼容熊猫桌面2.x最近安装和经常使用文件夹 add by caizp 2012-8-28
			String recentInstalled = mContext.getString(R.string.folder_recent_installed);
			String recentRunning = mContext.getString(R.string.folder_recent_running);
			db.execSQL("update favorites set " + Favorites.TITLE + "='" + recentInstalled + "'," + Favorites.ITEM_TYPE + "=" + Favorites.ITEM_TYPE_ANYTHING + "," + Favorites.ICON_TYPE + "="
					+ AnythingInfo.FLAG_FOLDER_RECENT_INSTALLED + " where " + Favorites.URI + "='" + Favorites.LATEST_INSTALL_APP_URI.toString() + "'");
			db.execSQL("update favorites set " + Favorites.TITLE + "='" + recentRunning + "'," + Favorites.ITEM_TYPE + "=" + Favorites.ITEM_TYPE_ANYTHING + "," + Favorites.ICON_TYPE + "="
					+ AnythingInfo.FLAG_FOLDER_RECENT_RUNNING + " where " + Favorites.URI + "='" + Favorites.OFTEN_USED_APP_URI.toString() + "'");
			// 删除无用快捷方式
			db.execSQL("delete from favorites where iconResource='"+Global.PKG_NAME+":drawable/launcher_soft_manager'");
			db.execSQL("delete from favorites where iconResource='"+Global.PKG_NAME+":drawable/theme_setting'");
			db.execSQL("delete from favorites where iconResource='"+Global.PKG_NAME+":drawable/icon_mt'");
			// 删除2.x版本的从托盘拖到桌面的"换装"的快捷方式
			db.execSQL("delete from favorites where iconResource='"+Global.PKG_NAME+":drawable/icon_mt'");
			// 删除2.x版本的从程序匣子中拖到桌面的"我的主题"
			db.execSQL("delete from favorites where intent like '%component="+Global.PKG_NAME+"/com.nd.android.pandahome2.manage.shop.ThemeShopLocalThemeActivity%'");
			// 删除无效数据
			db.execSQL("delete from favorites where " + Favorites.ITEM_TYPE + " in (1003,1004,1005,1009,1010,1011,1012,1013,1014,1015,12000)");
			// 兼容2.x百度小部件
			db.execSQL("update favorites set " + Favorites.ITEM_TYPE + "=" + Favorites.ITEM_TYPE_WIDGET_BAIDU + " where " + Favorites.ITEM_TYPE + "=1017");
			// 兼容熊猫桌面2.x非标小部件 add by caizp 2012-8-28
			loadOldPandaWidgetData(mContext, db);
			// 删除今日推荐小部件数据
			db.execSQL("delete from favorites where iconResource='panda_todayhot_widget'");
			db.execSQL("delete from favorites where (iconResource is null or iconResource='') and iconPackage='" + mContext.getPackageName() + "' and " + Favorites.ITEM_TYPE + "="
					+ Favorites.ITEM_TYPE_PANDA_WIDGET);
			// 兼容一键清理小部件
			db.execSQL("update favorites set " + Favorites.ITEM_TYPE + "=" + Favorites.ITEM_TYPE_WIDGET_CLEANER + "," + Favorites.SPANX + "=2," + Favorites.SPANY + "=1 " + " where "
					+ Favorites.ITEM_TYPE + "=1016");
			// 兼容一键关屏和手电筒
			db.execSQL("update favorites set " + Favorites.TITLE + "='" + mContext.getResources().getString(R.string.panda_widget_offscreen) + "'," + Favorites.ITEM_TYPE + "="
					+ Favorites.ITEM_TYPE_CUSTOM_INTENT + "," + Favorites.ICON_PACKAGE + "='" + mContext.getPackageName() + "'," + Favorites.ICON_RESOURCE
					+ "='"+Global.PKG_NAME+":drawable/main_dock_offscreen', intent='#Intent;action="+Global.PKG_NAME+".SETTING_WIDGET_OFFSCREEN;end'" + " where "
					+ Favorites.ICON_PACKAGE + "='com.nd.android.widget.pandahome.onekeyoffscreen'");
			db.execSQL("update favorites set " + Favorites.TITLE + "='" + mContext.getResources().getString(R.string.panda_widget_flashlight) + "'," + Favorites.ITEM_TYPE + "="
					+ Favorites.ITEM_TYPE_CUSTOM_INTENT + "," + Favorites.ICON_PACKAGE + "='" + mContext.getPackageName() + "'," + Favorites.ICON_RESOURCE
					+ "='"+Global.PKG_NAME+":drawable/main_dock_flashlight', intent='#Intent;action="+Global.PKG_NAME+".SETTING_WIDGET_FLASHLIGHT;end'" + " where "
					+ Favorites.ICON_PACKAGE + "='com.nd.android.widget.pandahome.flashlight'");
			// 初始化Dock栏图标
			// loadDockFavorites(db);
			adaptOldDockbar(mContext, db, mDatabaseHelper);
			/**
			 * 底部托盘第二屏
			 */
			LauncherProviderHelper.addMoreDockShortcut(mContext, db);
			// 迁移主题数据
			migrateThemeData30(mContext);
		} catch (Exception e) {// 数据迁移出错直接使用默认布局 caizp 2012-9-4
			e.printStackTrace();
			db.execSQL("delete from favorites");
			mDatabaseHelper.loadFavorites(db);
		}
	}

	/**
	 * <p>
	 * 适配2.7(含2.7)之前的dockbar图标
	 * </p>
	 * 
	 * <p>
	 * date: 2012-9-4 下午03:00:08
	 */
	private void adaptOldDockbar(Context ctx, SQLiteDatabase db, DatabaseHelper helper) {
		if (!shouldAdaptOlderDockbar(ctx)) {
			fixDockbarWrong(db, helper);
			return;
		}
		final SharedPreferences sp = ctx.getSharedPreferences("quick_shortcut", 0);
		if (sp == null) {
			fixDockbarWrong(db, helper);
			return;
		}
		final String[] appNumbers = { "20", "21", "00", "22", "23" };
		final int[] cells = { 0, 1, 2, 3, 4 };
		ContentValues values = null;
		Intent intent = null;
		for (int i = 0; i < appNumbers.length; i++) {
			try {
				final String appNum = appNumbers[i];
				if ("00".equals(appNum)) {
					addDrawerInDockbar(ctx, db, cells[i]);
					continue;
				}
				final String keyPackage = String.format("pref_key_launcher_app%s_package", new Object[] { appNum });
				final String keyClass = String.format("pref_key_launcher_app%s_class", new Object[] { appNum });
				final String keyUri = String.format("pref_key_launcher_app%s_uri", new Object[] { appNum });
				final String keyItemType = String.format("pref_key_launcher_app%s_item_type", new Object[] { appNum });
				String pack = sp.getString(keyPackage, "");
				String clazz = sp.getString(keyClass, "");
				final String uri = sp.getString(keyUri, "");
				int itemType = Integer.parseInt(sp.getString(keyItemType, "0"));
				if ("".equals(pack) || "".equals(clazz) || "".equals(uri))
					continue;
				if (Global.PKG_NAME.equals(pack) && "com.nd.android.pandahome2.manage.shop.ThemeShopMainActivity".equals(clazz)) {
					itemType = Favorites.ITEM_TYPE_APPLICATION;
				}
				if (itemType != Favorites.ITEM_TYPE_APPLICATION) {
					fixDockbarWrong(db, helper);
					return;
				}
				values = new ContentValues();
				LauncherProviderHelper.insertContentValues(values, cells[i], 0, 1, 1);
				values.put(Favorites.SCREEN, 0);
				values.put(Favorites.CONTAINER, Favorites.CONTAINER_DOCKBAR);
				values.put(Favorites.ITEM_TYPE, itemType);
				values.put(LauncherSettings.BaseLauncherColumns.ICON_TYPE, LauncherSettings.BaseLauncherColumns.ICON_TYPE_RESOURCE);
				intent = Intent.parseUri(uri, 0);
				String[] arr = ThemeIconIntentAdaptation.getInstance().getActualApplicationPackageAndClassName(StringUtil.getAppKey(pack, clazz));
				if (arr != null) {
					pack = arr[0];
					clazz = arr[1];
				}
				intent.setComponent(new ComponentName(pack, clazz));
				values.put(Favorites.INTENT, intent.toUri(0));
				db.insert(LauncherProvider.TABLE_FAVORITES, null, values);
			} catch (Exception ex) { // 异常后加载3.0dock
				fixDockbarWrong(db, helper);
				ex.printStackTrace();
				return;
			}
		}
	}

	/**
	 * 是否该适配之前旧版本的dockbar
	 * <p>
	 * TODO
	 * </p>
	 * 
	 * <p>
	 * date: 2012-9-4 下午06:30:20
	 * 
	 * @param ctx
	 * @return
	 */
	private boolean shouldAdaptOlderDockbar(Context ctx) {
		final SharedPreferences sp = ctx.getSharedPreferences("config", 0);
		if (sp != null) {
			/**
			 * 5 为熊猫桌面风格，具体风格参考V2.7源码的HandleStyleConstant常量类
			 */
			return sp.getInt("main_dock_style", 5) == 5;
		}
		return false;
	}

	private void fixDockbarWrong(SQLiteDatabase db, DatabaseHelper helper) {
		try {
			db.delete(LauncherProvider.TABLE_FAVORITES, Favorites.CONTAINER + "=" + Favorites.CONTAINER_DOCKBAR, null);
			loadDockFavorites(db);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * <p>
	 * 添加匣子到底部托盘
	 * </p>
	 * 
	 * <p>
	 * date: 2012-9-4 下午03:57:42
	 * 
	 * @param ctx
	 * @param db
	 * @param cellX
	 */
	private static void addDrawerInDockbar(Context ctx, SQLiteDatabase db, int cellX) {
		final ContentValues values = new ContentValues();
		LauncherProviderHelper.insertContentValues(values, cellX, 0, 1, 1);
		values.put(Favorites.SCREEN, 0);
		values.put(Favorites.CONTAINER, Favorites.CONTAINER_DOCKBAR);
		values.put(Favorites.ITEM_TYPE, Favorites.ITEM_TYPE_INDEPENDENCE);
		values.put(Favorites.INTENT, "#Intent;action="+Global.PKG_NAME+".OPEN_DRAWER;end");
		values.put(Favorites.TITLE, ctx.getString(R.string.dockbar_dock_drawer));
		values.put(Favorites.ICON_TYPE, Favorites.ICON_TYPE_BITMAP);

		final Bitmap bitmap = BitmapUtils.createIconBitmapThumbnail(ctx.getResources().getDrawable(R.drawable.pandahome_style_icon_tray_expand), ctx);
		if (bitmap != null) {
			values.put(Favorites.ICON, BitmapUtils.bitmap2Bytes(bitmap));
		}
		db.insert(LauncherProvider.TABLE_FAVORITES, null, values);
	}
	
	/**
	 * 
	 * 在桌面添加“一键装机”和“一键玩机” <br>
	 * Author:ryan <br>
	 * Date:2012-9-25下午07:52:08
	 */
	private void addAppMarket(SQLiteDatabase db) {
		if (ChannelUtil.getIsCustomByType(ChannelUtil.CUSTOM_CHANNELPACKAGE) || ChannelUtil.getIsCustomByType(ChannelUtil.CUSTOM_VIVO))
			return;

		try {
			addToolsMarket(db);
			addPlayMarket(db);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description: 再次在桌面添加“一键装机”和“一键玩机” Author: guojy Date: 2012-10-16
	 * 下午04:29:47
	 */
	private void addAppMarketAgain(SQLiteDatabase db) {
		if (ChannelUtil.getIsCustomByType(ChannelUtil.CUSTOM_CHANNELPACKAGE)|| ChannelUtil.getIsCustomByType(ChannelUtil.CUSTOM_VIVO))
			return;

		try {
			if (!hasToolsMarket(db)) {
				addToolsMarket(db);
			}
			if (!hasPlayMarket(db)) {
				addPlayMarket(db);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean hasToolsMarket(SQLiteDatabase db) {
		Cursor c = db.rawQuery("select " + Favorites.INTENT + " from favorites where " + Favorites.CONTAINER + " = " + LauncherSettings.Favorites.CONTAINER_DESKTOP + " and " + Favorites.INTENT
				+ " ='" + LauncherShortcutHelper.INTENT_PHONE_NEED + "'", null);
		return c.getCount() != 0;
	}

	private boolean hasPlayMarket(SQLiteDatabase db) {
		Cursor c = db.rawQuery("select " + Favorites.INTENT + " from favorites where " + Favorites.CONTAINER + " = " + LauncherSettings.Favorites.CONTAINER_DESKTOP + " and " + Favorites.INTENT
				+ " ='" + LauncherShortcutHelper.INTENT_PHONE_PLAY + "'", null);
		return c.getCount() != 0;
	}

	private void addToolsMarket(SQLiteDatabase db) {
		int spanX = 1;
		int spanY = 1;
		int[] vacantCell = LauncherProviderHelper.getVacantCellFromBottom(db, spanX, spanY, true);
		if (vacantCell != null) {
			db.execSQL("insert into favorites (" + Favorites.TITLE + ", " + Favorites.INTENT + ", " + Favorites.CONTAINER + ", " + Favorites.SCREEN + ", " + Favorites.CELLX + ", "
					+ Favorites.CELLY + ", " + Favorites.SPANX + ", " + Favorites.SPANY + ", " + Favorites.ITEM_TYPE + ", " + Favorites.ICON_TYPE + ", " + Favorites.ICON_PACKAGE + ", "
					+ Favorites.ICON_RESOURCE + ")" + "values('" + mContext.getString(R.string.app_market_one_key) + "', '" + LauncherShortcutHelper.INTENT_PHONE_NEED + "', "
					+ LauncherSettings.Favorites.CONTAINER_DESKTOP + ", " + vacantCell[0] + ", " + vacantCell[1] + ", " + vacantCell[2] + ", " + spanX + ", " + spanY + ", "
					+ LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT + ", " + LauncherSettings.BaseLauncherColumns.ICON_TYPE_RESOURCE + ", '" + Global.PKG_NAME + "', '"
					+ Global.PKG_NAME + ":drawable/app_market_app_icon" + "')");
		}
	}

	private void addPlayMarket(SQLiteDatabase db) {
		int spanX = 1;
		int spanY = 1;
		int[] vacantCell = LauncherProviderHelper.getVacantCellFromBottom(db, spanX, spanY, true);
		if (vacantCell != null) {
			db.execSQL("insert into favorites (" + Favorites.TITLE + ", " + Favorites.INTENT + ", " + Favorites.CONTAINER + ", " + Favorites.SCREEN + ", " + Favorites.CELLX + ", "
					+ Favorites.CELLY + ", " + Favorites.SPANX + ", " + Favorites.SPANY + ", " + Favorites.ITEM_TYPE + ", " + Favorites.ICON_TYPE + ", " + Favorites.ICON_PACKAGE + ", "
					+ Favorites.ICON_RESOURCE + ")" + "values('" + mContext.getString(R.string.app_market_one_key_play) + "', '" + LauncherShortcutHelper.INTENT_PHONE_PLAY + "', "
					+ LauncherSettings.Favorites.CONTAINER_DESKTOP + ", " + vacantCell[0] + ", " + vacantCell[1] + ", " + vacantCell[2] + ", " + spanX + ", " + spanY + ", "
					+ LauncherSettings.Favorites.ITEM_TYPE_CUSTOM_INTENT + ", " + LauncherSettings.BaseLauncherColumns.ICON_TYPE_RESOURCE + ", '" + Global.PKG_NAME + "', '"
					+ Global.PKG_NAME + ":drawable/app_market_app_icon_play" + "')");
		}
	}

	/**
	 * <br>
	 * Description: 迁移原主题数据 <br>
	 * Author:caizp <br>
	 * Date:2012-9-7下午09:38:29
	 * 
	 * @param ctx
	 */
	private void migrateThemeData30(Context ctx) {
		SQLiteDatabase themeDb = ctx.openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
		Cursor c = themeDb.rawQuery("select ID,NAME,EN_NAME,DESC,Version,type,pandaflag,versioncode from Theme", null);
		List<SimpleTheme> themes = null;
		try {
			if (null != c && c.getCount() > 0) {
				themes = new ArrayList<SimpleTheme>();
				LauncherThemeDataBase newThemeDb = LauncherThemeDataBase.getInstance(mContext);
				try {
					boolean ret = c.moveToFirst();
					while (ret) {
						String id = StringUtil.getNotNullString(c.getString(0));
						String name = StringUtil.getNotNullString(c.getString(c.getColumnIndex("NAME")));
						String enName = StringUtil.getNotNullString(c.getString(c.getColumnIndex("EN_NAME")));
						String desc = StringUtil.getNotNullString(c.getString(c.getColumnIndex("DESC")));
						String version = StringUtil.getNotNullString(c.getString(c.getColumnIndex("Version")));
						int type = c.getInt(c.getColumnIndex("type"));
						int pandaflag = c.getInt(c.getColumnIndex("pandaflag"));
						int versionCode = c.getInt(c.getColumnIndex("Version"));
						String sql = "INSERT INTO Theme (ID,NAME,EN_NAME,DESC,Version," + "type,pandaflag,versioncode,ID_FLAG,PATH,install_time) VALUES('" + id + "','" + name + "','" + enName
								+ "','" + desc + "','" + version + "'," + type + "," + pandaflag + "," + versionCode + ",'" + id + "','" + id.replace("/NOID", "") + "/',"
								+ System.currentTimeMillis() + ")";
						newThemeDb.execSQL(sql);
						SimpleTheme simpleTheme = new SimpleTheme();
						simpleTheme.id = id;
						simpleTheme.themeType = type;
						themes.add(simpleTheme);
						ret = c.moveToNext();
					}
					if (0 != themes.size()) {// 导入旧主题的91黄历天气皮肤 caizp
						// 2012-9-9
						ctx.getSharedPreferences("config", 0).edit().putBoolean("has_imoprt_old_theme_weather", false).commit();
						ThemeImportAssit.import91AlmanacSkin(mContext, themes);
					}
				} finally {
					newThemeDb.close();
				}
			}
		} finally {
			c.close();
			themeDb.close();
		}
	}

	/**
	 * <br>
	 * Description: 迁移熊猫桌面2.x非标小部件数据 <br>
	 * Author:caizp <br>
	 * Date:2012-8-28上午11:28:11
	 * 
	 * @param ctx
	 * @param db
	 */
	private void loadOldPandaWidgetData(Context ctx, SQLiteDatabase db) {
		Cursor c = db.rawQuery("select _id, appWidgetId, spanX, spanY from favorites where itemType='" + Favorites.ITEM_TYPE_PANDA_WIDGET + "'", null);
		if (null != c && c.getCount() > 0) {
			boolean ret = c.moveToFirst();
			SQLiteDatabase widgetDb = ctx.openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
			if (widgetDb.getVersion() < 7) {
				c.close();
				widgetDb.close();
				return;
			} else if (widgetDb.getVersion() >= 7 && widgetDb.getVersion() < 16) {
				while (ret) {
					int itemId = c.getInt(c.getColumnIndex(Favorites._ID));
					int appWidgetId = c.getInt(c.getColumnIndex(Favorites.APPWIDGET_ID));
					int spanX = c.getInt(c.getColumnIndex("spanX"));
					int spanY = c.getInt(c.getColumnIndex("spanY"));
					Cursor cs = widgetDb.rawQuery("select package from PANDAHOME_WIDGET where widgetid=" + appWidgetId, null);
					if (null != cs && cs.getCount() > 0) {
						cs.moveToFirst();
						String pck = cs.getString(cs.getColumnIndex("package"));
						String layoutResName = "";
						// 兼容2.x默认91天气 caizp 2012-8-30
						if (-1001 == appWidgetId) {
							layoutResName = "clockweather_widget_4x2";
						}
						if ("com.nd.android.widget.pandahome.clockweather".equals(pck)) {// 91天气秀
							if (spanX == 4 && spanY == 2) {
								layoutResName = "clockweather_widget_4x2";
							} else if (spanX == 4 && spanY == 1) {
								layoutResName = "clockweather_widget_4x1";
							}
						} else if ("com.nd.android.widget.pandahome.weibo".equals(pck)) {// 微博
							layoutResName = "weibo_widget";
						} else if ("com.nd.android.widget.pandahome.smsmanage".equals(pck)) {// 安卓短信
							layoutResName = "sms_manage_widget";
						} else if ("com.nd.android.widget.pandahome.dailyrecommend".equals(pck)) {// 每日一荐
							layoutResName = "daily_recommend_4x3";
						} else if ("com.nd.android.widget.pandahome.petalclock".equals(pck)) {// 时钟-花瓣时钟
							layoutResName = "petalclock_widget_4x2";
						} else if ("com.nd.android.widget.pandahome.candyclock".equals(pck)) {// 时钟-熊仔棒棒糖
							layoutResName = "candyclock_widget_4x2";
						} else if ("com.nd.android.widget.pandahome.conciseclock".equals(pck)) {// 时钟-简约时钟
							layoutResName = "conciseclock_widget_4x2";
						} else if ("com.nd.android.widget.pandahome.doubletrackclock".equals(pck)) {// 时钟-双轨道时钟
							layoutResName = "doubletrackclock_widget_4x2";
						} else if ("com.nd.android.widget.pandahome.stoneclock".equals(pck)) {// 时钟-石头宝石时钟
							layoutResName = "stoneclock_widget_4x2";
						} else if ("com.nd.android.widget.pandahome.magnifierclock".equals(pck)) {// 时钟-放大镜时钟
							layoutResName = "magnifierclock_widget_4x2";
						}
						db.execSQL("update favorites set iconPackage='" + pck + "',iconResource='" + layoutResName + "' where _id=" + itemId);

						if (mContext.getPackageName().equals(pck)) {// 熊猫桌面通过手动添加的一键清理和百度widget
							if (spanX == 1 && spanY == 1) {// 一键清理
								db.execSQL("update favorites set " + Favorites.ITEM_TYPE + "=" + Favorites.ITEM_TYPE_WIDGET_CLEANER + "," + Favorites.SPANX + "=2," + Favorites.SPANY + "=1 "
										+ " where _id=" + itemId);
							} else if (spanX == 4 && spanY == 1) {// 百度widget
								db.execSQL("update favorites set " + Favorites.ITEM_TYPE + "=" + Favorites.ITEM_TYPE_WIDGET_BAIDU + " where _id=" + itemId);
							}
						}
					}
					cs.close();
					ret = c.moveToNext();
				}
				c.close();
				widgetDb.close();
			} else if (widgetDb.getVersion() >= 16) {
				while (ret) {
					int itemId = c.getInt(c.getColumnIndex(Favorites._ID));
					int appWidgetId = c.getInt(c.getColumnIndex(Favorites.APPWIDGET_ID));
					int spanX = c.getInt(c.getColumnIndex("spanX"));
					int spanY = c.getInt(c.getColumnIndex("spanY"));
					Cursor cs = widgetDb.rawQuery("select package,layout_res_name from PANDAHOME_WIDGET where widgetid=" + appWidgetId, null);
					if (null != cs && cs.getCount() > 0) {
						cs.moveToFirst();
						String pck = cs.getString(cs.getColumnIndex("package"));
						String layoutResName = cs.getString(cs.getColumnIndex("layout_res_name"));
						// 兼容2.x默认91天气 caizp 2012-8-30
						if (-1001 == appWidgetId) {
							layoutResName = "clockweather_widget_4x2";
						}
						if ("com.nd.android.widget.pandahome.clockweather".equals(pck)) {// 91天气秀
							if (spanX == 4 && spanY == 2) {
								layoutResName = "clockweather_widget_4x2";
							} else if (spanX == 4 && spanY == 1) {
								layoutResName = "clockweather_widget_4x1";
							}
						} else if ("com.nd.android.widget.pandahome.weibo".equals(pck)) {// 微博
							layoutResName = "weibo_widget";
						} else if ("com.nd.android.widget.pandahome.smsmanage".equals(pck)) {// 安卓短信
							layoutResName = "sms_manage_widget";
						} else if ("com.nd.android.widget.pandahome.dailyrecommend".equals(pck)) {// 每日一荐
							layoutResName = "daily_recommend_4x3";
						} else if ("com.nd.android.widget.pandahome.petalclock".equals(pck)) {// 时钟-花瓣时钟
							layoutResName = "petalclock_widget_4x2";
						} else if ("com.nd.android.widget.pandahome.candyclock".equals(pck)) {// 时钟-熊仔棒棒糖
							layoutResName = "candyclock_widget_4x2";
						} else if ("com.nd.android.widget.pandahome.conciseclock".equals(pck)) {// 时钟-简约时钟
							layoutResName = "conciseclock_widget_4x2";
						} else if ("com.nd.android.widget.pandahome.doubletrackclock".equals(pck)) {// 时钟-双轨道时钟
							layoutResName = "doubletrackclock_widget_4x2";
						} else if ("com.nd.android.widget.pandahome.stoneclock".equals(pck)) {// 时钟-石头宝石时钟
							layoutResName = "stoneclock_widget_4x2";
						} else if ("com.nd.android.widget.pandahome.magnifierclock".equals(pck)) {// 时钟-放大镜时钟
							layoutResName = "magnifierclock_widget_4x2";
						}
						db.execSQL("update favorites set iconPackage='" + pck + "',iconResource='" + layoutResName + "' where _id=" + itemId);

						if (mContext.getPackageName().equals(pck)) {// 熊猫桌面通过手动添加的一键清理和百度widget
							if (spanX == 1 && spanY == 1) {// 一键清理
								db.execSQL("update favorites set " + Favorites.ITEM_TYPE + "=" + Favorites.ITEM_TYPE_WIDGET_CLEANER + "," + Favorites.SPANX + "=2," + Favorites.SPANY + "=1 "
										+ " where _id=" + itemId);
							} else if (spanX == 4 && spanY == 1) {// 百度widget
								db.execSQL("update favorites set " + Favorites.ITEM_TYPE + "=" + Favorites.ITEM_TYPE_WIDGET_BAIDU + " where _id=" + itemId);
							}
						}
					}
					cs.close();
					ret = c.moveToNext();
				}
				c.close();
				widgetDb.close();
			}
		}

		// 兼容2.x系统开关小部件
		c = db.rawQuery("select _id from favorites where itemType=1007 or itemType=1008", null);
		if (null != c && c.getCount() > 0) {
			boolean ret = c.moveToNext();
			while (ret) {
				int itemId = c.getInt(c.getColumnIndex("_id"));
				int appWidgetId = mDatabaseHelper.getAppWidgetHost().allocateAppWidgetId();
				db.execSQL("update favorites set spanX=4, spanY=1," + Favorites.ICON_PACKAGE + "='" + mContext.getPackageName() + "'," + Favorites.ICON_RESOURCE + "='widget_switch_view',"
						+ Favorites.ITEM_TYPE + "=" + Favorites.ITEM_TYPE_PANDA_WIDGET + "," + Favorites.APPWIDGET_ID + "=" + appWidgetId + " where _id=" + itemId);
				ret = c.moveToNext();
			}
		}
		c.close();
	}

	/**
	 * Loads the default dock app
	 * 
	 * @param db
	 *            The database to write the values into
	 */
	void loadDockFavorites(SQLiteDatabase db) {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		ContentValues values = new ContentValues();

		try {
			XmlResourceParser parser = null;
			parser = mContext.getResources().getXml(R.xml.default_workspace);
			AttributeSet attrs = Xml.asAttributeSet(parser);
			XmlUtils.beginDocument(parser, LauncherProviderHelper.TAG_FAVORITES);

			final int depth = parser.getDepth();

			int type;
			while (((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
				if (type != XmlPullParser.START_TAG) {
					continue;
				}

				final String name = parser.getName();

				TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.Favorite);
				String screen = a.getString(R.styleable.Favorite_screen);
				values.clear();
				values.put(LauncherSettings.Favorites.CONTAINER, LauncherSettings.Favorites.CONTAINER_DESKTOP);
				values.put(LauncherSettings.Favorites.SCREEN, screen);
				
				LauncherProviderHelper.insertContentValues(values, a.getInt(R.styleable.Favorite_x, 1), 
						a.getInt(R.styleable.Favorite_y, 1), 1, 1);
				
//				values.put(LauncherSettings.Favorites.CELLX, a.getString(R.styleable.Favorite_x));
//				values.put(LauncherSettings.Favorites.CELLY, a.getString(R.styleable.Favorite_y));

				if (LauncherProviderHelper.TAG_DOCK_SHORTCUT.equals(name)) {
					LauncherProviderHelper.addUriDockShortcut(mContext, db, values, a);
				}
				a.recycle();
			}
		} catch (XmlPullParserException e) {
			Log.w(TAG, "Got exception parsing favorites.", e);
		} catch (IOException e) {
			Log.w(TAG, "Got exception parsing favorites.", e);
		}
		return;
	}
}
