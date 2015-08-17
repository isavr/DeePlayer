package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RecommendedTrackViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedTracksView;

import javax.inject.Inject;

/**
 * Created by ilya.savritsky on 17.08.2015.
 */
public class RecommendedTracksFragment extends Fragment {
    public static final String TAG = RecommendedTracksFragment.class.getSimpleName();

    private RecommendedTracksView recommendedTrackView;
    @Inject
    RecommendedTrackViewModel trackViewModel;
//    @Inject
//    Instrumentation instrumentation;

    private RecommendedTracksView.OnTrackItemInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recommendedTrackView = (RecommendedTracksView) view.findViewById(R.id.tracks_view);
        DialogFactory.showProgressDialog(this.getActivity(),
                getActivity().getSupportFragmentManager());
        trackViewModel.subscribeToDataStore();
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
        //instrumentation.getLeakTracing().traceLeakage(this);
    }
}
