package com.BlackBerryJuice;


import android.os.AsyncTask;

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

    private String Message="";
    private String Code="";
    private String Link="";

    public updatemessage(String link,String message, String code, String status){
        Code=code;
        Message=message;
        Link=link;
    }

    @Override
    protected String doInBackground(Object... arg0) {

        try{
            String data=URLEncoder.encode("message","UTF8")+"="+URLEncoder.encode(Message,"UTF8");
            data+="&"+URLEncoder.encode("address","UTF8")+"="+URLEncoder.encode(Code,"UTF8");


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




        }catch(Exception e){



        }



        return "";
    }

}
