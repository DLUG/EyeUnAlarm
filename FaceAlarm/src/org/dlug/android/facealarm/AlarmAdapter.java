package org.dlug.android.facealarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.dlug.android.facealarm.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

public class AlarmAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MyData> arrData;
	private LayoutInflater inflater; // Inflater ��ü �� 
	/** Called when the activity is first created. */
	int state;
	
	GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
	GregorianCalendar gregorianCalendar;
	long alarmTime;
	
	public AlarmAdapter(Context c, ArrayList<MyData> arr) {
		 this.context = c;
		 this.arrData = arr;
		 inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 }
	public int getCount() {
		// TODO Auto-generated method stub
		return arrData.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrData.get(position).getName();
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int finalPosition = position;
		
		
		if(convertView == null){
			   convertView = inflater.inflate(R.layout.alarm_row, parent, false); 
			   // ���̾ƿ��� �°� ��� 
			  }
		final DbAdapter db = new DbAdapter(context);
		
		//db open ���� �˶� list���� on/off ������ db�� stater���� �޾ƿͼ� �˶��Ŵ��� set/release
		db.open();
		Cursor c = db.fetchAllBooks();
		c.moveToFirst();
		int i =0;
		int temp = finalPosition;
		
		while(i< temp){
			c.moveToNext();
			i++;
		}
		state = c.getInt(9);
		
		db.close();
	  ImageView icon = (ImageView)convertView.findViewById(R.id.image);
	  icon.setImageResource(arrData.get(finalPosition).getImage());
	  
	  TextView name = (TextView)convertView.findViewById(R.id.name);
	  name.setText(arrData.get(finalPosition).getName());
	 
	  final ImageView imageToggle = (ImageView)convertView.findViewById(R.id.imageToggle);
	  if(state == 1)// state�� 1�϶� 
		  imageToggle.setImageResource(R.drawable.icon_on);
	  else
		  imageToggle.setImageResource(R.drawable.icon_off);
	  //finalPosition ���� �޾ƿ��� ���ؼ� setTag()�� �̿� getTag()�� �޾ƿ´�. 
	  
	  imageToggle.setTag(finalPosition);
	  imageToggle.setOnClickListener(new OnClickListener(){
		public void onClick(View v) {
			
			final DbAdapter db = new DbAdapter(context);
			db.open();
			
			Cursor c = db.fetchAllBooks();
			c.moveToFirst();
			long tempId;
			int mHour;
			int mMinute;
			int i=0;
			int temp = (Integer) v.getTag();
			
			while(i< temp){
				c.moveToNext();
				i++;
			}
			tempId = c.getLong(0);
			mHour = c.getInt(1);
			mMinute = c.getInt(2);
			state = c.getInt(9);
			// set �������� �˶��ð� get
		
			
			gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
	        
    	    int currentYY = currentCalendar.get(Calendar.YEAR);
    	  	int currentMM = currentCalendar.get(Calendar.MONTH);
    	  	int currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);
    	  	
    	  	gregorianCalendar.set(currentYY, currentMM, currentDD, mHour, mMinute, 00);

    	  	if(gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
    	  		gregorianCalendar.set(currentYY, currentMM, currentDD+1, mHour, mMinute,00);
    	  		Log.i("TAG",gregorianCalendar.getTimeInMillis()+":");
    	  	}
    	  	
    	  	alarmTime = gregorianCalendar.getTimeInMillis();
			
			if(c.getInt(9)==0){ // alarm on ����
			imageToggle.setImageResource(R.drawable.icon_on); // on image ��ȯ 
			Toast.makeText(context, v.getTag()+"", Toast.LENGTH_SHORT).show();
			
			state = 1; // state �� 1 
			db.updateState(tempId, state); //db state 1
			
			AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
			Intent intent = new Intent(context, AlarmReceiver.class);
			Toast.makeText(context, "tempId : "+tempId,Toast.LENGTH_SHORT).show();
			PendingIntent pIntent = PendingIntent.getBroadcast(context, (int) tempId, intent,
																PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alarmTime, 24*1000*60*60, pIntent);
			//�˶��Ŵ��� set
			
			}
			else{
			imageToggle.setImageResource(R.drawable.icon_off); // on image ��ȯ
			
			state= 0; // state 0 
			db.updateState(tempId, state);  // db state 0 
	
			AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
			Intent intent = new Intent(context,AlarmReceiver.class);
			Toast.makeText(context, "tempId : "+tempId, Toast.LENGTH_SHORT).show();
			PendingIntent pIntent = PendingIntent.getBroadcast(context, (int) tempId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			alarmManager.cancel(pIntent);
			//�˶��Ŵ��� release
			
			}
			db.close();
		}
	
		  
	  });
			  
		  return convertView;
	}

}
