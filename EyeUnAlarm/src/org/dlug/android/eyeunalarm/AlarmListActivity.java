package org.dlug.android.eyeunalarm;

import java.util.List;
import java.util.Map;

import android.util.Log;
import android.widget.ListView;

public class AlarmListActivity extends TabNavigationActivity{
	protected static ListView alarmListView;
	protected static List<Map<String, Object>> alarmListData;
	protected static AlarmListAdapter alarmListAdapter;

	protected static int selectItemPosition = 0;
	
	protected void listUpdate(){
		Log.i("Update", "Run");
		alarmListData = myDb.getAlarmList();
		alarmListAdapter.setData(alarmListData);
		alarmListAdapter.notifyDataSetChanged();
		alarmListView.setSelection(0);
	}
}
