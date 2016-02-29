package com.BlackBerryJuice;



import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.BlackBerryJuice.util.ShamsiCalleder;
import com.BlackBerryJuice.utils.TextViewPlus;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.BlackBerryJuice.util.ErrorToast;
import com.BlackBerryJuice.util.ShamsiCalleder;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

public class Register extends Activity implements
        DatePickerDialog.OnDateSetListener {

    private EditText name1,mobile1,address1,phone1,instagram1;
    private TextViewPlus bithday1;
    private Button register;
    private TextView exit;
    private int count=0;
    public static String res="";
    public static boolean birthday_picked = false;


    public static String DATE_GOES_TO_SERVER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// keyboard hidden
        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.tameshk_dark));
        }
        setContentView(R.layout.activity_register);
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/IRANSansMobile_Light_Persian_Digits.ttf");

        register=(Button)findViewById(R.id.registerBtn);
        exit=(TextView)findViewById(R.id.reg_cancel_link);


        name1=(EditText) findViewById(R.id.name);
        name1.requestFocus();
        mobile1 =(EditText) findViewById(R.id.mobile);
        mobile1.setNextFocusDownId(R.id.birthday);
        bithday1=(TextViewPlus) findViewById(R.id.bithday);
        address1=(EditText) findViewById(R.id.address);
        phone1=(EditText) findViewById(R.id.phone);
        instagram1=(EditText) findViewById(R.id.instagram);

        name1.setTypeface(font);
        mobile1.setTypeface(font);
        address1.setTypeface(font);
        phone1.setTypeface(font);
        instagram1.setTypeface(font);

        register.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {

                register1(name1.getText().toString(), mobile1.getText().toString(), DATE_GOES_TO_SERVER,
                        address1.getText().toString(), phone1.getText().toString(), instagram1.getText().toString());

            }

        });
        bithday1.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                get_madafaka_date();
            }
        });

        exit.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                finish();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void register1(String name,String mobile,String bithday,String address,String phone,String instagram){

        if(((name1.getText().toString().trim().length()==0)) || ((mobile1.getText().toString().trim().length()==0)) ||
                ((bithday1.getText().toString().trim().length()==0)) || ((address1.getText().toString().trim().length()==0))
                || ((phone1.getText().toString().trim().length()==0)) || mobile1.getText().toString().startsWith("09") == false
                ||mobile1.getText().toString().trim().length()!=11){

            int ecolor = Color.RED;
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);

            if(name1.getText().toString().trim().length()==0){
                name1.setFocusableInTouchMode(true);
                name1.requestFocus();
                String estring = "این قسمت را باید تکمیل کنید";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                name1.setError(ssbuilder);}

            if(mobile1.getText().toString().trim().length()==0)
            {
                mobile1.setFocusableInTouchMode(true);
                mobile1.requestFocus();
                String estring = "این قسمت را باید تکمیل کنید";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                mobile1.setError(ssbuilder);
            }
            else if(mobile1.getText().toString().startsWith("09") == false)
            {
                mobile1.setFocusableInTouchMode(true);
                mobile1.requestFocus();
                String estring = "شماره موبایل باید با 09 آغاز شود";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                mobile1.setError(ssbuilder);
            }
            else if( mobile1.getText().toString().trim().length()!=11)
            {
                mobile1.setFocusableInTouchMode(true);
                mobile1.requestFocus();
                String estring = "شماره موبایل باید 11 رقم باشد";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                mobile1.setError(ssbuilder);
            }

            if(birthday_picked==false){
                bithday1.setFocusableInTouchMode(true);
                bithday1.requestFocus();
                String estring = "این قسمت را باید تکمیل کنید";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                bithday1.setError(ssbuilder);}

            if(address1.getText().toString().trim().length()==0){
                address1.setFocusableInTouchMode(true);
                address1.requestFocus();
                String estring = "این قسمت را باید تکمیل کنید";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                address1.setError(ssbuilder);}

            if(phone1.getText().toString().trim().length()==0){
                phone1.setFocusableInTouchMode(true);
                phone1.requestFocus();
                String estring = "این قسمت را باید تکمیل کنید";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                phone1.setError(ssbuilder);}

        }
        else{

            new registerserver(Constant.Register,name, mobile, bithday, address, phone, instagram).execute();

            final ProgressDialog pd=new ProgressDialog(Register.this);
            pd.setMessage("در حال ارسال اطلاعات به سرور");
            pd.show();

            final Timer tm = new Timer();
            tm.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            count++;
                            if (count == 30) {
                                pd.cancel();
                                tm.cancel();
                                count = 0;
                                Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط", Toast.LENGTH_LONG).show();
                            }
                            if (res.equals("ut")) {
                                pd.cancel();
                                Toast.makeText(getApplicationContext(), "شماره موبایل شما قبلا ثبت شده است، لطفا وارد شوید", Toast.LENGTH_LONG).show();
                                res = "";
                                tm.cancel();
                            } else if (res.toLowerCase().contains("ok")) {
                                pd.cancel();
                                Toast.makeText(getApplicationContext(), "ثبت نام با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                                String Code = res.replace("ok", "");
                                int newcode = Integer.valueOf(Code);
                                newcode++;
                                save_code_and_mobile_on_register(String.valueOf(newcode),mobile1.getText().toString(),Register.this);
                                res = "";
                                tm.cancel();
                                startActivity(new Intent(Register.this, Profile.class));
                                finish();
                            } else if (res.equals("no")) {

                                pd.cancel();
                                Toast.makeText(getApplicationContext(), "خطا در عملیات ثبت نام", Toast.LENGTH_LONG).show();
                                res = "";
                                tm.cancel();
                            }
                        }
                    });

                }

            }, 1, 1000);

        }
    }

    DatePickerDialog dpd;
    PersianCalendar  now;
    private static final String DATEPICKER = "DatePickerDialog";
    public void get_madafaka_date()
    {
        now = new PersianCalendar();
        dpd = DatePickerDialog.newInstance(
                Register.this,
                now.getPersianYear()-13,
                now.getPersianMonth(),
                now.getPersianDay()
        );
        dpd.setYearRange(1300, Integer.parseInt(ShamsiCalleder.getYear())-12);
        dpd.show(getFragmentManager(), DATEPICKER);
    }
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+ " " + ActivityReservation.MonthName(monthOfYear + 1) + " " + year ;
        DATE_GOES_TO_SERVER= year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
        birthday_picked=true;
        bithday1.setText(date);
    }


    public static void save_code_and_mobile_on_register(String code,String mobile,Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_registered", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("code", code);
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public static void clear_code_and_mobile_on_register(Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_registered", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static String load_code_from_register(Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_registered", Activity.MODE_PRIVATE);
        return sp.getString("code", "");
    }

    public static String load_mobile_from_register(Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_registered", Activity.MODE_PRIVATE);
        return sp.getString("mobile", "");
    }

}