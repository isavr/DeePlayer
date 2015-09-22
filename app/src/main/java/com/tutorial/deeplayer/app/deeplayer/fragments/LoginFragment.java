package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.LoginViewModel;
import com.tutorial.deeplayer.app.deeplayer.views.LoginView;

import javax.inject.Inject;

/**
 * Created by ilya.savritsky on 24.07.2015.
 */
public class LoginFragment extends BaseFragment implements LoginView.OnLoginInteractionListener {
    public static final String TAG = LoginFragment.class.getSimpleName();
    @Inject
    LoginViewModel loginViewModel;
    private LoginView.OnLoginInteractionListener listener;
    private LoginView loginView;
//    @Inject
//    Instrumentation instrumentation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DeePlayerApp.get().getGraph().inject(this);
        //loginViewModel = new LoginViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = this;
        loginView = (LoginView) view.findViewById(R.id.login_view);
        loginViewModel.subscribeToDataStore();
        loginView.setListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateViewModel();
    }

    public void updateViewModel() {
        if (loginViewModel != null) {
            loginViewModel.unsubscribeFromDataStore();
            loginViewModel = new LoginViewModel();
            loginViewModel.subscribeToDataStore();
        }
        loginView.setViewModel(loginViewModel);
    }

    @Override
    public void onPause() {
        super.onPause();
        loginView.setViewModel(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginViewModel.unsubscribeFromDataStore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loginViewModel.dispose();
        loginViewModel = null;
        listener = null;
        //instrumentation.getLeakTracing().traceLeakage(this);
        DeePlayerApp.getRefWatcher().watch(this, "Login Fragment");
    }

    @Override
    public void onError(Throwable err) {
        updateViewModel();
    }
}
