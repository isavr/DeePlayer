package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.BaseTypedItem;
import com.tutorial.deeplayer.app.deeplayer.pojo.DataList;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
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
 * Created by ilya.savritsky on 17.08.2015.
 */
public class RecommendedTrackViewModel extends AbstractViewModel {
    private static final String TAG = RecommendedAlbumsViewModel.class.getSimpleName();
    private final BehaviorSubject subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        compositeSubscription.add(getRecommendedAlbumsWithStatuses());
    }

    private Subscription getRecommendedAlbumsWithStatuses() {
        Observer<List<ContentValues[]>> trackObserver = new Observer<List<ContentValues[]>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Tracks");
                subject.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                subject.onError(e);
            }

            @Override
            public void onNext(List<ContentValues[]> tracks) {
                subject.onNext(tracks);
                final Context context = DeePlayerApp.get().getApplicationContext();
//                ContentValues[] values = new ContentValues[tracks.size()];
//                values = tracks.toArray(values);
                ArrayList<ContentProviderOperation> operations = new ArrayList<>();
                for (ContentValues[] data : tracks) {
                    operations.add(ContentProviderOperation.newInsert(SchematicDataProvider.Tracks.CONTENT_URI).
                            withValues(data[DataContract.getTrackIndex()]).build());
                    operations.add(ContentProviderOperation.newInsert(SchematicDataProvider.Artists.CONTENT_URI).
                            withValues(data[DataContract.getArtistIndex()]).build());
                    operations.add(ContentProviderOperation.newInsert(SchematicDataProvider.Albums.CONTENT_URI).
                            withValues(data[DataContract.getAlbumIndex()]).build());
                }
                try {
                    context.getContentResolver().applyBatch(SchematicDataProvider.AUTHORITY, operations);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }
                //context.getContentResolver().bulkInsert(SchematicDataProvider.Tracks.CONTENT_URI, values);
//                context.getContentResolver().delete(SchematicDataProvider.Albums.CONTENT_URI, null, null);
//                for (ContentValues data : tracks) {
//                    ContentValues album = data[DataContract.AlbumConverter.KEY_ALBUM];
//                    ContentValues artist = data[DataContract.AlbumConverter.KEY_ARTIST];
////                    long artistId = artist.getAsLong(ArtistColumns.ID);
////                    Cursor artistCursor = context.getContentResolver().query(SchematicDataProvider.Artists.withId(artistId),
////                            null, null, null, null);
////                    if (artistCursor == null || artistCursor.getCount() == 0) {
//                    context.getContentResolver().insert(SchematicDataProvider.Artists.CONTENT_URI, artist);
////                    }
//                    context.getContentResolver().insert(SchematicDataProvider.Albums.CONTENT_URI, album);
//                }
            }
        };
        RestService service = getRestService();
        Observable<Track> userTracks = getUserTracksDataObservable(service);
        Observable<Track> recommended = getTrackDataObservable(service).map(item -> {
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
                .toList().observeOn(AndroidSchedulers.mainThread()).subscribe(trackObserver);
    }

    private Observable<Track> getTrackDataObservable(RestService service) {
        return warpToIoThread(service.fetchTracksRecommendedForUser())
                .flatMap(item -> Observable.from(item.getData()));
    }

    private Observable<Track> getUserTracksDataObservable(RestService service, int index) {
        return warpToIoThread(service.fetchUserTracks(index))
                .flatMap(item -> Observable.from(item.getData()));
    }

    private Observable<Track> getUserTracksDataObservable(RestService service) {
        return warpToIoThread(service.fetchUserTracks())
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
                            Observable<Track> observable = getUserTracksDataObservable(service, index);
                            list.add(observable);
                        }
                        return Observable.merge(list);
                    } else {
                        return Observable.from(item.getUserData());
                    }
                });
    }

    protected Observable<DataList<Track>> warpToIoThread(Observable<DataList<Track>> dataObservable) {
        return dataObservable.subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> addTrackToFavourite(Track track) {
        return changeTrackFavouriteStatus(track, true);
    }

    public Observable<Boolean> removeTrackFromFavourite(Track track) {
        return changeTrackFavouriteStatus(track, false);
    }

    private Observable<Boolean> changeTrackFavouriteStatus(Track track, boolean isChecked) {
        if (isChecked) {
            return getRestService().fetchResultTrackAddToFavourite(track.getId());
        } else {
            return getRestService().fetchResultTrackRemoveFromFavourite(track.getId());
        }
    }

    public BehaviorSubject getSubject() {
        return subject;
    }
}
