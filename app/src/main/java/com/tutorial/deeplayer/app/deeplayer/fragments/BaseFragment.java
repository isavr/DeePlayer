package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.support.v4.app.Fragment;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;

/**
 * Created by ilya.savritsky on 16.07.2015.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
        DeePlayerApp.get().getRefWatcher().watch(this);
    }
}
