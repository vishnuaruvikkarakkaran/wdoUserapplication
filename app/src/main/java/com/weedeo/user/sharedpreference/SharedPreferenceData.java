package com.weedeo.user.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.weedeo.user.Utils.Constants;

/**
 * Created By Athul on 14-10-2019.
 */
public class SharedPreferenceData {

    private Context context;

    public SharedPreferenceData(Context mContext){
        this.context = mContext;
    }

    public final static String PREFS_NAME = Constants.PREFS_NAME;

    public void setInt( String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(key, 0);
    }

    public void setString(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key,"");
    }

    public void setBool(String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBool(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(key,false);
    }

    public void deleteAllPreference(){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().clear().apply();
    }

}
