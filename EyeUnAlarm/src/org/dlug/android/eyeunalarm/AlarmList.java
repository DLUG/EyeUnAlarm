package org.dlug.android.eyeunalarm;



import java.util.HashMap;
import java.util.Map;

import org.dlug.android.eyeunalarm.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;


public class AlarmList extends AlarmListActivity{
	int deletePosition = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_list);

		alarmListView = (ListView) findViewById(R.id.listAlarm);

		
		findViewById(R.id.btnAddAlarm).setOnClickListener(onClickAddAlarm);
		alarmListView.setOnItemClickListener(onClickList);
		alarmListView.setOnItemLongClickListener(onLongClickList);
		
		alarmListView.setAdapter(alarmListAdapter);
		
		alarmListAdapter.notifyDataSetChanged();
		
		if(alarmListData.size() == 0){
			showMessage(R.string.empty_title, R.string.empty_message);
		}
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
			selectAlarmId = (Integer) (alarmListData.get(position)).get("_id");
			
			Intent intent = new Intent(AlarmList.this, AlarmListAlarmModify.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
			goNextHistory("AlarmModify", intent);
		}
	};
	
	private OnItemLongClickListener onLongClickList = new OnItemLongClickListener(){
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			deletePosition = position;
			
			showQuestion(R.string.delete_title, R.string.delete_message, onClickQuestionOk);
			
			return false;
		}
	};
	
	DialogInterface.OnClickListener onClickQuestionOk = new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int which) {
			int dbId = (Integer) alarmListData.get(deletePosition).get(MyDbHelper.FIELD_ID);
			
			Map<String, Object> filter = new HashMap<String, Object>(2);

			alarmSet(getParent(), deletePosition, false);
			
			filter.put(MyDbHelper.FIELD_ID, dbId);
			myDb.delete(filter);
			listUpdate();
		}
	};
	
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	private void showQuestion(int title, int message, DialogInterface.OnClickListener onClickListener){
		AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(getString(R.string.btn_confirm), onClickListener);
		builder.setNegativeButton(getString(R.string.btn_cancel), null);
		builder.show();
	}
	
	private void showMessage(int title, int message){
		AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(getString(R.string.btn_confirm), null);
		builder.show();
	}
}