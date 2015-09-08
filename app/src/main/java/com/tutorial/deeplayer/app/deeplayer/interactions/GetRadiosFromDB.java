package com.tutorial.deeplayer.app.deeplayer.interactions;

import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.repository.UserDataRepository_Factory;

import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 07.09.2015.
 */
public class GetRadiosFromDB extends UseCase {
    @Override
    protected Observable<List<Radio>> buildUseCaseObservable() {
        return UserDataRepository_Factory.create().get().getRadiosFromDB();
    }
}
