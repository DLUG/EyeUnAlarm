package org.dlug.android.eyeunalarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.dlug.android.eyeunalarm.alarm.ReceiverAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlarmController{
	// SET DB Feature =========================
	private static final String DB_FILE_NAME = "eyeunclock.db";
	private static final String TABLE_NAME = "eyeunclock";

	public static final String FIELD_ID = "_id";
	public static final String FIELD_ALARM_NAME = "alarm_name";
	public static final String FIELD_HOURS = "hours";
	public static final String FIELD_MINUTES = "minutes";
	public static final String FIELD_REPEAT = "repeat";
	public static final String FIELD_SNOOZE = "snooze";
	public static final String FIELD_ALERT_SONG = "alert_song";
	public static final String FIELD_ALERT_VOLUME = "alert_volume";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_RECOG_TIME = "recog_time";
	public static final String FIELD_ALERT_STATE = "alert_state";

	private static final String[] FIELD_INFO = {
		FIELD_ID, "INTEGER PRIMARY KEY AUTOINCREMENT",
		FIELD_ALARM_NAME, "STRING", 
		FIELD_HOURS, "INTEGER NOT NULL",
		FIELD_MINUTES, "INTEGER NOT NULL",
		FIELD_REPEAT, "INTEGER NOT NULL",
		FIELD_SNOOZE, "INTEGER NOT NULL",
		FIELD_ALERT_SONG, "STRING",
		FIELD_ALERT_VOLUME,"INTEGER NOT NULL",
		FIELD_TYPE, "INTEGER NOT NULL",
		FIELD_RECOG_TIME, "INTEGER NOT NULL",
		FIELD_ALERT_STATE, "INTEGER NOT NULL"
	};
	
	protected static GregorianCalendar gregorianCalendar;
	protected static GregorianCalendar currentCalendar;
	protected static List<Map<String, Object>> alarmListData;

	protected static int currentYY;
	protected static int currentMM;
	protected static int currentDD;

// Codes =========================
	
	private static SQLiteOpenHelper sqlLiteOpenHelper;
	private static SQLiteDatabase oSQLiteDB;
	private static Context context;
	private static AlarmManager alarmManager;

	public static void init(Context context) {
		AlarmController.context = context;
		alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		PackageInfo packageInfo = null;		
		try {
			PackageManager pm = context.getPackageManager();
			packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		sqlLiteOpenHelper = new SQLiteOpenHelper(context, DB_FILE_NAME, null, packageInfo.versionCode) {
			
			@Override
			public void onCreate(SQLiteDatabase db) {
				String query = "CREATE TABLE " + TABLE_NAME + " (";
				
				for(int i = 0; i < FIELD_INFO.length; i += 2){
					query += FIELD_INFO[i] + " " + FIELD_INFO[i + 1] + ", ";
				}
				
				query = query.substring(0, query.length() - 2) + ");";

				Log.d("DBQuery - Create", query);
				db.execSQL(query);
			}
			
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
		};

		oSQLiteDB = sqlLiteOpenHelper.getReadableDatabase();
	}

	
	
	public static List<AlarmData> getAlarmList(){

		String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY alert_state DESC, hours ASC, minutes ASC;";
		
		Log.d("DBQuery - getAlarmList", query);
		
		Cursor cursor = oSQLiteDB.rawQuery(query, null);
		
		int cnt = cursor.getCount();
		
		Log.i("DB GetList Count", cnt + "");
		
		cursor.moveToFirst();
		List<AlarmData> result = new ArrayList<AlarmData>(cnt);
		
		for(int i = 0; i < cnt; i++, cursor.moveToNext()){
			AlarmData tmpData = new AlarmData();
		
			tmpData._id = cursor.getInt(cursor.getColumnIndex(FIELD_ID));
			tmpData.alarmName = cursor.getString(cursor.getColumnIndex(FIELD_ALARM_NAME));
			tmpData.alertSong = cursor.getString(cursor.getColumnIndex(FIELD_ALERT_SONG));
			tmpData.alertState = cursor.getInt(cursor.getColumnIndex(FIELD_ALERT_STATE));
			tmpData.alertVolume = cursor.getInt(cursor.getColumnIndex(FIELD_ALERT_VOLUME));
			tmpData.hours = cursor.getInt(cursor.getColumnIndex(FIELD_HOURS));
			tmpData.minutes = cursor.getInt(cursor.getColumnIndex(FIELD_MINUTES));
			tmpData.recogTime = cursor.getInt(cursor.getColumnIndex(FIELD_RECOG_TIME));
			tmpData.repeat = cursor.getInt(cursor.getColumnIndex(FIELD_REPEAT));
			tmpData.snooze = cursor.getInt(cursor.getColumnIndex(FIELD_SNOOZE));
			tmpData.type = cursor.getInt(cursor.getColumnIndex(FIELD_TYPE));
			
			result.add(tmpData);
		}
		
		return result;
	}
	
	public static AlarmData getAlarm(int idx){
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE _id = " + idx + ";";
		
		Log.d("DBQuery - getAlarm", query);
		
		Cursor cursor = oSQLiteDB.rawQuery(query, null);
		
		cursor.moveToFirst();
		
		AlarmData tmpData = new AlarmData();
		
		tmpData._id = cursor.getInt(cursor.getColumnIndex(FIELD_ID));
		tmpData.alarmName = cursor.getString(cursor.getColumnIndex(FIELD_ALARM_NAME));
		tmpData.alertSong = cursor.getString(cursor.getColumnIndex(FIELD_ALERT_SONG));
		tmpData.alertState = cursor.getInt(cursor.getColumnIndex(FIELD_ALERT_STATE));
		tmpData.alertVolume = cursor.getInt(cursor.getColumnIndex(FIELD_ALERT_VOLUME));
		tmpData.hours = cursor.getInt(cursor.getColumnIndex(FIELD_HOURS));
		tmpData.minutes = cursor.getInt(cursor.getColumnIndex(FIELD_MINUTES));
		tmpData.recogTime = cursor.getInt(cursor.getColumnIndex(FIELD_RECOG_TIME));
		tmpData.repeat = cursor.getInt(cursor.getColumnIndex(FIELD_REPEAT));
		tmpData.snooze = cursor.getInt(cursor.getColumnIndex(FIELD_SNOOZE));
		tmpData.type = cursor.getInt(cursor.getColumnIndex(FIELD_TYPE));
		
		return tmpData;
	}
	
/*
	public static AlarmData getAlarm(Map<String, Object> filter){
		Map<String, Object> rowData = new HashMap<String, Object>(10);
		
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE ";
		
		for(String key: filter.keySet()){
			query += key + " = '" + filter.get(key) + "' AND "; 
		}
		query = query.substring(0, query.length() - 5);
		
		Log.d("DBQuery - getAlarm", query);
		
		Cursor cursor = oSQLiteDB.rawQuery(query, null);
		
		cursor.moveToFirst();
		
		AlarmData tmpData = new AlarmData();
		
		tmpData._id = cursor.getInt(cursor.getColumnIndex(FIELD_ID));
		tmpData.alarmName = cursor.getString(cursor.getColumnIndex(FIELD_ALARM_NAME));
		tmpData.alertSong = cursor.getString(cursor.getColumnIndex(FIELD_ALERT_SONG));
		tmpData.alertState = cursor.getInt(cursor.getColumnIndex(FIELD_ALERT_STATE));
		tmpData.alertVolume = cursor.getInt(cursor.getColumnIndex(FIELD_ALERT_VOLUME));
		tmpData.hours = cursor.getInt(cursor.getColumnIndex(FIELD_HOURS));
		tmpData.minutes = cursor.getInt(cursor.getColumnIndex(FIELD_MINUTES));
		tmpData.recogTime = cursor.getInt(cursor.getColumnIndex(FIELD_RECOG_TIME));
		tmpData.repeat = cursor.getInt(cursor.getColumnIndex(FIELD_REPEAT));
		tmpData.snooze = cursor.getInt(cursor.getColumnIndex(FIELD_SNOOZE));
		tmpData.type = cursor.getInt(cursor.getColumnIndex(FIELD_TYPE));
		
		return tmpData;
	}
*/
	
	public static void addAlarm(AlarmData alarm){
		ContentValues tmpValue = new ContentValues();
		tmpValue.put(FIELD_ALARM_NAME, alarm.alarmName);
		tmpValue.put(FIELD_ALERT_SONG, alarm.alertSong);
		tmpValue.put(FIELD_ALERT_STATE, alarm.alertState);
		tmpValue.put(FIELD_ALERT_VOLUME, alarm.alertVolume);
		tmpValue.put(FIELD_HOURS, alarm.hours);
		tmpValue.put(FIELD_MINUTES, alarm.minutes);
		tmpValue.put(FIELD_RECOG_TIME, alarm.recogTime);
		tmpValue.put(FIELD_REPEAT, alarm.repeat);
		tmpValue.put(FIELD_SNOOZE, alarm.snooze);
		tmpValue.put(FIELD_TYPE, alarm.type);
		long newIdx = oSQLiteDB.insertOrThrow(TABLE_NAME, null, tmpValue);
		
		alarm._id = (int) newIdx;
		setAlarmManager(alarm);
	}

	public static void updateAlarm(Map<String, Object> updateData, int idx) {
		String query = "UPDATE " + TABLE_NAME + " SET ";
		for(String key: updateData.keySet()){
			query += key + " = '" + updateData.get(key) + "', "; 
		}
		query = query.substring(0, query.length() - 2);
		
		query += " WHERE _id='" + idx + "';";
		
		Log.d("DBQuery - update", query);
		
		oSQLiteDB.execSQL(query);
		
		setAlarmManager(getAlarm(idx));
	}
	
	public static void deleteAlarm(int idx){
		String query = "DELETE FROM " + TABLE_NAME + " WHERE _id='" + idx + "';";
		
		Log.d("DBQuery - delete", query);
		
		oSQLiteDB.execSQL(query);
		cancelAlarmManager(idx);
	}
	
	public static void disableAlarm(int idx){
		Map<String, Object> tmpData = new HashMap<String, Object>();
		tmpData.put(FIELD_ALERT_STATE, 0);
		updateAlarm(tmpData, idx);
		cancelAlarmManager(idx);
	}

	public static void enableAlarm(int idx){
		Map<String, Object> tmpData = new HashMap<String, Object>();
		tmpData.put(FIELD_ALERT_STATE, 1);
		updateAlarm(tmpData, idx);
		
		setAlarmManager(getAlarm(idx));
	}
	
	private static void setAlarmManager(AlarmData alarm){
		gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
		currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));

		currentYY = currentCalendar.get(Calendar.YEAR);
		currentMM = currentCalendar.get(Calendar.MONTH);
		currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);

		gregorianCalendar.set(currentYY, currentMM, currentDD, alarm.hours, alarm.minutes, 00);
		
	  	if(gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
	  		gregorianCalendar.set(currentYY, currentMM, currentDD+1, alarm.hours, alarm.minutes, 0);
	  	} else {
	  		gregorianCalendar.set(currentYY, currentMM, currentDD, alarm.hours, alarm.minutes, 0);
	  	}
	  	
	  	long alarmTime = gregorianCalendar.getTimeInMillis();

	  	Intent intent = new Intent(context, ReceiverAlarm.class);
	  	intent.putExtra("dbIdx", alarm._id);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, alarm._id, intent, 0);
		alarmManager.cancel(pIntent);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pIntent);
	}
	
	public static void setAlarmManagerSnooze(AlarmData alarm){
		gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
		currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));

		currentYY = currentCalendar.get(Calendar.YEAR);
		currentMM = currentCalendar.get(Calendar.MONTH);
		currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);

		gregorianCalendar.set(currentYY, currentMM, currentDD, alarm.hours, alarm.minutes, 00);
		
	  	if(gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
	  		gregorianCalendar.set(currentYY, currentMM, currentDD+1, alarm.hours, alarm.minutes, 0);
	  	} else {
	  		gregorianCalendar.set(currentYY, currentMM, currentDD, alarm.hours, alarm.minutes, 0);
	  	}
	  	
	  	long alarmTime = gregorianCalendar.getTimeInMillis();

	  	Intent intent = new Intent(context, ReceiverAlarm.class);
	  	intent.putExtra("dbIdx", alarm._id);
	  	intent.putExtra("snooze", true);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, alarm._id * -1, intent, 0);
		alarmManager.cancel(pIntent);
		alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pIntent);
	}
	
	public static void cancelAlarmManager(int idx){
		Intent intent = new Intent(context, ReceiverAlarm.class);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, idx, intent, 0);
		alarmManager.cancel(pIntent);		
	}
	
	public static void resetAlarm(){
		List<AlarmData> tmpAlarmList = getAlarmList();
		
		for(AlarmData alarm: tmpAlarmList){
			setAlarmManager(alarm);
		}
	}
	
	public static class AlarmData {
		public int 	_id;
		public String 	alarmName;
		public int 	hours;
		public int 	minutes;
		public int 	repeat;
		public int 	snooze;
		public String	alertSong;
		public int 	alertVolume;
		public int 	type;
		public int 	recogTime;
		public int 	alertState;
	}
}