package com.tutorial.deeplayer.app.deeplayer.repository;

import android.content.ContentValues;

import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 18.09.2015.
 */
public interface PlaylistRepository {
    Observable<List<ContentValues[]>> getChartedPlaylistsFromNet();
}
