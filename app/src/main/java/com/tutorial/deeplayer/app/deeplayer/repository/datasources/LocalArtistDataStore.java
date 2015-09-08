package com.tutorial.deeplayer.app.deeplayer.repository.datasources;

import android.content.Context;
import android.database.Cursor;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class LocalArtistDataStore extends BaseLocalDataStore {
    private static final String TAG = LocalArtistDataStore.class.getSimpleName();

    private Callable<List<Artist>> getArtistsFromDb() {
        return () -> {
            final Context context = DeePlayerApp.get().getApplicationContext();
            List<Artist> artists = new ArrayList<>();
            Cursor c = context.getContentResolver().query(SchematicDataProvider.Artists.CONTENT_URI, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                for (c.moveToFirst(); c.moveToNext(); ) {
                    Artist artist = DataContract.ArtistConverter.convertFromCursor(c);
                    artists.add(artist);
                }
            }
            c.close();
            return artists;
        };
    }

    public Observable<List<Artist>> getArtists() {
        return makeObservable(getArtistsFromDb());
    }
}
