package com.example.user.instagramclone;

public class AllImages{

    String name;

    String mImageUri;

    public AllImages() {

    }
    public AllImages(String mImageUri, String name){

        this.mImageUri = mImageUri;
        this.name = name;
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
}
