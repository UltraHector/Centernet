package com.TroyEmpire.Centernet.DB;

import com.TroyEmpire.Centernet.Constant.DBConstant;
import com.TroyEmpire.Centernet.Entity.AccessPoint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AccessPointDBManager {
	private CenternetDBHelper helper;
	private SQLiteDatabase db;
	
	public AccessPointDBManager(Context context){
		helper = new CenternetDBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}
	
	/**
	 * add AccessPoint
	 * 
	 * @param AccessPoint
	 */
	public void save(AccessPoint accessPoint) {
		db.beginTransaction(); // 开始事务
		try {
			db.execSQL("INSERT INTO " + DBConstant.ACCESSPOINT_TABLE_NAME
					+ " VALUES(?,?)",
					new Object[] {accessPoint.getBssid(), accessPoint.isIgnored()});
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}
	
	public void updateAPIgnored(AccessPoint ap) {
			ContentValues cv = new ContentValues();
			cv.put(DBConstant.ACCESSPOINT_COLUMN_IGNORED, ap.isIgnored());
			db.update(DBConstant.ACCESSPOINT_TABLE_NAME, cv,
					DBConstant.ACCESSPOINT_COLUMN_BSSID + " = ?",
					new String[] { ap.getBssid() });
	}
	
	public AccessPoint getAPByBSSID(String BSSID){
		String condition = DBConstant.ACCESSPOINT_COLUMN_BSSID + "='"
				+ BSSID + "'";
		Cursor c = queryTheCursor(condition);
		AccessPoint ap = loadSingleAPFromCursor(c);
		c.close();
		return ap;
	}
	
	private AccessPoint loadSingleAPFromCursor(Cursor c) {
		if (c.moveToFirst())
			return loadAPFromCursor(c);
		else
			return null;
	}

	/**
	 * query all AccessPoints, return cursor
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheCursor(String condition) {
		Cursor c = null;
		if (condition != null) {
			c = db.rawQuery("SELECT * FROM "
					+ DBConstant.ACCESSPOINT_TABLE_NAME + " where "
					+ condition, null);
		} else {
			c = db.rawQuery("SELECT * FROM "
					+ DBConstant.ACCESSPOINT_TABLE_NAME, null);
		}
		return c;
	}
	
	/**
	 * load a accessPoint from a cursor which has already pointed to the
	 * exact place
	 */
	private AccessPoint loadAPFromCursor(Cursor c) {

		AccessPoint ap = new AccessPoint();
		ap.setBssid(c.getString(c.getColumnIndex(DBConstant.ACCESSPOINT_COLUMN_BSSID)));
		if(c.getInt(c.getColumnIndex(DBConstant.ACCESSPOINT_COLUMN_IGNORED)) == 1)
			ap.setIgnored(true);
		else 
			ap.setIgnored(false);
		return ap;
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
