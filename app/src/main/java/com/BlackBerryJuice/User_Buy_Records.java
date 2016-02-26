package com.BlackBerryJuice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class User_Buy_Records extends Activity {
    // declate dbhelper and adapter objects
    DBHelper dbhelper;
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

    }


    public static void save_purchease(int rahgiri ,Context c) {
        SharedPreferences sp = c.getSharedPreferences("BUY", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("BUY", rahgiri);
        editor.commit();
    }

    public static int load_purchease(Context c) {
        SharedPreferences sp = c.getSharedPreferences("BUY", Activity.MODE_PRIVATE);
        return sp.getInt("BUY", 0);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbhelper.close();
        finish();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }
}
