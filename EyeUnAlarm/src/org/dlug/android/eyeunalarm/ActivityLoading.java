package org.dlug.android.eyeunalarm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

public class ActivityLoading extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		AsyncTask<Void, Void, Void> loadingTask = new AsyncTask<Void, Void, Void>(){

			boolean bgSwitch = true;
			LinearLayout layout;
			
			@Override
			protected Void doInBackground(Void... params) {
				layout = (LinearLayout) findViewById(R.id.layoutLoading);

				
				File xmlFile = new File(ActivityLoading.this.getFilesDir().getAbsolutePath() + "/eyes.xml");
				if(!xmlFile.exists()){
					Log.d("Loading", "'eyes.xml' isn't exist!");
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
				
				
				AlarmController.init(ActivityLoading.this);
				
/*				
				alarmListData = myDb.getAlarmList();
				alarmListAdapter = new AlarmListAdapter(AlarmListLoading.this, alarmListData);

				alarmUpdate();
*/
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

				Intent intent = new Intent(ActivityLoading.this, ActivityMain.class);
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
