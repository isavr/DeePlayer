package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.support.annotation.NonNull;

import com.tutorial.deeplayer.app.deeplayer.interactions.GetChartedTracks;
import com.tutorial.deeplayer.app.deeplayer.interactions.UseCase;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public class ChartedTracksViewModel extends FavouriteTracksViewModel {
    private static final String TAG = ChartedTracksViewModel.class.getSimpleName();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        UseCase useCase = new GetChartedTracks();
        compositeSubscription.add(useCase.build(Schedulers.computation(),
                AndroidSchedulers.mainThread(), getTracksObserver()));
    }
}
