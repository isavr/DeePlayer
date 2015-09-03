package com.tutorial.deeplayer.app.deeplayer.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.activities.MainActivity;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.utils.RxBinderUtil;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.LoginViewModel;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;

/**
 * Created by ilya.savritsky on 24.07.2015.
 */
public class LoginView extends LinearLayout {
    public static final String TAG = LoginView.class.getSimpleName();

    private final RxBinderUtil rxBinderUtil = new RxBinderUtil(this);
    @Inject
    LoginViewModel loginViewModel;

    @Bind(R.id.login_button)
    Button loginButton;

    private DeezerConnect deezerConnect;
    //private Observer deezerObserver;

    public LoginView(Context context) {
        super(context, null);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setupChildren();
    }


    private void setupChildren() {
        ButterKnife.bind(this);
        deezerConnect = new DeezerConnect(DeePlayerApp.get(),
                DeePlayerApp.get().getString(R.string.app_id));
        SessionStore sessionStore = new SessionStore();
        sessionStore.restore(deezerConnect, DeePlayerApp.get());
    }

    public void setViewModel(@Nullable LoginViewModel viewModel) {
        rxBinderUtil.clear();
        if (viewModel != null) {

            loginViewModel = viewModel;
            //rxBinderUtil.bindProperty(viewModel.getRepository(), this::loginSuccessfull);
            rxBinderUtil.bindProperty(viewModel.getSubject(), this::loginSuccessfull, this::onError);
            rxBinderUtil.bindProperty(ViewObservable.clicks(loginButton, false), this::attemptLogin, this::onError);
        }
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, "Handle Error");
    }


    private void loginSuccessfull(Bundle bundle) {
        SessionStore store = new SessionStore();
        store.save(deezerConnect, getContext());
        Intent intent = new Intent(getContext(), MainActivity.class);
        getContext().startActivity(intent);
    }

    private void attemptLogin(OnClickEvent onClickEvent) {
        if (deezerConnect != null && loginViewModel != null) {
//            loginSuccessfull(null);
//        } else {
            String[] permissions = new String[]{
                    Permissions.BASIC_ACCESS,
                    Permissions.MANAGE_LIBRARY,
                    Permissions.DELETE_LIBRARY
                    //Permissions.LISTENING_HISTORY,
            };
            // The listener for authentication events
            //Intent intent = new Intent(this, MainActivity.class);
            //DeezerDialogListener listener = new DeezerDialogListener(deezerObserver);
            //compositeSubscription.add(listener.getObservable().subscribe());
            // Launches the authentication process
            deezerConnect.authorize((Activity) getContext(), permissions, loginViewModel.getListener());
        }
    }
}
