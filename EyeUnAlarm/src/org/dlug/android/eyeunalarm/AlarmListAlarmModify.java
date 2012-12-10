package org.dlug.android.eyeunalarm;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class AlarmListAlarmModify extends AlarmListAlarmSet{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_setting);

		Map<String, Object> thisAlarmData = myDb.getAlarm(selectItemPosition);
		
		setTitle((String) thisAlarmData.get(myDb.FIELD_ALARM_NAME));
		setAlarmTime((Integer) thisAlarmData.get(myDb.FIELD_HOURS), (Integer) thisAlarmData.get(myDb.FIELD_MINUTES));
		setSnooze((Integer) thisAlarmData.get(myDb.FIELD_SNOOZE));
		setType((Integer) thisAlarmData.get(myDb.FIELD_TYPE));
		setVolume((Integer) thisAlarmData.get(myDb.FIELD_ALERT_VOLUME));
		setRepeat((Integer) thisAlarmData.get(myDb.FIELD_REPEAT));
		setRecogStrength((Integer) thisAlarmData.get(myDb.FIELD_RECOG_TIME));
		
		btnConfirm.setText(R.string.update);
	}
	
	private OnClickListener onClickConfirm = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Map<String, Object> inputData = new HashMap<String,Object>();
			inputData.put("alarm_name", title);
			inputData.put("hours", alarmTime[0]);
			inputData.put("minutes", alarmTime[1]);
			inputData.put("repeat", repeat);
			inputData.put("snooze", snooze);
			inputData.put("alert_song", "");
			inputData.put("type", (typeS * 2 + typeV));
			inputData.put("recog_time", recogStrength);
			inputData.put("alert_state", 1);
			myDb.setAlarm(inputData);
			Log.i("AlarmAdd", "DBAdded");
			
			onBackPressed();

			listUpdate();
		}
	};
}
