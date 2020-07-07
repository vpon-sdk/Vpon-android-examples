package com.example.mopubexample;

import android.app.Application;

import com.vpon.ads.VponMobileAds;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VponMobileAds.initialize(getBaseContext());
    }
}
