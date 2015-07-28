package com.tutorial.deeplayer.app.deeplayer.app;

import android.app.Application;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tutorial.deeplayer.app.deeplayer.injections.Graph;

/**
 * Created by ilya.savritsky on 16.07.2015.
 */
public class DeePlayerApp extends Application {

    private static DeePlayerApp _instance;
    private static RefWatcher _refWatcher;
    private Graph graph;


    @NonNull
    public static DeePlayerApp get() {
        return _instance;
    }

    public static RefWatcher getRefWatcher() {
        return DeePlayerApp.get()._refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        graph = Graph.Initializer.init(this);
        _instance = (DeePlayerApp) getApplicationContext();
        _refWatcher = LeakCanary.install(this);
    }

    @NonNull
    public Graph getGraph() {
        return graph;
    }
}
