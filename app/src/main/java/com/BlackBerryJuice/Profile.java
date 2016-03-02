package com.BlackBerryJuice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Profile extends Activity {
    private int count=0;
    public static String res_in_profile="";

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
        setContentView(R.layout.profile_layout);

        Intent intent = getIntent();
        boolean from_login = intent.getBooleanExtra("fromlogin", false);

        TextView profname = (TextView) findViewById(R.id.prof_name);
        TextView profnum = (TextView) findViewById(R.id.prof_num);

        final String s = EditProfile.load_code(Profile.this);
        if(from_login) {
            new updateuserserver(Constant.Update_ProfileURL, "", "", "", "", "", "", s, "get").execute();
            getdatafromserver();
        }

        profname.setText(EditProfile.load_name(Profile.this));
        profnum.setText(EditProfile.load_code(Profile.this));

        RelativeLayout Edit_Button = (RelativeLayout) findViewById(R.id.Edit_Button);
        Edit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, EditProfile.class));
                finish();
            }
        });

        RelativeLayout Exit = (RelativeLayout) findViewById(R.id.Exit_Button);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.set_user_logedin(false, Profile.this);
                Register.set_user_registered(false, Profile.this);

                Log.e("saeed_before_delete", EditProfile.load_address(Profile.this) + " " + EditProfile.load_code(Profile.this));

                EditProfile.delete_all_userinfo(Profile.this);
                ActivitySplash.delete_user_special_message(Profile.this);

                Log.e("saeed_after_delete", EditProfile.load_address(Profile.this) + " " + EditProfile.load_code(Profile.this));

                startActivity(new Intent(Profile.this, ActivityMainMenu.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Profile.this, ActivityMainMenu.class));
        finish();
    }

    private void po(String temp){
        String name="";
        String address="";
        String birthday="";
        String instagram="";
        String mobile="";
        String phone="";
        String code="";
        int f=0;
        int c=0;
        for(int i=0;i<temp.length();i++){

            if(temp.charAt(i)=='|'){

                String t=temp.substring(f, i);

                if(c==0){

                    name=t;
                }
                if(c==1){

                    address=t;
                }
                if(c==2){

                    birthday=t;
                }
                if(c==3){

                    instagram=t;
                }
                if(c==4){

                    mobile=t;
                }
                if(c==5){

                    phone=t;
                }
                if(c==6){

                    code=t;
                }
                c+=1;
                f=i+1;
            }
        }
        EditProfile.save_last_userinfo(name, birthday, address, phone, instagram, Profile.this);
        EditProfile.save_last_userinfo_cm(code, mobile, Profile.this);
    }


    private void getdatafromserver(){
        final String s = EditProfile.load_code(Profile.this);
        final ProgressDialog pd=new ProgressDialog(Profile.this);
        final Timer tm=new Timer();
        pd.setMessage("لطفا صبر کنید" + "\n" + "در حال دریافت اطلاعات از سرور");
        pd.show();
        pd.setCancelable(false);
        tm.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                        count++;
                        if (count == 30) {

                            pd.cancel();
                            tm.cancel();
                            count = 0;
                            new updateuserserver(Constant.Update_ProfileURL, "", "", "", "", "", "", s, "get").cancel(true);
                            Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط", Toast.LENGTH_LONG).show();
                            finish();

                        }

                        if (!res_in_profile.equals("")) {

                            pd.cancel();
                            po(res_in_profile);
                            Log.e("saeed", res_in_profile);
                            res_in_profile = "";
                            tm.cancel();

                        }

                    }
                });

            }

        }, 1, 1000);


    }
}
