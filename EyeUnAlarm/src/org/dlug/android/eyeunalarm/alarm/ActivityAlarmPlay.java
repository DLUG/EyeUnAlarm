package org.dlug.android.eyeunalarm.alarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.dlug.android.eyeunalarm.ActivityAlarmSetAbstract;
import org.dlug.android.eyeunalarm.AlarmController;
import org.dlug.android.eyeunalarm.R;
import org.dlug.android.eyeunalarm.AlarmController.AlarmData;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ActivityAlarmPlay extends ActivityAlarmPlayAbstract{

	private AlarmData currentAlarm;
	
	@Override
	protected boolean prepareData(){
		int dbIdx = getIntent().getIntExtra("dbIdx", -1);
		
		if(dbIdx == -1){
			Log.e("AlarmPlay", "Not Exist Db Idx");
			return false;
		}
		
		currentAlarm = AlarmController.getAlarm(dbIdx);
		
		if(currentAlarm.alertState == 0){
			return false;
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
			return false;
		
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Log.d("OnBackPressed", "Clicked");
	}
	
	@Override
	protected OnClickListener getButtonOnClickListener() {
		return new OnClickListener(){
			@Override
			public void onClick(View v) {
				currentAlarm.minutes = currentAlarm.minutes + snooze;
				if(currentAlarm.minutes >= 60){
					currentAlarm.minutes -= 60;
					currentAlarm.hours++;
				}
				
				if(currentAlarm.hours >= 24){
					currentAlarm.hours -= 24;
				}
				
				AlarmController.setAlarmManagerSnooze(currentAlarm);
				
				String tmpMessage = ActivityAlarmPlay.this.getString(R.string.toast_snooze).replace("{SnoozeTime}", String.valueOf(snooze));
				Toast.makeText(ActivityAlarmPlay.this, tmpMessage, Toast.LENGTH_SHORT).show();
				
				pass();
			}
		};
	}
	
	@Override
	protected void onPause() {
		if(!boolPassed){
			showNotification(this);
		}
			
		super.onPause();
	}
}
