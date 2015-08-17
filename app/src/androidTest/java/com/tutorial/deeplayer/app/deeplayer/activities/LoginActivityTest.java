package com.tutorial.deeplayer.app.deeplayer.activities;

import android.test.ActivityInstrumentationTestCase2;

import com.tutorial.deeplayer.app.deeplayer.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by ilya.savritsky on 31.07.2015.
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testSimpleClickAndCheckTest() {
        onView(withId(R.id.login_button)).perform(click());
    }
}
