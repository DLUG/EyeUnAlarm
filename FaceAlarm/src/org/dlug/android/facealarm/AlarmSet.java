package org.dlug.android.facealarm;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.dlug.android.facealarm.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AlarmSet extends AlarmData implements OnItemClickListener{
	
	ArrayList<ListItem> arItem;
	AlarmSetAdapter MyAdapter;
	Button cancelBtn;
	Button confirmBtn;
	String repeatTag;
	SharedPreferences sp;
	
	//Dialog
	static final int TIME_DIALOG_ID = 0;
	static final int SNOOZE_DIALOG_ID = 1;
	static final int REPEAT_DIALOG_ID =4;
	
	//시간,스누즈,타입,사운드,반복 
	private int mHour;
	private int mMinute;
	private int snooze;
	private int sound;
	int snoTemp;
	
	
	//AlarmManager
	GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
	
	private NotificationManager Noti;
	
     /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.alarm_set);
     
      //DbAdapter클래스 인스턴스 생성 
      final DbAdapter db = new DbAdapter(this);
      
      //데이터를 만드는 부분
      arItem = new ArrayList<ListItem>();
      arItem.add(new ListItem(3, "알람 시간"));
      arItem.add(new ListItem(0, "snooze"));
      arItem.add(new ListItem(4, "타입"));
      arItem.add(new ListItem(2, "사운드"));
      arItem.add(new ListItem(1, "반복"));
      
      //어댑터를 만들고
      MyAdapter = new AlarmSetAdapter(this, arItem);

      //리스트뷰를 만들고
      ListView MyList;
      MyList = (ListView)findViewById(R.id.list);
      MyList.setAdapter(MyAdapter);//이어줍니다.
      
      MyList.setOnItemClickListener(this);
    	 
      //버튼 리스너 
      confirmBtn = (Button)findViewById(R.id.alarm_confirm);
      cancelBtn = (Button)findViewById(R.id.alarm_cancel);
      
      //timePicker 
      final Calendar calDateTime = Calendar.getInstance();
      mHour = calDateTime.get(Calendar.HOUR_OF_DAY);
      mMinute = calDateTime.get(Calendar.MINUTE);
      updateDisplay();
      
      //SharedPreferences
      sp = getSharedPreferences("sp_pref",MODE_PRIVATE);
      //releaseAlarm(AlarmSet.this);
      
      Noti = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      
     
      confirmBtn.setOnClickListener(new OnClickListener(){
    	  public void onClick(View v){
    		  //db save
    		  sound = sp.getInt("sp_sound", 00);
 
    		  db.open();
    		  
    	 
    	      id = db.createBook(   		  
    	    		  mHour,
    	    		  mMinute,
    	    		  "always",
    	    		  snooze,
    	    		  sound,
    	    		  1,
    	    		  1
    	    		  );
    	      
    	      Cursor c = db.fetchBook(id);
    	      
    	     
    	      if (c.moveToFirst()) 
    	        DisplayTitle(c);
    	      else
    	        Toast.makeText(AlarmSet.this, "No title found",
    	        Toast.LENGTH_SHORT).show();

    	      db.close();

    	      /*
    	      Message msg = new Message();
    	      msg.what = 1;
    	      msg.arg1 = (int) id;
    	      //msgTag = 1;
    	      chatHandler.sendMessage(msg);
    	      */
    	    
    	      msgTag = 1;
    	      
    	      //AlarmManager 설
    	      setAlarm(AlarmSet.this,snooze);
    	      
    	      Intent intent = new Intent(AlarmSet.this,AlarmData.class);
    	      
  		  	  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
  		  	 
  		  	  goFreshHistory("AlarmData",intent);
  		  	 
    	  }

    	  
      });
      
      cancelBtn.setOnClickListener(new OnClickListener(){
    	  public void onClick(View v){
    		
    		  /*
    		  Message msg = new Message();
    	      msg.what = 2;
    	     
    	      chatHandler.sendMessage(msg);
    		  */
    		  msgTag = 0;
    		 // releaseAlarm(AlarmSet.this);
    		  Intent intent = new Intent(AlarmSet.this,AlarmData.class);
    		  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
    		  
    		  onBackPressed();
    		  
    		     	  
    	  }
 
    	  
      });
      
    
      
    
  }
  
  @Override
  public void onResume(){
	  super.onResume();
	  
	  if(setTag == 0){
		  
		  
	  }
	  else
	  {
		
		  /*
		  //editSet();
		  TextView snoozeTime = (TextView)findViewById(R.id.snoozeTime);
		   
		  
		  db.open();
		  Cursor c =  db.fetchAllBooks();
		  
		  int i=0;
		  if(c.moveToFirst()){
			 do{
				 if(i<itemPosition)
				 {}
				else
					 break;
				 i++;
			 }while(c.moveToNext());
		  }
		  
		  mHour = c.getInt(1);
		  mMinute = c.getInt(2);
		  snooze = c.getInt(4);
		  //sound = c.getInt(5);
		  
		  db.close();
		  
		  snoozeTime.setText(c.getInt(4));
		 //apm(mHour)+ " " + pad(mHour) + ":" + pad(mMinute)
		 */
	  }
	  
  }
  public void editSet(){
	  TextView timeView = (TextView)findViewById(R.id.timeView);
	  timeView.setText("am 11:00");
	
  }

  	//알람 등록
  	private void setAlarm(Context context, long second){
  
  		Toast.makeText(getApplicationContext(), "setAlarm()", Toast.LENGTH_SHORT).show();
  		
  		AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
  		
  		//설정 일시 
  		GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
	      
	    int currentYY = currentCalendar.get(Calendar.YEAR);
		int currentMM = currentCalendar.get(Calendar.MONTH);
		int currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);
		
		gregorianCalendar.set(currentYY, currentMM, currentDD, mHour, mMinute, 00);

		if(gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
			gregorianCalendar.set(currentYY, currentMM, currentDD+1, mHour, mMinute,00);
			Log.i("TAG",gregorianCalendar.getTimeInMillis()+":");
		}
  		
  		Intent intent = new Intent(context, AlarmReceiver.class);
  		
  		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  		
  		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,gregorianCalendar.getTimeInMillis(), snooze*1000*60, pIntent);
  	}
  	
  	//알람 해제 
  	private void releaseAlarm(Context context){
  		AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
  		Intent intent = new Intent(context,AlarmReceiver.class);
  		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
  		alarmManager.cancel(pIntent);
  	}
  
  	/*
  	 private PendingIntent pendingIntent() {
     	Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
     	PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
     	
     	int icon = R.drawable.bluedol;
     	String tickerText = "알람테스트";
     	long when = System.currentTimeMillis();
     	
     	Notification notifi = new Notification(icon, tickerText, when);
     	notifi.sound = Uri.parse("file:/system.media.audio/ringtones/ringer.mp3");
     	//notifi.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
     	notifi.vibrate = new long[]{1000};
     	notifi.setLatestEventInfo(AlarmSet.this, "AlarmTest", "제발 울려라!!", pi);
     	Noti.notify(1234, notifi);
     	
     	return pi;
     }
     */
	
  	//리스트뷰 리스너
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		 if(position == 0)
		 {
			 showDialog(TIME_DIALOG_ID);
			 
		 }
		 else if(position == 1)
		 {
			
			 showDialog(SNOOZE_DIALOG_ID);
		 }		
		 else if(position == 3)
		 {

		 }
		 else if(position == 4)
		 {
			 showDialog(REPEAT_DIALOG_ID);
		 }
		 
		 Toast.makeText(getApplicationContext(), "ITEM CLICK = "+ position, Toast.LENGTH_SHORT).show();
         
		
	
	}
	
	private void updateDisplay(){
		
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


	//TimePickerDialog 정의 
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
			new TimePickerDialog.OnTimeSetListener(){
				
		public void onTimeSet(TimePicker view, int hourOfDay, int minute){
			/*
			SharedPreferences.Editor editor = sp.edit();
			editor.putInt("sp_time_hour", mHour);
			editor.putInt("sp_time_minutes", mMinute);
			editor.commit();
			*/
			
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		
			
			TextView timeView = (TextView)findViewById(R.id.timeView);
       	  	timeView.setText(new StringBuilder().append(apm(mHour)).append(pad(mHour)).append(":").append(pad(mMinute)));
         
		}
			
	};
	//다이얼로그 정의 
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
		case TIME_DIALOG_ID:
			return new TimePickerDialog(getParent(), mTimeSetListener, mHour, mMinute, false );
		case SNOOZE_DIALOG_ID:
			String[] snoozes={"5분 후에", "10분 후에", "15분 후에", "20분 후에", "30분 후에"};
		
			
			return new AlertDialog.Builder(getParent())
				.setTitle("Snooze")
				.setPositiveButton("선택", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						
						snooze =snoTemp;
						
						TextView snoozeTime = (TextView)findViewById(R.id.snoozeTime);
						snoozeTime.setText(snooze + "minutes");
						dialog.dismiss();
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				})
				.setSingleChoiceItems(snoozes, -1, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						
						switch( which ){
						case 0: snoTemp = 5; break;
						case 1: snoTemp = 10; break;
						case 2: snoTemp = 15; break;
						case 3: snoTemp = 20; break;
						case 4: snoTemp = 30; break;
						}
					}
				}).create();
		case REPEAT_DIALOG_ID:
			String[] repeats={"월요일마다",
							  "화요일마다",
							  "수요일마다",
							  "목요일마다",
							  "금요일마다",
							  "토요일마다",
							  "일요일마다"
						   	  };
			return new AlertDialog.Builder(getParent())
            .setTitle("Repeat")
            .setPositiveButton("선택", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						final ImageView repeatToggle = (ImageView)findViewById(R.id.repeatToggle);
	               	  	repeatToggle.setImageResource(R.drawable.on);
						dialog.dismiss();
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				})
            .setMultiChoiceItems(repeats, new boolean[]{true,true,true,true,true,true,true}, 
                    new DialogInterface.OnMultiChoiceClickListener(){

                public void onClick(DialogInterface dialog, int which,
                        boolean isChecked) {
                                       
                }
                
                 
            }).create();
		}
		
		return null;
	}
	
	public void DisplayTitle(Cursor c)
	{
	Toast.makeText(this,
	"id: " + c.getString(0) + "\n" +
	"HOUR: " + c.getString(1) + "\n" +
	"MIN: " + c.getString(2) + "\n" +
	"REPEAT: " + c.getString(3) +"\n"+
	"SNOOZE: " + c.getString(4) +"\n"+
	"SOUND: " + c.getString(5) + "\n" + 
	"TYPE_S: " + c.getString(6) +"\n"+
	"TYPE_M: " + c.getString(7) +"\n",
	Toast.LENGTH_LONG).show(); 
	}
  


class ListItem{
     int Type;//항목뷰를 어떤걸 나타낼 것인지 0이면 textedit.xml 1이면 btnicon.xml
     String Text;
     String setText;
    
     ListItem(int aType, String aText){
           Type = aType;
           Text = aText;
     }
}

}

