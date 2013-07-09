package com.TroyEmpire.Centernet.Constant;

public class DBConstant {

	/* constants for ccu */
	public static final String CENTERNETCENTRALUNIT_TABLE_NAME = "centernetCentralUnit";
	public static final String CENTERNETCENTRALUNIT_COLUMN_ID = "_id";
	public static final String CENTERNETCENTRALUNIT_COLUMN_BSSID = "BSSID";
	public static final String CENTERNETCENTRALUNIT_COLUMN_CCUID = "ccuId";
	public static final String CENTERNETCENTRALUNIT_COLUMN_PPVERSIONS = "portalPacketVersions";
	public static final String CENTERNETCENTRALUNIT_COLUMN_TITLE = "title";
	public static final String CENTERNETCENTRALUNIT_COLUMN_BODY_TYPE = "bodyType";

	/* constants for accesspoint */
	public static final String ACCESSPOINT_TABLE_NAME = "accessPoint";
	public static final String ACCESSPOINT_COLUMN_BSSID = "BSSID";
	public static final String ACCESSPOINT_COLUMN_IGNORED = "ignored";

	/* constants for centernet installed applcations */
	public static final String CENTERNET_INSTALLED_APP_TABLE_NAME = "centernetInstalledApp";
	public static final String CENTERNET_INSTALLED_APP_COLUMN_ID = "id";
	public static final String CENTERNET_INSTALLED_APP_COLUMN_APPNAME = "appName";
	public static final String CENTERNET_INSTALLED_APP_COLUMN_APPVERSION = "appVersion";
	public static final String CENTERNET_INSTALLED_APP_COLUMN_APP_PACKAGENAME = "packageName";
	public static final String CENTERNET_INSTALLED_APP_COLUMN_APP_LUANCHER_ACTIVITY_NAME = "app_Launcher_name";
}
