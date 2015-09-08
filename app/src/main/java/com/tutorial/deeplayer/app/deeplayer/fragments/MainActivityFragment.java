package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.BaseFragment;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.MainViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.MainActivityView;

import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {
    private static final String TAG = MainActivityFragment.class.getSimpleName();


    private MainActivityView mainActivityView;
    @Inject
    MainViewModel viewModel;
//    @Inject
//    Instrumentation instrumentation;

    //
    //private CompositeSubscription compositeSubscription;
    //private RxDatabase rxCupboard = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DeePlayerApp.get().getGraph().inject(this);
        //viewModel = new MainViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivityView = (MainActivityView) view.findViewById(R.id.main_view);
        viewModel.subscribeToDataStore();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivityView.setViewModel(viewModel);
    }

    @Override
    public void onPause() {
        super.onPause();
        mainActivityView.setViewModel(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.unsubscribeFromDataStore();
        viewModel.dispose();
        viewModel = null;
        //instrumentation.getLeakTracing().traceLeakage(this);
        DeePlayerApp.getRefWatcher().watch(this, "Main Fragment");
    }
}
