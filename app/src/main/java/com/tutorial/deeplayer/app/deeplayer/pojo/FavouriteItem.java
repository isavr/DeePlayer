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

    public String getTitle() {
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavouriteItem)) return false;
        if (!super.equals(o)) return false;

        FavouriteItem that = (FavouriteItem) o;

        return favourite == that.favourite;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (favourite ? 1 : 0);
        return result;
    }
}
