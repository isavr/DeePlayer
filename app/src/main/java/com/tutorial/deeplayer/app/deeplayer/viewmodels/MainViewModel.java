package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.User;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 27.07.2015.
 */
public class MainViewModel extends AbstractViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();

    private BehaviorSubject subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        //compositeSubscription.add(getUserData());
    }

    private Observable<ContentValues> getUserDataObservable() {
        return warpToIoThread(getRestService().fetchUserInfo())
                .map(DataContract.UserConverter::convertFrom);
    }

    protected Observable<User> warpToIoThread(Observable<User> dataObservable) {
        return dataObservable.subscribeOn(Schedulers.io());
    }

    private Subscription getUserData() {
        Observer<ContentValues> userObserver = new Observer<ContentValues>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted User");
                subject.onCompleted();
                //DialogFactory.closeAlertDialog(getChildFragmentManager());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                subject.onError(e);
            }

            @Override
            public void onNext(ContentValues values) {
                //subject.onNext(radios);
                subject.onNext(values);
                final Context context = DeePlayerApp.get().getApplicationContext();
                context.getContentResolver().delete(SchematicDataProvider.User.CONTENT_URI, null, null);
                context.getContentResolver().insert(SchematicDataProvider.User.CONTENT_URI, values);
            }
        };
        return getUserDataObservable().subscribeOn(AndroidSchedulers.mainThread()).subscribe(userObserver);
    }

    public BehaviorSubject getSubject() {
        return subject;
    }
}
