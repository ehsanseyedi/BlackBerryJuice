package com.BlackBerryJuice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class ActivityMainMenu extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
	static DBHelper dbhelper;
	AdapterMainMenu mma;
	Intent iGet = getIntent();
	long Menu_ID;
	String MenuDetailAPI;
	ArrayList<String> images = new ArrayList<>();
	ArrayList<String> sliderimages = new ArrayList<>();
	ArrayList<String> slidertitles = new ArrayList<>();
	ArrayList<String> sliderlinks = new ArrayList<>();
	ProgressBar p1;
	ProgressBar p2;
	ProgressBar p3;
	String GalleryAPI;
	String SliderAPI;
	int IOConnect = 0;
	ImageView g1;
	ImageView g2;
	ImageView g3;
	Intent profile;
	SliderLayout mDemoSlider;
	TextView scrollingtext;
	int widthofscreen;
//	HorizontalScrollView sv;
//	int scroll_pos;
	public static Handler hHandler;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		if(Build.VERSION.SDK_INT >= 21) {
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(this.getResources().getColor(R.color.tameshk_dark));
		}
		setContentView(R.layout.the_new_main_activity);

		Display display = getWindowManager().getDefaultDisplay();
		Point size2 = new Point();
		display.getSize(size2);
		widthofscreen = size2.x;
		Toast.makeText(this,"px:" + widthofscreen,Toast.LENGTH_SHORT).show();

		mDemoSlider = (SliderLayout) findViewById(R.id.slider);


		scrollingtext = (TextView) findViewById(R.id.scrollingtext);
		//scrollingtext.setMovementMethod(new ScrollingMovementMethod());

//		Animation animationToRight = new TranslateAnimation(-600,600, 0, 0);
//		animationToRight.setDuration(17000);
//		animationToRight.setRepeatMode(Animation.RESTART);
//		animationToRight.setRepeatCount(Animation.INFINITE);

//		Rect bounds = new Rect();
//		Paint textPaint = scrollingtext.getPaint();
//		String text = scrollingtext.getText().toString();
//		textPaint.getTextBounds(text, 0, text.length(), bounds);
//		int width = bounds.width();
//		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) scrollingtext.getLayoutParams();
//		lp.width = width + 100;
//		int startX = 400;
		//int widthoftext = scrollingtext.getWidth();

//		final float densityMultiplier = this.getResources().getDisplayMetrics().density;
//		final float scaledPx = 20 * densityMultiplier;
//		Paint paint = new Paint();
//		paint.setTextSize(scaledPx);
//		final int widthoftext = (int) paint.measureText(scrollingtext.getText().toString());
//		scrollingtext.getLayoutParams().width = widthoftext + 200;
//		int offset = (widthoftext + 200) - widthofscreen;
//		Toast.makeText(this,widthoftext+"",Toast.LENGTH_SHORT).show();
//		TranslateAnimation ta = new TranslateAnimation(-widthofscreen, offset, 0, 0);
//		ta.setDuration(15000);
//		ta.setRepeatCount(-1);
//		scrollingtext.setAnimation(ta);



		//scrollingtext.setAnimation(animationToRight);
		//TranslateAnimation n = new TranslateAnimation()

		RelativeLayout order = (RelativeLayout) findViewById(R.id.Order_Cat_Button);
		order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				startActivity(new Intent(ActivityMainMenu.this, ActivityCategoryList.class));
				overridePendingTransition(R.anim.open_next, R.anim.close_next);
			}
		});

		RelativeLayout reserv = (RelativeLayout) findViewById(R.id.Reservation_Button);
		reserv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				startActivity(new Intent(ActivityMainMenu.this, ActivityReservation.class));
				overridePendingTransition (R.anim.open_next, R.anim.close_next);
			}
		});

		ImageView aboutt = (ImageView) findViewById(R.id.about_button);
		aboutt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				startActivity(new Intent(ActivityMainMenu.this, ActivityAbout.class));
				overridePendingTransition (R.anim.slide_up, R.anim.slide_up_2);
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
		SliderAPI = Constant.SliderAPI+"?accesskey="+Constant.AccessKey;

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

		profile = new Intent(ActivityMainMenu.this, Profile.class);
		ImageView profile_textscroller = (ImageView) findViewById(R.id.profile);
		ImageView profiles = (ImageView) findViewById(R.id.profile2);
		profile_textscroller.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(profile);
				finish();
			}
		});
		profiles.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(profile);
				finish();
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
	protected void onStop() {
		// To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
		mDemoSlider.stopAutoCycle();
		super.onStop();
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		//Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
		String curennt_text= slider.getBundle().get("extra")+"";

		String text1 = "";
		String text2 = "";
		String text3 = "";
		String text4 = "";

		try {
			text1 = slidertitles.get(0);
			text2 = slidertitles.get(1);
			text3 = slidertitles.get(2);
			text4 = slidertitles.get(3);
		}catch (Exception e){

		}

		if(curennt_text.equals(text1)){
			String url = sliderlinks.get(0);
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		}else if(curennt_text.equals(text2)){
			String url = sliderlinks.get(1);
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		}else if(curennt_text.equals(text3)) {
			String url = sliderlinks.get(2);
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		}else if (curennt_text.equals(text4)){
			String url = sliderlinks.get(3);
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

	@Override
	public void onPageSelected(int position) {
		Log.d("Slider", "Page Changed: " + position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {}



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

		getDataTask(){

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
			} else{
				g1.setVisibility(View.INVISIBLE);
				g2.setVisibility(View.INVISIBLE);
				g3.setVisibility(View.INVISIBLE);
				p1.setVisibility(View.INVISIBLE);
				p2.setVisibility(View.INVISIBLE);
				p3.setVisibility(View.INVISIBLE);
			}



			//slider

			HashMap<String, String> url_maps = new HashMap<String, String>();
			try {
				url_maps.put(slidertitles.get(0), sliderimages.get(0));
				url_maps.put(slidertitles.get(1), sliderimages.get(1));
				url_maps.put(slidertitles.get(2), sliderimages.get(2));
				url_maps.put(slidertitles.get(3), sliderimages.get(3));
			}catch (Exception e){

			}

			/////////////
			for (String name : url_maps.keySet()) {
				TextSliderView textSliderView = new TextSliderView(ActivityMainMenu.this);
				// initialize a SliderLayout
				textSliderView
						.description(name)
						.image(url_maps.get(name))
						.setScaleType(BaseSliderView.ScaleType.CenterCrop)
						.setOnSliderClickListener(ActivityMainMenu.this);

				//add your extra information
				textSliderView.bundle(new Bundle());
				textSliderView.getBundle()
						.putString("extra", name);

				mDemoSlider.addSlider(textSliderView);
			}
			//mDemoSlider.setPresetTransformer(SliderLayout.Transformer.);
			mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
			//mDemoSlider.setCustomAnimation(new DescriptionAnimation());
			mDemoSlider.setDuration(4000);
			mDemoSlider.addOnPageChangeListener(ActivityMainMenu.this);

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

			HttpUriRequest request2 = new HttpGet(SliderAPI);
			HttpResponse response2 = client.execute(request2);
			InputStream atomInputStream2 = response2.getEntity().getContent();
			BufferedReader in2 = new BufferedReader(new InputStreamReader(atomInputStream2));

			String line2;
			String str2 = "";
			while ((line2 = in2.readLine()) != null){
				str2 += line2;
			}

			Log.e("saeeeeeeed", str);
			// parse json data and store into arraylist variables
			JSONObject json = new JSONObject(str);
			JSONArray pic = json.getJSONArray("picture");

			JSONObject json2 = new JSONObject(str2);
			JSONArray pic2 = json2.getJSONArray("sliderdata");

			Log.e("saeeeeeeed", pic.length()+"");
			for (int i = 0; i < pic.length(); i++) {
				JSONObject object = pic.getJSONObject(i);
				JSONObject gallery = object.getJSONObject("Gallery");
				images.add(Constant.GalleryImageURL + gallery.getString("file"));
				Log.d("imagess", images.get(i));
			}

			Log.e("saeeeeeeed_slider", pic2.length()+"");
			for (int i = 0; i < pic2.length(); i++) {
				JSONObject object2 = pic2.getJSONObject(i);
				JSONObject slider = object2.getJSONObject("Slider");
				sliderimages.add(Constant.SliderImageURL + slider.getString("file"));
				slidertitles.add(slider.getString("text"));
				sliderlinks.add(slider.getString("link"));
				Log.d("slider", sliderimages.get(i));
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


