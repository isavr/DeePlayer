package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.DataContract;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.pojo.BaseTypedItem;
import com.tutorial.deeplayer.app.deeplayer.pojo.DataList;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;

import java.util.ArrayList;
import java.util.List;

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
public class RadioViewModel extends AbstractViewModel {
    private static final String TAG = RadioViewModel.class.getSimpleName();
    private final BehaviorSubject subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        compositeSubscription.add(getRadiosWithStatuses());
    }

    private Observable<Radio> getRadiosDataObservable(RestService service) {
        return warpToIoThread(service.fetchRadioInfo())
                .flatMap(item -> Observable.from(item.getData()));
    }

    private Observable<Radio> getUserFavouritesObservable(RestService service, int index) {
        return warpToIoThread(service.fetchUserRadioInfo(index))
                .flatMap(item -> Observable.from(item.getUserData()));
    }

    private Observable<Radio> getUserFavouritesObservable(RestService service) {
        return warpToIoThread(service.fetchUserRadioInfo())
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
                            Observable<Radio> observable = getUserFavouritesObservable(service, index);
                            list.add(observable);
                        }
                        return Observable.merge(list);
                    } else {
                        return Observable.from(item.getUserData());
                    }
                });
    }

    protected Observable<DataList<Radio>> warpToIoThread(Observable<DataList<Radio>> dataObservable) {
        return dataObservable.subscribeOn(Schedulers.io());
    }

    protected Observable<List<ContentValues>> getRadiosWithStatusesObservable() {
        RestService service = getRestService();

        return Observable.concat(getUserFavouritesObservable(service), getRadiosDataObservable(service)).distinct(BaseTypedItem::getId)
                .map(radio -> DataContract.RadioConverter.convertFrom(radio)).toList();
//                .toSortedList((radio, radio2) -> {
//                    if (radio.getTitle() != null && radio2.getTitle() != null) {
//                        String title1 = radio.getTitle().trim();
//                        String title2 = radio2.getTitle().trim();
//                        return title1.compareTo(title2);
//                    } else if (radio.getTitle() != null) {
//                        return 1;
//                    } else {
//                        return -1;
//                    }
//                });
    }

    private Subscription getRadiosWithStatuses() {
        Observer<List<ContentValues>> radioObserver = new Observer<List<ContentValues>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Radio");
                subject.onCompleted();
                //DialogFactory.closeAlertDialog(getChildFragmentManager());
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

        return getRadiosWithStatusesObservable().observeOn(AndroidSchedulers.mainThread())
                .subscribe(radioObserver);
    }

    public Observable<Boolean> addRadioToFavourite(Radio radio) {
        return changeRadioFavouriteStatus(radio, true);
    }

    public Observable<Boolean> removeRadioFromFavourite(Radio radio) {
        return changeRadioFavouriteStatus(radio, false);
    }

    private Observable<Boolean> changeRadioFavouriteStatus(Radio radio, boolean isChecked) {
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
