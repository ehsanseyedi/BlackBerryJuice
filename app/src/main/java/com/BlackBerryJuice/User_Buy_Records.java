package com.BlackBerryJuice;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class User_Buy_Records extends ListActivity {
    // declate dbhelper and adapter objects

    DBHelper dbhelper;
    static ArrayList<String> nameslist = new ArrayList<String>();
    static ArrayList<String> priceslist = new ArrayList<String>();
    static ArrayList<String> rahgirilist = new ArrayList<String>();

    public ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList<ArrayList<Object>> data;
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
        setContentView(R.layout.activity_user_buy_records);

        //test
        nameslist.add("سان شان 2 عدد  \nهویج بستنی 4 عدد ");
        nameslist.add("سان شان 10 عدد  \nهویج بستنی 3 عدد ");
        nameslist.add("سان شان 15 عدد  \nهویج بستنی 6 عدد ");
        priceslist.add("10000");
        priceslist.add("15000");
        priceslist.add("20000");
        rahgirilist.add("123245452");
        rahgirilist.add("123245999");
        rahgirilist.add("123245444");
        Log.e("saeed", nameslist.size() + "");
        //loadlists

        Collections.reverse(nameslist);
        Collections.reverse(priceslist);
        Collections.reverse(rahgirilist);

        String[] stockArr = new String[priceslist.size()];
        stockArr = priceslist.toArray(stockArr);

        adapter = new MyAdapter(this, android.R.layout.simple_list_item_1,R.id.names,stockArr);
        setListAdapter(adapter);
        lv = getListView();
//        Bundle b = getIntent().getExtras();
//        int totalprice = b.getInt("price");
//        String rahgir = b.getString("rahgir");





        dbhelper = new DBHelper(this);
        // open database
        try{
            dbhelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

        //clearData();

        data = dbhelper.getAllData();
        String allnames = "";
        // store data to arraylist variables

        for(int i=0;i<data.size();i++) {
            ArrayList<Object> row = data.get(i);
            allnames += row.get(1).toString() + " " + Integer.parseInt(row.get(2).toString()) +" عدد ";
            if(i==data.size()-1){

            }else{
                allnames += " \n ";
            }
        }
        Log.e("saeed", allnames);
//        if(!rahgir.equals("null")) {
//            nameslist.add(allnames);
//            priceslist.add(String.valueOf(totalprice));
//            rahgirilist.add(rahgir);
//        }else{
//            //namovafagh
//        }

        //save lists


    }

    public static void save_purchease(Set<String> names,Set<String> totalprice,Set<String> rahgiri,Context c) {
        SharedPreferences sp = c.getSharedPreferences("records", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet("name", names);
        editor.putStringSet("price", totalprice);
        editor.putStringSet("rahgiri", rahgiri);
        editor.commit();
    }

    public static Set<String> load_names_purchease(Context c) {
        SharedPreferences sp = c.getSharedPreferences("records", Activity.MODE_PRIVATE);
        return sp.getStringSet("name",null);
    }

    public static Set<String> load_prices_purchease(Context c) {
        SharedPreferences sp = c.getSharedPreferences("records", Activity.MODE_PRIVATE);
        return sp.getStringSet("price",null);
    }

    public static Set<String> load_rahgiris_purchease(Context c) {
        SharedPreferences sp = c.getSharedPreferences("records", Activity.MODE_PRIVATE);
        return sp.getStringSet("rahgiri",null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbhelper.close();
        finish();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }

    void clearData(){
        nameslist.clear();
        priceslist.clear();
        rahgirilist.clear();
    }







    private class MyAdapter extends ArrayAdapter<String>{

        public MyAdapter(Context context, int resource, int textViewResourceId,
                         String[] strings) {
            super(context, resource, textViewResourceId, strings);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.list_item, parent, false);

            //Log.e("saeed",nameslist.size()+" pos: " +position);
            if(position<nameslist.size()) {

                TextView names = (TextView) row.findViewById(R.id.names);
                TextView price = (TextView) row.findViewById(R.id.price);
                TextView rahgir = (TextView) row.findViewById(R.id.rahgir);

                names.setText(nameslist.get(position));
                //price.setText(priceslist.get(position));
                price.setText(NumberFormat.getNumberInstance(Locale.US).format((Integer.valueOf(priceslist.get(position)))));
                rahgir.setText(rahgirilist.get(position));
            }

            return row;
        }

    }

}
