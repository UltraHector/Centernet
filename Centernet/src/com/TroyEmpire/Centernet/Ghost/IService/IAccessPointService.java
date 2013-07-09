package com.TroyEmpire.Centernet.Ghost.IService;

import java.util.List;

import com.TroyEmpire.Centernet.Entity.AccessPoint;

public interface IAccessPointService {
	/**
	 * @param a list of mixed wifi
	 * get a list of wifi which are encrypted by password
	 * the result are orderd based on order algorithms
	 */
	List<AccessPoint> getOrderedEncryptedWifiList();
	/**
	 * @param a list of mixed wifi
	 * get a list of wifi which are open  
	 * the result are orderd based on order algorithms
	 */
	List<AccessPoint> getOrderedOpenWifiList();
	
	/**
	 * @param bssid 
	 * 		the bssid which will be ignored when when start scan
	 */
	void setAccessPointScanIgnored(String bssid);
	/**
	 * @param bssid 
	 * 		the bssid which will be covered when when start scan
	 */
	void setAccessPointScanRecover(String bssid);
	
	/**
	 * check whether wifi ignored
	 * @param bssid the wifi's bssid
	 */
	boolean checkWhetherWifiIgnored(String bssid);
	
	/**
	 * do somethind like close the database when necessary
	 */
	public void finalize();
	

}
