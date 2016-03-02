package com.BlackBerryJuice;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by EhCan on 2/27/2016.
 */
public class ActivityAbout extends Activity {
    EditText desc;
    private RelativeLayout Send_Button;

    private String name="";
    public static String res="";
    private int count=0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_layout);
        desc = (EditText)findViewById(R.id.desc);
        Send_Button=(RelativeLayout) findViewById(R.id.Send_Button);
        desc.setTypeface(ActivitySplash.F6);

//        Bundle extera=getIntent().getExtras();
//        name=extera.getString("name");

        Send_Button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


                if(((desc.getText().toString().trim().length()==0))){

                    int ecolor = Color.RED;
                    ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);

                    if(desc.getText().toString().trim().length()==0){
                        String estring = "لطفا متن مورد نظر خود را وارد کنید";
                        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                        desc.setError(ssbuilder);}




                }else{

                    new MessageSend(Constant.Message_Send,"حسین بیکی",desc.getText().toString(),"0").execute();

                    final Timer tm=new Timer();
                    final ProgressDialog pd=new ProgressDialog(ActivityAbout.this);
                    pd.setMessage("در حال ارسال اطلاعات به سرور..."+"\n"+"برای لغو عملیات کلید بازگشت را بزنید");
                    pd.show();


                    pd.setOnCancelListener(new ProgressDialog.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface arg0) {

                            new MessageSend(Constant.Message_Send,"حسین بیکی",desc.getText().toString(),"0").cancel(true);
                            count=0;
                            tm.cancel();

                        }
                    });



                    tm.scheduleAtFixedRate(new TimerTask(){
                        public void run() {
                            runOnUiThread(new Runnable(){
                                public void run() {

                                    count++;
                                    if(count==30){

                                        pd.cancel();
                                        tm.cancel();
                                        count=0;
                                        Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط"+"\n"+"لطفا بعدا دوباره امتحان کنید", Toast.LENGTH_LONG).show();


                                    }

                                    if(res.equals("ok")){

                                        pd.cancel();
                                        Toast.makeText(getApplicationContext(), "پیام شما با موفقیت به مدیریت ارسال شد با تشکر", Toast.LENGTH_LONG).show();
                                        res="";
                                        finish();
                                        tm.cancel();


                                    }else if(res.equals("no")){
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
        startActivity(new Intent(ActivityAbout.this, ActivityMainMenu.class));
        overridePendingTransition(R.anim.slide_down, R.anim.slide_down_2);
        finish();

    }
}