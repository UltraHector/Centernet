package com.TroyEmpire.Centernet.Util;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;

import com.TroyEmpire.Centernet.Constant.ConfigConstant;
import com.TroyEmpire.Centernet.Constant.PskType;

public class AndroidWifiUtil {
	

	
	/**
	 * get the security level for a wificonfiguration
	 * the level value are mapped in ConfigConstant
	 * @param config
	 */
	public static int getSecurity(WifiConfiguration config) {
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
			return ConfigConstant.SECURITY_PSK;
		}
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP)
				|| config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
			return ConfigConstant.SECURITY_EAP;
		}
		return (config.wepKeys[0] != null) ? ConfigConstant.SECURITY_WEP
				: ConfigConstant.SECURITY_NONE;
	}

	/**
	 * get the security level for a scan result
	 * the level value are mapped in ConfigConstant
	 * @param result
	 */
	public static int getSecurity(ScanResult result) {
		if (result.capabilities.contains("WEP")) {
			return ConfigConstant.SECURITY_WEP;
		} else if (result.capabilities.contains("PSK")) {
			return ConfigConstant.SECURITY_PSK;
		} else if (result.capabilities.contains("EAP")) {
			return ConfigConstant.SECURITY_EAP;
		}
		return ConfigConstant.SECURITY_NONE;
	}
	
	/**
	 * Generate and save a default wifiConfiguration with common values. Can
	 * only be called for unsecured networks.
	 */
	public static WifiConfiguration generateOpenNetworkConfig(ScanResult result) {
		WifiConfiguration mConfig = new WifiConfiguration();
		if (AndroidWifiUtil.getSecurity(result) != ConfigConstant.SECURITY_NONE)
			throw new IllegalStateException();
		mConfig.SSID = convertToQuotedString(result.SSID);
		mConfig.BSSID = result.BSSID;
		mConfig.allowedKeyManagement.set(KeyMgmt.NONE);
		return mConfig;
	}
	
	
	/**
	 * remove the double quote at the beginning and end of a string
	 */
	public static String removeDoubleQuotes(String string) {
		int length = string.length();
		if ((length > 1) && (string.charAt(0) == '"')
				&& (string.charAt(length - 1) == '"')) {
			return string.substring(1, length - 1);
		}
		return string;
	}
	/**
	 * add the double quote at the beginning and end of a string
	 */
	public static String convertToQuotedString(String string) {
		return "\"" + string + "\"";
	}
	
	/**
	 * get the psk type for a result
	 * @param result
	 */
	public static PskType getPskType(ScanResult result) {
		boolean wpa = result.capabilities.contains("WPA-PSK");
		boolean wpa2 = result.capabilities.contains("WPA2-PSK");
		if (wpa2 && wpa) {
			return PskType.WPA_WPA2;
		} else if (wpa2) {
			return PskType.WPA2;
		} else if (wpa) {
			return PskType.WPA;
		} else {
			return PskType.UNKNOWN;
		}
	}

}
