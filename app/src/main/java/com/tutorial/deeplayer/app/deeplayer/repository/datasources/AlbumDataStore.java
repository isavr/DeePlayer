package com.tutorial.deeplayer.app.deeplayer.repository.datasources;

import android.content.ContentValues;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.pojo.BaseTypedItem;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService_Factory;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class AlbumDataStore extends BaseLocalDataStore {
    private final String TAG = AlbumDataStore.class.getSimpleName();

    public Observable<List<ContentValues[]>> getFavouriteAlbums() {
        return getUserAlbumsDataObservable().map(item -> DataContract.AlbumConverter.convertFrom(item))
                .toList();
    }

    private Observable<Album> getUserAlbumsDataObservable(int index) {
        return RestService_Factory.create().get().fetchUserAlbums(index)
                .flatMap(item -> Observable.from(item.getUserData()));
    }

    private Observable<Album> getUserAlbumsDataObservable() {
        return RestService_Factory.create().get().fetchUserAlbums()
                .flatMap(item -> {
                    int total = item.getTotal();
                    int index = 0;
                    Log.d(TAG, "total count - " + total);
                    if (total > RestService.INDEX_STEP_VAL) {
                        List<Observable<Album>> list = new ArrayList<>();
                        list.add(Observable.from(item.getUserData()));
                        for (int i = 1; i < total / RestService.INDEX_STEP_VAL + 1; ++i) {
                            index = i * RestService.INDEX_STEP_VAL;
                            Log.d(TAG, "try index - " + index);
                            Observable<Album> observable = getUserAlbumsDataObservable(index);
                            list.add(observable);
                        }
                        return Observable.merge(list);
                    } else {
                        return Observable.from(item.getUserData());
                    }
                });
    }

    public Observable<List<ContentValues[]>> getRecommendedAlbums() {
        Observable<Album> userAlbums = getUserAlbumsDataObservable();
        Observable<Album> recommended = getAlbumDataObservable().map(item -> {
                    item.setIsRecommended(true);
                    return item;
                }
        );
        return Observable.concat(recommended, userAlbums).groupBy(BaseTypedItem::getId).flatMap(Observable::toList)
                .filter(item -> item.size() > 1).map(itemList -> {
                    int index = itemList.size() - 1;
                    Album album = itemList.get(index);
                    album.setFavourite(true);
                    album.setIsRecommended(true);
                    return album;
                }).concatWith(recommended).distinct(BaseTypedItem::getId)
                .map(item -> DataContract.AlbumConverter.convertFrom(item))
                .toList();
    }

    private Observable<Album> getAlbumDataObservable() {
        return RestService_Factory.create().get().fetchAlbumsRecommendedForUser()
                .flatMap(item -> Observable.from(item.getData()));
    }
}
