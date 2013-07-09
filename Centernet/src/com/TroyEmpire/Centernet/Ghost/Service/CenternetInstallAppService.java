package com.TroyEmpire.Centernet.Ghost.Service;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.TroyEmpire.Centernet.DB.CenternetInstalledAppDBManager;
import com.TroyEmpire.Centernet.Entity.CenternetInstalledApp;
import com.TroyEmpire.Centernet.Ghost.IService.ICenternetInstallAppService;

public class CenternetInstallAppService implements ICenternetInstallAppService {

	private static final String TAG = "com.TroyEmpire.Centernet.Ghost.Service.CenternetInstallAppService";
	private CenternetInstalledAppDBManager ciaDBManager;
	private Context context;
	private PackageManager pm;

	public CenternetInstallAppService(Context context) {
		Log.i(TAG, "CenternetInstallAppService()");
		this.context = context;
		pm = this.context.getPackageManager();
		ciaDBManager = new CenternetInstalledAppDBManager(this.context);
	}

	@Override
	public void saveCenternetInstallAppIntoDb(
			CenternetInstalledApp centernetInstalledApp) {
		ciaDBManager.save(centernetInstalledApp);
	}

	@Override
	public void finalize() {
		try {
			ciaDBManager.closeDB();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public CenternetInstalledApp getCIAInSystemBypackageName(String packageName) {
		CenternetInstalledApp installedApp;
		List<PackageInfo> packageInfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo info : packageInfos) {
			// 非系统程序
			if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				installedApp = getAppInfoInSystemByPackageInfo(info,
						packageName);
				if (null != installedApp) {
					return installedApp;
				}
			}
			// 本来是系统程序，被用户手动更新后，该系统程序成为第三方应用程序
			else if ((info.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
				installedApp = getAppInfoInSystemByPackageInfo(info,
						packageName);
				if (null != installedApp) {
					return installedApp;
				}
			}
		}
		return null;
	}

	@Override
	public CenternetInstalledApp getAppInfoInSystemByPackageInfo(
			PackageInfo pinfo, String packageName) {
		if (packageName.equals(pinfo.packageName)) {
			CenternetInstalledApp installedApp = new CenternetInstalledApp();
			installedApp.setAppName(pinfo.applicationInfo.loadLabel(pm)
					.toString());
			installedApp.setPackageName(pinfo.packageName);
			installedApp.setAppVersion(pinfo.versionName);
			installedApp
					.setLauncherActivityName(getLauncherActivityName(pinfo.packageName));
			return installedApp;
		} else
			return null;
	}

	@Override
	public List<CenternetInstalledApp> getAllAppInstalledByCenternet() {
		return ciaDBManager.getAllCenternetInstalledApps();
	}

	@Override
	public Drawable getAppDrawable(String packageName, String appName) {
		Drawable appIcon = null;
		List<PackageInfo> packageInfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo info : packageInfos) {
			// 非系统程序
			if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				appIcon = getAppDrawableByPackageInfo(info, packageName,
						appName);
				if (null != appIcon) {
					return appIcon;
				}
			}
			// 本来是系统程序，被用户手动更新后，该系统程序成为第三方应用程序
			else if ((info.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
				appIcon = getAppDrawableByPackageInfo(info, packageName,
						appName);
				if (null != appIcon) {
					return appIcon;
				}
			}
		}
		return appIcon;
	}

	@Override
	public Drawable getAppDrawableByPackageInfo(PackageInfo pinfo,
			String packageName, String appName) {
		String appLabel = (String) pinfo.applicationInfo.loadLabel(pm);
		String appPackageName = pinfo.packageName;
		if (appName.equals(appLabel) && packageName.equals(appPackageName)) {
			return pinfo.applicationInfo.loadIcon(pm);
		} else {
			return null;
		}

	}

	@Override
	public boolean isAppExistInDbByName(String appName) {
		return ciaDBManager.isCIAAppsExistByAppName(appName);
	}

	@Override
	public boolean isAppExistInDbByPackageName(String packageName) {
		return ciaDBManager.isCIAExistByAppPackageName(packageName);
	}

	@Override
	public void deleteAppInfoByAppName(String appName) {
		ciaDBManager.deleteCIAppInfobyAppName(appName);
	}

	@Override
	public int getCountOfInstalledApps() {
		return ciaDBManager.getNumsOfInstalledApps();
	}

	@Override
	public String getLauncherActivityName(String packageName) {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
		Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));
		Log.i(TAG, "packageName=" + packageName);
		for (ResolveInfo rInfo : appList) {
			if (packageName.equals(rInfo.activityInfo.packageName)) {
				Log.i(TAG, "afaf" + rInfo.activityInfo.loadLabel(pm).toString());
				return rInfo.activityInfo.loadLabel(pm).toString();
			}
		}
		return "";
	}

	@Override
	public String getAppVersionInDBByPackageName(String packageName) {
		return ciaDBManager.getAppVersionByPackageName(packageName);
	}

	@Override
	public CenternetInstalledApp getAppInfoInSystemByPackageName(
			String packageName) {
		CenternetInstalledApp cia = new CenternetInstalledApp();
		List<PackageInfo> packageInfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo info : packageInfos) {
			// 非系统程序 由于本程序只处理第三方程序
			if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				if (info.packageName.equals(packageName)) {
					cia.setAppVersion(info.versionName);
					cia.setAppName(info.applicationInfo.loadLabel(pm)
							.toString());
					cia.setPackageName(packageName);
					cia.setLauncherActivityName(getLauncherActivityName(packageName));
					return cia;
				}
			}
		}
		return null;
	}

	@Override
	public CenternetInstalledApp getCIAInDbByPackageName(String packageName) {
		return ciaDBManager.getCIAInfoByPackageName(packageName);
	}

	@Override
	public void updateAppVersionInDbByPackageName(String packageName,
			String newAppVersion) {
		ciaDBManager.updateAppVersion(packageName, newAppVersion);
	}

}
