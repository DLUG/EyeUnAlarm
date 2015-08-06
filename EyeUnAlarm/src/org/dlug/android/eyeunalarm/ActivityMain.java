package org.dlug.android.eyeunalarm;

import java.util.ArrayList;
import java.util.List;

import org.dlug.android.eyeunalarm.R.color;

import info.mabin.android.dolmantab.DolmanTabAdapter;
import info.mabin.android.dolmantab.DolmanTabInterface;
import info.mabin.android.dolmantab.DolmanTabLayout;
import info.mabin.android.dolmantab.DolmanTabWidget;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ActivityMain extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DolmanTabLayout tabLayout = (DolmanTabLayout) findViewById(R.id.tabLayout);
		DolmanTabWidget tabWidget = (DolmanTabWidget) findViewById(R.id.tabWidget);
		
		TabsAdapter tabsAdapter = new TabsAdapter(this, tabLayout);

		tabLayout.setTabWidget(tabWidget);
		tabLayout.setPageAnimator(DolmanTabInterface.PageAnimator.CARD_FLIP);
		
		ImageView tabAlarmListImage = new ImageView(this);
		tabAlarmListImage.setImageResource(R.drawable.tab_alarm_list);
		tabAlarmListImage.setScaleType(ScaleType.CENTER_CROP);
		
		tabsAdapter.addTab(tabWidget.newTab().setIcon(R.drawable.tab_clock), FragmentClock.class, null);
		tabsAdapter.addTab(tabWidget.newTab().setCustomView(tabAlarmListImage), FragmentAlarmList.class, null);
		
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		linearLayout.setBackgroundColor(color.primaryDark);
		
		tabWidget.setIndicator(R.layout.tab_indicator);
	}

	private class TabsAdapter extends DolmanTabAdapter{
		private List<Fragment> listFragment = new ArrayList<Fragment>();

		public TabsAdapter(Context context, DolmanTabLayout layout) {
			super(context, layout);
		}

		@Override
		public void addTab(DolmanTabWidget.Tab tab, Class<?> clss, Bundle args){
			Fragment tabFragment = Fragment.instantiate(ActivityMain.this, clss.getName());
			listFragment.add(tabFragment);

			super.addTab(tab, clss, args);
		}

		@Override
		public Fragment getItem(int position) {
			return listFragment.get(position);
		}

		@Override
		public int getCount() {
			return listFragment.size();
		}

		@Override
		public void onTabSelected(int position, DolmanTabLayout layout) {
			Log.d("onTabSelected", "Position: " + position);
			layout.setCurrentTab(position);
		}

		@Override
		public void onTabUnselected(int position, DolmanTabLayout layout) {
			Log.d("onTabUnSelected", "Position: " + position);
		}

		@Override
		public void onTabReselected(int position, DolmanTabLayout layout) {
			Log.d("onTabReSelected", "Position: " + position);
		}

		@Override
		public void onPageSelected(int position, DolmanTabWidget widget) {
			widget.setCurrentTab(position);
		}

		@Override
		public void onPageUnselected(int position, DolmanTabWidget widget) {}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				DolmanTabWidget widget) {
			widget.moveTabLocation(position, positionOffset);
		}
	}
}
