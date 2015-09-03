package com.tutorial.deeplayer.app.deeplayer.pojo;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class Radio extends BaseItemWithPicture {
    private String title;
    private String description;
    private String share;
    private String tracklist;
    private long time_add;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Radio)) return false;
        if (!super.equals(o)) return false;

        Radio radio = (Radio) o;

        if (time_add != radio.time_add) return false;
        if (title != null ? !title.equals(radio.title) : radio.title != null) return false;
        if (description != null ? !description.equals(radio.description) : radio.description != null)
            return false;
        if (share != null ? !share.equals(radio.share) : radio.share != null) return false;
        return !(tracklist != null ? !tracklist.equals(radio.tracklist) : radio.tracklist != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (share != null ? share.hashCode() : 0);
        result = 31 * result + (tracklist != null ? tracklist.hashCode() : 0);
        result = 31 * result + (int) (time_add ^ (time_add >>> 32));
        return result;
    }


    @Override
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

    public long getTime_add() {
        return time_add;
    }

    public void setTime_add(long time_add) {
        this.time_add = time_add;
    }
}
