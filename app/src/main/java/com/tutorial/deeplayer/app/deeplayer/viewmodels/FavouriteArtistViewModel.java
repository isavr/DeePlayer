package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.data.tables.ArtistColumns;
import com.tutorial.deeplayer.app.deeplayer.interactions.GetUserFavouriteArtists;
import com.tutorial.deeplayer.app.deeplayer.interactions.UseCase;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 04.09.2015.
 */
public class FavouriteArtistViewModel  extends AbstractViewModel {
    private static final String TAG = FavouriteArtistViewModel.class.getSimpleName();
    private final BehaviorSubject subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        UseCase useCase = new GetUserFavouriteArtists();
        compositeSubscription.add(useCase.build(Schedulers.computation(),
                AndroidSchedulers.mainThread(), getArtistObserver()));
    }

    protected  Observer<List<ContentValues>> getArtistObserver() {
        return new Observer<List<ContentValues>>() {
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
                int deleted = context.getContentResolver().delete(SchematicDataProvider.Artists.CONTENT_URI,
                        ArtistColumns.IS_RECOMMENDED + "=1 OR " + ArtistColumns.IS_FAVOURITE + "=1", null);
                Log.d(TAG, "Rows deleted -> " + deleted);
                int insertedRows = context.getContentResolver().bulkInsert(SchematicDataProvider.Artists.CONTENT_URI,
                        values);
                Log.d(TAG, "Rows inserted -> " + insertedRows);
            }
        };
    }

    public Observable<Boolean> addArtistToFavourite(Artist artist) {
        return changeArtistFavouriteStatus(artist, true);
    }

    public Observable<Boolean> removeArtistFromFavourite(Artist artist) {
        return changeArtistFavouriteStatus(artist, false);
    }

    protected Observable<Boolean> changeArtistFavouriteStatus(Artist artist, boolean isChecked) {
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
