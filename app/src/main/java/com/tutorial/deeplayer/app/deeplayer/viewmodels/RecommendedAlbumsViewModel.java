package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
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
 * Created by ilya.savritsky on 28.07.2015.
 */
public class RecommendedAlbumsViewModel extends AbstractViewModel {
    private static final String TAG = RadioViewModel.class.getSimpleName();
    private final BehaviorSubject<List<Album>> subject = BehaviorSubject.create();

    @Override
    void subscribeToDataStoreInternal(@NonNull CompositeSubscription compositeSubscription) {
        compositeSubscription.add(getRecommendedAlbumsWithStatuses());
    }

    private Subscription getRecommendedAlbumsWithStatuses() {
        //DialogFactory.showProgressDialog(getActivity(), getChildFragmentManager());
        Observer<List<Album>> radioObserver = new Observer<List<Album>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted Album");
                subject.onCompleted();
                //DialogFactory.closeAlertDialog(getChildFragmentManager());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                subject.onError(e);
            }

            @Override
            public void onNext(List<Album> albums) {
                subject.onNext(albums);
                //rxCupboard.put(radio);
            }
        };
        RestService service = new RestService();
        Observable<Album> userAlbums = service.fetchUserAlbums().subscribeOn(Schedulers.io())
                .flatMap(item -> Observable.from(item.getUserData()));
        Observable<Album> recommended = service.fetchAlbumsRecommendedForUser().subscribeOn(Schedulers.io())
                .flatMap(item -> Observable.from(item.getData()));
        return Observable.concat(recommended, userAlbums).groupBy(BaseTypedItem::getId).flatMap(Observable::toList)
                .filter(item -> item.size() > 1).map(itemList -> {
                    Album album = itemList.get(0);
                    album.setFavourite(true);
                    return album;
                }).concatWith(recommended).distinct(BaseTypedItem::getId).toSortedList((album, album2) -> {
                    if (album.getTitle() != null && album2.getTitle() != null) {
                        String title1 = album.getTitle().trim();
                        String title2 = album2.getTitle().trim();
                        return title1.compareTo(title2);
                    } else if (album.getTitle() != null) {
                        return 1;
                    } else {
                        return -1;
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(radioObserver);
    }

    public Observable<Boolean> addAlbumToFavourite(Album album) {
        return changeAlbumFavouriteStatus(album, true);
    }

    public Observable<Boolean> removeAlbumFromFavourite(Album album) {
        return changeAlbumFavouriteStatus(album, false);
    }

    private Observable<Boolean> changeAlbumFavouriteStatus(Album album, boolean isChecked) {
        if (isChecked) {
            return new RestService().fetchResultAlbumAddToFavourite(album.getId());
        } else {
            return new RestService().fetchResultAlbumRemoveFromFavourite(album.getId());
        }
    }

    public BehaviorSubject<List<Album>> getSubject() {
        return subject;
    }
}
