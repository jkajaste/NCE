package com.ghettoapps.nce;

/**
 * Created by kajajuh on 19.10.2015.
 */
public class SearchResult {

    private String mName, mAddress, mDistance = null;

    SearchResult(String name, String address, String distance) {
        mName = name;
        mAddress = address;
        mDistance = distance;
    }

    public String getName() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getDistance() {
        return mDistance;
    }

}
