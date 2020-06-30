package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;

import com.mopub.common.logging.MoPubLog;
import com.vpon.ads.VponAdListener;
import com.vpon.ads.VponAdRequest;
import com.vpon.ads.VponInterstitialAd;

import java.lang.ref.WeakReference;
import java.util.Map;

import static com.mopub.mobileads.MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR;

public class VponInterstitialCustomEvent extends CustomEventInterstitial {
    private final String LT = "VponInterstitialCustomEvent";

    private VponInterstitialAd vponInterstitialAd;
    private CustomEventInterstitialListener mInterstitialListener;

    private static final String AD_UNIT_ID_KEY = "adUnitID";

    @Override
    protected void loadInterstitial(Context context, final CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        this.mInterstitialListener = customEventInterstitialListener;
        Context weakContext = new WeakReference<>(context).get();

        String adUnitId = null;

        if (serverExtras.containsKey(AD_UNIT_ID_KEY)) {
            adUnitId = serverExtras.get(AD_UNIT_ID_KEY);
        }

        if (adUnitId == null) {
            MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "vpon adUnitId not found, make sure to set on mopub");
            if (mInterstitialListener != null) {
                mInterstitialListener.onInterstitialFailed(ADAPTER_CONFIGURATION_ERROR);
            }
            return;
        }else {
            MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "vpon adUnitId : " + adUnitId);
        }


        //TODO 舊版adapter也有這個判斷， mopub init IS 要傳入activity ，是否要留著？
        if (!(context instanceof Activity)) {
            MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "context not activity");
            if (mInterstitialListener != null) {
                mInterstitialListener.onInterstitialFailed(ADAPTER_CONFIGURATION_ERROR);
            }
            return;
        }
        vponInterstitialAd = new VponInterstitialAd(weakContext, adUnitId);

        vponInterstitialAd.setAdListener(new VponAdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (mInterstitialListener != null) {
                    mInterstitialListener.onInterstitialFailed(mapVponErrorCodeToMoPubErrorCode(errorCode));
                }
            }

            @Override
            public void onAdLeftApplication() {
                //do nothing, is an alias for onInterstitialClicked()
            }

            @Override
            public void onAdLoaded() {
                if (mInterstitialListener != null) {
                    mInterstitialListener.onInterstitialLoaded();
                }
            }

            @Override
            public void onAdOpened() {
                if (mInterstitialListener != null) {
                    mInterstitialListener.onInterstitialClicked();
                }
            }

            @Override
            public void onAdClosed() {
                if (mInterstitialListener != null) {
                    mInterstitialListener.onInterstitialDismissed();
                }
            }
        });

        vponInterstitialAd.loadAd(new VponAdRequest.Builder().build());

    }

    @Override
    protected void showInterstitial() {
        if (vponInterstitialAd.isReady()) {
            if (mInterstitialListener != null) {
                mInterstitialListener.onInterstitialShown();
            }
            vponInterstitialAd.show();
        } else {
            MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "vpon interstitial not ready");
        }
    }

    @Override
    protected void onInvalidate() {
        if (vponInterstitialAd != null) {
            vponInterstitialAd.destroy();
            vponInterstitialAd = null;
        }
    }

    private MoPubErrorCode mapVponErrorCodeToMoPubErrorCode(int vponErrorCode) {
        if (vponErrorCode == VponAdRequest.VponErrorCode.NO_FILL.getErrorCode()) {
            return MoPubErrorCode.NETWORK_NO_FILL;
        } else if (vponErrorCode == VponAdRequest.VponErrorCode.NETWORK_ERROR.getErrorCode()) {
            return MoPubErrorCode.SERVER_ERROR;
        } else if (vponErrorCode == VponAdRequest.VponErrorCode.INTERNAL_ERROR.getErrorCode()) {
            return MoPubErrorCode.INTERNAL_ERROR;
        } else if (vponErrorCode == VponAdRequest.VponErrorCode.INVALID_REQUEST.getErrorCode()) {
            return MoPubErrorCode.UNSPECIFIED;
        }
        return MoPubErrorCode.UNSPECIFIED;
    }
}
