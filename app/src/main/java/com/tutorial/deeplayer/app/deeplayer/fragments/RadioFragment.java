package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RadioViewModel;

import javax.inject.Inject;

public class RadioFragment extends BaseRadioFragment {
    public static final String TAG = RadioFragment.class.getSimpleName();

    @Inject
    RadioViewModel radioViewModel;

    @Override
    public void onResume() {
        super.onResume();
        radioView.setViewModel(radioViewModel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        radioViewModel.unsubscribeFromDataStore();
        radioViewModel.dispose();
        radioViewModel = null;
        DeePlayerApp.getRefWatcher().watch(this, "Radio Fragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DialogFactory.showProgressDialog(this.getActivity(),
                getActivity().getSupportFragmentManager());

        radioViewModel.subscribeToDataStore();
        radioViewModel.subscribeToFilterUpdates(searchView);

        initLoader("");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (radioView != null) {
            radioView.onLoadFinish(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (radioView != null) {
            radioView.onLoaderReset();
        }
    }
}
