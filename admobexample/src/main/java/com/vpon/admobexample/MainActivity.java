package com.vpon.admobexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private AdView adView;
    private InterstitialAd interstitial;
    private String MY_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";//TODO SET YOUR AD_UNIT_ID here
    private String MY_INTERSTITIAL_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";////TODO SET YOUR AD_UNIT_ID here
    private LinearLayout adBannerLayout;


    // In this sample, we will show you the banner and IS ads
    // by onclicks. Don't forget to import the SDK!
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this);

        // button1 for banner and button for IS
        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBannerAd();
            }
        });

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doInterstitialAd();
            }
        });

        // Create an ad.

        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(MY_AD_UNIT_ID);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(MY_INTERSTITIAL_UNIT_ID);


        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        adBannerLayout = findViewById(R.id.container);
        adBannerLayout.addView(adView);
        // start loading the ad in the background
    }

    private void doBannerAd(){
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdLeftApplication() {
            }
        });

    }

    private void doInterstitialAd() {
        AdRequest request = new AdRequest.Builder()
                .build();
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded(){
                interstitial.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode){}

            @Override
            public void onAdOpened(){}

            @Override
            public void onAdClosed(){}

            @Override
            public void onAdLeftApplication(){}
        });
        interstitial.loadAd(request);
    }

    /** Called before the activity is destroyed. */
    @Override
    public void onDestroy() {
        // Destroy the AdView.
        if (adView != null) {
            adView.destroy();
            adView = null;
        }
        super.onDestroy();
    }
}
