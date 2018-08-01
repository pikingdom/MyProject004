package com.nd.hilauncherdev.webconnect.downloadmanage;

import java.net.URLEncoder;

import android.content.Context;

import com.nd.hilauncherdev.datamodel.CommonGlobal;
import com.nd.hilauncherdev.kitset.util.CUIDUtil;
import com.nd.hilauncherdev.kitset.util.ChannelUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.url.URLs;

/**
 * 统一下载地址管理
 * <p>
 * Title: DownloadUrlManage
 * Description:
 * Company: ND
 * @author MaoLinnan
 * @date 2014-6-24
 */
public class DownloadUrlManage {


    /**
	 * MobileType 1代表IPHONE 2代表WINDOWSMOBILE 3代表 SYMBIAN S60 4代表ANDROID 5代表JAVA
	 * 6代表MTK
	 */
	public static final String MT = "4";
	/** 熊猫桌面项目编号 */
	public static final String PROJECT_OPTION = "1900";

    /**
	 * 通过包名获取统一下载app地址
	 * Title: getDownloadUrlFromPackageName
	 * Description:
	 * @param context
	 * @param packageName
	 * @param sp 在当前这个类里有静态变量，请使用里定义的sp变量
	 * @param sessionID 能获取到就传
	 *            ，获取不到就传null
	 * 建议下载目录使用BaseConfig.WIFI_DOWNLOAD_PATH目录
	 * @return
	 * @author maolinnan_350804
	 */
	public static String getDownloadUrlFromPackageName(Context context, String packageName, String sessionID,int sp) {
		if (context == null || packageName == null || "".equals(packageName)) {
			return null;
		}
		String downloadAddress = String.format(URLs.UNIFIED_DOWNLOAD_PATH, packageName,sp);
		StringBuffer result = new StringBuffer(downloadAddress);
		// 拼接通用统计参数
		addGlobalRequestValue(context, result, sessionID);
		return result.toString();
	}

	/**
	 * 添加全局参数
	 * Title: addGlobalRequestValue
	 * Description:
	 * @param context
	 * @param sb
	 * @param sessionID
	 *            能获取到就传，获取不到就传null
	 * @author maolinnan_350804
	 */
	public static void addGlobalRequestValue(Context context, StringBuffer sb, String sessionID) {
		if (sb == null || context == null)
			return;

		String imsiNumber = TelephoneUtil.getIMSI(context);
		if (null == imsiNumber) {
			imsiNumber = "";
		}
		String imeiNumber = TelephoneUtil.getIMEI(context);
		if (null == imeiNumber) {
			imeiNumber = "";
		}
		appendAttrValue(sb, "mt", MT);
		appendAttrValue(sb, "tfv", "40000");
//		appendAttrValue(sb, "pid", CommonGlobal.PID);
		appendAttrValue(sb, "pid", "6");
		appendAttrValue(sb, "imei", imeiNumber);
		appendAttrValue(sb, "imsi", imsiNumber);
		appendAttrValue(sb, "projectoption", PROJECT_OPTION);
		appendAttrValue(sb, "DivideVersion", TelephoneUtil.getVersionName(context, context.getPackageName()));
		appendAttrValue(sb, "SupPhone", encodeAttrValue(TelephoneUtil.getMachineName())); // 型号
		appendAttrValue(sb, "supfirm", TelephoneUtil.getFirmWareVersion()); // Android版本号
		appendAttrValue(sb, "company", encodeAttrValue(TelephoneUtil.getManufacturer())); // 制造商
		appendAttrValue(sb, "nt", TelephoneUtil.getNT(context)); // 网络类型
		appendAttrValue(sb, "chl", ChannelUtil.getChannel(context)); // 渠道ID
        appendAttrValue(sb, "CUID", encodeAttrValue(CUIDUtil.getCUID())); //加入CUID
		String isRoot = TelephoneUtil.hasRootPermission() ? "1" : "0"; // 是否root
		appendAttrValue(sb, "JailBroken", isRoot); // 渠道ID
		if (sessionID != null && !"".equals(sessionID)) {
			appendAttrValue(sb, "sessionid", sessionID);
		}
	}

	/**
	 * 拼接参数
	 * <p>
	 * Title: appendAttrValue
	 * Description:
	 * @param sb
	 * @param key
	 * @param values
	 * @author maolinnan_350804
	 */
	private static void appendAttrValue(StringBuffer sb, String key, String... values) {
		if (sb.indexOf("?" + key + "=") != -1 || sb.indexOf("&" + key + "=") != -1) {
			return;
		}
		for (String value : values) {
			if (sb.indexOf("?") == -1) {
				sb.append("?");
			} else {
				sb.append("&");
			}
			sb.append(key);
			sb.append("=");
			sb.append(value);
		}
	}
	/**
	 * 对参数进行转码
	 * <p>Title: encodeAttrValue</p>
	 * <p>Description: </p>
	 * @param value
	 * @return
	 * @author maolinnan_350804
	 */
	private static String encodeAttrValue(String value){
		String returnValue = "";
		try {
			value = URLEncoder.encode(value+"", "UTF-8");
			returnValue = value.replaceAll("\\+", "%20");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return returnValue;
	}
}
