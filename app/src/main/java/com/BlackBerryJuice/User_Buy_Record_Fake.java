package com.BlackBerryJuice;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Saeed on 3/3/2016.
 */
public class User_Buy_Record_Fake extends Activity {
    public static String trans="0";

    //Button btnSend;
    //EditText edtName, edtName2, edtPhone, edtOrderList, edtComment, edtAlamat, edtEmail, edtKota, edtProvinsi;

    // declare dbhelper object
    static DBHelper dbhelper;
    ArrayList<ArrayList<Object>> data;

    // declare string variables to store data
    String Name, Name2, Phone, Date_n_Time, Alamat, Email, Kota, Provinsi;
    String OrderList = "";
    String Comment = "";

    // declare static variables to store tax and currency data
    static double Tax;
    static String Currency;

    // create price format
    DecimalFormat formatData = new DecimalFormat("#.##");

    String Result;
    private Double totalprice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.tameshk_dark));
        }
        setContentView(R.layout.user_buy_record_fake);

        Button back_1 = (Button)findViewById(R.id.back_1);
        back_1.setTypeface(ActivitySplash.F2);
        back_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Buy_Record_Fake.this, ActivityMainMenu.class));
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                finish();
            }
        });

        TextView status = (TextView) findViewById(R.id.status);
        TextView rahgirt = (TextView) findViewById(R.id.rahgir);

        Intent intent = getIntent();
        String rahgir = intent.getStringExtra("rahgir");
        String paid = intent.getStringExtra("price");

        if(rahgir.equals("null")){
            status.setText("پرداخت ناموفق");
            rahgirt.setText("");
        }else{
            status.setText( "پرداخت موفقیت آمیز");
            rahgirt.setText(rahgir);
        }









        dbhelper = new DBHelper(this);
        // open database
        try{
            dbhelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }


        Bundle b = getIntent().getExtras();
        totalprice = b.getDouble("price");
        
        ////sapration

        new getDataTask().execute();


        Name = SharedData.load_name(User_Buy_Record_Fake.this);
        if(SharedData.load_address2(User_Buy_Record_Fake.this).equals("")){
            Alamat =  SharedData.load_address(User_Buy_Record_Fake.this);
        }else{
            Alamat =  SharedData.load_address2(User_Buy_Record_Fake.this);
        }
        if(!SharedData.load_reservarion_time(User_Buy_Record_Fake.this).equals("")){
            Kota =  SharedData.load_reservarion_desc(User_Buy_Record_Fake.this);
        }else{
            Kota = "";
        }
        Provinsi =  SharedData.load_phone(User_Buy_Record_Fake.this);
        Email = "cube";
        Name2 = SharedData.load_code(User_Buy_Record_Fake.this);
        Phone =  SharedData.load_mobile(User_Buy_Record_Fake.this);
        Comment = SharedData.load_user_product_desc(User_Buy_Record_Fake.this);
        if(!SharedData.load_reservarion_time(User_Buy_Record_Fake.this).equals("")){
            Date_n_Time =  SharedData.load_reservarion_time(User_Buy_Record_Fake.this);
        }else{
            Date_n_Time =  SharedData.load_user_order_time(User_Buy_Record_Fake.this);
        }

        new sendData().execute();


        SharedData.delete_address2(User_Buy_Record_Fake.this);
        SharedData.delete_user_reservarion_info(User_Buy_Record_Fake.this);
        SharedData.delete_user_order_time(User_Buy_Record_Fake.this);
        SharedData.delete_user_product_desc(User_Buy_Record_Fake.this);
        SharedData.RES_N_P("", "", User_Buy_Record_Fake.this);
        SharedData.RES_B(false,User_Buy_Record_Fake.this);



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(User_Buy_Record_Fake.this, ActivityMainMenu.class));
        dbhelper.close();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);

        finish();
    }


    // asynctask class to get data from database in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            getDataFromDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // hide progressbar and show reservation form
//            prgLoading.setVisibility(View.GONE);
//            sclDetail.setVisibility(View.VISIBLE);

        }
    }

    // asynctask class to send data to server in background
    public class sendData extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        // show progress dialog
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog= ProgressDialog.show(User_Buy_Record_Fake.this, "",
                    getString(R.string.sending_alert), true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // send data to server and store result to variable
            Result = getRequest(Name, Alamat, Kota, Provinsi, Email, Name2, Date_n_Time, Phone, OrderList, Comment);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // if finish, dismis progress dialog and show toast message
            dialog.dismiss();
            resultAlert(Result);

        }
    }

    // method to show toast message
    public void resultAlert(String HasilProses){
        Log.e("result",HasilProses);
        if(HasilProses.trim().equalsIgnoreCase("OK")){

            Toast.makeText(User_Buy_Record_Fake.this, R.string.ok_alert, Toast.LENGTH_SHORT).show();

        }else if(HasilProses.trim().equalsIgnoreCase("Failed")){
            Toast.makeText(User_Buy_Record_Fake.this, R.string.failed_alert, Toast.LENGTH_SHORT).show();
        }else{
            Log.d("HasilProses", HasilProses);
        }
    }

    // method to post data to server
    public String getRequest(String name, String alamat, String kota, String provinsi, String email, String name2, String date_n_time, String phone, String orderlist, String comment){
        String result = "";

        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(Constant.SendDataAPI);

        try{
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("alamat", alamat));
            nameValuePairs.add(new BasicNameValuePair("kota", kota));
            nameValuePairs.add(new BasicNameValuePair("provinsi", provinsi));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("name2", name2));
            nameValuePairs.add(new BasicNameValuePair("date_n_time", date_n_time));
            nameValuePairs.add(new BasicNameValuePair("phone", phone));
            nameValuePairs.add(new BasicNameValuePair("order_list", orderlist));
            nameValuePairs.add(new BasicNameValuePair("comment", comment));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            result = request(response);
        }catch(Exception ex){
            result = "Unable to connect.";
        }
        return result;
    }

    public static String request(HttpResponse response){
        String result = "";
        try{
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        }catch(Exception ex){
            result = "Error";
        }
        return result;
    }

    // method to get data from database
    public void getDataFromDatabase(){

        data = dbhelper.getAllData();

        double Order_price = 0;
        double Total_price = 0;
        double tax = 0;

        // store all data to variables
        for(int i=0;i<data.size();i++){
            ArrayList<Object> row = data.get(i);
            String Menu_name = row.get(1).toString();
            String Quantity = row.get(2).toString();
            double Sub_total_price = Double.parseDouble(formatData.format(Double.parseDouble(row.get(3).toString())));
            Order_price += Sub_total_price;

            // calculate order price
            OrderList += (Quantity+" عدد "+Menu_name+" "+(int)Sub_total_price+" "+" تومان"+",\n");
        }

        if(OrderList.equalsIgnoreCase("")){
            OrderList += getString(R.string.no_order_menu);
        }

        tax = Double.parseDouble(formatData.format(Order_price * (Tax / 100)));
        Total_price = Double.parseDouble(formatData.format(Order_price - tax));
        OrderList += "\nمبلغ پرداختی: "+(int)Order_price+" "+" تومان";
//    			+"\nTax: "+Tax+"%: "+tax+" "+Currency+
//    			"\nTotal: "+Total_price+" "+Currency;`
        //edtOrderList.setText(OrderList);
        Log.e("get data from database", OrderList);
    }

    // when back button pressed close database and back to previous page

        
}
