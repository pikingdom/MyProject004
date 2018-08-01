package com.nd.hilauncherdev.launcher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.nd.hilauncherdev.app.CustomIntent;
import com.nd.hilauncherdev.datamodel.Global;
import com.nd.hilauncherdev.drawer.data.DrawerDataFactory;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.config.ConfigPreferences;
import com.nd.hilauncherdev.kitset.util.StringUtil;

/**
 * Description:使用间隔分布统计
 * Author: guojianyun_91 
 * Date: 2015年5月15日 下午4:02:35
 */
public class UsingIntervalStatistics {
	
	private static final long ONEDAYMILLIS = 1000 * 60 * 60 * 24;

	public static final String RECOMMEND_FOLDER_KEY = Global.PKG_NAME + ".recommendfolder";
	public static final String SEARCH_KEY = Global.PKG_NAME + ".search";
	public static final String COMPAIGN_KEY = Global.PKG_NAME + ".compaign";
	/**
	 * Description: 打点统计使用间隔
	 * Author: guojianyun_91 
	 * Date: 2015年5月15日 下午4:19:44
	 * @param ctx
	 * @param cn
	 * @param intent
	 */
	public static void stat(Context ctx, ComponentName cn, Intent mIntent){
		stat(ctx, cn, mIntent, null);
	}
	
	/**
	 * Description: 打点统计使用间隔
	 * Author: guojianyun_91 
	 * Date: 2015年6月4日 下午2:14:59
	 * @param ctx
	 * @param cn
	 * @param mIntent
	 * @param key
	 */
	public static void stat(Context ctx, ComponentName cn, Intent mIntent, String key){
		try {
			String compStr1 = null, compStr2 = null;

			if(key != null){
				compStr1 = key;
			}else{
				if (cn == null && mIntent != null) {
					cn = mIntent.getComponent();
				}
				if (cn != null) {
					compStr1 = cn.getClassName();
				}

				if (mIntent != null) {
					Intent intent = (Intent) mIntent.clone();
					intent.setFlags(0);// 移除可能存在的flag
					intent.setSourceBounds(null);
					compStr2 = intent.toUri(0);
				}
			}

			if (compStr1 == null && compStr2 == null)
				return;

			int statId = 0;
			if (compStr2 != null && compStr2.equals(DrawerDataFactory.CUSTOM_AITAOBAO_CLS_NAME)) {// 旧版本的统计“爱淘宝”“天猫精选”
				statId = AnalyticsConstant.DISTRIBUTION_AITAOBAO;
			} else {
				if (compStr1 != null) {
					if (compStr1.equals("com.nd.hilauncherdev.appstore.AppStoreSwitchActivity")) {// 统计“应用商店”
						statId = AnalyticsConstant.DISTRIBUTION_APPSTORE;
					} else if (compStr1.equals("com.nd.android.pandahome2.manage.shop.ThemeShopMainActivity")) {// 统计“个性化”
						statId = AnalyticsConstant.DISTRIBUTION_INDIVIDUAL;
					} else if (compStr1.equals(RECOMMEND_FOLDER_KEY)) {// 统计“推荐文件夹”
						statId = AnalyticsConstant.DISTRIBUTION_RECOMMEND_FOLDER;
					} else if (compStr1.equals(SEARCH_KEY)) {// 统计“搜索”
						statId = AnalyticsConstant.DISTRIBUTION_SEARCH;
					} else if (compStr1.equals(COMPAIGN_KEY)) {// 统计“活动”
						statId = AnalyticsConstant.DISTRIBUTION_COMPAIGN;
					} else if (compStr1.equals(CustomIntent.ACTION_APP_AI_TAOBAO)) {// 统计桌面“爱淘宝””
						statId = AnalyticsConstant.DISTRIBUTION_AITAOBAO;
					}
				}
			}
			
			if (statId == 0)
				return;
			long currentTime = System.currentTimeMillis();
			String statValue = getDistributionDay(currentTime, ConfigPreferences.getInstance().getFirstLaunchTime());
			if(StringUtil.isEmpty(statValue))
				return;
			HiAnalytics.submitEvent(ctx, statId, statValue);
			//Log.e("xxxxxxxxxxxxxxxx" + key, statId + ":" + statValue);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Description:1天以内返回1...大于14天返回14+ 大于20天返回20+
	 * Author: guojianyun_91 
	 * Date: 2015年5月15日 下午5:59:02
	 * @param curTime
	 * @param lastTime
	 * @return
	 */
	private static String getDistributionDay(long curTime, long lastTime){
		if(lastTime <= 0 || curTime < lastTime)
			return null;
		int interval = (int) ((curTime - lastTime) / ONEDAYMILLIS)+1;
		if(interval <= 14){			
			return "" + interval;
		}else if(interval <= 20){
			return "14+";
		}else {
			return "20+";
		}
	}
}
