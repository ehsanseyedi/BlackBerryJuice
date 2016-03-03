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
