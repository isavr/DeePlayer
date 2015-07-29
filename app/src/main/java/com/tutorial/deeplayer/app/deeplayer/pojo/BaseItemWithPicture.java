package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class BaseItemWithPicture extends FavouriteItem {
    protected String picture;
    @SerializedName("picture_small")
    protected String pictureSmall;
    @SerializedName("picture_medium")
    protected String pictureMedium;
    @SerializedName("picture_big")
    protected String pictureBig;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPictureSmall() {
        return pictureSmall;
    }

    public void setPictureSmall(String pictureSmall) {
        this.pictureSmall = pictureSmall;
    }

    public String getPictureMedium() {
        return pictureMedium;
    }

    public void setPictureMedium(String pictureMedium) {
        this.pictureMedium = pictureMedium;
    }

    public String getPictureBig() {
        return pictureBig;
    }

    public void setPictureBig(String pictureBig) {
        this.pictureBig = pictureBig;
    }
}
