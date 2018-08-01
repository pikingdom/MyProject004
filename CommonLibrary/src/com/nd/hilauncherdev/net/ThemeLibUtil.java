package com.nd.hilauncherdev.net;

import java.lang.reflect.Method;
import java.util.Locale;

import com.nd.analytics.NdAnalytics;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

public class ThemeLibUtil {

	/**
	 * 取得IMEI号
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getIMEI(Context ctx) {
		if (ctx == null)
			return "91";

		String imei = "91";
		try {
			TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			imei = tm.getDeviceId();
			if (imei == null || "".equals(imei))
				return "91";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return imei;
	}

	public static String getIMSI(Context ctx) {
		if (ctx == null)
			return "91";

		String imsi = "91";
		try {
			TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			imsi = tm.getSubscriberId();
			if (imsi == null || "".equals(imsi))
				return "91";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return imsi;
	}

	/**
	 * 通过反射的方法，获取CUID
	 * 
	 * @param ctx
	 */
	public static String getCUID(Context ctx) {
		if (null == ctx)
			return "";

		return NdAnalytics.getCUID(ctx);
	}

	/**
	 * 获取versionName
	 * 
	 * @param context
	 * @param packageName
	 * @return String
	 */
	public static String getDivideVersion(Context context) {
		String versionName = "";
		try {
			PackageInfo packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			versionName = packageinfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	
	public static boolean isZh(Context context){
		Locale lo;
		if( null == context ) {
			return true;
		} else {
			lo = context.getResources().getConfiguration().locale;
		}
		if (lo.getLanguage().equals("zh")) 
			return true;
		return false;
	}
	
}
