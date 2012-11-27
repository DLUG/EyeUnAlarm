package org.dlug.android.facealarm;

import java.util.ArrayList;


import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ToggleButton;

public class FirstGroup extends ActivityGroup {
	
	public static FirstGroup group; // Activity들이 접근하기 위한 Group
	
	private ArrayList<View> history; //View 들을 관리하기 위한 List
	
	public static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    this.history = new ArrayList<View>();
	    group = this;
	    context = this;
	}
	public void changeView(View v){ //동일한 Level의 Activity를 다른 Activity로 변환하는 경우 
		history.remove(history.size()-1);
		history.add(v);
		setContentView(v);
	}
	
	public void replaceView(View v){ // 새로운 Level의 Activity를 추가하는 경우 
		history.add(v);
		
		setContentView(v);
		
	}
	
	public void back(){ //Back Key가 눌려졌을 경우에 대한 처리 
		if(history.size() > 1){
			history.remove(history.size()-1);
			setContentView(history.get(history.size()-1));
		}else{
			finish(); //최상위 Level의 경우 TabActivity를 종료해야 한다 
		}
			
	}
		
	
	public void onBackPressed(){ // Back Key에 대한 Event Handler
			FirstGroup.group.back();
			return;
	}
}


