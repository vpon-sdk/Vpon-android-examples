package com.vpon.admobexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.vpadn.mediation.VpadnAdapter;

import java.util.HashMap;

import static com.vpadn.mediation.VpadnAdapter.AD_CONTENT_DATA;
import static com.vpadn.mediation.VpadnAdapter.AD_CONTENT_URL;

public class MainActivity extends AppCompatActivity {

    private AdView adView;
    private InterstitialAd interstitial;
    private AdLoader adLoader;
    private UnifiedNativeAdView unifiedNativeAdView;
    private String MY_BANNER_UNIT_ID = "";//TODO SET YOUR AD_UNIT_ID here
    private String MY_INTERSTITIAL_UNIT_ID = "";////TODO SET YOUR AD_UNIT_ID here
    private String MY_NATIVE_UNIT_ID = "";////TODO SET YOUR AD_UNIT_ID here
    private LinearLayout adLayout;


    // In this sample, we will show you the banner and IS ads
    // by onclicks. Don't forget to import the SDK!
    private Button BannerButton;
    private Button InterstitialButton;
    private Button NativeAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // button1 for banner and button for IS
        BannerButton = findViewById(R.id.banner_button);
        BannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBannerAd();
            }
        });

        InterstitialButton = findViewById(R.id.is_button);
        InterstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doInterstitialAd();
            }
        });

        NativeAdButton = findViewById(R.id.native_ad_button);
        NativeAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNativeAd();
            }
        });

        // Create an ad.

        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(MY_BANNER_UNIT_ID);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(MY_INTERSTITIAL_UNIT_ID);


        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        adLayout = findViewById(R.id.container);
        adLayout.addView(adView);
        // start loading the ad in the background

        unifiedNativeAdView = (UnifiedNativeAdView) LayoutInflater.from(this).inflate(R.layout.layout_native_ad_template1, null);
        adLayout.addView(unifiedNativeAdView);
    }

    private void doBannerAd() {
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
        adView.loadAd(buildAdRequest(false));
    }

    private void doInterstitialAd() {
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                interstitial.show();
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
        interstitial.loadAd(buildAdRequest(false));
    }

    private void doNativeAd() {
        adLoader = new AdLoader.Builder(this, MY_NATIVE_UNIT_ID)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        populateUnifiedNativeAdView(unifiedNativeAd, unifiedNativeAdView);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                        Log.d("main", "fail to load " + errorCode);
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(buildAdRequest(true));
    }

    /**
     * Called before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        // Destroy the AdView.
        if (adView != null) {
            adView.destroy();
            adView = null;
        }
        super.onDestroy();
    }

    private AdRequest buildAdRequest(boolean isNativeAd){
        AdRequest.Builder builder = new AdRequest.Builder();

        //optional
        Bundle bundle = new Bundle();
        HashMap<String, Object> contentData = new HashMap<>();
        contentData.put("key1", "Vpon");
        contentData.put("key2", 1.2);
        contentData.put("key3", true);
        bundle.putSerializable(AD_CONTENT_DATA, contentData);
        bundle.putSerializable(AD_CONTENT_URL, "https://www.vpon.com/zh-hant/");
        if(isNativeAd) {
            builder.addCustomEventExtrasBundle(VpadnAdapter.class, bundle);
        }else {
            builder.addNetworkExtrasBundle(VpadnAdapter.class, bundle);
        }
        //

        return builder.build();
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        adView.setVisibility(View.VISIBLE);
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setPriceView(adView.findViewById(R.id.ad_price));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.GONE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);
    }
}
