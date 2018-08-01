package com.nd.hilauncherdev.myphone.swapwallpaper.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nd.hilauncherdev.myphone.swapwallpaper.bean.WallpaperItemInfo;

public class WallpaperDbHelper {
	
	public static final String QUERY_SQL = "select * from " + WallpaperDB.TABLE_NAME;
	private static final String INSERT_SQL = "INSERT INTO " + WallpaperDB.TABLE_NAME + "(url,typeId,localPath)" + "VALUES(?,?,?)";
	public static final String QUERY_SQL_1 = QUERY_SQL +" where 1=1 order by id";
	public static final String QUERY_SQL_BY_ISUSED = QUERY_SQL + " where isUsed=? order by id";
	public static final String QUERY_SQL_BY_ISUSED_1 = QUERY_SQL + " where isUsed='%s' order by id";
	public static final String UPDATE_SQL_ISUSED = "update " + WallpaperDB.TABLE_NAME + " set isUsed='%s' where id='%s'";
	public static final String UPDATE_SQL_LOCAL_PATH = "update " + WallpaperDB.TABLE_NAME + " set localPath='%s' where id='%s'";
	public static final String DELETE_SQL = "delete from "+ WallpaperDB.TABLE_NAME + " where url='%s'";
	public static final String DELETE_SQL_LOCALPATH = "delete from "+ WallpaperDB.TABLE_NAME + " where id in(select id from " + WallpaperDB.TABLE_NAME + " limit '%s')";
	private static final String SQL_QUERY_FILTER = QUERY_SQL +" where isUsed=2";
	private static final String SQL_DELETE_FILTER = "delete from " + WallpaperDB.TABLE_NAME + " where isUsed=2";
	private static final String SQL_DELETE_BY_ID = "delete from " + WallpaperDB.TABLE_NAME + " where id='%s'";
	
	
	
	/**
	 * 保存壁纸地址
	 * @param context
	 * @param wallpaperInfos
	 */
	public static boolean insertWallpaperInfo(Context context,List<WallpaperItemInfo> wallpaperInfos,String localPath){
		boolean bResult = false;
		WallpaperDB db = null;
		try{
			db = WallpaperDB.getInstance(context);
			if(wallpaperInfos != null && wallpaperInfos.size() > 0){
				for(WallpaperItemInfo info : wallpaperInfos){
					
					ContentValues values = new ContentValues();
					values.put("url", info.wallPaperurl);
					values.put("typeId", info.typeId);
					values.put("localPath", localPath + info.fileName);
					long id = db.insertOrThrow(WallpaperDB.TABLE_NAME, null, values);
					info.id = (int) id;
					
//					db.execSQL(INSERT_SQL, new String[]{info.wallPaperurl,info.typeId,localPath + info.fileName});
				}
				bResult = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (db!=null){
				db.close();
			}
		}
		return bResult;
	}
	
	public static void execSQL(Context context,String sql){
		WallpaperDB db = null;
		try{
			db = WallpaperDB.getInstance(context);
			db.execSQL(sql);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (db!=null){
				db.close();
			}
		}
	}
	
	public static void query(Context context,String sql,String []selectionArgs){
		List<WallpaperItemInfo> swapWallpaperInfos = null;
		WallpaperDB db = null;
		Cursor cursor = null;
		try{
			db = WallpaperDB.getInstance(context);
			cursor = db.query(sql, selectionArgs);
			if(cursor != null){
				swapWallpaperInfos = new ArrayList<WallpaperItemInfo>();
				WallpaperItemInfo info;
				while(cursor.moveToNext()){
					info = new WallpaperItemInfo();
					info.id = cursor.getInt(0);
					info.wallPaperurl = cursor.getString(1);
					info.isUsed = cursor.getInt(2);
					info.typeId = cursor.getString(3);
					info.wPaperType = cursor.getString(4);
					info.localPath = cursor.getString(5);
					swapWallpaperInfos.add(info);
				}	
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (db!=null){
				db.close();
			}
		}
	}
	
	/**
	 * 查询壁纸地址
	 */
	public static List<WallpaperItemInfo> queryWallpaperFromDB(Context context,String sql,String []selectionArgs){
		List<WallpaperItemInfo> swapWallpaperInfos = null;
		WallpaperDB db = null;
		Cursor cursor = null;
		try{
			db = WallpaperDB.getInstance(context);
			cursor = db.query(sql,selectionArgs);
			if(cursor != null){
				swapWallpaperInfos = new ArrayList<WallpaperItemInfo>();
				WallpaperItemInfo info;
				while(cursor.moveToNext()){
					info = new WallpaperItemInfo();
					info.id = cursor.getInt(0);
					info.wallPaperurl = cursor.getString(1);
					info.isUsed = cursor.getInt(2);
					info.typeId = cursor.getString(3);
					info.wPaperType = cursor.getString(4);
					info.localPath = cursor.getString(5);
					swapWallpaperInfos.add(info);
				}	
			}
		}catch (Exception e) {
			e.printStackTrace();	
		}finally{
			if (cursor!=null) {
				cursor.close();
			}
			if (db!=null) {
				db.close();
			}
		}
		return swapWallpaperInfos;
	}
	
	public static void deleteUsedWallpaper(Context context, List<WallpaperItemInfo> wallpaperInfos) {
		if (wallpaperInfos == null || wallpaperInfos.size() <= 0) {
			return;
		}
		
		WallpaperDB db = null;
		try {
			db = WallpaperDB.getInstance(context);
			for (WallpaperItemInfo info : wallpaperInfos) {
				if (info == null) {
					continue;
				}
				
				String sql = String.format(WallpaperDbHelper.SQL_DELETE_BY_ID, info.id);
				db.execSQL(sql);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (db!=null){
				db.close();
			}
		}
	}
	
	public static void moveToFilterList(Context context, List<WallpaperItemInfo> wallpaperInfos) {
		if (wallpaperInfos == null || wallpaperInfos.size() <= 0) {
			return;
		}
		
		WallpaperDB db = null;
		try {
			db = WallpaperDB.getInstance(context);
			for (WallpaperItemInfo info : wallpaperInfos) {
				if (info == null) {
					continue;
				}
				
				String sql = String.format(WallpaperDbHelper.UPDATE_SQL_ISUSED, '2', info.id);
				db.execSQL(sql);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (db!=null){
				db.close();
			}
		}
	}
	
	public static int getFilterListSize(Context context) {
		WallpaperDB db = null;
		Cursor cursor = null;
		int size = 0;
		try{
			db = WallpaperDB.getInstance(context);
			cursor = db.query(SQL_QUERY_FILTER, null);
			if (cursor != null) {
				size = cursor.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (cursor != null) {
				cursor.close();
			}
			
			if (db!=null) {
				db.close();
			}
		}
		
		return size;
	}
	
	public static void clearFilterList(Context context) {
		execSQL(context, SQL_DELETE_FILTER);
	}
}
