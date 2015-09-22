package com.tutorial.deeplayer.app.deeplayer.fragments.chart;

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
import com.tutorial.deeplayer.app.deeplayer.fragments.BaseFragment;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.ChartedPlaylistsViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.PlaylistsView;

import javax.inject.Inject;

/**
 * Created by ilya.savritsky on 18.09.2015.
 */
public class ChartedPlaylistsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = ChartedPlaylistsFragment.class.getSimpleName();
    private static final int LOADER_PLAYLISTS = 50;
    @Inject
    ChartedPlaylistsViewModel playlistsViewModel;
    private PlaylistsView playlistsView;
//    @Inject
//    Instrumentation instrumentation;
    private PlaylistsView.OnPlaylistItemInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playlistsView = (PlaylistsView) view.findViewById(R.id.playlist_view);
        DialogFactory.showProgressDialog(this.getActivity(),
                getActivity().getSupportFragmentManager());
        playlistsViewModel.subscribeToDataStore();
        getLoaderManager().initLoader(LOADER_PLAYLISTS, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        playlistsView.setViewModel(playlistsViewModel);
        playlistsView.setListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        playlistsView.setViewModel(null);
        playlistsView.setListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        playlistsViewModel.unsubscribeFromDataStore();
        playlistsViewModel.dispose();
        playlistsViewModel = null;
        //instrumentation.getLeakTracing().traceLeakage(this);
        DeePlayerApp.getRefWatcher().watch(this, "Recommended Albums Fragment");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (PlaylistsView.OnPlaylistItemInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPlaylistItemInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        playlistsView.clean();
        playlistsView.setListener(null);
        //instrumentation.getLeakTracing().traceLeakage(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return new CursorLoader(getActivity(), SchematicDataProvider.Albums.CONTENT_URI, null,
//                AlbumColumns.IS_RECOMMENDED + "=1", null, null);
        return new CursorLoader(getActivity(), SchematicDataProvider.Playlists.CONTENT_URI, null,
                null, null, null);
        // PlaylistColumns.POSITION + "!=0", null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (playlistsView != null) {
            playlistsView.onLoadFinish(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (playlistsView != null) {
            playlistsView.onLoaderReset();
        }
    }
}

