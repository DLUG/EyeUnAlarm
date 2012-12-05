package org.dlug.android.facealarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class AlarmSet extends AlarmData {

	Button cancelBtn;
	Button confirmBtn;
	TextView snoozeTxt;
   	TextView repeatTxt;
   	TextView soundTxt;
   	TextView timeTxt;
   	TextView typeTxt;
   	TextView eyesTxt;
   	TextView timeView;
   	TextView eyesTime;
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
	static final int EYES_DIALOG_ID = 5;
	
	//�ð�,확인��,Ÿ��,확인,�ݺ� 
	private int mHour;
	private int mMinute;
	private int snooze;
	private int sound;
	private int eyes;
	private int toggle_s;
	private int toggle_v;
	private int repeat;
	private int eye;
	int snoTemp;
	int soundTemp;
	int eyeTemp;
	int cnt = 0;
	
	//AlarmManager
	GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
	long alarmTime;
	GregorianCalendar gregorianCalendar;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.alarm_modif);
	
	
	      //DbAdapterŬ확인 �ν��Ͻ� �� 
	      final DbAdapter db = new DbAdapter(this);
	      
	      db.open();
	      
	      
	    //��ư,text 확인�� 
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
	      timeTxt.setText("시간");
	      typeTxt = (TextView)findViewById(R.id.typeTxt);
	      typeTxt.setText("type");
	      eyesTxt = (TextView)findViewById(R.id.eyesTxt);
	      eyesTxt.setText("eyes time");
	      
	      timeView = (TextView)findViewById(R.id.timeView);
	      snoozeTime = (TextView)findViewById(R.id.snoozeTime);
	      soundSeekbar = (SeekBar)findViewById(R.id.soundBar);
	  	  soundSeekbar.setMax(100);
   	  	  soundSeekbar.incrementProgressBy(10);
   	  	  eyesTime = (TextView)findViewById(R.id.eyesTime);
   	  	  soundSeekbar.setOnSeekBarChangeListener(controlListener);
   	  	  typeToggle_s = (ImageView)findViewById(R.id.typeToggle_s);
 	  	  typeToggle_v = (ImageView)findViewById(R.id.typeToggle_v);
 	  	  repeatToggle = (ImageView)findViewById(R.id.repeatToggle);
 	  	  
 	  	  typeToggle_s.setOnClickListener(new OnClickListener(){

	  		public void onClick(View v) {
				cnt++;
				// TODO Auto-generated method stub
				if(cnt%2 ==0){
					typeToggle_s.setImageResource(R.drawable.btn_bgm);
					toggle_s = 1;
					cnt = 0;
				}
				else
				{
					typeToggle_s.setImageResource(R.drawable.clock);
					toggle_s = 0;
				}
			}
	
 	  	  });
 	  	  
 	  	typeToggle_v.setOnClickListener(new OnClickListener(){

	  		public void onClick(View v) {
				cnt++;
				// TODO Auto-generated method stub
				if(cnt%2 ==0){
					typeToggle_v.setImageResource(R.drawable.btn_vib);
					toggle_v = 1;
					cnt = 0;
				}
				else
				{
					typeToggle_v.setImageResource(R.drawable.clock);
					toggle_v = 0;
				}
			}
	
 	  	  });
 	  	
 	  	repeatToggle.setOnClickListener(new OnClickListener(){

	  		public void onClick(View v) {
				cnt++;
				// TODO Auto-generated method stub
				if(cnt%2 ==0){
					repeatToggle.setImageResource(R.drawable.icon_on);
					repeat = 1;
					cnt = 0;
				}
				else
				{
					repeatToggle.setImageResource(R.drawable.icon_off);
					repeat = 0;
				}
			}
	
	  	  });
	     //layout 확인��
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
					Toast.makeText(AlarmSet.this, "type취소̾ƿ� �ǵ帲",
			    	        Toast.LENGTH_SHORT).show();
				}
			}); 
	      LinearLayout soundLyt = (LinearLayout)findViewById(R.id.layout_sound);
	      soundLyt.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(AlarmSet.this, "sound취소̾ƿ� �ǵ帲",
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
	      
	      LinearLayout eyestimeLyt = (LinearLayout)findViewById(R.id.layout_eyes);
	      eyestimeLyt.setOnClickListener(new View.OnClickListener(){
	    	 
	    	  	public void onClick(View v)
	    	  	{
	    	  		showDialog(EYES_DIALOG_ID);
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
	    		  int state;
	    		  db.open();
	    		  
	    		  Cursor c = db.fetchAllBooks();
	    		  c.moveToFirst();
	    		  
	    	      int i=0;
	    		  while(i<itemPosition)
	    		  {
	    			  c.moveToNext();
	    			  i++;
	    		  }
	    		  id = c.getInt(0);
	    		  state = c.getInt(9);
	    	      db.updateBook(id, mHour, mMinute, repeat, snooze, sound, toggle_s, toggle_v, eye, state);
	    	      

	    	      db.close();
	    	    
	    	      msgTag = 0;
	    	  	
	    	    //확인 �Ͻ� 
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
	    	      
    	  	  //AlarmManager 확인
	    	  if(state!=0)
    	      setAlarm(AlarmSet.this,alarmTime,id);
	    	      	      
    	      Intent intent = new Intent(AlarmSet.this,AlarmData.class);
    	      
  		  	  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
  		  	 
  		  	  goFreshHistory("AlarmData",intent);
  		  	 
	    	  }

	    	  
	      });
	      
	      cancelBtn.setOnClickListener(new OnClickListener(){
	    	  public void onClick(View v){
	    		
	    		  
	    		  msgTag = 0;
	    		  
	    		 // releaseAlarm(AlarmSet.this);
	    		  Intent intent = new Intent(AlarmSet.this,AlarmData.class);
	    		  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    		  
	    		  onBackPressed();
	    		  
	    		     	  
	    	  }
	 
	    	  
	      });
	      
	    
	      
	    
	}

@Override
  public void onResume(){
	  super.onResume();
		  
	  db.open();
	  Cursor c = db.fetchAllBooks();
	  c.moveToFirst();
	  
      int i=0;
	  while(i<itemPosition)
	  {
		  c.moveToNext();
		  i++;
	  }
	  
	  mHour = c.getInt(1);
	  mMinute = c.getInt(2);
	  repeat = c.getInt(3);
	  snooze = c.getInt(4);
	  sound = c.getInt(5);
	  toggle_s = c.getInt(6);
	  toggle_v = c.getInt(7);
	  eye = c.getInt(8);
	  
	  timeView.setText(new StringBuilder().append(apm(mHour))
    		  .append(pad(mHour))
    		  .append(":").append(pad(mMinute)));
	  snoozeTime.setText(snooze+"minutes");
	  soundSeekbar.setProgress(sound);
	  eyesTime.setText(eye+"second");
	  if(repeat ==1)
		  repeatToggle.setImageResource(R.drawable.icon_on);
	  else
		  repeatToggle.setImageResource(R.drawable.icon_off);
	  if(toggle_s == 1)
		  typeToggle_s.setImageResource(R.drawable.btn_bgm);
	  else
		  typeToggle_s.setImageResource(R.drawable.icon_alert_off);
	  if(toggle_v == 1)
		  typeToggle_v.setImageResource(R.drawable.btn_vib);
	  else
		  typeToggle_v.setImageResource(R.drawable.icon_alert_off);
	  
	  db.close();
	  
	  
	  
  }
  public void editSet(){
	  TextView timeView = (TextView)findViewById(R.id.timeView);
	  timeView.setText("am 11:00");
	
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


//TimePickerDialog 확인 
private TimePickerDialog.OnTimeSetListener mTimeSetListener =
		new TimePickerDialog.OnTimeSetListener(){
			
	public void onTimeSet(TimePicker view, int hourOfDay, int minute){
		
		mHour = hourOfDay;
		mMinute = minute;
		updateDisplay();
	
   	  	timeView.setText(new StringBuilder().append(apm(mHour)).append(pad(mHour)).append(":").append(pad(mMinute)));
     
	}
		
};
//취소̾�α� 확인 
@Override
protected Dialog onCreateDialog(int id){
	switch(id){
	case TIME_DIALOG_ID:
		return new TimePickerDialog(getParent(), mTimeSetListener, mHour, mMinute, false );
	case SNOOZE_DIALOG_ID:
		String[] snoozes={"5분 후에", "10분 후에", "15분 후에", "20분 후에", "30분 후에"};
	
		
		return new AlertDialog.Builder(getParent())
			.setTitle("Snooze")
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				
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
        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					final ImageView repeatToggle = (ImageView)findViewById(R.id.repeatToggle);
               	  	repeatToggle.setImageResource(R.drawable.icon_on);
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
	case EYES_DIALOG_ID:
String[] eyes={"5초 후에", "10초 후에", "15초 후에", "30초 후에", "40초 후에"};
	
		
		return new AlertDialog.Builder(getParent())
			.setTitle("Eyes Time")
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					eye =eyeTemp;
				
					eyesTime.setText(eye + "second");
					dialog.dismiss();
				}
			})
			.setNegativeButton("취소", new DialogInterface.OnClickListener() {
				
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			})
			.setSingleChoiceItems(eyes, -1, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					switch( which ){
					case 0: eyeTemp = 5; break;
					case 1: eyeTemp = 10; break;
					case 2: eyeTemp = 15; break;
					case 3: eyeTemp = 30; break;
					case 4: eyeTemp = 40; break;
					}
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
	"TYPE_M: " + c.getString(7) +"\n"+
	"EYE: " + c.getString(8) +"\n"+
	"STATE: " + c.getString(9) +"\n",
	Toast.LENGTH_LONG).show(); 
}

}