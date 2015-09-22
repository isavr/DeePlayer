package com.tutorial.deeplayer.app.deeplayer.fragments.chart;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.View;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.data.tables.AlbumColumns;
import com.tutorial.deeplayer.app.deeplayer.fragments.BaseAlbumFragment;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.ChartedAlbumsViewModel;

import javax.inject.Inject;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public class ChartedAlbumsFragment extends BaseAlbumFragment {
    public static final String TAG = ChartedAlbumsFragment.class.getSimpleName();

    @Inject
    ChartedAlbumsViewModel albumsViewModel;

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

        albumsViewModel.subscribeToDataStore();
        albumsViewModel.subscribeToFilterUpdates(searchView);

        initLoader("");
    }

    @Override
    public void onResume() {
        super.onResume();
        recommendedAlbumsView.setViewModel(albumsViewModel);
    }

    @Override
    public void onDestroyView() {
        albumsViewModel.unsubscribeFromDataStore();
        albumsViewModel.dispose();
        albumsViewModel = null;
        super.onDestroyView();
    }

    @Override
    protected Uri createUri() {
        return SchematicDataProvider.Albums.CONTENT_URI;//SchematicDataProvider.Albums.chartedQueryWithArtists(0);
    }

    @Override
    protected String createFilter(final String filterKeyVal) {
        // TODO: fix search through artists names
        String selectionString = AlbumColumns.POSITION + "!=0";
        if (filterKeyVal != null && !filterKeyVal.trim().isEmpty()) {
            final String comparedVal = "\'%" + filterKeyVal + "%\'";
            selectionString += " AND ( " + AlbumColumns.TITLE + " LIKE " + comparedVal + " )";
//            selectionString += " AND ( artistSelection.name LIKE " + comparedVal +
//                    "  OR " + AlbumColumns.TITLE + " LIKE " + comparedVal + " )";
        }
        return selectionString;
    }

    @Nullable
    protected String createSortString() {
        return AlbumColumns.POSITION + " ASC";
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (recommendedAlbumsView != null) {
            recommendedAlbumsView.onLoadFinish(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (recommendedAlbumsView != null) {
            recommendedAlbumsView.onLoaderReset();
        }
    }
}
