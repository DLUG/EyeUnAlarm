package org.dlug.android.eyeunalarm.alarm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.dlug.android.eyeunalarm.AlarmController;
import org.dlug.android.eyeunalarm.R;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup.LayoutParams;
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
	protected int recogStrength = 10;
	protected boolean boolPassed = false;
	protected NotificationManager notificationManager;

	final long[] vibePattern = {1000, 200, 1000, 2000, 1200};

	CameraPreview cameraView;
	
	LinearLayout layoutPlayVideo;
	
	ImageView viewStart;
	public ProgressBar barRecogEye;

	MediaPlayer player;
	Ringtone ringtone;
	Vibrator vibe;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		AlarmController.init(this);

		if(prepareData() == false){
			finish();
			return;
		}

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		File xmlFile = new File(this.getFilesDir().getPath() + "/eyes.xml");
		if(!xmlFile.exists()){
			try {
				InputStream input = getAssets().open("haarcascade_eye.xml");
				byte[] data = new byte[input.available()];
				input.read(data);
				input.close();
				FileOutputStream output = openFileOutput("eyes.xml", Context.MODE_PRIVATE);
				output.write(data);
				output.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
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

		layoutPlayVideo = (LinearLayout)findViewById(R.id.layoutPlayVideo);
		layoutPlayVideo.setBackgroundColor(0);
		
		viewStart = new ImageView(this);
		viewStart.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		viewStart.setImageResource(R.drawable.clock);
		
		layoutPlayVideo.addView(viewStart);

		viewStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				layoutPlayVideo.setBackgroundColor(Color.BLACK);
				layoutPlayVideo.removeAllViews();
				layoutPlayVideo.addView(cameraView);
			}
		});
		
		try{
			cameraView = new CameraPreview(getApplicationContext(), 640, 480, this, recogStrength);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);

			cameraView.setLayoutParams(params);

		} catch(Exception e){
			Log.e("OpenCV", "Cannot connect to OpenCV Manager");
			Toast.makeText(this, R.string.toast_cannot_open_camera , 5).show();
			faultOpenVideo();
		}


		if(typeS){
			try{
				player = new MediaPlayer();
				if(bellURI.equals("")){
					bellURI = "content://settings/system/ringtone";
				}

				player.setAudioStreamType(AudioManager.STREAM_ALARM);
				player.setDataSource(this, Uri.parse(bellURI));

				player.setLooping(true);
				player.setVolume(volume/100f, volume/100f);
				player.prepare();
				player.start();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		if(typeV){
			vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vibe.vibrate(vibePattern, 0);
		}


		findViewById(R.id.btnSnooze).setOnClickListener(getButtonOnClickListener());
		barRecogEye = (ProgressBar) findViewById(R.id.barRecogEye);

		barRecogEye.setProgress(0);
		barRecogEye.setMax(100);
	}

	@Override
	protected void onResume() {
		notificationManager.cancel(0);

		super.onResume();
	}

	protected abstract boolean prepareData();	
	protected abstract OnClickListener getButtonOnClickListener();

	public void pass(){
		boolPassed = true;

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

		int dbIdx = this.getIntent().getIntExtra("dbIdx", -1);
		boolean snoozeMode = this.getIntent().getBooleanExtra("snoozeMode", false);
		if(snoozeMode && dbIdx >= 0){
			AlarmController.cancelAlarmManager(dbIdx * -1);
		}

		String toastMessage = getString(R.string.toast_dismissed).replace("{AlarmName}", title);
		Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
		
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

	protected void showNotification(ActivityAlarmPlayAbstract object){
	
		
		Intent restorentent = new Intent(object, object.getClass());
		restorentent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent restorePendingIntent = PendingIntent.getActivity(this, 0, restorentent, PendingIntent.FLAG_CANCEL_CURRENT);

		Notification tmpNotification = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.notification_icon)
		.setContentTitle(getString(R.string.message_still_alarming))
		.setContentText(getString(R.string.message_still_alarming2))
		.setContentIntent(restorePendingIntent)
		.build();

		tmpNotification.flags |= Notification.FLAG_ONGOING_EVENT;


		notificationManager.notify(0, tmpNotification);
	}
	
	@Override
	protected void onPause() {
		layoutPlayVideo.setBackgroundColor(0);
		layoutPlayVideo.removeAllViews();
		layoutPlayVideo.addView(viewStart);

		super.onPause();
	}
}