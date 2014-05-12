package com.example.myalarm;

import java.io.File;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

public class PlayerService extends Service {
	private MediaPlayer player;
	private Vibrator v;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS)
						+ "/1.mp3");
		AudioManager audioManager = (AudioManager) getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
		Uri uri = Uri.fromFile(file);

		player = MediaPlayer.create(getApplicationContext(), uri);

		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.stop();
				mp.release();

			}
		});

	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Intent forActivity = new Intent(getBaseContext(), AlarmActivity.class);
		forActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		forActivity.putExtra("Wake Up", "Wake");
		startActivity(forActivity);
		player.start();
		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		final long[] pattern = { 0, 150, 1000, 750, 2000 };
		v.vibrate(pattern, 0);

		return 1;
	}

	public void onStart(Intent intent, int startId) {

	}

	public IBinder onUnBind(Intent arg0) {

		return null;
	}

	public void onStop() {

	}

	public void onPause() {

	}

	@Override
	public void onDestroy() {
		Log.d("Service", "Destroyed");
		player.stop();
		player.release();
		v.cancel();
		Log.d("OnDestroyService", "ExecutorShutDown");
	}

	@Override
	public void onLowMemory() {

	}
}
