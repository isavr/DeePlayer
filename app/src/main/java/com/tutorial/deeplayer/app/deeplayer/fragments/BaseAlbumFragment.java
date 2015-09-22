package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.data.tables.AlbumColumns;
import com.tutorial.deeplayer.app.deeplayer.utils.FilterableContent;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedAlbumsView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilya.savritsky on 22.09.2015.
 */
public class BaseAlbumFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, FilterableContent {
    public static final String TAG = BaseAlbumFragment.class.getSimpleName();
    protected static final int LOADER_ALBUMS = 30;
    protected static String BUNDLE_LOADER_KEY_TYPE = "filter";

    @Bind(R.id.album_view)
    protected RecommendedAlbumsView recommendedAlbumsView;
    @Bind(R.id.search_view)
    protected EditText searchView;

    protected RecommendedAlbumsView.OnAlbumItemInteractionListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_container, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected void initLoader(String defValue) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_LOADER_KEY_TYPE, defValue);
        getLoaderManager().initLoader(LOADER_ALBUMS, bundle, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (RecommendedAlbumsView.OnAlbumItemInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAlbumItemInteractionListener");
        }
    }

    @Override
    public void OnFilterUpdate(String filter) {
        Log.d(TAG, "filter updated with -> " + filter);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_LOADER_KEY_TYPE, filter);
        getLoaderManager().restartLoader(LOADER_ALBUMS, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String filterVal = null;
        if (bundle != null) {
            filterVal = bundle.getString(BUNDLE_LOADER_KEY_TYPE, null);
        }
        return new CursorLoader(getActivity(), createUri(), null,
                createFilter(filterVal), null, createSortString());
    }

    protected Uri createUri() {
        return SchematicDataProvider.Albums.CONTENT_URI;
    }

    @Nullable
    protected String createSortString() {
        return " artistSelection.name ASC";
    }

    protected String createFilter(final String filterKeyVal) {
        String selectionString = AlbumColumns.IS_RECOMMENDED + "=1";
        if (filterKeyVal != null && !filterKeyVal.trim().isEmpty()) {
            final String comparedVal = "\'%" + filterKeyVal + "%\'";
            selectionString += " AND ( artistSelection.name LIKE " + comparedVal +
                    "  OR " + AlbumColumns.TITLE + " LIKE " + comparedVal + " )";
        }
        return selectionString;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onResume() {
        super.onResume();
        recommendedAlbumsView.setListener(listener);
        recommendedAlbumsView.setFilterListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        recommendedAlbumsView.setViewModel(null);
        recommendedAlbumsView.setListener(null);
        recommendedAlbumsView.setFilterListener(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        if (recommendedAlbumsView != null) {
            recommendedAlbumsView.clean();
            recommendedAlbumsView.setListener(null);
            recommendedAlbumsView.setFilterListener(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (recommendedAlbumsView != null) {
            recommendedAlbumsView.clean();
            recommendedAlbumsView.setListener(null);
        }
        ButterKnife.unbind(this);
        DeePlayerApp.getRefWatcher().watch(this, "Recommended Artist Fragment");
    }
}
