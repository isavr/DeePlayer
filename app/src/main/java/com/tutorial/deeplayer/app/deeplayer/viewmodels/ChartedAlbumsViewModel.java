package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.support.annotation.NonNull;

import com.tutorial.deeplayer.app.deeplayer.interactions.GetChartedAlbums;
import com.tutorial.deeplayer.app.deeplayer.interactions.UseCase;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public class ChartedAlbumsViewModel extends FavouriteAlbumsViewModel {
    private static final String TAG = RecommendedAlbumsViewModel.class.getSimpleName();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        UseCase useCase = new GetChartedAlbums();
        compositeSubscription.add(useCase.build(Schedulers.computation(),
                AndroidSchedulers.mainThread(), getAlbumObserver()));
    }
}
