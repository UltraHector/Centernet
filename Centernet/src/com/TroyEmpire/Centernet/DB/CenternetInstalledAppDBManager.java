package com.TroyEmpire.Centernet.DB;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.TroyEmpire.Centernet.Constant.DBConstant;
import com.TroyEmpire.Centernet.Entity.CenternetInstalledApp;

public class CenternetInstalledAppDBManager {
	private CenternetDBHelper helper;
	private SQLiteDatabase db;
	private static final String TAG = "com.TroyEmpire.Centernet.DB.CenternetInstalledAppDBManager";

	public CenternetInstalledAppDBManager(Context context) {
		helper = new CenternetDBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	/**
	 * add centernetApp
	 * 
	 * @param centernetApp
	 */
	public void save(CenternetInstalledApp centernetApp) {
		db.beginTransaction(); // 开始事务
		try {
			db.execSQL(
					"INSERT INTO "
							+ DBConstant.CENTERNET_INSTALLED_APP_TABLE_NAME
							+ "("
							+ DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APPNAME
							+ ","
							+ DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APPVERSION
							+ ","
							+ DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APP_PACKAGENAME
							+ ","
							+ DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APP_LUANCHER_ACTIVITY_NAME
							+ ") " + " VALUES(?,?,?,?)",
					new Object[] { centernetApp.getAppName(),
							centernetApp.getAppVersion(),
							centernetApp.getPackageName(),
							centernetApp.getLauncherActivityName() });
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * @return all the centernet installed apps
	 */
	public List<CenternetInstalledApp> getAllCenternetInstalledApps() {
		String condition = null;
		Cursor c = queryTheCursor(condition);
		List<CenternetInstalledApp> aps = loadListCIAppFromCursor(c);
		c.close();
		return aps;
	}

	/**
	 * @return the number of installed apps
	 * */
	public int getNumsOfInstalledApps() {
		String condition = null;
		Cursor c = queryTheCursor(condition);
		if (null == c) {
			return 0;
		} else {
			return c.getCount();
		}
	}

	/**
	 * @param appName
	 *            the application's name
	 * @return has the applicaton save into db
	 * 
	 * */
	public boolean isCIAAppsExistByAppName(String appName) {
		boolean exist = false;
		String condition = DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APPNAME
				+ " = '" + appName + "'";
		Cursor c = queryTheCursor(condition);
		if (null != c && c.getCount() != 0) {
			exist = true;
			c.close();
		}
		return exist;
	}

	/**
	 * @param appName
	 *            the application's name
	 * @return has the applicaton save into db
	 * 
	 * */
	public boolean isCIAExistByAppPackageName(String packageName) {
		boolean exist = false;
		String condition = DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APP_PACKAGENAME
				+ " = '" + packageName + "'";
		Cursor c = queryTheCursor(condition);
		if (null != c && c.getCount() != 0) {
			exist = true;
			c.close();
		}
		Log.i(TAG, "isCIAExistByAppPackageName exist:" + exist);
		return exist;
	}

	/**
	 * 
	 * */
	public String getAppVersionByPackageName(String packageName) {
		String condition = DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APP_PACKAGENAME
				+ " = '" + packageName + "'";
		Cursor c = queryTheCursor(condition);
		if (c.moveToFirst()) {
			String version = c
					.getString(c
							.getColumnIndex(DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APPVERSION));
			Log.i(TAG, "version = " + version);
			return version;
		}
		return "";
	}

	/**
	 * delete the specific ciApp based on the ciApp's id
	 */
	public void deleteCIAppServer(CenternetInstalledApp ciApp) {
		db.delete(DBConstant.CENTERNET_INSTALLED_APP_TABLE_NAME,
				DBConstant.CENTERNET_INSTALLED_APP_COLUMN_ID + "= ?",
				new String[] { String.valueOf(ciApp.getId()) });
	}

	/**
	 * @param appName
	 *            the name of the app
	 * @return delete the pointed app by appName
	 * */
	public void deleteCIAppInfobyAppName(String appName) {
		db.delete(DBConstant.CENTERNET_INSTALLED_APP_TABLE_NAME,
				DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APPNAME + " = ?",
				new String[] { appName });
		Log.i(TAG, "deleteCIAppInfobyAppName delete");
	}

	/**
	 * @param packageName
	 *            the packageName of the App Installed by CIA
	 * */
	public void updateAppVersion(String packageName, String newAppVersion) {
		ContentValues agrs = new ContentValues();
		agrs.put(DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APPVERSION,
				newAppVersion);
		try {
			db.update(DBConstant.CENTERNET_INSTALLED_APP_TABLE_NAME, agrs,
					DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APP_PACKAGENAME
							+ " = ?", new String[] { packageName });

		} catch (SQLiteException e) {
			e.printStackTrace();
			Log.v(TAG, "packageName=" + packageName);
		}
		Log.i(TAG, "updateAppVersion update");
	}

	/**
	 * @param packageName
	 *            the packageName of the app
	 * @return CenternetInstalledApp object
	 * */
	public CenternetInstalledApp getCIAInfoByPackageName(String packageName) {
		String condition = DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APP_PACKAGENAME
				+ " = '" + packageName + "'";
		Cursor c = queryTheCursor(condition);
		if (c.moveToFirst()) {
			return loadCIAppFromCursor(c);
		}
		return null;
	}

	/**
	 * get a list of centernet apps from the cursor
	 * 
	 * @return List<CenternetInstalledApp>
	 */
	private List<CenternetInstalledApp> loadListCIAppFromCursor(Cursor c) {
		List<CenternetInstalledApp> ciApps = new ArrayList<CenternetInstalledApp>();
		while (c.moveToNext()) {
			ciApps.add(loadCIAppFromCursor(c));
		}
		return ciApps;
	}

	/**
	 * configure a centernet app from the cursor
	 * 
	 * @return CenternetInstalledApp
	 */
	private CenternetInstalledApp loadCIAppFromCursor(Cursor c) {
		CenternetInstalledApp ciApp = new CenternetInstalledApp();
		ciApp.setAppName(c.getString(c
				.getColumnIndex(DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APPNAME)));
		ciApp.setAppVersion(c.getString(c
				.getColumnIndex(DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APPVERSION)));
		ciApp.setPackageName(c.getString(c
				.getColumnIndex(DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APP_PACKAGENAME)));
		ciApp.setLauncherActivityName(c.getString(c
				.getColumnIndex(DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APP_LUANCHER_ACTIVITY_NAME)));
		return ciApp;
	}

	/**
	 * query all centernetapps, return cursor
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheCursor(String condition) {
		Cursor c = null;
		try {
			if (condition != null) {
				c = db.rawQuery("SELECT * FROM "
						+ DBConstant.CENTERNET_INSTALLED_APP_TABLE_NAME
						+ " where " + condition, null);
			} else {
				c = db.rawQuery("SELECT * FROM "
						+ DBConstant.CENTERNET_INSTALLED_APP_TABLE_NAME, null);
			}
		} catch (SQLiteException e) {
			return null;
		}
		return c;
	}

	/**
	 * close database
	 * 
	 * @throws Throwable
	 */
	public void closeDB() throws Throwable {
		if (helper != null) {
			helper.close();
		}
		if (null != db) {
			db.close();
		}
		super.finalize();
	}
}
