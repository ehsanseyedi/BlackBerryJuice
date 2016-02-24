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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.BlackBerryJuice.util.ErrorToast;
import com.BlackBerryJuice.util.ShamsiCalleder;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.Calendar;
import java.util.TimeZone;

public class ActivityReservation extends Activity implements
		TimePickerDialog.OnTimeSetListener,
		DatePickerDialog.OnDateSetListener,
		View.OnClickListener {

	private static final String TIMEPICKER = "TimePickerDialog",
			DATEPICKER = "DatePickerDialog";

	private CheckBox mode24Hours, modeDarkTime, modeDarkDate;
	private TextView timeTextView, dateTextView;
	private Button Req_Btn; //timeButton, dateButton;
	EditText desc;
	FrameLayout table_2 , table_5;
	ImageView table_2_check,table_5_check;
	LinearLayout Time_Pick , Date_Pick;
	TimePickerDialog tpd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reservation);
		initializeViews();
		handleClicks();
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DATE);
		int mounth = c.get(Calendar.MONTH)+1;
		int year = c.get(Calendar.YEAR);
		ShamsiCalleder.getCurrentShamsidate();
		Log.d("EHSAN", day + "  " + mounth + "  " + year);
		Log.d("EHSAN", ShamsiCalleder.getDay() + "  " + ShamsiCalleder.getMonth() + "  " + ShamsiCalleder.getYear());
		Log.d("EHSAN", ShamsiCalleder.getCurrentShamsidate());



	}

	private void initializeViews() {
		timeTextView = (TextView)findViewById(R.id.Time_Text);
		dateTextView = (TextView)findViewById(R.id.Date_Text);
		Req_Btn = (Button)findViewById(R.id.RequestBtn);
		Req_Btn.setTypeface(ActivitySplash.F2);
		table_2 = (FrameLayout)findViewById(R.id.table_2);
		table_5 = (FrameLayout)findViewById(R.id.table_5);
		table_2_check = (ImageView)findViewById(R.id.table_2_check);
		table_5_check = (ImageView)findViewById(R.id.table_5_check);
		table_2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				table_2_check.setVisibility(View.VISIBLE);
				table_5_check.setVisibility(View.INVISIBLE);
			}
		});
		table_5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				table_2_check.setVisibility(View.INVISIBLE);
				table_5_check.setVisibility(View.VISIBLE);
			}
		});

		Time_Pick = (LinearLayout)findViewById(R.id.Time_Pick);
		Date_Pick = (LinearLayout)findViewById(R.id.Date_Pick);

		desc = (EditText)findViewById(R.id.desc);
		desc.setTypeface(ActivitySplash.F6);



//		timeButton = (Button)findViewById(R.id.time_button);
//		dateButton = (Button)findViewById(R.id.date_button);
		//mode24Hours = (CheckBox)findViewById(R.id.mode_24_hours);
//		modeDarkTime = (CheckBox)findViewById(R.id.mode_dark_time);
//		modeDarkDate = (CheckBox)findViewById(R.id.mode_dark_date);
	}

	private void handleClicks() {
		Time_Pick.setOnClickListener(this);
		Date_Pick.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.Time_Pick : {
				get_madafaka_time();
//				PersianCalendar now = new PersianCalendar();
//				tpd = TimePickerDialog.newInstance(
//						ActivityReservation.this,
//						now.get(PersianCalendar.HOUR_OF_DAY),
//						now.get(PersianCalendar.MINUTE),
//						true
//				);
//				tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//					@Override
//					public void onCancel(DialogInterface dialogInterface) {
//						Log.d(TIMEPICKER, "Dialog was cancelled");
//					}
//				});
//				tpd.setTitle("زمان مورد نظر را انتخاب کنید");
//				tpd.show(getFragmentManager(), TIMEPICKER);
				break;
			}
			case R.id.Date_Pick : {
				PersianCalendar now = new PersianCalendar();
				DatePickerDialog dpd = DatePickerDialog.newInstance(
						ActivityReservation.this,
						now.getPersianYear(),
						now.getPersianMonth(),
						now.getPersianDay()
				);
				dpd.setYearRange(Integer.parseInt(ShamsiCalleder.getYear()), Integer.parseInt(ShamsiCalleder.getYear()) + 1);
				dpd.setMinDate(now);
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
		if(hourOfDay <2 || hourOfDay >= 9 )
			timeTextView.setText(time);
		else
		{
			//ErrorToast.makeToast(ActivityReservation.this, "لطفاً در بازه زمانی فعالیت فروشگاه زمان انتخاب کنید (9 صبح تا 2 بامداد)", Toast.LENGTH_LONG).show();
			ErrorToast toast = new ErrorToast(ActivityReservation.this);
			toast .setText("لطفاً در بازه زمانی فعالیت فروشگاه زمان انتخاب کنید (9 صبح تا 2 بامداد)");
			//toast .setGravity(Toast.LENGTH_SHORT);
			toast .show();
			//Toast.makeText(ActivityReservation.this, "لطفاً در بازه زمانی فعالیت فروشگاه زمان انتخاب کنید (9 صبح تا 2 بامداد)", Toast.LENGTH_LONG).show();

			get_madafaka_time();
		}
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		// Note: monthOfYear is 0-indexed
		String date = year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
		dateTextView.setText(date);
	}
	public void get_madafaka_time()
	{
		PersianCalendar now = new PersianCalendar();
		tpd = TimePickerDialog.newInstance(
				ActivityReservation.this,
				now.get(PersianCalendar.HOUR_OF_DAY),
				now.get(PersianCalendar.MINUTE),
				true
		);
		tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				Log.d(TIMEPICKER, "Dialog was cancelled");
			}
		});
		tpd.setTitle("زمان مورد نظر را انتخاب کنید");
		tpd.show(getFragmentManager(), TIMEPICKER);
	}

}
