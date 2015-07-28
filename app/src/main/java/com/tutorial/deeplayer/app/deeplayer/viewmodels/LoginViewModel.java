package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.deezer.DeezerDialogListener;

import rx.Observer;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 24.07.2015.
 */
public class LoginViewModel extends AbstractViewModel {
    private static final String TAG = LoginViewModel.class.getSimpleName();

    private BehaviorSubject<Bundle> subject = BehaviorSubject.create();

    // TODO: add data layer properties

    // TODO: listener setup refactoring
    private DeezerDialogListener listener;

    public LoginViewModel() {
        // Data layers setup
        listener = new DeezerDialogListener(new Observer<Bundle>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "on completed");
                subject.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "on error");
                subject.onError(e);
            }

            @Override
            public void onNext(Bundle values) {
                Log.d(TAG, "on next");
                subject.onNext(values);
            }
        });
        Log.v(TAG, "LoginViewModel");

    }

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        //compositeSubscription.add(listener.getObservable().subscribe(subject));
    }

    public DeezerDialogListener getListener() {
        return listener;
    }

//    @NonNull
//    public Observable<Bundle> getRepository() {
//        return subject.asObservable();
//    }

    @NonNull
    public BehaviorSubject<Bundle> getSubject() {
        return subject;
    }
}
