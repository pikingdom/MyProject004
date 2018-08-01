package com.nd.hilauncherdev.analysis;

import android.content.Context;

import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;

/**
 * APP分发转化率统计和搜索转化率统计
 * <p>Title: AppDistributeAndSearchStatistics</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 * @author    MaoLinnan
 * @date       2015年3月5日
 */
public class AppDistributeStatistics {
	/**
	 * 检测app分发产品端的转化率
	 * label列表请查阅相关文档
	 * <p>Title: AppDistributePercentConversionStatistics</p>
	 * <p>Description: </p>
	 * @param label
	 * @author maolinnan_350804
	 */
	public static void AppDistributePercentConversionStatistics(Context context,String label){
		if(context == null || label == null || "".equals(label)){
			return;
		}
		HiAnalytics.submitEvent(context, AnalyticsConstant.APP_DISTRIBUTE_PERCENT_CONVERSION_STATISTICS, label);
	}
}
