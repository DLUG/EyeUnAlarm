package org.dlug.android.eyeunalarm.alarm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.dlug.android.eyeunalarm.AlarmController;
import org.dlug.android.eyeunalarm.R;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public abstract class ActivityAlarmPlayAbstract extends Activity{
	protected String title = "";
	protected int[] alarmTime = {0, 0};
	protected int snooze = 5;
	protected boolean typeS = true;
	protected boolean typeV = true;
	protected String bellURI = "content://settings/system/ringtone";
	protected int volume = 100;
	protected int repeatBinary = 127;
	protected boolean[] repeat;
	protected int recogStrength = 5;
	protected boolean isTest = true;

	final long[] pattern = {1000, 200, 1000, 2000, 1200};

	CameraPreview cameraView;
	public ProgressBar barRecogEye;

	MediaPlayer player;
	Ringtone ringtone;
	Vibrator vibe;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		AlarmController.init(this);

		prepareData();
		
		File xmlFile = new File(this.getFilesDir().getPath() + "/eyes.xml");
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

		setContentView(R.layout.activity_alarm_play);
		
		
		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
		wakeLock.acquire();

		KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE); 
		KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
		keyguardLock.disableKeyguard();

		try{
			cameraView = new CameraPreview(getApplicationContext(), 640, 480, this, recogStrength);
			LinearLayout layoutPlayVideo = (LinearLayout)findViewById(R.id.layoutPlayVideo);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
	
			cameraView.setLayoutParams(params);
			
			layoutPlayVideo.addView(cameraView);
		} catch(Exception e){
			Log.e("OpenCV", "Cannot connect to OpenCV Manager");
			Toast.makeText(this, R.string.toast_cannot_open_camera , 5).show();
			faultOpenVideo();
		}


		if(typeS){
//			ringtone = RingtoneManager.getRingtone(getApplicationContext(), Uri.parse(bellURI));
//			ringtone.play();
			try{
				player = new MediaPlayer();
				if(bellURI.equals("")){
					bellURI = "content://settings/system/ringtone";
				}
				
				player.setAudioStreamType(AudioManager.STREAM_ALARM);
				player.setDataSource(this, Uri.parse(bellURI));
				
				player.setLooping(true);
				player.setVolume(volume, volume);
				player.prepare();
				player.start();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		if(typeV){
			vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vibe.vibrate(pattern, 0);
		}
		
		
		findViewById(R.id.btnSnooze).setOnClickListener(onClickSnooze);
		barRecogEye = (ProgressBar) findViewById(R.id.barRecogEye);
		
		
		barRecogEye.setMax(100);
	}

	

	protected abstract void prepareData();	

	
	OnClickListener onClickSnooze = new OnClickListener(){
		@Override
		public void onClick(View v) {
//			finish();
		}
		
	};
	
	public void pass(){
		try{
			cameraView.pause();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		if(typeS)
			player.stop();

		if(typeV)
			vibe.cancel();
		
		AlarmController.resetAlarm();
		
		finish();
	}
	
	private void faultOpenVideo(){
		LinearLayout layoutPlayVideo = (LinearLayout)findViewById(R.id.layoutPlayVideo);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	            LinearLayout.LayoutParams.MATCH_PARENT,
	            LinearLayout.LayoutParams.MATCH_PARENT);
		
		Button unlockBtn = new Button(this);
		unlockBtn.setText("Temporary unlock");
		layoutPlayVideo.removeAllViewsInLayout();
		layoutPlayVideo.addView(unlockBtn, params);
		unlockBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				pass();
			}
		});
	}
	
	public void setProgressBar(int percent){
		barRecogEye.setProgress(percent);
	}
}