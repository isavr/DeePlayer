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
import com.tutorial.deeplayer.app.deeplayer.data.tables.AlbumColumns;
import com.tutorial.deeplayer.app.deeplayer.data.tables.ArtistColumns;
import com.tutorial.deeplayer.app.deeplayer.interactions.GetUserFavouriteTracks;
import com.tutorial.deeplayer.app.deeplayer.interactions.UseCase;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;

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
 * Created by ilya.savritsky on 07.09.2015.
 */
public class FavouriteTracksViewModel extends AbstractViewModel {
    private static final String TAG = FavouriteTracksViewModel.class.getSimpleName();
    private final BehaviorSubject subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        UseCase useCase = new GetUserFavouriteTracks();
        compositeSubscription.add(useCase.build(Schedulers.computation(),
                AndroidSchedulers.mainThread(), getTracksObserver()));
    }

    protected Observer<List<ContentValues[]>> getTracksObserver() {
        return new Observer<List<ContentValues[]>>() {
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
                Cursor albumIds = context.getContentResolver().query(SchematicDataProvider.Albums.CONTENT_URI,
                        new String[]{
                                AlbumColumns.ID,
                        }, null, null, null);
                int albumCount = albumIds.getCount();
                Set<Long> existingAlbums = new HashSet<>(albumCount);
                if (albumIds.moveToFirst()) {
                    final int columnIndex = albumIds.getColumnIndex(AlbumColumns.ID);
                    for (int i = 0; i < albumCount; ++i) {
                        final int albumId = albumIds.getInt(columnIndex);
                        existingAlbums.add(Long.valueOf(albumId));
                        albumIds.moveToNext();
                    }
                }
                albumIds.close();
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
                        existingArtists.add(Long.valueOf(artistId));
                        artistIds.moveToNext();
                    }
                }
                artistIds.close();
                ArrayList<ContentProviderOperation> operations = new ArrayList<>();
                operations.add(ContentProviderOperation.newDelete(SchematicDataProvider.Tracks.CONTENT_URI).build());
                for (ContentValues[] data : tracks) {
                    ContentValues artist = data[DataContract.getArtistIndex()];
                    ContentValues album = data[DataContract.getAlbumIndex()];
                    ContentValues track = data[DataContract.getTrackIndex()];
                    operations.add(ContentProviderOperation.newInsert(SchematicDataProvider.Tracks.CONTENT_URI).
                            withValues(track).build());
                    Long artistId = (Long) artist.get(ArtistColumns.ID);
                    if (!existingArtists.contains(artistId)) {
                        operations.add(ContentProviderOperation.newInsert(SchematicDataProvider.Artists.CONTENT_URI).
                                withValues(artist).build());
                    }
                    Long albumId = (Long) album.get(AlbumColumns.ID);
                    if (!existingAlbums.contains(albumId)) {
                        operations.add(ContentProviderOperation.newInsert(SchematicDataProvider.Albums.CONTENT_URI).
                                withValues(album).build());
                    }
                }
                existingAlbums.clear();
                existingArtists.clear();
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