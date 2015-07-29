package com.tutorial.deeplayer.app.deeplayer.pojo;

/**
 * Created by ilya.savritsky on 28.07.2015.
 */
public class FavouriteItem extends BaseTypedItem {
    private boolean favourite;

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
