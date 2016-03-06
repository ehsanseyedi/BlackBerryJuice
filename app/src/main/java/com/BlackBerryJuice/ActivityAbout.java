package com.BlackBerryJuice;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BlackBerryJuice.utils.TextViewPlus;

/**
 * Created by EhCan on 2/27/2016.
 */
public class ActivityAbout extends Activity {
    EditText desc;
    private RelativeLayout Send_Button , cancel_button , Pic_Send_Button;

    private String name="";
    public static String res="";
    private int count=0;

    TextViewPlus vis_;
    LinearLayout vis_1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_layout);
        desc = (EditText)findViewById(R.id.desc);
        Send_Button=(RelativeLayout) findViewById(R.id.Send_Button);
        cancel_button=(RelativeLayout) findViewById(R.id.cancel);
        Pic_Send_Button=(RelativeLayout) findViewById(R.id.Pic_Send_Button);

        Pic_Send_Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("http://telegram.me/blackberry_juice");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.telegram");

                try {
                    startActivity(likeIng);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_up_2);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://telegram.me/blackberry_juice")));
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_up_2);
                }

            }
        });
        desc.setTypeface(ActivitySplash.F6);
        vis_ = (TextViewPlus)findViewById(R.id.vis_);
        vis_1 = (LinearLayout)findViewById(R.id.vis_1);
        vis_.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                vis_1.setVisibility(View.VISIBLE);
                vis_.setVisibility(View.GONE);
            }
        });
        cancel_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                vis_1.setVisibility(View.GONE);
                vis_.setVisibility(View.VISIBLE);
            }
        });
//        Bundle extera=getIntent().getExtras();
//        name=extera.getString("name");

        Send_Button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


                if (((desc.getText().toString().trim().length() == 0)) || (!SharedData.do_user_logedin(ActivityAbout.this) && !SharedData.do_user_registered(ActivityAbout.this))) {

                    Log.e("1",""+SharedData.do_user_logedin(ActivityAbout.this));
                    Log.e("2",""+SharedData.do_user_registered(ActivityAbout.this));

                    int ecolor = Color.RED;
                    ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
                    if (desc.getText().toString().trim().length() == 0) {
                        String estring = "لطفا متن مورد نظر خود را وارد کنید";
                        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                        desc.setError(ssbuilder);
                    }
                    else if (!SharedData.do_user_logedin(ActivityAbout.this) && !SharedData.do_user_registered(ActivityAbout.this)) {
                        Toast.makeText(ActivityAbout.this,"لطفاً ابتدا وارد حساب کاربری خود شوید!",Toast.LENGTH_LONG).show();
                    }



                } else {

                    new MessageSend(Constant.Message_Send, SharedData.load_name(ActivityAbout.this), desc.getText().toString(), "0").execute();

                    final Timer tm = new Timer();
                    final ProgressDialog pd = new ProgressDialog(ActivityAbout.this);
                    pd.setMessage("در حال ارسال اطلاعات به سرور..." + "\n" + "برای لغو عملیات کلید بازگشت را بزنید");
                    pd.show();


                    pd.setOnCancelListener(new ProgressDialog.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface arg0) {

                            new MessageSend(Constant.Message_Send, SharedData.load_name(ActivityAbout.this), desc.getText().toString(), "0").cancel(true);
                            count = 0;
                            tm.cancel();

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
                                        Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط" + "\n" + "لطفا بعدا دوباره امتحان کنید", Toast.LENGTH_LONG).show();


                                    }

                                    if (res.equals("ok")) {

                                        pd.cancel();
                                        Toast.makeText(getApplicationContext(), "پیام شما با موفقیت به مدیریت ارسال شد با تشکر", Toast.LENGTH_LONG).show();
                                        res = "";
                                        finish();
                                        tm.cancel();


                                    } else if (res.equals("no")) {
                                        pd.cancel();
                                        Toast.makeText(getApplicationContext(), "خطایی رخ داده است", Toast.LENGTH_LONG).show();
                                        tm.cancel();


                                    }

                                }
                            });

                        }

                    }, 1, 1000);

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