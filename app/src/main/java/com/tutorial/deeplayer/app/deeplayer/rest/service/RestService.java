package com.tutorial.deeplayer.app.deeplayer.rest.service;

import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.pojo.RadioList;
import com.tutorial.deeplayer.app.deeplayer.pojo.TrackList;
import com.tutorial.deeplayer.app.deeplayer.pojo.User;
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

            //request.addQueryParam("response_type", "token");
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WEB_SERVICE_BASE_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        token = DeePlayerApp.get().getApplicationContext().
                getSharedPreferences("deezer-session", 0).getString("access_token", null);
        Log.d(TAG, "");
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

    public Observable<RadioList> fetchRadioInfo() {
        return radioAPI.getRadios().flatMap(radioList -> {
            if (radioList.getError() != null) {
                return Observable.error(radioList.getError());
            }
            return Observable.just(radioList);
        });
    }

    public Observable<RadioList> fetchUserRadioInfo() {
        return radioAPI.getUserRadios().flatMap(radioList -> {
            if (radioList.getError() != null) {
                return Observable.error(radioList.getError());
            }
            return Observable.just(radioList);
        });
    }

    public Observable<RadioList> fetchRadioTopInfo() {
        return radioAPI.getRadiosByTop().flatMap(radioList -> {
            if (radioList.getError() != null) {
                return Observable.error(radioList.getError());
            }
            return Observable.just(radioList);
        });
    }

    public Observable<TrackList> fetchRadioTracks(long radioId) {
        return radioAPI.getRadioTracks(radioId).flatMap(radioList -> {
            if (radioList.getError() != null) {
                return Observable.error(radioList.getError());
            }
            return Observable.just(radioList);
        });
    }

    public Observable<Boolean> fetchResultRadioAddToFavourite(long radioId) {
        return radioAPI.addRadioToFavourite(radioId);
    }

    public Observable<Boolean> fetchResultRadioRemoveFromFavourite(long radioId) {
        return radioAPI.removeRadioFromFavourite(radioId);
    }

}
