package com.tutorial.deeplayer.app.deeplayer.repository;

import android.content.ContentValues;

import com.tutorial.deeplayer.app.deeplayer.pojo.Album;

import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public interface AlbumRepository {
    Observable<List<Album>> getAlbumsFromDB();

    Observable<List<ContentValues[]>> getAlbumsFromNet();

    Observable<List<ContentValues[]>> getFavouriteAlbumsFromNet();

    Observable<List<ContentValues[]>> getChartedAlbumsFromNet();
}
