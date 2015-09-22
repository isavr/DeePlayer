package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.app.Activity;
import android.database.Cursor;
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
import com.tutorial.deeplayer.app.deeplayer.data.tables.ArtistColumns;
import com.tutorial.deeplayer.app.deeplayer.utils.FilterableContent;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedArtistsView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilya.savritsky on 22.09.2015.
 */
public class BaseArtistFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        FilterableContent {
    public static final String TAG = BaseArtistFragment.class.getSimpleName();
    protected static final int LOADER_ARTISTS = 20;

    protected static String BUNDLE_LOADER_KEY_TYPE = "filter";

    @Bind(R.id.artist_view)
    protected RecommendedArtistsView recommendedArtistsView;
    @Bind(R.id.search_view)
    protected EditText searchView;

    protected RecommendedArtistsView.OnArtistItemInteractionListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists_container, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected void initLoader(String defValue) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_LOADER_KEY_TYPE, defValue);
        getLoaderManager().initLoader(LOADER_ARTISTS, bundle, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (RecommendedArtistsView.OnArtistItemInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnArtistItemInteractionListener");
        }
    }

    @Override
    public void OnFilterUpdate(String filter) {
        Log.d(TAG, "filter updated with -> " + filter);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_LOADER_KEY_TYPE, filter);
        getLoaderManager().restartLoader(LOADER_ARTISTS, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String filterVal = null;
        if (bundle != null) {
            filterVal = bundle.getString(BUNDLE_LOADER_KEY_TYPE, null);
        }
        return new CursorLoader(getActivity(), SchematicDataProvider.Artists.CONTENT_URI, null,
                createFilter(filterVal), null, createSortString());
    }

    @Nullable
    protected String createSortString() {
        return null;
    }

    protected String createFilter(String filterKeyVal) {
        String selectionString = ArtistColumns.IS_RECOMMENDED + "=1";
        if (filterKeyVal != null && !filterKeyVal.trim().isEmpty()) {
            selectionString += " AND " + ArtistColumns.NAME + " LIKE " + "\'%" + filterKeyVal + "%\'";
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
        recommendedArtistsView.setListener(listener);
        recommendedArtistsView.setFilterListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        recommendedArtistsView.setViewModel(null);
        recommendedArtistsView.setListener(null);
        recommendedArtistsView.setFilterListener(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        if (recommendedArtistsView != null) {
            recommendedArtistsView.clean();
            recommendedArtistsView.setListener(null);
            recommendedArtistsView.setFilterListener(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (recommendedArtistsView != null) {
            recommendedArtistsView.clean();
            recommendedArtistsView.setListener(null);
        }
        ButterKnife.unbind(this);
        DeePlayerApp.getRefWatcher().watch(this, "Recommended Artist Fragment");
    }
}
