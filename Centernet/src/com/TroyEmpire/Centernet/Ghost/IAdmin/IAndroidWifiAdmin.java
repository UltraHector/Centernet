/**
 * 
 */

package com.TroyEmpire.Centernet.Ghost.IAdmin;

import java.util.List;
import java.util.Set;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;

public interface IAndroidWifiAdmin {

	/**
	 * 打开wifi
	 */
	void openWifi();

	/**
	 * 关闭wifi
	 */
	void closeWifi();

	/**
	 * 检查当前wifi状态，
	 * 
	 * @return 状态代号
	 */
	int checkState();

	/**
	 * 锁定wifiLock
	 */
	void acquireWifiLock();

	/**
	 * 检查当前Wifi状态，
	 * 
	 * @return 返回可读的状态信息
	 */
	String checkStateInString();

	/**
	 * 释放wifiLock
	 */
	void releaseWifiLock();

	/**
	 * 创建一个wifiLock
	 */
	void createWifiLock();

	/**
	 * 得到配置好的网络
	 */
	List<WifiConfiguration> getConfiguration();

	/**
	 * 指定配置好的网络进行连接
	 */
	boolean connetionConfigurationByIndex(int index);

	/**
	 * 开始一次Wifi信号的扫描
	 */
	void startScan();

	/**
	 * @return 返回扫描到的Wifi结果
	 */
	List<ScanResult> getWifiList();

	/**
	 * @return 返回扫描到的Wifi结果,以可读的String形式呈现
	 */
	StringBuffer lookUpScanResultInString();

	void disConnectionWifi(int netId);

	/**
	 * 添加一个网络连接
	 * @param configuration 一个配置好的wifi
	 */
	void addNetWork(WifiConfiguration configuration);
	
	
	/**
	 * @return 以可读String的形式返回当前连接的Wifi信息
	 */
	String getWifiInfo();

	/**
	 * @return 获取当前连接网络的Id
	 */
	int getNetWordId();

	/**
	 * @return 获取连接网络路由器的Ip
	 */
	String getAPIpAddress();

	/**
	 * @return 获取本机当前Ip address
	 */
	String getLocalIpAddress();

	/**
	 * @return 获取当前连接网络的Mac
	 */
	String getMacAddress();

	/**
	 * @return 获取当前连接网络的Bssid
	 */
	String getBSSID();

	/**
	 * @return 获取当前连接网络的ssid
	 */
	String getSSID();

	/**
	 * 删除一个配置好的Wifi by specific BSSID
	 */
	void removeConfiguredWifiByBSSID(String BSSID);

	/**
	 * 更新当前连接的wifi信息
	 */
	void refreshConnectWifiInto();

	/**
	 * 连接一个网络，如果是没有配置过的无密码保护的Wifi则先配置后保存
	 * 有密码但是没有配置的Wifi返回 false
	 * @return 是否成功连接此指定的Wifi
	 */
	boolean connetWifiByScanResult(ScanResult scanResult);

	Set<String> getWifiConfigurationsBssidSet();
}
