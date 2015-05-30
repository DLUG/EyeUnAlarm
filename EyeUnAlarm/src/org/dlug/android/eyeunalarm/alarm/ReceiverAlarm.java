package org.dlug.android.eyeunalarm.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverAlarm extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//broadcast sender id 
		int dbIdx = intent.getIntExtra("dbIdx", -1);
		Log.i("dbIdx", dbIdx + "");
		
		if(dbIdx == -1){
			return;
		}
		
		Intent i = new Intent(context, ActivityAlarmPlay.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("dbIdx", dbIdx);
		context.startActivity(i);
	}
}
