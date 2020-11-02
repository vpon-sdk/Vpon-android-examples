package com.mopub.mobileads;

import com.vpon.pojo.VponObstructView;

import java.util.HashMap;
import java.util.List;

public class VponObstruction {
    private List<VponObstructView> views;
    private HashMap<String, List<VponObstructView>> map = new HashMap<>();

    List<VponObstructView> getViewsByLicenseKey(String licenseKey) {
        return map.get(licenseKey);
    }

    public void addViews(String licenseKey, List<VponObstructView> views) {
        this.map.put(licenseKey, views);
    }
}
