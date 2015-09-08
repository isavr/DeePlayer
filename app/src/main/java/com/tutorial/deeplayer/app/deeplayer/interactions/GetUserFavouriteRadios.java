package com.tutorial.deeplayer.app.deeplayer.interactions;

import android.util.Log;

import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.repository.UserDataRepository_Factory;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService_Factory;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by ilya.savritsky on 04.09.2015.
 */
public class GetUserFavouriteRadios extends UseCase {
    private static final String TAG = GetUserFavouriteRadios.class.getSimpleName();
    @Override
    protected Observable buildUseCaseObservable() {
        return UserDataRepository_Factory.create().get().getFavouriteRadiosFromNet();
    }
}
