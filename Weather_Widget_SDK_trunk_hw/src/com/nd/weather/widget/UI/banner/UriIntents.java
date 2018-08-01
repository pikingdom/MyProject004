package com.nd.weather.widget.UI.banner;

import android.content.Context;
import android.content.Intent;

/**
 * 桌面资源调用Intent
 *
 * @author guojianyun
 */
public class UriIntents {

	/**
	 * 主题专辑
	 * @param ctx
	 * @param tagName
	 * @param tagId
	 * @return
	 */
	public static Intent getThemeEvent(Context ctx, final String tagName, final int tagId){
		Intent openThemeTagView = new Intent();
		openThemeTagView.setClassName(ctx, "com.nd.hilauncherdev.shop.shop6.themelist.ThemeShopV6ThemeListActivity");
		openThemeTagView.putExtra("paraObjTitle", tagName);
		openThemeTagView.putExtra("paraCataEnumCode", 2);
		openThemeTagView.putExtra("paraObjRid", tagId);
		return openThemeTagView;
	}
	
	/**
	 * web主题专辑
	 * @param ctx
	 * @param linkUrl
	 * @param linkTitle
	 * @return
	 */
	public static Intent getThemeEventWeb(Context ctx, final String linkUrl, final String linkTitle){
		Intent intent = new Intent();
		intent.setClassName(ctx, "com.nd.hilauncherdev.shop.shop6.webview.ThemeShopV6ThemeListActivity");
		intent.putExtra("postUrl", linkUrl);
		intent.putExtra("postTitle", linkTitle);
		return intent;
	}
	
	/**
	 * 主题详情
	 * @param ctx
	 * @param themeId
	 * @param placeId
	 * @return
	 */
	public static Intent getThemeDetailIntent(Context ctx, String themeId, int placeId){
		Intent intent = new Intent();
		intent.setClassName(ctx, "com.nd.hilauncherdev.shop.shop6.themedetail.ThemeShopV6DetailActivity");
		if (placeId>0){
			intent.putExtra("placeId", placeId);
		}
		intent.putExtra("themeid", themeId);
		return intent;
	}
	
	/**
	 * 活动详情
	 * @param ctx
	 * @param linkUrl
	 * @return
	 */
	public static Intent getCompaignIntent(Context ctx, final String linkUrl){
		Intent intent = new Intent();
		intent.setClassName(ctx, "com.nd.hilauncherdev.menu.personal.icompaign.ForwordCompaignActivity");
		intent.putExtra("postUrl", linkUrl);
		return intent;
	}

}
