package com.vpon.vponexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vpon.ads.VponAdListener;
import com.vpon.ads.VponAdRequest;
import com.vpon.ads.VponAdSize;
import com.vpon.ads.VponBanner;
import com.vpon.ads.VponInterstitialAd;
import com.vpon.ads.VponMobileAds;

public class MainActivity extends AppCompatActivity {

    private VponBanner adView;
    private VponInterstitialAd interstitial;
    private String MY_AD_UNIT_ID = "8a80854b6a90b5bc016ad81c2a136532";//TODO SET YOUR AD_UNIT_ID here
    private String MY_INTERSTITIAL_UNIT_ID = "8a80854b6a90b5bc016ad81c64786533";////TODO SET YOUR AD_UNIT_ID here
    private LinearLayout adBannerLayout;

    // In this sample, we will show you the banner and IS ads
    // by onclicks. Don't forget to import the SDK!
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VponMobileAds.initialize(this);

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

        adView = new VponBanner(this, MY_AD_UNIT_ID, VponAdSize.BANNER);

        interstitial = new VponInterstitialAd(this, MY_INTERSTITIAL_UNIT_ID);

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        adBannerLayout = findViewById(R.id.container);
        adBannerLayout.addView(adView);
        // start loading the ad in the background
    }

    private void doBannerAd(){
        VponAdRequest adRequest = new VponAdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new VponAdListener() {
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
        VponAdRequest request = new VponAdRequest.Builder()
                .build();
        interstitial.setAdListener(new VponAdListener() {
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
