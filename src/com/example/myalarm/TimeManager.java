package com.example.myalarm;

public class TimeManager {
public static int hoursManage(int current, int post){
	if(current>post){
		return post-current+24;
	}
	return post-current;
}
public static int minutesManage(int current, int post){
	if(current>post){
		return post-current+24;
	}
	return post-current;
}
public static String timeCorrector(int minutes){
	if(minutes<10){
		return "0"+minutes;
	}
	return ""+minutes;
}
}
