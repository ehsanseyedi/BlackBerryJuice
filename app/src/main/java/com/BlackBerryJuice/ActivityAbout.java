package com.BlackBerryJuice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by EhCan on 2/27/2016.
 */
public class ActivityAbout extends Activity {
    EditText desc;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_layout);
        desc = (EditText)findViewById(R.id.desc);
        desc.setTypeface(ActivitySplash.F6);
        ScrollView sc = (ScrollView)findViewById(R.id.scc);

//
//        RelativeLayout req_f = (RelativeLayout)findViewById(R.id.req_focus);
//        req_f.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!desc.isFocusable()) {
//                    desc.setFocusable(true);
//                    desc.requestFocus();
//                }
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityAbout.this, ActivityMainMenu.class));
        overridePendingTransition(R.anim.slide_down, R.anim.slide_down_2);
        finish();

    }
}