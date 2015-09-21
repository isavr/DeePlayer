package com.tutorial.deeplayer.app.deeplayer.repository.datasources;

import android.content.ContentValues;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.pojo.BaseTypedItem;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService_Factory;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class TrackDataStore extends BaseLocalDataStore {
    private final String TAG = RadioDataStore.class.getSimpleName();

    private Observable<Track> getUserTracksDataObservable(int index) {
        return RestService_Factory.create().get().fetchUserTracks(index)
                .flatMap(item -> Observable.from(item.getUserData()));
    }

    private Observable<Track> getUserTracksDataObservable() {
        return RestService_Factory.create().get().fetchUserTracks()
                .flatMap(item -> {
                    int total = item.getTotal();
                    int index = 0;
                    Log.d(TAG, "total count - " + total);
                    if (total > RestService.INDEX_STEP_VAL) {
                        List<Observable<Track>> list = new ArrayList<>();
                        list.add(Observable.from(item.getUserData()));
                        for (int i = 1; i < total / RestService.INDEX_STEP_VAL + 1; ++i) {
                            index = i * RestService.INDEX_STEP_VAL;
                            Log.d(TAG, "try index - " + index);
                            Observable<Track> observable = getUserTracksDataObservable(index);
                            list.add(observable);
                        }
                        return Observable.merge(list);
                    } else {
                        return Observable.from(item.getUserData());
                    }
                });
    }

    private Observable<Track> getTrackDataObservable() {
        return RestService_Factory.create().get().fetchTracksRecommendedForUser()
                .flatMap(item -> Observable.from(item.getData()));
    }

    public Observable<List<ContentValues[]>> getRecommendedTracksWithStatuses() {
        Observable<Track> userTracks = getUserTracksDataObservable();
        Observable<Track> recommended = getTrackDataObservable().map(item -> {
                    item.setIsRecommended(true);
                    return item;
                }
        );
        return Observable.concat(recommended, userTracks).groupBy(BaseTypedItem::getId).flatMap(Observable::toList)
                .filter(item -> item.size() > 1).map(itemList -> {
                    int index = itemList.size() - 1;
                    Track track = itemList.get(index);
                    track.setFavourite(true);
                    track.setIsRecommended(true);
                    return track;
                }).concatWith(recommended).distinct(BaseTypedItem::getId)
                .map(item -> DataContract.TrackConverter.convertFrom(item))
                .toList();
    }

    public Observable<List<ContentValues[]>> getUserFavouriteTracks() {
        return getUserTracksDataObservable()
                .map(item -> DataContract.TrackConverter.convertFrom(item)).toList();
    }

    private Observable<Track> getChartedTracksDataObservable() {
        return RestService_Factory.create().get().fetchChartInfo().flatMap(item -> {
            if (item.getTracks() != null) {
                return Observable.from(item.getTracks().getData());
            }
            return Observable.empty();
        });
    }

    public Observable<List<ContentValues[]>> getChartedTracks() {
        Observable<Track> userAlbums = getUserTracksDataObservable();
        Observable<Track> charted = getChartedTracksDataObservable();
        return Observable.concat(charted, userAlbums).groupBy(BaseTypedItem::getId).flatMap(Observable::toList)
                .filter(item -> item.size() > 1).map(itemList -> {
                    final int index = 0;//itemList.size() - 1;
                    Track track = itemList.get(index);
                    track.setFavourite(true);
//                    album.setIsRecommended(true);
                    return track;
                }).concatWith(charted).distinct(BaseTypedItem::getId)
                .map(item -> DataContract.TrackConverter.convertFrom(item))
                .toList();
    }
}
