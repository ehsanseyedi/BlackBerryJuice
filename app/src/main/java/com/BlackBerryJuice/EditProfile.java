package com.BlackBerryJuice;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.BlackBerryJuice.util.ShamsiCalleder;
import com.BlackBerryJuice.utils.TextViewPlus;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.w3c.dom.Text;

public class EditProfile extends Activity implements
        DatePickerDialog.OnDateSetListener {


    EditText name,mobile,address,phone,instagram;
    TextViewPlus birthday;
    String code;
    @SuppressWarnings("unused")
    //private TextView tname,tfamily,toldpass,tnewpass,temail,tstatus;
    private TextView email;
    @SuppressWarnings("unused")
    private TextView update,exit;
    public static String res="";
    public static String DATE_GOES_TO_SERVER;
    private String pass="";
    private int count=0;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.tameshk_dark));
        }
        setContentView(R.layout.activity_edit_user_profile);

        update = (TextView) findViewById(R.id.update);
        exit = (TextView) findViewById(R.id.cancel);

        tarif();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile_Light_Persian_Digits.ttf");
        name.setTypeface(font);
        mobile.setTypeface(font);
        address.setTypeface(font);
        phone.setTypeface(font);
        instagram.setTypeface(font);

        code = SharedData.load_code(EditProfile.this);
        Log.e("code in shared",code);

        new updateuserserver(Constant.Update_ProfileURL,"","","","","","",code,"get",EditProfile.this).execute();

        final Timer tm=new Timer();
        final ProgressDialog pd=new ProgressDialog(EditProfile.this);
        pd.setMessage("لطفا صبر کنید"+"\n"+"در حال دریافت اطلاعات از سرور");
        pd.show();
        pd.setCancelable(false);
        pd.setOnCancelListener(new ProgressDialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {

                tm.cancel();
                pd.cancel();
                new updateuserserver(Constant.Update_ProfileURL, "", "", "", "", "", "", code, "get", EditProfile.this).cancel(true);

            }
        });


        tm.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                        count++;
                        if (count == 30) {

                            pd.cancel();
                            tm.cancel();
                            count = 0;
                            new updateuserserver(Constant.Update_ProfileURL, "", "", "", "", "", "", code, "get", EditProfile.this).cancel(true);
                            Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط", Toast.LENGTH_LONG).show();
                            finish();

                        }

                        if (!res.equals("")) {

                            pd.cancel();
                            po(res);
                            Log.e("saeed", res);
                            res = "";
                            tm.cancel();

                        }

                    }
                });

            }

        }, 1, 1000);

        birthday.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                get_madafaka_date();
            }
        });
        update.setTypeface(ActivitySplash.F2);
        update.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View arg0) {

                new updateuserserver(Constant.Update_ProfileURL,name.getText().toString(),address.getText().toString(),DATE_GOES_TO_SERVER,instagram.getText().toString(),mobile.getText().toString(),phone.getText().toString(),code,"put",EditProfile.this).
                        execute();

                final ProgressDialog pd=new ProgressDialog(EditProfile.this);
                pd.setMessage("لطفا صبر کنید"+"در حال ارسال اطلاعات به سرور");
                pd.show();

                final Timer tm=new Timer();
                tm.scheduleAtFixedRate(new TimerTask(){
                    public void run() {
                        runOnUiThread(new Runnable(){
                            public void run() {

                                count++;
                                if(count==30){
                                    pd.cancel();
                                    tm.cancel();
                                    count=0;
                                    Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط", Toast.LENGTH_LONG).show();
                                }

                                if(!res.equals("")){
                                    pd.cancel();
                                    Toast.makeText(getApplicationContext(), "اطلاعات با موفقیت آپدیت شد", Toast.LENGTH_LONG).show();
                                    tm.cancel();
                                }
                            }
                        });

                    }

                }, 1, 1000);
            }
        });

    }

    private void tarif(){

        name    =(EditText) findViewById(R.id.name);
        mobile    =(EditText) findViewById(R.id.mobile);
        address    =(EditText) findViewById(R.id.address);
        phone    =(EditText) findViewById(R.id.phone);
        instagram    =(EditText) findViewById(R.id.instagram);
        birthday    =(TextViewPlus)findViewById(R.id.bithday);



//        tname=(TextView) findViewById(R.id.edit_name_t);
//        tfamily=(TextView) findViewById(R.id.edit_family_t);
//        toldpass=(TextView) findViewById(R.id.edit_oldpass_t);
//        tnewpass=(TextView) findViewById(R.id.edit_newpass_t);
//        temail=(TextView) findViewById(R.id.edit_email_t);
//        tstatus=(TextView) findViewById(R.id.edit_status_t);
//        status=(TextView) findViewById(R.id.edit_status_e);
//        update=(Button) findViewById(R.id.update);
//        exit=(Button) findViewById(R.id.cancel);

    }

    private void po(String temp){
        String code;
        int f=0;
        int c=0;
        for(int i=0;i<temp.length();i++){

            if(temp.charAt(i)=='|'){

                String t=temp.substring(f, i);

                if(c==0){

                    name.setText(t);
                }
                if(c==1){

                    address.setText(t);
                }
                if(c==2){

                    birthday.setText(t);
                }
                if(c==3){

                    instagram.setText(t);
                }
                if(c==4){

                    mobile.setText(t);
                }
                if(c==5){

                    phone.setText(t);
                }
                if(c==6){

                    code=t;
                }
                c+=1;
                f=i+1;
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditProfile.this, Profile.class));
        finish();
    }

    DatePickerDialog dpd;
    PersianCalendar now;
    private static final String DATEPICKER = "DatePickerDialog";
    public void get_madafaka_date()
    {
        now = new PersianCalendar();
        dpd = DatePickerDialog.newInstance(
                EditProfile.this,
                now.getPersianYear()-13,
                now.getPersianMonth(),
                now.getPersianDay()
        );
        dpd.setYearRange(1300, Integer.parseInt(ShamsiCalleder.getYear())-12);
        dpd.show(getFragmentManager(), DATEPICKER);
    }
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+ " " + ActivityReservation.MonthName(monthOfYear + 1) + " " + year ;
        DATE_GOES_TO_SERVER= dayOfMonth+ " " + ActivityReservation.MonthName(monthOfYear + 1) + " " + year ;
        birthday.setText(date);
    }



}

