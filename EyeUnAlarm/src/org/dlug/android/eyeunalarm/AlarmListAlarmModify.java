package org.dlug.android.eyeunalarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AlarmListAlarmModify extends AlarmListAlarmSet{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_setting);

		//TODO: GetDB
		/*
		setTitle("Alarm");
		setAlarmTime(currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE));
		setSnooze(snooze);
		setTypeS(TRUE);
		setTypeV(TRUE);
		setVolume(100);
		setRepeat(127);
		setRecogStrength(5);
		 */
		
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
