package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RecommendedAlbumsViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedAlbumsView;

import javax.inject.Inject;

public class AlbumFragment extends Fragment {
    public static final String TAG = AlbumFragment.class.getSimpleName();

    private RecommendedAlbumsView recommendedAlbumsView;
    @Inject
    RecommendedAlbumsViewModel albumsViewModel;
//    @Inject
//    Instrumentation instrumentation;

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
        recommendedAlbumsView = (RecommendedAlbumsView) view.findViewById(R.id.album_view);
        albumsViewModel.subscribeToDataStore();
    }

    @Override
    public void onResume() {
        super.onResume();
        recommendedAlbumsView.setViewModel(albumsViewModel);
        recommendedAlbumsView.setListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        recommendedAlbumsView.setViewModel(null);
        recommendedAlbumsView.setListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        albumsViewModel.unsubscribeFromDataStore();
        albumsViewModel.dispose();
        albumsViewModel = null;
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
        recommendedAlbumsView.clean();
        recommendedAlbumsView.setListener(null);
        //instrumentation.getLeakTracing().traceLeakage(this);
    }

}
