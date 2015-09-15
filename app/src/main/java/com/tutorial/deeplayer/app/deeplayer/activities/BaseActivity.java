package com.tutorial.deeplayer.app.deeplayer.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tutorial.deeplayer.app.deeplayer.R;

// from https://github.com/alexdantas/kure-music-player project ( MasterActivity )

/**
 * Master Activity from which every other Activity inherits
 * (except for `ActivityMenuSettings`).
 * <p>
 * If contains some things they all have in common:
 * <p>
 * - They can change the color theme at runtime;
 * - They all have the same context menu (bottom menu).
 * (note that there's an extra item "Now Playing" that
 * only appears if user started playing something)
 * <p>
 * What we do is make each Activity keep track of which
 * theme it currently has.
 * Whenever they have focus, we test to see if the global theme
 * was changed by the user.
 * If it was, it `recreate()`s itself.
 *
 * @note We must call `Activity.setTheme()` BEFORE
 * `Activity.setContentView()`!
 * <p>
 * Sources that made me apply this idea, thank you so much:
 * - http://stackoverflow.com/a/4673209
 * - http://stackoverflow.com/a/11875930
 */
@SuppressLint("Registered") // No need to register this class on AndroidManifest
public class BaseActivity extends AppCompatActivity {
    /**
     * Keeping track of the current theme name.
     *
     * @note It's name and valid values are defined on
     * `res/values/strings.xml`, at the fields
     * we can change on the Settings menu.
     */
    protected String currentTheme = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Mandatory - when creating we don't have
        // a theme applied yet.
        refreshTheme();

        // TODO: think about starting & binding to music service here
        //kMP.startMusicService(this);
    }

    /**
     * Called when the user returns to this activity after leaving.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Every time the user focuses this Activity,
        // we need to check it.
        // It the theme changed, recreate ourselves.
        if (refreshTheme()) {
            recreate();
        }
    }

    /**
     * Tests if our current theme is the same as the one
     * specified on `Settings`, reapplying the theme if
     * not the case.
     *
     * @return Flag that tells if we've changed the theme.
     */
    public boolean refreshTheme() {
        // Getting global theme name from the Settings.
        // Second argument is the default value, in case
        // something went wrong.
        String theme = PreferenceManager.getDefaultSharedPreferences(this).getString("themes", "default");//kMP.settings.get("themes", "default");

        if (currentTheme != theme) {
            setupTheme(theme);
            currentTheme = theme;
            return true;
        }
        return false;
    }

    private void setupTheme(String themeKey) {
        switch (themeKey) {
            case "grey": {
                setTheme(R.style.CustomTheme_Grey);
                break;
            }
            case "indigo": {
                setTheme(R.style.CustomTheme_Indigo);
                break;
            }
            case "blue": {
                setTheme(R.style.CustomTheme_Blue);
                break;
            }
            case "green": {
                setTheme(R.style.CustomTheme_Green);
                break;
            }
            default: {
                setTheme(R.style.CustomTheme_Grey);
            }
        }
    }

    /**
     * Let's set a context menu (menu that appears when
     * the user presses the "menu" button).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Default options specified on the XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Extra option to go to Now Playing screen
        // (only activated when there's an actual Now Playing screen)
        // TODO: check if we should enable now playing menu item
//        if (kMP.mainMenuHasNowPlayingItem) {
//            menu.findItem(R.id.context_menu_now_playing).setVisible(true);
//        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method gets called whenever the user clicks an
     * item on the context menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            // I know it's bad to force quiting the program,
            // but I just love when applications have this option
            case R.id.context_menu_end: {
                // TODO: quit app
                //kMP.forceExit(this);
                break;
            }
            case R.id.context_menu_settings: {
                // TODO: start settings activity
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            }
            case R.id.context_menu_now_playing: {
                // TODO: Show now playing song data
//                Intent nowPlayingIntent = new Intent(this, ActivityNowPlaying.class);
//                nowPlayingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                startActivity(nowPlayingIntent);
                break;
            }
            default: {

            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected int getPersistedItem(final String keyName, final int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt(keyName, defaultValue);
    }

    protected void setPersistedItem(final String keyName, final int position) {
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt(keyName, position).commit();
    }
}
