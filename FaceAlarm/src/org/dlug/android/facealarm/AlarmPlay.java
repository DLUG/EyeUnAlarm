package org.dlug.android.facealarm;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
	
	FrameLayout previewFrame;
	ImageView modifyImage;
	CameraPreview cameraView;
	
	//AlarmManager
	GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
	GregorianCalendar gregorianCalendar;
	long alarmTime;
	
	
    private BaseLoaderCallback  mOpenCVCallBack = new BaseLoaderCallback(this) {
    	@Override
    	public void onManagerConnected(int status) {
    		switch (status) {
				case LoaderCallbackInterface.SUCCESS:
				{
					Log.i("OpenCV", "OpenCV loaded successfully");
					
					// Load native library after(!) OpenCV initialization
					System.loadLibrary("EyeRecog");
					
					previewFrame.removeAllViews();
					previewFrame.addView(cameraView);
					cameraView.setModifyView(modifyImage);
				} break;
				default:
				{
					super.onManagerConnected(status);
				} break;
			}
    	}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.alarm_play);
	    
		previewFrame = (FrameLayout) findViewById(R.id.PreviewFrame);
		modifyImage = (ImageView) findViewById(R.id.modifyImage);
		
		File xmlFile = new File("/data/data/org.dlug.android.facealarm/files/eyes.xml");
		if(!xmlFile.exists()){
/*
			File filesFolder = new File("/data/data/com.example.detectobject/files");
			if(!filesFolder.exists()){
				filesFolder.mkdir();
			}
*/			
			try {
				InputStream input = getAssets().open("haarcascade_eye.xml");
				byte[] data = new byte[input.available()];
				input.read(data);
				input.close();
				FileOutputStream output = openFileOutput("eyes.xml", Context.MODE_PRIVATE);
				output.write(data);
				output.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		xmlFile = new File("/data/data/org.dlug.android.facealarm/files/face.xml");
		if(!xmlFile.exists()){
/*
			File filesFolder = new File("/data/data/com.example.detectobject/files");
			if(!filesFolder.exists()){
				filesFolder.mkdir();
			}
*/			
			try {
				InputStream input = getAssets().open("haarcascade_frontalface_alt.xml");
				byte[] data = new byte[input.available()];
				input.read(data);
				input.close();
				FileOutputStream output = openFileOutput("face.xml", Context.MODE_PRIVATE);
				output.write(data);
				output.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		cameraView = new CameraPreview(getApplicationContext(), 640, 480, 100, this);
		
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mOpenCVCallBack))
        {
        	Log.e("OpenCV", "Cannot connect to OpenCV Manager");
        }
	    
	    snoozePlay = (Button)findViewById(R.id.snoozePlay);
	    player = MediaPlayer.create(getApplicationContext(),R.raw.run);
	    
	    Intent intent = getIntent();
	    Id =  intent.getFlags();
	    
	    db.open();
	    
	    
	    Date today = new Date();
	    
	    
	    String query = "SELECT * FROM data WHERE hour=" + today.getHours() + " AND minutes=" + today.getMinutes() + ";";
	    Log.i("Query", query);
	    Cursor c = db.getResult(query);
	    
//	    result.moveToFirst();
	    
	    
	    Log.i("adsf","alarmPlay : "+Id);
	    
	    
//	    Cursor c = db.fetchBook(Id);
	    c.moveToFirst();
	    /*
	    pHour = c.getInt(1); 
	    pMinute = c.getInt(2);
	    pRepeat = c.getInt(3);
	    pSnooze = c.getInt(4);
	    pSound = c.getInt(5);
	    pType_s = c.getInt(6);
	    pType_v = c.getInt(7);
	    eye = c.getInt(8);
	    pState = c.getInt(9);
	    */
	    
	    pHour = 9;
	    pMinute = 59;
	    pRepeat = 0;
	    pSnooze = 0;
	    pSound = 100;
	    pType_s = 1;
	    pType_v = 1;
	    eye = 5;
	    pState = 1;
	    
	    
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
	
//	public void onPause(){
		//cameraView.pause();
//	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(pType_s != 0)
			player.stop();
		if(pType_v != 0)
			vibe.cancel();
		
	    //���� �Ͻ� 
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

    public void ok(){
		if(pType_s != 0)
			player.stop();
		if(pType_v != 0)
			vibe.cancel();

		AlarmManager alarmManager = (AlarmManager)getSystemService(AlarmPlay.ALARM_SERVICE);
		Intent intent = new Intent(AlarmPlay.this, AlarmReceiver.class);
		intent.setFlags((int) Id);
		PendingIntent pIntent = PendingIntent.getBroadcast(AlarmPlay.this, (int) Id, intent,
															PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pIntent);
		
    	
    	finish();
    }
	
}
