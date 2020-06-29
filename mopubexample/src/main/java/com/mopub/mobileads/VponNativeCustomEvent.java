package com.mopub.mobileads;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.NativeImageHelper;
import com.mopub.nativeads.StaticNativeAd;
import com.vpon.ads.VponAdListener;
import com.vpon.ads.VponAdRequest;
import com.vpon.ads.VponNativeAd;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_ATTEMPTED;
import static com.mopub.nativeads.NativeImageHelper.preCacheImages;

public class VponNativeCustomEvent extends CustomEventNative {
    private final String LT = "VponNativeCustomEvent";

    private static final String AD_UNIT_ID_KEY = "adUnitID";
    public static final String AD_CONTENT_URL = "contentURL";
    public static final String AD_CONTENT_DATA = "contentData";

    private VponNativeAd vponNativeAd;

    private CustomEventNativeListener mNativeListener;

    @Override
    protected void loadNativeAd(@NonNull Context context, @NonNull CustomEventNativeListener customEventNativeListener, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) {
        this.mNativeListener = customEventNativeListener;
        Context weakContext = new WeakReference<>(context).get();

        String adUnitId = null;
        MoPubLog.log(LT, LOAD_ATTEMPTED, LT);
        if (serverExtras.containsKey(AD_UNIT_ID_KEY)) {
            adUnitId = serverExtras.get(AD_UNIT_ID_KEY);
        }
        if (adUnitId == null) {
            MoPubLog.log(LT, MoPubLog.AdLogEvent.LOAD_FAILED, "vpon adUnitId not found, make sure to set on mopub");
            if (mNativeListener != null) {
                mNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            }
            return;
        } else {
            MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "vpon adUnitId : " + adUnitId);
        }

        if (mNativeListener != null) {
            VponStaticNativeAd vponStaticNativeAd = new VponStaticNativeAd(weakContext
                    , new VponNativeAd(weakContext, adUnitId), mNativeListener);

            VponAdRequest.Builder builder = new VponAdRequest.Builder();

            if (localExtras.get(AD_CONTENT_URL) instanceof String){
                builder.setContentUrl((String) localExtras.get(AD_CONTENT_URL));
            }

            if (localExtras.get(AD_CONTENT_DATA) instanceof HashMap){
                builder.setContentData((HashMap<String, Object>) localExtras.get(AD_CONTENT_DATA));
            }

            vponStaticNativeAd.loadAd(builder.build());
        }
    }


    private static class VponStaticNativeAd extends StaticNativeAd {
        private final String LT = "VponStaticNativeAd";

        private CustomEventNativeListener nativeListener;
        private VponNativeAd vponNativeAd;
        private Context context;

        VponStaticNativeAd(Context context, VponNativeAd vponNativeAd, CustomEventNativeListener nativeListener) {
            this.vponNativeAd = vponNativeAd;
            this.nativeListener = nativeListener;
            this.context = context;
        }

        public void loadAd(VponAdRequest vponAdRequest) {
            vponNativeAd.setAdListener(new VponAdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    MoPubLog.log(LT, MoPubLog.AdLogEvent.LOAD_FAILED, "errorCode:" + errorCode);
                    if (nativeListener != null) {
                        nativeListener.onNativeAdFailed(mapVponErrorCodeToMoPubErrorCode(errorCode));
                    }
                }

                @Override
                public void onAdLoaded() {
                    MoPubLog.log(LT, MoPubLog.AdLogEvent.LOAD_SUCCESS, "onAdLoaded");
                }
            });

            vponNativeAd.withNativeAdLoadedListener(new VponNativeAd.OnNativeAdLoadedListener() {
                @Override
                public void onNativeAdLoaded(VponNativeAd.NativeAdData nativeAdData) {
                    MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "onNativeAdLoaded");

                    if (nativeListener != null) {
                        setTitle(nativeAdData.getTitle());
                        setText(nativeAdData.getBody());
                        setIconImageUrl(nativeAdData.getIcon().getUrl());
                        setMainImageUrl(nativeAdData.getCoverImage().getUrl());
                        setCallToAction(nativeAdData.getCallToAction());
                        setStarRating(nativeAdData.getRating() == null ? null : nativeAdData.getRating().getValue());
                        preCacheImages(context, getAllImageUrls(), new NativeImageHelper.ImageListener() {
                            @Override
                            public void onImagesCached() {
                                if (isInvalidated()) {
                                    return;
                                }
                                nativeListener.onNativeAdLoaded(VponStaticNativeAd.this);
                            }

                            @Override
                            public void onImagesFailedToCache(final NativeErrorCode errorCode) {
                                MoPubLog.log(LT, MoPubLog.AdLogEvent.LOAD_FAILED, "errorCode:" + errorCode);
                                nativeListener.onNativeAdFailed(errorCode);
                            }
                        });
                    }
                }
            });


            vponNativeAd.loadAd(vponAdRequest);
        }

        @Override
        public void prepare(@NonNull View view) {
            super.prepare(view);
            vponNativeAd.registerViewForInteraction(view);
        }

        @Override
        public void clear(@NonNull View view) {
            super.clear(view);
        }

        @Override
        public void destroy() {
            super.destroy();
            vponNativeAd.destroy();
        }

        @NonNull
        List<String> getAllImageUrls() {
            final List<String> imageUrls = new ArrayList<String>();
            if (!TextUtils.isEmpty(getMainImageUrl())) {
                imageUrls.add(getMainImageUrl());
            }
            if (!TextUtils.isEmpty(getIconImageUrl())) {
                imageUrls.add(getIconImageUrl());
            }
            return imageUrls;
        }

        private NativeErrorCode mapVponErrorCodeToMoPubErrorCode(int vponErrorCode) {
            if (vponErrorCode == VponAdRequest.VponErrorCode.NO_FILL.getErrorCode()) {
                return NativeErrorCode.NETWORK_NO_FILL;
            } else if (vponErrorCode == VponAdRequest.VponErrorCode.NETWORK_ERROR.getErrorCode()) {
                return NativeErrorCode.CONNECTION_ERROR;
            } else if (vponErrorCode == VponAdRequest.VponErrorCode.INTERNAL_ERROR.getErrorCode()) {
                return NativeErrorCode.NETWORK_INVALID_STATE;
            } else if (vponErrorCode == VponAdRequest.VponErrorCode.INVALID_REQUEST.getErrorCode()) {
                return NativeErrorCode.INVALID_REQUEST_URL;
            }
            return NativeErrorCode.UNSPECIFIED;
        }
    }
}
