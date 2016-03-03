package com.BlackBerryJuice;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Review extends Activity {

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

        Bundle b = getIntent().getExtras();
        Double totalprice = b.getDouble("price");

        TextView address = (TextView) findViewById(R.id.nowaddress);
        address.setText(SharedData.load_address(Review.this));
        EditText newAd = (EditText) findViewById(R.id.newaddress);
        if(!newAd.getText().toString().equals("")){
           SharedData.save_address2(newAd.getText().toString(),Review.this);
        }

        final Intent gotobank = new Intent(Review.this,Paaay.class);
        gotobank.putExtra("price",totalprice);

        Button paay = (Button) findViewById(R.id.paay);
        paay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(gotobank);
                finish();
            }
        });


    }


}
