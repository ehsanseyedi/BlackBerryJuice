package com.BlackBerryJuice;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.BlackBerryJuice.util.ErrorToast;
import com.BlackBerryJuice.util.ShamsiCalleder;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.Calendar;

public class Review extends Activity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private static final String TIMEPICKER = "TimePickerDialog",
            DATEPICKER = "DatePickerDialog";
    PersianCalendar now2 , now;
    DatePickerDialog dpd;
    private TextView timeTextView, dateTextView;
    private Button Req_Btn; //timeButton, dateButton;
    public static boolean is_today_selected = false , is_date_picked = false , is_time_picked = false;
    EditText desc;
    RelativeLayout Time_Pick , Date_Pick;
    TimePickerDialog tpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.tameshk_dark));
        }

        setContentView(R.layout.activity_review);
        desc = (EditText)findViewById(R.id.desc);
        desc.setTypeface(ActivitySplash.F6);
        timeTextView = (TextView)findViewById(R.id.Time_Text);
        dateTextView = (TextView)findViewById(R.id.Date_Text);

        Time_Pick = (RelativeLayout)findViewById(R.id.Time_Pick);
        Date_Pick = (RelativeLayout)findViewById(R.id.Date_Pick);

        Time_Pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_date_picked)
                    get_madafaka_time();
                else
                {
                    ErrorToast toast = new ErrorToast(Review.this);
                    toast .setText("لطفاً ابتدا تاریخ را انتخاب کنید");
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast .show();
                }
            }
        });
        Date_Pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_madafaka_date();
            }
        });

        Bundle b = getIntent().getExtras();
        Double totalprice = b.getDouble("price");

        TextView address = (TextView) findViewById(R.id.nowaddress);
        address.setText(SharedData.load_address(Review.this));
        EditText newAd = (EditText) findViewById(R.id.newaddress);
        newAd.setTypeface(ActivitySplash.F6);

        if(!newAd.getText().toString().equals("")){
           SharedData.save_address2(newAd.getText().toString(),Review.this);
        }


        final Intent gotobank = new Intent(Review.this,Paaay.class);
        gotobank.putExtra("price", totalprice);
        Button paay = (Button) findViewById(R.id.paay);
        paay.setTypeface(ActivitySplash.F2);
        paay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_date_picked && is_time_picked)
                {
                    String timee = "ارسال در تاریخ "
                            +dateTextView.getText()
                            +" و ساعت "
                            +timeTextView.getText();
                    SharedData.save_user_order_time(timee,Review.this);
                    String desss = ""+desc.getText();
                    SharedData.save_user_product_desc(desss, Review.this);
                    startActivity(gotobank);
                    finish();
                }
                else
                {
                    startActivity(gotobank);
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Review.this,ActivityCart.class));
        finish();
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
                ErrorToast toast = new ErrorToast(Review.this);
                toast .setText("لطفاً برای امروز، برای ساعات بعد انتخاب بفرمایید.");
                toast.setDuration(Toast.LENGTH_LONG);
                toast .show();
                get_madafaka_time();
            }
        }
        else
        {
            //ErrorToast.makeToast(Review.this, "لطفاً در بازه زمانی فعالیت فروشگاه زمان انتخاب کنید (9 صبح تا 2 بامداد)", Toast.LENGTH_LONG).show();
            ErrorToast toast = new ErrorToast(Review.this);
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
            ErrorToast toast = new ErrorToast(Review.this);
            String dayy = now.getPersianDay()+" "+ MonthName(now2.getPersianMonth()) + " " + now2.getPersianYear();
            String temp = "حداکثر زمان ممکن برای ارسال یک ماه آینده می\u200Cباشد.("+dayy+")";
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
                Review.this,
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
                Review.this,
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

}
