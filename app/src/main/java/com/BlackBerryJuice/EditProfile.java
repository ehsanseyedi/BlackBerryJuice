package com.BlackBerryJuice;

        import java.util.Timer;
        import java.util.TimerTask;
        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.Window;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

public class EditProfile extends Activity{


    private EditText name,family,oldpass,newpass;
    @SuppressWarnings("unused")
    private TextView tname,tfamily,toldpass,tnewpass,temail,tstatus;
    private TextView email,status;
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
        setContentView(R.layout.editfile);

        tarif();

        Bundle extera=getIntent().getExtras();
        final String s=extera.getString("email");

        email.setText(s);

        new updateuserserver("http://unixantivirus.wc.lt/update.php","","","",s,"get").execute();

        final Timer tm=new Timer();
        final ProgressDialog pd=new ProgressDialog(editprofile.this);
        pd.setMessage("لطفا صبر کنید"+"\n"+"در حال دریافت اطلاعات از سرور");
        pd.show();

        pd.setOnCancelListener(new ProgressDialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {

                tm.cancel();
                pd.cancel();
                new updateuserserver("http://unixantivirus.wc.lt/update.php","","","",s,"get").cancel(true);

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
                            new updateuserserver("http://unixantivirus.wc.lt/update.php","","","",s,"get").cancel(true);
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


                if(!oldpass.getText().toString().equals("")){

                    if(oldpass.getText().toString().equals(pass)){

                        pass=newpass.getText().toString();
                    }else{

                        Toast.makeText(getApplicationContext(), "کلمه عبور صحیح نیست", Toast.LENGTH_LONG).show();

                    }

                }

                new updateuserserver("http://unixantivirus.wc.lt/update.php",name.getText().toString(),family.getText().toString(),pass,s,"put").execute();



                final ProgressDialog pd=new ProgressDialog(editprofile.this);
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

        name=(EditText) findViewById(R.id.edit_name_e);
        family=(EditText) findViewById(R.id.edit_family_e);
        oldpass=(EditText) findViewById(R.id.edit_oldpass_e);
        newpass=(EditText) findViewById(R.id.edit_newpass_e);
        email=(TextView) findViewById(R.id.edit_email_e);

        tname=(TextView) findViewById(R.id.edit_name_t);
        tfamily=(TextView) findViewById(R.id.edit_family_t);
        toldpass=(TextView) findViewById(R.id.edit_oldpass_t);
        tnewpass=(TextView) findViewById(R.id.edit_newpass_t);
        temail=(TextView) findViewById(R.id.edit_email_t);
        tstatus=(TextView) findViewById(R.id.edit_status_t);

        status=(TextView) findViewById(R.id.edit_status_e);

        update=(Button) findViewById(R.id.edit_update_k);
        exit=(Button) findViewById(R.id.edit_exit_k);

    }

    private void po(String temp){


        int f=0;
        int c=0;
        for(int i=0;i<temp.length();i++){

            if(temp.charAt(i)=='|'){

                String t=temp.substring(f, i);

                if(c==0){

                    name.setText(t);
                }
                if(c==1){

                    family.setText(t);
                }
                if(c==2){

                    email.setText(t);
                }
                if(c==3){

                    pass=t;
                }
                if(c==4){

                    if(t.equals("a")){
                        status.setText("دیتابیس شما آپدیت است");
                    }else if(t.equals("b")){
                        status.setText("دیتابیس شما نیاز به بروزرسانی دارد");
                    }

                }

                c+=1;
                f=i+1;
            }

        }


    }

}

