package com.tutorial.deeplayer.app.deeplayer.pojo;

import java.util.List;

/**
 * Created by ilya.savritsky on 22.07.2015.
 */
public class RadioGenre extends BaseTypedItem {
    private String title;
    private List<Radio> radios;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Radio> getRadios() {
        return radios;
    }

    public void setRadios(List<Radio> radios) {
        this.radios = radios;
    }
}
