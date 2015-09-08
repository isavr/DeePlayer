package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.support.annotation.NonNull;

import com.tutorial.deeplayer.app.deeplayer.interactions.GetRecommendedArtists;
import com.tutorial.deeplayer.app.deeplayer.interactions.UseCase;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ilya.savritsky on 30.07.2015.
 */
public class RecommendedArtistViewModel extends FavouriteArtistViewModel {
//    private static final String TAG = RecommendedArtistViewModel.class.getSimpleName();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        UseCase useCase = new GetRecommendedArtists();
        compositeSubscription.add(useCase.build(Schedulers.computation(),
                AndroidSchedulers.mainThread(), getArtistObserver()));
    }
}
