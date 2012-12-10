package org.dlug.android.eyeunalarm;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

@SuppressWarnings("deprecation")
public class TabNavigationActivity extends MyActivity {
	  public void goNextHistory(String id,Intent intent)  {
          TabGroupFirst parent = ((TabGroupFirst)getParent());
          
		View view = parent.group.getLocalActivityManager()	 
        	.startActivity(id, intent) 
               .getDecorView();   
            parent.group.replaceView(view);
	  }
	  
	  public void goFreshHistory(String id,Intent intent){
		  TabGroupFirst parent = ((TabGroupFirst)getParent());
		  View view = parent.group.getLocalActivityManager()
				  .startActivity(id, intent)
				  .getDecorView();
		  parent.group.changeView(view);
	  }
  
	  @Override
	  public void onBackPressed() {
		  TabGroupFirst parent = ((TabGroupFirst)getParent());
            parent.back();
	  } 
}
