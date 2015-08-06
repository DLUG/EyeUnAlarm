package org.dlug.android.eyeunalarm.boot;

import org.dlug.android.eyeunalarm.AlarmController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverBoot extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			AlarmController.init(context);
			AlarmController.resetAlarm();
        }
	}
}
