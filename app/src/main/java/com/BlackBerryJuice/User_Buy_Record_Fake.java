package com.BlackBerryJuice;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Saeed on 3/3/2016.
 */
public class User_Buy_Record_Fake extends Activity {
    public static String trans="0";
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
}
