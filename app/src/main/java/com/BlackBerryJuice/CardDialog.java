package com.BlackBerryJuice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CardDialog extends Activity {


    DBHelper dbhelper;
    TextView counter;
    TextView sub_price;
    TextView name;
//    String Menu_name;
//    double Menu_price;
//    int Menu_quantity;


    public static String Currency;
    Context c;
    static DecimalFormat formatData = new DecimalFormat("#.##");
    // declare arraylist variable to store data
    public ArrayList<ArrayList<Object>> data;
    public ArrayList<Integer> Menu_ID = new ArrayList<Integer>();
    public ArrayList<String> Menu_name = new ArrayList<String>();
    public ArrayList<Integer> Quantity = new ArrayList<Integer>();
    public ArrayList<Double> Sub_total_price = new ArrayList<Double>();
    public ArrayList<Integer> Single_price = new ArrayList<Integer>();

    int ID;
    int POS;



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
        setContentView(R.layout.activity_card_dialog);


        Intent iGet = getIntent();
        ID = iGet.getIntExtra("id", 0);
        POS = iGet.getIntExtra("pos", 0);

        Log.e("saeed", ID + "");

        counter = (TextView) findViewById(R.id.txtQuantity);
        sub_price = (TextView) findViewById(R.id.txtPrice);
        name = (TextView) findViewById(R.id.txtMenuName);

        Button plus = (Button) findViewById(R.id.plus);
        Button minez = (Button) findViewById(R.id.minez);
        LinearLayout done = (LinearLayout) findViewById(R.id.countdone);
        LinearLayout undone = (LinearLayout) findViewById(R.id.countcancel);
        //counter.setText("1");

        dbhelper = new DBHelper(this);

        // open database
        try{
            dbhelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }


        new getDataTask().execute();
        final Vibrator vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);



        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(counter.getText().toString());
                if (i <= 99) {
                    i++;
                }
                counter.setText(i + "");
                int sub = (Single_price.get(POS)*i);
                sub_price.setText(sub + " تومان");
            }
        });

        minez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(counter.getText().toString());
                if (i > 1) {
                    i--;
                } else {
                    vib.vibrate(300);
                }
                counter.setText(i + "");
                int sub = (Single_price.get(POS)*i);
                sub_price.setText(sub + " تومان");
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = 0;
                quantity = Integer.parseInt(counter.getText().toString());
                dbhelper.updateData(ID, quantity, (Single_price.get(POS) * quantity));
                Toast.makeText(CardDialog.this, "سفارش بروز شد", Toast.LENGTH_SHORT).show();
                ActivityCart.fa.finish();
                Intent d = new Intent(CardDialog.this,ActivityCart.class);
                dbhelper.close();
                startActivity(d);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        undone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent d = new Intent(CardDialog.this,ActivityCart.class);
                ActivityCart.fa.finish();
                dbhelper.deleteData(ID);
                clearData();
                dbhelper.close();
                startActivity(d);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });


    }


    public void clearData(){
        Menu_ID.clear();
        Menu_name.clear();
        Quantity.clear();
        Sub_total_price.clear();
    }


    public class getDataTask extends AsyncTask<Void, Void, Void> {

        // show progressbar first
        getDataTask(){
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            // get data from database
            getDataFromDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // show data
            //txtTotal.setText(NumberFormat.getNumberInstance(Locale.US).format((int)Total_price)+" "+Currency);
            //txtTotalLabel.setText(c.getString(R.string.total_order));
            //txtTotalLabel.setText(getString(R.string.total_order)+" (Tax "+Tax+"%)");
            // if data available show data on list
            // otherwise, show alert text

            counter.setText(Quantity.get(POS)+"");
            name.setText(Menu_name.get(POS));
            sub_price.setText(NumberFormat.getNumberInstance(Locale.US).format(Sub_total_price.get(POS))+" تومان");


//            Log.e("menu_ids:", Menu_ID.toString());
//            Log.e("menu_names:",Menu_name.toString());
//            Log.e("menu_quantitys:",Quantity.toString());
//            Log.e("menu_sub_total_prices:",Sub_total_price.toString());
//            Log.e("menu_single_prices:",Single_price.toString());

        }
    }

    public void getDataFromDatabase(){

        clearData();
        data = dbhelper.getAllData();

        // store data to arraylist variables
        for(int i=0;i<data.size();i++){
            ArrayList<Object> row = data.get(i);

            Menu_ID.add(Integer.parseInt(row.get(0).toString()));
            Menu_name.add(row.get(1).toString());
            Quantity.add(Integer.parseInt(row.get(2).toString()));
            Sub_total_price.add(Double.parseDouble(formatData.format(Double.parseDouble(row.get(3).toString()))));
            Single_price.add((int)(Sub_total_price.get(Sub_total_price.size()-1)/Quantity.get(Quantity.size()-1)));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbhelper.close();
        //ActivityCart.fa.finish();
        //startActivity(new Intent(CardDialog.this, ActivityCart.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();

    }
}
