package com.tutorial.deeplayer.app.deeplayer.fragments.library;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.tables.RadioColumns;
import com.tutorial.deeplayer.app.deeplayer.fragments.BaseRadioFragment;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.FavouriteRadiosViewModel;

import javax.inject.Inject;

/**
 * Created by ilya.savritsky on 04.09.2015.
 */
public class FavouriteRadiosFragment extends BaseRadioFragment {
    public static final String TAG = FavouriteRadiosFragment.class.getSimpleName();


    @Inject
    FavouriteRadiosViewModel radioViewModel;

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
    public void onResume() {
        super.onResume();
        radioView.setViewModel(radioViewModel);
        radioView.setListener(listener);
        radioView.setFilterListener(this);
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
    protected String createFilter(String filterKeyVal) {
        String selectionString = RadioColumns.IS_FAVOURITE + "=1";
        if (filterKeyVal != null && !filterKeyVal.trim().isEmpty()) {
            selectionString += " AND " + RadioColumns.TITLE + " LIKE " + "\'%" + filterKeyVal + "%\'";
        }
        return selectionString;
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
