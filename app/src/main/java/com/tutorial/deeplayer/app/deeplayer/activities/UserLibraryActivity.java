package com.tutorial.deeplayer.app.deeplayer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.adapters.UserLibrarySectionsPagerAdapter;
import com.tutorial.deeplayer.app.deeplayer.kMP;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
import com.tutorial.deeplayer.app.deeplayer.services.MusicService;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.views.RadioView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendationsTypes;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedAlbumsView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedArtistsView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedTracksView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserLibraryActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
        RecommendedAlbumsView.OnAlbumItemInteractionListener,
        RecommendedArtistsView.OnArtistItemInteractionListener,
        RecommendedTracksView.OnTrackItemInteractionListener,
        RadioView.OnRadioItemInteractionListener {
    private static final String TAG = UserLibraryActivity.class.getSimpleName();
    private static final String LIBRARY_TAB_KEY = "library_control_val";
    private static final int LIBRARY_DEF_VALUE = 0;

    @Bind(R.id.app_bar)
    android.support.v7.widget.Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    UserLibrarySectionsPagerAdapter mSectionsPagerAdapter;

    @Bind(R.id.pager)
    ViewPager mViewPager;

    @Override
    public void onAlbumItemInteraction(@NonNull Album album) {
        if (kMP.musicService != null) {
            initMusicService(RecommendationsTypes.Albums);
            kMP.musicService.setData(album);
            kMP.musicService.play();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initMusicService(RecommendationsTypes type) {
        switch (type) {
            case Artists: {
                kMP.musicService.initPlayer(MusicService.PlayerType.ARTIST);
                break;
            }
            case Albums: {
                kMP.musicService.initPlayer(MusicService.PlayerType.ALBUM);
                break;
            }
            case Tracks: {
                kMP.musicService.initPlayer(MusicService.PlayerType.TRACK);
                break;
            }
            case Flow: {
                kMP.musicService.initPlayer(MusicService.PlayerType.FLOW);
                break;
            }
            default: {

            }
        }
    }

    @Override
    public void onArtistItemInteraction(@NonNull Artist artist) {
        if (kMP.musicService != null) {
            initMusicService(RecommendationsTypes.Artists);
            kMP.musicService.setData(artist);
            kMP.musicService.play();
        }
    }

    @Override
    public void onRadioItemInteraction(@NonNull Radio radio) {
        if (kMP.musicService != null) {
            kMP.musicService.initPlayer(MusicService.PlayerType.RADIO);
            kMP.musicService.setData(radio);
            kMP.musicService.play();
        }
    }

    @Override
    public void onTrackItemInteraction(@NonNull Track track) {
        if (kMP.musicService != null) {
            initMusicService(RecommendationsTypes.Tracks);
            kMP.musicService.setData(track);
            kMP.musicService.play();
        }
    }

    @Override
    public void onStopProgress() {
        DialogFactory.closeAlertDialog(getSupportFragmentManager());
    }

    @Override
    public void onError(Throwable err) {
        DialogFactory.closeAlertDialog(getSupportFragmentManager());
        //DialogFactory.showSimpleErrorMessage(this, getSupportFragmentManager(), err.getMessage());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_library);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewPager.setNestedScrollingEnabled(true);
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mSectionsPagerAdapter = new UserLibrarySectionsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        final int tabPosition = getPersistedItem(LIBRARY_TAB_KEY, LIBRARY_DEF_VALUE);
        mViewPager.setCurrentItem(tabPosition);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    protected void onDestroy() {
        super.onDestroy();
        tabLayout.removeAllTabs();
        tabLayout.setOnTabSelectedListener(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (mViewPager != null) {
            final int position = tab.getPosition();
            Log.d(TAG, "tab selected -> " + position);
            mViewPager.setCurrentItem(position);
            setPersistedItem(LIBRARY_TAB_KEY, position);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab != null) {
            final int position = tab.getPosition();
            Log.d(TAG, "tab unselected -> " + position);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab != null) {
            final int position = tab.getPosition();
            Log.d(TAG, "tab reselected -> " + position);
        }
    }
}
