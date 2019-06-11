package com.example.arsam.cloudcomputing;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by arsam on 29/03/2018.
 */


public class SharedPref extends Application {

    private Context context;

    /**
     * Call the constructor to initialize the context
     *
     * @param context - current context of your activity
     */
    public SharedPref(Context context) {
        this.context = context;
    }

    /**
     * Store any string value through this function
     *
     * @param key:   set the associate key for each value, Key is use to retrieve the saved value in SharedPreference.
     * @param value: the value you want to store against the key.
     */

    public void putPref(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Retrieve the stored value
     *
     * @param key:          use the key to retrieve your stored value.
     * @param defaultValue: you can specify the default value in case there is no any value stored associate to entered key
     * @return String the stored value
     */
    public String getPref(String key, String defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defaultValue);
    }


    /**
     * Store any boolean value through this function
     *
     * @param key:   set the associate key for each value, Key is use to retrieve the saved value in SharedPreference.
     * @param value: the value you want to store against the key.
     */
    public void putPrefBool(String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Retrieve the stored value
     *
     * @param key:          use the key to retrieve your stored value.
     * @param defaultValue: you can specify the default value in case there is no any value stored associate to entered key
     * @return boolean the stored value
     */
    public boolean getPrefBool(String key, boolean defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defaultValue);

    }

    /**
     * Store any Arraylist<String> value
     *
     * @param values:  the values you need to store in  ArrayList
     * @param sizeKey: the key to store the size of the user entered array list. Will be useful in retrieve
     * @param key:     the key for stored array list
     */
    public void putArray(ArrayList<String> values, String sizeKey, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(sizeKey, values.size());

        for (int i = 0; i < values.size(); i++) {
            editor.putString(key + i, values.get(i));
            editor.commit();
        }


    }

    /**
     * get the stored Array list
     *
     * @param sizeKey      the key to get the size of dynamic array.
     * @param arraykey:    use the key to retrieve your stored values.
     * @param defaultValue if there will be no value for a particular array list you can define any default value
     * @return Arraylist<String>
     */

    public String getArray(String sizeKey, String arraykey, String defaultValue) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int size = preferences.getInt(sizeKey, 0);
        String array[] = new String[size];

        for (int i = 0; i < size; i++) {

            array[i] = preferences.getString(arraykey + i, defaultValue);

        }

        return array[size];
    }

    /**
     * Store any int value through this function
     *
     * @param key:   set the associate key for each value, Key is use to retrieve the saved value in SharedPreference.
     * @param value: the value you want to store against the key.
     */

    public void putIntPref(String key, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Retrieve the stored value
     *
     * @param key:          use the key to retrieve your stored value.
     * @param defaultValue: you can specify the default value in case there is no any value stored associate to entered key
     * @return int the stored value
     */

    public int getIntPref(String key, int defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, defaultValue);
    }

    /**
     * Store any long value through this function
     *
     * @param key:   set the associate key for each value, Key is use to retrieve the saved value in SharedPreference.
     * @param value: the value you want to store against the key.
     */

    public void putLongPref(String key, long value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Retrieve the stored value
     *
     * @param key:          use the key to retrieve your stored value.
     * @param defaultValue: you can specify the default value in case there is no any value stored associate to entered key
     * @return long the stored value
     */
    public long getLongPref(String key, long defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(key, defaultValue);
    }

}
