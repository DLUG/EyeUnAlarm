package org.dlug.android.facealarm;

import java.util.ArrayList;


import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ToggleButton;

public class FirstGroup extends ActivityGroup {
	
	public static FirstGroup group; // Activity���� �����ϱ� ���� Group
	
	private ArrayList<View> history; //View ���� �����ϱ� ���� List
	
	public static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    this.history = new ArrayList<View>();
	    group = this;
	    context = this;
	}
	public void changeView(View v){ //������ Level�� Activity�� �ٸ� Activity�� ��ȯ�ϴ� ��� 
		history.remove(history.size()-1);
		history.add(v);
		setContentView(v);
	}
	
	public void replaceView(View v){ // ���ο� Level�� Activity�� �߰��ϴ� ��� 
		history.add(v);
		
		setContentView(v);
		
	}
	
	public void back(){ //Back Key�� �������� ��쿡 ���� ó�� 
		if(history.size() > 1){
			history.remove(history.size()-1);
			setContentView(history.get(history.size()-1));
		}else{
			finish(); //�ֻ��� Level�� ��� TabActivity�� �����ؾ� �Ѵ� 
		}
			
	}
		
	
	public void onBackPressed(){ // Back Key�� ���� Event Handler
			FirstGroup.group.back();
			return;
	}
}


