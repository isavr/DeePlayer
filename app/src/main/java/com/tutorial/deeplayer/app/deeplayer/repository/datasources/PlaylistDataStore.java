package com.tutorial.deeplayer.app.deeplayer.repository.datasources;

import android.content.ContentValues;

import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.pojo.Playlist;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService_Factory;

import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 18.09.2015.
 */
public class PlaylistDataStore extends BaseLocalDataStore {
    private final String TAG = PlaylistDataStore.class.getSimpleName();

    private Observable<Playlist> getChartedPlaylistsDataObservable() {
        return RestService_Factory.create().get().fetchChartInfo().flatMap(item -> {
            if (item.getPlaylists() != null) {
                return Observable.from(item.getPlaylists().getData());
            }
            return Observable.empty();
        });
    }

    public Observable<List<ContentValues[]>> getChartedPlaylists() {
        Observable<Playlist> charted = getChartedPlaylistsDataObservable();
        return charted.map(item -> DataContract.PlaylistConverter.convertFrom(item)).toList();
    }
}
