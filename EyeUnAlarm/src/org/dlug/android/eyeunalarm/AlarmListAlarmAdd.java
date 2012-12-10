package org.dlug.android.eyeunalarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class AlarmListAlarmAdd extends AlarmListAlarmSet{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		titleAlarmSet.setText(R.string.title_add_alarm);
		btnConfirm.setText(R.string.btn_confirm);
		findViewById(R.id.btnConfirm).setOnClickListener(onClickConfirm);
	}
	
	public void onResume(){
		GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
		
		setTitle("Alarm");
		setAlarmTime(currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE));
		setSnooze(snooze);
		setTypeS(TRUE);
		setTypeV(TRUE);
		setVolume(100);
		setRepeat(127);
		setRecogStrength(5);

		super.onResume();
	}
	
	private OnClickListener onClickConfirm = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Map<String, Object> inputData = new HashMap<String,Object>();
			inputData.put(MyDbHelper.FIELD_ALARM_NAME, title);
			inputData.put(MyDbHelper.FIELD_HOURS, alarmTime[0]);
			inputData.put(MyDbHelper.FIELD_MINUTES, alarmTime[1]);
			inputData.put(MyDbHelper.FIELD_REPEAT, repeat);
			inputData.put(MyDbHelper.FIELD_SNOOZE, snooze);
			inputData.put(MyDbHelper.FIELD_ALERT_SONG, "");
			inputData.put(MyDbHelper.FIELD_ALERT_VOLUME, volume);
			inputData.put(MyDbHelper.FIELD_TYPE, (typeS * 2 + typeV));
			inputData.put(MyDbHelper.FIELD_RECOG_TIME, recogStrength);
			inputData.put(MyDbHelper.FIELD_ALERT_STATE, 1);
			myDb.setAlarm(inputData);
			Log.i("AlarmAdd", "DBAdded");
			Toast.makeText(AlarmListAlarmAdd.this, R.string.toast_added_alarm, Toast.LENGTH_SHORT).show();
			onBackPressed();

			listUpdate();
		}
	};
}
