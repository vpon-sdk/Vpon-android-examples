package com.mopub.mobileads;

import com.mopub.nativeads.NativeRendererHelper;
import com.vpon.ads.VponMediaView;
import com.vpon.ads.VponNativeAd;

public class VponNativeRendererHelper extends NativeRendererHelper {
    public static void addMediaView(final VponMediaView mediaView,
                                    VponNativeAd vponNativeAd, VponNativeAd.NativeAdData nativeAdData) {
        mediaView.setNativeAd(vponNativeAd, nativeAdData);
    }
}
