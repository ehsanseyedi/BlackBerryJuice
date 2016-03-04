package com.BlackBerryJuice;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ActivityMenuList extends Activity {
	
	ListView listMenu;
	ProgressBar prgLoading;
	//TextView txtTitle;
	EditText edtKeyword;
	ImageButton btnSearch;
	LinearLayout empty_;
	
	static double Tax;
	static String Currency;
	static String Menu_serve;

	AdapterMenuList mla;
	
	// create arraylist variables to store data from server
	static ArrayList<Long> Menu_ID = new ArrayList<Long>();
	static ArrayList<String> Menu_name = new ArrayList<String>();
	static ArrayList<String> Menu_price = new ArrayList<String>();
	static ArrayList<String> Menu_image = new ArrayList<String>();
	static ArrayList<String> Menu_exist = new ArrayList<String>();

	String MenuAPI;
	String TaxCurrencyAPI;
	int IOConnect = 0;
	long Category_ID;
	String Category_name;
	String Keyword;
	static DBHelper dbhelper;
	
	DecimalFormat formatData = new DecimalFormat("#.##");
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(Build.VERSION.SDK_INT >= 21) {
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(this.getResources().getColor(R.color.tameshk_dark));
		}
        setContentView(R.layout.menu_list);
		dbhelper = new DBHelper(this);

        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        listMenu = (ListView) findViewById(R.id.listMenu);
        edtKeyword = (EditText) findViewById(R.id.edtKeyword);
		edtKeyword.setTypeface(ActivitySplash.F1);
				btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		empty_= (LinearLayout) findViewById(R.id.empty_);
        
        // menu API url
        MenuAPI = Constant.MenuAPI+"?accesskey="+Constant.AccessKey+"&category_id=";
        // tax and currency API url
        TaxCurrencyAPI = Constant.TaxCurrencyAPI+"?accesskey="+Constant.AccessKey;
        
        // get category id and category name that sent from previous page
        Intent iGet = getIntent();
        Category_ID = iGet.getLongExtra("category_id",0);
        Category_name = iGet.getStringExtra("category_name");
        MenuAPI += Category_ID;
        
        // set category name to textview
//        txtTitle.setText(Category_name);
        
        mla = new AdapterMenuList(ActivityMenuList.this);
       
        // call asynctask class to request tax and currency data from server
        new getTaxCurrency().execute();
		
        // event listener to handle search button when clicked
		btnSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// get keyword and send it to server
				try {
					Keyword = URLEncoder.encode(edtKeyword.getText().toString(), "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MenuAPI += "&keyword="+Keyword;
				IOConnect = 0;
    			listMenu.invalidateViews();
    			clearData();
				new getDataTask().execute();
			}
		});
		
		// event listener to handle list when clicked
		listMenu.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				// go to menu detail page
				Intent iDetail = new Intent(ActivityMenuList.this, ActivityMenuDetail.class);
				iDetail.putExtra("menu_id", Menu_ID.get(position));
				startActivity(iDetail);
				overridePendingTransition(R.anim.open_next, R.anim.close_next);
			}
		});       
        
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_category, menu);
        
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.cart:
			// refresh action
			Intent iMyOrder = new Intent(ActivityMenuList.this, ActivityCart.class);
			startActivity(iMyOrder);
			overridePendingTransition (R.anim.open_next, R.anim.close_next);
			return true;
			
		case R.id.refresh:
			IOConnect = 0;
			listMenu.invalidateViews();
			clearData();
			new getDataTask().execute();
			return true;			
			
		case android.R.id.home:
            // app icon in action bar clicked; go home
        	this.finish();
        	overridePendingTransition(R.anim.open_main, R.anim.close_next);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
    // asynctask class to handle parsing json in background
    public class getTaxCurrency extends AsyncTask<Void, Void, Void> {
    	
    	// show progressbar first
    	getTaxCurrency(){
    		if(!prgLoading.isShown()){
    			prgLoading.setVisibility(View.VISIBLE);
				empty_.setVisibility(View.GONE);
    		}
    	}
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// parse json data from server in background
			parseJSONDataTax();
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// when finish parsing, hide progressbar
			prgLoading.setVisibility(View.GONE);
			// if internet connection and data available request menu data from server
			// otherwise, show alert text
			if((Currency != null) && IOConnect == 0){
				new getDataTask().execute();
			}else{
				empty_.setVisibility(View.VISIBLE);
			}
		}
    }

    // method to parse json data from server
	public void parseJSONDataTax(){
		try {
	        // request data from tax and currency API
	        HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
	        HttpUriRequest request = new HttpGet(TaxCurrencyAPI);
			HttpResponse response = client.execute(request);
			InputStream atomInputStream = response.getEntity().getContent();
	
			BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));
		        
	        String line;
	        String str = "";
	        while ((line = in.readLine()) != null){
	        	str += line;
	        }
    
	        
	        // parse json data and store into tax and currency variables
			JSONObject json = new JSONObject(str);
			JSONArray data = json.getJSONArray("data"); // this is the "items: [ ] part
			
			JSONObject object_tax = data.getJSONObject(0);
			JSONObject tax = object_tax.getJSONObject("tax_n_currency");
			    
			Tax = Double.parseDouble(tax.getString("Value"));
			//JSONObject object = data.getJSONObject(i);
			JSONObject object_currency = data.getJSONObject(1);
			JSONObject currency = object_currency.getJSONObject("tax_n_currency");
			//JSONObject menu = object.getJSONObject("Menu_detail");

			Currency = currency.getString("Value");
			//Menu_serve = menu.getString("Serve_for");
			
			
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
			IOConnect = 1;
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}	
	}
    
	// clear arraylist variables before used
    void clearData(){
    	Menu_ID.clear();
    	Menu_name.clear();
    	Menu_price.clear();
    	Menu_exist.clear();
    	Menu_image.clear();
    }
    
    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {
    	
    	// show progressbar first
    	getDataTask(){
    		if(!prgLoading.isShown()){
    			prgLoading.setVisibility(View.VISIBLE);
				empty_.setVisibility(View.GONE);
    		}
    	}
    	
    	@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
    		// parse json data from server in background
			parseJSONData();
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// when finish parsing, hide progressbar
			prgLoading.setVisibility(View.GONE);
			
			// if data available show data on list
			// otherwise, show alert text
			if(Menu_ID.size() > 0){
				listMenu.setVisibility(View.VISIBLE);
				listMenu.setAdapter(mla);
			}else{
				empty_.setVisibility(View.VISIBLE);
			}
			
		}
    }
    
    // method to parse json data from server
    public void parseJSONData(){
    	
    	clearData();
    	
    	try {
	        // request data from menu API
	        HttpClient client = new DefaultHttpClient();
	        HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
	        HttpUriRequest request = new HttpGet(MenuAPI);
			HttpResponse response = client.execute(request);
			InputStream atomInputStream = response.getEntity().getContent();

			BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));
		        
	        String line;
	        String str = "";
	        while ((line = in.readLine()) != null){
	        	str += line;
	        }
        
			// parse json data and store into arraylist variables
			JSONObject json = new JSONObject(str);
			JSONArray data = json.getJSONArray("data"); // this is the "items: [ ] part
			
			for (int i = 0; i < data.length(); i++) {
			    JSONObject object = data.getJSONObject(i);
			    JSONObject menu = object.getJSONObject("Menu");

				//me
				//JSONObject menusaeed = object.getJSONObject("Menu_detail");

			    Menu_ID.add(Long.parseLong(menu.getString("Menu_ID")));
				Menu_exist.add(menu.getString("Serve_for"));
			    Menu_name.add(menu.getString("Menu_name"));
				int price = (int) menu.getDouble("Price");
				//int toman = (int) price / 10;
				String sp = NumberFormat.getNumberInstance(Locale.US).format(price);
			    Menu_price.add(sp);
			    Menu_image.add(menu.getString("Menu_image"));
				    

			}
				
				
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}	
    }


    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	//mla.imageLoader.clearCache();
    	listMenu.setAdapter(null);
    	super.onDestroy();
    }
	 
    
    @Override
	public void onConfigurationChanged(final Configuration newConfig)
	{
	    // Ignore orientation change to keep activity from restarting
	    super.onConfigurationChanged(newConfig);
	}
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	finish();
    	overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }

    
}
