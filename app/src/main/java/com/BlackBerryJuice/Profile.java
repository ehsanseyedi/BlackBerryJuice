package com.BlackBerryJuice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BlackBerryJuice.util.ErrorToast;

import java.util.Timer;
import java.util.TimerTask;

public class Profile extends Activity {
    private int count=0;
    TextView profname;
    TextView profnum;
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

        profname = (TextView) findViewById(R.id.prof_name);
        profnum = (TextView) findViewById(R.id.prof_num);

        if(!SharedData.load_name(Profile.this).equals("") && !SharedData.load_code(Profile.this).equals("")){
            profname.setText(SharedData.load_name(Profile.this));
            profnum.setText(SharedData.load_code(Profile.this));
        }else{
            String code = SharedData.load_code(Profile.this);
            countforget(code);
        }

        RelativeLayout Edit_Button = (RelativeLayout) findViewById(R.id.Edit_Button);
        Edit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, EditProfile.class));
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                //finish();
            }
        });

        RelativeLayout Cart_Button = (RelativeLayout) findViewById(R.id.Cart_Button);
        Cart_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, ActivityCart.class));
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                //finish();
            }
        });
        RelativeLayout Exit = (RelativeLayout) findViewById(R.id.Exit_Button);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedData.set_user_logedin(false, Profile.this);
                SharedData.set_user_registered(false, Profile.this);
                SharedData.delete_all_userinfo(Profile.this);
                SharedData.delete_user_special_message(Profile.this);

                startActivity(new Intent(Profile.this, ActivityMainMenu.class));
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);

    }

        public void countforget (String code){
            final String codeee = code;
            new updateuserserver(Constant.Update_ProfileURL,"","","","","","",codeee,"get",Profile.this).execute();
        final Timer tm=new Timer();
        final ProgressDialog pd=new ProgressDialog(Profile.this);
        pd.setMessage("لطفا صبر کنید"+"\n"+"در حال دریافت اطلاعات از سرور");
        pd.show();
        pd.setCancelable(false);
        pd.setOnCancelListener(new ProgressDialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {

                tm.cancel();
                pd.cancel();
                new updateuserserver(Constant.Update_ProfileURL, "", "", "", "", "", "", codeee, "get", Profile.this).cancel(true);

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
                            new updateuserserver(Constant.Update_ProfileURL, "", "", "", "", "", "", codeee, "get", Profile.this).cancel(true);
                            ErrorToast.makeToast(Profile.this, "خطا در برقراری ارتباط", Toast.LENGTH_LONG).show();
                            finish();
                        }

                        if (!EditProfile.res.equals("")) {
                            pd.cancel();
                            Log.e("saeed", "taken from profile done: " + EditProfile.res);
                            EditProfile.res = "";
                            profname.setText(SharedData.load_name(Profile.this));
                            profnum.setText(SharedData.load_code(Profile.this));
                            tm.cancel();

                        }

                    }
                });

            }

        }, 1, 1000);
    }

}
