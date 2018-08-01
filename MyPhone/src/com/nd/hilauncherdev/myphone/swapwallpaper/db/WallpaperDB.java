package com.nd.hilauncherdev.myphone.swapwallpaper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nd.hilauncherdev.framework.db.AbstractDataBase;

public class WallpaperDB  extends AbstractDataBase{
	private static final int VERSION = 2;
	private static final String DB_NAME = "wallpaper.db";
	public static final String TABLE_NAME = "Wallpaper";

	private static WallpaperDB mWallpaperDB;
	//壁纸
	private static final String SQL_CREATE_TABLE_WALLPAPER = ""
			+ "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
			+ "  ( "
			+ "     id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "     url TEXT NOT NULL, "
			+ "     isUsed INTEGER DEFAULT 0, "
			+ "     typeId VARCHAR(10), "
			+ "     wPaperType TEXT, "
			+ "     localPath TEXT"
			+ "  )";
	
	private static final String[] SQL_ALERT_TABLE_WALLPAPER ={"ALTER TABLE" + TABLE_NAME + " ADD typeId VARCHAR(10)","ALTER TABLE" + TABLE_NAME + " ADD wPaperType TEXT",
		"ALTER TABLE" + TABLE_NAME + " ADD localPath TEXT"};
	
	

	private WallpaperDB(Context c) {
		super(c, DB_NAME, VERSION);
		
	}
	
	public static WallpaperDB getInstance(Context c){
		if(mWallpaperDB == null){
			mWallpaperDB = new WallpaperDB(c.getApplicationContext());
		}
		return mWallpaperDB;
	}

	@Override
	public void onDataBaseCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_WALLPAPER);
		
	}

	@Override
	public void onDataBaseDowngrade(SQLiteDatabase db, int arg1, int arg2) {
		
	}

	@Override
	public void onDataBaseUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion < 2){
			try {
				for (String sql : SQL_ALERT_TABLE_WALLPAPER) {
					db.execSQL(sql);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e("WallpaperDB", ex.getMessage());
			}
		}
	}

}
