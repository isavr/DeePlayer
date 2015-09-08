package com.tutorial.deeplayer.app.deeplayer.interactions;

import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
import com.tutorial.deeplayer.app.deeplayer.repository.UserDataRepository_Factory;

import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class GetTracksFromDb extends UseCase {
    @Override
    protected Observable<List<Track>> buildUseCaseObservable() {
        return UserDataRepository_Factory.create().get().getTracksFromDB();
    }
}
