package com.TroyEmpire.Centernet.DB;

import com.TroyEmpire.Centernet.Constant.ConfigConstant;
import com.TroyEmpire.Centernet.Constant.DBConstant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CenternetDBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;

	public CenternetDBHelper(Context context) {
		// CursorFactory设置为null,使用默认值
		super(context, ConfigConstant.CENTERNET_DB_NAME, null, DATABASE_VERSION);
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 仅仅存储AP的几个field ,并不是全部数据
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.ACCESSPOINT_TABLE_NAME + "("
				+ DBConstant.ACCESSPOINT_COLUMN_BSSID + " TEXT PRIMARY KEY,"
				+ DBConstant.ACCESSPOINT_COLUMN_IGNORED + " INTEGER " + ")");
		// 存储CCU
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.CENTERNETCENTRALUNIT_TABLE_NAME + "("
				+ DBConstant.CENTERNETCENTRALUNIT_COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DBConstant.CENTERNETCENTRALUNIT_COLUMN_BSSID + " TEXT,"
				+ DBConstant.CENTERNETCENTRALUNIT_COLUMN_CCUID + " INTEGER,"
				+ DBConstant.CENTERNETCENTRALUNIT_COLUMN_PPVERSIONS + " TEXT, "
				+ DBConstant.CENTERNETCENTRALUNIT_COLUMN_TITLE + " TEXT, "
				+ DBConstant.CENTERNETCENTRALUNIT_COLUMN_BODY_TYPE + "  TEXT "
				+ ")");
		// 存储Centernet installed applications
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.CENTERNET_INSTALLED_APP_TABLE_NAME
				+ "("
				+ DBConstant.CENTERNET_INSTALLED_APP_COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APPNAME
				+ " TEXT, "
				+ DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APPVERSION
				+ " TEXT, "
				+ DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APP_PACKAGENAME
				+ " TEXT, "
				+ DBConstant.CENTERNET_INSTALLED_APP_COLUMN_APP_LUANCHER_ACTIVITY_NAME
				+ " TEXT" + ")");
	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("ALTER TABLE person ADD COLUMN other STRING");

	}
}