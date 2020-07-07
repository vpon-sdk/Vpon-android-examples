package com.vpon.admobexample;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.vpon.ads.VponMobileAds;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VponMobileAds.initialize(getBaseContext());
        MobileAds.initialize(getBaseContext());
    }

}
