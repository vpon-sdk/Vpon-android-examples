package com.example.mopubexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.VponAdapterConfiguration;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;
import com.mopub.network.AdLoader;

import java.util.HashMap;
import java.util.Map;

import static com.mopub.mobileads.VponBannerCustomEvent.AD_SIZE_KEY;

public class MainActivity extends AppCompatActivity {
    private static final String LT = "MainActivity";

    protected MoPubView adView = null;
    protected MoPubInterstitial interstitialAd = null;
    protected AdLoader adLoader = null;
    protected MoPubNative moPubNative = null;
    protected MoPubNative.MoPubNativeNetworkListener moPubNativeNetworkListener = new MoPubNative.MoPubNativeNetworkListener() {
        @Override
        public void onNativeLoad(NativeAd nativeAd) {
            Log.d(LT, "onNativeLoad");
            final AdapterHelper adapterHelper = new AdapterHelper(MainActivity.this, 0, 3);

            View v = adapterHelper.getAdView(null, null, nativeAd, new ViewBinder.Builder(0).build());
            adLayout.addView(v);
        }

        @Override
        public void onNativeFail(NativeErrorCode errorCode) {
            Log.d(LT, "onNativeFail:" + errorCode.toString());
        }
    };

    private String MY_BANNER_UNIT_ID = "";//TODO SET YOUR AD_UNIT_ID here
    private String MY_INTERSTITIAL_UNIT_ID = "";////TODO SET YOUR AD_UNIT_ID here
    private String MY_NATIVE_UNIT_ID = "";////TODO SET YOUR AD_UNIT_ID here
    private LinearLayout adLayout;

    private Button BannerButton;
    private Button InterstitialButton;
    private Button NativeAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mopubInit();

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

        adLayout = findViewById(R.id.container);
    }



    private void mopubInit() {
        SdkConfiguration sdkConfiguration = new SdkConfiguration
                .Builder(MY_BANNER_UNIT_ID)
                .withAdditionalNetwork(VponAdapterConfiguration.class.getName())
                .withLogLevel(MoPubLog.LogLevel.DEBUG)
                .build();

        MoPub.initializeSdk(this, sdkConfiguration, new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                Log.d(LT, "onInitializationFinished");
            }
        });
    }

    private void doBannerAd() {
        adView = new MoPubView(this);

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        adLayout.addView(adView);

        adView.setAdUnitId(MY_BANNER_UNIT_ID);

        adView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(MoPubView banner) {
                Log.d(LT, "onBannerLoaded");
            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                Log.d(LT, "onBannerFailed : " + errorCode);
            }

            @Override
            public void onBannerClicked(MoPubView banner) {
                Log.d(LT, "onBannerClicked");
            }

            @Override
            public void onBannerExpanded(MoPubView banner) {
                Log.d(LT, "onBannerExpanded");
            }

            @Override
            public void onBannerCollapsed(MoPubView banner) {
                Log.d(LT, "onBannerCollapsed");
            }
        });

        //TODO
        //make sure you set correct AdSize for corresponding Ad Unit ID (MoPub format: Medium rectangle or Banner?)
        adView.setAdSize(MoPubView.MoPubAdSize.HEIGHT_250);

        //set ad size info for vpon mediation adapter
        Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(AD_SIZE_KEY, adView.getAdSize());
        adView.setLocalExtras(localExtras);

        adView.loadAd();
    }

    private void doInterstitialAd() {
        interstitialAd = new MoPubInterstitial(this, MY_INTERSTITIAL_UNIT_ID);
        interstitialAd.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                Log.d(LT, "onInterstitialLoaded");

                if (interstitialAd != null && interstitialAd.isReady()) {
                    interstitialAd.show();
                }
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                Log.d(LT, "onInterstitialFailed " + errorCode);
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial interstitial) {
                Log.d(LT, "onInterstitialShown");
            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial interstitial) {
                Log.d(LT, "onInterstitialClicked");
            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial interstitial) {
                Log.d(LT, "onInterstitialDismissed");
            }
        });

        interstitialAd.load();
    }

    private void doNativeAd() {
        moPubNative = new MoPubNative(this, MY_NATIVE_UNIT_ID, moPubNativeNetworkListener);

        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.mopub_native_layout)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);
        moPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);

        moPubNative.makeRequest();
    }
}
