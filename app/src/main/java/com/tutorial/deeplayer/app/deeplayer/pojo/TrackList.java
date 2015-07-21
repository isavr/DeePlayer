package com.tutorial.deeplayer.app.deeplayer.pojo;

import java.util.List;

/**
 * Created by ilya.savritsky on 20.07.2015.
 */
public class TrackList extends BaseTypedItem {
    private List<Track> data;

    public List<Track> getData() {
        return data;
    }

    public void setData(List<Track> data) {
        this.data = data;
    }
}
