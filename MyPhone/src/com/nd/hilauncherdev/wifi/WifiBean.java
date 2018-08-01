package com.nd.hilauncherdev.wifi;

import com.nd.hilauncherdev.wifi.WifiHelper.PskType;

public class WifiBean implements Comparable<WifiBean> {

	public static final int TYPE_WIFICONFIGURATION = 0;
	public static final int TYPE_SCANRESULT = 1;

	public String ssid;
	public String passwd;
	public boolean isStatic;
	public PskType pskType;
	public String ip;
	/**
	 * 网络前缀长度 4.x使用
	 */
	public int networkPrefix;
	/**
	 * 子网掩码2.x使用
	 */
//	private String netmask;
	public String gateway;
	public String dns1;
	public String dns2;
	public int type;

	// private ScanResult result;
	public int level = -100;
	public String capabilities;

	// private WifiConfiguration configuration;
	public int networkId = -1;

	public boolean isCurrentUsed = false;

	@Override
	public int compareTo(WifiBean another) {
		if (this.isCurrentUsed) {
			return -1;
		}
		if (another.isCurrentUsed) {
			return 1;
		}

		if (this.type > another.type) {
			return -1;
		} else if (this.type < another.type) {
			return 1;
		} else {
			if (this.networkId > another.networkId) {
				return -1;
			} else if (this.networkId < another.networkId) {
				return 1;
			} else {
				if (this.level > another.level) {
					return -1;
				} else if (this.level < another.level) {
					return 1;
				} else {
					return this.ssid.compareTo(another.ssid);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "WifiBean [ssid=" + ssid + ", passwd=" + passwd + ", isStatic=" + isStatic + ", pskType=" + pskType + ", ip=" + ip + ", gateway=" + gateway + ", dns1=" + dns1 + ", dns2=" + dns2 + ", type=" + type + ", level=" + level + ", capabilities=" + capabilities + ", networkId=" + networkId + ", isCurrentUsed=" + isCurrentUsed + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()) {
			return false;
		}
		WifiBean other = (WifiBean) obj;
		if (ssid == null) {
			if (other.ssid != null) {
				return false;
			}
		} else if (!ssid.equals(other.ssid)) {
			return false;
		} else if (ssid.equals(other.ssid)) {
			if (level != other.level) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ssid == null) ? 0 : ssid.hashCode());
		return result;
	}
}
