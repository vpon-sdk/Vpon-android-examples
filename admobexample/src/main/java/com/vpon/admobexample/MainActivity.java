package com.vpon.admobexample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class MainActivity extends AppCompatActivity {

    private static final String LT = "AdMobSample";

    private InterstitialAd interstitial = null;
    private AdView adView = null;
    private NativeAdView nativeAdView;

    private static final String MY_AD_UNIT_ID = "ca-app-pub-7987617251221645/4356477328";//TODO SET YOUR AD_UNIT_ID here
    private static final String MY_INTERSTITIAL_UNIT_ID = "ca-app-pub-7987617251221645/1211439235";////TODO SET YOUR AD_UNIT_ID here
    private static final String MY_NATIVE_UNIT_ID = "ca-app-pub-7987617251221645/8706785875";////TODO SET YOUR AD_UNIT_ID here

    private Button button1;
    private Button button2;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // button1 for banner and button for IS
        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> doBannerAd());

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(view -> {
            if (interstitial != null) {
                interstitial.show(this);
            } else {
                doInterstitialAd();
            }
        });
        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> doNativeAd());


        // Create an ad.
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(MY_AD_UNIT_ID);

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        LinearLayout adLayout = findViewById(R.id.container);
        adLayout.addView(adView);
        // start loading the ad in the background

        nativeAdView = (NativeAdView) LayoutInflater.from(this)
                .inflate(R.layout.layout_native_ad_template, adLayout, false);
        adLayout.addView(nativeAdView);

    }

    private void doBannerAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        adView.setAdListener(adListener);
        adView.loadAd(adRequest);
    }

    private void doInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        InterstitialAd.load(this, MY_INTERSTITIAL_UNIT_ID, adRequest
                , new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(LT, "onAdFailedToLoad(" + loadAdError.getMessage() + ") invoked!!");
                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        Log.e(LT, "onAdLoaded invoked!!");
                        if (button2 != null) {
                            button2.setText("Show Interstitial");
                        }
                        interstitial = interstitialAd;
                        interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                                Log.e(LT, "onAdClicked invoked!!");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                Log.e(LT, "onAdDismissedFullScreenContent invoked!!");
                                interstitial = null;
                                if (button2 != null) {
                                    button2.setText("Interstitial");
                                }
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                                Log.e(LT, "onAdFailedToShowFullScreenContent(" + adError.getMessage() + ") invoked!!");
                                interstitial = null;
                                button2.setText("Interstitial");
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                                Log.e(LT, "onAdImpression invoked!!");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                Log.e(LT, "onAdShowedFullScreenContent invoked!!");
                            }
                        });
                    }
                });
    }

    private final AdListener adListener = new AdListener() {
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            Log.e(LT, "onAdClicked invoked!!");
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            Log.e(LT, "onAdClosed invoked!!");
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            Log.e(LT, "onAdFailedToLoad(" + loadAdError.getMessage() + ") invoked!!");
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
            Log.e(LT, "onAdImpression invoked!!");
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Log.e(LT, "onAdLoaded invoked!!");
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            Log.e(LT, "onAdOpened invoked!!");
        }

    };

    private final NativeAd.OnNativeAdLoadedListener onNativeAdLoadedListener
            = nativeAd -> populateUnifiedNativeAdView(nativeAd, nativeAdView);

    private void doNativeAd() {
        AdLoader adLoader = new AdLoader.Builder(getBaseContext(), MY_NATIVE_UNIT_ID)
                .forNativeAd(onNativeAdLoadedListener)
                .withAdListener(adListener).build();
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adLoader.loadAd(adRequest);
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
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
        if(adView.getHeadlineView() != null) {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if(adView.getBodyView() != null) {
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.GONE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
        }

        if(adView.getCallToActionView() != null) {
            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.GONE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
        }

        if(adView.getIconView() != null) {
            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        }

        if(adView.getPriceView() != null) {
            if (nativeAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.GONE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            }
        }

        if(adView.getStoreView() != null) {
            if (nativeAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.GONE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            }
        }

        if (adView.getStarRatingView() != null) {
            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.GONE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }
        }

        if(adView.getAdvertiserView() != null) {
            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.GONE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);
    }

    /**
     * Called before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        adView.destroy();
        // Destroy the AdView.
        super.onDestroy();
    }
}
