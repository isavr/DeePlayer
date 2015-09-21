package com.tutorial.deeplayer.app.deeplayer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.fragments.RadioFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.chart.ChartsContainerFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.LibraryContainerFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.RecommendationsContainerFragment;
import com.tutorial.deeplayer.app.deeplayer.kMP;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.pojo.Playlist;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
import com.tutorial.deeplayer.app.deeplayer.services.MusicService;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.views.FlowView;
import com.tutorial.deeplayer.app.deeplayer.views.MainActivityView;
import com.tutorial.deeplayer.app.deeplayer.views.PlaylistsView;
import com.tutorial.deeplayer.app.deeplayer.views.RadioView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedAlbumsView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedArtistsView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedTracksView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements MainActivityView.OnMainItemInteractionListener,
        RecommendedAlbumsView.OnAlbumItemInteractionListener,
        RecommendedArtistsView.OnArtistItemInteractionListener,
        RecommendedTracksView.OnTrackItemInteractionListener,
        PlaylistsView.OnPlaylistItemInteractionListener,
        FlowView.OnFlowInteractionListener,
        RadioView.OnRadioItemInteractionListener {

    @Bind(R.id.app_bar)
    android.support.v7.widget.Toolbar toolbar;

    @Bind(R.id.fragment_container)
    @Nullable
    View container;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (container == null) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }
        initNavigationView();
    }

    private void initNavigationView() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            final boolean menuChecked = false;//menuItem.isChecked();
            menuItem.setChecked(!menuChecked);
            //Closing drawer on item click
            drawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.user_library_menu: {
                    selectUserLibrary();
                    return true;
                }
                case R.id.recommendations_menu: {
                    selectHearThis();
                    return true;
                }
                case R.id.new_releases_menu: {
                    return true;
                }
                case R.id.chart_menu: {
                    selectCharts();
                    return true;
                }
                case R.id.mix_menu: {
                    selectMixes();
                    return true;
                }
                default: {
                }
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (kMP.musicService != null) {
            kMP.musicService.updateFavouriteTracksList();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        kMP.initialize(getApplicationContext());
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, kMP.musicConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(kMP.musicConnection);
    }

    @Override
    public void onAlbumItemInteraction(@NonNull Album album) {

    }

    @Override
    public void onArtistItemInteraction(@NonNull Artist artist) {

    }

    @Override
    public void onTrackItemInteraction(@NonNull Track track) {

    }

    @Override
    public void onRadioItemInteraction(@NonNull Radio radio) {

    }

    @Override
    public void onPlaylistItemInteraction(@NonNull Playlist playlist) {

    }

    @Override
    public void onStopProgress() {
        DialogFactory.closeAlertDialog(getSupportFragmentManager());
    }

    @Override
    public void selectItemWithKey(String key) {
        int index = -1;
        switch (key) {
            case "Library": {
                selectUserLibrary();
                index = 0;
                break;
            }
            case "Hear This": {
                selectHearThis();
                index = 1;
                break;
            }
            case "Mixes": {
                selectMixes();
                index = 4;
                break;
            }
            case "Charts": {
                selectCharts();
                index = 3;
                break;
            }
            default: {

            }
        }
        if (index != -1) {
            navigationView.getMenu().getItem(index).setChecked(true);
        }

    }

    private void selectUserLibrary() {
        if (mTwoPane) {
            LibraryContainerFragment fragment = (LibraryContainerFragment) getSupportFragmentManager()
                    .findFragmentByTag(LibraryContainerFragment.TAG);
            if (fragment == null) {
                fragment = new LibraryContainerFragment();
            }
            setupFragment(fragment, LibraryContainerFragment.TAG);
        } else {
            Intent intent = new Intent(this, UserLibraryActivity.class);
            startActivity(intent);
        }
    }

    private void selectHearThis() {
        if (mTwoPane) {
            RecommendationsContainerFragment fragment = (RecommendationsContainerFragment) getSupportFragmentManager()
                    .findFragmentByTag(RecommendationsContainerFragment.TAG);
            if (fragment == null) {
                fragment = new RecommendationsContainerFragment();
            }
            setupFragment(fragment, RecommendationsContainerFragment.TAG);
        } else {
            Intent intent = new Intent(this, RecommendationsActivity.class);
            startActivity(intent);
        }
    }

    private void selectCharts() {
        if (mTwoPane) {
            ChartsContainerFragment fragment = (ChartsContainerFragment) getSupportFragmentManager()
                    .findFragmentByTag(ChartsContainerFragment.TAG);
            if (fragment == null) {
                fragment = new ChartsContainerFragment();
            }
            setupFragment(fragment, ChartsContainerFragment.TAG);
        } else {
            Intent intent = new Intent(this, ChartActivity.class);
            startActivity(intent);
        }
    }

    private void setupFragment(Fragment fragment, final String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }

    private void selectMixes() {
        if (mTwoPane) {
            RadioFragment fragment = (RadioFragment) getSupportFragmentManager()
                    .findFragmentByTag(RadioFragment.TAG);
            if (fragment == null) {
                fragment = new RadioFragment();
            }
            setupFragment(fragment, RadioFragment.TAG);
        } else {
            Intent intent = new Intent(this, MixActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onError(Throwable err) {
        DialogFactory.closeAlertDialog(getSupportFragmentManager());
    }

    @Override
    public void onFlowInteraction(long userId) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && navigationView != null) {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawers();
                return;
            }
        }
        super.onBackPressed();

    }
}
