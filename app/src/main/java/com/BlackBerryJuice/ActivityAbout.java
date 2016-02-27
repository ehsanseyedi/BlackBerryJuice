package com.BlackBerryJuice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

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

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityAbout.this, ActivityMainMenu.class));
        overridePendingTransition(R.anim.slide_down, R.anim.slide_down_2);
        finish();

    }
}