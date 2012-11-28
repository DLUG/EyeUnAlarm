package org.dlug.android.facealarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
	public static final String KEY_TIME_HOUR = "hour";
	public static final String KEY_TIME_MIN = "minutes";
	public static final String KEY_REPEAT = "repeat";
	public static final String KEY_SNOOZE = "snooze";
	public static final String KEY_SOUND = "sound";
	public static final String KEY_TYPE_S = "type_s";
	public static final String KEY_TYPE_V = "type_v";
	public static final String KEY_ROWID = "_id";
	
	public static final int FIND_BY_TIME_HOUR = 0;
	public static final int FIND_BY_TIME_MIN = 1;
	public static final int FIND_BY_REPEAT = 2;
	public static final int FIND_BY_SNOOZE = 3;
	public static final int FIND_BY_SOUND = 4;
	public static final int FIND_BY_TYPE_S = 5;
	public static final int FIND_BY_TYPE_V = 6;
	
	private static final String TAG = "DbAdapter";
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb; // 데이터베이스를 저장
	
	private static final String DATABASE_CREATE =
			"CREATE TABLE data (_id integer primary key autoincrement,"+
			"hour integer not null, minutes integer not null, repeat text not null, snooze integer not null, sound integer not null, type_s integer not null, type_v integer not null)";
	
	private static final String DATABASE_NAME = "setdata.db";
	private static final String DATABASE_TABLE = "data";
	private static final int DATABASE_VERSION = 2;
	
	private final Context mCtx;
	
	private class DatabaseHelper extends SQLiteOpenHelper{
		
		public DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			
		}

		public void onCreate(SQLiteDatabase db){
			db.execSQL(DATABASE_CREATE);
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			Log.w(TAG, "Upgrading db from version"+ oldVersion + " to" +
					newVersion +", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS data");
			onCreate(db);
		}
		
	}
	
	public DbAdapter(Context ctx){
		this.mCtx = ctx;
		mDbHelper = new DatabaseHelper(mCtx);
	}
	
	public DbAdapter open() throws SQLException{
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		mDbHelper.close();
	}
	
	public long createBook(int hour,  int minutes, String repeat, int snooze, int sound, int type_s, int type_v){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TIME_HOUR,hour);
		initialValues.put(KEY_TIME_MIN,minutes);
		initialValues.put(KEY_REPEAT,repeat);
		initialValues.put(KEY_SNOOZE,snooze);
		initialValues.put(KEY_SOUND,sound);
		initialValues.put(KEY_TYPE_S,type_s);
		initialValues.put(KEY_TYPE_V,type_v);
		
		return mDb.insert(DATABASE_TABLE,null,initialValues);
	}
	
	public boolean deleteBook(long rowID){
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowID, null) > 0;
		
	}
	
	public Cursor fetchAllBooks(){
		return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TIME_HOUR, KEY_TIME_MIN, KEY_REPEAT, KEY_SNOOZE, KEY_SOUND, KEY_TYPE_S, KEY_TYPE_V}, null, null, null, null, null);
		
	}
	
	public Cursor fetchBook(long rowID) throws SQLException{
		Cursor mCursor =
			mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TIME_HOUR, KEY_TIME_MIN, KEY_REPEAT, KEY_SNOOZE, KEY_SOUND, KEY_TYPE_S, KEY_TYPE_V}, KEY_ROWID + "=" + rowID, null, null, null, null, null);
		if(mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}
		
	public boolean updateBook(long rowID, int hour, int minutes, String repeat, int snooze, int sound, int type_s, int type_v){
		ContentValues args = new ContentValues();
		args.put(KEY_TIME_HOUR, hour);
		args.put(KEY_TIME_MIN, minutes);
		args.put(KEY_REPEAT, repeat);
		args.put(KEY_SNOOZE, snooze);
		args.put(KEY_SOUND, sound);
		args.put(KEY_TYPE_S, type_s);
		args.put(KEY_TYPE_V, type_v);
		
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowID, null) > 0;
	}

	public void deleteAll() {
		// TODO Auto-generated method stub
		mDb.delete(DATABASE_TABLE,null,null);
	}
	
	
}
