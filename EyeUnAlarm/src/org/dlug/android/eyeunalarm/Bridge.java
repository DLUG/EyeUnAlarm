package org.dlug.android.eyeunalarm;

public class Bridge {
	private static AdapterAlarmList adapterAlarmList;
	
	public static void setAdapterAlarmList(AdapterAlarmList adapterAlarmList){
		Bridge.adapterAlarmList = adapterAlarmList;
	}
	
	public static  AdapterAlarmList getAdapterAlarmList(){
		return adapterAlarmList;
	}
}
