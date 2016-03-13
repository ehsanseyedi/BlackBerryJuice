package com.BlackBerryJuice;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.BlackBerryJuice.util.ErrorToast;

public class Login extends Activity {

    public EditText passtext;
    private Button login;
    private TextView register;

    public static String res="";
    private int count=0;
    private SharedPreferences sp;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// keyboard hidden
        setContentView(R.layout.activity_login);
        passtext =(EditText) findViewById(R.id.phone);

        passtext.setTypeface(ActivitySplash.F6);
        //Bundle b = getIntent().getExtras();

        login =(Button) findViewById(R.id.loginBtn);
        login.setTypeface(ActivitySplash.F2);
        register =(TextView) findViewById(R.id.reg_now_link);

        login.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                login1(passtext.getText().toString());
            }

        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                finish();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void login1(final String mobile){

        if(((passtext.getText().toString().trim().length()==0))){

            int ecolor = Color.RED;
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
            /////

            if(passtext.getText().toString().trim().length()==0){
                passtext.setFocusableInTouchMode(true);
                passtext.requestFocus();

                String estring = "این قسمت را باید تکمیل کنید";
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                passtext.setError(ssbuilder);}
        }
        else{

            new loginserver(Constant.Login,mobile).execute();/////

            final ProgressDialog pd=new ProgressDialog(Login.this);
            pd.setMessage("لطفا کمی صبر کنید...");
            pd.show();

            final Timer tm=new Timer();
            tm.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            count++;
                            if (count == 25) {

                                pd.cancel();
                                tm.cancel();
                                new loginserver(Constant.Login, "").cancel(true); /////
                                ErrorToast.makeToast(Login.this, "خطا در برقراری ارتباط با سرور", Toast.LENGTH_LONG).show();
                            }

                            if (res.toLowerCase().contains("ok")) {

                                pd.cancel();

                                sp = getApplicationContext().getSharedPreferences("userP", 0);
                                Editor edit = sp.edit();
                                //edit.putString("email", code);
                                edit.commit();
                                String Code = res.replace("ok", "");
                                int newcode = Integer.valueOf(Code);
                                //final String s = sp.getString("email", "");
                                SharedData.set_user_logedin(true, Login.this);
                                SharedData.save_last_userinfo_cm(String.valueOf(newcode), mobile, Login.this);
                                new updatemessage(Constant.Update_Message,String.valueOf(newcode),"","get",Login.this).execute();
                                new updateuserserver(Constant.Update_ProfileURL,"","","","","","",String.valueOf(newcode),"get",Login.this).execute();
                                Intent ed = new Intent(Login.this, Profile.class);
                                ed.putExtra("fromlogin", true);
                                res = "";
                                tm.cancel();
                                f();
                                startActivity(ed);
                                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                            } else if (res.equals("wrong password")) {

                                pd.cancel();
                                ErrorToast.makeToast(Login.this, " شماره موبایل صحیح نیست", Toast.LENGTH_LONG).show();
                                res = "";
                                tm.cancel();
                            } else if (res.equals("no user")) {

                                pd.cancel();
                                ErrorToast.makeToast(Login.this, "این کاربر وجود ندارد", Toast.LENGTH_LONG).show();
                                res = "";
                                tm.cancel();
                            }
                        }

                    });

                }

            }, 1, 1000);

        }

    }

//    public static void set_user_logedin(Boolean dolog,Context c) {
//        SharedPreferences sp = c.getSharedPreferences("user_logedin", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean("log", dolog);
//        editor.commit();
//    }
//
//    public static Boolean do_user_logedin(Context c) {
//        SharedPreferences sp = c.getSharedPreferences("user_logedin", Activity.MODE_PRIVATE);
//        return sp.getBoolean("log", false);
//    }

    private void f(){
        this.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }
}
