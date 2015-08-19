package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class Track extends FavouriteItem {
    private boolean readable;
    private String title;
    @SerializedName("title_short")
    private
    String shortTitle;
    @SerializedName("title_version")
    private
    String titleVersion;
    private String isrc;
    @SerializedName("link")
    private String deezerURL;
    private String share;
    private int duration; // in seconds
    private int trackPosition;
    private int diskNumber;
    private String releaseDate;
    @SerializedName("explicit_lyrics")
    private boolean hasExplicitLyrics;
    private String preview;
    private List<Artist> contributors;
    private Artist artist;
    private Album album;

    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getTitleVersion() {
        return titleVersion;
    }

    public void setTitleVersion(String titleVersion) {
        this.titleVersion = titleVersion;
    }

    public String getIsrc() {
        return isrc;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public String getDeezerURL() {
        return deezerURL;
    }

    public void setDeezerURL(String deezerURL) {
        this.deezerURL = deezerURL;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTrackPosition() {
        return trackPosition;
    }

    public void setTrackPosition(int trackPosition) {
        this.trackPosition = trackPosition;
    }

    public int getDiskNumber() {
        return diskNumber;
    }

    public void setDiskNumber(int diskNumber) {
        this.diskNumber = diskNumber;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isHasExplicitLyrics() {
        return hasExplicitLyrics;
    }

    public void setHasExplicitLyrics(boolean hasExplicitLyrics) {
        this.hasExplicitLyrics = hasExplicitLyrics;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public List<Artist> getContributors() {
        return contributors;
    }

    public void setContributors(List<Artist> contributors) {
        this.contributors = contributors;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
