package com.tutorial.deeplayer.app.deeplayer.fragments.recommended;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendationsControlsView;

/**
 * Created by ilya.savritsky on 30.07.2015.
 */
public class RecommendationsControlsFragment extends Fragment {
    public static final String TAG = RecommendationsControlsFragment.class.getSimpleName();

    private RecommendationsControlsView recommendationsControlsView;

    private RecommendationsControlsView.OnTypeSelectedListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommendations_controls, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recommendationsControlsView = (RecommendationsControlsView) view.findViewById(R.id.controls);
    }

    @Override
    public void onResume() {
        super.onResume();
        recommendationsControlsView.setListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        recommendationsControlsView.setListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DeePlayerApp.getRefWatcher().watch(this, "Recommended Albums Fragment");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (RecommendationsControlsView.OnTypeSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTypeSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
