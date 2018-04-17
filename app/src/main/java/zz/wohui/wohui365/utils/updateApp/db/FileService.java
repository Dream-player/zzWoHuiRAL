package zz.wohui.wohui365.utils.updateApp.db;

import java.util.HashMap;
import java.util.Map;




import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import zz.wohui.wohui365.utils.updateApp.util.LogUtil;

public class FileService {
	private static final String TAG = "FileService";
	private DBOpenHelper openHelper;

	public FileService(Context context) {
		openHelper = new DBOpenHelper(context);
	}

	/**
	 * 获取特定URI的每条线程已经下载的文件长度
	 * @param path
	 * @return
	 */
	public Map<Integer, Integer> getData(String path) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(DBManager.DOWNLOADLOG_THREADID).append(",")
		.append(DBManager.DOWNLOADLOG_LENGTHDOWNLOADED ).append(" from ")
		.append(DBManager.DOWNLOADLOG_TABLENAME).append(" where ")
		.append(DBManager.DOWNLOADLOG_DOWNLOADPATH).append(" = ?");
		LogUtil.d(TAG, sql.toString());
		Cursor cursor = db.rawQuery(sql.toString(), new String[] { path });
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		while (cursor.moveToNext()) {
			data.put(cursor.getInt(0), cursor.getInt(1));
			data.put(cursor.getInt(cursor.getColumnIndex(DBManager.DOWNLOADLOG_THREADID)),
					cursor.getInt(cursor.getColumnIndex(DBManager.DOWNLOADLOG_LENGTHDOWNLOADED )));
		}
		cursor.close();
		db.close();
		return data;
	}

	/**
	 * 保存每条线程已经下载的文件长度
	 * @param path 下载的路径
	 * @param map 现在的id和已经下载的长度的集合
	 */
	public void save(String path, Map<Integer, Integer> map) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			StringBuffer sql = new StringBuffer();
			for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
				sql.append("insert into ").append(DBManager.DOWNLOADLOG_TABLENAME)
				.append("(").append(DBManager.DOWNLOADLOG_DOWNLOADPATH).append(",")
				.append(DBManager.DOWNLOADLOG_THREADID).append(",")
				.append(DBManager.DOWNLOADLOG_LENGTHDOWNLOADED ).append(")")
				.append("values(?,?,?)");
				db.execSQL(sql.toString(), new Object[] { path, entry.getKey(), entry.getValue()});
				sql.delete(0, sql.length());
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		db.close();
	}

	public void update(String path, int threadId, int position) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(DBManager.DOWNLOADLOG_TABLENAME).append(" set ")
		.append(DBManager.DOWNLOADLOG_LENGTHDOWNLOADED ).append("=?").append(" where ")
		.append(DBManager.DOWNLOADLOG_DOWNLOADPATH).append("=? and ")
		.append(DBManager.DOWNLOADLOG_THREADID).append("=?");
		db.execSQL(sql.toString(), new Object[] { position, path, threadId });
	}

	public void delete(String path) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(DBManager.DOWNLOADLOG_TABLENAME)
		.append(" where ").append(DBManager.DOWNLOADLOG_DOWNLOADPATH).append("=?");
		db.execSQL(sql.toString(), new Object[] { path });
		db.close();
	}

}
