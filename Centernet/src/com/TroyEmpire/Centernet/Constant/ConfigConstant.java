package com.TroyEmpire.Centernet.Constant;

import android.os.Environment;

/**
 * Constants used by the Centernet package
 */
public class ConfigConstant {

	// the max time to wait to connect to wifi and pick up AP's MAC
	public static final int MAX_WIFI_TRY_TIME = 3000; // has not been used,  == 300 * 10
	public static final int TRY_SEND_PACKET_INTERVAL = 300; // every 300ms try to send the packet
	public static final int TRY_SEND_PACKET_TIMES = 10; // maxmum try 20 times to wait the network is available to send data
	
	// the interval to refresh bssid to make sure to connect to the wifi and
	// picked up AP's MAC
	public static final int TRY_WIFI_INTERVAL = 300;
	// the maximum time to wait the machine to get the ip address
	public static final int MAX_WAIT_IP_TIME = 5000;// 4 second

	public static final int PROBE_SERVER_PORT = 15000;
	public static final int MAX_WAIT_RESPONSE_HANDSHAKE = 3000;// 1500 milli

	public static final int PROBE_REQUEST_TO_SERVER_MAX_BUFFER = 1500; // 1500byte

	public static final String ANDROID = "android";

	/* Data base configurations */
	// for the db managed by the system which is under /database
	public static final String CENTERNET_DB_NAME = "centernet.db";
	// for the files saved in the SD card
	public static final String CENTERNET_STORAGE_ROOT = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/CENTERNET";
	public static final String CENTERNET_SERVER_PROBE_RESPONE_BODY_ROOT = CENTERNET_STORAGE_ROOT
			+ "/ProbeResponseBody";

	/* Preferences */
	public static final String WIFI_PASSWORD_SHARED_REFEREMCE = "wifi_password_shared_reference";
	public static final String WIFI_SCAN_INFO = "wifi_scan_info";

	/* WIFI */
	/**
	 * These values are matched in string arrays -- changes must be kept in sync
	 */
	public static final int SECURITY_NONE = 0;
	public static final int SECURITY_WEP = 1;
	public static final int SECURITY_PSK = 2;
	public static final int SECURITY_EAP = 3;
	/**
	 * security string
	 */
	public static final String KEY_DETAILEDSTATE = "key_detailedstate";
	public static final String KEY_WIFIINFO = "key_wifiinfo";
	public static final String KEY_SCANRESULT = "key_scanresult";
	public static final String KEY_CONFIG = "key_config";

	public static final String wifi_security_none = "None";
	public static final String wifi_security_short_wep = "WEP";
	public static final String wifi_security_short_wpa = "WPA";
	public static final String wifi_security_short_wpa2 = "WPA2";
	public static final String wifi_security_short_wpa_wpa2 = "WPA/WPA2";
	public static final String wifi_security_short_psk_generic = "WPA/WPA2";
	public static final String wifi_security_short_eap = "802.1x";
	public static final String wifi_security_wep = "WEP";
	public static final String wifi_security_wpa = "WPA PSK";
	public static final String wifi_security_wpa2 = "WPA2 PSK";
	public static final String wifi_security_wpa_wpa2 = "WPA/WPA2 PSK";
	public static final String wifi_security_psk_generic = "WPA/WPA2 PSK";
	public static final String wifi_security_eap = "802.1x EAP";

	/**
	 * probe response broad cast action
	 */
	public final static String BROADCAST_NEW_PROBE_RESPONE_ACTION = "com.TroyEmpire.Centernet.broadcast.BROADCAST_NEW_PROBE_RESPONE_ACTION";
	public final static String BROADCAST_NEW_PROBE_RESPONE_VALUE = "com.TroyEmpire.Centernet.broadcast.BROADCAST_NEW_PROBE_RESPONE_VALUE";

	/*
	 * shared precerences
	 */
}
