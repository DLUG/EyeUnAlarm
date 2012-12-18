package org.dlug.android.eyeunalarm;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class AlarmListAlarmModify extends AlarmListAlarmSet{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		titleAlarmSet.setText(R.string.title_modify_alarm);
		btnConfirm.setText(R.string.btn_update);
		
		findViewById(R.id.btnConfirm).setOnClickListener(onClickConfirm);
	}
	
	@Override
	protected void onResume(){
		Map<String, Object> thisAlarmData = myDb.getAlarm(selectAlarmId);
		
		setTitle((String) thisAlarmData.get(MyDbHelper.FIELD_ALARM_NAME));
		setAlarmTime((Integer) thisAlarmData.get(MyDbHelper.FIELD_HOURS), (Integer) thisAlarmData.get(MyDbHelper.FIELD_MINUTES));
		setSnooze((Integer) thisAlarmData.get(MyDbHelper.FIELD_SNOOZE));
		setType((Integer) thisAlarmData.get(MyDbHelper.FIELD_TYPE));
		setBellURI((String) thisAlarmData.get(MyDbHelper.FIELD_ALERT_SONG));
		setVolume((Integer) thisAlarmData.get(MyDbHelper.FIELD_ALERT_VOLUME));
		setRepeat((Integer) thisAlarmData.get(MyDbHelper.FIELD_REPEAT));
		setRecogStrength((Integer) thisAlarmData.get(MyDbHelper.FIELD_RECOG_TIME));
		
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
			
			Map<String, Object> filter = new HashMap<String,Object>(2);
			filter.put("_id", selectAlarmId);
			myDb.updateAlarm(inputData, filter);
			
			Log.i("AlarmUpdate", "DBModified");
			Toast.makeText(AlarmListAlarmModify.this, R.string.toast_updated_alarm, Toast.LENGTH_SHORT).show();
			
			onBackPressed();

			listUpdate();
			try {
				AlarmListAlarmModify.this.finalize();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
}
