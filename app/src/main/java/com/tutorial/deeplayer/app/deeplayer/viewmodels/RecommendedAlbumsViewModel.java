package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
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
 * Created by ilya.savritsky on 28.07.2015.
 */
public class RecommendedAlbumsViewModel extends AbstractViewModel {
    private static final String TAG = RecommendedAlbumsViewModel.class.getSimpleName();
    private final BehaviorSubject subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        compositeSubscription.add(getRecommendedAlbumsWithStatuses());
    }

    private Subscription getRecommendedAlbumsWithStatuses() {
        Observer<List<ContentValues[]>> albumObserver = new Observer<List<ContentValues[]>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Album");
                subject.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                subject.onError(e);
            }

            @Override
            public void onNext(List<ContentValues[]> albums) {
                subject.onNext(albums);
                final Context context = DeePlayerApp.get().getApplicationContext();
//                context.getContentResolver().delete(SchematicDataProvider.Albums.CONTENT_URI, null, null);
                for (ContentValues[] data : albums) {
                    ContentValues album = data[DataContract.getAlbumIndex()];
                    ContentValues artist = data[DataContract.getArtistIndex()];
//                    long artistId = artist.getAsLong(ArtistColumns.ID);
//                    Cursor artistCursor = context.getContentResolver().query(SchematicDataProvider.Artists.withId(artistId),
//                            null, null, null, null);
//                    if (artistCursor == null || artistCursor.getCount() == 0) {
                    context.getContentResolver().insert(SchematicDataProvider.Artists.CONTENT_URI, artist);
//                    }
                    context.getContentResolver().insert(SchematicDataProvider.Albums.CONTENT_URI, album);
                }
//                int insertedRows = context.getContentResolver().bulkInsert(SchematicDataProvider.Albums.CONTENT_URI,
//                        values);
            }
        };
        RestService service = getRestService();
        Observable<Album> userAlbums = getUserAlbumsDataObservable(service);
        Observable<Album> recommended = getAlbumDataObservable(service).map(item -> {
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
                .toList().observeOn(AndroidSchedulers.mainThread()).subscribe(albumObserver);
    }

    private Observable<Album> getAlbumDataObservable(RestService service) {
        return warpToIoThread(service.fetchAlbumsRecommendedForUser())
                .flatMap(item -> Observable.from(item.getData()));
    }

    private Observable<Album> getUserAlbumsDataObservable(RestService service, int index) {
        return warpToIoThread(service.fetchUserAlbums(index))
                .flatMap(item -> Observable.from(item.getData()));
    }

    private Observable<Album> getUserAlbumsDataObservable(RestService service) {
        return warpToIoThread(service.fetchUserAlbums())
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
                            Observable<Album> observable = getUserAlbumsDataObservable(service, index);
                            list.add(observable);
                        }
                        return Observable.merge(list);
                    } else {
                        return Observable.from(item.getUserData());
                    }
                });
    }

    protected Observable<DataList<Album>> warpToIoThread(Observable<DataList<Album>> dataObservable) {
        return dataObservable.subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> addAlbumToFavourite(Album album) {
        return changeAlbumFavouriteStatus(album, true);
    }

    public Observable<Boolean> removeAlbumFromFavourite(Album album) {
        return changeAlbumFavouriteStatus(album, false);
    }

    private Observable<Boolean> changeAlbumFavouriteStatus(Album album, boolean isChecked) {
        if (isChecked) {
            return getRestService().fetchResultAlbumAddToFavourite(album.getId());
        } else {
            return getRestService().fetchResultAlbumRemoveFromFavourite(album.getId());
        }
    }

    public BehaviorSubject getSubject() {
        return subject;
    }
}
