package com.tutorial.deeplayer.app.deeplayer.repository.datasources;

import android.content.ContentValues;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.pojo.BaseTypedItem;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService_Factory;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class ArtistDataStore extends BaseLocalDataStore {
    private final String TAG = ArtistDataStore.class.getSimpleName();

    private Observable<Artist> getUserArtistsDataObservable(int index) {
        return RestService_Factory.create().get().fetchUserArtists(index)
                .flatMap(item -> Observable.from(item.getUserData()));
    }

    private Observable<Artist> getUserArtistsDataObservable() {
        return RestService_Factory.create().get().fetchUserArtists()
                .flatMap(item -> {
                    int total = item.getTotal();
                    int index = 0;
                    Log.d(TAG, "total count - " + total);
                    if (total > RestService.INDEX_STEP_VAL) {
                        List<Observable<Artist>> list = new ArrayList<>();
                        list.add(Observable.from(item.getUserData()));
                        for (int i = 1; i < total / RestService.INDEX_STEP_VAL + 1; ++i) {
                            index = i * RestService.INDEX_STEP_VAL;
                            Log.d(TAG, "try index - " + index);
                            Observable<Artist> observable = getUserArtistsDataObservable(index);
                            list.add(observable);
                        }
                        return Observable.merge(list);
                    } else {
                        return Observable.from(item.getUserData());
                    }
                });
    }

    private Observable<Artist> getArtistDataObservable() {
        return RestService_Factory.create().get().fetchArtistsRecommendedForUser()
                .flatMap(item -> Observable.from(item.getData()));
    }

    public Observable<List<ContentValues>> getFavouriteArtists() {
        return getUserArtistsDataObservable().map(item -> DataContract.ArtistConverter.convertFrom(item))
                .toList();
    }

    public Observable<List<ContentValues>> getRecommendedArtists() {
        Observable<Artist> recommended = getArtistDataObservable().map(item -> {
                    item.setIsRecommended(true);
                    return item;
                }
        );
        Observable<Artist> userArtists = getUserArtistsDataObservable();
        return Observable.concat(recommended, userArtists).groupBy(BaseTypedItem::getId).flatMap(Observable::toList)
                .filter(item -> item.size() > 1).map(itemList -> {
                    int index = itemList.size() - 1;
                    Artist artist = itemList.get(index);
                    artist.setFavourite(true);
                    artist.setIsRecommended(true);
                    return artist;
                }).concatWith(recommended).distinct(BaseTypedItem::getId)
                .map(item -> DataContract.ArtistConverter.convertFrom(item))
                .toList();
    }
}
