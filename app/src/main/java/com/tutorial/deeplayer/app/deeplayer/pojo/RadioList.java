package com.tutorial.deeplayer.app.deeplayer.pojo;

import java.util.List;

/**
 * Created by ilya.savritsky on 20.07.2015.
 */
public class RadioList extends BaseTypedItem {
    private List<Radio> data;

    public List<Radio> getData() {
        return data;
    }

    public void setData(List<Radio> data) {
        this.data = data;
    }

    public List<Radio> getUserData() {
        if (data != null) {
            for (Radio radio : data) {
                radio.setFavourite(true);
            }
        }
        return data;
    }
}
