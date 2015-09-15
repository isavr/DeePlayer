package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.data.tables.ArtistColumns;
import com.tutorial.deeplayer.app.deeplayer.interactions.GetUserFavouriteAlbums;
import com.tutorial.deeplayer.app.deeplayer.interactions.UseCase;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 04.09.2015.
 */
public class FavouriteAlbumsViewModel extends AbstractViewModel {
    private static final String TAG = FavouriteAlbumsViewModel.class.getSimpleName();
    private final BehaviorSubject subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        UseCase useCase = new GetUserFavouriteAlbums();
        compositeSubscription.add(useCase.build(Schedulers.computation(),
                AndroidSchedulers.mainThread(), getAlbumObserver()));
    }

    protected Observer<List<ContentValues[]>> getAlbumObserver() {
        return new Observer<List<ContentValues[]>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Albums");
                subject.onCompleted();
                final Context context = DeePlayerApp.get().getApplicationContext();
                context.getContentResolver().notifyChange(SchematicDataProvider.Albums.recommendedQueryWithArtists(true), null);
                context.getContentResolver().notifyChange(SchematicDataProvider.Albums.queryWithArtists(true), null);

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

                Cursor artistIds = context.getContentResolver().query(SchematicDataProvider.Artists.CONTENT_URI,
                        new String[]{
                                ArtistColumns.ID,
                        }, null, null, null);
                int artistCount = artistIds.getCount();
                Set<Long> existingArtists = new HashSet<>(artistCount);
                if (artistIds.moveToFirst()) {
                    final int columnIndex = artistIds.getColumnIndex(ArtistColumns.ID);
                    for (int i = 0; i < artistCount; ++i) {
                        final int artistId = artistIds.getInt(columnIndex);
                        existingArtists.add((long) artistId);
                        artistIds.moveToNext();
                    }
                }
                artistIds.close();

                ArrayList<ContentProviderOperation> operations = new ArrayList<>();
                operations.add(ContentProviderOperation.newDelete(SchematicDataProvider.Albums.CONTENT_URI).build());

                for (ContentValues[] data : albums) {
                    ContentValues album = data[DataContract.getAlbumIndex()];
                    ContentValues artist = data[DataContract.getArtistIndex()];
                    Long artistId = (Long) artist.get(ArtistColumns.ID);
                    if (!existingArtists.contains(artistId)) {
                        operations.add(ContentProviderOperation.newInsert(
                                SchematicDataProvider.Artists.CONTENT_URI).withValues(artist).build());
                    }
                    operations.add(ContentProviderOperation.newInsert(
                            SchematicDataProvider.Albums.CONTENT_URI).withValues(album).build());
                }

                try {
                    context.getContentResolver().applyBatch(SchematicDataProvider.AUTHORITY, operations);
                    Log.d(TAG, "applying batch size -> " + operations.size());
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public Observable<Boolean> addAlbumToFavourite(Album album) {
        return changeAlbumFavouriteStatus(album, true);
    }

    public Observable<Boolean> removeAlbumFromFavourite(Album album) {
        return changeAlbumFavouriteStatus(album, false);
    }

    protected Observable<Boolean> changeAlbumFavouriteStatus(Album album, boolean isChecked) {
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
