package com.example.wheatherforecast;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String PREF_NAME = "weather_prefs";
    private static final String KEY_CITY = "city";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Save city to SharedPreferences
    public void saveCity(String city) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CITY, city);
        editor.apply();
    }

    // Retrieve city from SharedPreferences
    public String getCity() {
        return sharedPreferences.getString(KEY_CITY, null);
    }
}
