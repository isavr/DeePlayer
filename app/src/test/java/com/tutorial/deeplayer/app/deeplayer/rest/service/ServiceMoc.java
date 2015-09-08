package com.tutorial.deeplayer.app.deeplayer.rest.service;

import com.tutorial.deeplayer.app.deeplayer.pojo.DataList;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by ilya.savritsky on 19.08.2015.
 */
public class ServiceMoc extends RestService {
    public ServiceMoc() {
        super();
    }

    @Override
    protected String extractDeeToken() {
        return null;
    }

    private Radio getRadioMoc1() {
//        Radio r1 = mock(Radio.class);
//        when(r1.getId()).thenReturn(30901L);
//        when(r1.getTitle()).thenReturn("Metal");
//        when(r1.getDescription()).thenReturn("Metal");
//        when(r1.getShare())
//                .thenReturn("http://www.deezer.com/mixes/genre/30901?utm_source=deezer&utm_content=mixes-genre-30901&utm_term=700316071_1439977034&utm_medium=web");
//        when(r1.getPicture()).thenReturn("https://api.deezer.com/radio/30901/image");
//        when(r1.getTracklist()).thenReturn("https://api.deezer.com/radio/30901/tracks");
//        when(r1.getType()).thenReturn("radio");
        Radio r1 = new Radio();
        r1.setId(30901L);
        r1.setTitle("Metal");
        r1.setDescription("Metal");
        r1.setShare("http://www.deezer.com/mixes/genre/30901?utm_source=deezer&utm_content=mixes-genre-30901&utm_term=700316071_1439977034&utm_medium=web");
        r1.setPicture("https://api.deezer.com/radio/30901/image");
        r1.setTracklist("https://api.deezer.com/radio/30901/tracks");
        r1.setType("radio");
        return r1;
    }

    private Radio getRadioMoc2() {
//        Radio r2 = mock(Radio.class);
//        when(r2.getId()).thenReturn(37031L);
//        when(r2.getTitle()).thenReturn("Hard Rock");
//        when(r2.getDescription()).thenReturn("Hard Rock");
//        when(r2.getShare())
//                .thenReturn("http://www.deezer.com/mixes/genre/37031?utm_source=deezer&utm_content=mixes-genre-37031&utm_term=700316071_1439977982&utm_medium=web");
//        when(r2.getPicture()).thenReturn("https://api.deezer.com/radio/37031/image");
//        when(r2.getTracklist()).thenReturn("https://api.deezer.com/radio/37031/tracks");
//        when(r2.getType()).thenReturn("radio");
        Radio r2 = new Radio();
        r2.setId(37031L);
        r2.setTitle("Hard Rock");
        r2.setDescription("Hard Rock");
        r2.setShare("http://www.deezer.com/mixes/genre/37031?utm_source=deezer&utm_content=mixes-genre-37031&utm_term=700316071_1439977982&utm_medium=web");
        r2.setPicture("https://api.deezer.com/radio/37031/image");
        r2.setTracklist("https://api.deezer.com/radio/37031/tracks");
        r2.setType("radio");
        return r2;
    }

    public DataList<Radio> getMocRadioInfo() {
//        DataList<Radio> radioList = mock(DataList.class);
//        when(radioList.getError()).thenReturn(null);
        DataList<Radio> radioList = new DataList<>();
        radioList.setData(new ArrayList<>());
        radioList.getData().add(getRadioMoc1());
        radioList.getData().add(getRadioMoc2());
        return radioList;
    }

    public DataList<Radio> getMocUserRadioInfo() {
//        DataList<Radio> radioList = mock(DataList.class);
        DataList<Radio> radioList = new DataList<>();
        radioList.setData(new ArrayList<>());
        Radio radio = getRadioMoc1();
        radio.setFavourite(true);
        Radio r2 = getRadioMoc2();
        r2.setFavourite(true);
//        when(radio.isFavourite()).thenReturn(true);
        radioList.getData().add(radio);
        radioList.getData().add(r2);
        return radioList;
    }

    private Observable<DataList<Radio>> getMocRadioInfoObservable() {
        return Observable.just(getMocRadioInfo());
    }

    private Observable<DataList<Radio>> getMocUserRadioInfoObservable() {
        return Observable.just(getMocUserRadioInfo());
    }

    @Override
    public Observable<DataList<Radio>> fetchRadioInfo() {
        return prepareRadioInfoData(getMocRadioInfoObservable());
    }

    public Observable<DataList<Radio>> fetchUserRadioInfo() {
        return prepareRadioInfoData(getMocUserRadioInfoObservable());
    }
}
