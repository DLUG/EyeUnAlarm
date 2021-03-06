package org.dlug.android.eyeunalarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.dlug.android.eyeunalarm.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.media.AudioManager;
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

public abstract class ActivityAlarmSetAbstract extends Activity{
	protected static final int[] arrSnooze = {5, 10, 15, 20};
	protected static final int[] arrRecogStrength = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
	protected static final int TRUE = 1;
	protected static final int FALSE = 0;
	
	String[] arrSnoozeString;
	String[] arrRecogString;
	String stringMinutes;
	String stringSeconds;
	
	protected String title = "";
	protected int[] alarmTime;
	protected int snooze = arrSnooze[0];
	protected int typeS = 1;
	protected int typeV = 1;
	protected String bellURI = "content://settings/system/ringtone";
	protected int volume = 100;
	protected int repeat = 127;
	protected int recogStrength = arrRecogStrength[0];
	
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
	
	protected GregorianCalendar currentCalendar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_setting);

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
		
		stringMinutes = getString(R.string.minutes);
		stringSeconds = getString(R.string.seconds);

		arrSnoozeString = new String[arrSnooze.length];
		for(int i = 0; i < arrSnooze.length; i++){
			arrSnoozeString[i] = String.valueOf(arrSnooze[i]) + " " + stringMinutes;
		}

		arrRecogString = new String[arrRecogStrength.length];
		for(int i = 0; i < arrRecogStrength.length; i++){
			arrRecogString[i] = String.valueOf(arrRecogStrength[i]) + " " + stringSeconds;
		}
		
		currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
		alarmTime = new int[]{currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE)};
		title = getString(R.string.alarm);
		
		AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		volume = (int) (audio.getStreamVolume(AudioManager.STREAM_ALARM) / (float) audio.getStreamMaxVolume(AudioManager.STREAM_ALARM) * 100);
		Log.d("Volume", audio.getStreamVolume(AudioManager.STREAM_ALARM) + "");
		Log.d("VolumeMax", audio.getStreamMaxVolume(AudioManager.STREAM_ALARM) + "");
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
			btnTypeS.setImageResource(R.drawable.icon_bgm_on);
		} else {
			typeS = FALSE;
			btnTypeS.setImageResource(R.drawable.icon_bgm_or_vib_off);
		}
		
		typeS = type;
	}

	protected void toggleTypeS(){
		if(typeS == TRUE){
			typeS = FALSE;
			btnTypeS.setImageResource(R.drawable.icon_bgm_or_vib_off);
		} else {
			typeS = TRUE;
			btnTypeS.setImageResource(R.drawable.icon_bgm_on);
		}
	}
	
	protected void setTypeV(int type){
		if(type == TRUE){
			typeV = TRUE;
			btnTypeV.setImageResource(R.drawable.icon_vib_on);
		} else {
			typeV = FALSE;
			btnTypeV.setImageResource(R.drawable.icon_bgm_or_vib_off);
		}
		
		typeV = type;
	}
	
	protected void toggleTypeV(){
		if(typeV == TRUE){
			typeV = FALSE;
			btnTypeV.setImageResource(R.drawable.icon_bgm_or_vib_off);
		} else {
			typeV = TRUE;
			btnTypeV.setImageResource(R.drawable.icon_vib_on);
		}
	}
	
	protected void setBellURI(Uri uri){
		
		String resultString = "";
		
		if(uri == null){
			resultString = getString(R.string.ringtone_slient);
		} else if(uri.toString().equals("content://settings/system/ringtone")){
			resultString = getString(R.string.ringtone_default);
		} else {
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), uri);

			resultString = r.getTitle(getApplicationContext());
		}
		Log.d("BellString", resultString);
		viewBell.setText(resultString);
		bellURI = uri.toString();
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

			if(result.equals("")){
				result = getResources().getString(R.string.weekday_none);
			} else {
				result = result.substring(0, result.length() - 2);
			}
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
		viewRecogStrength.setText(strength + getString(R.string.seconds));
		recogStrength = strength;
	}
	
	protected OnClickListener onClickTitle = new OnClickListener(){
		@Override
		public void onClick(View v) {
			final LinearLayout alarmTitleDialog = (LinearLayout) View.inflate(ActivityAlarmSetAbstract.this, R.layout.dialog_alarm_title, null);
			EditText tmpTitle = (EditText)alarmTitleDialog.findViewById(R.id.editAlarmTitle);
			
			tmpTitle.setText(title);
			
			new AlertDialog.Builder(ActivityAlarmSetAbstract.this)
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
			TimePickerDialog alert = new TimePickerDialog(ActivityAlarmSetAbstract.this, mTimeSetListener, alarmTime[0], alarmTime[1], true);
			
			alert.show();
		}
		
		

	};
	protected OnClickListener onClickSnooze = new OnClickListener(){
		@Override
		public void onClick(View v) {
			
			new AlertDialog.Builder(ActivityAlarmSetAbstract.this)
			.setTitle(R.string.label_snooze)
//			.setIcon(R.drawable.clock)
			.setItems(arrSnoozeString, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					snooze = arrSnooze[which];
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
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
	        ActivityAlarmSetAbstract.this.startActivityForResult(intent, 0);
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
			new AlertDialog.Builder(ActivityAlarmSetAbstract.this)
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
			
			new AlertDialog.Builder(ActivityAlarmSetAbstract.this)
			.setTitle(R.string.label_recog_strength)
//			.setIcon(R.drawable.clock)
			.setItems(arrRecogString, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					recogStrength = arrRecogStrength[which];
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
			try {
				ActivityAlarmSetAbstract.this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	};
	
/*	
	@Override
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
		Log.d("ResultCode", requestCode + "");
		if(resultCode != 0){
	    	switch(requestCode){
	    	case 0 :
	    		Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
	    		Log.d("URI", uri + "");
	    		Log.d("Ttile", data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_TITLE) + "");
	    		
	    		setBellURI(uri);
	
	    		break;
	    	}
		}
    }
}
