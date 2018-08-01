package com.nd.hilauncherdev.kitset.Analytics;

import android.content.Context;

import com.nd.hilauncherdev.kitset.AppDistributeUtil;

/**
 * 可控统计工具类
 * <p>
 * Title: ThemeShopAnalyticsUtil
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: ND
 * </p>
 * 
 * @author MaoLinnan
 * @date 2014年11月17日
 */
public class ControlledAnalyticsUtil {
	/**
	 * 添加UED打点统计信息
	 * <p>
	 * Title: addCommonAnalytics
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param analyticsId
	 * @param label
	 *            为null就直接统计id
	 * @author maolinnan_350804
	 */
	public static void addCommonAnalytics(Context context, int analyticsId, String label) {
		if (context == null || "".equals(label) || label == null) {
			return;
		}
		if (AppDistributeUtil.AppDistributePreference.getInstance().isSubmitEvent()) {
			HiAnalytics.submitEvent(context, analyticsId, label);
		}
	}

	public static void addCommonAnalytics(Context context, int analyticsId) {
		if (context == null) {
			return;
		}
		if (AppDistributeUtil.AppDistributePreference.getInstance().isSubmitEvent()) {
			HiAnalytics.submitEvent(context, analyticsId);
		}
	}
}
