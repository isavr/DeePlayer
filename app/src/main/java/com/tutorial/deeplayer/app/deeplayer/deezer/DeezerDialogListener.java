package com.tutorial.deeplayer.app.deeplayer.deezer;

import android.os.Bundle;
import android.util.Log;

import com.deezer.sdk.network.connect.event.DialogListener;

import rx.Observer;

/**
 * Created by ilya.savritsky on 16.07.2015.
 */
public class DeezerDialogListener implements DialogListener {
    public static final String TAG = DeezerDialogListener.class.getSimpleName();

    private Observer observer;

    public DeezerDialogListener(Observer observer) {
        this.observer = observer;
    }

    public Observer getObserver() {
        return observer;
    }


    @Override
    public void onComplete(Bundle values) {
        observer.onNext(values);
    }

    @Override
    public void onCancel() {
        observer.onNext(new Bundle());
    }

    @Override
    public void onException(Exception e) {
        observer.onError(e);
    }

}
