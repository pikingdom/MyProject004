package com.nd.hilauncherdev.wifi;

import java.net.InetAddress;
import java.util.List;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.nd.hilauncherdev.wifi.WifiHelper.KeyMgmt;
import com.nd.hilauncherdev.wifi.WifiHelper.PskType;

public class WifiConnect {

	WifiManager wifiManager;

	// 构造函数
	public WifiConnect(WifiManager wifiManager) {
		this.wifiManager = wifiManager;
	}

	// 打开wifi功能
	private boolean OpenWifi() {
		boolean bRet = true;
		if (!wifiManager.isWifiEnabled()) {
			bRet = wifiManager.setWifiEnabled(true);
		}
		return bRet;
	}

	/**
	 * 
	 * @Title: connect
	 * @Description: wifi连接时间长，需在线程中运行
	 * @param wifiBean
	 *            wifi连接点的具体配置信息
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean connect(WifiBean wifiBean) {
		if (!this.OpenWifi()) {
			return false;
		}
		while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		WifiConfiguration wifiConfig = this.CreateWifiInfo(wifiBean);
		if (wifiConfig == null) {
			return false;
		}

		WifiConfiguration tempConfig = this.IsExsits(wifiBean.ssid);
		if (tempConfig != null) {
			wifiManager.removeNetwork(tempConfig.networkId);
		}
		try {
			// "DHCP" for dynamic
			boolean isStatic = wifiBean.isStatic;
			if(Build.VERSION.SDK_INT > 10){
				WifiHelper.setIpAssignment(isStatic ? "STATIC" : "DHCP", wifiConfig);
			}
			if (isStatic) {
				WifiHelper.setIpAddress(InetAddress.getByName(wifiBean.ip), wifiBean.networkPrefix, wifiConfig);
				WifiHelper.setGateway(InetAddress.getByName(wifiBean.gateway), wifiConfig);
				
				InetAddress[] dns = new InetAddress[2];
				dns[0] = InetAddress.getByName(wifiBean.dns1);
				dns[1] = InetAddress.getByName(wifiBean.dns2);
				WifiHelper.setDNS(dns, wifiConfig);
			}
			// wifiManager.updateNetwork(wifiConfig); // apply the setting
		} catch (Exception e) {
			e.printStackTrace();
		}

		int netID = wifiManager.addNetwork(wifiConfig);
		boolean bRet = wifiManager.enableNetwork(netID, true);
		return bRet;
	}

	/**
	 * @Title: connect
	 * @Description: wifi连接时间长，需在线程中运行
	 * @param config
	 *            wifi连接点的具体配置信息
	 * @param @return
	 * @return boolean
	 * @throws
	 */

	public boolean connect(WifiConfiguration config) {
		if (!this.OpenWifi()) {
			return false;
		}
		while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (config == null) {
			return false;
		}
		boolean bRet = wifiManager.enableNetwork(config.networkId, true);
		return bRet;
	}

	// 查看以前是否也配置过这个网络
	public WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (SSID.equals(WifiHelper.dealWithSSID(existingConfig.SSID))) {
				return existingConfig;
			}
		}
		return null;
	}

	private WifiConfiguration CreateWifiInfo(WifiBean wifiBean) {
		String SSID = wifiBean.ssid;
		String Password = wifiBean.passwd;
		PskType type = WifiHelper.getSecurityType(wifiBean.capabilities);
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		if (type == PskType.NOPASS) {
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (type == PskType.WEP) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (type == PskType.WPA) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);// wpa
			if (getProtocolByKeyMgmt(wifiBean.capabilities)) {
				config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);// wpa2
			}
			config.status = WifiConfiguration.Status.ENABLED;
		} else {
			return null;
		}
//		Config ssid="linyt",status=2,networkid=5,
//				allowedAuthAlgorithms={},
//				allowedGroupCiphers={0, 1, 2, 3},
//				allowedKeyManagement={1},
//				allowedPairwiseCiphers={1, 2},
//				allowedProtocols={0, 1}

		// GroupCipher.CCMP
		// GroupCipher.TKIP
		// GroupCipher.WEP104
		// GroupCipher.WEP40
		// ScanResult
		// SSID=ND-BUF-5F-4,capabilities=[WPA2-PSK-TKIP+CCMP][WPS][ESS]
		// ScanResult
		// SSID=ND-BUF-5F-8,capabilities=[WPA2-PSK-TKIP+CCMP][WPS][ESS]

		return config;
	}

	/**
	 * 检测安全协议是否为wpa2,仅适用于检测wpa方式加密
	 * 
	 * @Title: getProtocolByKeyMgmt
	 * @Description: TODO
	 * @param @param capabilities
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	private boolean getProtocolByKeyMgmt(String capabilities) {
		if (null == capabilities || "".equals(capabilities)) {
			return true;
		}
		KeyMgmt keyMgmt = WifiHelper.getKeyMgmt(capabilities);
		return !(KeyMgmt.SECURITY_WPA_PSK == keyMgmt);
	}

	// public String getSecurityString(boolean concise) {
	// Context context = getContext();
	// switch(security) {
	// case SECURITY_EAP:
	// return concise ? context.getString(R.string.wifi_security_short_eap) :
	// context.getString(R.string.wifi_security_eap);
	// case SECURITY_PSK:
	// switch (pskType) {
	// case WPA:
	// return concise ? context.getString(R.string.wifi_security_short_wpa) :
	// context.getString(R.string.wifi_security_wpa);
	// case WPA2:
	// return concise ? context.getString(R.string.wifi_security_short_wpa2) :
	// context.getString(R.string.wifi_security_wpa2);
	// case WPA_WPA2:
	// return concise ? context.getString(R.string.wifi_security_short_wpa_wpa2)
	// :
	// context.getString(R.string.wifi_security_wpa_wpa2);
	// case UNKNOWN:
	// default:
	// return concise ?
	// context.getString(R.string.wifi_security_short_psk_generic)
	// : context.getString(R.string.wifi_security_psk_generic);
	// }
	// case SECURITY_WEP:
	// return concise ? context.getString(R.string.wifi_security_short_wep) :
	// context.getString(R.string.wifi_security_wep);
	// case SECURITY_NONE:
	// default:
	// return concise ? "" : context.getString(R.string.wifi_security_none);
	// }
	// }

}