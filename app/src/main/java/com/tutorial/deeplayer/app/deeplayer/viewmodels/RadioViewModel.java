package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.pojo.BaseTypedItem;
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

    private Subscription getRadiosWithStatuses() {
        //DialogFactory.showProgressDialog(getActivity(), getChildFragmentManager());
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
        RestService service = new RestService();
        Observable<Radio> radioObservable = service.fetchRadioInfo().subscribeOn(Schedulers.io())
                .flatMap(item -> Observable.from(item.getData()));
        Observable<Radio> userFavourites = service.fetchUserRadioInfo().subscribeOn(Schedulers.io())
                .flatMap(item -> Observable.from(item.getUserData()));
        return Observable.concat(userFavourites, radioObservable).distinct(BaseTypedItem::getId)
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
                }).observeOn(AndroidSchedulers.mainThread())
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
            return new RestService().fetchResultRadioAddToFavourite(radio.getId());
        } else {
            return new RestService().fetchResultRadioRemoveFromFavourite(radio.getId());
        }
    }

    public BehaviorSubject<List<Radio>> getSubject() {
        return subject;
    }
}
