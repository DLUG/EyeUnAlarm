package org.dlug.android.facealarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmModif extends AlarmData {
	
	Button cancelBtn;
	Button confirmBtn;
	TextView snoozeTxt;
   	TextView repeatTxt;
   	TextView soundTxt;
   	TextView timeTxt;
   	TextView typeTxt;
   	TextView timeView;
   	TextView snoozeTime;
   	SeekBar soundSeekbar;
   	ImageView typeToggle_s;
   	ImageView typeToggle_v;
   	ImageView repeatToggle;
   	
	SharedPreferences sp;
	
	//Dialog
	static final int TIME_DIALOG_ID = 0;
	static final int SNOOZE_DIALOG_ID = 1;
	static final int REPEAT_DIALOG_ID =4;
	
	//시간,스누즈,타입,사운드,반복 
	private int mHour;
	private int mMinute;
	private int snooze;
	private int sound;
	private int toggle_s;
	private int toggle_v;
	private int repeat;
	int snoTemp;
	int soundTemp;
	int cnt=0;
	
	//AlarmManager
	GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
	GregorianCalendar gregorianCalendar;
	long alarmTime;
	
		
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.alarm_modif);
	
	      //releaseAlarm(AlarmModif.this);
	      //DbAdapter클래스 인스턴스 생성 
	      final DbAdapter db = new DbAdapter(this);
	      
	    //버튼,text 리스너 
	      confirmBtn = (Button)findViewById(R.id.confirmBtn);
	      confirmBtn.setText("확인");
	      cancelBtn = (Button)findViewById(R.id.cancelBtn);
	      cancelBtn.setText("취소");
	      snoozeTxt = (TextView)findViewById(R.id.snoozeTxt);
	      snoozeTxt.setText("snooze");
	      repeatTxt = (TextView)findViewById(R.id.repeatTxt);
	      repeatTxt.setText("반복");
	      soundTxt = (TextView)findViewById(R.id.soundTxt);
	      soundTxt.setText("sound");
	      timeTxt = (TextView)findViewById(R.id.timeTxt);
	      timeTxt.setText("알람 시간");
	      typeTxt = (TextView)findViewById(R.id.typeTxt);
	      typeTxt.setText("type");
	      timeView = (TextView)findViewById(R.id.timeView);
	      snoozeTime = (TextView)findViewById(R.id.snoozeTime);
	      soundSeekbar = (SeekBar)findViewById(R.id.soundBar);
	  	  soundSeekbar.setMax(100);
   	  	  soundSeekbar.incrementProgressBy(10);
   	  	  soundSeekbar.setOnSeekBarChangeListener(controlListener);
   	  	  typeToggle_s = (ImageView)findViewById(R.id.typeToggle_s);
   	  	  typeToggle_s.setImageResource(R.drawable.btn_bgm_final);
   	  	  toggle_s = 1;
   	  	  typeToggle_v = (ImageView)findViewById(R.id.typeToggle_v);
   	  	  typeToggle_v.setImageResource(R.drawable.btn_vibration_final);
   	  	  toggle_v = 1;
   	  	  repeatToggle = (ImageView)findViewById(R.id.repeatToggle);
   	  	  repeatToggle.setImageResource(R.drawable.on);
   	  	  repeat = 1;
   	  	  
   	  	  typeToggle_s.setOnClickListener(new OnClickListener(){

	  		public void onClick(View v) {
				cnt++;
				// TODO Auto-generated method stub
				if(cnt%2 ==0){
					typeToggle_s.setImageResource(R.drawable.btn_bgm_final);
					toggle_s = 1;
					cnt = 0;
				}
				else
				{
					typeToggle_s.setImageResource(R.drawable.off);
					toggle_s = 0;
				}
			}
  	
   	  	  });
   	  	  
   	  	typeToggle_v.setOnClickListener(new OnClickListener(){

	  		public void onClick(View v) {
				cnt++;
				// TODO Auto-generated method stub
				if(cnt%2 ==0){
					typeToggle_v.setImageResource(R.drawable.btn_vibration_final);
					toggle_v = 1;
					cnt = 0;
				}
				else
				{
					typeToggle_v.setImageResource(R.drawable.off);
					toggle_v = 0;
				}
			}
  	
   	  	  });
   	  	
   	  	repeatToggle.setOnClickListener(new OnClickListener(){

	  		public void onClick(View v) {
				cnt++;
				// TODO Auto-generated method stub
				if(cnt%2 ==0){
					repeatToggle.setImageResource(R.drawable.on);
					repeat = 1;
					cnt = 0;
				}
				else
				{
					repeatToggle.setImageResource(R.drawable.off);
					repeat = 0;
				}
			}
	
	  	  });
   	  	  
	     //layout 리스너
	      LinearLayout timeLyt = (LinearLayout)findViewById(R.id.layout_time);
	      timeLyt.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub

				 showDialog(TIME_DIALOG_ID);
			}			
		  }); 
	      
	      LinearLayout snoozeLyt = (LinearLayout)findViewById(R.id.layout_snooze);
	      snoozeLyt.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(SNOOZE_DIALOG_ID);
				}
			}); 
	      LinearLayout typeLyt = (LinearLayout)findViewById(R.id.layout_type);
	      typeLyt.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(AlarmModif.this, "type레이아웃 건드림",
			    	        Toast.LENGTH_SHORT).show();
				}
			}); 
	      LinearLayout soundLyt = (LinearLayout)findViewById(R.id.layout_sound);
	      soundLyt.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(AlarmModif.this, "sound레이아웃 건드림",
			    	        Toast.LENGTH_SHORT).show();
				}
			}); 
	      LinearLayout repeatLyt = (LinearLayout)findViewById(R.id.layout_repeat);
	      repeatLyt.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(REPEAT_DIALOG_ID);
				}
			}); 
	    
	      //timePicker 
	      final Calendar calDateTime = Calendar.getInstance();
	      mHour = calDateTime.get(Calendar.HOUR_OF_DAY);
	      mMinute = calDateTime.get(Calendar.MINUTE);
	      updateDisplay();
	      
	      //releaseAlarm(AlarmSet.this);
	      
	     
	      confirmBtn.setOnClickListener(new OnClickListener(){
	    	  public void onClick(View v){
	    		  //db save
	    		
	    		  
	    		  db.open();
	    		  
	    	 
	    	      id = db.createBook(   		  
	    	    		  mHour,
	    	    		  mMinute,
	    	    		  repeat,
	    	    		  snooze,
	    	    		  sound,
	    	    		  toggle_s,
	    	    		  toggle_v,
	    	    		  1
	    	    		  );
	    	      
	    	      Cursor c = db.fetchBook(id);
	    	      
	    	     
	    	      if (c.moveToFirst()) 
	    	        DisplayTitle(c);
	    	      else
	    	        Toast.makeText(AlarmModif.this, "No title found",
	    	        Toast.LENGTH_SHORT).show();

	    	      db.close();
	    	      
	    	      msgTag = 1;
	    	      
	    	    //설정 일시 
	    	  	gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
	    	        
	    	      int currentYY = currentCalendar.get(Calendar.YEAR);
	    	  	int currentMM = currentCalendar.get(Calendar.MONTH);
	    	  	int currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);
	    	  	
	    	  	gregorianCalendar.set(currentYY, currentMM, currentDD, mHour, mMinute, 00);

	    	  	if(gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
	    	  		gregorianCalendar.set(currentYY, currentMM, currentDD+1, mHour, mMinute,00);
	    	  		Log.i("TAG",gregorianCalendar.getTimeInMillis()+":");
	    	  	}
	    	  	
	    	  	alarmTime = gregorianCalendar.getTimeInMillis();
	    	      
	    	      //AlarmManager 설정 
	    	      setAlarm(AlarmModif.this,alarmTime, id);
	    	      
	    	      Intent intent = new Intent(AlarmModif.this,AlarmData.class);
	    	      
	  		  	  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
	  		  	 
	  		  	  goFreshHistory("AlarmData",intent);
	  		  	 
	    	  }

	    	  
	      });
	      
	      cancelBtn.setOnClickListener(new OnClickListener(){
	    	  public void onClick(View v){
	    		
	    		  msgTag = 0;
	    		  
	    		 // releaseAlarm(AlarmSet.this);
	    		  Intent intent = new Intent(AlarmModif.this,AlarmData.class);
	    		  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    		  
	    		  onBackPressed();
	    		  
	    		     	  
	    	  }
	 
	    	  
	      });
	      
	    
	      
	    
	}

@Override
  public void onResume(){
	  super.onResume();
	  
	  timeView.setText(new StringBuilder().append(apm(currentCalendar.get(Calendar.HOUR)))
    		  .append(pad(currentCalendar.get(Calendar.HOUR)))
    		  .append(":").append(pad(currentCalendar.get(Calendar.MINUTE))));
	  snoozeTime.setText("5minutes");
	  soundSeekbar.setProgress(10);
	  typeToggle_s.setImageResource(R.drawable.btn_bgm_final);
	  toggle_s = 1;
	  typeToggle_v.setImageResource(R.drawable.btn_vibration_final);
	  toggle_v = 1;
	  repeatToggle.setImageResource(R.drawable.on);
	  repeat = 1;
}


private void updateDisplay(){
	
}

private static String pad (int c){
	if(c >= 10){
		return String.valueOf(c);
	}
	else
		return "0" + String.valueOf(c);
}

private static String apm(int apm){
	  if( apm<12)
	   return "AM ";
	  else
	   return "PM ";
}


//TimePickerDialog 정의 
private TimePickerDialog.OnTimeSetListener mTimeSetListener =
		new TimePickerDialog.OnTimeSetListener(){
			
	public void onTimeSet(TimePicker view, int hourOfDay, int minute){

		mHour = hourOfDay;
		mMinute = minute;
		updateDisplay();
	
   	  	timeView.setText(new StringBuilder().append(apm(mHour)).append(pad(mHour)).append(":").append(pad(mMinute)));
     
	}
		
};
//다이얼로그 정의 
@Override
protected Dialog onCreateDialog(int id){
	switch(id){
	case TIME_DIALOG_ID:
		return new TimePickerDialog(getParent(), mTimeSetListener, mHour, mMinute, false );
	case SNOOZE_DIALOG_ID:
		String[] snoozes={"5분 후에", "10분 후에", "15분 후에", "20분 후에", "30분 후에"};
	
		
		return new AlertDialog.Builder(getParent())
			.setTitle("Snooze")
			.setPositiveButton("선택", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					snooze =snoTemp;
				
					snoozeTime.setText(snooze + "minutes");
					dialog.dismiss();
				}
			})
			.setNegativeButton("취소", new DialogInterface.OnClickListener() {
				
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			})
			.setSingleChoiceItems(snoozes, -1, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					switch( which ){
					case 0: snoTemp = 5; break;
					case 1: snoTemp = 10; break;
					case 2: snoTemp = 15; break;
					case 3: snoTemp = 20; break;
					case 4: snoTemp = 30; break;
					}
				}
			}).create();
	case REPEAT_DIALOG_ID:
		String[] repeats={"월요일마다",
						  "화요일마다",
						  "수요일마다",
						  "목요일마다",
						  "금요일마다",
						  "토요일마다",
						  "일요일마다"
					   	  };
		return new AlertDialog.Builder(getParent())
        .setTitle("Repeat")
        .setPositiveButton("선택", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					final ImageView repeatToggle = (ImageView)findViewById(R.id.repeatToggle);
               	  	repeatToggle.setImageResource(R.drawable.on);
               	  	repeat = 1;
					dialog.dismiss();
				}
			})
			.setNegativeButton("취소", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			})
        .setMultiChoiceItems(repeats, new boolean[]{true,true,true,true,true,true,true}, 
                new DialogInterface.OnMultiChoiceClickListener(){

            public void onClick(DialogInterface dialog, int which,
                    boolean isChecked) {
                                   
            }
            
             
        }).create();
	}
	
	return null;
}

private SeekBar.OnSeekBarChangeListener controlListener = new SeekBar.OnSeekBarChangeListener() {
	
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		sound = soundTemp;
		//Toast.makeText(getParent(), "sound = " + soundTemp, Toast.LENGTH_SHORT).show();
		
	}
	
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		soundTemp = progress;
	}
};

public void DisplayTitle(Cursor c)
{
	Toast.makeText(this,
	"id: " + c.getString(0) + "\n" +
	"HOUR: " + c.getString(1) + "\n" +
	"MIN: " + c.getString(2) + "\n" +
	"REPEAT: " + c.getString(3) +"\n"+
	"SNOOZE: " + c.getString(4) +"\n"+
	"SOUND: " + c.getString(5) + "\n" + 
	"TYPE_S: " + c.getString(6) +"\n"+
	"TYPE_M: " + c.getString(7) +"\n",
	Toast.LENGTH_LONG).show(); 
}

}
