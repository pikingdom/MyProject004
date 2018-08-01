package com.nd.weather.widget.UI.banner;

import android.content.Context;
import android.content.Intent;

/**
 * 主题商城对外接口
 */
public class ThemeShopActivityOpenApi {

	public static final String TAG = ThemeShopActivityOpenApi.class.getSimpleName();
	
	/**
	 * 打开主题详情
	 * @param ctx
	 * @param themeId
	 * @param placeId 下载位置
	 */
	public static void startThemePreview(Context ctx, String themeId, int placeId){
		startActivitySafe(ctx, UriIntents.getThemeDetailIntent(ctx, themeId, placeId));
	}
	
	/**
	 * 打开主题详情
	 * @param ctx
	 * @param themeId
	 */
	public static void startThemePreview(Context ctx, String themeId){
		startThemePreview(ctx, themeId, -1);
	}
	
	/**
	 * 打开主题专辑
	 * @param ctx
	 * @param tagName 专辑名称
	 * @param tagId   专辑id
	 */
	public static void startThemeTag(final Context ctx, final String tagName, final int tagId){
		startActivitySafe(ctx, UriIntents.getThemeEvent(ctx, tagName, tagId));
	}
	
	/**
	 * 打开活动
	 * @param ctx
	 * @param linkUrl
	 */
	public static void startCompaignActivity(final Context ctx, final String linkUrl){
		startActivitySafe(ctx, UriIntents.getCompaignIntent(ctx, linkUrl));
	}
	
	/**
	 *打开web主题专题
	 */
	public static void startThemeWebTag(final Context ctx, final String linkUrl, final String linkTitle){
		startActivitySafe(ctx, UriIntents.getThemeEventWeb(ctx, linkUrl, linkTitle));
	}
	
	public static void startActivitySafe(Context ctx, Intent intent){
		try {
			ctx.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
