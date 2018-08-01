package com.nd.hilauncherdev.kitset;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.nd.hilauncherdev.analysis.AppAnalysisConstant;
import com.nd.hilauncherdev.analysis.distribute.AppDistributeService;
import com.nd.hilauncherdev.kitset.Analytics.AnalyticsConstant;
import com.nd.hilauncherdev.kitset.Analytics.HiAnalytics;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
import com.nd.hilauncherdev.net.ThemeHttpCommon;

/**
 * description: 应用分发统计接口<br/>
 * author: dingdj<br/>
 * date: 2014/10/8<br/>
 */
public class AppDistributeUtil {

    public static final String TAG = AppDistributeUtil.class.getSimpleName();

    public static void logAppDisDownloadStart(final Context ctx, final String packName, final int sp) {
        Log.e(TAG, "packName:" + packName + "|sp:" + sp + "|APP_DISTRIBUTE_STEP_START");
        logAppDis(ctx, packName, sp, AppAnalysisConstant.APP_DISTRIBUTE_STEP_START);
    }

    /*public static void logAppDisBrowse(final Context ctx, final String packName, final int sp) {
        Log.e(TAG, "packName:" + packName + "|sp:" + sp + "|APP_DISTRIBUTE_STEP_BROWSE");
        logAppDis(ctx, packName, sp, AppAnalysisConstant.APP_DISTRIBUTE_STEP_BROWSE);
    }*/

    public static void logAppDisDownloadSucc(final Context ctx, final String packName, final int sp) {
        Log.e(TAG, "packName:" + packName + "|sp:" + sp + "|APP_DISTRIBUTE_STEP_DOWNLOAD_SUCCESS");
        logAppDis(ctx, packName, sp, AppAnalysisConstant.APP_DISTRIBUTE_STEP_DOWNLOAD_SUCCESS);
        HiAnalytics.submitEvent(ctx, AnalyticsConstant.APP_DISTRIBUTE_DOWNLOAD_SUC, sp + "");
        AppDistributeService.getInstance().addRecord(packName, sp);

    }

    public static void logAppDisInstallSucc(final Context ctx, final String packName, final int sp) {
        Log.e(TAG, "packName:" + packName + "|sp:" + sp + "|APP_DISTRIBUTE_STEP_INSTALLED_SUCCESS");
        logAppDis(ctx, packName, sp, AppAnalysisConstant.APP_DISTRIBUTE_STEP_INSTALLED_SUCCESS);
        HiAnalytics.submitEvent(ctx, AnalyticsConstant.APP_DISTRIBUTE_INSTALL, sp + "");
    }

    /*public static void logAppDisInstallFail(final Context ctx, final String packName, final int sp) {
        Log.e(TAG, "packName:" + packName + "|sp:" + sp + "|APP_DISTRIBUTE_STEP_INSTALLED_FAILED");
        logAppDis(ctx, packName, sp, AppAnalysisConstant.APP_DISTRIBUTE_STEP_INSTALLED_FAILED);
    }*/

    public static void logAppDisDownloadFail(final Context ctx, final String packName, final int sp, final String downloadUrl, final String netInfo) {
        Log.e(TAG, "packName:" + packName + "|sp:" + sp + "|APP_DISTRIBUTE_STEP_DOWNLOAD_FAILED");
        logAppDisEx(ctx, packName, sp, AppAnalysisConstant.APP_DISTRIBUTE_STEP_DOWNLOAD_FAILED, downloadUrl, netInfo);
        HiAnalytics.submitEvent(ctx, AnalyticsConstant.APP_DISTRIBUTE_DOWNLOAD_FAIL, sp + "");
    }

    public static void logAppDisActive(final Context ctx, final String packName, final int sp) {
        Log.e(TAG, "packName:" + packName + "|sp:" + sp + "|APP_DISTRIBUTE_STEP_ACTIVE");
        logAppDis(ctx, packName, sp, AppAnalysisConstant.APP_DISTRIBUTE_STEP_ACTIVE);
        HiAnalytics.submitEvent(ctx, AnalyticsConstant.APP_DISTRIBUTE_ACTIVE, sp + "");
    }

    private static void logAppDis(final Context ctx, final String packName, final int sp, final int stateStep) {
        if (TelephoneUtil.isNetworkAvailable(ctx) && !StringUtil.isEmpty(packName) &&
                AppDistributePreference.getInstance().isAppDistributeAllow()) {
            ThreadUtil.executeMore(new Runnable() {
                @Override
                public void run() {
                    ThemeHttpCommon.postAppDistributeState(ctx, sp, packName, stateStep, "", "");
                }
            });
        }
    }

    private static void logAppDisEx(final Context ctx, final String packName, final int sp, final int stateStep,
                                    final String downloadUrl, final String netInfo) {
        if (TelephoneUtil.isNetworkAvailable(ctx) && !StringUtil.isEmpty(packName) &&
                AppDistributePreference.getInstance().isAppDistributeAllow()) {
            ThreadUtil.executeMore(new Runnable() {
                @Override
                public void run() {
                    ThemeHttpCommon.postAppDistributeState(ctx, sp, packName, stateStep, downloadUrl, netInfo);
                }
            });
        }
    }

    public static class AppDistributePreference {
        public static final String NAME = "app_distribute_sp";
        private boolean isAppDistributeAllow = true;
        private boolean isSubmitEvent = true;
        private static AppDistributePreference appDistributePreference;
        private SharedPreferences sp;

        /**
         * 用户分发
         */
        private static final String KEY_APP_DISTRIBUTE = "key_app_distribute";

        /**
         * 是否打点
         */
        private static final String KEY_APP_SUBMIT_EVENT = "key_app_submit_event";

        public synchronized static AppDistributePreference getInstance() {
            if (appDistributePreference == null) {
                appDistributePreference = new AppDistributePreference();
            }
            return appDistributePreference;
        }

        private AppDistributePreference() {
            sp = BaseConfig.getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
            isAppDistributeAllow = sp.getBoolean(KEY_APP_DISTRIBUTE, true);
            isSubmitEvent = sp.getBoolean(KEY_APP_SUBMIT_EVENT, true);
        }

        public boolean isAppDistributeAllow() {
            return isAppDistributeAllow;
        }

        public void setAppDistributeAllow(boolean isAppDistributeAllow) {
            this.isAppDistributeAllow = isAppDistributeAllow;
            sp.edit().putBoolean(KEY_APP_DISTRIBUTE, isAppDistributeAllow).commit();
        }

        public boolean isSubmitEvent() {
            return isSubmitEvent;
        }

        public void setSubmitEvent(boolean isSubmitEvent) {
            this.isSubmitEvent = isSubmitEvent;
            sp.edit().putBoolean(KEY_APP_SUBMIT_EVENT, isSubmitEvent).commit();
        }

        public void setSugguestionChannel(String sugguestionChannel){
//        	sp.edit().putString("sugguestion_channel", sugguestionChannel).commit();
        }
        /**百度相关渠道号获取  2015-9-1 V6.8.1修改为映射后的渠道号*/
        public String getSugguestionChannel(){
        	return sp.getString("sugguestion_channel","1006401i");
        }

		public void setSearchChannel(String searchChannel){
//			sp.edit().putString("search_channel", searchChannel).commit();
		}
		public String getSearchChannel(){
			return sp.getString("search_channel","1015708b");
	    }

		public void setHotwordChannel(String hotwordChannel){
//			sp.edit().putString("hotword_channel", hotwordChannel).commit();
		}
		public String getHotwordChannel(){
			return sp.getString("hotword_channel","1015708b");
	    }
    }
}
