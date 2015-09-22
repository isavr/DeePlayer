package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService_Factory;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.widget.WidgetObservable;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ttuo on 06/04/15.
 */
abstract public class AbstractViewModel {
    private static final String TAG = AbstractViewModel.class.getSimpleName();
    private CompositeSubscription compositeSubscription;
    private Observable<String> filterObservable;

    final public void subscribeToDataStore() {
        Log.v(TAG, "subscribeToDataStore");
        unsubscribeFromDataStore();
        compositeSubscription = new CompositeSubscription();
        subscribeToDataStoreInternal(compositeSubscription);
    }

    public void subscribeToFilterUpdates(TextView input) {
        filterObservable =
                WidgetObservable.text(input)
                        .map(textChangeEvent -> textChangeEvent.text().toString())
                        .filter(item -> item.length() > 2 || item.length() == 0)
                        .debounce(500, TimeUnit.MILLISECONDS);
    }

    public void dispose() {
        Log.v(TAG, "dispose");

        if (compositeSubscription != null) {
            Log.e(TAG, "Disposing without calling unsubscribe FromDataStore first");

            // Unsubscribe in case we are still for some reason subscribed
            unsubscribeFromDataStore();
        }
    }

    abstract void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription);

    public void unsubscribeFromDataStore() {
        Log.v(TAG, "unsubscribeToDataStore");
        if (compositeSubscription != null) {
            compositeSubscription.clear();
            compositeSubscription = null;
        }
    }

    protected RestService getRestService() {
        return RestService_Factory.create().get();
    }

    public Observable<String> getFilterObservable() {
        return filterObservable;
    }
}
