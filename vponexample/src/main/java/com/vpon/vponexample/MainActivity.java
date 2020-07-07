package com.vpon.vponexample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.vpon.ads.VponAdListener;
import com.vpon.ads.VponAdRequest;
import com.vpon.ads.VponAdSize;
import com.vpon.ads.VponBanner;
import com.vpon.ads.VponInterstitialAd;
import com.vpon.ads.VponMediaView;
import com.vpon.ads.VponMobileAds;
import com.vpon.ads.VponNativeAd;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    private VponBanner adView;
    private VponInterstitialAd interstitial;
    private VponNativeAd nativeAd;
    private String MY_BANNER_UNIT_ID = "8a80854b6a90b5bc016ad81c2a136532";//TODO SET YOUR AD_UNIT_ID here
    private String MY_INTERSTITIAL_UNIT_ID = "8a80854b6a90b5bc016ad81c64786533";////TODO SET YOUR AD_UNIT_ID here
    private String MY_NATIVE_UNIT_ID = "8a80854b6a90b5bc016ad81ca1336534";////TODO SET YOUR AD_UNIT_ID here
    private LinearLayout adLayout;
    private ConstraintLayout nativeAdContainer;

    // In this sample, we will show you the banner and IS ads
    // by onclicks. Don't forget to import the SDK!
    private Button bannerButton;
    private Button interstitialButton;
    private Button nativeAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bannerButton = findViewById(R.id.request_banner_button);
        bannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBannerAd();
            }
        });

        interstitialButton = findViewById(R.id.request_is_button);
        interstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doInterstitialAd();
            }
        });

        nativeAdButton = findViewById(R.id.request_native_button);
        nativeAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNativeAd();
            }
        });

        // Create an ad.

        adView = new VponBanner(this, MY_BANNER_UNIT_ID, VponAdSize.BANNER);

        interstitial = new VponInterstitialAd(this, MY_INTERSTITIAL_UNIT_ID);

        nativeAd = new VponNativeAd(this, MY_NATIVE_UNIT_ID);
        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        adLayout = findViewById(R.id.container);
        adLayout.addView(adView);
        // start loading the ad in the background


        // Inflate your custom ad layout for native ad
        LayoutInflater.from(this).inflate(R.layout.layout_native_ad_template, adLayout, true);
        nativeAdContainer = findViewById(R.id.native_ad_container);
    }

    private void doBannerAd(){
        VponAdRequest adRequest = new VponAdRequest.Builder()
                .build();
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
        adView.loadAd(buildAdRequest());
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

    private void doNativeAd() {
        nativeAd.withNativeAdLoadedListener(onNativeAdLoadedListener);
        nativeAd.loadAd(buildAdRequest());
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.resume();
        }

        if (nativeAd != null){
            nativeAd.resume();
        }

        super.onPause();
    }


    @Override
    protected void onResume() {
        if (adView != null) {
            adView.resume();
        }


        if (nativeAd != null){
            nativeAd.resume();
        }
        super.onResume();
    }

    /** Called before the activity is destroyed. */
    @Override
    public void onDestroy() {
        // Destroy the AdView.
        if (adView != null) {
            adView.destroy();
            adView = null;
        }

        if (nativeAd != null){
            nativeAd.destroy();
            nativeAd = null;
        }

        super.onDestroy();
    }

    private VponAdRequest buildAdRequest(){
        VponAdRequest.Builder builder = new VponAdRequest.Builder();

        //this only works for banner
        builder.setAutoRefresh(true);

        //optional
        HashMap<String, Object> contentData = new HashMap<>();
        contentData.put("key1", "Vpon");
        contentData.put("key2", 1.2);
        contentData.put("key3", true);

        builder.setContentData(contentData);
        builder.setContentUrl("https://www.vpon.com/zh-hant/");
        //

        return builder.build();
    }

    private ImageView nativeAdIcon = null;
    private TextView nativeAdTitle = null;
    private TextView nativeAdBody = null;

    private VponMediaView nativeAdMedia = null;

    private TextView nativeAdSocialContext = null;
    private Button nativeAdCallToAction = null;
    private RatingBar nativeAdStarRating = null;

    private VponNativeAd.OnNativeAdLoadedListener onNativeAdLoadedListener =
            new VponNativeAd.OnNativeAdLoadedListener() {
                @Override
                public void onNativeAdLoaded(VponNativeAd.NativeAdData localNativeAdData) {
                    // Set ad datas to your custom ad layout
                    setNativeAdData(localNativeAdData, nativeAdContainer);
                }
            };

    private void setNativeAdData(VponNativeAd.NativeAdData adData, View adContainer) {
        ImageView nativeAdIcon = adContainer.findViewById(R.id.ad_app_icon);
        TextView nativeAdTitle = adContainer.findViewById(R.id.ad_headline);
        TextView nativeAdBody = adContainer.findViewById(R.id.ad_body);
        VponMediaView nativeMediaView = adContainer.findViewById(R.id.ad_media_view);
        Button nativeAdCallToAction = adContainer.findViewById(R.id.ad_call_to_action);
        RatingBar nativeAdStarRating = adContainer.findViewById(R.id.ad_stars);

        // Use VponNativeAd.downloadAndDisplayImage to display icon in your custom ad layout
        VponNativeAd.downloadAndDisplayImage(adData.getIcon(), nativeAdIcon);

        nativeAdTitle.setText(adData.getTitle());
        if (adData.getBody() != null) {
            nativeAdBody.setText(adData.getBody());
        } else {
            nativeAdBody.setVisibility(View.INVISIBLE);
        }

        nativeMediaView.setNativeAd(nativeAd ,adData);

        if (adData.getCallToAction() != null) {
            nativeAdCallToAction.setText(adData.getCallToAction());
        } else {
            nativeAdCallToAction.setVisibility(View.INVISIBLE);
        }

        VponNativeAd.NativeAdData.Rating rating = adData.getRating();
        if (rating != null) {
            nativeAdStarRating.setNumStars((int) rating.getScale());
            nativeAdStarRating.setRating((float) rating.getValue());
        } else {
            nativeAdStarRating.setVisibility(View.INVISIBLE);
        }

        adContainer.setVisibility(View.VISIBLE);
        // Register your view for click interaction
        nativeAd.registerViewForInteraction(adContainer);
    }
}
