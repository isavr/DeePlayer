package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.BaseFragment;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RadioViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.RadioView;

import javax.inject.Inject;

public class RadioFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = RadioFragment.class.getSimpleName();
    private static final int LOADER_RADIOS = 10;

    private RadioView radioView;
    @Inject
    RadioViewModel radioViewModel;

    private RadioView.OnRadioItemInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioView = (RadioView) view.findViewById(R.id.radio_view);
        DialogFactory.showProgressDialog(this.getActivity(),
                getActivity().getSupportFragmentManager());
        radioViewModel.subscribeToDataStore();
        getLoaderManager().initLoader(LOADER_RADIOS, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        radioView.setViewModel(radioViewModel);
        radioView.setListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        radioView.setViewModel(null);
        radioView.setListener(null);
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
    public void onDetach() {
        super.onDetach();
        listener = null;
        radioView.clean();
        radioView.setListener(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(getActivity(), SchematicDataProvider.Radios.CONTENT_URI, null, null, null, null);
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
