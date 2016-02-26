package com.BlackBerryJuice;

/**
 * Created by Saeed on 2/26/2016.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.os.AsyncTask;



@SuppressWarnings("rawtypes")
public class registerserver extends AsyncTask{

    private String Link="";

    private String name="";
    private String address="";
    private String bithday="";
    private String instagram="";
    private String mobile="";
    private String phone="";
    //private String code="";


    public registerserver(String link,String namez,String addressz,String bithdayz,String instagramz,String mobilez,String phonez){

        Link=link;
        name=namez;
        address=addressz;
        bithday=bithdayz;
        instagram = instagramz;
        mobile=mobilez;
        phone=phonez;
        //code=codez;
    }



    @Override
    protected String doInBackground(Object... arg0) {


        try{

            String data=URLEncoder.encode("namez","UTF8")+"="+URLEncoder.encode(name,"UTF8");
            data+="&"+URLEncoder.encode("addressz","UTF8")+"="+URLEncoder.encode(address,"UTF8");
            data+="&"+URLEncoder.encode("bithdayz","UTF8")+"="+URLEncoder.encode(bithday,"UTF8");
            data+="&"+URLEncoder.encode("instagramz","UTF8")+"="+URLEncoder.encode(instagram,"UTF8");
            data+="&"+URLEncoder.encode("mobilez","UTF8")+"="+URLEncoder.encode(mobile,"UTF8");
            data+="&"+URLEncoder.encode("phonez","UTF8")+"="+URLEncoder.encode(phone,"UTF8");
            //data+="&"+URLEncoder.encode("codez","UTF8")+"="+URLEncoder.encode(code,"UTF8");

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

            Register.res=sb.toString();




        }catch(Exception e){



        }



        return "";
    }

}
