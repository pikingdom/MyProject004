package com.nd.hilauncherdev.launcher;

import com.nd.hilauncherdev.launcher.info.ItemInfo;

import android.content.ContentValues;
/**
 * 任意类型桌面信息
 * <br>Author:ryan
 * <br>Date:2012-7-19下午04:09:07
 */
public class AnythingInfo extends ItemInfo {
    /**
	 * 最近安装
	 */
	public static final String INSTALLED = "folder_recent_installed";
    /**
	 * 最近打开
	 */
	public static final String RUNNNING = "folder_recent_running";
    /**
     * 最近任务
     */
    public static final String RECENT = "folder_recent";


    public static int FLAG_FOLDER_RECENT_INSTALLED = 1;
	public static int FLAG_FOLDER_RECENT_RUNNING = 2;
	public static int FLAG_FOLDER_RECOMMEND_APPS = 3;// 推荐文件夹
    public static int FLAG_FOLDER_RECENT = 4; //最近文件夹 包含最近安装 最近打开 最近使用
	
	public int flag;
	public String title;
	
	@Override
	public void onAddToDatabase(ContentValues values) {
		super.onAddToDatabase(values);
		values.put(LauncherSettings.BaseLauncherColumns.ICON_TYPE, flag);
	}
}
