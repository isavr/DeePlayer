package com.tutorial.deeplayer.app.deeplayer.rest.interfaces;

import com.tutorial.deeplayer.app.deeplayer.pojo.Chart;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public interface ChartsAPI {
    @GET("/chart/0")
    Observable<Chart> getChartsInfo();
}
