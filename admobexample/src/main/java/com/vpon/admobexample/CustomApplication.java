package com.vpon.admobexample;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.vpon.ads.VponMobileAds;

public class CustomApplication extends Application {

    private static final String LT = "CustomApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        VponMobileAds.initialize(getBaseContext());
        MobileAds.initialize(this, initializationStatus ->
                Log.e(LT, "onInitializationComplete(" + initializationStatus + ")"));
    }

}
