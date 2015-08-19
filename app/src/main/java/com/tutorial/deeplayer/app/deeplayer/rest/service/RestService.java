package com.tutorial.deeplayer.app.deeplayer.rest.service;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.pojo.*;
import com.tutorial.deeplayer.app.deeplayer.rest.interfaces.RadioAPI;
import com.tutorial.deeplayer.app.deeplayer.rest.interfaces.UserAPI;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import rx.Observable;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public class RestService {
    private static final String TAG = RestService.class.getSimpleName();
    private static final String WEB_SERVICE_BASE_URL = "http://api.deezer.com/";
    private final UserAPI userAPI;
    private final RadioAPI radioAPI;
    private String token;

    public RestService() {
        RequestInterceptor requestInterceptor = request -> {
            request.addQueryParam("output", "json");
            if (token != null && !"".equals(token)) {
                request.addQueryParam("access_token", token);
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WEB_SERVICE_BASE_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        if (DeePlayerApp.get() != null) {
            // we are not testing
            token = DeePlayerApp.get().getApplicationContext().
                    getSharedPreferences("deezer-session", 0).getString("access_token", null);
        }
        userAPI = restAdapter.create(UserAPI.class);
        radioAPI = restAdapter.create(RadioAPI.class);
    }

    public Observable<User> fetchUserInfoService() {
        return fetchUserInfoService(-1);
    }

    public Observable<User> fetchUserInfoService(int userId) {
        Observable<User> userObservable;
        if (userId == -1) {
            userObservable = userAPI.getUser();
        } else {
            userObservable = userAPI.getUser(userId);
        }
        return userObservable.flatMap(user -> {
            if (user.getError() != null) {
                return Observable.error(user.getError());
            }
            return Observable.just(user);
        });
    }

    public Observable<DataList<Radio>> fetchRadioInfo() {
        return prepareRadioInfoData(radioAPI.getRadios());
    }

    public Observable<DataList<Radio>> fetchUserRadioInfo() {
        return prepareRadioInfoData(userAPI.getUserRadios());
    }

    public Observable<Boolean> fetchResultRadioAddToFavourite(long radioId) {
        return userAPI.addRadioToFavourite(radioId);
    }

    public Observable<Boolean> fetchResultRadioRemoveFromFavourite(long radioId) {
        return userAPI.removeRadioFromFavourite(radioId);
    }

    protected Observable<DataList<Radio>> prepareRadioInfoData(Observable<DataList<Radio>> radioInfo) {
        return radioInfo.flatMap(radioList -> {
            if (radioList.getError() != null) {
                return Observable.error(radioList.getError());
            }
            return Observable.just(radioList);
        });
    }

    public Observable<DataList<Radio>> fetchRadioTopInfo() {
        return radioAPI.getRadiosByTop().flatMap(radioList -> {
            if (radioList.getError() != null) {
                return Observable.error(radioList.getError());
            }
            return Observable.just(radioList);
        });
    }

    public Observable<DataList<Track>> fetchRadioTracks(long radioId) {
        return radioAPI.getRadioTracks(radioId).flatMap(radioList -> {
            if (radioList.getError() != null) {
                return Observable.error(radioList.getError());
            }
            return Observable.just(radioList);
        });
    }

    public Observable<DataList<Album>> fetchAlbumsRecommendedForUser() {
        return prepareAlbumsData(userAPI.getAlbumsRecommendedForUser());
    }

    public Observable<DataList<Album>> fetchUserAlbums() {
        return prepareAlbumsData(userAPI.getUserAlbums());
    }

    public Observable<Boolean> fetchResultAlbumAddToFavourite(long albumId) {
        return userAPI.addAlbumToFavourite(albumId);
    }

    public Observable<Boolean> fetchResultAlbumRemoveFromFavourite(long albumId) {
        return userAPI.removeAlbumFromFavourite(albumId);
    }

    protected Observable<DataList<Album>> prepareAlbumsData(Observable<DataList<Album>> dataObservable) {
        return dataObservable.flatMap(albumDataList -> {
            if (albumDataList.getError() != null) {
                return Observable.error(albumDataList.getError());
            }
            return Observable.just(albumDataList);
        });
    }

    public Observable<DataList<Artist>> fetchArtistsRecommendedForUser() {
        return prepareArtistsData(userAPI.getArtistsRecommendedForUser());
    }

    public Observable<DataList<Artist>> fetchUserArtists() {
        return prepareArtistsData(userAPI.getUserArtists());
    }

    public Observable<Boolean> fetchResultArtistAddToFavourite(long artistId) {
        return userAPI.addArtistToFavourite(artistId);
    }

    public Observable<Boolean> fetchResultArtistRemoveFromFavourite(long artistId) {
        return userAPI.removeArtistFromFavourite(artistId);
    }

    protected Observable<DataList<Artist>> prepareArtistsData(Observable<DataList<Artist>> dataObservable) {
        return dataObservable.flatMap(artistDataList -> {
            if (artistDataList.getError() != null) {
                return Observable.error(artistDataList.getError());
            }
            return Observable.just(artistDataList);
        });
    }

    public Observable<DataList<Track>> fetchTracksRecommendedForUser() {
        return prepareTracksData(userAPI.getTracksRecommendedForUser());
    }

    public Observable<DataList<Track>> fetchUserTrack() {
        return prepareTracksData(userAPI.getUserTracks());
    }

    public Observable<Boolean> fetchResultTrackAddToFavourite(long trackId) {
        return userAPI.addArtistToFavourite(trackId);
    }

    public Observable<Boolean> fetchResultTrackRemoveFromFavourite(long trackId) {
        return userAPI.removeArtistFromFavourite(trackId);
    }

    protected Observable<DataList<Track>> prepareTracksData(Observable<DataList<Track>> dataObservable) {
        return dataObservable.flatMap(trackDataList -> {
            if (trackDataList.getError() != null) {
                return Observable.error(trackDataList.getError());
            }
            return Observable.just(trackDataList);
        });
    }
}
