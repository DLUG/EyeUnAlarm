package org.dlug.android.eyeunalarm;


import java.util.List;
import java.util.Map;

import org.dlug.android.eyeunalarm.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;


public class AlarmList extends AlarmListActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_list);

		alarmListView = (ListView) findViewById(R.id.listAlarm);

		
		findViewById(R.id.btnAddAlarm).setOnClickListener(onClickAddAlarm);
		alarmListView.setOnItemClickListener(onClickList);
		alarmListView.setOnItemLongClickListener(onLongClickList);
		
		alarmListData = myDb.getAlarmList();

		alarmListAdapter = new AlarmListAdapter(AlarmList.this, alarmListData);
		alarmListView.setAdapter(alarmListAdapter);
		
		alarmListAdapter.notifyDataSetChanged();
	}

	private OnClickListener onClickAddAlarm = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(AlarmList.this, AlarmListAlarmAdd.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
			goNextHistory("AlarmSet", intent);
		}
	};
	
	private OnItemClickListener onClickList = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
		}
	};
	
	private OnItemLongClickListener onLongClickList = new OnItemLongClickListener(){
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			return false;
		}
	};
	
	public void onResume(){
		super.onResume();
	}
}