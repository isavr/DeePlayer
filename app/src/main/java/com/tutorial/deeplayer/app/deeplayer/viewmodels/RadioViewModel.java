package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.support.annotation.NonNull;

import com.tutorial.deeplayer.app.deeplayer.interactions.GetAllRadios;
import com.tutorial.deeplayer.app.deeplayer.interactions.UseCase;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 27.07.2015.
 */
public class RadioViewModel extends FavouriteRadiosViewModel {
    private static final String TAG = RadioViewModel.class.getSimpleName();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        final UseCase getRadiosUseCase = new GetAllRadios();
        compositeSubscription.add(getRadiosUseCase.build(Schedulers.computation(),
                AndroidSchedulers.mainThread(), getRadioObserver()));
    }
}
