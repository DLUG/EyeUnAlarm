package org.dlug.android.eyeunalarm;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public abstract class MyActivity extends Activity{
	protected static MyDbHelper myDb = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		if(myDb == null){

			PackageManager pm = this.getPackageManager();
			PackageInfo packageInfo = null;
			try {
				packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			int VERSION = packageInfo.versionCode;

			
			myDb = new MyDbHelper(this, VERSION);
		}
	}
	
	public MyDbHelper getMyDb(){
		return myDb;
	}
}