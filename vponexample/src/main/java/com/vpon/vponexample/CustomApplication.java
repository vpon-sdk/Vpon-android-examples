package com.vpon.vponexample;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.vpon.ads.VponMobileAds;

public class CustomApplication extends Application {

    private static final String PREF_NAME_ADID = "_vpon_advertisingId";

    @Override
    public void onCreate() {
        super.onCreate();
        VponMobileAds.initialize(getBaseContext());


        // cef23de9-9ed9-41dd-9cb2-e568c5053e4e
        SharedPreferences sharedPreferences = getBaseContext()
                .getSharedPreferences(PREF_NAME_ADID, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(PREF_NAME_ADID, "cef23de9-9ed9-41dd-9cb2-e568c5053e4e").apply();
    }
}
