package com.bconnect.b_connect;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class SharedPreferencesSR {

    public static void setSP(Context mContext, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(key);
        editor.putString(key, value).apply();
        editor.apply();

    }



    public static void setArraySP(Context mContext, String key, ArrayList<String> arrayList) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(key+"_size", arrayList.size());

        for(int i=0;i<arrayList.size();i++)
        {
            editor.remove(key+ i);
            editor.putString(key + i, arrayList.get(i));
        }
        editor.apply();


    }


    public static String getSP(Context context, String keyValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(keyValue, "");
    }

    public static int getSPINT(Context context, String keyValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(keyValue, 0);
    }



    public static void removeAllSP(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
    public static void removeONE(Context mContext, String keyValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(keyValue).apply();
    }

}