package com.tutorial.deeplayer.app.deeplayer.rest.interfaces;

import com.tutorial.deeplayer.app.deeplayer.pojo.RadioList;
import com.tutorial.deeplayer.app.deeplayer.pojo.TrackList;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by ilya.savritsky on 20.07.2015.
 */
public interface RadioAPI {
    @GET("/radio")
    Observable<RadioList> getRadios();

    @GET("/radio/genres")
    Observable<RadioList> getRadiosByGenre();

    @GET("/radio/top")
    Observable<RadioList> getRadiosByTop();

    @GET("/radio/{id}/tracks")
    Observable<TrackList> getRadioTracks(@Path("id") long radioId);

    @GET("/user/me/radios")
    Observable<RadioList> getUserRadios();
}
