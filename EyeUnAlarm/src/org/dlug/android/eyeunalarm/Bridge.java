package org.dlug.android.eyeunalarm;

public class Bridge {
	private static AdapterAlarmList adapterAlarmList;
	private static boolean showedPopup = false;
	
	public static void setAdapterAlarmList(AdapterAlarmList adapterAlarmList){
		Bridge.adapterAlarmList = adapterAlarmList;
	}
	
	public static  AdapterAlarmList getAdapterAlarmList(){
		return adapterAlarmList;
	}
	
	public static boolean isShowedPopup(){
		return showedPopup;
	}
	
	public static void setShowedPopup(boolean showedPopup){
		Bridge.showedPopup = showedPopup;
	}
}
