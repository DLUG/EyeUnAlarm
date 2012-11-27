package org.dlug.android.facealarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ViewCounselGroup extends FirstGroup {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
          // TODO Auto-generated method stub
          super.onCreate(savedInstanceState);
          Intent intent = new Intent(this,AlarmData.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
          View view = getLocalActivityManager().startActivity("AlarmData",intent)
             .getDecorView();   
         replaceView(view);   
    }

    @Override
    public void onBackPressed() { // Back Key에 대한 처리 요청
          super.onBackPressed();
    }

}
