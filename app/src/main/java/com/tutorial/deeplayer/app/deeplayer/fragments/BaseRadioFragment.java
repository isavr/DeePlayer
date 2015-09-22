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
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.data.tables.RadioColumns;
import com.tutorial.deeplayer.app.deeplayer.utils.FilterableContent;
import com.tutorial.deeplayer.app.deeplayer.views.RadioView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilya.savritsky on 21.09.2015.
 */
public class BaseRadioFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, FilterableContent {
    public static final String TAG = RadioFragment.class.getSimpleName();
    protected static final int LOADER_RADIOS = 10;
    protected static String BUNDLE_LOADER_KEY_TYPE = "filter";


    @Bind(R.id.radio_view)
    protected RadioView radioView;
    @Bind(R.id.search_view)
    protected EditText searchView;

    protected RadioView.OnRadioItemInteractionListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mix_container, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (RadioView.OnRadioItemInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRadioItemInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        radioView.setListener(listener);
        radioView.setFilterListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        radioView.clean();
        radioView.setListener(null);
        radioView.setFilterListener(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        radioView.setViewModel(null);
        radioView.setListener(null);
        radioView.setFilterListener(null);
    }

    protected void initLoader(String defValue) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_LOADER_KEY_TYPE, defValue);
        getLoaderManager().initLoader(LOADER_RADIOS, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String filterVal = null;
        if (bundle != null) {
            filterVal = bundle.getString(BUNDLE_LOADER_KEY_TYPE, null);
        }
        return new CursorLoader(getActivity(), SchematicDataProvider.Radios.CONTENT_URI, null,
                createFilter(filterVal), null, createSortString());
    }

    protected String createFilter(String filterKeyVal) {
        String selectionString = null;
        if (filterKeyVal != null && !filterKeyVal.trim().isEmpty()) {
            selectionString = RadioColumns.TITLE + " LIKE " + "\'%" + filterKeyVal + "%\'";
        }
        return selectionString;
    }

    @Nullable
    protected String createSortString() {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void OnFilterUpdate(String filter) {
        Log.d(TAG, "filter updated with -> " + filter);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_LOADER_KEY_TYPE, filter);
        getLoaderManager().restartLoader(LOADER_RADIOS, bundle, this);
    }
}
