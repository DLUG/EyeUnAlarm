package org.dlug.android.eyeunalarm;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.dlug.android.eyeunalarm.R;
import org.dlug.android.eyeunalarm.AlarmController.AlarmData;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterAlarmList extends BaseAdapter{
	private Context context;
	private List<AlarmData> alarmListData;
	private SparseArray<ViewHolder> mapHolder = new SparseArray<ViewHolder>();
	private LayoutInflater inflater; 
	
	GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
	GregorianCalendar gregorianCalendar;
	long alarmTime;
	
	public AdapterAlarmList(Context c) {
		super();
		this.context = c;
		this.alarmListData = AlarmController.getAlarmList();
		inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 }
	
	@Override
	public int getCount() {
		return alarmListData.size();
	}

	@Override
	public AlarmData getItem(int position) {
		return alarmListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder tmpHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_alarm_list, parent, false);
			
			tmpHolder = new ViewHolder();
			tmpHolder.viewAlarmTime = (TextView) convertView.findViewById(R.id.txtAlarmTime);
			tmpHolder.viewAlarmTitle = (TextView) convertView.findViewById(R.id.txtAlarmTitle);
			tmpHolder.viewAlarmState = (ImageView) convertView.findViewById(R.id.imageToggle);
			tmpHolder.viewTxtRepeat = (TextView) convertView.findViewById(R.id.txtRepeat);
			
			mapHolder.put(convertView.hashCode(), tmpHolder);
		} else {
			tmpHolder = mapHolder.get(convertView.hashCode());
		}
		AlarmData currentData = alarmListData.get(position); 
		String alarmTime = String.format("%02d", currentData.hours) + ":" + String.format("%02d", currentData.minutes);
		String alarmTitle = (String)currentData.alarmName;
		boolean alert_state;
		if((currentData.alertState) == 1){
			alert_state = true;
		} else {
			alert_state = false;
		}
		
		tmpHolder.viewAlarmTime.setText(alarmTime);
		tmpHolder.viewAlarmTitle.setText(alarmTitle);
		if(alert_state){
			tmpHolder.viewAlarmState.setImageResource(R.drawable.icon_alarm_on);
		} else {
			tmpHolder.viewAlarmState.setImageResource(R.drawable.icon_alarm_off);
		}
		tmpHolder.viewAlarmState.setOnClickListener(getOnClickAlarmState(position));
		
		int repeat = currentData.repeat;
		
		String[] weekday = context.getResources().getStringArray(R.array.weekday);
		
		if(repeat == 127){
			tmpHolder.viewTxtRepeat.setText(context.getString(R.string.everyday));
		} else if (repeat == 62){
			tmpHolder.viewTxtRepeat.setText(context.getString(R.string.workday));
		} else {
			String txtRepeat = "";
			
			if((repeat & 64) == 64)
				txtRepeat += weekday[0] + ", ";
			if((repeat & 32) == 32)
				txtRepeat += weekday[1] + ", ";
			if((repeat & 16) == 16)
				txtRepeat += weekday[2] + ", ";
			if((repeat & 8) == 8)
				txtRepeat += weekday[3] + ", ";
			if((repeat & 4) == 4)
				txtRepeat += weekday[4] + ", ";
			if((repeat & 2) == 2)
				txtRepeat += weekday[5] + ", ";
			if((repeat & 1) == 1)
				txtRepeat += weekday[6] + ", ";

			txtRepeat = txtRepeat.substring(0, txtRepeat.length() - 2);
			tmpHolder.viewTxtRepeat.setText(txtRepeat);
		}
		
		
		return convertView;
	}

	public void reloadData() {
		this.alarmListData = AlarmController.getAlarmList();
		this.notifyDataSetChanged();
	}
	
	private OnClickListener getOnClickAlarmState(final int position){
		OnClickListener onClickAlarmState = new OnClickListener(){
			@Override
			public void onClick(View v) {
				ImageView viewHandler = (ImageView) v;
				
				AlarmData alarmItemHandler = alarmListData.get(position);
				if(alarmItemHandler.alertState == 1){
					viewHandler.setImageResource(R.drawable.icon_alarm_off);
					alarmItemHandler.alertState = 0;
					
					AlarmController.disableAlarm(alarmItemHandler._id);
					
					String tmpMessage = context.getString(R.string.toast_alarm_disabled_prefix).replace("{AlarmName}", alarmItemHandler.alarmName);
					Toast.makeText(context, tmpMessage, Toast.LENGTH_SHORT).show();
				} else {
					viewHandler.setImageResource(R.drawable.icon_alarm_on);
					alarmItemHandler.alertState = 1;
					
					AlarmController.enableAlarm(alarmItemHandler._id);
					String tmpMessage = context.getString(R.string.toast_alarm_enabled_prefix).replace("{AlarmName}", alarmItemHandler.alarmName);
					Toast.makeText(context, tmpMessage, Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		return onClickAlarmState;
	}
	
	private static class ViewHolder {
		TextView viewAlarmTime;
		TextView viewAlarmTitle;
		ImageView viewAlarmState;
		TextView viewTxtRepeat;
	}
}
