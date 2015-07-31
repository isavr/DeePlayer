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
import com.tutorial.deeplayer.app.deeplayer.viewmodels.RadioViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.RadioView;

import javax.inject.Inject;

public class RadioFragment extends Fragment {
    public static final String TAG = RadioFragment.class.getSimpleName();

    private RadioView radioView;
    @Inject
    RadioViewModel radioViewModel;

    private RadioView.OnRadioItemInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioView = (RadioView) view.findViewById(R.id.radio_view);
        DialogFactory.showProgressDialog(this.getActivity(),
                getActivity().getSupportFragmentManager());
        radioViewModel.subscribeToDataStore();
    }

    @Override
    public void onResume() {
        super.onResume();
        radioView.setViewModel(radioViewModel);
        radioView.setListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        radioView.setViewModel(null);
        radioView.setListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        radioViewModel.unsubscribeFromDataStore();
        radioViewModel.dispose();
        radioViewModel = null;
        DeePlayerApp.getRefWatcher().watch(this, "Radio Fragment");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (RadioView.OnRadioItemInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRadioItemInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        radioView.clean();
        radioView.setListener(null);
    }
}
