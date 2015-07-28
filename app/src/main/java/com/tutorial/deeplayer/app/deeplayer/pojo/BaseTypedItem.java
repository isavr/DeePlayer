package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.annotations.SerializedName;
import com.tutorial.deeplayer.app.deeplayer.errors.DeezerError;

import nl.qbusict.cupboard.annotation.Ignore;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class BaseTypedItem {
    @Ignore
    private DeezerError error;
    @SerializedName("id")
    protected Long _id;
    protected String type;

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        this._id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DeezerError getError() {
        return null;
    }

    public void setError(DeezerError error) {
        this.error = error;
    }
}
