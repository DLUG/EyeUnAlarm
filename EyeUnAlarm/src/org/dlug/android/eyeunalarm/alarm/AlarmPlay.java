package org.dlug.android.eyeunalarm.alarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.dlug.android.eyeunalarm.AlarmListAlarmSet;
import org.dlug.android.eyeunalarm.MyActivity;
import org.dlug.android.eyeunalarm.MyDbHelper;
import org.dlug.android.eyeunalarm.R;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AlarmPlay extends MyActivity{
	protected String title = "";
	protected int[] alarmTime = {0, 0};
	protected int snooze = 5;
	protected boolean typeS = true;
	protected boolean typeV = true;
	protected String bellURI;
	protected int volume = 100;
	protected int repeatBinary = 127;
	protected boolean[] repeat;
	protected int recogStrength = 5;
	
	final long[] pattern = {1000, 200, 1000, 2000, 1200};
	
	ImageView modifyImage;
	CameraPreview cameraView;
	public ProgressBar barRecogEye;
	
	MediaPlayer player;
	Vibrator vibe;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_play);
		
		gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
		currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));

		int currentHH = currentCalendar.get(Calendar.HOUR_OF_DAY);
		int currentmm = currentCalendar.get(Calendar.MINUTE);

		Map<String, Object>filter = new HashMap<String, Object>(3);
		filter.put(MyDbHelper.FIELD_HOURS, currentHH);
		filter.put(MyDbHelper.FIELD_MINUTES, currentmm);
		
		Map<String, Object> currentAlarm = myDb.getAlarm(filter);
	
		findViewById(R.id.btnSnooze).setOnClickListener(onClickSnooze);
		
		barRecogEye = (ProgressBar) findViewById(R.id.barRecogEye);
		
		title = (String) currentAlarm.get(MyDbHelper.FIELD_ALARM_NAME);
		alarmTime[0] = (Integer) currentAlarm.get(MyDbHelper.FIELD_HOURS);
		alarmTime[1] = (Integer) currentAlarm.get(MyDbHelper.FIELD_MINUTES);
		snooze = (Integer) currentAlarm.get(MyDbHelper.FIELD_SNOOZE);
		int type = (Integer) currentAlarm.get(MyDbHelper.FIELD_TYPE);
		typeS = (type & 2) == 2;
		typeV = (type & 1) == 1;
		bellURI = (String) currentAlarm.get(MyDbHelper.FIELD_ALERT_SONG);
		volume = (Integer) currentAlarm.get(MyDbHelper.FIELD_ALERT_VOLUME);
		repeatBinary = (Integer) currentAlarm.get(MyDbHelper.FIELD_REPEAT);
		repeat = AlarmListAlarmSet.parseRepeatBinary(repeatBinary);
		recogStrength = (Integer) currentAlarm.get(MyDbHelper.FIELD_RECOG_TIME);

		barRecogEye.setMax(recogStrength);
		

		
		int todayDayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
		if(!repeat[todayDayOfWeek - 1])
			finish();
		
		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
		
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE); 
        KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
        
        
        
		modifyImage = (ImageView) findViewById(R.id.modifyImage);
		try{
			cameraView = new CameraPreview(getApplicationContext(), 640, 480, this, recogStrength);
			 if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mOpenCVCallBack))
		        {
		        	Log.e("OpenCV", "Cannot connect to OpenCV Manager");
		        	faultOpenVideo();
		        }
		} catch(Exception e){
			Log.e("OpenCV", "Cannot connect to OpenCV Manager");
			Toast.makeText(this, R.string.toast_cannot_open_camera , 5).show();
			faultOpenVideo();
		}
			
       
	    
        if(typeS){
/*
        	player = MediaPlayer.create(getApplicationContext(), Uri.parse(bellURI));

            player.setLooping(true);
			player.setVolume(volume, volume);
		    player.start();
*/
        	
        	try{
        		player = new MediaPlayer();
        		player.setDataSource(getApplicationContext(), Uri.parse(bellURI));

        		player.setAudioStreamType(AudioManager.STREAM_ALARM);
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
	}
	
	OnClickListener onClickSnooze = new OnClickListener(){
		@Override
		public void onClick(View v) {
//			finish();
		}
		
	};
	
	private BaseLoaderCallback  mOpenCVCallBack = new BaseLoaderCallback(this) {
    	@Override
    	public void onManagerConnected(int status) {
    		switch (status) {
				case LoaderCallbackInterface.SUCCESS:
					Log.i("OpenCV", "OpenCV loaded successfully");
					
					// Load native library after(!) OpenCV initialization
					
					cameraView.setModifyView(modifyImage);
					cameraView.start();
				    
					break;
				default:
					super.onManagerConnected(status);
					break;
			}
    	}
	};
	
	public void pass(){
		if(typeS)
			player.stop();
		if(typeV)
			vibe.cancel();
		
		try{
			cameraView.pause();
		} catch(Exception e){
			
		}
		
		finish();
		
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	@Override
	public void onBackPressed(){
	}
}
