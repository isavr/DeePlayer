package com.tutorial.deeplayer.app.deeplayer.repository.datasources;

import android.content.Context;
import android.database.Cursor;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class LocalAlbumDataStore extends BaseLocalDataStore {
    private static final String TAG = LocalAlbumDataStore.class.getSimpleName();

    private Callable<List<Album>> getAlbumsFromDb() {
        return () -> {
            final Context context = DeePlayerApp.get().getApplicationContext();
            List<Album> albums = new ArrayList<>();
            Cursor c = context.getContentResolver().query(SchematicDataProvider.Albums.CONTENT_URI, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                for (c.moveToFirst(); c.moveToNext(); ) {
                    Album album = DataContract.AlbumConverter.convertFromCursor(c);
                    albums.add(album);
                }
            }
            c.close();
            return albums;
        };
    }

    public Observable<List<Album>> getAlbums() {
        return makeObservable(getAlbumsFromDb());
    }
}
