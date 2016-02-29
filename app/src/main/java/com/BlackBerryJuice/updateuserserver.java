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
    private String Family="";
    private String Pass="";
    private String Email="";
    private String Code="";

    public updateuserserver(String link,String name,String family,String pass,String email,String code){

        Link=link;
        Name=name;
        Family=family;
        Pass=pass;
        Email=email;
        Code=code;
    }



    @Override
    protected String doInBackground(Object... arg0) {


        try{
            String data=URLEncoder.encode("name","UTF8")+"="+URLEncoder.encode(Name,"UTF8");
            data+="&"+URLEncoder.encode("family","UTF8")+"="+URLEncoder.encode(Family,"UTF8");
            data+="&"+URLEncoder.encode("password","UTF8")+"="+URLEncoder.encode(Pass,"UTF8");
            data+="&"+URLEncoder.encode("email","UTF8")+"="+URLEncoder.encode(Email,"UTF8");
            data+="&"+URLEncoder.encode("code","UTF8")+"="+URLEncoder.encode(Code,"UTF8");


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

            editprofile.res=sb.toString();




        }catch(Exception e){



        }



        return "";
    }

}
