package com.TroyEmpire.Centernet.UI.Entity;

import android.graphics.drawable.Drawable;

public class CIAListViewItem {
	private String appName;
	private String appVersion;
	private String packageName;
	private String launcherActivityName;
	private Drawable applcon;
	private int selectStateIconId;

	public int getSelectStateIconId() {
		return selectStateIconId;
	}

	public void setSelectStateIconId(int selectStateIconId) {
		this.selectStateIconId = selectStateIconId;
	}

	public CIAListViewItem() {
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getLauncherActivityName() {
		return launcherActivityName;
	}

	public void setLauncherActivityName(String launcherActivityName) {
		this.launcherActivityName = launcherActivityName;
	}

	public Drawable getApplcon() {
		return applcon;
	}

	public void setApplcon(Drawable applcon) {
		this.applcon = applcon;
	}

}
