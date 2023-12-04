package com.vpon.vponexample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.vpon.ads.VponAdListener;
import com.vpon.ads.VponAdLoader;
import com.vpon.ads.VponAdRequest;
import com.vpon.ads.VponAdSize;
import com.vpon.ads.VponBanner;
import com.vpon.ads.VponFullScreenContentCallback;
import com.vpon.ads.VponInterstitialAd;
import com.vpon.ads.VponInterstitialAdLoadCallback;
import com.vpon.ads.VponMediaView;
import com.vpon.ads.VponNativeAd;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private VponBanner adView;
    private VponInterstitialAd interstitial;
    private VponNativeAd nativeAd;
    private VponAdLoader vponAdLoader;
    private String MY_BANNER_UNIT_ID = "8a80854b6a90b5bc016ad81c2a136532";//TODO SET YOUR AD_UNIT_ID here
    private String MY_INTERSTITIAL_UNIT_ID = "8a80854b6a90b5bc016ad81c64786533";////TODO SET YOUR AD_UNIT_ID here
    private String MY_NATIVE_UNIT_ID = "8a80854b6a90b5bc016ad81ca1336534";////TODO SET YOUR AD_UNIT_ID here
    private ConstraintLayout nativeAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // In this sample, we will show you the banner and IS ads
        // by onclicks. Don't forget to import the SDK!
        Button bannerButton = findViewById(R.id.request_banner_button);
        bannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBannerAd();
            }
        });

        Button interstitialButton = findViewById(R.id.request_is_button);
        interstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doInterstitialAd();
            }
        });

        Button nativeAdButton = findViewById(R.id.request_native_button);
        nativeAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNativeAd();
            }
        });

        // Create an ad.

        adView = new VponBanner(this, MY_BANNER_UNIT_ID, VponAdSize.BANNER);


        vponAdLoader = new VponAdLoader.Builder(this, MY_NATIVE_UNIT_ID)
                .forNativeAd(new VponNativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@Nullable VponNativeAd vponNativeAd) {
                        nativeAd = vponNativeAd;
                        // Set ad datas to your custom ad layout
                        setNativeAdData(vponNativeAd, nativeAdContainer);
                    }
                }).build();


        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        LinearLayout adLayout = findViewById(R.id.container);
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
        VponInterstitialAd.load(this, MY_INTERSTITIAL_UNIT_ID, buildAdRequest(),
                new VponInterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull VponAdRequest.VponErrorCode vponErrorCode) {

                    }

                    @Override
                    public void onAdLoaded(VponInterstitialAd vponInterstitialAd) {
                        interstitial = vponInterstitialAd;
                        interstitial.setFullScreenContentCallback(new VponFullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(int i) {
                                super.onAdFailedToShowFullScreenContent(i);
                                interstitial = null;
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                interstitial = null;
                            }
                        });
                        interstitial.show();
                    }
                });
    }

    private void doNativeAd() {
        if(vponAdLoader != null){
            vponAdLoader.loadAd(buildAdRequest());
        }
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.resume();
        }
        super.onPause();
    }


    @Override
    protected void onResume() {
        if (adView != null) {
            adView.resume();
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


    private void setNativeAdData(VponNativeAd vponNativeAd, View adContainer) {
        ImageView nativeAdIcon = adContainer.findViewById(R.id.ad_app_icon);
        TextView nativeAdTitle = adContainer.findViewById(R.id.ad_headline);
        TextView nativeAdBody = adContainer.findViewById(R.id.ad_body);
        VponMediaView nativeMediaView = adContainer.findViewById(R.id.ad_media_view);
        Button nativeAdCallToAction = adContainer.findViewById(R.id.ad_call_to_action);
        RatingBar nativeAdStarRating = adContainer.findViewById(R.id.ad_stars);

        // Use VponAdLoader.downloadAndDisplayImage to display icon in your custom ad layout
        VponAdLoader.downloadAndDisplayImage(vponNativeAd.getIcon(), nativeAdIcon);

        nativeAdTitle.setText(vponNativeAd.getTitle());
        if (vponNativeAd.getBody() != null) {
            nativeAdBody.setText(vponNativeAd.getBody());
        } else {
            nativeAdBody.setVisibility(View.INVISIBLE);
        }

        nativeMediaView.setNativeAd(vponNativeAd);

        if (vponNativeAd.getCallToAction() != null) {
            nativeAdCallToAction.setText(vponNativeAd.getCallToAction());
        } else {
            nativeAdCallToAction.setVisibility(View.INVISIBLE);
        }

        VponNativeAd.Rating rating = vponNativeAd.getRating();
        if (rating != null) {
            nativeAdStarRating.setNumStars((int) rating.getScale());
            nativeAdStarRating.setRating((float) rating.getValue());
        } else {
            nativeAdStarRating.setVisibility(View.INVISIBLE);
        }

        adContainer.setVisibility(View.VISIBLE);

        // Register your view for click interaction
        // Need to be called "after" nativeMediaView.setNativeAd
        vponNativeAd.registerViewForInteraction(adContainer);
    }
}
