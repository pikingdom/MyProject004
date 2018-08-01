package com.nd.hilauncherdev.wifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.nd.android.pandahome2.R;

/**
 * 
 * @ClassName: WifiUtil
 * @Description: 获取连接点信息、扫描周围热点信息、开关wifi、连接热点等功能
 * @author linyt
 * @date 2013-3-7 下午5:32:22
 * 
 */
public class WifiUtil {
	private static WifiUtil mWifiUtil;
	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	private List<WifiBean> wifiList;
	private List<WifiConfiguration> mWifiConfigurations;
	// 在范围内
	public static final String SCAN_LIST = "scan";
	// 不在范围内
	public static final String NO_IN_SCAPE_LIST = "notinscape";

	public enum WIFI_RSSI {
		RSSI_PERFECT {
			public String getName(Context ctx) {
				return ctx.getString(R.string.wifi_rssi_level_perfect);
			}

			public int getIconRes(Context ctx) {
				return R.drawable.wifi_rssi_level_perfect;
			}
		}, // 强
		RSSI_GOOD {
			public String getName(Context ctx) {
				return ctx.getString(R.string.wifi_rssi_level_good);
			}

			public int getIconRes(Context ctx) {
				return R.drawable.wifi_rssi_level_good;
			}
		}, // 较强
		RSSI_NORMAL {
			public String getName(Context ctx) {
				return ctx.getString(R.string.wifi_rssi_level_normal);
			}

			public int getIconRes(Context ctx) {
				return R.drawable.wifi_rssi_level_normal;
			}
		}, // 一般
		RSSI_BAD {
			public String getName(Context ctx) {
				return ctx.getString(R.string.wifi_rssi_level_bad);
			}

			public int getIconRes(Context ctx) {
				return R.drawable.wifi_rssi_level_bad;
			}
		}, // 弱
		RSSI_NOTHING {
			public String getName(Context ctx) {
				return ctx.getString(R.string.wifi_rssi_level_nothing);
			}

			public int getIconRes(Context ctx) {
				return R.drawable.wifi_rssi_level_nothing;
			}
		};// 无信号

		public abstract String getName(Context ctx);

		public abstract int getIconRes(Context ctx);
	}

	public static WifiUtil getInstance(Context context) {
		if (null == mWifiUtil) {
			mWifiUtil = new WifiUtil(context);
		}
		return mWifiUtil;
	}

	private WifiUtil(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	// 打开wifi
	public void openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	// 关闭wifi
	public void closeWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	public int checkState() {
		return mWifiManager.getWifiState();
	}

	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfigurations;
	}

	/**
	 * 
	 * @Title: startScan
	 * @Description: 扫描热点信息
	 * @param
	 * @return void
	 * @throws
	 */
	public void startScan() {
		//在2.3.3上可能报出 SecurityException, requires android.permission.BROADCAST_STICKY
		try {
			mWifiInfo = mWifiManager.getConnectionInfo();
		} catch (Exception e) {
		}
		mWifiConfigurations = mWifiManager.getConfiguredNetworks();
		mWifiManager.startScan();
	}

	public void dualWithScanResult() {
		String current_ssid = WifiHelper.dealWithSSID(getSSID());
		int rssi = getIntRssi();
		wifiList = new ArrayList<WifiBean>();
		/**
		 * 扫描到的热点
		 */
		List<ScanResult> scanlist = mWifiManager.getScanResults();
		if (scanlist == null) {
			return;
		}
		WifiBean bean = null;
		ScanResult scanResult = null;
		Map<String, WifiBean> tmpMap = new HashMap<String, WifiBean>();
		for (Iterator<ScanResult> iterator = scanlist.iterator(); iterator.hasNext();) {
			scanResult = iterator.next();
			if (tmpMap.containsKey(scanResult.SSID)) {
				continue;
			}
			bean = new WifiBean();
			String ssid = WifiHelper.dealWithSSID(scanResult.SSID);
			bean.ssid = ssid;
			if (ssid.equals(current_ssid)) {
				bean.isCurrentUsed = true;
				bean.level = rssi;
			} else {
				bean.level = scanResult.level;
			}
			bean.type = WifiBean.TYPE_SCANRESULT;
			bean.capabilities = scanResult.capabilities;
			tmpMap.put(scanResult.SSID, bean);
			wifiList.add(bean);
		}
		/**
		 * 不在范围的热点
		 */
		if (mWifiConfigurations != null) {
			for (Iterator<WifiConfiguration> iterator = mWifiConfigurations.iterator(); iterator.hasNext();) {
				WifiConfiguration configuaration = iterator.next();
				String ssid = WifiHelper.dealWithSSID(configuaration.SSID);
				// Log.e("asd", "configuaration =" + configuaration);
				if (tmpMap.containsKey(ssid)) {
					bean = tmpMap.get(ssid);
					bean.networkId = configuaration.networkId;
				} else {
					// 不在范围的热点
					bean = new WifiBean();
					bean.ssid = ssid;
					bean.type = WifiBean.TYPE_WIFICONFIGURATION;
					bean.networkId = configuaration.networkId;
					wifiList.add(bean);
				}
			}
		}

		try {
			Collections.sort(wifiList);
		} catch (Exception e) {
			//比较的时候会报比较器异常错误，如果有异常，就不执行排序。
			//后果就是wifi列表的显示顺序是乱的。但是不至于导致桌面崩溃。
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: getWifiData
	 * @Description: 返回扫描到的热点信息（包括在范围的热点、不在范围的热点，按可连接性带排序）
	 * @param @return
	 * @return List<WifiBean>
	 * @throws
	 */
	public List<WifiBean> getWifiData() {
		return wifiList;
	}

	// public StringBuffer lookUpScan() {
	// StringBuffer sb = new StringBuffer();
	// for (int i = 0; i < mWifiList.size(); i++) {
	// sb.append("Index_" + new Integer(i + 1).toString() + ":");
	// sb.append((mWifiList.get(i)).toString()).append("\n");
	// }
	// return sb;
	// }

	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	public String getSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
	}

	public int getIpAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	public String getLinkSpeed() {
		return (mWifiInfo == null) ? "" : mWifiInfo.getLinkSpeed() + WifiInfo.LINK_SPEED_UNITS;
	}

	public WIFI_RSSI getRssi() {
		int RSSI = mWifiInfo.getRssi();
		return getRssi(RSSI);
	}

	public WIFI_RSSI getRssi(int RSSI) {
		if (RSSI < -95) {
			return WIFI_RSSI.RSSI_NOTHING;
		} else if (RSSI >= -95 && RSSI < -90) {
			return WIFI_RSSI.RSSI_BAD;
		} else if (RSSI >= -90 && RSSI <= -70) {
			return WIFI_RSSI.RSSI_NORMAL;
		} else if (RSSI >= -70 && RSSI <= -50) {
			return WIFI_RSSI.RSSI_GOOD;
		} else {
			return WIFI_RSSI.RSSI_PERFECT;
		}
	}

	public int getIntRssi() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getRssi();
	}

	public int getNetWordId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	// public void disConnectionWifi(int netId) {
	// mWifiManager.disableNetwork(netId);
	// mWifiManager.disconnect();
	// }

}
