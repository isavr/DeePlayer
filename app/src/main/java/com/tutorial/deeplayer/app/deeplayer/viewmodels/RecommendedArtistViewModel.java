package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.data.tables.ArtistColumns;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.pojo.BaseTypedItem;
import com.tutorial.deeplayer.app.deeplayer.pojo.DataList;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 30.07.2015.
 */
public class RecommendedArtistViewModel extends AbstractViewModel {
    private static final String TAG = RecommendedArtistViewModel.class.getSimpleName();
    private final BehaviorSubject subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        compositeSubscription.add(getRecommendedArtistsWithStatuses());
    }

    private Subscription getRecommendedArtistsWithStatuses() {
        Observer<List<ContentValues>> artistObserver = new Observer<List<ContentValues>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Artist");
                subject.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                subject.onError(e);
            }

            @Override
            public void onNext(List<ContentValues> valuesList) {
                subject.onNext(valuesList);
                final Context context = DeePlayerApp.get().getApplicationContext();
                ContentValues[] values = new ContentValues[valuesList.size()];
                values = valuesList.toArray(values);
//                int deleted = context.getContentResolver().delete(SchematicDataProvider.Artists.CONTENT_URI,
//                        null, null);
//                Log.d(TAG, "Rows deleted -> " + deleted);
                int insertedRows = context.getContentResolver().bulkInsert(SchematicDataProvider.Artists.CONTENT_URI,
                        values);
                Log.d(TAG, "Rows inserted -> " + insertedRows);
            }
        };

        RestService service = getRestService();
        Observable<Artist> recommended = getArtistDataObservable(service).map(item -> {
                    item.setIsRecommended(true);
                    return item;
                }
        );
        Observable<Artist> userArtists = getUserArtistsDataObservable(service);
        return Observable.concat(recommended, userArtists).groupBy(BaseTypedItem::getId).flatMap(Observable::toList)
                .filter(item -> item.size() > 1).map(itemList -> {
                    int index = itemList.size() - 1;
                    Artist artist = itemList.get(index);
                    artist.setFavourite(true);
                    artist.setIsRecommended(true);
                    return artist;
                }).concatWith(recommended).distinct(BaseTypedItem::getId)
                .map(item -> DataContract.ArtistConverter.convertFrom(item))
                .toList().observeOn(AndroidSchedulers.mainThread()).subscribe(artistObserver);
    }

    private Observable<Artist> getArtistDataObservable(RestService service) {
        return warpToIoThread(service.fetchArtistsRecommendedForUser())
                .flatMap(item -> Observable.from(item.getData()));
    }

    private Observable<Artist> getUserArtistsDataObservable(RestService service, int index) {
        return warpToIoThread(service.fetchUserArtists(index))
                .flatMap(item -> Observable.from(item.getData()));
    }

    private Observable<Artist> getUserArtistsDataObservable(RestService service) {
        return warpToIoThread(service.fetchUserArtists())
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
                            Observable<Artist> observable = getUserArtistsDataObservable(service, index);
                            list.add(observable);
                        }
                        return Observable.merge(list);
                    } else {
                        return Observable.from(item.getUserData());
                    }
                });
    }

    protected Observable<DataList<Artist>> warpToIoThread(Observable<DataList<Artist>> dataObservable) {
        return dataObservable.subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> addArtistToFavourite(Artist artist) {
        return changeArtistFavouriteStatus(artist, true);
    }

    public Observable<Boolean> removeArtistFromFavourite(Artist artist) {
        return changeArtistFavouriteStatus(artist, false);
    }

    private Observable<Boolean> changeArtistFavouriteStatus(Artist artist, boolean isChecked) {
        if (isChecked) {
            return getRestService().fetchResultArtistAddToFavourite(artist.getId());
        } else {
            return getRestService().fetchResultArtistRemoveFromFavourite(artist.getId());
        }
    }

    public BehaviorSubject getSubject() {
        return subject;
    }
}
