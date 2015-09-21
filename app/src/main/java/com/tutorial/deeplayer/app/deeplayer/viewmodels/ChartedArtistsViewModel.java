package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.support.annotation.NonNull;

import com.tutorial.deeplayer.app.deeplayer.interactions.GetChartedArtists;
import com.tutorial.deeplayer.app.deeplayer.interactions.UseCase;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public class ChartedArtistsViewModel extends FavouriteArtistViewModel {

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        UseCase useCase = new GetChartedArtists();
        compositeSubscription.add(useCase.build(Schedulers.computation(),
                AndroidSchedulers.mainThread(), getArtistObserver()));
    }
}
