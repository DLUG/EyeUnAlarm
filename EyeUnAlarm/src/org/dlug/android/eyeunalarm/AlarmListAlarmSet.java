package org.dlug.android.eyeunalarm;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.SeekBar.OnSeekBarChangeListener;

public abstract class AlarmListAlarmSet extends AlarmListActivity{
	protected static final int[] snoozeArr = {5, 10, 15, 20};
	protected static final int[] recogStrengthArr = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
	protected static final int TRUE = 1;
	protected static final int FALSE = 0;
	
	protected String title = "";
	protected int[] alarmTime = {0, 0};
	protected int snooze = 5;
	protected int typeS = 1;
	protected int typeV = 1;
	protected String bellURI;
	protected int volume = 100;
	protected int repeat = 127;
	protected int recogStrength = 5;
	
	protected TextView titleAlarmSet;
	protected TextView viewTitle;
	protected TextView viewTime;
	protected TextView viewSnooze;
	protected ImageView btnTypeS;
	protected ImageView btnTypeV;
	protected TextView viewBell;
	protected SeekBar barVolume;
	protected TextView viewRepeat;
	protected TextView viewRecogStrength;
	protected Button btnConfirm;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_setting);

		titleAlarmSet = (TextView) findViewById(R.id.titleAlarmSet);
		viewTitle = (TextView) findViewById(R.id.viewTitle);
		viewTime = (TextView) findViewById(R.id.viewTime);
		viewSnooze = (TextView) findViewById(R.id.viewSnooze);
		btnTypeS = (ImageView) findViewById(R.id.btnTypeS);
		btnTypeV = (ImageView) findViewById(R.id.btnTypeV);
		viewBell = (TextView) findViewById(R.id.viewBell);
		barVolume = (SeekBar) findViewById(R.id.barVolume);
		barVolume.setMax(100);
		viewRepeat = (TextView) findViewById(R.id.viewRepeat);
		viewRecogStrength = (TextView) findViewById(R.id.viewRecogStrength);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		
		findViewById(R.id.layoutTitle).setOnClickListener(onClickTitle);
		findViewById(R.id.layoutTime).setOnClickListener(onClickTime);
		findViewById(R.id.layoutSnooze).setOnClickListener(onClickSnooze);
		btnTypeS.setOnClickListener(onClickTypeS);
		btnTypeV.setOnClickListener(onClickTypeV);
		findViewById(R.id.layoutBell).setOnClickListener(onClickBell);
		barVolume.setOnSeekBarChangeListener(onChangeVolume);
		findViewById(R.id.layoutRepeat).setOnClickListener(onClickRepeat);
		findViewById(R.id.layoutRecogTime).setOnClickListener(onClickRecogStrength);
		findViewById(R.id.btnCancel).setOnClickListener(onClickCancel);
		
		btnConfirm.setText("Modify This");
	}
	
	protected void setTitle(String title){
		viewTitle.setText(title);
		this.title = title; 
	}
	
	protected void setAlarmTime(int Hours, int minutes){
		viewTime.setText(String.format("%02d", Hours) + ":" + String.format("%02d", minutes));
		this.alarmTime[0] = Hours;
		this.alarmTime[1] = minutes;
	}
	
	protected void setSnooze(int snooze){
		viewSnooze.setText(snooze + " " + getString(R.string.minutes));
		this.snooze = snooze;
	}
	
	protected void setType(int type){
		if((type & 2) == 2){
			setTypeS(TRUE);
		} else {
			setTypeS(FALSE);
		}
		if((type & 1) == 1){
			setTypeV(TRUE);
		} else {
			setTypeV(FALSE);
		}
	}
	
	protected void setTypeS(int type){
		if(type == TRUE){
			typeS = TRUE;
			btnTypeS.setImageResource(R.drawable.btn_bgm);
		} else {
			typeS = FALSE;
			btnTypeS.setImageResource(R.drawable.icon_alert_off);
		}
		
		typeS = type;
	}

	protected void toggleTypeS(){
		if(typeS == TRUE){
			typeS = FALSE;
			btnTypeS.setImageResource(R.drawable.icon_alert_off);
		} else {
			typeS = TRUE;
			btnTypeS.setImageResource(R.drawable.btn_bgm);
		}
	}
	
	protected void setTypeV(int type){
		if(type == TRUE){
			typeV = TRUE;
			btnTypeV.setImageResource(R.drawable.btn_vib);
		} else {
			typeV = FALSE;
			btnTypeV.setImageResource(R.drawable.icon_alert_off);
		}
		
		typeV = type;
	}
	
	protected void toggleTypeV(){
		if(typeV == TRUE){
			typeV = FALSE;
			btnTypeV.setImageResource(R.drawable.icon_alert_off);
		} else {
			typeV = TRUE;
			btnTypeV.setImageResource(R.drawable.btn_vib);
		}
	}
	
	protected void setBellURI(String uri){
		bellURI = uri;
		String resultString = "";
		
		if(uri == null){
			resultString = getString(R.string.ringtone_slient);
		} else if(uri.equals("content://settings/system/ringtone")){
			resultString = getString(R.string.ringtone_default);
		} else {
			Uri mUri = Uri.parse(uri);
			Ringtone r = RingtoneManager.getRingtone(this, mUri);

			resultString = r.getTitle(this);
		}
		
		viewBell.setText(resultString);
/*
		try {
			URI oURI = new URI(bellURI);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/		
	}
	
	protected void setVolume(int volume){
		barVolume.setProgress(volume);
		this.volume = volume;
	}
	
	protected void setRepeat(int repeat){
		if(repeat == 127){
			viewRepeat.setText(R.string.everyday);
		} else if (repeat == 62){
			viewRepeat.setText(R.string.workday);
		} else {
			String result = "";
			
			String[] weekday = getResources().getStringArray(R.array.weekday);
			
			if((repeat & 64) == 64)
				result += weekday[0] + ", ";
			if((repeat & 32) == 32)
				result += weekday[1] + ", ";
			if((repeat & 16) == 16)
				result += weekday[2] + ", ";
			if((repeat & 8) == 8)
				result += weekday[3] + ", ";
			if((repeat & 4) == 4)
				result += weekday[4] + ", ";
			if((repeat & 2) == 2)
				result += weekday[5] + ", ";
			if((repeat & 1) == 1)
				result += weekday[6] + ", ";

			result = result.substring(0, result.length() - 2);
			viewRepeat.setText(result);
		}
		
		this.repeat = repeat;
	}
	
	public static boolean[] parseRepeatBinary(int repeat){
		boolean[] result = {false, false, false, false, false, false, false}; 
		
		if((repeat & 64) == 64)
			result[0] = true;
		if((repeat & 32) == 32)
			result[1] = true;
		if((repeat & 16) == 16)
			result[2] = true;
		if((repeat & 8) == 8)
			result[3] = true;
		if((repeat & 4) == 4)
			result[4] = true;
		if((repeat & 2) == 2)
			result[5] = true;
		if((repeat & 1) == 1)
			result[6] = true;
		
		return result;
	}
	
	public static int makeRepeatBinary(boolean[] repeat){
		int result = 0;
		
		for(int i = 0; i < 7; i++){
			if(repeat[i])
				result += (int) Math.pow(2, (6-i));
		}
		
		return result;
	}
	
	protected void setRecogStrength(int strength){
		viewRecogStrength.setText(strength + "");
		recogStrength = strength;
	}
	
	protected OnClickListener onClickTitle = new OnClickListener(){
		@Override
		public void onClick(View v) {
			final LinearLayout alarmTitleDialog = (LinearLayout) View.inflate(getParent(), R.layout.alarm_title_dialog, null);
			EditText tmpTitle = (EditText)alarmTitleDialog.findViewById(R.id.editAlarmTitle);
			
			tmpTitle.setText(title);
			
			new AlertDialog.Builder(getParent())
			.setTitle(R.string.label_alarm_title)
//			.setIcon(R.drawable.clock)
			.setView(alarmTitleDialog)
			.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					EditText tmpTitle = (EditText)alarmTitleDialog.findViewById(R.id.editAlarmTitle);
					setTitle(tmpTitle.getText().toString());
				}
			})
			.setNegativeButton(R.string.btn_cancel, null)
			.show();
		}
	};
	protected OnClickListener onClickTime = new OnClickListener(){
		@Override
		public void onClick(View v) {
			TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					setAlarmTime(hourOfDay, minute);
				}
			};
			TimePickerDialog alert = new TimePickerDialog(getParent(), mTimeSetListener, alarmTime[0], alarmTime[1], true);
			
			alert.show();
		}
		
		

	};
	protected OnClickListener onClickSnooze = new OnClickListener(){
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(getParent())
			.setTitle(R.string.label_snooze)
//			.setIcon(R.drawable.clock)
			.setItems(R.array.dialog_snooze, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					snooze = snoozeArr[which];
					viewSnooze.setText(snooze + " " + getString(R.string.minutes));
				}
			})
			.setNegativeButton(R.string.btn_cancel, null)
			.show();
		}
	};
	protected OnClickListener onClickTypeS = new OnClickListener(){
		@Override
		public void onClick(View v) {
			toggleTypeS();
		}
	};
	protected OnClickListener onClickTypeV = new OnClickListener(){
		@Override
		public void onClick(View v) {
			toggleTypeV();
		}
	};
	protected OnClickListener onClickBell = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
			if(bellURI != null){
				Uri oUri = Uri.parse(bellURI);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, oUri);
			}
	        getParent().startActivityForResult(intent, 0);
		}
	};
	protected OnSeekBarChangeListener onChangeVolume = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {}
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			setVolume(progress);
		}
	};
	protected OnClickListener onClickRepeat = new OnClickListener(){
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(getParent())
			.setTitle(R.string.label_repeat)
//			.setIcon(R.drawable.clock)
			.setMultiChoiceItems(R.array.weekday, parseRepeatBinary(repeat), new OnMultiChoiceClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					boolean[] tmp = parseRepeatBinary(repeat);
					tmp[which] = isChecked;
					int tmpRepeat = makeRepeatBinary(tmp);
					setRepeat(tmpRepeat);
				}
			})
			.setNeutralButton(R.string.btn_back, null)
			.show();
		}
	};
	protected OnClickListener onClickRecogStrength = new OnClickListener(){
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(getParent())
			.setTitle(R.string.label_recog_strength)
//			.setIcon(R.drawable.clock)
			.setItems(R.array.dialog_recog_strength, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					recogStrength = recogStrengthArr[which];
					viewRecogStrength.setText(recogStrength + "");
				}
			})
			.setNegativeButton(R.string.btn_cancel, null)
			.show();
		}
	};
	protected OnClickListener onClickCancel = new OnClickListener(){
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};
	
/*	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode){
    	case 0 :
    		Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
    		Log.d("URI", uri + "");

    		break;
    	}
    }
*/
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("ResultCode", resultCode + "");
		if(resultCode != 0){
	    	switch(requestCode){
	    	case 0 :
	    		Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
	    		Log.d("URI", uri + "");
	    		
	    		String resultURI = "";
	    		
	    		if(uri == null)
	    			resultURI = null;
	    		else
	    			resultURI = uri.toString();
	    		
	    		setBellURI(resultURI);
	
	    		break;
	    	}
		}
    }
}
