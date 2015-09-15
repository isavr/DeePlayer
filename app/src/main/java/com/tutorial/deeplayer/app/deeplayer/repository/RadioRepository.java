package com.tutorial.deeplayer.app.deeplayer.repository;

import android.content.ContentValues;

import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;

import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public interface RadioRepository {
    Observable<List<Radio>> getRadiosFromDB();
    Observable<List<ContentValues>> getRadiosFromNet();
    Observable<List<ContentValues>> getFavouriteRadiosFromNet();

    Observable<List<ContentValues>> getAllRadiosDBComposingWithNet();
}
