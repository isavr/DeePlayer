package com.tutorial.deeplayer.app.deeplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.deezer.DeezerDialogListener;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private CompositeSubscription compositeSubscription;
    private DeezerConnect deezerConnect;
    private Observer deezerObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        deezerConnect = new DeezerConnect(getApplicationContext(), getString(R.string.app_id));
        compositeSubscription = new CompositeSubscription();
        deezerObserver = new Observer<Bundle>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "deezer listener received onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "deezer listener received onError");
                e.printStackTrace();
            }

            @Override
            public void onNext(Bundle values) {
                Log.d(TAG, "deezer listener received onNext");
                if (!values.isEmpty()) {
                    SessionStore sessionStore = new SessionStore();
                    sessionStore.save(deezerConnect, LoginActivity.this.getApplicationContext());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @OnClick(R.id.login_button)
    public void onLoginCLick(View v) {
        if (deezerConnect.isSessionValid()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            attemptLogin();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "deezer listener received onResume");
        SessionStore sessionStore = new SessionStore();
        sessionStore.restore(deezerConnect, this);
    }

    public void attemptLogin() {
        // The set of Deezer Permissions needed by the app
        String[] permissions = new String[]{
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY};
        // The listener for authentication events
        //Intent intent = new Intent(this, MainActivity.class);
        DeezerDialogListener listener = new DeezerDialogListener(deezerObserver);
        compositeSubscription.add(Observable.just(listener.getObserver()).subscribe());
        // Launches the authentication process
        deezerConnect.authorize(this, permissions, listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeSubscription.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

