package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.pojo.BaseTypedItem;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
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
 * Created by ilya.savritsky on 17.08.2015.
 */
public class RecommendedTrackViewModel extends AbstractViewModel {
    private static final String TAG = RecommendedAlbumsViewModel.class.getSimpleName();
    private final BehaviorSubject<List<Track>> subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        compositeSubscription.add(getRecommendedAlbumsWithStatuses());
    }

    private Subscription getRecommendedAlbumsWithStatuses() {
        //DialogFactory.showProgressDialog(getActivity(), getChildFragmentManager());
        Observer<List<Track>> trackObserver = new Observer<List<Track>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Tracks");
                subject.onCompleted();
                //DialogFactory.closeAlertDialog(getChildFragmentManager());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                subject.onError(e);
            }

            @Override
            public void onNext(List<Track> tracks) {
                subject.onNext(tracks);
                //rxCupboard.put(radio);
            }
        };
        RestService service = new RestService();
        Observable<Track> userTracks = service.fetchUserTrack().subscribeOn(Schedulers.io())
                .flatMap(item -> Observable.from(item.getUserData()));
        Observable<Track> recommended = service.fetchTracksRecommendedForUser().subscribeOn(Schedulers.io())
                .flatMap(item -> Observable.from(item.getData()));
        return Observable.concat(recommended, userTracks).groupBy(BaseTypedItem::getId).flatMap(Observable::toList)
                .filter(item -> item.size() > 1).map(itemList -> {
                    Track track = itemList.get(0);
                    track.setFavourite(true);
                    return track;
                }).concatWith(recommended).distinct(BaseTypedItem::getId).toSortedList((track, track2) -> {
                    if (track.getTitle() != null && track2.getTitle() != null) {
                        String title1 = track.getTitle().trim();
                        String title2 = track2.getTitle().trim();
                        return title1.compareTo(title2);
                    } else if (track.getTitle() != null) {
                        return 1;
                    } else {
                        return -1;
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(trackObserver);
    }

    public Observable<Boolean> addTrackToFavourite(Track track) {
        return changeTrackFavouriteStatus(track, true);
    }

    public Observable<Boolean> removeTrackFromFavourite(Track track) {
        return changeTrackFavouriteStatus(track, false);
    }

    private Observable<Boolean> changeTrackFavouriteStatus(Track track, boolean isChecked) {
        if (isChecked) {
            return new RestService().fetchResultTrackAddToFavourite(track.getId());
        } else {
            return new RestService().fetchResultTrackRemoveFromFavourite(track.getId());
        }
    }

    public BehaviorSubject<List<Track>> getSubject() {
        return subject;
    }
}
