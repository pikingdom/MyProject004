package com.nd.hilauncherdev.myphone.swapwallpaper.bean;

import android.os.Environment;

import com.nd.hilauncherdev.datamodel.CommonGlobal;

/**
 * 描述:我的壁纸的相关常量
 * 
 * @author linqiang(866116)
 * @Since 2012-5-18
 */
public class WallPaperContants {

	/**
	 * 我的手机我的壁纸 sdcard根目录
	 */

	public static final String BASE_DIR = CommonGlobal.getBaseDir() + "/myphone/wallpaper";

	/**
	 * 其它壁纸目录
	 */
	public static final String[] OTHER_WALLPAPER_DIRS = new String[] { Environment.getExternalStorageDirectory() + "/wallpaper/", Environment.getExternalStorageDirectory() + "/SmartHome/Pictures/",
			Environment.getExternalStorageDirectory() + "/PandaSpace/pictures/" };

	/**
	 * 其它壁纸目录名称
	 */
	public static final String[] OTHER_WALLPAPER_NAME = new String[] { "默认壁纸", "安卓桌面壁纸", "91桌面壁纸" };

	public static final String OTHER_WALLPAPER_BASE = BASE_DIR + "/.cache/local/thumb/";

	/**
	 * 其它壁纸缩略图目录
	 */
	public static final String[] OTHER_WALLPAPER_THUMB_PATH = new String[] { OTHER_WALLPAPER_BASE + "other/", OTHER_WALLPAPER_BASE + "SmartHome/", OTHER_WALLPAPER_BASE + "PandaSpace/" };

	/**
	 * 在线下载的壁纸存放目录
	 */
	public final static String PICTURES_HOME = BASE_DIR + "/Pictures/";
	
	/**
	 * 一键换壁纸存放目录
	 * WallpaperOneClickChange.java有使用
	 */
	public final static String PICTURES_ONEKEY_HOME = BASE_DIR + "/PicturesOneKey/";
	
	/**
	 * 一键换壁纸类型风格存放目录
	 */
	public final static String PICTURES_ONEKEY_STYLE = BASE_DIR + "/wallPaperStyleLogo/";

	/**
	 * 一键换壁纸使用前存放目录
	 */
	public final static String PICTURES_ONEKEY_TMP = BASE_DIR + "/PicturesOneKey_tmp/";
	
	/**
	 * 在线商城列表缩略图下载线程池大小
	 */
	public static final int ONLINE_SHOP_THUMB_DOWNLOAD_THREAD_POOL_NUMS = 10;


    /**
	 * 推荐
	 */
	public final static int TAB_RECOMMEND_TAG = 19;
	/**
	 * 最新
	 */
	public final static int TAB_NEWEST_TAG = 4;
	/**
	 * 最热
	 */
	public final static int TAB_HOTEST_TAG = 14;

	/**
	 * 本周排行
	 */
	public final static int TAB_WEEK_HOT_TAG = 14;
	/**
	 * 总排行
	 */
	public final static int TAB_TOTAL_HOT_TAG = -1;
	/**
	 * 月排行
	 */
	public final static int TAB_MONTH_HOT_TAG = 12;

	/**
	 * 壁纸分类图标缓存目录
	 */
	public final static String CLASSIFY_CACHE_DIR = BASE_DIR + "/.cache/classify/";

	/**
	 * 壁纸缩略图缓存目录
	 */
	public final static String WP_THUMB_CACHE_DIR = BASE_DIR + "/.cache/thumb/";

	/**
	 * 本地壁纸缩略图缓存目录
	 * WallpaperUtil.java也有使用
	 */
	public final static String WP_LOCAL_THUMB_CACHE_DIR = BASE_DIR + "/.cache/local/thumb/";

	/**
	 * 壁纸原图缓存目录
	 */
	public final static String WP_REAL_CACHE_DIR = BASE_DIR + "/.cache/wpreal/";

	/**
	 * 国内服务器地址
	 */
	public static final String BASE_SERVER_ZH = "pandahome.ifjing.com";

	/**
	 * 英文服务器地址
	 */
	public static final String BASE_SERVER_EN = "bbx.pandaapp.com";

	/**
	 * 国内服务器地址
	 */
	public static final String BASE_CLASSIFY_SERVER_ZH = "pandahome.ifjing.com";

	/**
	 * 在线壁纸每页显示个数
	 */
	public static final int SHOP_WALLPAPER_PAGE_SIZE = 10;

	/**
	 * 精品壁纸
	 */
	public final static int HOT_WALLPAPER = 0;

	/**
	 * 最新壁纸
	 */
	public final static int NEWEST_WALLPAPER = HOT_WALLPAPER + 1;

	/**
	 * 最新壁纸
	 */
	public final static int CALSSIFY_WALLPAPER = HOT_WALLPAPER + 2;

	/**
	 * 壁纸搜索
	 */
	public final static int SEARCH_WALLPAPER = HOT_WALLPAPER + 3;
	
	/**
	 * 滤镜
	 */
	public final static int FILTER = HOT_WALLPAPER + 4;

	/**
	 * 刷新热词间隔时间
	 */

	public final static long constLongRefreshIntervalTime = 100;

	// 壁纸新功能
	public static String WP_NEW_FUNCTION = "wp_new_function";

}
