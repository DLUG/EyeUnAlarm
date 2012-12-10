package org.dlug.android.eyeunalarm;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
//import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabMain extends TabActivity {
	
//	private TabHost tabHost;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
       
        TabHost tabHost = getTabHost();
//        TabSpec spec;
        
        tabHost.addTab(tabHost.newTabSpec("TabFirst")
        		.setIndicator("", getResources().getDrawable(R.drawable.clock))
        		.setContent(new Intent().setClass(this, ViewCounselGroup.class)));

        tabHost.addTab(tabHost.newTabSpec("TabSecond")
        		.setIndicator("", getResources().getDrawable(R.drawable.clock))
        		.setContent(new Intent().setClass(this, TabGroupSecond.class)));
        
        
/*        intent = new Intent().setClass(this, TabGroupFirst.class);
        
        spec = tabHost.newTabSpec("AlarmList").setIndicator("", 
        		getResources().getDrawable(R.drawable.clock))
        		.setContent(intent);

        tabHost.addTab(spec);
        
        
        intent = new Intent().setClass(this, TabGroupSecond.class);
        
        spec = tabHost.newTabSpec("TabGroupSecond").setIndicator("",
        		getResources().getDrawable(R.drawable.clockv2))
        		.setContent(intent);
        
        tabHost.addTab(spec);

             tabHost.setCurrentTab(0);
 */
    }
}