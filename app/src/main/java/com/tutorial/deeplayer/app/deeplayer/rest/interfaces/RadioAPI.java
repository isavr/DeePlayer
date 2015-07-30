package com.tutorial.deeplayer.app.deeplayer.rest.interfaces;

import com.tutorial.deeplayer.app.deeplayer.pojo.DataList;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by ilya.savritsky on 20.07.2015.
 */
public interface RadioAPI {
    @GET("/radio")
    Observable<DataList<Radio>> getRadios();

    @GET("/radio/genres")
    Observable<DataList<Radio>> getRadiosByGenre();

    @GET("/radio/top")
    Observable<DataList<Radio>> getRadiosByTop();

    @GET("/radio/{id}/tracks")
    Observable<DataList<Track>> getRadioTracks(@Path("id") long radioId);

}
