package org.dlug.android.eyeunalarm.alarm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
	
	FrameLayout previewFrame;
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
		
		previewFrame = (FrameLayout) findViewById(R.id.PreviewFrame);
		modifyImage = (ImageView) findViewById(R.id.modifyImage);
		
		File xmlFile = new File("/data/data/org.dlug.android.eyeunalarm/files/eyes.xml");
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
		
		cameraView = new CameraPreview(getApplicationContext(), 640, 480, 100, this, recogStrength);
		
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mOpenCVCallBack))
        {
        	Log.e("OpenCV", "Cannot connect to OpenCV Manager");
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
				{
					Log.i("OpenCV", "OpenCV loaded successfully");
					
					// Load native library after(!) OpenCV initialization
					System.loadLibrary("DetectEye");
					
					previewFrame.removeAllViews();
					previewFrame.addView(cameraView);
					cameraView.setModifyView(modifyImage);
					
					if(typeS){
						player = MediaPlayer.create(getApplicationContext(), Uri.parse(bellURI));
						player.setVolume(volume, volume);
						player.setOnCompletionListener(new OnCompletionListener(){
							public void onCompletion(MediaPlayer arg0) {
								
							}
						});
					    player.start();
				    }
				    if(typeV){
				    	vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				    	long[] pattern = {1000, 200, 1000, 2000, 1200};
				    	vibe.vibrate(pattern, 0);
				    	vibe.vibrate(10000);
				    }
				    
				} break;
				default:
				{
					super.onManagerConnected(status);
				} break;
			}
    	}
	};
	
	public void pass(){
		if(typeS)
			player.stop();
		if(typeV)
			vibe.cancel();
		
		cameraView.pause();
		finish();
		
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
