package org.dlug.android.eyeunalarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

public abstract class AlarmListActivity extends TabNavigationActivity{
	protected static ListView alarmListView;
	protected static AlarmListAdapter alarmListAdapter;

	protected static int selectAlarmId = 0;

	protected void listUpdate(){
		Log.i("Update", "Run");
		alarmListData = myDb.getAlarmList();
		alarmListAdapter.setData(alarmListData);
		alarmListAdapter.notifyDataSetChanged();
		alarmListView.setSelection(0);
		
		alarmUpdate();
	}
	
	protected void alarmUpdate(){
	  	AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, org.dlug.android.eyeunalarm.alarm.AlarmReceiver.class);
	  	
		gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
		currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));

		currentYY = currentCalendar.get(Calendar.YEAR);
		currentMM = currentCalendar.get(Calendar.MONTH);
		currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);

	  	for(Map<String, Object> item: alarmListData){
	  		int mHour = (Integer) item.get(MyDbHelper.FIELD_HOURS);
	  		int mMinute = (Integer)item.get(MyDbHelper.FIELD_MINUTES);

			gregorianCalendar.set(currentYY, currentMM, currentDD, mHour, mMinute, 00);
	  		
    	  	if(gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
    	  		gregorianCalendar.set(currentYY, currentMM, currentDD+1, mHour, mMinute, 0);
    	  		Log.i("TAG",gregorianCalendar.getTimeInMillis()+":");
    	  	} else {
    	  		gregorianCalendar.set(currentYY, currentMM, currentDD, mHour, mMinute, 0);
    	  	}
    	  	
    	  	long alarmTime = gregorianCalendar.getTimeInMillis();

    	  	int dbId = (Integer) item.get(MyDbHelper.FIELD_ID);
    	  	
    	  	int alertState = (Integer) item.get(MyDbHelper.FIELD_ALERT_STATE);
			
    	  	if(alertState == 1){
				PendingIntent pIntent = PendingIntent.getBroadcast(this, dbId, intent,
																	PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pIntent);
    	  	} else {
    			PendingIntent pIntent = PendingIntent.getBroadcast(this, dbId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    			alarmManager.cancel(pIntent);
    	  	}
	  	}
	}
	
	public void onActivityResultTrigger(int requestCode, int resultCode, Intent data){
		onActivityResult(requestCode, resultCode, data);
	}
}