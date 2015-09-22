package com.tutorial.deeplayer.app.deeplayer.fragments.chart;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.tables.ArtistColumns;
import com.tutorial.deeplayer.app.deeplayer.fragments.BaseArtistFragment;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.ChartedArtistsViewModel;

import javax.inject.Inject;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public class ChartedArtistsFragment extends BaseArtistFragment {
    public static final String TAG = ChartedArtistsFragment.class.getSimpleName();

    @Inject
    ChartedArtistsViewModel artistViewModel;

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

        artistViewModel.subscribeToDataStore();
        artistViewModel.subscribeToFilterUpdates(searchView);

        initLoader("");
    }

    @Override
    public void onResume() {
        super.onResume();
        recommendedArtistsView.setViewModel(artistViewModel);
    }

    @Override
    public void onDestroyView() {
        artistViewModel.unsubscribeFromDataStore();
        artistViewModel.dispose();
        artistViewModel = null;
        super.onDestroyView();
    }

    @Override
    protected String createFilter(String filterKeyVal) {
        String selectionString = ArtistColumns.POSITION + "!=0";
        if (filterKeyVal != null && !filterKeyVal.trim().isEmpty()) {
            selectionString += " AND " + ArtistColumns.NAME + " LIKE " + "\'%" + filterKeyVal + "%\'";
        }
        return selectionString;
    }

    @Override
    protected String createSortString() {
        return ArtistColumns.POSITION + " ASC";
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (recommendedArtistsView != null) {
            recommendedArtistsView.onLoadFinish(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (recommendedArtistsView != null) {
            recommendedArtistsView.onLoaderReset();
        }
    }
}