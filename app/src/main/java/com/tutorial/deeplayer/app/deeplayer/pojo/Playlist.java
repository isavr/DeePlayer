package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public class Playlist extends BaseItemWithPicture {
    private String title;
    private String link;
    private int duration; // in seconds
    @SerializedName("public")
    private boolean isPublic;
    @SerializedName("is_loved_track")
    private boolean isLovedTrack;
    @SerializedName("collaborative")
    private boolean isCollaborative;
    @SerializedName("nb_tracks")
    private int tracksNumber;
    private String tracklist;
    @SerializedName("user")
    private User creator;

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isLovedTrack() {
        return isLovedTrack;
    }

    public void setIsLovedTrack(boolean isLovedTrack) {
        this.isLovedTrack = isLovedTrack;
    }

    public boolean isCollaborative() {
        return isCollaborative;
    }

    public void setIsCollaborative(boolean isCollaborative) {
        this.isCollaborative = isCollaborative;
    }

    public int getTracksNumber() {
        return tracksNumber;
    }

    public void setTracksNumber(int tracksNumber) {
        this.tracksNumber = tracksNumber;
    }

    public String getTracklist() {
        return tracklist;
    }

    public void setTracklist(String tracklist) {
        this.tracklist = tracklist;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
