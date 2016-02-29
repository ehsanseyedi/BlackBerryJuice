package com.BlackBerryJuice;

        import java.util.Timer;
        import java.util.TimerTask;
        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Build;
        import android.os.Bundle;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

public class EditProfile extends Activity{


    EditText name,mobile,birthday,address,phone,instagram;
    @SuppressWarnings("unused")
    //private TextView tname,tfamily,toldpass,tnewpass,temail,tstatus;
    private TextView email;
    @SuppressWarnings("unused")
    private Button update,exit;
    public static String res="";
    private String pass="";
    private int count=0;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.tameshk_dark));
        }
        setContentView(R.layout.activity_edit_user_profile);

        tarif();

        Bundle extera=getIntent().getExtras();
        final String s=extera.getString("email");

        email.setText(s);

        //new updateuserserver(Constant.Update_ProfileURL,"","","",s,"get").execute();

        final Timer tm=new Timer();
        final ProgressDialog pd=new ProgressDialog(EditProfile.this);
        pd.setMessage("لطفا صبر کنید"+"\n"+"در حال دریافت اطلاعات از سرور");
        pd.show();

        pd.setOnCancelListener(new ProgressDialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {

                tm.cancel();
                pd.cancel();
                //new updateuserserver(Constant.Update_ProfileURL,"","","",s,"get").cancel(true);

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
                            //new updateuserserver(Constant.Update_ProfileURL,"","","",s,"get").cancel(true);
                            Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط", Toast.LENGTH_LONG).show();
                            finish();

                        }

                        if(!res.equals("")){

                            pd.cancel();
                            po(res);
                            res="";
                            tm.cancel();

                        }

                    }
                });

            }

        }, 1, 1000);


        update.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View arg0) {

                //new updateuserserver(Constant.Update_ProfileURL,name.getText().toString(),mobile.getText().toString(),birthday.getText().toString(),address.getText().toString(),phone.getText().toString(),instagram.getText().toString(),"put").execute();


                final ProgressDialog pd=new ProgressDialog(EditProfile.this);
                pd.setMessage("لطفا صبر کنید"+"در حال ارسال اطلاعات به سرور");
                pd.show();


                final Timer tm=new Timer();
                tm.scheduleAtFixedRate(new TimerTask(){
                    public void run() {
                        runOnUiThread(new Runnable(){
                            public void run() {

                                count++;
                                if(count==30){

                                    pd.cancel();
                                    tm.cancel();
                                    count=0;
                                    Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط", Toast.LENGTH_LONG).show();


                                }

                                if(!res.equals("")){

                                    pd.cancel();
                                    Toast.makeText(getApplicationContext(), "اطلاعات با موفقیت آپدیت شد", Toast.LENGTH_LONG).show();
                                    tm.cancel();
                                }



                            }
                        });

                    }

                }, 1, 1000);
            }
        });

    }

    private void tarif(){

        name    =(EditText) findViewById(R.id.name);
        mobile    =(EditText) findViewById(R.id.mobile);
        birthday    =(EditText) findViewById(R.id.birthday);
        address    =(EditText) findViewById(R.id.address);
        phone    =(EditText) findViewById(R.id.phone);
        instagram    =(EditText) findViewById(R.id.instagram);


//        tname=(TextView) findViewById(R.id.edit_name_t);
//        tfamily=(TextView) findViewById(R.id.edit_family_t);
//        toldpass=(TextView) findViewById(R.id.edit_oldpass_t);
//        tnewpass=(TextView) findViewById(R.id.edit_newpass_t);
//        temail=(TextView) findViewById(R.id.edit_email_t);
//        tstatus=(TextView) findViewById(R.id.edit_status_t);

        //status=(TextView) findViewById(R.id.edit_status_e);

        update=(Button) findViewById(R.id.update);
        exit=(Button) findViewById(R.id.cancel);

    }

    private void po(String temp){

        String code;
        int f=0;
        int c=0;
        for(int i=0;i<temp.length();i++){

            if(temp.charAt(i)=='|'){

                String t=temp.substring(f, i);

                if(c==0){

                    name.setText(t);
                }
                if(c==1){

                    mobile.setText(t);
                }
                if(c==2){

                    birthday.setText(t);
                }
                if(c==3){

                    address.setText(t);
                }
                if(c==4){

                    phone.setText(t);
                }
                if(c==5){

                    instagram.setText(t);
                }
                if(c==6){

                    code=t;
                }
                c+=1;
                f=i+1;
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditProfile.this,Profile.class));
        finish();
    }

}

