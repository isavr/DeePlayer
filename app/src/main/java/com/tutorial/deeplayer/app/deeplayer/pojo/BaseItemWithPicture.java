package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class BaseItemWithPicture extends FavouriteItem {
    @SerializedName("picture")
    protected String picture;
    @SerializedName("picture_small")
    protected String pictureSmall;
    @SerializedName("picture_medium")
    protected String pictureMedium;
    @SerializedName("picture_big")
    protected String pictureBig;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseItemWithPicture)) return false;
        if (!super.equals(o)) return false;

        BaseItemWithPicture that = (BaseItemWithPicture) o;

        if (picture != null ? !picture.equals(that.picture) : that.picture != null) return false;
        if (pictureSmall != null ? !pictureSmall.equals(that.pictureSmall) : that.pictureSmall != null)
            return false;
        if (pictureMedium != null ? !pictureMedium.equals(that.pictureMedium) : that.pictureMedium != null)
            return false;
        return !(pictureBig != null ? !pictureBig.equals(that.pictureBig) : that.pictureBig != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (picture != null ? picture.hashCode() : 0);
        result = 31 * result + (pictureSmall != null ? pictureSmall.hashCode() : 0);
        result = 31 * result + (pictureMedium != null ? pictureMedium.hashCode() : 0);
        result = 31 * result + (pictureBig != null ? pictureBig.hashCode() : 0);
        return result;
    }


    @Override
    public String getPicture() {
        return picture;
    }

    @Override
    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String getPictureSmall() {
        return pictureSmall;
    }

    @Override
    public void setPictureSmall(String pictureSmall) {
        this.pictureSmall = pictureSmall;
    }

    @Override
    public String getPictureMedium() {
        return pictureMedium;
    }

    @Override
    public void setPictureMedium(String pictureMedium) {
        this.pictureMedium = pictureMedium;
    }

    @Override
    public String getPictureBig() {
        return pictureBig;
    }

    @Override
    public void setPictureBig(String pictureBig) {
        this.pictureBig = pictureBig;
    }
}
