package org.dlug.android.eyeunalarm;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;

public class AlarmListLoading extends AlarmListActivity{
	Context thisContext;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		thisContext = this;
	}
	
	public void onResume(){
		super.onResume();
		
		
		AsyncTask<Void, Void, Void> loadingTask = new AsyncTask<Void, Void, Void>(){

			boolean bgSwitch = true;
			LinearLayout layout;
			
			@Override
			protected Void doInBackground(Void... params) {
				layout = (LinearLayout) findViewById(R.id.layoutLoading);
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

