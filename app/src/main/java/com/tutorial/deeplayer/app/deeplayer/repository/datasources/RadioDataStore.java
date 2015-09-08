package com.tutorial.deeplayer.app.deeplayer.repository.datasources;

import android.content.ContentValues;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.pojo.BaseTypedItem;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService_Factory;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class RadioDataStore extends BaseLocalDataStore {
    private final String TAG = RadioDataStore.class.getSimpleName();

    public Observable<Radio> getRadiosDataObservable() {
        return RestService_Factory.create().get().fetchRadioInfo()
                .flatMap(item -> Observable.from(item.getData()));
    }

    private Observable<Radio> getUserFavouritesObservable(final int index) {
        return RestService_Factory.create().get().fetchUserRadioInfo(index)
                .flatMap(item -> Observable.from(item.getUserData()));
    }

    private Observable<Radio> getUserFavouritesObservable() {
        return RestService_Factory.create().get().fetchUserRadioInfo()
                .flatMap(item -> {
                    int total = item.getTotal();
                    int index = 0;
                    Log.d(TAG, "total count - " + total);
                    if (total > RestService.INDEX_STEP_VAL) {
                        List<Observable<Radio>> list = new ArrayList<>();
                        list.add(Observable.from(item.getUserData()));
                        for (int i = 1; i < total / RestService.INDEX_STEP_VAL + 1; ++i) {
                            index = i * RestService.INDEX_STEP_VAL;
                            Log.d(TAG, "try index - " + index);
                            Observable<Radio> observable = getUserFavouritesObservable(index);
                            list.add(observable);
                        }
                        return Observable.merge(list);
                    } else {
                        return Observable.from(item.getUserData());
                    }
                });
    }

    public Observable<List<ContentValues>> getRadiosWithStatusesObservable() {
        return Observable.concat(getUserFavouritesObservable(), getRadiosDataObservable()).distinct(BaseTypedItem::getId)
                .map(radio -> DataContract.RadioConverter.convertFrom(radio)).toList();
    }

    public Observable<List<ContentValues>> getUserFavouriteRadios() {
        return getUserFavouritesObservable().map(radio -> DataContract.RadioConverter.convertFrom(radio)).toList();
    }
}
