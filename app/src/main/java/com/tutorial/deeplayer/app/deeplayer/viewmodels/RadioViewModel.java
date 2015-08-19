package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.pojo.BaseTypedItem;
import com.tutorial.deeplayer.app.deeplayer.pojo.DataList;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;

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
    private final BehaviorSubject<List<Radio>> subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        compositeSubscription.add(getRadiosWithStatuses());
    }

    protected RestService getRestService() {
        return new RestService();
    }

    private Observable<Radio> getRadiosDataObservable(RestService service) {
        return warpToIoThread(service.fetchRadioInfo())
                .flatMap(item -> Observable.from(item.getData()));
    }

    private Observable<Radio> getUserFavouritesObservable(RestService service) {
        return warpToIoThread(service.fetchUserRadioInfo())
                .flatMap(item -> Observable.from(item.getUserData()));
    }

    protected Observable<DataList<Radio>> warpToIoThread(Observable<DataList<Radio>> dataObservable) {
        return dataObservable.subscribeOn(Schedulers.io());
    }

    protected Observable<List<Radio>> getRadiosWithStatusesObservable() {
        RestService service = getRestService();
        return Observable.concat(getUserFavouritesObservable(service), getRadiosDataObservable(service)).distinct(BaseTypedItem::getId)
                .toSortedList((radio, radio2) -> {
                    if (radio.getTitle() != null && radio2.getTitle() != null) {
                        String title1 = radio.getTitle().trim();
                        String title2 = radio2.getTitle().trim();
                        return title1.compareTo(title2);
                    } else if (radio.getTitle() != null) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
    }

    private Subscription getRadiosWithStatuses() {
        Observer<List<Radio>> radioObserver = new Observer<List<Radio>>() {
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
            public void onNext(List<Radio> radios) {
                subject.onNext(radios);
                //rxCupboard.put(radio);
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

    public BehaviorSubject<List<Radio>> getSubject() {
        return subject;
    }
}
