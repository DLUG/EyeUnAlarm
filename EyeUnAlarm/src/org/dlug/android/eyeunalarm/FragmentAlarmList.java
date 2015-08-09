package org.dlug.android.eyeunalarm;

import org.dlug.android.eyeunalarm.alarm.ActivityAlarmPlayTest;

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
		
//		adapter = new AdapterAlarmList(getActivity());
		adapter = Bridge.getAdapterAlarmList();
//		Bridge.setAdapterAlarmList(adapter);
		
		viewAlarmList.setAdapter(adapter);
		
//		adapterAlarmList.notifyDataSetChanged();
		
		if(adapter.getCount() == 0 && !Bridge.isShowedPopup()){
			showMessage(R.string.empty_title, R.string.empty_message);
			Bridge.setShowedPopup(true);
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
			final int finalPosition = position;
			
			new AlertDialog.Builder(FragmentAlarmList.this.getActivity())
//			.setTitle(R.string.label_snooze)
//			.setIcon(R.drawable.clock)
			.setItems(new String[]{"Test", "Remove"}, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == 0){
						Intent i = new Intent(getActivity(), ActivityAlarmPlayTest.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.putExtra("dbIdx", adapter.getItem(finalPosition)._id);
						i.putExtra("isTest", true);
						getActivity().startActivity(i);

					} else if(which == 1){
						deleteTargetPosition = finalPosition;
						
						showQuestion(R.string.delete_title, R.string.delete_message, onClickQuestionOk);
					}
				}
			})
//			.setNegativeButton(R.string.btn_cancel, null)
			.show();
			
			return true;
		}
	};
	
	DialogInterface.OnClickListener onClickQuestionOk = new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int which) {
			int dbId = adapter.getItem(deleteTargetPosition)._id;

			AlarmController.deleteAlarm(dbId);
			
			adapter.reloadData();
			if(adapter.getCount() == 0){
				Bridge.setShowedPopup(false);
			}
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
