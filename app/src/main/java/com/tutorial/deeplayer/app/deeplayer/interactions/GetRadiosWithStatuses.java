package com.tutorial.deeplayer.app.deeplayer.interactions;

import com.tutorial.deeplayer.app.deeplayer.repository.UserDataRepository_Factory;

import rx.Observable;

/**
 * Created by ilya.savritsky on 10.09.2015.
 */
public class GetRadiosWithStatuses extends UseCase {
    private static final String TAG = GetRadiosWithStatuses.class.getSimpleName();

    @Override
    protected Observable buildUseCaseObservable() {
        return UserDataRepository_Factory.create().get().getAllRadiosDBComposingWithNet();
    }
}
