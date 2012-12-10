package org.dlug.android.eyeunalarm;


import android.content.Intent;
import android.view.View;

@SuppressWarnings("deprecation")
public class TabNavigationActivity extends MyActivity {
	  public void goNextHistory(String id,Intent intent)  {
          
		View view = TabGroupFirst.group.getLocalActivityManager()	 
        	.startActivity(id, intent) 
               .getDecorView();   
            TabGroupFirst.group.replaceView(view);
	  }
	  
	  public void goFreshHistory(String id,Intent intent){
		  View view = TabGroupFirst.group.getLocalActivityManager()
				  .startActivity(id, intent)
				  .getDecorView();
		  TabGroupFirst.group.changeView(view);
	  }
  
	  @Override
	  public void onBackPressed() {
		  TabGroupFirst parent = ((TabGroupFirst)getParent());
            parent.back();
	  } 
}
