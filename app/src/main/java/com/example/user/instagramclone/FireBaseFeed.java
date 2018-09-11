package com.example.user.instagramclone;

public class FireBaseFeed {


    String name;

    String mImageUri;
    String profilerUri;

    public FireBaseFeed() {

    }
    public FireBaseFeed(String mImageUri, String name, String profileUri){

        this.mImageUri = mImageUri;
        this.name = name;
        this.profilerUri = profileUri;
    }

    public String getmImageUri() {
        return mImageUri;

    }

    public void setmImageUri(String mImageUri){

        this.mImageUri = mImageUri;
    }


    public String getName() {
        return name;
    }

    public String getProfilerUri() {

        return profilerUri;
    }
}
