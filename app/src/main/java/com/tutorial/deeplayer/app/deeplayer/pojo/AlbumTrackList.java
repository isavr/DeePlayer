package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ilya.savritsky on 20.07.2015.
 */
public class AlbumTrackList {
    @SerializedName("data")
    private List<Track> data;

    public List<Track> getData() {
        return data;
    }

    public void setData(List<Track> tracks) {
        this.data = tracks;
    }
}
