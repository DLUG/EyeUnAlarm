package org.dlug.android.facealarm;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;


import java.util.ArrayList;

import org.dlug.android.facealarm.R;


public class Main extends android.app.TabActivity {
	
	public TabHost tabhost;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
       
        this.tabhost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;
        
        intent = new Intent().setClass(this,ViewCounselGroup.class);
        
        spec = tabhost.newTabSpec("FirstGroup").setIndicator("알람 ", 
        		getResources().getDrawable(R.drawable.googleicon))
        		.setContent(intent);

        tabhost.addTab(spec);
        
        
        intent = new Intent().setClass(this,SecondGroup.class);
        
        spec = tabhost.newTabSpec("SecondGroup").setIndicator("시계 ",
        		getResources().getDrawable(R.drawable.clockv2))
        		.setContent(intent);
        
        tabhost.addTab(spec);
      
        tabhost.setCurrentTab(0);
     
    }
}
