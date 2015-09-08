package com.tutorial.deeplayer.app.deeplayer.repository.datasources;

import android.content.Context;
import android.database.Cursor;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class LocalTrackDataStore extends BaseLocalDataStore {
    private static final String TAG = LocalTrackDataStore.class.getSimpleName();

    private Callable<List<Track>> getTracksFromDb() {
        return () -> {
            final Context context = DeePlayerApp.get().getApplicationContext();
            List<Track> tracks = new ArrayList<>();
            Cursor c = context.getContentResolver().query(SchematicDataProvider.Tracks.CONTENT_URI, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                for (c.moveToFirst();c.moveToNext();) {
                    Track track = DataContract.TrackConverter.convertFromCursor(c);
                    tracks.add(track);
                }
            }
            c.close();
            return tracks;
        };
    }

    public Observable<List<Track>> getTracks() {
        return makeObservable(getTracksFromDb());
    }
}
