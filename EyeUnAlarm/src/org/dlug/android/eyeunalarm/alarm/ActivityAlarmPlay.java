package org.dlug.android.eyeunalarm.alarm;

import org.dlug.android.eyeunalarm.ActivityAlarmSetAbstract;
import org.dlug.android.eyeunalarm.AlarmController;
import org.dlug.android.eyeunalarm.AlarmController.AlarmData;

import android.util.Log;

public class ActivityAlarmPlay extends ActivityAlarmPlayAbstract{

	@Override
	protected void init(){
		int dbIdx = getIntent().getIntExtra("dbIdx", -1);
		if(dbIdx == -1){
			Log.e("AlarmPlay", "Not Exist Db Idx");
			finish();
		}
		
		AlarmData currentAlarm = AlarmController.getAlarm(dbIdx);
		
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
		
	}
}
