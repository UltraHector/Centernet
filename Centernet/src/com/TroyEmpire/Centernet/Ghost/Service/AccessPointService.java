package com.TroyEmpire.Centernet.Ghost.Service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import com.TroyEmpire.Centernet.Constant.ConfigConstant;
import com.TroyEmpire.Centernet.DB.AccessPointDBManager;

import com.TroyEmpire.Centernet.Entity.AccessPoint;
import com.TroyEmpire.Centernet.Ghost.Admin.AndroidWifiAdmin;
import com.TroyEmpire.Centernet.Ghost.IAdmin.IAndroidWifiAdmin;
import com.TroyEmpire.Centernet.Ghost.IService.IAccessPointService;
import com.TroyEmpire.Centernet.Util.AndroidWifiUtil;

public class AccessPointService implements IAccessPointService {

	private AccessPointDBManager apDbManager;
	private IAndroidWifiAdmin androidWifiAdmin;

	public AccessPointService(Context context) {
		apDbManager = new AccessPointDBManager(context);
		androidWifiAdmin = new AndroidWifiAdmin(context);
	}

	public String getSecurityString(boolean concise, AccessPoint ap) {
		switch (ap.getSecurity()) {
		case ConfigConstant.SECURITY_EAP:
			return concise ? ConfigConstant.wifi_security_short_eap
					: ConfigConstant.wifi_security_eap;
		case ConfigConstant.SECURITY_PSK:
			switch (ap.getPskType()) {
			case WPA:
				return concise ? ConfigConstant.wifi_security_short_wpa
						: ConfigConstant.wifi_security_wpa;
			case WPA2:
				return concise ? ConfigConstant.wifi_security_short_wpa2
						: ConfigConstant.wifi_security_wpa2;
			case WPA_WPA2:
				return concise ? ConfigConstant.wifi_security_short_wpa_wpa2
						: ConfigConstant.wifi_security_wpa_wpa2;
			case UNKNOWN:
			default:
				return concise ? ConfigConstant.wifi_security_short_psk_generic
						: ConfigConstant.wifi_security_psk_generic;
			}
		case ConfigConstant.SECURITY_WEP:
			return concise ? ConfigConstant.wifi_security_short_wep
					: ConfigConstant.wifi_security_wep;
		case ConfigConstant.SECURITY_NONE:
		default:
			return concise ? "" : ConfigConstant.wifi_security_none;
		}
	}

	public AccessPoint loadConfig(WifiConfiguration config) {
		AccessPoint ap = new AccessPoint();
		ap.setSsid(config.SSID == null ? "" : AndroidWifiUtil
				.removeDoubleQuotes(config.SSID));
		ap.setBssid(config.BSSID);
		ap.setSecurity(AndroidWifiUtil.getSecurity(config));
		ap.setNetworkId(config.networkId);
		ap.setNetworkId(Integer.MAX_VALUE);
		return ap;
	}

	public AccessPoint loadResult(ScanResult result) {
		AccessPoint ap = new AccessPoint();
		ap.setSsid(result.SSID);
		ap.setBssid(result.BSSID);
		ap.setSecurity(AndroidWifiUtil.getSecurity(result));
		ap.setWpsAvailable(ap.getSecurity() != ConfigConstant.SECURITY_EAP
				&& result.capabilities.contains("WPS"));
		if (ap.getSecurity() == ConfigConstant.SECURITY_PSK)
			ap.setPskType(AndroidWifiUtil.getPskType(result));
		AccessPoint tempAp = apDbManager.getAPByBSSID(result.BSSID);
		ap.setIgnored(tempAp == null ? false : tempAp.isIgnored());
		if (androidWifiAdmin.getWifiConfigurationsBssidSet().contains(
				result.BSSID))
			ap.setAlreadyConfigured(true);
		else
			ap.setAlreadyConfigured(false);
		ap.setNetworkId(-1);
		ap.setLevel(result.level);
		return ap;
	}

	@Override
	public List<AccessPoint> getOrderedOpenWifiList() {
		androidWifiAdmin.startScan();
		List<AccessPoint> openWifi = new ArrayList<AccessPoint>();
		for (ScanResult result : androidWifiAdmin.getWifiList()) {
			if (AndroidWifiUtil.getSecurity(result) == ConfigConstant.SECURITY_NONE) {
				openWifi.add(loadResult(result));
			}
		}
		// TODO how to order ?
		return openWifi;
	}

	@Override
	public List<AccessPoint> getOrderedEncryptedWifiList() {
		androidWifiAdmin.startScan();
		List<AccessPoint> encryptedWifi = new ArrayList<AccessPoint>();
		for (ScanResult result : androidWifiAdmin.getWifiList()) {
			if (AndroidWifiUtil.getSecurity(result) != ConfigConstant.SECURITY_NONE)
				encryptedWifi.add(loadResult(result));
		}
		// TODO how to order ?
		return encryptedWifi;
	}

	@Override
	public void setAccessPointScanIgnored(String bssid) {
		AccessPoint ap = apDbManager.getAPByBSSID(bssid);
		if (ap == null) {
			ap = new AccessPoint();
			ap.setBssid(bssid);
			ap.setIgnored(true);
			apDbManager.save(ap);
		} else {
			ap.setIgnored(true);
			apDbManager.updateAPIgnored(ap);
		}
	}

	@Override
	public void setAccessPointScanRecover(String bssid) {
		AccessPoint ap = apDbManager.getAPByBSSID(bssid);
		if (ap == null) {
			ap = new AccessPoint();
			ap.setBssid(bssid);
			ap.setIgnored(false);
			apDbManager.save(ap);
		} else {
			ap.setIgnored(false);
			apDbManager.updateAPIgnored(ap);
		}

	}

	@Override
	public void finalize() {
		try {
			apDbManager.closeDB();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkWhetherWifiIgnored(String bssid) {
		AccessPoint ap = apDbManager.getAPByBSSID(bssid);
		if (ap == null || !ap.isIgnored()) {
			ap = null;
			return false;
		} else {
			ap = null;
			return true;
		}

	}

}
