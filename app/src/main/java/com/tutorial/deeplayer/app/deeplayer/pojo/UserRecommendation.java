package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class UserRecommendation extends BaseTypedItem {
    String title;
    @SerializedName("public")
    boolean isPublic;
    @SerializedName("nb_count")
    String tracksCount;
    private URL link;
    private URL tracklist;
    private URL picture;
    @SerializedName("picture_small")
    private URL pictureSmall;
    @SerializedName("picture_medium")
    private URL pictureMedium;
    @SerializedName("picture_big")
    private URL pictureBig;
    @SerializedName("timestamp")
    long timeStamp;
    User user;
    @SerializedName("artist")
    User artist;
    boolean radio;

}
