package com.BlackBerryJuice;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

public class ActivityReservation extends Activity implements
		TimePickerDialog.OnTimeSetListener,
		DatePickerDialog.OnDateSetListener,
		View.OnClickListener {

	private static final String TIMEPICKER = "TimePickerDialog",
			DATEPICKER = "DatePickerDialog";

	private CheckBox mode24Hours, modeDarkTime, modeDarkDate;
	private TextView timeTextView, dateTextView;
	private Button Req_Btn; //timeButton, dateButton;
	RadioButton table3 , table5;
	EditText desc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reservation);
		initializeViews();
		handleClicks();
	}

	private void initializeViews() {
		timeTextView = (TextView)findViewById(R.id.Time_Text);
		dateTextView = (TextView)findViewById(R.id.Date_Text);
		Req_Btn = (Button)findViewById(R.id.RequestBtn);
		Req_Btn.setTypeface(ActivitySplash.F2);
		table3 = (RadioButton)findViewById(R.id.table3);
		table5 = (RadioButton)findViewById(R.id.table5);

		table3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (table3.isChecked())
				{
					table5.setChecked(false);
//					SaveQ1(1);
				}
			}
		});
		table5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (table5.isChecked())
				{
					table3.setChecked(false);
//					SaveQ1(1);
				}
			}
		});

		desc = (EditText)findViewById(R.id.desc);
		desc.setTypeface(ActivitySplash.F2);



//		timeButton = (Button)findViewById(R.id.time_button);
//		dateButton = (Button)findViewById(R.id.date_button);
		//mode24Hours = (CheckBox)findViewById(R.id.mode_24_hours);
//		modeDarkTime = (CheckBox)findViewById(R.id.mode_dark_time);
//		modeDarkDate = (CheckBox)findViewById(R.id.mode_dark_date);
	}

	private void handleClicks() {
		timeTextView.setOnClickListener(this);
		dateTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.Time_Text : {
				PersianCalendar now = new PersianCalendar();
				TimePickerDialog tpd = TimePickerDialog.newInstance(
						ActivityReservation.this,
						now.get(PersianCalendar.HOUR_OF_DAY),
						now.get(PersianCalendar.MINUTE),
						true
				);
//				tpd.setThemeDark(true);

				tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {
						Log.d(TIMEPICKER, "Dialog was cancelled");
					}
				});
				tpd.show(getFragmentManager(), TIMEPICKER);
				break;
			}
			case R.id.Date_Text : {
				PersianCalendar now = new PersianCalendar();
				DatePickerDialog dpd = DatePickerDialog.newInstance(
						ActivityReservation.this,
						now.getPersianYear(),
						now.getPersianMonth(),
						now.getPersianDay()
				);
//				dpd.setThemeDark(true);
				dpd.show(getFragmentManager(), DATEPICKER);
				break;
			}
			default: break;
		}
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
		String minuteString = minute < 10 ? "0"+minute : ""+minute;
		String time = hourString+":"+minuteString;
		timeTextView.setText(time);
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		// Note: monthOfYear is 0-indexed
		String date = year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
		dateTextView.setText(date);
	}

}
