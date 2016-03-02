package com.BlackBerryJuice;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Profile extends Activity {

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
        startActivity(new Intent(Profile.this,ActivityMainMenu.class));
        finish();
    }
}
