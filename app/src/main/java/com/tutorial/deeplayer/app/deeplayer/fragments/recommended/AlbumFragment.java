package com.tutorial.deeplayer.app.deeplayer.fragments.recommended;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.data.SchematicDataProvider;
import com.tutorial.deeplayer.app.deeplayer.fragments.BaseAlbumFragment;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RecommendedAlbumsViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedAlbumsView;

import javax.inject.Inject;

public class AlbumFragment extends BaseAlbumFragment {
    public static final String TAG = AlbumFragment.class.getSimpleName();
    @Inject
    RecommendedAlbumsViewModel albumsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recommendedAlbumsView = (RecommendedAlbumsView) view.findViewById(R.id.album_view);
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

    @Override
    protected Uri createUri() {
        return SchematicDataProvider.Albums.recommendedQueryWithArtists(true);
    }
}
