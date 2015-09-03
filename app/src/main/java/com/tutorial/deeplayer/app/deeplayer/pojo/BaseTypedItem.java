package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.annotations.SerializedName;
import com.tutorial.deeplayer.app.deeplayer.errors.DeezerError;

//import nl.qbusict.cupboard.annotation.Ignore;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class BaseTypedItem {
//    @Ignore
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseTypedItem)) return false;

        BaseTypedItem that = (BaseTypedItem) o;

        if (error != null ? !error.equals(that.error) : that.error != null) return false;
        if (_id != null ? !_id.equals(that._id) : that._id != null) return false;
        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        int result = error != null ? error.hashCode() : 0;
        result = 31 * result + (_id != null ? _id.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
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
