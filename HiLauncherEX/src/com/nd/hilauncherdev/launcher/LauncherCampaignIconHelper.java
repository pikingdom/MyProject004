package com.nd.hilauncherdev.launcher;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;

import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.DateUtil;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.launcher.info.ApplicationInfo;
import com.nd.hilauncherdev.launcher.model.BaseLauncherModel;
import com.nd.hilauncherdev.launcher.model.BaseLauncherSettings;
import com.nd.hilauncherdev.launcher.screens.ScreenViewGroup;
import com.nd.hilauncherdev.shop.util.ThemeShopV6PreferencesUtils;
import com.nd.hilauncherdev.theme.ThemeOperator;
import com.nd.hilauncherdev.theme.data.SimpleTheme;

/**
 * <br>Description: 桌面活动图标相关中类
 * <br>Author:caizp
 * <br>Date:2015年10月21日下午3:51:16
 */
public class LauncherCampaignIconHelper {
	
	/**
	 * <br>Description: 处理活动桌面图标
	 * <br>Author:caizp
	 * <br>Date:2015年10月21日下午3:55:26
	 * @param context
	 * @param workspaceHelper
	 * @param themeId
	 */
	public static void processCampaignIcon(final Context context, String themeId) {
		try {
			if (null == context || null == Global.getLauncher()) return;
			String campaignIconJson = ThemeShopV6PreferencesUtils.getCampaignIconJson(context);
			if(TextUtils.isEmpty(campaignIconJson)) return;
			JSONObject campaignIconObject = new JSONObject(campaignIconJson);
			String startDate = campaignIconObject.optString("startDate");
			String endDate = campaignIconObject.optString("endDate");
			if(TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) return;
			// 当前时间处于活动时间内
			if(!DateUtil.isBetweenDate(startDate, endDate)) return;
			SimpleTheme simpleTheme = ThemeOperator.getSimpleThemeById(themeId);
			if(null == simpleTheme) return;
			// 主题安装时间处于活动时间内
			if(!DateUtil.isBetweenDate(simpleTheme.installTime, startDate, endDate)) return;
			String action = campaignIconObject.optString("action");
			if(TextUtils.isEmpty(action)) return;
			String iconUrl = campaignIconObject.optString("iconUrl");
			if(TextUtils.isEmpty(iconUrl)) return;
			String matchStr = campaignIconObject.optString("matchStr");
			if(TextUtils.isEmpty(iconUrl)) return;
			// 已存在桌面则不添加
			if(isCampaignIconExist(context, matchStr))return;
			
			int[] psInfo = WorkspaceHelper.getVacantCellFromBottom();
			// 找不到空白位置
			if (psInfo == null) return;
			final ApplicationInfo info = new ApplicationInfo();
			info.title = campaignIconObject.optString("title");
			info.itemType = BaseLauncherSettings.Favorites.ITEM_TYPE_SHORTCUT;
			info.intent = Intent.parseUri(action, 0);
			info.customIcon = true;
			info.iconResource = new Intent.ShortcutIconResource();
			info.iconResource.packageName = iconUrl;
			String iconPath = com.nd.hilauncherdev.shop.Global.CACHES_HOME_MARKET + FileUtil.getFileName(iconUrl, true);
			info.iconBitmap = BitmapFactory.decodeFile(iconPath);
			if (info.iconBitmap == null)
				return;
			info.container = BaseLauncherSettings.Favorites.CONTAINER_DESKTOP;
			info.screen = psInfo[0];
			info.cellX = psInfo[1];
			info.cellY = psInfo[2];
			info.spanX = 1;
			info.spanY = 1;
			BaseLauncherModel.addItemToDatabase(BaseConfig.getApplicationContext(), info, false);
			if(null == BaseConfig.getBaseLauncher()) return;
			final ScreenViewGroup wk = BaseConfig.getBaseLauncher().getScreenViewGroup();
			wk.post(new Runnable() {
				@Override
				public void run() {
					View view = BaseConfig.getBaseLauncher().createCommonAppView((ApplicationInfo) info);
					wk.addInScreen(view, info.screen, info.cellX, info.cellY, info.spanX, info.spanY);
					wk.getCellLayoutAt(info.screen).invalidate();
					HiAnalytics.submitEvent(context, AnalyticsConstant.LAUNCHER_CAMPAIGN_ICON, "tj");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 活动图标是否已存在桌面
	 * @param context
	 * @param intent
	 * @return
	 */
	private static boolean isCampaignIconExist(Context context, String matchStr) {
		if(TextUtils.isEmpty(matchStr)) return true;
		Cursor c = null;
		try {
			ContentResolver cr = context.getContentResolver();
			c = cr.query(LauncherSettings.Favorites.getContentUri(),null,"intent like ? or intent like ?",
					new String[]{"%"+matchStr+"%", "%"+URLEncoder.encode(matchStr, "ISO8859-1")+"%"},null);
			if(c.getCount() > 0) return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (null != c) {
				c.close();
			}
		}
		return false;
	}
	
}
