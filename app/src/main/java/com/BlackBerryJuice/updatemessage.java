package com.BlackBerryJuice;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Ho33ein on 29/02/2016.
 */
public class updatemessage extends AsyncTask{

    private String Code="";
    private String Status="";
    private String Messagee="";
    private String Link="";
    Context c;

    public updatemessage(String link,String code,String message, String status , Context context){
        Code=code;
        Link=link;
        Messagee = message;
        Status = status;
        c= context;
    }

    @Override
    protected String doInBackground(Object... arg0) {

        try{
            String data=URLEncoder.encode("code","UTF8")+"="+URLEncoder.encode(Code,"UTF8");
            data+="&"+URLEncoder.encode("message","UTF8")+"="+URLEncoder.encode(Messagee,"UTF8");
            data+="&"+URLEncoder.encode("status","UTF8")+"="+URLEncoder.encode(Status,"UTF8");

            URL mylink=new URL(Link);
            URLConnection connect=mylink.openConnection();

            connect.setDoOutput(true);
            OutputStreamWriter wr=new OutputStreamWriter(connect.getOutputStream());
            wr.write(data);
            wr.flush();


            BufferedReader reader=new BufferedReader(new InputStreamReader(connect.getInputStream()));
            StringBuilder sb=new StringBuilder();

            String line=null;

            while((line=reader.readLine()) !=null){

                sb.append(line);

            }

            ActivitySplash.mes=sb.toString();
            Log.e("saeed_ums",sb.toString());
            SharedData.save_user_special_message(sb.toString(), c);

        }catch(Exception e){



        }



        return "";
    }

}
