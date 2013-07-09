package com.TroyEmpire.Centernet.DB;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.TroyEmpire.Centernet.Constant.DBConstant;
import com.TroyEmpire.Centernet.Constant.ProbeResponseBodyType;
import com.TroyEmpire.Centernet.Entity.CenternetCentralUnit;

public class CenternetCentralUnitDBManager {
	private CenternetDBHelper helper;
	private SQLiteDatabase db;

	public CenternetCentralUnitDBManager(Context context) {
		helper = new CenternetDBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	/**
	 * add CenternetCentralUnits
	 * 
	 * @param CenternetCentralUnits
	 */
	public void save(CenternetCentralUnit ccu) {
		db.beginTransaction(); // 开始事务
		try {
			db.execSQL("INSERT INTO " + DBConstant.CENTERNETCENTRALUNIT_TABLE_NAME
					+ " VALUES(null, ?, ?, ?, ?, ?)",
					new Object[] { ccu.getBSSID(), ccu.getCcuId(),
							ccu.getPortalPacketVersion(), ccu.getTitile(),
							ccu.getBodyType().toString() });
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void updateCCU(CenternetCentralUnit ccu) {
		// update the sqlite database
		ContentValues cv = new ContentValues();
		cv.put(DBConstant.CENTERNETCENTRALUNIT_COLUMN_TITLE, ccu.getTitile());
		cv.put(DBConstant.CENTERNETCENTRALUNIT_COLUMN_PPVERSIONS,
				ccu.getPortalPacketVersion());
		cv.put(DBConstant.CENTERNETCENTRALUNIT_COLUMN_BSSID, ccu.getBSSID());
		db.update(DBConstant.CENTERNETCENTRALUNIT_TABLE_NAME, cv,
				DBConstant.CENTERNETCENTRALUNIT_COLUMN_CCUID + " = ?",
				new String[] { String.valueOf(ccu.getCcuId()) });
	}

	public void updataAdVersion(CenternetCentralUnit ccu) {
		ContentValues cv = new ContentValues();
		cv.put("adVersion", ccu.getPortalPacketVersion());
		db.update(DBConstant.CENTERNETCENTRALUNIT_TABLE_NAME, cv,
				DBConstant.CENTERNETCENTRALUNIT_COLUMN_PPVERSIONS + " = ?",
				new String[] { ccu.getPortalPacketVersion() });
	}

	public void deleteCCU(int ccuId) {
		db.delete(DBConstant.CENTERNETCENTRALUNIT_TABLE_NAME,
				DBConstant.CENTERNETCENTRALUNIT_COLUMN_ID + "= ?",
				new String[] { String.valueOf(ccuId) });
	}

	public List<CenternetCentralUnit> getAllCCUs() {
		Cursor c = queryTheCursor(null);
		List<CenternetCentralUnit> ccus = loadListCCUsFromCursor(c);
		c.close();
		return ccus;
	}

	/**
	 * get a list of CCUs by ap's bssid
	 * 
	 * @param BSSID
	 *            the AP's Bssid
	 */
	public List<CenternetCentralUnit> getCCUsByAP(String BSSID) {
		String condition = DBConstant.CENTERNETCENTRALUNIT_COLUMN_BSSID + "='"
				+ BSSID + "'";
		Cursor c = queryTheCursor(condition);
		List<CenternetCentralUnit> ccus = loadListCCUsFromCursor(c);
		c.close();
		return ccus;
	}

	/**
	 * @param type
	 *            the bodytype in string
	 * @return the body type in enum
	 */
	private ProbeResponseBodyType stringToBodyType(String type) {
		if (type.equals(ProbeResponseBodyType.ZIP.toString()))
			return ProbeResponseBodyType.ZIP;
		return null;
	}

	private List<CenternetCentralUnit> loadListCCUsFromCursor(Cursor c) {
		ArrayList<CenternetCentralUnit> ccus = new ArrayList<CenternetCentralUnit>();
		while (c.moveToNext()) {
			ccus.add(loadCCUsFromCursor(c));
		}
		return ccus;
	}

	/**
	 * load a ccu from a cursor which has already pointed to the
	 * exact place
	 */
	private CenternetCentralUnit loadCCUsFromCursor(Cursor c) {
		CenternetCentralUnit ccu = new CenternetCentralUnit();
		ccu.set_id(c.getInt(c
				.getColumnIndex(DBConstant.CENTERNETCENTRALUNIT_COLUMN_ID)));
		ccu.setBSSID(c.getString(c
				.getColumnIndex(DBConstant.CENTERNETCENTRALUNIT_COLUMN_BSSID)));
		ccu.setCcuId(c.getInt(c
				.getColumnIndex(DBConstant.CENTERNETCENTRALUNIT_COLUMN_CCUID)));
		ccu
				.setPortalPacketVersion(c.getString(c
						.getColumnIndex(DBConstant.CENTERNETCENTRALUNIT_COLUMN_PPVERSIONS)));
		ccu
				.setBodyType(stringToBodyType(c.getString(c
						.getColumnIndex(DBConstant.CENTERNETCENTRALUNIT_COLUMN_BODY_TYPE))));

		ccu.setTitile(c.getString(c
				.getColumnIndex(DBConstant.CENTERNETCENTRALUNIT_COLUMN_TITLE)));
		return ccu;
	}

	/**
	 * query all CenternetCentralUnits, return cursor
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheCursor(String condition) {
		Cursor c = null;
		if (condition != null) {
			c = db.rawQuery("SELECT * FROM "
					+ DBConstant.CENTERNETCENTRALUNIT_TABLE_NAME + " where "
					+ condition, null);
		} else {
			c = db.rawQuery("SELECT * FROM "
					+ DBConstant.CENTERNETCENTRALUNIT_TABLE_NAME, null);
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
