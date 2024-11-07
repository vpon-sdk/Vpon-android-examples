package com.vpon.dfpexample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    private static final String LT = "AdManagerSample";

    private AdManagerAdView adView = null;
    private AdManagerInterstitialAd interstitial= null;
    private String MY_AD_UNIT_ID = "/6499/example/banner";//TODO SET YOUR AD_UNIT_ID here
    private String MY_INTERSTITIAL_UNIT_ID = "/6499/example/interstitial";////TODO SET YOUR AD_UNIT_ID here
    private LinearLayout adBannerLayout;


    // In this sample, we will show you the banner and IS ads
    // by onclicks. Don't forget to import the SDK!
    private Button button1;
    private Button button2;
    private TextView labelAdid;

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


        // Create an ad.
        adView = new AdManagerAdView(this);
        adView.setAdSizes(AdSize.BANNER);
        adView.setAdUnitId(MY_AD_UNIT_ID);

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        adBannerLayout = findViewById(R.id.container);
        adBannerLayout.addView(adView);
        // start loading the ad in the background

    }

    private void doBannerAd() {
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder()
                .build();
        adView.setAdListener(new AdListener() {
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

        });
        adView.loadAd(adRequest);
    }

    private void doInterstitialAd() {
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder()
                .build();

        AdManagerInterstitialAd.load(this, MY_INTERSTITIAL_UNIT_ID
                , adRequest, new AdManagerInterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(LT, "onAdFailedToLoad(" + loadAdError.getMessage() + ") invoked!!");
                    }

                    @Override
                    public void onAdLoaded(@NonNull AdManagerInterstitialAd adManagerInterstitialAd) {
                        super.onAdLoaded(adManagerInterstitialAd);
                        Log.e(LT, "onAdLoaded invoked!!");
                        if (button2 != null) {
                            button2.setText("Show Interstitial");
                        }
                        interstitial = adManagerInterstitialAd;
                        interstitial.setAppEventListener((name, data) -> {
                            Log.e(LT, "onAppEvent(" + name + "/" + data + ") invoked!!");
                        });
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
}
