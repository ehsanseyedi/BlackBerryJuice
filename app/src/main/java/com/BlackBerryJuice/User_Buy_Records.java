package com.BlackBerryJuice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Set;

public class User_Buy_Records extends Activity {
    // declate dbhelper and adapter objects
    DBHelper dbhelper;
    static ArrayList<String> nameslist = new ArrayList<String>();
    static ArrayList<String> priceslist = new ArrayList<String>();
    static ArrayList<String> rahgirilist = new ArrayList<String>();
    ArrayList<ArrayList<Object>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.tameshk_dark));
        }
        setContentView(R.layout.activity_user_buy_records);

        dbhelper = new DBHelper(this);
        // open database
        try{
            dbhelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

        clearData();

        data = dbhelper.getAllData();
        String allnames = "";
        // store data to arraylist variables
        for(int i=0;i<data.size();i++) {
            ArrayList<Object> row = data.get(i);
            allnames += row.get(1).toString() + " " + Integer.parseInt(row.get(2).toString()) +" عدد ";
            if(i==data.size()-1){

            }else{
                allnames += " | ";
            }
        }
        nameslist.add(allnames);
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
}
