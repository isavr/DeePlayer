package com.tutorial.deeplayer.app.deeplayer.app;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by ilya.savritsky on 16.07.2015.
 */
public class DeePlayerApp extends Application {

    private static DeePlayerApp _instance;
    private RefWatcher _refWatcher;

    public static DeePlayerApp get() {
        return _instance;
    }

    public static RefWatcher getRefWatcher() {
        return DeePlayerApp.get()._refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = (DeePlayerApp) getApplicationContext();
        _refWatcher = LeakCanary.install(this);
    }
}
