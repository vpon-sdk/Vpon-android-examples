package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;

import com.mopub.common.LifecycleListener;
import com.mopub.common.logging.MoPubLog;
import com.vpon.ads.VponAdListener;
import com.vpon.ads.VponAdRequest;
import com.vpon.ads.VponInterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_ATTEMPTED;
import static com.mopub.mobileads.MoPubErrorCode.MISSING_AD_UNIT_ID;

public class VponInterstitialAdAdapter extends BaseAd {
    private static final String LT = "VponInterstitialAdAdapter";
    private VponInterstitialAd vponInterstitialAd;

    private String vponAdUnitID;

    private static final String AD_UNIT_ID_KEY = "adUnitID";
    public static final String AD_CONTENT_URL = "contentURL";
    public static final String AD_CONTENT_DATA = "contentData";

    @Override
    protected void onInvalidate() {
        if (vponInterstitialAd != null) {
            vponInterstitialAd.setAdListener(null);
            vponInterstitialAd.destroy();
            vponInterstitialAd = null;
        }
    }

    @Nullable
    @Override
    protected LifecycleListener getLifecycleListener() {
        return lifecycleListener;
    }


    @NonNull
    @Override
    protected String getAdNetworkId() {
        return vponAdUnitID == null ? "" : vponAdUnitID;
    }

    @Override
    protected boolean checkAndInitializeSdk(@NonNull Activity launcherActivity, @NonNull AdData adData) throws Exception {
        return false;
    }

    @Override
    protected void load(@NonNull Context context, @NonNull AdData adData) throws Exception {
        MoPubLog.log(LT, LOAD_ATTEMPTED, LT);
        Map<String, String> extras = adData.getExtras();
        Context weakContext = new WeakReference<>(context).get();

        vponAdUnitID = extras.get(AD_UNIT_ID_KEY);

        if (vponAdUnitID == null) {
            MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "vpon adUnitId not found, make sure to set on mopub");
            if (mLoadListener != null) {
                mLoadListener.onAdLoadFailed(MISSING_AD_UNIT_ID);
            }
            return;
        } else {
            MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "vpon adUnitId : " + vponAdUnitID);
        }

        vponInterstitialAd = new VponInterstitialAd(weakContext, vponAdUnitID);

        vponInterstitialAd.setAdListener(adListener);

        VponAdRequest.Builder builder = new VponAdRequest.Builder();

        if (extras.get(AD_CONTENT_URL) instanceof String) {
            builder.setContentUrl((String) extras.get(AD_CONTENT_URL));
        }

        if (extras.get(AD_CONTENT_DATA) instanceof String) {
            String jsonString = extras.get(AD_CONTENT_DATA);
            builder.setContentData(jsonStringToHashMap(jsonString));
        }

        vponInterstitialAd.loadAd(builder.build());
    }

    @Override
    protected void show() {
        super.show();
        if (vponInterstitialAd != null) {
            if (vponInterstitialAd.isReady()) {
                vponInterstitialAd.show();
            } else {
                MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "vpon interstitial not ready");
            }
        } else {
            MoPubLog.log(LT, MoPubLog.AdLogEvent.SHOW_FAILED, "vpon interstitial null");
        }
    }

    private VponAdListener adListener = new VponAdListener() {
        @Override
        public void onAdFailedToLoad(int errorCode) {
            if (mLoadListener != null) {
                mLoadListener.onAdLoadFailed(mapVponErrorCodeToMoPubErrorCode(errorCode));
            }
        }

        @Override
        public void onAdLeftApplication() {
            if (mInteractionListener != null) {
                mInteractionListener.onAdClicked();
            }
        }

        @Override
        public void onAdLoaded() {
            if (mLoadListener != null) {
                mLoadListener.onAdLoaded();
            }
        }

        @Override
        public void onAdOpened() {
            if (mInteractionListener != null) {
                mInteractionListener.onAdShown();
            }
        }

        @Override
        public void onAdClosed() {
            if (mInteractionListener != null) {
                mInteractionListener.onAdDismissed();
            }
        }
    };

    private LifecycleListener lifecycleListener = new LifecycleListener() {
        @Override
        public void onCreate(@NonNull Activity activity) {

        }

        @Override
        public void onStart(@NonNull Activity activity) {

        }

        @Override
        public void onPause(@NonNull Activity activity) {
            if (vponInterstitialAd != null) {
                vponInterstitialAd.pause();
            }
        }

        @Override
        public void onResume(@NonNull Activity activity) {
            if (vponInterstitialAd != null) {
                vponInterstitialAd.resume();
            }
        }

        @Override
        public void onRestart(@NonNull Activity activity) {

        }

        @Override
        public void onStop(@NonNull Activity activity) {

        }

        @Override
        public void onDestroy(@NonNull Activity activity) {
            if (vponInterstitialAd != null) {
                vponInterstitialAd.destroy();
            }
        }

        @Override
        public void onBackPressed(@NonNull Activity activity) {

        }
    };

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

    private HashMap<String, Object> jsonStringToHashMap(String jsonString) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                Object value = jsonObject.get(key);

                hashMap.put(key, value);
            }
            return hashMap;
        } catch (JSONException ignored) {
            return hashMap;
        }
    }
}
