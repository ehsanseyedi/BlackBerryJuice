package com.BlackBerryJuice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.BlackBerryJuice.activities.ImageGalleryActivity;
import com.BlackBerryJuice.enums.PaletteColorType;

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
import java.util.ArrayList;
import com.BlackBerryJuice.Constant;

public class ActivityMainMenu extends Activity {
	static DBHelper dbhelper;
	AdapterMainMenu mma;
	Intent iGet = getIntent();
	long Menu_ID;
	String MenuDetailAPI;
	ArrayList<String> images = new ArrayList<>();
	ProgressBar p1;
	ProgressBar p2;
	ProgressBar p3;
	String GalleryAPI;
	int IOConnect = 0;
	ImageView g1;
	ImageView g2;
	ImageView g3;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		if(Build.VERSION.SDK_INT >= 21) {
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(this.getResources().getColor(R.color.tameshk_dark));
		}
		setContentView(R.layout.main_layout);

		RelativeLayout order = (RelativeLayout) findViewById(R.id.Order_Cat_Button);
		order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				startActivity(new Intent(ActivityMainMenu.this, ActivityCategoryList.class));
				overridePendingTransition (R.anim.open_next, R.anim.close_next);
			}
		});

		// get menu id that sent from previous page
		Intent iGet = getIntent();
		Menu_ID = iGet.getLongExtra("menu_id", 0);
		// Menu detail API url
		MenuDetailAPI = Constant.MenuDetailAPI+"?accesskey="+Constant.AccessKey+"&menu_id="+Menu_ID;

		g1 = (ImageView) findViewById(R.id.g1);
		g2 = (ImageView) findViewById(R.id.g2);
		g3 = (ImageView) findViewById(R.id.g3);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		// checking internet connection
		if (!Constant.isNetworkAvailable(ActivityMainMenu.this)) {
			Toast.makeText(ActivityMainMenu.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
		}

		mma = new AdapterMainMenu(this);
		dbhelper = new DBHelper(this);

		// create database
		try {
			dbhelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		// then, the database will be open to use
		try {
			dbhelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}

		// if user has already ordered food previously then show confirm dialog
		if (dbhelper.isPreviousDataExist()) {
			showAlertDialog();
		}

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}


		//saeed
		GalleryAPI = Constant.GalleryAPI+"?accesskey="+Constant.AccessKey;

		new getDataTask().execute();

		p1 = (ProgressBar)findViewById(R.id.pr1);
		p2 = (ProgressBar)findViewById(R.id.pr2);
		p3 = (ProgressBar)findViewById(R.id.pr3);


		p1.setVisibility(View.VISIBLE);
		p2.setVisibility(View.VISIBLE);
		p3.setVisibility(View.VISIBLE);

		final Intent intent = new Intent(ActivityMainMenu.this, ImageGalleryActivity.class);
		ImageView gal = (ImageView) findViewById(R.id.gogal);
		gal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.putStringArrayListExtra("images", images);
				// optionally set background color using Palette
				intent.putExtra("palette_color_type", PaletteColorType.VIBRANT);
				startActivity(intent);
			}
		});

	}


	// show confirm dialog to ask user to delete previous order or not
	void showAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.confirm);
		builder.setMessage(getString(R.string.db_exist_alert));
		builder.setCancelable(false);
		builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// delete order data when yes button clicked
				dbhelper.deleteAllData();
				dbhelper.close();

			}
		});

		builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// close dialog when no button clicked
				dbhelper.close();
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		dbhelper.deleteAllData();
		dbhelper.close();
		finish();
		overridePendingTransition(R.anim.open_main, R.anim.close_next);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
			case 0:
				fragment = new ActivityHome();
				break;

			case 1:
				dbhelper.deleteAllData();
				dbhelper.close();
				ActivityMainMenu.this.finish();
				overridePendingTransition(R.anim.open_next, R.anim.close_next);
				break;

			default:
				break;
		}

		if (fragment != null) {
			//android.app.FragmentManager fragmentManager = getFragmentManager();
			//fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}


	// clear arraylist variables before used
	void clearData(){
		images.clear();
	}

	// asynctask class to handle parsing json in background
	public class getDataTask extends AsyncTask<Void, Void, Void> {

		// show progressbar first
		getDataTask(){
//			if(!prgLoading.isShown()){
//				prgLoading.setVisibility(0);
//				txtAlert.setVisibility(8);
//			}
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



			if((images.size() >= 3) && (IOConnect == 0)){
				new DownloadImageTask(g1,p1)
						.execute(images.get(0));
				new DownloadImageTask(g2,p2)
						.execute(images.get(1));
				new DownloadImageTask(g3,p3)
						.execute(images.get(2));
			}else if((images.size() == 2) && (IOConnect == 0)){
				new DownloadImageTask(g1,p1)
						.execute(images.get(0));
				new DownloadImageTask(g2,p2)
						.execute(images.get(1));
				g3.setVisibility(View.INVISIBLE);
				p3.setVisibility(View.INVISIBLE);

			}else if((images.size() == 1) && (IOConnect == 0)){
				new DownloadImageTask(g1,p1)
						.execute(images.get(0));

				g2.setVisibility(View.INVISIBLE);
				g3.setVisibility(View.INVISIBLE);
				p2.setVisibility(View.INVISIBLE);
				p3.setVisibility(View.INVISIBLE);
			}
			else{
				g1.setVisibility(View.INVISIBLE);
				g2.setVisibility(View.INVISIBLE);
				g3.setVisibility(View.INVISIBLE);
				p1.setVisibility(View.INVISIBLE);
				p2.setVisibility(View.INVISIBLE);
				p3.setVisibility(View.INVISIBLE);
			}
		}
	}

	// method to parse json data from server
	public void parseJSONData(){

		clearData();

		try {
			// request data from Category API
			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
			HttpUriRequest request = new HttpGet(GalleryAPI);
			HttpResponse response = client.execute(request);
			InputStream atomInputStream = response.getEntity().getContent();
			BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));

			String line;
			String str = "";
			while ((line = in.readLine()) != null){
				str += line;
			}
			Log.e("saeeeeeeed", str);
			// parse json data and store into arraylist variables
			JSONObject json = new JSONObject(str);
			JSONArray pic = json.getJSONArray("picture");
			Log.e("saeeeeeeed", pic.length()+"");
			for (int i = 0; i < pic.length(); i++) {
				JSONObject object = pic.getJSONObject(i);
				JSONObject gallery = object.getJSONObject("Gallery");
				images.add(Constant.GalleryImageURL + gallery.getString("file"));
				Log.d("imagess", images.get(i));
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

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		ProgressBar PPBB;
		public DownloadImageTask(ImageView bmImage,ProgressBar pb) {
			this.bmImage = bmImage;
			this.PPBB = pb;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
			PPBB.setVisibility(View.GONE);
		}
	}

}