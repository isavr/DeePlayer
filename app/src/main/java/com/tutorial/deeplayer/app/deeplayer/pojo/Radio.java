package com.tutorial.deeplayer.app.deeplayer.pojo;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class Radio extends BaseItemWithPicture {
    private String title;
    private String description;
    private String share;
    private String tracklist;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getTracklist() {
        return tracklist;
    }

    public void setTracklist(String tracklist) {
        this.tracklist = tracklist;
    }
}
