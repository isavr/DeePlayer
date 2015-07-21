package com.tutorial.deeplayer.app.deeplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.deezer.sdk.model.Album;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.User;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.request.DeezerRequest;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.deezer.DeezerDialogListener;

import rx.Observable;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private CompositeSubscription compositeSubscription;
    private DeezerConnect deezerConnect;
    private Observer deezerObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "deezer listener received onResume");
        SessionStore sessionStore = new SessionStore();
        if (sessionStore.restore(deezerConnect, this)) {
            // The restored session is valid, navigate to the Home Activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            attemptLogin();
        }
    }

    public void attemptLogin() {
        // The set of Deezer Permissions needed by the app
        String[] permissions = new String[]{
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY};
        // The listener for authentication events
        Intent intent = new Intent(this, MainActivity.class);
        DeezerDialogListener listener = new DeezerDialogListener(deezerObserver);
        compositeSubscription.add(Observable.just(listener.getObserver()).subscribe());
        // Launches the authentication process
        deezerConnect.authorize(this, permissions, listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}

