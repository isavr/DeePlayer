package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.pojo.BaseTypedItem;
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
 * Created by ilya.savritsky on 30.07.2015.
 */
public class RecommendedArtistViewModel extends AbstractViewModel {
    private static final String TAG = RecommendedArtistViewModel.class.getSimpleName();
    private final BehaviorSubject<List<Artist>> subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        compositeSubscription.add(getRecommendedArtistsWithStatuses());
    }

    private Subscription getRecommendedArtistsWithStatuses() {
        Observer<List<Artist>> radioObserver = new Observer<List<Artist>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Artist");
                subject.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                subject.onError(e);
            }

            @Override
            public void onNext(List<Artist> artist) {
                subject.onNext(artist);
                //rxCupboard.put(radio);
            }
        };
        RestService service = new RestService();
        Observable<Artist> userArtists = service.fetchUserArtists().subscribeOn(Schedulers.io())
                .flatMap(item -> Observable.from(item.getUserData()));
        Observable<Artist> recommended = service.fetchArtistsRecommendedForUser().subscribeOn(Schedulers.io())
                .flatMap(item -> Observable.from(item.getData()));
        return Observable.concat(recommended, userArtists).groupBy(BaseTypedItem::getId).flatMap(Observable::toList)
                .filter(item -> item.size() > 1).map(itemList -> {
                    Artist artist = itemList.get(0);
                    artist.setFavourite(true);
                    return artist;
                }).concatWith(recommended).distinct(BaseTypedItem::getId)/*.toSortedList((artist, artist2) -> {
                    if (artist.getName() != null && artist2.getName() != null) {
                        String title1 = artist.getName().trim();
                        String title2 = artist2.getName().trim();
                        return title1.compareTo(title2);
                    } else if (artist.getName() != null) {
                        return 1;
                    } else {
                        return -1;
                    }
                })*/
                .toList().observeOn(AndroidSchedulers.mainThread()).subscribe(radioObserver);
    }

    public Observable<Boolean> addArtistToFavourite(Artist artist) {
        return changeArtistFavouriteStatus(artist, true);
    }

    public Observable<Boolean> removeArtistFromFavourite(Artist artist) {
        return changeArtistFavouriteStatus(artist, false);
    }

    private Observable<Boolean> changeArtistFavouriteStatus(Artist artist, boolean isChecked) {
        if (isChecked) {
            return new RestService().fetchResultArtistAddToFavourite(artist.getId());
        } else {
            return new RestService().fetchResultArtistRemoveFromFavourite(artist.getId());
        }
    }

    public BehaviorSubject<List<Artist>> getSubject() {
        return subject;
    }
}
