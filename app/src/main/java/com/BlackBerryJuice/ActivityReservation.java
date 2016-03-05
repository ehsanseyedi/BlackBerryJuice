package com.BlackBerryJuice;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BlackBerryJuice.util.ErrorToast;
import com.BlackBerryJuice.util.ShamsiCalleder;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;

public class ActivityReservation extends Activity implements
		TimePickerDialog.OnTimeSetListener,
		DatePickerDialog.OnDateSetListener,
		View.OnClickListener {

	private static final String TIMEPICKER = "TimePickerDialog",
			DATEPICKER = "DatePickerDialog";
	static DBHelper dbhelper;
	private CheckBox mode24Hours, modeDarkTime, modeDarkDate;
	private TextView timeTextView, dateTextView;
	private Button Req_Btn; //timeButton, dateButton;
	int end_of_month;
	PersianCalendar now2 , now;
	PersianCalendar[] now_Araay;
	DatePickerDialog dpd;
	public static boolean is_today_selected = false , is_date_picked = false , is_time_picked = false;
	EditText desc;
	FrameLayout table_2 , table_5;
	ImageView table_2_check,table_5_check , pre_order_check;
	boolean pre_order = true;
	RelativeLayout Time_Pick , Date_Pick;
	TimePickerDialog tpd;
	LinearLayout birthday_oc , anniversary_oc , together_oc , one_hour ,one_hour_plus;
	public String selected_oc="together_oc" , selected_time="one_hour" , table_sel = "2";
	int index_of;
	static ArrayList<Long> Menu_ID = new ArrayList<Long>();
	static ArrayList<String> Menu_name = new ArrayList<String>();
	static ArrayList<Double> Menu_price = new ArrayList<Double>();

	String MenuDetailAPI;
	int IOConnect = 0;
	DecimalFormat formatData = new DecimalFormat("#.##");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reservation);
		initializeViews();
		handleClicks();
		Calendar c = Calendar.getInstance();
		dbhelper = new DBHelper(this);
		try{
			dbhelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}
		int day = c.get(Calendar.DATE);
		int mounth = c.get(Calendar.MONTH)+1;
		int year = c.get(Calendar.YEAR);
		ShamsiCalleder.getCurrentShamsidate();
		Log.d("EHSAN", day + "  " + mounth + "  " + year);
		Log.d("EHSAN", ShamsiCalleder.getDay() + "  " + ShamsiCalleder.getMonth() + "  " + ShamsiCalleder.getYear());
		Log.d("EHSAN", ShamsiCalleder.getCurrentShamsidate());


		MenuDetailAPI = Constant.MenuAPI+"?accesskey="+Constant.AccessKey+"&category_id=0";

		// call asynctask class to request data from server
		new getDataTask().execute();


	}

	private void initializeViews() {
		timeTextView = (TextView)findViewById(R.id.Time_Text);
		dateTextView = (TextView)findViewById(R.id.Date_Text);
		pre_order_check = (ImageView)findViewById(R.id.pre_order_check);
		pre_order_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(pre_order == true)
				{
					pre_order = false;
					pre_order_check.setImageResource(R.drawable.check_1);
				}
				else
				{
					pre_order = true;
					pre_order_check.setImageResource(R.drawable.check_2);
				}

			}
		});
		Req_Btn = (Button)findViewById(R.id.RequestBtn);
		Req_Btn.setTypeface(ActivitySplash.F2);
		Req_Btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(is_date_picked && is_time_picked)

				{
					index_of = 0;


					if (table_sel.equals("2"))
						index_of = 0;
					else if (table_sel.equals("5"))
						index_of = 6;

					if (selected_oc.equals("together_oc"))
						index_of = index_of + 1;
					else if (selected_oc.equals("anniversary_oc"))
						index_of = index_of + 2;
					else if (selected_oc.equals("birthday_oc"))
						index_of = index_of + 3;

					if (selected_time.equals("one_hour_plus"))
						index_of = index_of + 3;

					Log.e("#####", index_of + "");
					Log.e("table_sel", table_sel + "");
					Log.e("selected_oc", selected_oc + "");
					Log.e("selected_time", selected_time + "");
					Log.e("name", Menu_name.get(index_of - 1) + "");

					clearReservation();

					if (dbhelper.isDataExist(Menu_ID.get(index_of - 1))) {
						dbhelper.updateData(Menu_ID.get(index_of - 1), 1, Menu_price.get(index_of - 1));
					} else {
						dbhelper.addData(Menu_ID.get(index_of - 1), Menu_name.get(index_of - 1), 1, (Menu_price.get(index_of - 1)));
					}
					Toast.makeText(ActivityReservation.this, "رزرو شما با موفقیت به سبد خرید افزوده شد", Toast.LENGTH_SHORT).show();
					String timee = "رزرو در تاریخ "
							+dateTextView.getText()
							+" و ساعت "
							+timeTextView.getText();
					String desss = ""+desc.getText();

					SharedData.save_user_reservarion_info(timee,desss, ActivityReservation.this);
					if (pre_order == true) {
						startActivity(new Intent(ActivityReservation.this, ActivityCategoryList.class));
						overridePendingTransition(R.anim.slide_up, R.anim.slide_up_2);
						finish();
					} else {
						startActivity(new Intent(ActivityReservation.this, ActivityCart.class));
						overridePendingTransition(R.anim.slide_up, R.anim.slide_up_2);
						finish();
					}
				}
				else
				{
					Toast.makeText(ActivityReservation.this, "لطفاً تاریخ و ساعت رزرو را مشخص کنید", Toast.LENGTH_SHORT).show();
				}
			}
		});


		table_2 = (FrameLayout)findViewById(R.id.table_2);
		table_5 = (FrameLayout)findViewById(R.id.table_5);
		table_2_check = (ImageView)findViewById(R.id.table_2_check);
		table_5_check = (ImageView)findViewById(R.id.table_5_check);
		table_2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				table_sel="2";
				table_2_check.setVisibility(View.VISIBLE);
				table_5_check.setVisibility(View.INVISIBLE);
			}
		});
		table_5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				table_sel="5";
				table_2_check.setVisibility(View.INVISIBLE);
				table_5_check.setVisibility(View.VISIBLE);
			}
		});

		Time_Pick = (RelativeLayout)findViewById(R.id.Time_Pick);
		Date_Pick = (RelativeLayout)findViewById(R.id.Date_Pick);

		desc = (EditText)findViewById(R.id.desc);
		desc.setTypeface(ActivitySplash.F6);

		birthday_oc = (LinearLayout)findViewById(R.id.birthday_oc);
		anniversary_oc  = (LinearLayout)findViewById(R.id.annivarsary_oc);
		together_oc = (LinearLayout)findViewById(R.id.together_oc);
		one_hour = (LinearLayout)findViewById(R.id.one_hour);
		one_hour_plus = (LinearLayout)findViewById(R.id.one_hour_plus);

		birthday_oc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selected_oc = "birthday_oc";
				birthday_oc.setBackgroundResource(R.drawable.backg_2_selected);
				anniversary_oc.setBackgroundResource(R.drawable.backg_2);
				together_oc.setBackgroundResource(R.drawable.backg_2);
			}
		});
		anniversary_oc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selected_oc = "anniversary_oc";
				birthday_oc.setBackgroundResource(R.drawable.backg_2);
				anniversary_oc.setBackgroundResource(R.drawable.backg_2_selected);
				together_oc.setBackgroundResource(R.drawable.backg_2);
			}
		});
		together_oc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selected_oc = "together_oc";
				birthday_oc.setBackgroundResource(R.drawable.backg_2);
				anniversary_oc.setBackgroundResource(R.drawable.backg_2);
				together_oc.setBackgroundResource(R.drawable.backg_2_selected);
			}
		});
		one_hour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selected_time = "one_hour";
				one_hour.setBackgroundResource(R.drawable.backg_2_selected);
				one_hour_plus.setBackgroundResource(R.drawable.backg_2);
			}
		});
		one_hour_plus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selected_time = "one_hour_plus";
				one_hour.setBackgroundResource(R.drawable.backg_2);
				one_hour_plus.setBackgroundResource(R.drawable.backg_2_selected);
			}
		});


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
				if(is_date_picked)
					get_madafaka_time();
				else
				{
					ErrorToast toast = new ErrorToast(ActivityReservation.this);
					toast .setText("لطفاً ابتدا تاریخ را انتخاب کنید");
					toast.setDuration(Toast.LENGTH_LONG);
					toast .show();
				}
				break;
			}
			case R.id.Date_Pick : {
				get_madafaka_date();
				break;
			}
			default: break;
		}
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
		String minuteString = minute < 10 ? "0" + minute : "" + minute;
		String time = hourString + ":" + minuteString;
		Log.e("HOUR_OF_DAY", now.get(PersianCalendar.HOUR_OF_DAY) + "  " + hourOfDay);
		if (hourOfDay < 2 || hourOfDay >= 9) {
			if(now.get(PersianCalendar.HOUR_OF_DAY)+4 < hourOfDay && is_today_selected)
			{
				timeTextView.setText(time);
				is_time_picked=true;
			}
			else if(!is_today_selected)
			{
				timeTextView.setText(time);
				is_time_picked=true;
			}
			else
			{
				ErrorToast toast = new ErrorToast(ActivityReservation.this);
				toast .setText("لطفاً برای امروز، برای ساعات بعد رزرو بفرمایید.");
				toast.setDuration(Toast.LENGTH_LONG);
				toast .show();
				get_madafaka_time();
			}
		}
		else
		{
			//ErrorToast.makeToast(ActivityReservation.this, "لطفاً در بازه زمانی فعالیت فروشگاه زمان انتخاب کنید (9 صبح تا 2 بامداد)", Toast.LENGTH_LONG).show();
			ErrorToast toast = new ErrorToast(ActivityReservation.this);
			toast .setText("لطفاً در بازه زمانی فعالیت فروشگاه زمان انتخاب کنید (9 صبح تا 2 بامداد)");
			toast.setDuration(Toast.LENGTH_LONG);
			toast .show();
			get_madafaka_time();
		}
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		Log.e("MONTH", monthOfYear + "  " + now2.getPersianMonth() + "  " + now.getPersianMonth());
		if(year==now.getPersianYear() && monthOfYear==now.getPersianMonth() && dayOfMonth > now.getPersianDay() )
		{
			String date = dayOfMonth+ " " + MonthName(monthOfYear + 1) + " " + year ;
			dateTextView.setText(date);
			is_date_picked=true;
		}
		else if(year==now.getPersianYear() && monthOfYear==now.getPersianMonth() && dayOfMonth == now.getPersianDay() )
		{
			String date = dayOfMonth+ " " + MonthName(monthOfYear + 1) + " " + year ;
			dateTextView.setText(date);
			is_today_selected=true;
			is_date_picked=true;
			Log.e("TODAY_SELECTED" , is_today_selected+"");
		}
		else if(year == now2.getPersianYear() && monthOfYear==now2.getPersianMonth()-1  && dayOfMonth <= now.getPersianDay())
		{
			String date = dayOfMonth+ " " + MonthName(monthOfYear + 1) + " " + year ;
			//String date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
			dateTextView.setText(date);
			is_date_picked=true;
		}
		else
		{
			ErrorToast toast = new ErrorToast(ActivityReservation.this);
			String dayy = now.getPersianDay()+" "+ MonthName(now2.getPersianMonth()) + " " + now2.getPersianYear();
			String temp = "حداکثر زمان ممکن برای رزرو یک ماه آینده می\u200Cباشد.\n("+dayy+")";
			toast .setText(temp);
			toast.setDuration(Toast.LENGTH_LONG);
			toast .show();
			get_madafaka_date();
		}
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

	public void get_madafaka_date()
	{
		now = new PersianCalendar();
		dpd = DatePickerDialog.newInstance(
				ActivityReservation.this,
				now.getPersianYear(),
				now.getPersianMonth(),
				now.getPersianDay()
		);
		now2 = new PersianCalendar();
		now2.addPersianDate(Calendar.MONTH, 1);
		dpd.setMinDate(now);
		dpd.setYearRange(Integer.parseInt(ShamsiCalleder.getYear()), Integer.parseInt(ShamsiCalleder.getYear()) + 1);
		dpd.show(getFragmentManager(), DATEPICKER);
	}

	public static String MonthName(int i)
	{
		switch (i) {
			case 1:
				return "فروردین";
			//break;
			case 2:
				return "اردیبهشت";
			//break;
			case 3:
				return "خرداد";
			//break;
			case 4:
				return "تیر";
			//break;
			case 5:
				return "مرداد";
			//break;
			case 6:
				return "شهریور";
			//break;
			case 7:
				return "مهر";
			//break;
			case 8:
				return "آبان";
			//break;
			case 9:
				return "آذر";
			//break;
			case 10:
				return "دی";
			//break;
			case 11:
				return "بهمن";
			//break;
			case 12:
				return "اسفند";
			//break;

			default: break;
		}
		return i+"";
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		dbhelper.close();
		finish();
		overridePendingTransition(R.anim.open_main, R.anim.close_next);
	}

	public class getDataTask extends AsyncTask<Void, Void, Void> {

		// show progressbar first
		getDataTask(){}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// parse json data from server in background
			parseJSONData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			Collections.reverse(Menu_ID);
			Collections.reverse(Menu_name);
			Collections.reverse(Menu_price);
		}
	}

	public void parseJSONData(){

		clearData();

		try {
			// request data from menu API
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
			HttpUriRequest request = new HttpGet(MenuDetailAPI);
			HttpResponse response = client.execute(request);
			InputStream atomInputStream = response.getEntity().getContent();

			BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));

			String line;
			String str = "";
			while ((line = in.readLine()) != null){
				str += line;
			}

			// parse json data and store into arraylist variables
			JSONObject json = new JSONObject(str);
			JSONArray data = json.getJSONArray("data"); // this is the "items: [ ] part

			for (int i = 0; i < data.length(); i++) {
				JSONObject object = data.getJSONObject(i);
				JSONObject menu = object.getJSONObject("Menu");

				//me
				//JSONObject menusaeed = object.getJSONObject("Menu_detail");

				Menu_ID.add(Long.parseLong(menu.getString("Menu_ID")));
				Menu_name.add(menu.getString("Menu_name"));
				Log.e("NAMEEEEEEEEEE" , Menu_name.get(i));
				//int price = (int) menu.getDouble("Price");
				//int toman = (int) price / 10;
				//String sp = NumberFormat.getNumberInstance(Locale.US).format(price);
				Menu_price.add(menu.getDouble("Price"));


			}


		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void clearData(){
		Menu_ID.clear();
		Menu_name.clear();
		Menu_price.clear();
	}

	void clearReservation()
	{
		for(int i=0 ; i<12 ; i++)
		{
			if(dbhelper.isDataExist(Menu_ID.get(i)))
			{
				dbhelper.deleteData(Menu_ID.get(i));
			}
		}
	}

}
