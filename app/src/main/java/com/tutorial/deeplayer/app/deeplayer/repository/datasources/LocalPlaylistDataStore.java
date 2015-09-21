package com.tutorial.deeplayer.app.deeplayer.repository.datasources;

import android.content.Context;
import android.database.Cursor;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.Playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Created by ilya.savritsky on 18.09.2015.
 */
public class LocalPlaylistDataStore extends BaseLocalDataStore {
    private Callable<List<Playlist>> getPlaylistsFromDb() {
        return () -> {
            final Context context = DeePlayerApp.get().getApplicationContext();
            List<Playlist> playlists = new ArrayList<>();
            Cursor c = context.getContentResolver().query(SchematicDataProvider.Playlists.CONTENT_URI,
                    null, null, null, null);
            if (c != null && c.getCount() > 0) {
                for (c.moveToFirst(); c.moveToNext(); ) {
                    Playlist playlist = DataContract.PlaylistConverter.convertFromCursor(c);
                    playlists.add(playlist);
                }
            }
            c.close();
            return playlists;
        };
    }

    public Observable<List<Playlist>> getPlaylists() {
        return makeObservable(getPlaylistsFromDb());
    }
}
