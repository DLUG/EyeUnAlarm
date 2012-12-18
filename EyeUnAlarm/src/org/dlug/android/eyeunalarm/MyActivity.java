package org.dlug.android.eyeunalarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

public abstract class MyActivity extends Activity{
	protected static MyDbHelper myDb = null;
	
	protected static GregorianCalendar gregorianCalendar;
	protected static GregorianCalendar currentCalendar;
	protected static List<Map<String, Object>> alarmListData;

	protected static int currentYY;
	protected static int currentMM;
	protected static int currentDD;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		if(myDb == null){

			PackageManager pm = this.getPackageManager();
			PackageInfo packageInfo = null;
			try {
				packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			int VERSION = packageInfo.versionCode;

			
			myDb = new MyDbHelper(this, VERSION);
			
			
			gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
			currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));

			currentYY = currentCalendar.get(Calendar.YEAR);
			currentMM = currentCalendar.get(Calendar.MONTH);
			currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);
		}
	}
	
	public MyDbHelper getMyDb(){
		return myDb;
	}
	
	protected void alarmSet(Context context, int positionId, boolean alertState){
	  	AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, org.dlug.android.eyeunalarm.alarm.AlarmReceiver.class);
		
		Map<String, Object> item = alarmListData.get(positionId);
		
		int mHour = (Integer) item.get(MyDbHelper.FIELD_HOURS);
  		int mMinute = (Integer)item.get(MyDbHelper.FIELD_MINUTES);
  		
		gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
		currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));

		currentYY = currentCalendar.get(Calendar.YEAR);
		currentMM = currentCalendar.get(Calendar.MONTH);
		currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);

		gregorianCalendar.set(currentYY, currentMM, currentDD, mHour, mMinute, 00);
		
	  	if(gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
	  		gregorianCalendar.set(currentYY, currentMM, currentDD+1, mHour, mMinute, 0);
	  	} else {
	  		gregorianCalendar.set(currentYY, currentMM, currentDD, mHour, mMinute, 0);
	  	}
	  	
	  	long alarmTime = gregorianCalendar.getTimeInMillis();

	  	int dbId = (Integer) item.get(MyDbHelper.FIELD_ID);
	  	
	  	if(alertState){
			PendingIntent pIntent = PendingIntent.getBroadcast(context, dbId, intent,
																PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pIntent);
	  	} else {
			PendingIntent pIntent = PendingIntent.getBroadcast(context, dbId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			alarmManager.cancel(pIntent);
	  	}
	}
}