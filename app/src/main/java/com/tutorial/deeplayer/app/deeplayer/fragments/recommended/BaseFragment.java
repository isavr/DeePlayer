package com.tutorial.deeplayer.app.deeplayer.fragments.recommended;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.utils.Utils;

import rx.Observable;
import rx.android.lifecycle.LifecycleEvent;
import rx.subjects.BehaviorSubject;

/**
 * Created by ilya.savritsky on 16.07.2015.
 */
public class BaseFragment extends Fragment {
    private final BehaviorSubject<LifecycleEvent> lifecycleSubject = BehaviorSubject.create();

    public Observable<LifecycleEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(LifecycleEvent.CREATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(LifecycleEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(LifecycleEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(LifecycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(LifecycleEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY);
        super.onDestroy();
        DeePlayerApp.getRefWatcher().watch(this);
        Utils.unbindDrawables(this.getView());
        //DeePlayerApp.getRefWatcher().watch(lifecycleSubject);
    }
}
