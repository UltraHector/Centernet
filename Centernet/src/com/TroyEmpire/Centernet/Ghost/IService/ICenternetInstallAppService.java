package com.TroyEmpire.Centernet.Ghost.IService;

import java.util.List;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import com.TroyEmpire.Centernet.Entity.CenternetInstalledApp;

public interface ICenternetInstallAppService {

	/**
	 * save the centernetInstalledApp into sqlite database
	 * */
	void saveCenternetInstallAppIntoDb(
			CenternetInstalledApp centernetInstalledApp);

	/**
	 * do somethind like close the database when necessary
	 */
	public void finalize();

	/**
	 * @param packageName
	 *            the packageName of the app
	 * @return get the CenternetInstalledApp entity by packageName
	 * */
	CenternetInstalledApp getCIAInSystemBypackageName(String packageName);

	/**
	 * used by getInstalledAppByAppName to achieve it's goal
	 * */
	CenternetInstalledApp getAppInfoInSystemByPackageInfo(PackageInfo pinfo,
			String appname);

	/**
	 * @param packageName
	 *            the packageName of the app
	 * @return CenternetInstalledApp
	 * */
	CenternetInstalledApp getCIAInDbByPackageName(String packageName);

	/**
	 * 
	 * @return get all the app installed by our progaram
	 * */
	List<CenternetInstalledApp> getAllAppInstalledByCenternet();

	/**
	 * @param packageName
	 *            the packagename of the application
	 * @param appName
	 *            the name of the application
	 * @return the icon of the pointed application
	 * 
	 * */
	Drawable getAppDrawable(String packageName, String appName);

	/**
	 * @param packageName
	 *            the packagename of the application
	 * @param appName
	 *            the name of the application
	 * @parem pinfo the PackageInfo Object to get applicationInfo then get the
	 *        icon of the app
	 * @return the icon of the pointed application
	 * 
	 * */
	Drawable getAppDrawableByPackageInfo(PackageInfo pinfo, String packageName,
			String appName);

	/**
	 * @param appName
	 *            name of the app ready to check
	 * @return the check result
	 * */
	boolean isAppExistInDbByName(String appName);

	/**
	 * @param packageName
	 *            packageName of the app ready to check
	 * @return the check result
	 * */
	boolean isAppExistInDbByPackageName(String packageName);

	/**
	 * @param appNmae
	 *            name of the app ready to delete
	 * */
	void deleteAppInfoByAppName(String appName);

	/**
	 * @return the amount of the apps installed by this program
	 * */
	int getCountOfInstalledApps();

	/**
	 * @param packageName
	 *            the packageName of the App Installed by CIA
	 * @return the name of the launcher activity
	 * */
	String getLauncherActivityName(String packageName);

	/**
	 * @param packageName
	 *            the packageName of the App Installed by CIA
	 * @return the app version stored in the db
	 * */
	String getAppVersionInDBByPackageName(String packageName);

	/**
	 * @param packageName
	 *            the packageName of the App Installed by CIA
	 * @return the latest app version
	 * 
	 */
	CenternetInstalledApp getAppInfoInSystemByPackageName(String packageName);

	/**
	 * @param packageName
	 *            the packageName of the App Installed by CIA
	 * */
	void updateAppVersionInDbByPackageName(String packageName,
			String newAppVersion);
}
