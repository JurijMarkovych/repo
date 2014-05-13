package com.example.myalarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class MyAlarm extends BroadcastReceiver {
	private final static String ACTION_COMMAND = "onetime";
	private AlarmManager alarmManager;
	

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceIntent=new Intent(context,PlayerService.class);
		if (intent.getIntExtra(ACTION_COMMAND, 0) == 1) {
			Log.d("OnRecieve", "start service");
			context.startService(serviceIntent);

		}
		if (intent.getIntExtra(ACTION_COMMAND, 0) == -1) {
			
			context.stopService(serviceIntent);
		}

		System.out.println(3434);
	}

	


	public void setAlarm(Context context) {
	/*	alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, MyAlarm.class);
		
		PendingIntent multiple = PendingIntent.getBroadcast(context, 0, intent,
				0);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), 1000 * 5, multiple);*/
		throw new UnsupportedOperationException();

	}

	public void cancelAlarm(Context context, Intent intent)
			throws CanceledException {

		intent.putExtra(ACTION_COMMAND, -1);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		context.sendBroadcast(intent);
		alarmManager.cancel(sender);

	}

	public void setOnetimeTimer(Context context, Intent intent, Long millis) {
		
		intent.putExtra(ACTION_COMMAND, 1);
		PendingIntent oneTime = PendingIntent.getBroadcast(context, 0, intent,
				0);
		Calendar calendar = Calendar.getInstance();
		
		alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
			
		alarmManager.set(AlarmManager.RTC_WAKEUP,
				(calendar.getTimeInMillis() + millis), oneTime);
		SharedPreferences settings = context.getSharedPreferences("SaveActivityState",
				0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("IsEnabled", true);
		editor.commit();
		// TODO: check with just created intent
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		Log.d("check", "comparation: " + oneTime.equals(pi));
	}

}
