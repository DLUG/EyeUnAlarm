package org.dlug.android.eyeunalarm;

import com.nineoldandroids.animation.ObjectAnimator;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class LayoutForTouch extends RelativeLayout{
	DisplayMetrics dm;
	float density;
	
	/**
	 * CAUTION!
	 * X must higher than Y
	 */
	private int TOUCH_SENSING_VALUE_X = 6;
	private int TOUCH_SENSING_VALUE_Y = 5;
	
	private Rect RectEventTouch;
	
	private FrameLayout[] cards;
	
	public LayoutForTouch(Context context) {
		super(context);
		init(context);
	}

	public LayoutForTouch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	public LayoutForTouch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	FrameLayout fl1, fl2;
	
	private void init(Context context){
		dm = context.getResources().getDisplayMetrics();
		density = dm.density;
		
		TOUCH_SENSING_VALUE_X *= density;
		TOUCH_SENSING_VALUE_Y *= density;

		Log.d("LOG", "TOUCH_SENSING_VALUE_X: " + TOUCH_SENSING_VALUE_X);
		Log.d("LOG", "TOUCH_SENSING_VALUE_Y: " + TOUCH_SENSING_VALUE_Y);
		
		RectEventTouch = new Rect();
		
		cards = new FrameLayout[2];
		
		ViewConfiguration vc = ViewConfiguration.get(context);
	    mTouchSlop = vc.getScaledTouchSlop();
	    
	    fl1 = new FrameLayout(context);
	    fl1.setId(132435467);
	    fl1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    
	    ((FragmentActivity) context).getSupportFragmentManager()
        .beginTransaction()
        .add(132435467, new FragmentClock())
        .commit();
	    
	    fl2 = new FrameLayout(context);
	    fl2.setId(132435468);
	    fl2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    
	    ((FragmentActivity) context).getSupportFragmentManager()
        .beginTransaction()
        .add(132435468, new FragmentAlarmList())
        .commit();
	    
	    
	    
	    this.addView(fl1);
	    this.addView(fl2);
	    
	    
	    ObjectAnimator oa = ObjectAnimator.ofFloat(fl2, "rotationY", 0, 90);
		oa.setDuration(100);
		oa.setCurrentPlayTime(100);
		oa.start();
	    
	    viewWidth = this.getWidth();
	    viewHeight = this.getHeight();
	}
	
	 private int mTouchSlop;
	 private boolean mIsScrolling = false;
	 private int viewWidth;
	 private int viewHeight;
	 private int startX;
	 private int startY;

	 private int LockX;
	 private int LockY;

	 
	 private int currentX;
	 private int currentY;
	 

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         */

    	Log.i("intercept", "in");
    	ViewConfiguration vc = ViewConfiguration.get(getContext());
    	int mSlop = vc.getScaledTouchSlop();
    	int mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
    	int mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
    	
    	Log.i("rawX", ev.getRawX() + "");
    	Log.i("mtouchslop", mTouchSlop + "");
    	Log.i("mSlop", mSlop + "");
    	Log.i("mMinFlingVelocity", mMinFlingVelocity + "");
    	Log.i("mMaxFlingVelocity", mMaxFlingVelocity + "");
    	
    	startX = (int) ev.getRawX();
    	startY = (int) ev.getRawY();
    	
    	verticalScroll = false;
    	horizenScroll = false;

		oa1 = ObjectAnimator.ofFloat(fl1, "rotationY", 0, 90);
		oa1.setDuration(50);

		oa2 = ObjectAnimator.ofFloat(fl2, "rotationY", -90, 0);
		oa2.setDuration(50);

    	
    	return true;
    }

    ObjectAnimator oa1, oa2;
    
    private float thresholdRatioTransform = (float) 0.5;
    private float thresholdRatioSense = (float) 0.02;
    
    private boolean verticalScroll = false;
    private boolean horizenScroll = false;
    
    private float ratio;
    
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Here we actually handle the touch event (e.g. if the action is ACTION_MOVE, 
        // scroll this container).
        // This method will only be called if the touch event was intercepted in 
        // onInterceptTouchEvent
 //       ...
		
		int action = ev.getAction();
		

		
		if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE){
			LockX = startX;
			LockY = startY;
			
			currentX = (int) ev.getRawX();
			currentY = (int) ev.getRawY();
			if(verticalScroll){
				return false;
			}
			
			if(horizenScroll){
				
				ratio = (startX - ev.getRawX()) / startX;
				//Log.i("Ratio", ratio + "");
				if(ratio > 0){
					
					if(ratio < 0.5){
						oa1.setCurrentPlayTime((int)(100 * ratio));
						oa2.setCurrentPlayTime(0);
					} else if( ratio >= 0.5){
						oa2.setCurrentPlayTime((int)(100 * (ratio - 0.5)));
						oa1.setCurrentPlayTime(50);
					}
					
					
				}
				
				return true;
			}
			
			
			if(Math.abs(ev.getRawX() - startX) > ((float)this.getWidth() * thresholdRatioSense)){
				horizenScroll = true;
				Log.i("scrollType", "Horizen");
				return true;
			}
			
			if(Math.abs(ev.getRawY() - startY) > ((float)this.getWidth() * thresholdRatioSense)){
				verticalScroll = true;
				Log.i("scrollType", "Vertical");
				return true;
			}
		} else if(action == MotionEvent.ACTION_UP){
//			ratio = (LockX - ev.getRawX()) / LockX;
			
			Log.i("Ratio", ratio + "");
//			if(ratio > thresholdRatioTransform){
				if(ratio < 0.5){
//					oa1.setCurrentPlayTime((int)(100 * ratio));
					oa1.setFrameDelay((int)(100 * (ratio - 0.5)));

					oa1.reverse();
//					oa1.start();
				} else if( ratio >= 0.5){
					frame = (int)(100 * (ratio - 0.5));
					aniHandle();
				}

//			}
		}
		
		return true;
    }
	
	Handler aniHandler;
	void aniHandle(){
		if(aniHandler == null){
			aniHandler = new Handler();
		}
		aniHandler.post(increaseAni);
	}
	
	Runnable increaseAni = new Runnable(){
		@Override
		public void run() {
			if(frame < 50){
				oa2.setCurrentPlayTime(frame);
				frame += 2;
				aniHandler.post(increaseAni);
			}
		}
	};
	
	int frame;
}
