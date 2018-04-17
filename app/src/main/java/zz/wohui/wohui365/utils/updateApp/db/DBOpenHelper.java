package zz.wohui.zz.utils.updateApp.db;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import zz.wohui.zz.utils.updateApp.util.LogUtil;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = "DBOpenHelper";

	public DBOpenHelper(Context context) {
		super(context, DBManager.DATABASE_NAME, null, DBManager.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE IF NOT EXISTS ").append(DBManager.DOWNLOADLOG_TABLENAME).append("(")
		.append(DBManager.DOWNLOADLOG_LOGID).append(" integer primary key autoincrement,")
		.append(DBManager.DOWNLOADLOG_DOWNLOADPATH).append(" varchar(500),")
		.append(DBManager.DOWNLOADLOG_THREADID).append(" INTEGER,")
		.append(DBManager.DOWNLOADLOG_LENGTHDOWNLOADED ).append(" INTEGER)");
		LogUtil.d(TAG, sql.toString());
		db.execSQL(sql.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DBManager.DOWNLOADLOG_TABLENAME);
		onCreate(db);
	}
}
