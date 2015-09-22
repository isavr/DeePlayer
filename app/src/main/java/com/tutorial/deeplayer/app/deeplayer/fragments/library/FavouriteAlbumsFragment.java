package com.tutorial.deeplayer.app.deeplayer.fragments.library;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.data.tables.AlbumColumns;
import com.tutorial.deeplayer.app.deeplayer.fragments.BaseAlbumFragment;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.FavouriteAlbumsViewModel;

import javax.inject.Inject;

/**
 * Created by ilya.savritsky on 04.09.2015.
 */
public class FavouriteAlbumsFragment extends BaseAlbumFragment {
    public static final String TAG = FavouriteAlbumsFragment.class.getSimpleName();

    @Inject
    FavouriteAlbumsViewModel albumsViewModel;

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
        return SchematicDataProvider.Albums.queryWithArtists(true);
    }

    @Override
    protected String createFilter(final String filterKeyVal) {
        String selectionString = AlbumColumns.IS_FAVOURITE + "=1";
        if (filterKeyVal != null && !filterKeyVal.trim().isEmpty()) {
            final String comparedVal = "\'%" + filterKeyVal + "%\'";
            selectionString += " AND ( artistSelection.name LIKE " + comparedVal +
                    "  OR " + AlbumColumns.TITLE + " LIKE " + comparedVal + " )";
        }
        return selectionString;
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
