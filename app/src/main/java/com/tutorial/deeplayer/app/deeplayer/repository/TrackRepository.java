package com.tutorial.deeplayer.app.deeplayer.repository;

import android.content.ContentValues;

import com.tutorial.deeplayer.app.deeplayer.pojo.Track;

import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public interface TrackRepository {
    Observable<List<Track>> getTracksFromDB();
    Observable<List<ContentValues[]>> getTracksFromNet();
    Observable<List<ContentValues[]>> getFavouriteTracksFromNet();
}
