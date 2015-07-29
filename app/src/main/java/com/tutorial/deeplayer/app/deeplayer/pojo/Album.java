package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class Album extends FavouriteItem {
    private String title;
    private String upc;
    private String link;
    private String share;
    private String cover;
    @SerializedName("cover_small")
    private String coverSmall;
    @SerializedName("cover_medium")
    private String coverMedium;
    @SerializedName("cover_big")
    private String coverBig;
    @SerializedName("genre_id")
    private int genreID;
    private GenresList genres;
    private String label;
    @SerializedName("nb_tracks")
    private
    int tracksCount;
    private int duration;
    @SerializedName("nb_fans")
    private int fansCount;
    private int rating;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("record_type")
    private String recordType;

    private List<Artist> contributors;

    private boolean available;
    @SerializedName("explicit_lyrics")
    private boolean hasExplicitLyrics;
    private Artist artist;
    private String tracklist;
    private AlbumTrackList tracks;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCoverSmall() {
        return coverSmall;
    }

    public void setCoverSmall(String coverSmall) {
        this.coverSmall = coverSmall;
    }

    public String getCoverMedium() {
        return coverMedium;
    }

    public void setCoverMedium(String coverMedium) {
        this.coverMedium = coverMedium;
    }

    public String getCoverBig() {
        return coverBig;
    }

    public void setCoverBig(String coverBig) {
        this.coverBig = coverBig;
    }

    public int getGenreID() {
        return genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public GenresList getGenres() {
        return genres;
    }

    public void setGenres(GenresList genres) {
        this.genres = genres;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getTracksCount() {
        return tracksCount;
    }

    public void setTracksCount(int tracksCount) {
        this.tracksCount = tracksCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public List<Artist> getContributors() {
        return contributors;
    }

    public void setContributors(List<Artist> contributors) {
        this.contributors = contributors;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isHasExplicitLyrics() {
        return hasExplicitLyrics;
    }

    public void setHasExplicitLyrics(boolean hasExplicitLyrics) {
        this.hasExplicitLyrics = hasExplicitLyrics;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getTracklist() {
        return tracklist;
    }

    public void setTracklist(String tracklist) {
        this.tracklist = tracklist;
    }

    public AlbumTrackList getTracks() {
        return tracks;
    }

    public void setTracks(AlbumTrackList tracks) {
        this.tracks = tracks;
    }
}
