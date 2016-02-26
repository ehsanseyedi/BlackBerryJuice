package com.BlackBerryJuice;



import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {

    private EditText name1,family1,pass1;
    private Button register;
    private TextView exit;
    private int count=0;
    public static String res="";


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


        register=(Button)findViewById(R.id.registerBtn);
        exit=(TextView)findViewById(R.id.reg_cancel_link);


        name1=(EditText) findViewById(R.id.re_name);
        family1=(EditText) findViewById(R.id.re_family);
        pass1=(EditText) findViewById(R.id.re_pass);
        email1=(EditText) findViewById(R.id.re_email);

        name1.setTypeface(flow);
        family1.setTypeface(flow);
        email1.setTypeface(flow);
        pass1.setTypeface(flow);


        register.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {

                register1(name1.getText().toString(),family1.getText().toString(),pass1.getText().toString(),
                        email1.getText().toString());

            }

        });


        exit.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                finish();
            }

        });

    }

    @SuppressWarnings("unchecked")
    private void register1(String name,String family,String pass,String email){



        if(((name1.getText().toString().trim().length()==0)) || ((family1.getText().toString().trim().length()==0)) ||
                ((pass1.getText().toString().trim().length()==0)) || ((email1.getText().toString().trim().length()==0))){

            int ecolor = Color.RED;
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);

            if(name1.getText().toString().trim().length()==0){
                name1.setFocusableInTouchMode(true);
                name1.requestFocus();
                String estring = "این قسمت را باید تکمیل کنید";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                name1.setError(ssbuilder);}

            if(family1.getText().toString().trim().length()==0){
                family1.setFocusableInTouchMode(true);
                family1.requestFocus();
                String estring = "این قسمت را باید تکمیل کنید";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                family1.setError(ssbuilder);}

            if(pass1.getText().toString().trim().length()==0){
                pass1.setFocusableInTouchMode(true);
                pass1.requestFocus();
                String estring = "این قسمت را باید تکمیل کنید";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                pass1.setError(ssbuilder);}

            if(email1.getText().toString().trim().length()==0){
                email1.setFocusableInTouchMode(true);
                email1.requestFocus();
                String estring = "این قسمت را باید تکمیل کنید";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                email1.setError(ssbuilder);}

        }
        else{


            new registerserver("http://unixantivirus.wc.lt/register.php",name,family,pass,email).execute();

            final ProgressDialog pd=new ProgressDialog(register.this);
            pd.setMessage("در حال ارسال اطلاعات به سرور");
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
                                Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط", Toast.LENGTH_LONG).show();}

                            if(res.equals("ut")){
                                pd.cancel();
                                Toast.makeText(getApplicationContext(), "نام کاربری انتخابی شما تکراری میباشد.لطفا یک نام کاربری دیگر انتخاب نمایید", Toast.LENGTH_LONG).show();
                                res="";
                                tm.cancel();}
                            else if(res.equals("ok")){
                                pd.cancel();
                                Toast.makeText(getApplicationContext(), "ثبت نام با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                                res="";
                                tm.cancel();
                                finish();}
                            else if(res.equals("no")){

                                pd.cancel();
                                Toast.makeText(getApplicationContext(), "خطا در عملیات ثبت نام", Toast.LENGTH_LONG).show();
                                res="";
                                tm.cancel();}

                        }
                    });

                }

            }, 1, 1000);

        }
    }


}