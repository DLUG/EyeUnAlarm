package org.dlug.android.eyeunalarm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.LinearLayout;

public class AlarmListLoading extends AlarmListActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);

		AsyncTask<Void, Void, Void> loadingTask = new AsyncTask<Void, Void, Void>(){

			boolean bgSwitch = true;
			LinearLayout layout;
			
			@Override
			protected Void doInBackground(Void... params) {
				layout = (LinearLayout) findViewById(R.id.layoutLoading);

				
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
				
				
				
				
				
				alarmListData = myDb.getAlarmList();
				alarmListAdapter = new AlarmListAdapter(AlarmListLoading.this, alarmListData);

				alarmUpdate();
				try {
					Thread.sleep(150);
					publishProgress();
					Thread.sleep(150);
					publishProgress();
					Thread.sleep(1000);
					publishProgress();
					Thread.sleep(150);
					publishProgress();
					Thread.sleep(150);
					publishProgress();
					Thread.sleep(150);
					publishProgress();
					Thread.sleep(150);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Intent intent = new Intent(AlarmListLoading.this, TabMain.class);
				startActivity(intent);
				finish();

				
				return null;
			}
			
			 protected void onProgressUpdate(Void... progress) {
				try {
					if(bgSwitch){
						bgSwitch = false;
						layout.setBackgroundResource(R.drawable.loading02);
						layout.refreshDrawableState();
					} else {
						bgSwitch = true;
						layout.setBackgroundResource(R.drawable.loading01);
						layout.refreshDrawableState();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		};
		
		loadingTask.execute();
	}
}

