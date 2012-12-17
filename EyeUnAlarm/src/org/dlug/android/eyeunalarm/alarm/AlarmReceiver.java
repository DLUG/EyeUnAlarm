package org.dlug.android.eyeunalarm.alarm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		int Id=0;
		
		//broadcast sender id 
		Id = intent.getFlags();
		Log.i("adsf","alarmReceiver : "+Id);
		
		Intent i = new Intent(context,AlarmPlay.class);
		i.setFlags(Id);
		PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
		
		try { 
           pi.send();
        } 
        catch (Exception e) { 
        }
	}

	
}
