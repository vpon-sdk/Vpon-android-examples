package com.mopub.mobileads;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.BaseNativeAd;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.NativeImageHelper;
import com.vpon.ads.VponAdListener;
import com.vpon.ads.VponAdRequest;
import com.vpon.ads.VponNativeAd;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static com.mopub.nativeads.NativeImageHelper.preCacheImages;

public class VponBaseNativeAd  extends BaseNativeAd {
    private final String LT = "VponBaseNativeAd";

    private VponNativeAd.NativeAdData nativeAdData;
    private String mMainImageUrl;
    private String mIconImageUrl;
    private String mClickDestinationUrl;
    private String mCallToAction;
    private String mTitle;
    private String mText;
    private Double mStarRating;
    private String mPrivacyInformationIconClickThroughUrl;
    private String mPrivacyInformationIconImageUrl;
    private String mSponsored;

    private CustomEventNative.CustomEventNativeListener nativeListener;

    private VponNativeAd vponNativeAd;
    private Context context;

    public String getMainImageUrl() {
        return mMainImageUrl;
    }

    public void setMainImageUrl(String mMainImageUrl) {
        this.mMainImageUrl = mMainImageUrl;
    }

    public String getIconImageUrl() {
        return mIconImageUrl;
    }

    public void setIconImageUrl(String mIconImageUrl) {
        this.mIconImageUrl = mIconImageUrl;
    }

    public String getClickDestinationUrl() {
        return mClickDestinationUrl;
    }

    public void setClickDestinationUrl(String mClickDestinationUrl) {
        this.mClickDestinationUrl = mClickDestinationUrl;
    }

    public String getCallToAction() {
        return mCallToAction;
    }

    public void setCallToAction(String mCallToAction) {
        this.mCallToAction = mCallToAction;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public Double getStarRating() {
        return mStarRating;
    }

    public void setStarRating(Double mStarRating) {
        this.mStarRating = mStarRating;
    }

    public String getPrivacyInformationIconClickThroughUrl() {
        return mPrivacyInformationIconClickThroughUrl;
    }

    public void setPrivacyInformationIconClickThroughUrl(String mPrivacyInformationIconClickThroughUrl) {
        this.mPrivacyInformationIconClickThroughUrl = mPrivacyInformationIconClickThroughUrl;
    }

    public String getPrivacyInformationIconImageUrl() {
        return mPrivacyInformationIconImageUrl;
    }

    public void setPrivacyInformationIconImageUrl(String mPrivacyInformationIconImageUrl) {
        this.mPrivacyInformationIconImageUrl = mPrivacyInformationIconImageUrl;
    }

    public String getSponsored() {
        return mSponsored;
    }

    public void setSponsored(String mSponsored) {
        this.mSponsored = mSponsored;
    }

    public VponNativeAd.NativeAdData getNativeAdData() {
        return nativeAdData;
    }

    public void setNativeAdData(VponNativeAd.NativeAdData nativeAdData) {
        this.nativeAdData = nativeAdData;
    }

    public VponNativeAd getVponNativeAd() {
        return vponNativeAd;
    }

    VponBaseNativeAd(Context context, VponNativeAd vponNativeAd, CustomEventNative.CustomEventNativeListener nativeListener) {
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

            @Override
            public void onAdImpression() {
                MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "onAdImpression");
                notifyAdImpressed();
            }

            @Override
            public void onAdClicked() {
                MoPubLog.log(LT, MoPubLog.AdLogEvent.CLICKED, "onAdClicked");
                notifyAdClicked();
            }
        });

        vponNativeAd.withNativeAdLoadedListener(new VponNativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(VponNativeAd.NativeAdData nativeAdData) {
                MoPubLog.log(LT, MoPubLog.AdLogEvent.CUSTOM, "onNativeAdLoaded");

                if (nativeListener != null) {
                    setNativeAdData(nativeAdData);
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
                            nativeListener.onNativeAdLoaded(VponBaseNativeAd.this);
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
        vponNativeAd.registerViewForInteraction(view);
    }

    @Override
    public void clear(@NonNull View view) {
    }

    @Override
    public void destroy() {
        vponNativeAd.destroy();
    }

    @NonNull
    List<String> getAllImageUrls() {
        final List<String> imageUrls = new ArrayList<String>();
//        if (!TextUtils.isEmpty(getMainImageUrl())) {
//            imageUrls.add(getMainImageUrl());
//        }
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
