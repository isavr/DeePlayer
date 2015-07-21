package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.tutorial.deeplayer.app.deeplayer.errors.DeezerError;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class BaseTypedItem {
    private DeezerError error;
    protected int id;
    protected String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DeezerError getError() {
        return error;
    }

    public void setError(DeezerError error) {
        this.error = error;
    }
}
