package com.mopub.mobileads;

import android.content.Context;
import android.util.Log;

import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Views;
import com.vpon.ads.VponAdListener;
import com.vpon.ads.VponAdRequest;
import com.vpon.ads.VponAdSize;
import com.vpon.ads.VponBanner;

import java.lang.ref.WeakReference;
import java.util.Map;

import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_ATTEMPTED;
import static com.mopub.mobileads.MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR;

public class VponBannerCustomEvent extends CustomEventBanner {
    private final String LT = "VponBannerCustomEvent";

    private static final String AD_UNIT_ID_KEY = "adUnitID";
    public static final String AD_SIZE_KEY = "adSize";

    private VponAdSize adSize = VponAdSize.SMART_BANNER;
    private VponBanner vponBanner = null;
    private CustomEventBannerListener mBannerListener = null;

    @Override
    protected void loadBanner(Context context, final CustomEventBannerListener customEventBannerListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        this.mBannerListener = customEventBannerListener;
        Context weakContext = new WeakReference<>(context).get();

        String adUnitId = null;
        MoPubLog.log(LT, LOAD_ATTEMPTED, LT);
        if (serverExtras.containsKey(AD_UNIT_ID_KEY)) {
            adUnitId = serverExtras.get(AD_UNIT_ID_KEY);
        }
        if (adUnitId == null) {
            MoPubLog.log(LT, MoPubLog.AdLogEvent.LOAD_FAILED, "vpon adUnitId not found, make sure to set on mopub");
            if (mBannerListener != null) {
                mBannerListener.onBannerFailed(ADAPTER_CONFIGURATION_ERROR);
            }
            return;
        } else {
            MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "vpon adUnitId : " + adUnitId);
        }
        if (localExtras.containsKey(AD_SIZE_KEY)) {
            MoPubView.MoPubAdSize moPubAdSize = (MoPubView.MoPubAdSize) localExtras.get(AD_SIZE_KEY);
            if (moPubAdSize != null) {
                adSize = calculateAdSize(moPubAdSize);
            }
        }

        vponBanner = new VponBanner(weakContext, adUnitId, adSize);

        vponBanner.setAdListener(new VponAdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d(LT, "onAdFailedToLoad");
                if (mBannerListener != null) {
                    mBannerListener.onBannerFailed(mapVponErrorCodeToMoPubErrorCode(errorCode));
                }
            }

            @Override
            public void onAdLeftApplication() {
                Log.d(LT, "onAdLeftApplication");
                if (mBannerListener != null) {
                    mBannerListener.onLeaveApplication();
                }
            }

            @Override
            public void onAdLoaded() {
                Log.d(LT, "onAdLoaded");
                if (mBannerListener != null) {
                    mBannerListener.onBannerLoaded(vponBanner);
                }
            }

            @Override
            public void onAdOpened() {
                Log.d(LT, "onAdOpened");
                if (mBannerListener != null) {
                    mBannerListener.onBannerClicked();
                }
            }
        });

        vponBanner.loadAd(new VponAdRequest.Builder().build());
    }

    @Override
    protected void onInvalidate() {
        Views.removeFromParent(vponBanner);
        if (vponBanner != null) {
            vponBanner.setAdListener(null);
            vponBanner.destroy();
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

    private VponAdSize calculateAdSize(MoPubView.MoPubAdSize adSize) {
        switch (adSize) {
            case HEIGHT_50:
                return VponAdSize.BANNER;
            case HEIGHT_90:
                return VponAdSize.IAB_LEADERBOARD;
            case HEIGHT_250:
            case HEIGHT_280:
                return VponAdSize.IAB_MRECT;
            case MATCH_VIEW:
            default:
                return VponAdSize.SMART_BANNER;
        }
    }
}
