package com.BlackBerryJuice;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Saeed on 3/3/2016.
 */
public class User_Buy_Record_Fake extends Activity {

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
        setContentView(R.layout.user_buy_record_fake);

        Button back_1 = (Button)findViewById(R.id.back_1);
        back_1.setTypeface(ActivitySplash.F2);
        back_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Buy_Record_Fake.this, ActivityMainMenu.class));
                finish();
            }
        });

        TextView status = (TextView) findViewById(R.id.status);
        TextView rahgirt = (TextView) findViewById(R.id.rahgir);

        Intent intent = getIntent();
        String rahgir = intent.getStringExtra("rahgir");
        String paid = intent.getStringExtra("price");

        if(rahgir.equals("null")){
            status.setText("پرداخت ناموفق");
            rahgirt.setText("");
        }else{
            status.setText( "پرداخت موفقیت آمیز");
            rahgirt.setText(rahgir);
        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(User_Buy_Record_Fake.this, ActivityMainMenu.class));
        finish();
    }
}
