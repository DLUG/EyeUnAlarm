package org.dlug.android.facealarm;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AlarmPlay extends Activity implements OnClickListener, OnCompletionListener {
	Button snoozePlay;
	private MediaPlayer player;
	Vibrator vibe;
	final DbAdapter db = new DbAdapter(this);
	long Id;
	int pHour;
	int pMinute;
	int pSnooze;
	int pSound;
	int pType_s;
	int pType_v;
	int pRepeat;
	int eye;
	int pState;
	
	//AlarmManager
	GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
	GregorianCalendar gregorianCalendar;
	long alarmTime;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.alarm_play);
	    
	    snoozePlay = (Button)findViewById(R.id.snoozePlay);
	    player = MediaPlayer.create(getApplicationContext(),R.raw.withme);
	    
	    Intent intent = getIntent();
	    Id =  intent.getFlags();
	    db.open();
	    Log.i("adsf","slo : "+Id);
	    Cursor c = db.fetchBook(Id);
	    c.moveToFirst();
	    
	    pHour = c.getInt(1); 
	    pMinute = c.getInt(2);
	    pRepeat = c.getInt(3);
	    pSnooze = c.getInt(4);
	    pSound = c.getInt(5);
	    pType_s = c.getInt(6);
	    pType_v = c.getInt(7);
	    eye = c.getInt(8);
	    pState = c.getInt(9);
	    
	   	player.setVolume(pSound,pSound );
	    
	   	if(pType_s !=0){
	    	player.setOnCompletionListener(this);
	    	player.start();
	    }
	    if(pType_v !=0){
	    	vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    	long[] pattern = {1000, 200, 1000, 2000, 1200};
	    	vibe.vibrate(pattern, 0);
	    	vibe.vibrate(10000);
	    }
	    
	    snoozePlay.setOnClickListener(this);
	    
	    db.close();
	    Toast.makeText(this, "play id = "+ Id,Toast.LENGTH_SHORT).show();
	}

	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(pType_s != 0)
			player.stop();
		if(pType_v != 0)
			vibe.cancel();
		
	    //설정 일시 
	  	gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
	        
	    int currentYY = currentCalendar.get(Calendar.YEAR);
	  	int currentMM = currentCalendar.get(Calendar.MONTH);
	  	int currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);
	  	
	  	gregorianCalendar.set(currentYY, currentMM, currentDD, pHour, pMinute, 00);

	  	if(gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
	  		gregorianCalendar.set(currentYY, currentMM, currentDD+1, pHour, pMinute,00);
	  		Log.i("TAG",gregorianCalendar.getTimeInMillis()+":");
	  	}
	  	
	  	alarmTime = gregorianCalendar.getTimeInMillis();
	    Log.i("adsf","slo : "+Id);
		
		AlarmManager alarmManager = (AlarmManager)getSystemService(AlarmPlay.ALARM_SERVICE);
		Intent intent = new Intent(AlarmPlay.this, AlarmReceiver.class);
		intent.setFlags((int) Id);
		PendingIntent pIntent = PendingIntent.getBroadcast(AlarmPlay.this, (int) Id, intent,
															PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime+pSnooze*1000*60, pIntent); 
		
		finish();
	}

}
