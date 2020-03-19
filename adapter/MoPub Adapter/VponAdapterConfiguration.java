package com.mopub.mobileads;

import android.content.Context;

import com.mopub.common.BaseAdapterConfiguration;
import com.mopub.common.OnNetworkInitializationFinishedListener;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class VponAdapterConfiguration extends BaseAdapterConfiguration {
    @NonNull
    @Override
    public String getAdapterVersion() {
        return "v1.0.0";
    }

    @Nullable
    @Override
    public String getBiddingToken(@NonNull Context context) {
        return null;
    }

    @NonNull
    @Override
    public String getMoPubNetworkName() {
        return "vpon network";
    }

    @NonNull
    @Override
    public String getNetworkSdkVersion() {
        return "5.0.0";
    }

    @Override
    public void initializeNetwork(@NonNull Context context, @Nullable Map<String, String> configuration, @NonNull OnNetworkInitializationFinishedListener listener) {
        listener.onNetworkInitializationFinished(VponAdapterConfiguration.class, MoPubErrorCode.ADAPTER_INITIALIZATION_SUCCESS);
    }
}
