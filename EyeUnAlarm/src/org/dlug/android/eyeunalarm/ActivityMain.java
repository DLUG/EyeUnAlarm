package org.dlug.android.eyeunalarm;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class ActivityMain extends FragmentActivity{
	LinearLayout tabClock;
	LinearLayout tabList;
	
	FrameLayout container1;
	FrameLayout container2;
	
	Boolean tabClockStatus = true;
	Boolean tabListStatus = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		tabClock = (LinearLayout) findViewById(R.id.tabClock);
		tabList = (LinearLayout) findViewById(R.id.tabList);
		
		container1 = (FrameLayout) findViewById(R.id.container1);
		container2 = (FrameLayout) findViewById(R.id.container2);

		
		tabClock.setOnClickListener(onClickTabClock);
		tabList.setOnClickListener(onClickTabList);
		
		tabClock.setBackgroundColor(Color.rgb(128, 128, 0));
		
		
		if (savedInstanceState == null) {
			ObjectAnimator oa = ObjectAnimator.ofFloat(container2, "rotationY", 0, 90);
			oa.setDuration(100);
			oa.setCurrentPlayTime(100);
			oa.start();
			
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container1, new FragmentClock())
                    .commit();
            getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.container2, new FragmentAlarmList())
            .commit();
        }
	}
	
	private OnClickListener onClickTabClock = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(!tabClockStatus){
				ObjectAnimator oa1 = ObjectAnimator.ofFloat(container1, "rotationY", -90, 00);
				oa1.setDuration(100);
				oa1.setStartDelay(100);
				oa1.addListener(new AnimatorListener(){
					@Override
					public void onAnimationEnd(Animator animation) {
						tabClockStatus = true;
						tabListStatus = false;
						tabClock.setBackgroundColor(Color.rgb(128, 128, 0));
						tabList.setBackgroundColor(Color.rgb(0, 0, 0));
					}
					@Override
					public void onAnimationStart(Animator animation) {}
					@Override
					public void onAnimationCancel(Animator animation) {}
					@Override
					public void onAnimationRepeat(Animator animation) {}
				});
				
				ObjectAnimator oa2 = ObjectAnimator.ofFloat(container2, "rotationY", 0, 90);
				oa2.setDuration(100);

				oa1.start();
				oa2.start();
			}
		}
	};

	private OnClickListener onClickTabList = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(!tabListStatus){
				ObjectAnimator oa1 = ObjectAnimator.ofFloat(container2, "rotationY", -90, 00);
				oa1.setDuration(100);
				oa1.setStartDelay(100);
				oa1.addListener(new AnimatorListener(){
					@Override
					public void onAnimationEnd(Animator animation) {
						tabListStatus = true;
						tabClockStatus = false;
						tabList.setBackgroundColor(Color.rgb(128, 128, 0));
						tabClock.setBackgroundColor(Color.rgb(0, 0, 0));
					}
					@Override
					public void onAnimationStart(Animator animation) {}
					@Override
					public void onAnimationCancel(Animator animation) {}
					@Override
					public void onAnimationRepeat(Animator animation) {}
				});
				
				ObjectAnimator oa2 = ObjectAnimator.ofFloat(container1, "rotationY", 0, 90);
				oa2.setDuration(100);

				oa1.start();
				oa2.start();
			}
		}
	};
}
