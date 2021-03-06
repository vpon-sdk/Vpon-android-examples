package com.mopub.mobileads;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class VponViewBinder {
    public final static class Builder {
        private final int layoutId;
        private int titleId;
        private int textId;
        private int callToActionId;
        private int mainImageId;
        private int iconImageId;
        private int privacyInformationIconImageId;
        private int sponsoredTextId;

        @NonNull
        private Map<String, Integer> extras = Collections.emptyMap();

        public Builder(final int layoutId) {
            this.layoutId = layoutId;
            this.extras = new HashMap<String, Integer>();
        }

        @NonNull
        public final Builder titleId(final int titleId) {
            this.titleId = titleId;
            return this;
        }

        @NonNull
        public final Builder textId(final int textId) {
            this.textId = textId;
            return this;
        }

        @NonNull
        public final Builder callToActionId(final int callToActionId) {
            this.callToActionId = callToActionId;
            return this;
        }

        @NonNull
        public final Builder mainImageId(final int mediaLayoutId) {
            this.mainImageId = mediaLayoutId;
            return this;
        }

        @NonNull
        public final Builder iconImageId(final int iconImageId) {
            this.iconImageId = iconImageId;
            return this;
        }

        @NonNull
        public final Builder privacyInformationIconImageId(final int privacyInformationIconImageId) {
            this.privacyInformationIconImageId = privacyInformationIconImageId;
            return this;
        }

        @NonNull
        public final Builder sponsoredTextId(final int sponsoredTextId) {
            this.sponsoredTextId = sponsoredTextId;
            return this;
        }

        @NonNull
        public final Builder addExtras(final Map<String, Integer> resourceIds) {
            this.extras = new HashMap<String, Integer>(resourceIds);
            return this;
        }

        @NonNull
        public final Builder addExtra(final String key, final int resourceId) {
            this.extras.put(key, resourceId);
            return this;
        }

        @NonNull
        public final VponViewBinder build() {
            return new VponViewBinder(this);
        }
    }

    final int layoutId;
    final int titleId;
    final int textId;
    final int callToActionId;
    final int mainImageId;
    final int iconImageId;
    final int privacyInformationIconImageId;
    final int sponsoredTextId;
    @NonNull
    final Map<String, Integer> extras;

    private VponViewBinder(@NonNull final Builder builder) {
        this.layoutId = builder.layoutId;
        this.titleId = builder.titleId;
        this.textId = builder.textId;
        this.callToActionId = builder.callToActionId;
        this.mainImageId = builder.mainImageId;
        this.iconImageId = builder.iconImageId;
        this.privacyInformationIconImageId = builder.privacyInformationIconImageId;
        this.sponsoredTextId = builder.sponsoredTextId;
        this.extras = builder.extras;
    }
}
