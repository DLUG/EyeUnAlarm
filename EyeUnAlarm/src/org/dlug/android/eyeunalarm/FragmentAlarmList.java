package org.dlug.android.eyeunalarm;

import java.util.List;

import org.dlug.android.eyeunalarm.AlarmController.AlarmData;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class FragmentAlarmList extends Fragment {
	private ListView viewAlarmList;
	private AdapterAlarmList adapter;
	
	private int deleteTargetPosition;
	
	public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm_list, container, false);

		viewAlarmList = (ListView) rootView.findViewById(R.id.listAlarm);

		
		rootView.findViewById(R.id.btnAddAlarm).setOnClickListener(onClickAddAlarm);
		viewAlarmList.setOnItemClickListener(onClickList);
		viewAlarmList.setOnItemLongClickListener(onLongClickList);
		
		List<AlarmData> alarmListData = AlarmController.getAlarmList();
		adapter = new AdapterAlarmList(getActivity());
		Bridge.setAdapterAlarmList(adapter);
		
		viewAlarmList.setAdapter(adapter);
		
//		adapterAlarmList.notifyDataSetChanged();
		
		if(alarmListData.size() == 0){
			showMessage(R.string.empty_title, R.string.empty_message);
		}

        
        return rootView;
    }
    

    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	//first saving my state, so the bundle wont be empty. 
    	outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE"); 
    	super.onSaveInstanceState(outState); 
    }
    
    
	private OnClickListener onClickAddAlarm = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(FragmentAlarmList.this.getActivity(), ActivityAlarmAdd.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
			getActivity().startActivity(intent);
		}
	};
	
	private OnItemClickListener onClickList = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(FragmentAlarmList.this.getActivity(), ActivityAlarmModify.class);
			intent.putExtra("selectedAlarmId", adapter.getItem(position)._id);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
			getActivity().startActivity(intent);
		}
	};
	
	private OnItemLongClickListener onLongClickList = new OnItemLongClickListener(){
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			deleteTargetPosition = position;
			
			showQuestion(R.string.delete_title, R.string.delete_message, onClickQuestionOk);
			
			return true;
		}
	};
	
	DialogInterface.OnClickListener onClickQuestionOk = new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int which) {
			int dbId = adapter.getItem(deleteTargetPosition)._id;

			AlarmController.deleteAlarm(dbId);
			
			Bridge.getAdapterAlarmList().reloadData();
		}
	};
	
	private void showQuestion(int title, int message, DialogInterface.OnClickListener onClickListener){
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(getString(R.string.btn_confirm), onClickListener);
		builder.setNegativeButton(getString(R.string.btn_cancel), null);
		builder.show();
	}
	
	private void showMessage(int title, int message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(getString(R.string.btn_confirm), null);
		builder.show();
	}
}
