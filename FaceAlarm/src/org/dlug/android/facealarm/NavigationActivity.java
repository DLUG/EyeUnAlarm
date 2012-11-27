package org.dlug.android.facealarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NavigationActivity extends Activity {

	  public void goNextHistory(String id,Intent intent)  { //앞으로 가기 처리
          FirstGroup parent = ((FirstGroup)getParent());
          View view = parent.group.getLocalActivityManager()	 
        	.startActivity(id, intent) 
               .getDecorView();   
            parent.group.replaceView(view);
	  }
	  
	  public void goFreshHistory(String id,Intent intent){
		  FirstGroup parent = ((FirstGroup)getParent());
		  View view = parent.group.getLocalActivityManager()
				  .startActivity(id, intent)
				  .getDecorView();
		  parent.group.changeView(view);
	  }
  
	  @Override
	  public void onBackPressed() { //뒤로가기 처리
            FirstGroup parent = ((FirstGroup)getParent());
            parent.back();
	  } 
}
