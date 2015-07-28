package com.tutorial.deeplayer.app.deeplayer.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.fragments.LoginFragment;

import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();

    public LoginActivity() {
        //DeePlayerApp.get().getGraph().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new LoginFragment())
                    .commit();
        }
    }
}

