package com.BlackBerryJuice;


        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.net.URL;
        import java.net.URLConnection;
        import java.net.URLEncoder;
        import android.os.AsyncTask;

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

    public updateuserserver(String link,String name,String mobile,String birthday,String address,String phone, String instagram,String code,String status){

        Link=link;
        Name=name;
        Mobile=mobile;
        Birthday=birthday;
        Address=address;
        Phone=phone;
        Instagram=instagram;
        Status=status;
        Code=code;
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




        }catch(Exception e){



        }



        return "";
    }

}
