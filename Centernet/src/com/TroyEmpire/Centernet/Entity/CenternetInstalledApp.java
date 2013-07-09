package com.TroyEmpire.Centernet.Entity;

/**
 * @entity the entity to save the applications which is installed through
 *         centernet client
 */

public class CenternetInstalledApp {

	private long id;
	private String appName;
	private String appVersion;
	private String packageName;
	private String launcherActivityName;

	public CenternetInstalledApp() {

	}

	public String getLauncherActivityName() {
		return launcherActivityName;
	}

	public void setLauncherActivityName(String launcherActivityName) {
		this.launcherActivityName = launcherActivityName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
