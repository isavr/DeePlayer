package com.tutorial.deeplayer.app.deeplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class LoginFragment extends Fragment {
    public static final String TAG = LoginFragment.class.getSimpleName();

    private LoginView loginView;
    @Inject
    LoginViewModel loginViewModel;
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
        loginView = (LoginView) view.findViewById(R.id.login_view);
        loginViewModel.subscribeToDataStore();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        //instrumentation.getLeakTracing().traceLeakage(this);
        DeePlayerApp.get().getRefWatcher().watch(this, "Login Fragment");
    }
}
