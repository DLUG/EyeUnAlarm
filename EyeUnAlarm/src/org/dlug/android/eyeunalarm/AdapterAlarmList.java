package org.dlug.android.eyeunalarm;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.dlug.android.eyeunalarm.R;
import org.dlug.android.eyeunalarm.AlarmController.AlarmData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterAlarmList extends BaseAdapter{
	private Context context;
	private List<AlarmData> alarmListData;
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
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_alarm_list, parent, false);
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
		
		TextView alarmTimeView = (TextView) convertView.findViewById(R.id.txtAlarmTime);
		TextView alarmTitleView = (TextView) convertView.findViewById(R.id.txtAlarmTitle);
		ImageView alarmStateView = (ImageView) convertView.findViewById(R.id.imageToggle);

		alarmTimeView.setText(alarmTime);
		alarmTitleView.setText(alarmTitle);
		if(alert_state){
			alarmStateView.setImageResource(R.drawable.icon_alarm_on);
		} else {
			alarmStateView.setImageResource(R.drawable.icon_alarm_off);
		}
		alarmStateView.setTag(position);

		alarmStateView.setOnClickListener(onClickAlarmState);
		
		TextView txtRepeat = (TextView) convertView.findViewById(R.id.txtRepeat);
		
		int repeat = (Integer) currentData.repeat;
		
		String[] weekday = context.getResources().getStringArray(R.array.weekday);
		
		if(repeat == 127){
			txtRepeat.setText(context.getString(R.string.everyday));
		} else if (repeat == 62){
			txtRepeat.setText(context.getString(R.string.workday));
		} else {
			String result = "";
			
			if((repeat & 64) == 64)
				result += weekday[0] + ", ";
			if((repeat & 32) == 32)
				result += weekday[1] + ", ";
			if((repeat & 16) == 16)
				result += weekday[2] + ", ";
			if((repeat & 8) == 8)
				result += weekday[3] + ", ";
			if((repeat & 4) == 4)
				result += weekday[4] + ", ";
			if((repeat & 2) == 2)
				result += weekday[5] + ", ";
			if((repeat & 1) == 1)
				result += weekday[6] + ", ";

			result = result.substring(0, result.length() - 2);
			txtRepeat.setText(result);
		}
		
		
		return convertView;
	}

	public void reloadData() {
		this.alarmListData = AlarmController.getAlarmList();
		this.notifyDataSetChanged();
	}
	
	private OnClickListener onClickAlarmState = new OnClickListener(){
		@Override
		public void onClick(View v) {
			ImageView viewHandler = (ImageView) v;
			int position = (Integer) v.getTag();
			
			AlarmData alarmItemHandler = alarmListData.get(position);
			if(alarmItemHandler.alertState == 1){
				viewHandler.setImageResource(R.drawable.icon_alarm_off);
				alarmItemHandler.alertState = 0;
				
				AlarmController.disableAlarm(position);
			} else {
				viewHandler.setImageResource(R.drawable.icon_alarm_on);
				alarmItemHandler.alertState = 1;
				
				AlarmController.enableAlarm(position);
			}
		}
	};
	
	
}
