package org.dlug.android.eyeunalarm;

import android.app.TabActivity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
//import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabMain extends TabActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
       
        TabHost tabHost = getTabHost();
        
        tabHost.addTab(tabHost.newTabSpec("TabFirst")
        		.setIndicator("", getResources().getDrawable(R.drawable.alarm_list))
        		.setContent(new Intent().setClass(this, ViewCounselGroup.class)));

        tabHost.addTab(tabHost.newTabSpec("TabSecond")
        		.setIndicator("", getResources().getDrawable(R.drawable.clockv3))
        		.setContent(new Intent().setClass(this, TabGroupSecond.class)));
    }
}