package org.dlug.android.eyeunalarm;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Context;
import android.os.Bundle;
import android.view.*;

@SuppressWarnings("deprecation")
public class TabGroupFirst extends ActivityGroup {
	
	public static TabGroupFirst group;
	private ArrayList<View> history;
	public static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    this.history = new ArrayList<View>();
	    group = this;
	    context = this;
	}
	
	public void changeView(View v){
		history.remove(history.size()-1);
		history.add(v);
		setContentView(v);
	}
	
	public void replaceView(View v){
		history.add(v);
		
		setContentView(v);
	}
	
	@Override
	public void onBackPressed(){
			TabGroupFirst.group.back();
			return;
	}
	
	public void back(){
		if(history.size() > 1){
			history.remove(history.size()-1);
			setContentView(history.get(history.size()-1));
		} else {
			finish();
		}
	}
}


