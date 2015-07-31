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
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RecommendedArtistViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedArtistsView;

import javax.inject.Inject;


public class ArtistFragment extends Fragment {
    public static final String TAG = AlbumFragment.class.getSimpleName();

    private RecommendedArtistsView recommendedArtistsView;
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
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recommendedArtistsView = (RecommendedArtistsView) view.findViewById(R.id.artist_view);
        DialogFactory.showProgressDialog(this.getActivity(),
                getActivity().getSupportFragmentManager());
        artistViewModel.subscribeToDataStore();
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
        //instrumentation.getLeakTracing().traceLeakage(this);
        DeePlayerApp.getRefWatcher().watch(this, "Recommended Albums Fragment");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (RecommendedArtistsView.OnArtistItemInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAlbumItemInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        recommendedArtistsView.clean();
        recommendedArtistsView.setListener(null);
        //instrumentation.getLeakTracing().traceLeakage(this);
    }

}
