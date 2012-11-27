package org.dlug.android.facealarm;

import java.util.ArrayList;

import org.dlug.android.facealarm.AlarmSet.ListItem;

import org.dlug.android.facealarm.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class AlarmSetAdapter extends BaseAdapter {

	LayoutInflater mInflater;
     ArrayList<ListItem> arSrc;
     int cnt = 0;
     int soundTemp;
     SharedPreferences sp;
     
     //생성자
     public AlarmSetAdapter(Context context, ArrayList<ListItem> arItem){
           //인플레이트 준비를 합니다.
           mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           arSrc = arItem;
           
           //SharedPreferences 
           sp = context.getSharedPreferences("sp_pref",context.MODE_PRIVATE);
           
           
		  
     }
     
	public int getCount() {
		// TODO Auto-generated method stub
		return arSrc.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arSrc.get(position);
	}
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public int getItemViewType(int position){
        return arSrc.get(position).Type;
	}
	
	//getView가 생성하는 뷰의 개수를 리턴한다. 항상 같은 뷰를 생성하면 1을 리턴한다.
    public int getViewTypeCount(){
          return 5;
    }
    
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		int res = 0;
		
        //최초 호출이면 항목 뷰를 생성한다.
        //타입별로 뷰를 다르게 디자인 할 수 있으며 높이가 달라도 상관없다.
        if(convertView == null){
       
               res = getItemViewType(position);//현재 위치의 Type을 조사해보고
               switch(res){
               case 0://0이면 textedit
                      res = R.layout.snooze;
                      break;
               case 1://1이면 btnicon으로 R.layout값을 넣어주고
                      res = R.layout.repeat;
                      break;
               case 2:
             	  res = R.layout.sound;
             	  break;
               case 3:
             	  res = R.layout.time;
             	  break;
               case 4:
             	  res = R.layout.type;
             	  break;
               }
              
               
               //인플레이트. 즉 화면에 뿌림. 
               convertView = mInflater.inflate(res, parent, false);
        }
       
               //화면에 뿌린뒤 여기서 각항목에 해당하는 값을 바꿔주는 부분
               res = getItemViewType(position);
              
               switch(res){
               case 0:
            	   	TextView txt = (TextView)convertView.findViewById(R.id.snooze);
            	   	txt.setText(arSrc.get(position).Text);
            	   	TextView snoozeTime = (TextView)convertView.findViewById(R.id.snoozeTime); 
            	   	snoozeTime.setText("5minutes");
                      break;
               case 1:
             	  	TextView txt1 = (TextView)convertView.findViewById(R.id.repeat);
             	  	txt1.setText(arSrc.get(position).Text);           	  
                      break;
               case 2:
	           	  	TextView txt2 = (TextView)convertView.findViewById(R.id.sound);
	           	  	txt2.setText(arSrc.get(position).Text);
	           	  	SeekBar soundSeekbar = (SeekBar)convertView.findViewById(R.id.soundBar);
	           	  	soundSeekbar.setMax(100);
	           	  	soundSeekbar.incrementProgressBy(10);
	           	  	soundSeekbar.setOnSeekBarChangeListener(controlListener);
	                  break;
               case 3:
	           	  	TextView txt3 = (TextView)convertView.findViewById(R.id.time);
	           	  	txt3.setText(arSrc.get(position).Text);
	           		TextView timeView = (TextView)convertView.findViewById(R.id.timeView);
            		timeView.setText("AM 7:00");
	           	  	  break;
               case 4:
	           	  	TextView txt4 = (TextView)convertView.findViewById(R.id.type);
	           	  	txt4.setText(arSrc.get(position).Text);
	           	  	final ImageView typeToggle_s = (ImageView)convertView.findViewById(R.id.typeToggle_s);
	        	  	typeToggle_s.setImageResource(R.drawable.on);
	        	  	typeToggle_s.setOnClickListener(new OnClickListener(){
					
	        	  		public void onClick(View v) {
							cnt++;
							// TODO Auto-generated method stub
							if(cnt%2 ==0){
							typeToggle_s.setImageResource(R.drawable.on);
							cnt = 0;
							}
							else
							typeToggle_s.setImageResource(R.drawable.off);
						}
        	  	
	        	  	});
	        	  	final ImageView typeToggle_v = (ImageView)convertView.findViewById(R.id.typeToggle_v);
	        	  	typeToggle_v.setImageResource(R.drawable.on);
	        	  	typeToggle_v.setOnClickListener(new OnClickListener(){
 				
	        	  		public void onClick(View v) {
 						cnt++;
 						// TODO Auto-generated method stub
 						if(cnt%2 ==0){
 						typeToggle_v.setImageResource(R.drawable.on);
 						cnt = 0;
 						}
 						else
 						typeToggle_v.setImageResource(R.drawable.off);
 					}
         	  	
	        	  	});
	        	  	break;
               }
       
        return convertView;//getCount만큼 반복
        //리스트의 갯수만큼 반복하게 됩니다.
  }
	 //SeekBar
		private SeekBar.OnSeekBarChangeListener controlListener = new SeekBar.OnSeekBarChangeListener() {
		
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor editor = sp.edit();
					editor.putInt("sp_sound", soundTemp);
					editor.commit();
					//Toast.makeText(getParent(), "sound = " + soundTemp, Toast.LENGTH_SHORT).show();
					
				}
				
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO Auto-generated method stub
					soundTemp = progress;
				}
		};


}
