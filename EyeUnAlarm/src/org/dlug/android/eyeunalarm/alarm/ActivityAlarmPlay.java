package org.dlug.android.eyeunalarm.alarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.dlug.android.eyeunalarm.ActivityAlarmSetAbstract;
import org.dlug.android.eyeunalarm.AlarmController;
import org.dlug.android.eyeunalarm.AlarmController.AlarmData;

import android.os.Bundle;
import android.util.Log;

public class ActivityAlarmPlay extends ActivityAlarmPlayAbstract{

	@Override
	protected void prepareData(){
		int dbIdx = getIntent().getIntExtra("dbIdx", -1);
		
		if(dbIdx == -1){
			Log.e("AlarmPlay", "Not Exist Db Idx");
			finish();
		}
		
		AlarmData currentAlarm = AlarmController.getAlarm(dbIdx);
		
		if(currentAlarm.alertState == 0){
			finish();
		}
		
		title = currentAlarm.alarmName;
		alarmTime[0] = currentAlarm.hours;
		alarmTime[1] = currentAlarm.minutes;
		snooze = currentAlarm.snooze;
		int type = currentAlarm.type;
		typeS = (type & 2) == 2;
		typeV = (type & 1) == 1;
		bellURI = currentAlarm.alertSong;
		volume = currentAlarm.alertVolume;
		repeatBinary = currentAlarm.repeat;
		repeat = ActivityAlarmSetAbstract.parseRepeatBinary(repeatBinary);
		recogStrength = currentAlarm.recogTime;

		GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
		int todayDayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
		if(!repeat[todayDayOfWeek - 1])
			finish();
	}
	
	@Override
	public void onBackPressed() {
		Log.d("OnBackPressed", "Clicked");
	}
}
