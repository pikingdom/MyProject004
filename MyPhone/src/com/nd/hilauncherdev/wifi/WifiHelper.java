package com.nd.hilauncherdev.wifi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiConfiguration;

import com.nd.android.pandahome2.R;

public class WifiHelper {

	public enum KeyMgmt {
		// 定义几种加密方式
		SECURITY_NONE, SECURITY_WPA_PSK, SECURITY_WEP, SECURITY_WPA2_PSK, SECURITY_WPA_WPA2_PSK
	}

	public enum PskType {
		WEP, WPA, NOPASS
	}

	/**
	 * 获取加密方式,用于设置
	 * 
	 * @Title: getPskType
	 * @Description: TODO
	 * @param @param capabilities
	 * @param @return
	 * @return PskType
	 * @throws
	 */
	public static PskType getSecurityType(String capabilities) {
		if (capabilities.contains("WEP")) {
			return PskType.WEP;
		} else if (capabilities.contains("PSK")) {
			return PskType.WPA;
		}
		return PskType.NOPASS;
	}

	/**
	 * 获取加密方式,用于显示
	 * 
	 * @Title: getKeyMgmt
	 * @Description: TODO
	 * @param @param capabilities
	 * @param @return
	 * @return KeyMgmt
	 * @throws
	 */
	public static KeyMgmt getKeyMgmt(String capabilities) {
		boolean wpa = capabilities.contains("WPA-PSK");
		boolean wpa2 = capabilities.contains("WPA2-PSK");
		boolean wep = capabilities.contains("WEP");
		if (wpa2 && wpa) {
			return KeyMgmt.SECURITY_WPA_WPA2_PSK;
		} else if (wpa2) {
			return KeyMgmt.SECURITY_WPA2_PSK;
		} else if (wpa) {
			return KeyMgmt.SECURITY_WPA_PSK;
		} else if (wep) {
			return KeyMgmt.SECURITY_WEP;
		} else {
			return KeyMgmt.SECURITY_NONE;
		}
	}

	// public static ConnectType getSecurity(WifiConfiguration config) {
	// if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
	// return ConnectType.WPA;
	// }
	// if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) ||
	// config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
	// return KeyMgmt.SECURITY_EAP;
	// }
	// return (config.wepKeys[0] != null) ? KeyMgmt.SECURITY_WEP :
	// KeyMgmt.SECURITY_NONE;
	// }

	public static void setIpAssignment(String assign, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		setEnumField(wifiConf, assign, "ipAssignment");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setIpAddress(InetAddress addr, int prefixLength, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InstantiationException, InvocationTargetException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;
		Class laClass = Class.forName("android.net.LinkAddress");
		Constructor laConstructor = laClass.getConstructor(new Class[] { InetAddress.class, int.class });
		Object linkAddress = laConstructor.newInstance(addr, prefixLength);

		ArrayList<Object> mLinkAddresses = (ArrayList) getDeclaredField(linkProperties, "mLinkAddresses");
		mLinkAddresses.clear();
		mLinkAddresses.add(linkAddress);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setGateway(InetAddress gateway, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;
		Class routeInfoClass = Class.forName("android.net.RouteInfo");
		Constructor routeInfoConstructor = routeInfoClass.getConstructor(new Class[] { InetAddress.class });
		Object routeInfo = routeInfoConstructor.newInstance(gateway);

		ArrayList mRoutes = (ArrayList) getDeclaredField(linkProperties, "mRoutes");
		mRoutes.clear();
		mRoutes.add(routeInfo);
	}

	@SuppressWarnings("unchecked")
	public static void setDNS(InetAddress[] dns, WifiConfiguration wifiConf) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;

		ArrayList<InetAddress> mDnses = (ArrayList<InetAddress>) getDeclaredField(linkProperties, "mDnses");
		mDnses.clear();
		for (InetAddress inetAddress : dns) {
			mDnses.add(inetAddress);
		}
	}

	public static Object getField(Object obj, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getField(name);
		Object out = f.get(obj);
		return out;
	}

	public static Object getDeclaredField(Object obj, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getDeclaredField(name);
		f.setAccessible(true);
		Object out = f.get(obj);
		return out;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setEnumField(Object obj, String value, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getField(name);
		f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
	}

	/**
	 * 去除ssid首尾的引号
	 * 
	 * @param ssid
	 * @return
	 */
	public static String dealWithSSID(String ssid) {
		if (null == ssid || "".equals("ssid")) {
			return "";
		}
		if (ssid.startsWith("\"")) {
			ssid = ssid.substring(1);
		}
		if (ssid.endsWith("\"")) {
			ssid = ssid.substring(0, ssid.length() - 1);
		}
		return ssid;
	}

	public static String changeName(Context ctx, KeyMgmt type) {
		String name = "";
		switch (type) {
		case SECURITY_NONE:
			name = ctx.getString(R.string.wifi_keymgmt_none);
			break;
		case SECURITY_WPA_PSK:
			name = ctx.getString(R.string.wifi_keymgmt_wpa);
			break;
		case SECURITY_WEP:
			name = ctx.getString(R.string.wifi_keymgmt_wep);
			break;
		case SECURITY_WPA2_PSK:
			name = ctx.getString(R.string.wifi_keymgmt_wpa2);
			break;
		case SECURITY_WPA_WPA2_PSK:
			name = ctx.getString(R.string.wifi_keymgmt_wpa_wpa2);
			break;
		default:
			break;
		}
		return name;
	}

	/**
	 * @Title: intIpToStringIp
	 * @Description: 将ip数字转换成可读的ip字符串
	 * @param @param ip
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String intIpToStringIp(int ip) {
		if (ip == 0)
			return null;
		return ((ip & 0xff) + "." + (ip >> 8 & 0xff) + "." + (ip >> 16 & 0xff) + "." + (ip >> 24 & 0xff));
	}

	// 点击正在使用的连接
	// 返回 不保存 断开

	// 点击不在使用范围的连接
	// 返回 不保存 修改

	// 点击不在使用的连接，但是可用的
	// 返回 不保存 连接
	public static final int IN_USE_POINT_CLICK = 0;
	public static final int NOT_IN_SCAPE_POINT_CLICK = 1;
	public static final int NOT_IN_USE_BUT_SAVED_POINT_CLICK = 2;

	public static void showActionDialog(final Context ctx, int type, int iconId, String title, String msg, String first_tag, OnClickListener first_listen, String second_tag, OnClickListener second_listen, String third_tag, OnClickListener third_listen) {
		Builder builder = new AlertDialog.Builder(ctx).setIcon(iconId).setTitle(title).setMessage(msg).setPositiveButton(first_tag, first_listen).setNegativeButton(second_tag, second_listen);
		if (third_listen != null && !"".equals(third_tag)) {
			builder.setNeutralButton(third_tag, third_listen);
		}
		Dialog dialog = builder.create();
		dialog.show();
	}

	public static void showActionDialog(final Context ctx, int type, int iconId, String title, String msg, String first_tag, OnClickListener first_listen, String second_tag, OnClickListener second_listen) {
		showActionDialog(ctx, type, iconId, title, msg, first_tag, first_listen, second_tag, second_listen, "", null);
	}

	public static boolean isValidIp(String ip) {
		if (null == ip || "".equals(ip)) {
			return false;
		}
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

}
