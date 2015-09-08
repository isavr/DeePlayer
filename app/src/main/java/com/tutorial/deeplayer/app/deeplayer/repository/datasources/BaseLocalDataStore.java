package com.tutorial.deeplayer.app.deeplayer.repository.datasources;

import android.util.Log;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class BaseLocalDataStore {
    private static final String TAG = BaseLocalDataStore.class.getSimpleName();

    static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch (Exception ex) {
                            Log.e(TAG, "Error reading from the database", ex);
                        }
                    }
                });
    }
}
