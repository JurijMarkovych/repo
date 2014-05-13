package com.example.myalarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.PendingIntent.CanceledException;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmActivity extends Activity {

	private MyAlarm alarm;
	private Intent intent;
	private TextView time;
	private Long millis;
	private int hour;
	private int minute;
	private Switch state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initStuff();

		if (getIntent().getStringExtra("Wake Up") != null
				&& getIntent().getStringExtra("Wake Up").equals("Wake")) {
			Log.d("Bundle", "not null");
			state.setChecked(true);
		}
		SharedPreferences settings = getSharedPreferences("SaveActivityState",
				0);
		Boolean isEnabled = settings.getBoolean("IsEnabled", false);

		if (isEnabled == true) {
			state.setChecked(true);
		}
		time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickedAdapter adapter = new TimePickedAdapter();
				Log.d("OnclicTime", "Clicked");
				TimePickerDialog dialog = new TimePickerDialog(
						AlarmActivity.this, adapter, hour, minute, true);
				dialog.setIcon(R.drawable.ic_launcher);
				dialog.show();

			}

		});

		state.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (state.isChecked()) {
					onetimeTimer();
				}
				if (!state.isChecked()) {
					try {
						cancelRepeatingTimer();
						getSharedPreferences("SaveActivityState", 0).edit()
								.remove("IsEnabled").commit();
					} catch (CanceledException e) {

						e.printStackTrace();
					}
				}

			}
		});
	}

	private class TimePickedAdapter implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
			millis = (long) ((Math
					.abs(TimeManager.hoursManage(hour, hourOfDay)) * 3600000) + (Math
					.abs(TimeManager.minutesManage(minute, minuteOfDay)) * 60000));
			time.setText("" + hourOfDay + ":"
					+ TimeManager.timeCorrector(minuteOfDay));
			Log.d("dd", "" + millis);

		}
	}

	public void startRepeatingTimer(View view) throws CanceledException {

		double d = Math.random();
		intent.putExtra("oh me~", d);
		Log.d("D", "real: " + d);
		sendBroadcast(intent);

	}

	public void cancelRepeatingTimer() throws CanceledException {

		if (alarm != null) {

			alarm.cancelAlarm(this, intent);
			Toast.makeText(this, "Alarm was canceled", Toast.LENGTH_SHORT)
					.show();

		} else {

			Toast.makeText(this, "Alarm is null", Toast.LENGTH_SHORT).show();

		}

	}

	public void onetimeTimer() {

		Log.d("OneTimeTimer", "start");
		if (alarm != null) {
			Log.d("OneTimeTimer", "startNotNull");
			alarm.setOnetimeTimer(this, intent, millis);
			Toast.makeText(this, "Alarm is staritng(without repeating)",
					Toast.LENGTH_SHORT).show();

		} else {

			Toast.makeText(this, "Alarm is null", Toast.LENGTH_SHORT).show();

		}

	}

	private void initStuff() {
		alarm = new MyAlarm();
		intent = new Intent(this, MyAlarm.class);
		time = (TextView) findViewById(R.id.time);
		state = (Switch) findViewById(R.id.enableAlarm);

		Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		time.setText("" + hour + ":" + TimeManager.timeCorrector(minute));
	}
}
