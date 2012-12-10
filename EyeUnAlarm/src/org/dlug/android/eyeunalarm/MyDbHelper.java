package org.dlug.android.eyeunalarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDbHelper extends SQLiteOpenHelper{
	// SET DB Feature =========================
	public static final String DB_FILE_NAME = "eyeunclock.db";
	public static final String TABLE_NAME = "eyeunclock";

	public static final String[] FIELD_NAMES = {
		"_id", 
		"alarm_name", 
		"hours", 
		"minutes", 
		"repeat", 
		"snooze", 
		"alert_song", 
		"type", 
		"recog_time", 
		"alert_state"
	};
	
	public static final String[] FIELD_TYPES = {
		"INTEGER PRIMARY KEY AUTOINCREMENT",
		"STRING", 
		"INTEGER NOT NULL", 
		"INTEGER NOT NULL", 
		"INTEGER NOT NULL", 
		"INTEGER NOT NULL", 
		"INTEGER NOT NULL", 
		"INTEGER NOT NULL", 
		"INTEGER NOT NULL",
		"INTEGER NOT NULL"
	};

// Codes =========================
	
	private SQLiteDatabase oSQLiteDB;
	
	public MyDbHelper(Context context, int version) {
		super(context, TABLE_NAME, null, version);

		oSQLiteDB = this.getReadableDatabase();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "CREATE TABLE " + TABLE_NAME + " (";
		
		for(int i = 0; i < FIELD_NAMES.length; i++){
			query += FIELD_NAMES[i] + " " + FIELD_TYPES[i] + ", ";
		}
		
		Log.d("DBQuery - Create", query);
		
		query = query.substring(0, query.length() - 2) + ");";
		db.execSQL(query);
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	public List<Map<String, Object>> getAlarmList(){

		String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY alert_state DESC, hours ASC, minutes ASC;";
		
		Log.d("DBQuery - getAlarmList", query);
		
		Cursor cursor = oSQLiteDB.rawQuery(query, null);
		
		int cnt = cursor.getCount();
		
		Log.i("DB GetList Count", cnt + "");
		
		cursor.moveToFirst();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(cnt);
		
		for(int i = 0; i < cnt; i++, cursor.moveToNext()){
			Map<String, Object> rowData = new HashMap<String, Object>(10);
		
			for(int j = 0; j < FIELD_NAMES.length; j++){
				if(FIELD_TYPES[j].subSequence(0, 3).equals("INT"))
					rowData.put(FIELD_NAMES[j], cursor.getInt(j));
				else if(FIELD_TYPES[j].subSequence(0, 3).equals("STR"))
					rowData.put(FIELD_NAMES[j], cursor.getString(j));
			}
			result.add(rowData);
		}
		
		return result;
	}
	
	public Map<String, Object> getAlarm(int id){
		Map<String, Object> rowData = new HashMap<String, Object>(10);
		
		String query = "SELECT * FROM " + TABLE_NAME + "WHERE _id = " + id + ";";
		
		Log.d("DBQuery - getAlarm", query);
		
		Cursor cursor = oSQLiteDB.rawQuery(query, null);
		
		cursor.moveToFirst();
		
		if(cursor.getCount() != 0){
			for(int j = 0; j < FIELD_NAMES.length; j++){
				if(FIELD_TYPES[j].subSequence(0, 3).equals("INT"))
					rowData.put(FIELD_NAMES[j], cursor.getInt(j));
				else if(FIELD_TYPES[j].subSequence(0, 3).equals("STR"))
					rowData.put(FIELD_NAMES[j], cursor.getString(j));
			}
		}
		
		return rowData;
	}
	
	public void setAlarm(Map<String, Object> alarm){
		String query = "INSERT INTO " + TABLE_NAME + "(";
		
		for(int i = 1; i < FIELD_NAMES.length; i++){
			query += FIELD_NAMES[i] + ", ";
		}
		
		query = query.substring(0, query.length() - 2) + ") VALUES (";
		
		for(int i = 1; i < FIELD_NAMES.length; i++){
			query += "'" + alarm.get(FIELD_NAMES[i]) + "', ";
		}
		
		query = query.substring(0, query.length() - 2) + ");";
		Log.d("DBQuery - setAlarm", query);
		
		oSQLiteDB.execSQL(query);
	}

	public void update(Map<String, Object> updateData, Map<String, Object> filter) {
		String query = "UPDATE " + TABLE_NAME + " SET ";
		for(String key: updateData.keySet()){
			query += key + " = '" + updateData.get(key) + "', "; 
		}
		query = query.substring(0, query.length() - 2);
		
		query += " WHERE ";
		
		for(String key: filter.keySet()){
			query += key + " = '" + filter.get(key) + "', "; 
		}
		query = query.substring(0, query.length() - 2);
		
		Log.d("DBQuery - update", query);
		
		oSQLiteDB.execSQL(query);
	}
	
	public void delete(Map<String, Object> filter){
		String query = "UPDATE FROM " + TABLE_NAME + " WHERE ";
		
		for(String key: filter.keySet()){
			query += key + " = '" + filter.get(key) + "', "; 
		}
		query = query.substring(0, query.length() - 2);
		
		Log.d("DBQuery - delete", query);
		
		oSQLiteDB.execSQL(query);
	}
}