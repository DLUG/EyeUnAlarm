package org.dlug.android.eyeunalarm;

import java.util.HashMap;
import java.util.Map;

import org.dlug.android.eyeunalarm.R;
import org.dlug.android.eyeunalarm.AlarmController.AlarmData;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ActivityAlarmModify extends ActivityAlarmSetAbstract{
	private int selectedAlarmId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		titleAlarmSet.setText(R.string.title_modify_alarm);
		btnConfirm.setText(R.string.btn_update);
		
		findViewById(R.id.btnConfirm).setOnClickListener(onClickConfirm);
		selectedAlarmId = this.getIntent().getIntExtra("selectedAlarmId", 0);

		AlarmData thisAlarmData = AlarmController.getAlarm(selectedAlarmId);
		
		setTitle(thisAlarmData.alarmName);
		setAlarmTime(thisAlarmData.hours, thisAlarmData.minutes);
		setSnooze(thisAlarmData.snooze);
		setType(thisAlarmData.type);
		setBellURI(Uri.parse(thisAlarmData.alertSong));
		setVolume(thisAlarmData.alertVolume);
		setRepeat(thisAlarmData.repeat);
		setRecogStrength(thisAlarmData.recogTime);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	private OnClickListener onClickConfirm = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Map<String, Object> inputData = new HashMap<String,Object>(10);
			inputData.put("alarm_name", title);
			inputData.put("hours", alarmTime[0]);
			inputData.put("minutes", alarmTime[1]);
			inputData.put("repeat", repeat);
			inputData.put("snooze", snooze);
			inputData.put("alert_song", bellURI);
			inputData.put("type", (typeS * 2 + typeV));
			inputData.put("recog_time", recogStrength);
			inputData.put("alert_state", 1);
			
			AlarmController.updateAlarm(inputData, selectedAlarmId);
			
			Log.i("AlarmUpdate", "DBModified");
			Toast.makeText(ActivityAlarmModify.this, R.string.toast_updated_alarm, Toast.LENGTH_SHORT).show();
			Bridge.getAdapterAlarmList().reloadData();
			
			onBackPressed();

			ActivityAlarmModify.this.finish();
			
		}
	};
}
