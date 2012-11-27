package org.dlug.android.facealarm;

public class MyData {
 private String time;
 private int icon;
 private int imageToggle;
 
 public MyData(int icon, String name){
	 this.icon = icon; 
	 this.time = name;
 }
 
 public String getName() {
	 return time;
 }
 
 public int getImage(){
	 return icon; 
 }
 public int getToggle(){
	 return imageToggle; 
 }
   
}

