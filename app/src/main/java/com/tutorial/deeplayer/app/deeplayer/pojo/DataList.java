package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ilya.savritsky on 28.07.2015.
 */
public class DataList<T extends FavouriteItem> extends BaseTypedItem {
    @SerializedName("data")
    private List<T> data;
    @SerializedName("total")
    private int total;

    public List<T> getUserData() {
        if (data != null) {
            for (T item : data) {
                item.setFavourite(true);
            }
        }
        return data;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
