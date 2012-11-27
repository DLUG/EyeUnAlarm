package org.dlug.android.facealarm;


import java.util.ArrayList;
import java.util.Calendar;

import org.dlug.android.facealarm.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


public class AlarmData extends NavigationActivity  implements OnItemClickListener{

	 ListView list;
	 protected ArrayList<MyData> arrData= new ArrayList<MyData>();
	 protected AlarmAdapter adapter;
	 protected static int msgTag= 0;
	 protected static long id = 0;
	 protected static int itemPosition = 0;
	 protected static int setTag = 0;
	 String tempAPM;
	 final DbAdapter db = new DbAdapter(this);
	 
	 
	 static final int MESSAGE_DIALOG_ID =0;
	 
	 /*
	 public Handler chatHandler = new Handler(){
    	 @Override
    	 public void handleMessage(Message msg){
    		
    		 switch(msg.what){
    		 	case 1:
    		 		msgTag = 1; 
    		 		Toast.makeText(AlarmData.this, "msg = 1", Toast.LENGTH_SHORT).show();
    		 		 id = msg.arg1;
    		 		break;
    		 	case 2:
    		 		msgTag = 0; 
    		 		Toast.makeText(AlarmData.this, "msg = 0", Toast.LENGTH_SHORT).show();
    		 		break;
  
    		 		
    		 }
    	 }
    	 
     };
	 */
	

 @Override
 public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.alarm_data);
     
     //알람 등록 버튼 
     Button setBtn = (Button)findViewById(R.id.alarm_set);    
     list = (ListView)findViewById(R.id.list);
     
     
     adapter = new AlarmAdapter(AlarmData.this,arrData);
     list.setAdapter(adapter);
     
     
     
     db.open();
     //db.deleteAll();
     // 앱을 재실행 했을때 리스트목록이 없어지는 것을 방지하기 위해 매번 db 전체를 뿌려준다.
	 Cursor c = db.fetchAllBooks();
	 
	 if(c.moveToFirst()){
		 do{
			 tempAPM = apm(c.getInt(1));
			 setData(tempAPM + " "+ pad(c.getInt(1))+":"+pad(c.getInt(2)));
			 updateData();
		 }while(c.moveToNext());
	 }
	 db.close();
  

    list.setOnItemClickListener(this);
     
     setBtn.setOnClickListener(new OnClickListener(){
    	 
    	 public void onClick(View v){
    	
    		 Toast.makeText(AlarmData.this, "등록 액티비티 이동!", Toast.LENGTH_SHORT).show();
    		 
			 Intent intent = new Intent(AlarmData.this,AlarmSet.class);
    		
    		 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
    		
    		 goNextHistory("AlarmSet",intent);
    		 
    	 }
    	 
    	 
     });
     
 }
 
 
  @Override
 public void onPause(){
	 super.onPause();
	 /*
	 SharedPreferences sp = getSharedPreferences("sp_name_edit_text",MODE_PRIVATE);
	 arrData.add(new MyData(R.drawable.bluedol,sp.getString("sp_key_edit_text_value", "default")));
	 */
 }
 
 @Override
 public void onResume(){
	 super.onResume();
	
	 //AlarmSet activity에서 set버튼을 누른경우 msgTag = 1 로 변경 
	 if(msgTag == 1){
		 db.open();
		 Cursor c = db.fetchBook(id);
		 c.moveToFirst();
		 
		 tempAPM = apm(c.getInt(1));
		 setData(tempAPM + " " + pad(c.getInt(1))+ ":"+ pad(c.getInt(2)) );
		 updateData();
		 
		 db.close();
		 msgTag = 0;
	 }
	
 }
 

 
 public void updateData(){
	   //arrData.clear();
	   adapter.notifyDataSetChanged();
	 
 }
 
 protected void setData(String time ){
	 arrData.add(new MyData(R.drawable.clockv2,time));
	 
 }


public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	
	 Toast.makeText(getApplicationContext(), "ITEM CLICK = "+ position, Toast.LENGTH_SHORT).show();
	 
	 itemPosition = position;
	 setTag = 1;
	 
	 Intent intent = new Intent(AlarmData.this,AlarmSet.class);
		
	 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
	
	 goNextHistory("AlarmSet",intent);
	 
	 parent.setOnItemLongClickListener(new OnItemLongClickListener(){

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "LONG ITEM CLICK = "+ position, Toast.LENGTH_SHORT).show();
			
			
			DialogMessage(position);
			return false;
		}
		 
		 
	 });
	
}

private static String pad (int c){
	if(c >= 10){
		return String.valueOf(c);
	}
	else
		return "0" + String.valueOf(c);
}

private static String apm(int apm){
	  if( apm<12)
	   return "AM ";
	  else
	   return "PM ";
}

protected void DialogMessage(final int position){
	AlertDialog.Builder alt_dig = new AlertDialog.Builder(getParent());
	alt_dig.setMessage("Do you want to delete this alarm?")
	.setCancelable(false).setPositiveButton("Yes",
		new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int id){
			db.open();
			
			Cursor c = db.fetchAllBooks();
			c.moveToFirst();
			long tempId;
			int i=0;
			while(i<position){
				c.moveToNext();
				i++;
			}
			tempId = c.getLong(0);
			db.deleteBook(tempId);
			
			db.close();
			
			arrData.remove(position);
			updateData();	
		}
		
		}).setNegativeButton("No",
				new DialogInterface.OnClickListener() {	
		public void onClick(DialogInterface dialog, int id) {
						
		}
		});
	AlertDialog alert = alt_dig.create();
	//
	alert.setTitle("Delete");
	alert.show();
	
}


}
