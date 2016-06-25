package com.evansappwriter.photolist.model;

/**
 * Created by markevans on 6/20/16.
 */
public class Photo implements Comparable<Photo>  {
    private static final String PHOTO_URL_DOMAIN1 = "https://farm";
    private static final String PHOTO_URL_DOMAIN2 = ".staticflickr.com/";
    private static final String PHOTO_EXTENSION = "jpg";

    private String mId;
    private String mTitle;
    private String mOwner;
    private String mSecret;
    private String mServer;
    private String mFarm;

    public Photo() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    public String getSecret() {
        return mSecret;
    }

    public void setSecret(String secret) {
        mSecret = secret;
    }

    public String getServer() {
        return mServer;
    }

    public void setServer(String server) {
        mServer = server;
    }

    public String getFarm() {
        return mFarm;
    }

    public void setFarm(String farm) {
        mFarm = farm;
    }

    public String getPhotoUrl () {
        return PHOTO_URL_DOMAIN1 + getFarm() + PHOTO_URL_DOMAIN2 + getServer() + "/"
                + getId() + "_" + getSecret() + "." + PHOTO_EXTENSION;
    }

    @Override
    public int compareTo(Photo p) {
        return 0;
    }
}
