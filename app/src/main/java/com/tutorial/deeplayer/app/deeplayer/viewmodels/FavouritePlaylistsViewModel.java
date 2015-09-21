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
import com.tutorial.deeplayer.app.deeplayer.interactions.GetChartedPlaylists;
import com.tutorial.deeplayer.app.deeplayer.interactions.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 18.09.2015.
 */
public class FavouritePlaylistsViewModel extends AbstractViewModel {
    // TODO: // FIXME: 18.09.2015 // User playlists view model would be base // no favourite needed
    private static final String TAG = FavouritePlaylistsViewModel.class.getSimpleName();
    private final BehaviorSubject subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        UseCase useCase = new GetChartedPlaylists();
        compositeSubscription.add(useCase.build(Schedulers.computation(),
                AndroidSchedulers.mainThread(), getPlaylistsObserver()));
    }

    protected Observer<List<ContentValues[]>> getPlaylistsObserver() {
        return new Observer<List<ContentValues[]>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Playlists");
                subject.onCompleted();
                final Context context = DeePlayerApp.get().getApplicationContext();
                context.getContentResolver().notifyChange(SchematicDataProvider.Playlists.CONTENT_URI, null);
//                context.getContentResolver().notifyChange(SchematicDataProvider.Albums.recommendedQueryWithArtists(true), null);
//                context.getContentResolver().notifyChange(SchematicDataProvider.Albums.queryWithArtists(true), null);
//                context.getContentResolver().notifyChange(SchematicDataProvider.Albums.CONTENT_URI, null);

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                subject.onError(e);
            }

            @Override
            public void onNext(List<ContentValues[]> playlists) {
                subject.onNext(playlists);
                final Context context = DeePlayerApp.get().getApplicationContext();

//                Cursor artistIds = context.getContentResolver().query(SchematicDataProvider.Playlis.CONTENT_URI,
//                        new String[]{
//                                ArtistColumns.ID,
//                        }, null, null, null);
//                int artistCount = artistIds.getCount();
//                Set<Long> existingArtists = new HashSet<>(artistCount);
//                if (artistIds.moveToFirst()) {
//                    final int columnIndex = artistIds.getColumnIndex(ArtistColumns.ID);
//                    for (int i = 0; i < artistCount; ++i) {
//                        final int artistId = artistIds.getInt(columnIndex);
//                        existingArtists.add((long) artistId);
//                        artistIds.moveToNext();
//                    }
//                }
//                artistIds.close();

                ArrayList<ContentProviderOperation> operations = new ArrayList<>();
                operations.add(ContentProviderOperation.newDelete(SchematicDataProvider.Playlists.CONTENT_URI).build());

                context.getContentResolver().delete(SchematicDataProvider.Playlists.CONTENT_URI, null, null);
                for (ContentValues[] data : playlists) {
                    ContentValues playlist = data[DataContract.getPlaylistIndex()];
                    ContentValues user = data[DataContract.getUserIndex()];
//                    context.getContentResolver().insert(SchematicDataProvider.User.CONTENT_URI, user);
//                    context.getContentResolver().insert(SchematicDataProvider.Playlists.CONTENT_URI, playlist);

                    operations.add(ContentProviderOperation.newInsert(
                            SchematicDataProvider.User.CONTENT_URI).withValues(user).build());
                    operations.add(ContentProviderOperation.newInsert(
                            SchematicDataProvider.Playlists.CONTENT_URI).withValues(playlist).build());
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

    public BehaviorSubject getSubject() {
        return subject;
    }
}
