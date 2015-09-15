package com.tutorial.deeplayer.app.deeplayer.repository;

import android.content.ContentValues;

import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
import com.tutorial.deeplayer.app.deeplayer.repository.datasources.AlbumDataStore;
import com.tutorial.deeplayer.app.deeplayer.repository.datasources.ArtistDataStore;
import com.tutorial.deeplayer.app.deeplayer.repository.datasources.LocalAlbumDataStore;
import com.tutorial.deeplayer.app.deeplayer.repository.datasources.LocalArtistDataStore;
import com.tutorial.deeplayer.app.deeplayer.repository.datasources.LocalRadioDataStore;
import com.tutorial.deeplayer.app.deeplayer.repository.datasources.LocalTrackDataStore;
import com.tutorial.deeplayer.app.deeplayer.repository.datasources.RadioDataStore;
import com.tutorial.deeplayer.app.deeplayer.repository.datasources.TrackDataStore;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
@Singleton
public class UserDataRepository implements RadioRepository, TrackRepository, ArtistRepository, AlbumRepository {

    @Inject
    public UserDataRepository() {
    }

    @Override
    public Observable<List<Radio>> getRadiosFromDB() {
        final LocalRadioDataStore radioStore = new LocalRadioDataStore();
        return radioStore.getRadios();
    }

    @Override
    public Observable<List<ContentValues>> getRadiosFromNet() {
        final RadioDataStore radioStore = new RadioDataStore();
        return radioStore.getRadiosWithStatusesObservable();
    }

    @Override
    public Observable<List<ContentValues>> getAllRadiosDBComposingWithNet() {
        final RadioDataStore radioStore = new RadioDataStore();
        final LocalRadioDataStore localRadioStore = new LocalRadioDataStore();
        return radioStore.getAllRadiosObservable(localRadioStore.getRadiosItems());
    }

    @Override
    public Observable<List<ContentValues>> getFavouriteRadiosFromNet() {
        final RadioDataStore radioStore = new RadioDataStore();
        return radioStore.getUserFavouriteRadios();
    }

    @Override
    public Observable<List<Track>> getTracksFromDB() {
        final LocalTrackDataStore trackDataStore = new LocalTrackDataStore();
        return trackDataStore.getTracks();
    }

    @Override
    public Observable<List<ContentValues[]>> getTracksFromNet() {
        final TrackDataStore trackDataStore = new TrackDataStore();
        return trackDataStore.getRecommendedTracksWithStatuses();
    }

    @Override
    public Observable<List<ContentValues[]>> getFavouriteTracksFromNet() {
        final TrackDataStore trackDataStore = new TrackDataStore();
        return trackDataStore.getUserFavouriteTracks();
    }

    @Override
    public Observable<List<Artist>> getArtistsFromDB() {
        final LocalArtistDataStore trackDataStore = new LocalArtistDataStore();
        return trackDataStore.getArtists();
    }

    @Override
    public Observable<List<ContentValues>> getArtistsFromNet() {
        final ArtistDataStore trackDataStore = new ArtistDataStore();
        return trackDataStore.getRecommendedArtists();
    }

    @Override
    public Observable<List<ContentValues>> getFavouriteArtistsFromNet() {
        final ArtistDataStore trackDataStore = new ArtistDataStore();
        return trackDataStore.getFavouriteArtists();
    }

    @Override
    public Observable<List<Album>> getAlbumsFromDB() {
        final LocalAlbumDataStore trackDataStore = new LocalAlbumDataStore();
        return trackDataStore.getAlbums();
    }

    @Override
    public Observable<List<ContentValues[]>> getAlbumsFromNet() {
        final AlbumDataStore trackDataStore = new AlbumDataStore();
        return trackDataStore.getRecommendedAlbums();
    }

    @Override
    public Observable<List<ContentValues[]>> getFavouriteAlbumsFromNet() {
        final AlbumDataStore trackDataStore = new AlbumDataStore();
        return trackDataStore.getFavouriteAlbums();
    }
}
