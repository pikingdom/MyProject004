package com.nd.hilauncherdev.kitset.Analytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.nd.analytics.CustomParamBuilder;
import com.nd.analytics.NdAnalytics;
import com.nd.analytics.NdAnalyticsSettings;
import com.nd.android.pandahome2.R;
import com.nd.hilauncherdev.kitset.util.ChannelUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.launcher.config.BaseConfig;
/**
 * 功能统计封装类
 */
public class HiAnalytics {
	private static int init = -1;
	private static final String TAG = "HiAnalytics";
	/*
	 * 用户统计数据上传后的广播
	 */
	public static final String START_UP_ACTION = "com.nd.analytics.startup";
	public static final String START_UP_EXTRA = "startup_result";

	/**
	 *区别分支版本升级统计key
	 */
	public static final String CONST_STRING_KEY_ANALYTICS_WAY = "key_analytics_way";
	
	/**
	 * 产品ID,暂用熊猫2.7的id
	 */
	public static final int AppId = BaseConfig.APPID;

	/**
	 * 产品Key,暂用熊猫2.7的key
	 */
	public static final String AppKey = BaseConfig.APPKEY;

    private static String CUID = null;


	/**
	 * 统计分析初始化
	 * @param context
	 *            不能使用Application Context
	 */
	public static void init(Context context) {
		if (init != -1)
			return;
		
		// 初始化数据分析
		NdAnalyticsSettings settings = new NdAnalyticsSettings();
		settings.setAppId(AppId);
		settings.setAppKey(AppKey);
		Log.e("HiAnalytics", "AppId:" + AppId +"   AppKey:" + AppKey);

		/**
		 * 查看是否分支版本升级，开关是否打开
		 */
		ifUpdateFromBranch(context);
		/**
		 * 是否安装为ROM
		 */
		if (ChannelUtil.getIsCustomByType(ChannelUtil.CUSTOM_ROM)){
			setCustomChannelID(context);
		}
		
		NdAnalytics.setReportStartupOnlyOnceADay(true);
		NdAnalytics.initialize(context, settings);
		init = 1;
		Log.e(TAG, "=============================HiAnalytics.init=============================");
	}
	
	/**
	 * 
	 * 如果91桌面安装在系统应用里面
	 * 更改统计的渠道ID
	 * 
	 * @author HCF,2014.5.22
	 */
	private static void setCustomChannelID(Context context){
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			//91桌面刷到系统应用里面
			if ((info.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {			
				String channelid = context.getString(R.string.rom_app_chl);
				if (channelid!=null && !"".equals(channelid)){
					CustomParamBuilder paramBuilder = new CustomParamBuilder();
					paramBuilder.setChannel(channelid);
					NdAnalytics.setCustomParamBuilder(paramBuilder);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查看是否是分支版本 升级到主版本
	 * 分支版本升级上来的统计方式不变，继续用旧版本统计方式
	 * TelephoneUtil.switchPrefix 为 true
	 * @param context
	 */
	private static void ifUpdateFromBranch(Context context) {
		SharedPreferences sp = context.getSharedPreferences("configsp", Context.MODE_PRIVATE);
		String valueAnalyticsWay = sp.getString(CONST_STRING_KEY_ANALYTICS_WAY, "");

		if (StringUtil.isAnyEmpty(valueAnalyticsWay)) {
			if (TelephoneUtil.switchPrefix) {
				valueAnalyticsWay = TelephoneUtil.CONST_STRING_PREFIX_MAC;
			} else {
				valueAnalyticsWay = TelephoneUtil.CONST_STRING_PREFIX_IMEI;
			}
			sp.edit().putString(CONST_STRING_KEY_ANALYTICS_WAY, valueAnalyticsWay).commit();
		} else {
			if (valueAnalyticsWay.equals(TelephoneUtil.CONST_STRING_PREFIX_MAC)) {
				TelephoneUtil.switchPrefix = true;
			} else {
				TelephoneUtil.switchPrefix = false;
			}
		}
		
		CustomParamBuilder paramBuilder = new CustomParamBuilder();
		
		if (TelephoneUtil.switchPrefix) {
			paramBuilder.setModel(TelephoneUtil.getMachineName());
		}
		
		/**
		 * 解决某些定制版,在升级到主版本后,不采用主版本中的渠道标识进行相关统计,
		 * 而是采用原先定制版本中所配置的渠道标识
		 * 
		 * @author wangguomei,2013.11.22
		 **/
		if (ChannelUtil.getIsCustomByType(ChannelUtil.CUSTOM_SEPECIALCHANNEL)) {
	        if (!sp.contains(ChannelUtil.sepecialChannelPackageIdSpKey)) {
	        	sp.edit().putString(ChannelUtil.sepecialChannelPackageIdSpKey, 
					ChannelUtil.getChannelOnlyForSpecialLauncherDoAnalyticInit(context)).commit();
	        }
		}
		String sepecialChannelPackageId = sp.getString(ChannelUtil.sepecialChannelPackageIdSpKey, "");
		if (!StringUtil.isEmpty(sepecialChannelPackageId)) {
			paramBuilder.setChannel(sepecialChannelPackageId);
		}
		
		NdAnalytics.setCustomParamBuilder(paramBuilder);

	}

	
	public static void submitEvent(Context context, int eventId, String label) {
		NdAnalytics.onEvent(context, eventId, label);
	}
	
	
	public static void submitEvent(Context context, int eventId) {
		submitEvent(context, eventId, "");
	}
	
	public static void startUp(Context ctx) {
		NdAnalytics.startup(ctx);
		Log.e(TAG, "=============================HiAnalytics.startUp=============================");
	}
	
	public static String getChannel(Context ctx) {
		return NdAnalytics.getChannel(ctx);
	}

    public static String getCUID(Context ctx) {
        try {
            if(StringUtil.isEmpty(CUID)) {
                CUID = NdAnalytics.getCUID(ctx);
                Log.e(TAG, "CUID--------------------------:"+CUID);
            }
            return CUID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
