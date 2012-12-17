package org.dlug.android.eyeunalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

@SuppressWarnings("deprecation")
public class ViewCounselGroup extends TabGroupFirst {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          Intent intent = new Intent(this,AlarmList.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
		View view = getLocalActivityManager().startActivity("AlarmList", intent)
        		  .getDecorView();
         replaceView(view);
    }

    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	AlarmListActivity tmp = (AlarmListActivity) this.getCurrentActivity();
    	tmp.onActivityResultTrigger(requestCode, resultCode, data);
    }
}
