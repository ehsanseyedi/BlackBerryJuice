package com.BlackBerryJuice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.os.AsyncTask;

public class MessageSend extends AsyncTask{

	private String Link="";	
	private String Name="";
	private String Text="";
	private String Status="";
	
	
	public MessageSend(String link,String name,String text,String status){
		
		Link=link;		
		Name=name;
		Text=text;
		Status=status;
	}
	
	
	
	@Override
	protected String doInBackground(Object... arg0) {
	
		
		try{
			
			String data=URLEncoder.encode("name","UTF8")+"="+URLEncoder.encode(Name,"UTF8");
			data+="&"+URLEncoder.encode("text","UTF8")+"="+URLEncoder.encode(Text,"UTF8");
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
			
			ActivityAbout.res=sb.toString();
	
		}catch(Exception e){}

		return "";
	}

}
