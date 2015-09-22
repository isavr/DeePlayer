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
import com.tutorial.deeplayer.app.deeplayer.data.tables.TrackColumns;
import com.tutorial.deeplayer.app.deeplayer.fragments.BaseFragment;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.ChartedTracksViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedTracksView;

import javax.inject.Inject;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public class ChartedTracksFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = ChartedTracksFragment.class.getSimpleName();
    private static final int LOADER_TRACKS = 40;
    @Inject
    ChartedTracksViewModel trackViewModel;
    private RecommendedTracksView recommendedTrackView;
    private RecommendedTracksView.OnTrackItemInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite_tracks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recommendedTrackView = (RecommendedTracksView) view.findViewById(R.id.tracks_view);
        DialogFactory.showProgressDialog(this.getActivity(),
                getActivity().getSupportFragmentManager());
        trackViewModel.subscribeToDataStore();
        getLoaderManager().initLoader(LOADER_TRACKS, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        recommendedTrackView.setViewModel(trackViewModel);
        recommendedTrackView.setListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        recommendedTrackView.setViewModel(null);
        recommendedTrackView.setListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        trackViewModel.unsubscribeFromDataStore();
        trackViewModel.dispose();
        trackViewModel = null;
        //instrumentation.getLeakTracing().traceLeakage(this);
        DeePlayerApp.getRefWatcher().watch(this, "Recommended Tracks Fragment");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (RecommendedTracksView.OnTrackItemInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTrackItemInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        recommendedTrackView.clean();
        recommendedTrackView.setListener(null);
        getLoaderManager().destroyLoader(LOADER_TRACKS);
        //instrumentation.getLeakTracing().traceLeakage(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), SchematicDataProvider.Tracks.CONTENT_URI, null,
                TrackColumns.POSITION + "!=0", null, TrackColumns.POSITION + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (recommendedTrackView != null) {
            recommendedTrackView.onLoadFinish(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (recommendedTrackView != null) {
            recommendedTrackView.onLoaderReset();
        }
    }
}
