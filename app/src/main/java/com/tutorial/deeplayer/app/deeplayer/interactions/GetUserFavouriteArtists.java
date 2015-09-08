package com.tutorial.deeplayer.app.deeplayer.interactions;

import com.tutorial.deeplayer.app.deeplayer.repository.UserDataRepository_Factory;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class GetUserFavouriteArtists extends UseCase {
    private static final String TAG = GetUserFavouriteArtists.class.getSimpleName();

    @Override
    protected Observable buildUseCaseObservable() {
        return UserDataRepository_Factory.create().get().getFavouriteArtistsFromNet();
    }
}
