package com.tutorial.deeplayer.app.deeplayer.pojo;

import com.tutorial.deeplayer.app.deeplayer.errors.DeezerError;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public class Chart {
    private DeezerError error;
    private DataList<Track> tracks;
    private DataList<Album> albums;
    private DataList<Artist> artists;
    private DataList<Playlist> playlists;

    public DeezerError getError() {
        return error;
    }

    public void setError(DeezerError error) {
        this.error = error;
    }

    public DataList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(DataList<Track> tracks) {
        this.tracks = tracks;
    }

    public DataList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(DataList<Album> albums) {
        this.albums = albums;
    }

    public DataList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(DataList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public DataList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(DataList<Artist> artists) {
        this.artists = artists;
    }
}
