package com.BlackBerryJuice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedData {

    public static void save_last_userinfo (String name,String birthday,String address , String phone , String insta,Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", name);
        editor.putString("birthday", birthday);
        editor.putString("address", address);
        editor.putString("phone", phone);
        editor.putString("insta", insta);
        editor.commit();
    }

    public static void save_last_userinfo_cm (String code,String mobile,Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("code", code);
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public static void save_address2 (String address,Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo2", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("address2", address);
        editor.commit();
    }

    public static void delete_address2 (Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo2", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static void delete_all_userinfo (Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static String load_code(Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        return sp.getString("code", "");
    }
    public static String load_name(Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        return sp.getString("name", "");
    }
    public static String load_mobile(Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        return sp.getString("mobile", "");
    }
    public static String load_birthday(Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        return sp.getString("birthday", "");
    }
    public static String load_address(Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        return sp.getString("address", "");
    }
    public static String load_address2(Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo2", Activity.MODE_PRIVATE);
        return sp.getString("address2", "");
    }
    public static String load_phone(Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        return sp.getString("phone", "");
    }
    public static String load_insta(Context c) {
        SharedPreferences sp = c.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        return sp.getString("insta", "");
    }



    public static void save_user_reservarion_info (String time,String desc,Context c) {
        SharedPreferences sp = c.getSharedPreferences("reservarion_sp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("reservarion_time", time);
        editor.putString("reservarion_desc", desc);
        editor.commit();
    }

    public static void delete_user_reservarion_info (Context c) {
        SharedPreferences sp = c.getSharedPreferences("reservarion_sp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static String load_reservarion_time(Context c) {
        SharedPreferences sp = c.getSharedPreferences("reservarion_sp", Activity.MODE_PRIVATE);
        return sp.getString("reservarion_time", "");
    }

    public static String load_reservarion_desc(Context c) {
        SharedPreferences sp = c.getSharedPreferences("reservarion_sp", Activity.MODE_PRIVATE);
        return sp.getString("reservarion_desc", "");
    }
//--------------------------------------------------------------------------------------------------
    public static void save_user_product_desc (String time,Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_product_desc_sp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_product_desc", time);
        editor.commit();
    }

    public static void delete_user_product_desc(Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_product_desc_sp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static String load_user_product_desc(Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_product_desc_sp", Activity.MODE_PRIVATE);
        return sp.getString("user_product_desc", "");
    }
//--------------------------------------------------------------------------------------------------
    public static void save_user_order_time (String time,Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_order_time_sp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_order_time", time);
        editor.commit();
    }

    public static void delete_user_order_time (Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_order_time_sp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static String load_user_order_time (Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_order_time_sp", Activity.MODE_PRIVATE);
        return sp.getString("user_order_time", "");
    }
//--------------------------------------------------------------------------------------------------
    public static void save_user_special_message (String message,Context c) {
        SharedPreferences sp = c.getSharedPreferences("usermes", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("message", message);
        editor.commit();
    }

    public static void delete_user_special_message (Context c) {
        SharedPreferences sp = c.getSharedPreferences("usermes", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static String load_user_special_message(Context c) {
        SharedPreferences sp = c.getSharedPreferences("usermes", Activity.MODE_PRIVATE);
        return sp.getString("message", "");
    }







    public static void set_user_logedin(Boolean dolog,Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_logedin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("log", dolog);
        editor.commit();
    }

    public static Boolean do_user_logedin(Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_logedin", Activity.MODE_PRIVATE);
        return sp.getBoolean("log", false);
    }


    public static void RES_B(Boolean dolog,Context c) {
        SharedPreferences sp = c.getSharedPreferences("RES_B", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("RES_B", dolog);
        editor.commit();
    }

    public static Boolean get_RES_B(Context c) {
        SharedPreferences sp = c.getSharedPreferences("RES_B", Activity.MODE_PRIVATE);
        return sp.getBoolean("RES_B", false);
    }

    public static void RES_N_P (String name,String price,Context c) {
        SharedPreferences sp = c.getSharedPreferences("RES_N_P", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("RES_N", name);
        editor.putString("RES_P", price);
        editor.commit();
    }
    public static String get_RES_N(Context c) {
        SharedPreferences sp = c.getSharedPreferences("RES_N_P", Activity.MODE_PRIVATE);
        return sp.getString("RES_N", "");
    }

    public static String get_RES_P(Context c) {
        SharedPreferences sp = c.getSharedPreferences("RES_N_P", Activity.MODE_PRIVATE);
        return sp.getString("RES_P", "");
    }





    public static void set_user_registered(Boolean doreg,Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_registered", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("reg", doreg);
        editor.commit();
    }

    public static Boolean do_user_registered(Context c) {
        SharedPreferences sp = c.getSharedPreferences("user_registered", Activity.MODE_PRIVATE);
        return sp.getBoolean("reg", false);
    }









}
