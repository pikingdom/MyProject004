package com.nd.hilauncherdev.shop.api6.model;

import android.graphics.Rect;

/**
 * 广告
 */
public class AdvertItem {
	
	/**Loading位置*/
	public static final int AD_POSTION_LOADING = 0;

	/**Banner位置*/
	public static final int AD_POSTION_BANNER = 1;
	
	/**主题专辑 TAG TOPIC位置*/
	public static final int AD_POSTION_TOPIC = 2;	
	
	/**天气插件设置界面 Banner位置 天气jar包有引用该属性勿删 */
	public static final int AD_POSTION_WEATHER_BANNER = 3;
	
	/**广点通banner广告*/
	public static final int AD_POSTION_WIDE_POINT_BANNER = 8;
	
	/**广点通开屏广告*/
	//public static final int AD_POSTION_WIDE_POINT_OPEN_SCREEN = 9;
	
	/**广告分发统计-资源类型-广告*/
	public static final int AD_REPORT_RESTYPE_AD = 2;
	
	/**广告分发统计-统计类型-点击*/
	public static final int AD_REPORT_STATTYPE_CLICK = 1;
	
	/**广告分发统计-统计类型-展示*/
	public static final int AD_REPORT_STATTYPE_SHOW = 2;
	
	/**广告id*/
	public int adItemId = -1;
	
	/**广告位置: 0Loading 1主题Banner 2主题专辑  */
	public int adPostion;

	/**广告名称*/
	public String adName;
	
	/**图片高度*/
	public int adHeight;
	
	/**图片宽度*/
	public int adWidth;
	
	/**图片地址*/
	public String adPicUrl;
	
	/**打开广告链接*/
	public String actionUrl;
	
	/**广告动作参数*/
	public String actionIntent;
	
	/**展示时间 (loading位置专用)*/
	public long splashTime = 3000;
	
	/** 展示截止时间  */
	public String endTime;
	
	/**  点击响应区域  */
	public Rect actionArea;
}
