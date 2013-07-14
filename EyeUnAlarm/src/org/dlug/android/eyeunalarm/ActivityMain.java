package org.dlug.android.eyeunalarm;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ActivityMain extends FragmentActivity {
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
		
//		container1 = (FrameLayout) findViewById(R.id.container1);
//		container2 = (FrameLayout) findViewById(R.id.container2);

		
		tabClock.setOnClickListener(onClickTabClock);
		tabList.setOnClickListener(onClickTabList);
		
		tabClock.setBackgroundColor(Color.rgb(128, 128, 0));

//		RelativeLayout fl = (RelativeLayout) findViewById(R.id.container);
//		LayoutForTouch lft = new LayoutForTouch(this);
		LayoutForTouch lft = (LayoutForTouch) findViewById(R.id.containerTest);
//		lft.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		fl.addView(lft);
		
/*		
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
*/
	}

	private class CardFlipPagerAdapter extends FragmentStatePagerAdapter {
		Fragment[] fragments;

		public CardFlipPagerAdapter(FragmentManager fm) {
			super(fm);

			fragments = new Fragment[2];
			fragments[0] = new FragmentClock();
			fragments[1] = new FragmentAlarmList();
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments[arg0];
		}

		@Override
		public int getCount() {
			return 2;
		}
	}

	private OnClickListener onClickTabClock = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!tabClockStatus) {
				ObjectAnimator oa1 = ObjectAnimator.ofFloat(container1,
						"rotationY", -90, 00);
				oa1.setDuration(100);
				oa1.setStartDelay(100);
				oa1.addListener(new AnimatorListener() {
					@Override
					public void onAnimationEnd(Animator animation) {
						tabClockStatus = true;
						tabListStatus = false;
						tabClock.setBackgroundColor(Color.rgb(128, 128, 0));
						tabList.setBackgroundColor(Color.rgb(0, 0, 0));
					}

					@Override
					public void onAnimationStart(Animator animation) {
					}

					@Override
					public void onAnimationCancel(Animator animation) {
					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}
				});

				ObjectAnimator oa2 = ObjectAnimator.ofFloat(container2,
						"rotationY", 0, 90);
				oa2.setDuration(100);

				oa1.start();
				oa2.start();
			}
		}
	};

	private OnClickListener onClickTabList = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!tabListStatus) {
				ObjectAnimator oa1 = ObjectAnimator.ofFloat(container2,
						"rotationY", -90, 00);
				oa1.setDuration(100);
				oa1.setStartDelay(100);
				oa1.addListener(new AnimatorListener() {
					@Override
					public void onAnimationEnd(Animator animation) {
						tabListStatus = true;
						tabClockStatus = false;
						tabList.setBackgroundColor(Color.rgb(128, 128, 0));
						tabClock.setBackgroundColor(Color.rgb(0, 0, 0));
					}

					@Override
					public void onAnimationStart(Animator animation) {
					}

					@Override
					public void onAnimationCancel(Animator animation) {
					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}
				});

				ObjectAnimator oa2 = ObjectAnimator.ofFloat(container1,
						"rotationY", 0, 90);
				oa2.setDuration(100);

				oa1.start();
				oa2.start();
			}
		}
	};
}
