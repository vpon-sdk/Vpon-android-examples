package com.mopub.mobileads;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mopub.common.logging.MoPubLog;
import com.vpon.ads.VponMediaView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.mopub.common.logging.MoPubLog.SdkLogEvent.ERROR_WITH_THROWABLE;

class VponNativeViewHolder {
    @Nullable View mainView;
    @Nullable TextView titleView;
    @Nullable TextView textView;
    @Nullable TextView callToActionView;
    @Nullable ImageView iconImageView;
    @Nullable ImageView privacyInformationIconImageView;
    @Nullable TextView sponsoredTextView;
    @Nullable
    VponMediaView vponMediaView;

    private VponNativeViewHolder() {}

    static VponNativeViewHolder fromViewBinder(@NonNull final View view,
                                               @NonNull final VponViewBinder viewBinder) {
        final VponNativeViewHolder nativeViewHolder = new VponNativeViewHolder();
        nativeViewHolder.mainView = view;
        try {
            nativeViewHolder.titleView = view.findViewById(viewBinder.titleId);
            nativeViewHolder.textView = view.findViewById(viewBinder.textId);
            nativeViewHolder.callToActionView = view.findViewById(viewBinder.callToActionId);
            nativeViewHolder.vponMediaView = view.findViewById(viewBinder.mainImageId);
            nativeViewHolder.iconImageView = view.findViewById(viewBinder.iconImageId);
            nativeViewHolder.privacyInformationIconImageView =
                    view.findViewById(viewBinder.privacyInformationIconImageId);
            nativeViewHolder.sponsoredTextView = view.findViewById(viewBinder.sponsoredTextId);
            return nativeViewHolder;
        } catch (ClassCastException exception) {
            MoPubLog.log(ERROR_WITH_THROWABLE, "Could not cast from id in ViewBinder to expected " +
                    "View type", exception);
            return null;
        }
    }
}
