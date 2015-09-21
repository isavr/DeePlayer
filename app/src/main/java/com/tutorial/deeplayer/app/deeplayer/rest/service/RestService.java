package com.tutorial.deeplayer.app.deeplayer.rest.service;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.pojo.Chart;
import com.tutorial.deeplayer.app.deeplayer.pojo.DataList;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
import com.tutorial.deeplayer.app.deeplayer.pojo.User;
import com.tutorial.deeplayer.app.deeplayer.rest.interfaces.ChartsAPI;
import com.tutorial.deeplayer.app.deeplayer.rest.interfaces.RadioAPI;
import com.tutorial.deeplayer.app.deeplayer.rest.interfaces.UserAPI;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import rx.Observable;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
@Singleton
public class RestService {
    private static final String TAG = RestService.class.getSimpleName();
    public static int INDEX_STEP_VAL = 25;
    private static final String WEB_SERVICE_BASE_URL = "http://api.deezer.com/";
    private final UserAPI userAPI;
    private final RadioAPI radioAPI;
    private final ChartsAPI chartsAPI;
    private String token;

    @Inject
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
        token = extractDeeToken();
        userAPI = restAdapter.create(UserAPI.class);
        radioAPI = restAdapter.create(RadioAPI.class);
        chartsAPI = restAdapter.create(ChartsAPI.class);
    }

    protected String extractDeeToken() {
        if (DeePlayerApp.get() != null) {
            // we are not testing
            return DeePlayerApp.get().getApplicationContext().
                    getSharedPreferences("deezer-session", 0).getString("access_token", null);
        }
        return null;
    }

    public Observable<Chart> fetchChartInfo() {
        return chartsAPI.getChartsInfo().flatMap(item -> {
            if (item.getError() != null) {
                return Observable.error(item.getError());
            }
            return Observable.just(item);
        });
    }

    public Observable<User> fetchUserInfo() {
        return fetchUserInfo(-1);
    }

    public Observable<User> fetchUserInfo(int userId) {
        Observable<User> userObservable;
        if (userId == -1) {
            userObservable = userAPI.getUser();
        } else {
            userObservable = userAPI.getUser(userId);
        }
        return prepareUserInfoData(userObservable);
    }

    protected Observable<User> prepareUserInfoData(Observable<User> userInfo) {
        return userInfo.flatMap(user -> {
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
        return fetchUserRadioInfo(0);
    }

    public Observable<DataList<Radio>> fetchUserRadioInfo(int index) {
        return prepareRadioInfoData(userAPI.getUserRadios(index));
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
        return fetchUserAlbums(0);
    }

    public Observable<DataList<Album>> fetchUserAlbums(int index) {
        return prepareAlbumsData(userAPI.getUserAlbums(index));
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
        return fetchUserArtists(0);
    }

    public Observable<DataList<Artist>> fetchUserArtists(int index) {
        return prepareArtistsData(userAPI.getUserArtists(index));
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

    public Observable<DataList<Track>> fetchUserTracks() {
        return fetchUserTracks(0);
    }

    public Observable<DataList<Track>> fetchUserTracks(int index) {
        return prepareTracksData(userAPI.getUserTracks(index));
    }

    public Observable<Set<Long>> fetchUserTrackIds() {
        return prepareTracksData(userAPI.getUserTracks()).map(item -> item.getUserData())
                .reduce(new HashSet<>(), (dataSet, item) -> {
                    for (Track track : item) {
                        dataSet.add(track.getId());
                    }
                    return dataSet;
                });
    }

    public Observable<Boolean> fetchResultTrackAddToFavourite(long trackId) {
        return userAPI.addTrackToFavourite(trackId);
    }

    public Observable<Boolean> fetchResultTrackRemoveFromFavourite(long trackId) {
        return userAPI.removeTrackFromFavourite(trackId);
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
