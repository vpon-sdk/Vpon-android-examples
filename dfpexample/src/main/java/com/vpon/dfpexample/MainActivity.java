package com.vpon.dfpexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

public class MainActivity extends AppCompatActivity {

    private PublisherAdView adView;
    private PublisherInterstitialAd interstitial;
    private String MY_AD_UNIT_ID = "/6499/example/banner";//TODO SET YOUR AD_UNIT_ID here
    private String MY_INTERSTITIAL_UNIT_ID = "/6499/example/interstitial";////TODO SET YOUR AD_UNIT_ID here
    private LinearLayout adBannerLayout;


    // In this sample, we will show you the banner and IS ads
    // by onclicks. Don't forget to import the JARs!
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        adView = new PublisherAdView(this);
        adView.setAdSizes(AdSize.BANNER);
        adView.setAdUnitId(MY_AD_UNIT_ID);

        interstitial = new PublisherInterstitialAd(this);
        interstitial.setAdUnitId(MY_INTERSTITIAL_UNIT_ID);


        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        adBannerLayout = findViewById(R.id.container);
        adBannerLayout.addView(adView);
        // start loading the ad in the background

    }

    private void doBannerAd(){
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .setManualImpressionsEnabled(true)
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
        PublisherAdRequest request = new PublisherAdRequest.Builder()
                .setManualImpressionsEnabled(true)
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
