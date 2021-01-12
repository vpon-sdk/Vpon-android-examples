package com.mopub.mobileads;

import android.content.Context;

import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;
import com.vpon.ads.VponAdRequest;
import com.vpon.ads.VponNativeAd;
import com.vpon.pojo.VponObstructView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_ATTEMPTED;

public class VponNativeCustomEvent extends CustomEventNative {
    private final String LT = "VponNativeCustomEvent";

    private static final String AD_UNIT_ID_KEY = "adUnitID";
    public static final String AD_CONTENT_URL = "contentURL";
    public static final String AD_CONTENT_DATA = "contentData";

    private VponNativeAd vponNativeAd;

    private static VponObstruction vponObstruction = new VponObstruction();

    private CustomEventNativeListener mNativeListener;

    public static VponObstruction getVponObstruction() {
        return vponObstruction;
    }

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
            VponBaseNativeAd vponBaseNativeAd = new VponBaseNativeAd(weakContext
                    , new VponNativeAd(weakContext, adUnitId), mNativeListener);

            VponAdRequest.Builder builder = new VponAdRequest.Builder();

            if (localExtras.get(AD_CONTENT_URL) instanceof String){
                builder.setContentUrl((String) localExtras.get(AD_CONTENT_URL));
            }

            if (localExtras.get(AD_CONTENT_DATA) instanceof HashMap){
                builder.setContentData((HashMap<String, Object>) localExtras.get(AD_CONTENT_DATA));
            }

            List<VponObstructView> views = vponObstruction.getViewsByLicenseKey(adUnitId);

            if(views != null){
                for(VponObstructView view : views){
                    builder.addFriendlyObstruction(view.getObstructView(), view.getPurpose(), view.getDescription());
                }
            }
            vponBaseNativeAd.loadAd(builder.build());
        }
    }
}
