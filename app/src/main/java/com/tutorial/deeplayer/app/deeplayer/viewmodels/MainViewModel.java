package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.os.Bundle;
import android.support.annotation.NonNull;

import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 27.07.2015.
 */
public class MainViewModel extends AbstractViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();

    private BehaviorSubject<Bundle> subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {

    }
}
