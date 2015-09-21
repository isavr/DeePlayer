package com.tutorial.deeplayer.app.deeplayer.fragments.library;

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
import com.tutorial.deeplayer.app.deeplayer.data.tables.AlbumColumns;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.BaseFragment;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.FavouriteAlbumsViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedAlbumsView;

import javax.inject.Inject;

/**
 * Created by ilya.savritsky on 04.09.2015.
 */
public class FavouriteAlbumsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = FavouriteAlbumsFragment.class.getSimpleName();
    private static final int LOADER_ALBUMS = 30;

    private RecommendedAlbumsView favouriteAlbumsView;
    @Inject
    FavouriteAlbumsViewModel albumsViewModel;

    private RecommendedAlbumsView.OnAlbumItemInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favouriteAlbumsView = (RecommendedAlbumsView) view.findViewById(R.id.album_view);
        DialogFactory.showProgressDialog(this.getActivity(),
                getActivity().getSupportFragmentManager());
        albumsViewModel.subscribeToDataStore();
        getLoaderManager().initLoader(LOADER_ALBUMS, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        favouriteAlbumsView.setViewModel(albumsViewModel);
        favouriteAlbumsView.setListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        favouriteAlbumsView.setViewModel(null);
        favouriteAlbumsView.setListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        albumsViewModel.unsubscribeFromDataStore();
        albumsViewModel.dispose();
//        albumsViewModel = null;
        //instrumentation.getLeakTracing().traceLeakage(this);
        DeePlayerApp.getRefWatcher().watch(this, "Recommended Albums Fragment");
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
    public void onDetach() {
        super.onDetach();
        listener = null;
        favouriteAlbumsView.clean();
        favouriteAlbumsView.setListener(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), SchematicDataProvider.Albums.queryWithArtists(true), null,
                AlbumColumns.IS_FAVOURITE + "=1", null, " artistSelection.name ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (favouriteAlbumsView != null) {
            favouriteAlbumsView.onLoadFinish(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (favouriteAlbumsView != null) {
            favouriteAlbumsView.onLoaderReset();
        }
    }
}
