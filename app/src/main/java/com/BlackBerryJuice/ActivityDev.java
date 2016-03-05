package com.BlackBerryJuice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.BlackBerryJuice.utils.TextViewPlus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by EhCan on 2/27/2016.
 */
public class ActivityDev extends Activity {
    TextViewPlus vis_;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cube_layout);

        vis_ = (TextViewPlus)findViewById(R.id.vis_);
        vis_.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Uri uri = Uri.parse("http://www.cube-dev.ir");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(likeIng);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_up_2);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.cube-dev.ir")));
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_up_2);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_down, R.anim.slide_down_2);


    }
}