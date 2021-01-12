package com.mopub.mobileads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.nativeads.BaseNativeAd;
import com.mopub.nativeads.MoPubAdRenderer;
import com.mopub.nativeads.NativeImageHelper;

import java.util.WeakHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.view.View.VISIBLE;

/**
 * An implementation of {@link MoPubAdRenderer} for rendering native ads.
 */
public class VponNativeAdRenderer implements MoPubAdRenderer<VponBaseNativeAd> {
    @NonNull
    private final VponViewBinder mViewBinder;


    @VisibleForTesting
    @NonNull
    final WeakHashMap<View, VponNativeViewHolder> mViewHolderMap;

    /**
     * Constructs a native ad renderer with a view binder.
     *
     * @param viewBinder The view binder to use when inflating and rendering an ad.
     */
    public VponNativeAdRenderer(@NonNull final VponViewBinder viewBinder) {
        mViewBinder = viewBinder;
        mViewHolderMap = new WeakHashMap<View, VponNativeViewHolder>();
    }

    @Override
    @NonNull
    public View createAdView(@NonNull final Context context, @Nullable final ViewGroup parent) {
        return LayoutInflater
                .from(context)
                .inflate(mViewBinder.layoutId, parent, false);
    }

    @Override
    public void renderAdView(@NonNull View view, @NonNull VponBaseNativeAd ad) {
        VponNativeViewHolder staticNativeViewHolder = mViewHolderMap.get(view);
        if (staticNativeViewHolder == null) {
            staticNativeViewHolder = VponNativeViewHolder.fromViewBinder(view, mViewBinder);
            mViewHolderMap.put(view, staticNativeViewHolder);
        }

        update(staticNativeViewHolder, ad);

        setViewVisibility(staticNativeViewHolder, VISIBLE);
    }

    @Override
    public boolean supports(@NonNull final BaseNativeAd nativeAd) {
        Preconditions.checkNotNull(nativeAd);
        return nativeAd instanceof VponBaseNativeAd;
    }

    //set data to UI
    private void update(@NonNull final VponNativeViewHolder staticNativeViewHolder,
                        @NonNull final VponBaseNativeAd staticNativeAd) {
        VponNativeRendererHelper.addTextView(staticNativeViewHolder.titleView,
                staticNativeAd.getTitle());
        VponNativeRendererHelper.addTextView(staticNativeViewHolder.textView, staticNativeAd.getText());
        VponNativeRendererHelper.addTextView(staticNativeViewHolder.callToActionView,
                staticNativeAd.getCallToAction());
        NativeImageHelper.loadImageView(staticNativeAd.getIconImageUrl(),
                staticNativeViewHolder.iconImageView);
        VponNativeRendererHelper.addPrivacyInformationIcon(
                staticNativeViewHolder.privacyInformationIconImageView,
                staticNativeAd.getPrivacyInformationIconImageUrl(),
                staticNativeAd.getPrivacyInformationIconClickThroughUrl());
        VponNativeRendererHelper.addSponsoredView(staticNativeAd.getSponsored(),
                staticNativeViewHolder.sponsoredTextView);

        VponNativeRendererHelper.addMediaView(staticNativeViewHolder.vponMediaView,
                staticNativeAd.getVponNativeAd(), staticNativeAd.getNativeAdData());
    }

    private void setViewVisibility(@NonNull final VponNativeViewHolder staticNativeViewHolder,
                                   final int visibility) {
        if (staticNativeViewHolder.mainView != null) {
            staticNativeViewHolder.mainView.setVisibility(visibility);
        }
    }
}
