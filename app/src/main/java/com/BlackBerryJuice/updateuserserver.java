package com.BlackBerryJuice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Ho33ein on 29/02/2016.
 */
public class updateuserserver extends AsyncTask{

    private String Link="";
    private String Name="";
    private String Mobile="";
    private String Birthday="";
    private String Address="";
    private String Phone="";
    private String Instagram="";
    private String Status="";
    private String Code="";
    Context contex;

    public updateuserserver(String link,String name,String address,String birthday,String instagram,String mobile, String phone,String code,String status,Context context){

        Link=link;
        Name=name;
        Mobile=mobile;
        Birthday=birthday;
        Address=address;
        Phone=phone;
        Instagram=instagram;
        Status=status;
        Code=code;
        contex = context;
    }

    @Override
    protected String doInBackground(Object... arg0) {

        try{
            String data=URLEncoder.encode("name","UTF8")+"="+URLEncoder.encode(Name,"UTF8");
            data+="&"+URLEncoder.encode("address","UTF8")+"="+URLEncoder.encode(Address,"UTF8");
            data+="&"+URLEncoder.encode("birthday","UTF8")+"="+URLEncoder.encode(Birthday,"UTF8");
            data+="&"+URLEncoder.encode("instagram","UTF8")+"="+URLEncoder.encode(Instagram,"UTF8");
            data+="&"+URLEncoder.encode("mobile","UTF8")+"="+URLEncoder.encode(Mobile,"UTF8");
            data+="&"+URLEncoder.encode("phone","UTF8")+"="+URLEncoder.encode(Phone,"UTF8");
            data+="&"+URLEncoder.encode("code","UTF8")+"="+URLEncoder.encode(Code,"UTF8");
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

            EditProfile.res=sb.toString();
            String local = sb.toString();

            Log.e("res in updateserver" , local);
            if(Status.equals("get") && local.length()>12) {
                String name = "";
                String address = "";
                String birthday = "";
                String instagram = "";
                String mobile = "";
                String phone = "";
                String code = "";
                int f = 0;
                int c = 0;
                for (int i = 0; i < local.length(); i++) {

                    if (local.charAt(i) == '|') {

                        String t = local.substring(f, i);

                        if (c == 0) {

                            name = t;
                        }
                        if (c == 1) {

                            address = t;
                        }
                        if (c == 2) {

                            birthday = t;
                        }
                        if (c == 3) {

                            instagram = t;
                        }
                        if (c == 4) {

                            mobile = t;
                        }
                        if (c == 5) {

                            phone = t;
                        }
                        if (c == 6) {

                            code = t;
                        }
                        c += 1;
                        f = i + 1;
                    }
                }
                SharedData.save_last_userinfo(name, birthday, address, phone, instagram, contex);
                //SharedData.save_last_userinfo_cm(code, mobile, contex);
                Log.e("saeed_everything_saved", name + " " + birthday + " " + address + " " + phone + " " + instagram);
            }
        }catch(Exception e){

        }



        return "";
    }

}
