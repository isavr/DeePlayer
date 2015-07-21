package com.tutorial.deeplayer.app.deeplayer.errors;

/**
 * Created by ilya.savritsky on 20.07.2015.
 */
public class DeezerError extends Throwable {
    private String type;
    private String message;
    private int code;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
