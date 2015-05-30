package org.dlug.android.eyeunalarm.alarm;

import org.dlug.android.eyeunalarm.ActivityAlarmSetAbstract;

public class ActivityAlarmPlayTest extends ActivityAlarmPlayAbstract{

	@Override
	protected void init(){
// Sample Data
		title = "TestAlarm";
		alarmTime[0] = 12;
		alarmTime[1] = 12;
		snooze = 5;
		int type = 2;
		typeS = (type & 2) == 2;
		typeV = (type & 1) == 1;
		bellURI = "content://settings/system/ringtone";
		volume = 100;
		repeatBinary = 255;
		repeat = ActivityAlarmSetAbstract.parseRepeatBinary(repeatBinary);
		recogStrength = (Integer) 30;
// Sample Data End

	}
}
