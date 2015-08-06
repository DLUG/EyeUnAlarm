package org.dlug.android.eyeunalarm;

import org.dlug.android.eyeunalarm.R;
import org.dlug.android.eyeunalarm.AlarmController.AlarmData;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ActivityAlarmAdd extends ActivityAlarmSetAbstract{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		titleAlarmSet.setText(R.string.title_add_alarm);
		btnConfirm.setText(R.string.btn_confirm);
		findViewById(R.id.btnConfirm).setOnClickListener(onClickConfirm);
	}
	
	@Override
	protected void onResume(){
		setTitle(this.title);
		setAlarmTime(this.alarmTime[0], this.alarmTime[1]);
		setSnooze(this.snooze);
		setTypeS(this.typeS);
		setTypeV(this.typeV);
		setBellURI(Uri.parse(this.bellURI));
		setVolume(this.volume);
		setRepeat(this.repeat);
		setRecogStrength(this.recogStrength);

		super.onResume();
	}
	
	private OnClickListener onClickConfirm = new OnClickListener(){
		@Override
		public void onClick(View v) {
			AlarmData inputData = new AlarmData();
			inputData.alarmName = title;
			inputData.hours = alarmTime[0];
			inputData.minutes = alarmTime[1];
			inputData.repeat = repeat;
			inputData.snooze = snooze;
			inputData.alertSong = bellURI;
			inputData.alertVolume = volume;
			inputData.type = (typeS * 2 + typeV);
			inputData.recogTime = recogStrength;
			inputData.alertState = 1;
			
			AlarmController.addAlarm(inputData);
			Bridge.getAdapterAlarmList().reloadData();
			Log.d("AlarmAdd", "DBAdded");
			Toast.makeText(ActivityAlarmAdd.this, R.string.toast_added_alarm, Toast.LENGTH_SHORT).show();
			onBackPressed();
			
			ActivityAlarmAdd.this.finish();
		}
	};
}
