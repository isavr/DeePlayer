package com.tutorial.deeplayer.app.deeplayer.fragments.recommended;

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
import com.tutorial.deeplayer.app.deeplayer.data.tables.ArtistColumns;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RecommendedArtistViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedArtistsView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArtistFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = ArtistFragment.class.getSimpleName();
    private static final int LOADER_ARTISTS = 20;

    @Bind(R.id.artist_view)
    RecommendedArtistsView recommendedArtistsView;

    @Inject
    RecommendedArtistViewModel artistViewModel;
//    @Inject
//    Instrumentation instrumentation;

    private RecommendedArtistsView.OnArtistItemInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //recommendedArtistsView = (RecommendedArtistsView) view.findViewById(R.id.artist_view);
        DialogFactory.showProgressDialog(this.getActivity(),
                getActivity().getSupportFragmentManager());
        artistViewModel.subscribeToDataStore();
        getLoaderManager().initLoader(LOADER_ARTISTS, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        recommendedArtistsView.setViewModel(artistViewModel);
        recommendedArtistsView.setListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        recommendedArtistsView.setViewModel(null);
        recommendedArtistsView.setListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        artistViewModel.unsubscribeFromDataStore();
        artistViewModel.dispose();
        artistViewModel = null;
        if (recommendedArtistsView != null) {
            recommendedArtistsView.clean();
            recommendedArtistsView.setListener(null);
        }
        ButterKnife.unbind(this);
        DeePlayerApp.getRefWatcher().watch(this, "Recommended Artist Fragment");
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
    public void onDetach() {
        super.onDetach();
        listener = null;
        if (recommendedArtistsView != null) {
            recommendedArtistsView.clean();
            recommendedArtistsView.setListener(null);
        }
        //instrumentation.getLeakTracing().traceLeakage(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), SchematicDataProvider.Artists.CONTENT_URI, null,
                ArtistColumns.IS_RECOMMENDED + "=1", null, null);
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
