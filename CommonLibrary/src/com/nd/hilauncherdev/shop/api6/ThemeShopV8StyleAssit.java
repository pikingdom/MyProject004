package com.nd.hilauncherdev.shop.api6;

import android.content.Context;

import com.nd.hilauncherdev.theme.pref.ThemeSharePref;

public class ThemeShopV8StyleAssit {

	// 默认有匣子桌面风格
	public static final String DEFAULT_COMPLEX_SCENE_ID = "-1";
	// 无匣子桌面风格
	public static final String DEFAULT_SIMPLE_SCENE_ID = "-2";
	/** v6风格 */
	public static final String STYLE_V6_SCENE_ID = "v6";
	// 品牌定制
	public static final String STYLE_GOOGLE_SCENE_ID = "google_style";
	public static final String STYLE_MIUI_SCENE_ID = "miui_style";
	private static final String STYLE_FLYME_SCENE_ID = "flyme_style";
	// private static final String STYLE_EMUI_SCENE_ID = "emui_style";

	/**
	 * 桌面默认风格配置数组 字段分别表示：桌面风格的情景id， 桌面数据存储的表名，是否有匣子
	 */
	private static final String[][] DEFAULT_LAUNCHER_STYLES = {
			{ DEFAULT_COMPLEX_SCENE_ID, "favorites", "true" },
			{ DEFAULT_SIMPLE_SCENE_ID, "favorites_simple", "false" } };

	/**
	 * v7新桌面风格配置数组 字段分别表示：桌面风格的情景id， 桌面数据存储的表名，是否有匣子
	 */
	private static final String[][] V7_NEW_STYLES = {
			{ STYLE_GOOGLE_SCENE_ID, "favorites_google", "true" },
			{ STYLE_MIUI_SCENE_ID, "favorites_miui", "false" },
			// {STYLE_EMUI_SCENE_ID, "favorites_emui", "false"},
			{ STYLE_FLYME_SCENE_ID, "favorites_flyme", "false" },
			{ STYLE_V6_SCENE_ID, "favorites_v6", "true" } };

	/**
	 * v5情景的id和resid
	 */
	public static final String[][] v5_EX_STYLES = { { "dishes_1000", "9" },
			{ "myhouse_201308111442", "17" },
			{ "pinkhouse_201308221406", "19" }
	// {"apk_com.nd.old.desktopcontacts", "42"}
	};

	/**
	 * Description: 是否为默认风格桌面(有匣子或没匣子，不是老人桌面和情景桌面) Author: guojianyun_91 Date:
	 * 2015年8月31日 下午5:54:58
	 * 
	 * @param sceneId
	 * @return
	 */
	public static boolean isDefaultStyleLauncher(String sceneId) {
		for (String[] sArray : DEFAULT_LAUNCHER_STYLES) {
			if (sArray[0].equals(sceneId))
				return true;
		}
		return false;
	}

	/**
	 * Description: 是否为新风格情景桌面，这些情景需在线下载 Author: guojianyun_dian91 Date:
	 * 2015年11月15日 上午9:35:55
	 * 
	 * @param sceneId
	 * @return
	 */
	public static boolean isV7NewStyleScene(String sceneId) {
		for (String[] sArray : V7_NEW_STYLES) {
			if (sArray[0].equals(sceneId))
				return true;
		}
		return false;
	}

	/**
	 * Description: 是否为V5情景 Author: guojianyun_dian91 Date: 2015年11月15日
	 * 上午10:42:24
	 * @param id
	 * @return
	 */
	public static boolean isV5StyleScene(String id) {
		if (id == null)
			return false;
		for (String[] ids : v5_EX_STYLES) {
			if (ids[0].equals(id))
				return true;
		}
		return false;
	}
	
	/**
	 * <br>Description: 判断是否当前风格
	 * <br>Author:caizp
	 * <br>Date:2016年12月27日上午11:33:28
	 * @param context
	 * @param styleId
	 * @param styleThemeId
	 * @return
	 */
	public static boolean isCurrentStyle(Context context, String styleId, String styleThemeId) {
        String currentStyleId = ThemeSharePref.getInstance(context).getCurrentSceneId();
        if(isDefaultStyleLauncher(styleId) || isV5StyleScene(styleId) || isV7NewStyleScene(styleId)) {
        	return styleId.equals(currentStyleId);
        }
        String currentStyleThemeId = context.getSharedPreferences("shopdataprefs", Context.MODE_PRIVATE | 4).getString("current_v8_new_style_theme_id", "");
        return currentStyleId.equals(styleId) && currentStyleThemeId.equals(styleThemeId);
	}

}
