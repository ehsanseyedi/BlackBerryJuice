package com.BlackBerryJuice;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.BlackBerryJuice.utils.TextViewPlus;

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
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ActivityMenuDetail extends Activity {
	Intent gotocard;
	Intent gotologin;
	ImageView imgPreview;
	TextView txtText, txtSubText , txtSubText2;
	TextViewPlus txtDescription;
	Button btnAdd;
	ScrollView sclDetail;
	ProgressBar prgLoading;
	TextView txtAlert;
	LinearLayout adder;
	LinearLayout buylayout;
	LinearLayout desc;
	// declare dbhelper object
	DBHelper dbhelper;
	TextView counter;
	// declare ImageLoader object
	ImageLoader imageLoader;
	
	// declare variables to store menu data
	String Menu_image, Menu_name, Menu_serve, Menu_description;
	double Menu_price;
	int Menu_quantity;
	long Menu_ID;
	String MenuDetailAPI;
	int IOConnect = 0;
	
	// create price format
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
        setContentView(R.layout.menu_detail);

//        ActionBar bar = getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
//        bar.setTitle("Detail Menu");
//        bar.setDisplayHomeAsUpEnabled(true);
//        bar.setHomeButtonEnabled(true);

		counter = (TextView) findViewById(R.id.counter);
		adder = (LinearLayout) findViewById(R.id.adder);
		buylayout = (LinearLayout) findViewById(R.id.buylayout);
		desc = (LinearLayout) findViewById(R.id.desclayout);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        txtText = (TextView) findViewById(R.id.txtText);
        txtSubText = (TextView) findViewById(R.id.txtSubText);
        txtSubText2 = (TextView) findViewById(R.id.txtSubText2);
        txtDescription = (TextViewPlus) findViewById(R.id.txtDescription);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        //btnShare = (Button) findViewById(R.id.btnShare);
        sclDetail = (ScrollView) findViewById(R.id.sclDetail);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        txtAlert = (TextView) findViewById(R.id.txtAlert);

        // get screen device width and height
        DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int wPix = dm.widthPixels;
		int hPix = wPix / 2 + 50;
		
		// change menu image width and height
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wPix, hPix);
        imgPreview.setLayoutParams(lp);
        
        imageLoader = new ImageLoader(ActivityMenuDetail.this);
        dbhelper = new DBHelper(this);
		
		// get menu id that sent from previous page
        Intent iGet = getIntent();
        Menu_ID = iGet.getLongExtra("menu_id", 0);
        // Menu detail API url
		Log.e("menu id in detail",Menu_ID+"");
        MenuDetailAPI = Constant.MenuDetailAPI+"?accesskey="+Constant.AccessKey+"&menu_id="+Menu_ID;
        
        // call asynctask class to request data from server
        new getDataTask().execute();      
        
        // event listener to handle add button when clicked
        btnAdd.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gotocard = new Intent(ActivityMenuDetail.this, ActivityCart.class);
				gotologin = new Intent(ActivityMenuDetail.this, Login2.class);
				if( SharedData.do_user_registered(ActivityMenuDetail.this) || SharedData.do_user_logedin(ActivityMenuDetail.this)) {
					startActivity(gotocard);
				}else{
					startActivity(gotologin);
				}
				finish();
				overridePendingTransition (R.anim.open_next, R.anim.close_next);
			}
		});

		LinearLayout add = (LinearLayout) findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (adder.isShown()){
					try {
						adder.startAnimation(AnimationUtils.loadAnimation(ActivityMenuDetail.this, android.R.anim.fade_out));
					} catch (Exception e) {}
					adder.setVisibility(View.GONE);
				}else{
					inputDialog();
				}
			}
		});
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_detail, menu);
		return true;
	}

    
    // method to show number of order form
    void inputDialog(){
    	
    	// open database first
    	try{
			dbhelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}

		adder.setVisibility(View.VISIBLE);
		try {
			adder.startAnimation(AnimationUtils.loadAnimation(ActivityMenuDetail.this, android.R.anim.fade_in));
		} catch (Exception e) {}
    	//AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	Button plus = (Button) findViewById(R.id.plus);
    	Button minez = (Button) findViewById(R.id.minez);
		LinearLayout done = (LinearLayout) findViewById(R.id.countdone);
		LinearLayout undone = (LinearLayout) findViewById(R.id.countcancel);
		//counter.setText("1");


		final Vibrator vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);


		plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int i = Integer.parseInt(counter.getText().toString());
				if (i <= 99) {
					i++;
				}
				counter.setText(i + "");
			}
		});

		minez.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int i = Integer.parseInt(counter.getText().toString());
				if(i > 1){
					i--;
				}else{
					vib.vibrate(300);
				}
				counter.setText(i+"");
			}
		});

		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				int quantity = 0;
				quantity = Integer.parseInt(counter.getText().toString());
				if(dbhelper.isDataExist(Menu_ID)){
					dbhelper.updateData(Menu_ID, quantity, (Menu_price*quantity));
				}else{
					dbhelper.addData(Menu_ID, Menu_name, quantity, (Menu_price*quantity));
				}
				adder.setVisibility(View.GONE);
				try {
					adder.startAnimation(AnimationUtils.loadAnimation(ActivityMenuDetail.this, android.R.anim.fade_out));
				} catch (Exception e) {}
				Toast.makeText(ActivityMenuDetail.this,"سفارش با موفقیت به سبد خرید افزوده شد",Toast.LENGTH_SHORT).show();
			}
		});

		undone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					adder.startAnimation(AnimationUtils.loadAnimation(ActivityMenuDetail.this, android.R.anim.fade_out));
				} catch (Exception e) {}
				adder.setVisibility(View.GONE);
			}
		});

    }
    
    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {
    	
    	// show progressbar first
    	getDataTask(){
    		if(!prgLoading.isShown()){
    			prgLoading.setVisibility(View.VISIBLE);
				txtAlert.setVisibility(View.GONE);
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
			// if internet connection and data available show data
			// otherwise, show alert text
			if((Menu_name != null) && IOConnect == 0){
				sclDetail.setVisibility(View.VISIBLE);
			
				imageLoader.DisplayImage(Constant.AdminPageURL + Menu_image, imgPreview);
				
				txtText.setText(Menu_name);
				int price = (int) Menu_price;
				//int toman = (int) price / 10;
				String sp = NumberFormat.getNumberInstance(Locale.US).format(price);
				txtSubText.setText("قیمت : " + sp + " " + ActivityMenuList.Currency);
				txtSubText2.setText("وضعیت : " + Menu_serve);

				txtDescription.setText(Menu_description);

				if (Menu_serve.equals("ناموجود")){
					buylayout.setVisibility(View.GONE);
				}else{
					buylayout.setVisibility(View.VISIBLE);
				}

				if (Menu_description.equals(" ")){
					desc.setVisibility(View.GONE);
				}else{
					desc.setVisibility(View.VISIBLE);
				}

			}else{
				txtAlert.setVisibility(View.VISIBLE);
			}
		}
    }

    // method to parse json data from server
    public void parseJSONData(){
    	
    	try {
    		// request data from menu detail API
	        HttpClient client = new DefaultHttpClient();
	        HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
	        HttpUriRequest request = new HttpGet(MenuDetailAPI);
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
				
			for (int i = 0; i < data.length(); i++){
			    JSONObject object = data.getJSONObject(i);
			    JSONObject menu = object.getJSONObject("Menu_detail");
			    
			    Menu_image = menu.getString("Menu_image");
			    Menu_name = menu.getString("Menu_name");
			    Menu_price = Double.valueOf(formatData.format(menu.getDouble("Price")));
			    Menu_serve = menu.getString("Serve_for");
			    Menu_description = menu.getString("Description");
			    Menu_quantity = menu.getInt("Quantity");
			}
				
				
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
			IOConnect = 1;
		    e.printStackTrace();
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}	
    }

	
    // close database before back to previous page
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	dbhelper.close();
    	finish();
    	overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }
	

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	//imageLoader.clearCache();
    	super.onDestroy();
    }
	 
    
    @Override
	public void onConfigurationChanged(final Configuration newConfig)
	{
	    // Ignore orientation change to keep activity from restarting
	    super.onConfigurationChanged(newConfig);
	}
    
    
}
