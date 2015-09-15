package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.interactions.GetUserFavouriteRadios;
import com.tutorial.deeplayer.app.deeplayer.interactions.UseCase;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 04.09.2015.
 */
public class FavouriteRadiosViewModel extends AbstractViewModel {
    private static final String TAG = RadioViewModel.class.getSimpleName();
    private final BehaviorSubject subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        UseCase getRadiosUseCase = new GetUserFavouriteRadios();
        compositeSubscription.add(getRadiosUseCase.build(Schedulers.computation(),
                AndroidSchedulers.mainThread(), getRadioObserver()));
    }

    protected Observer<List<ContentValues>> getRadioObserver() {
        return new Observer<List<ContentValues>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Radio");
                subject.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                subject.onError(e);
            }

            @Override
            public void onNext(List<ContentValues> radios) {
                //subject.onNext(radios);
                subject.onNext(radios);
                final Context context = DeePlayerApp.get().getApplicationContext();
                ContentValues[] values = new ContentValues[radios.size()];
                values = radios.toArray(values);
                context.getContentResolver().delete(SchematicDataProvider.Radios.CONTENT_URI, null, null);
                int insertedRows = context.getContentResolver().bulkInsert(SchematicDataProvider.Radios.CONTENT_URI,
                        values);
                Log.d(TAG, "Rows inserted -> " + insertedRows);
            }
        };
    }

    public Observable<Boolean> addRadioToFavourite(Radio radio) {
        return changeRadioFavouriteStatus(radio, true);
    }

    public Observable<Boolean> removeRadioFromFavourite(Radio radio) {
        return changeRadioFavouriteStatus(radio, false);
    }

    protected Observable<Boolean> changeRadioFavouriteStatus(Radio radio, boolean isChecked) {
        if (isChecked) {
            return getRestService().fetchResultRadioAddToFavourite(radio.getId());
        } else {
            return getRestService().fetchResultRadioRemoveFromFavourite(radio.getId());
        }
    }

    public BehaviorSubject getSubject() {
        return subject;
    }
}
