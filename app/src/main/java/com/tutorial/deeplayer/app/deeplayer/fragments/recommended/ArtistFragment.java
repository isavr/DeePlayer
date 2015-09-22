package com.tutorial.deeplayer.app.deeplayer.fragments.recommended;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.fragments.BaseArtistFragment;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RecommendedArtistViewModel;

import javax.inject.Inject;

public class ArtistFragment extends BaseArtistFragment {
    public static final String TAG = ArtistFragment.class.getSimpleName();

    @Inject
    RecommendedArtistViewModel artistViewModel;

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
