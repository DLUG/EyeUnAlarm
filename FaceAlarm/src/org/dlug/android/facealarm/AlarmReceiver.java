package org.dlug.android.facealarm;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    // TODO Auto-generated method stub
		Toast.makeText(context, "일어나라! 알람이다!",
    	        Toast.LENGTH_SHORT).show();
		
		
	}
	
}
