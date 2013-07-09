package com.TroyEmpire.Centernet.Ghost.Admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.TroyEmpire.Centernet.Constant.ConfigConstant;
import com.TroyEmpire.Centernet.Ghost.IAdmin.IAndroidWifiAdmin;
import com.TroyEmpire.Centernet.Util.AndroidWifiUtil;
import com.TroyEmpire.Centernet.Util.NetworkUtil;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class AndroidWifiAdmin implements IAndroidWifiAdmin {

	// 定义一个WifiManager对象
	private WifiManager mWifiManager;
	// 定义一个WifiInfo对象
	private WifiInfo mWifiInfo;
	// 扫描出的网络连接列表
	private List<ScanResult> mWifiList = new ArrayList<ScanResult>();
	// 网络连接列表
	private List<WifiConfiguration> mWifiConfigurations = new ArrayList<WifiConfiguration>();
	WifiLock mWifiLock;

	private Set<String> wifiConfigurationsBssidSet = new HashSet<String>();

	public AndroidWifiAdmin(Context context) {
		// 取得WifiManager对象
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 取得WifiInfo对象
		mWifiInfo = mWifiManager.getConnectionInfo();
		// 初始化其他数据成员
		startScan();
	}

	// 打开wifi
	@Override
	public void openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	// 关闭wifi
	@Override
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	// 检查当前wifi状态
	@Override
	public int checkState() {
		if (mWifiManager != null)
			return mWifiManager.getWifiState();
		else
			// Error Happened
			return 0;
	}

	// 检查当前wifi状态, 返回可读的状态信息
	@Override
	public String checkStateInString() {
		switch (mWifiManager.getWifiState()) {
		case 1:
			return "WIFI_STATE_DISABLED";
		case 2:
			return "WIFI_STATE_DISABLING";
		case 3:
			return "WIFI_STATE_ENABLED";
		case 4:
			return "WIFI_STATE_ENABLING";
		case 5:
			return "WIFI_STATE_UNKNOWN";
		default:
			return "ERROR_HAPPEND";
		}
	}

	// 锁定wifiLock
	@Override
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	// 解锁wifiLock
	@Override
	public void releaseWifiLock() {
		// 判断是否锁定
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	// 创建一个wifiLock
	@Override
	public void createWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("DebugTag");
	}

	// 得到配置好的网络
	@Override
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfigurations;
	}

	// 指定配置好的网络进行连接
	@Override
	public boolean connetionConfigurationByIndex(int index) {
		if (index > mWifiConfigurations.size()) {
			return false;
		}
		// 连接配置好指定ID的网络
		return mWifiManager.enableNetwork(
				mWifiConfigurations.get(index).networkId, true);
	}

	@Override
	public void startScan() {
		mWifiManager.startScan();
		// 得到扫描结果
		mWifiList = mWifiManager.getScanResults();
		// 得到配置好的网络连接
		mWifiConfigurations = mWifiManager.getConfiguredNetworks();
		// 把configurationwifi 存入到一个Set中
		if (null != mWifiConfigurations) {
			for (WifiConfiguration wifi : mWifiConfigurations) {
				getWifiConfigurationsBssidSet().add(wifi.BSSID);
			}
		}

	}

	// 得到网络列表
	@Override
	public List<ScanResult> getWifiList() {
		return mWifiList;
	}

	// 查看扫描结果
	@Override
	public StringBuffer lookUpScanResultInString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mWifiList.size(); i++) {
			sb.append("Index_" + new Integer(i + 1).toString() + ":");
			// 将ScanResult信息转换成一个字符串包
			// 其中把包括：BSSID、SSID、capabilities、frequency、level
			sb.append((mWifiList.get(i)).toString()).append("\n");
		}
		return sb;
	}

	@Override
	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	@Override
	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	@Override
	public String getSSID() {
		// need to clean the " at the head and the end.
		if (mWifiInfo == null || mWifiInfo.getSSID() == null)
			return "NULL";
		else
			return mWifiInfo.getSSID().substring(1,
					mWifiInfo.getSSID().length() - 1);
	}

	@Override
	public String getAPIpAddress() {
		int ip = (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
		String ipString = String.format("%d.%d.%d.%d", (ip & 0xff),
				(ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
		return ipString;
	}

	@Override
	public String getLocalIpAddress() {
		return NetworkUtil.getAndroidIP();
	}

	@Override
	public int getNetWordId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	@Override
	public String getWifiInfo() {
		return (mWifiInfo != null) ? "NULL" : mWifiInfo.toString();
	}

	// 添加一个网络并连接
	@Override
	public void addNetWork(WifiConfiguration configuration) {
		int wcgId = mWifiManager.addNetwork(configuration);
		mWifiManager.enableNetwork(wcgId, true);
	}

	// 断开指定ID的网络
	@Override
	public void disConnectionWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	@Override
	public void removeConfiguredWifiByBSSID(String SSID) {
		for (WifiConfiguration config : mWifiManager.getConfiguredNetworks()) {
			if (config.SSID == "\"" + SSID + "\"") {
				mWifiManager.removeNetwork(config.networkId);
				break;
			}
		}

	}

	@Override
	public void refreshConnectWifiInto() {
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	@Override
	public boolean connetWifiByScanResult(ScanResult scan) {
		int networkId = -1; // network id
		boolean cont = true;
		String s, bs;
		for (WifiConfiguration w : mWifiConfigurations) {
			s = "\"" + scan.SSID + "\"";
			bs = scan.BSSID;
			networkId = w.networkId;
			/* Make sure the scan result is the same as the configured WIFI */
			if ((w.SSID != null && w.SSID.equals(s))
					|| (w.BSSID != null && w.BSSID.equals(bs))) {
				cont = false;
				break;
			}
		}
		if (cont) {
			// Deal with a LAN not configured but without password
			if (AndroidWifiUtil.getSecurity(scan) == ConfigConstant.SECURITY_NONE) {
				networkId = mWifiManager.addNetwork(AndroidWifiUtil
						.generateOpenNetworkConfig(scan));
				mWifiManager.saveConfiguration();
				return connect(networkId);
			}
			/* An uniconfigured wifi */
			return false;

		} else if (networkId != -1) {
			return connect(networkId);
		}
		return false;
	}

	private boolean connect(int networkId) {
		// Connect to network by disabling others.
		mWifiManager.enableNetwork(networkId, true);
		return mWifiManager.reconnect();
	}

	/**
	 * get the configurated wifi's bssid set
	 */
	@Override
	public Set<String> getWifiConfigurationsBssidSet() {
		return wifiConfigurationsBssidSet;
	}
}
