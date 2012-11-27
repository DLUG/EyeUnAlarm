package org.dlug.android.facealarm;

import java.util.ArrayList;

import org.dlug.android.facealarm.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class AlarmAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MyData> arrData;
	private LayoutInflater inflater; // Inflater 객체 생성 
	/** Called when the activity is first created. */
	int cnt=0;
	
	
	
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
			   // 레이아웃에 맞게 적용 
			  }
			  
			  ImageView icon = (ImageView)convertView.findViewById(R.id.image);
			  icon.setImageResource(arrData.get(finalPosition).getImage());
			  
			  TextView name = (TextView)convertView.findViewById(R.id.name);
			  name.setText(arrData.get(finalPosition).getName());
			 
			  final ImageView imageToggle = (ImageView)convertView.findViewById(R.id.imageToggle);
			  imageToggle.setImageResource(R.drawable.on);
			  imageToggle.setOnClickListener(new OnClickListener(){
				
				public void onClick(View v) {
					cnt++;
					// TODO Auto-generated method stub
					if(cnt%2 ==0){
					imageToggle.setImageResource(R.drawable.on);
					cnt = 0;
					}
					else
					imageToggle.setImageResource(R.drawable.off);
				}
				  
				  
			  });
			  
		  return convertView;
	}

}
