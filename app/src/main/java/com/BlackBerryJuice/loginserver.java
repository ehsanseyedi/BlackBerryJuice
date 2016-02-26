package com.BlackBerryJuice;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.os.AsyncTask;


@SuppressWarnings("rawtypes")
public class loginserver extends AsyncTask{

    private String Link="";
    private String code="";
    private String mobile="";



    public loginserver(String link,String email,String pass){

        Link=link;
        code=email;
        mobile=pass;
    }



    @Override
    protected String doInBackground(Object... arg0) {


        try{

            String data=URLEncoder.encode("code","UTF8")+"="+URLEncoder.encode(code,"UTF8");
            data+="&"+URLEncoder.encode("mobile","UTF8")+"="+URLEncoder.encode(mobile,"UTF8");



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

            Login.res=sb.toString();




        }catch(Exception e){



        }



        return "";
    }

}
