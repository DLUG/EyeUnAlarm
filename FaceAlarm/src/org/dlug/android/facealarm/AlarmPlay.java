package org.dlug.android.facealarm;


import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AlarmPlay extends Activity implements OnClickListener, OnCompletionListener {
	Button snoozePlay;
	private MediaPlayer player;
	Vibrator vibe;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.alarm_play);
	    
	    snoozePlay = (Button)findViewById(R.id.snoozePlay);
	    player = MediaPlayer.create(getApplicationContext(),R.raw.withme);
	    
	    player.setOnCompletionListener(this);
	    
	    player.start();
	    
	    vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    long[] pattern = {1000, 200, 1000, 2000, 1200};
        vibe.vibrate(pattern, 0);
	    vibe.vibrate(10000);
	    
	    snoozePlay.setOnClickListener(this);
	    

	}

	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		player.stop();
		vibe.cancel();
		
	}

}
