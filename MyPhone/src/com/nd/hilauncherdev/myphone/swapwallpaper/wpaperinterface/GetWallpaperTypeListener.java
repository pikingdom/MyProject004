package com.nd.hilauncherdev.myphone.swapwallpaper.wpaperinterface;


public interface GetWallpaperTypeListener {
	
	/**
	 * 获取壁纸类型列表成功
	 */
	public void onGetWallPaperItemSuccess(Object object);
	
	 /**
     * 获取壁纸类型列表失败
     */
    public void onGetWallPaperItemFailed();

}
