package org.dlug.android.facealarm;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore.Audio;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    // TODO Auto-generated method stub
		int Id;
		
		//broadcast sender id 
		Id = intent.getFlags();
		
		Toast.makeText(context, "�Ͼ��! �˶��̴�! id = "+ Id,Toast.LENGTH_SHORT).show();
				
		
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
