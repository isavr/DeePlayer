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
import com.tutorial.deeplayer.app.deeplayer.adapters.RecommendationsSectionsPagerAdapter;
import com.tutorial.deeplayer.app.deeplayer.kMP;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.services.MusicService;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.views.FlowView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendationsTypes;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedAlbumsView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedArtistsView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedTracksView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilya.savritsky on 28.07.2015.
 */
public class RecommendationsActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
        RecommendedAlbumsView.OnAlbumItemInteractionListener,
        RecommendedArtistsView.OnArtistItemInteractionListener,
        RecommendedTracksView.OnTrackItemInteractionListener,
        FlowView.OnFlowInteractionListener {
    public static final String TAG = RecommendationsActivity.class.getSimpleName();
    private static final String RECOMMENDATIONS_TAB_KEY = "recommendations_control_val";
    private static final int RECOMMENDATIONS_DEF_VALUE = 0;

    @Bind(R.id.app_bar)
    android.support.v7.widget.Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    RecommendationsSectionsPagerAdapter mSectionsPagerAdapter;

    @Bind(R.id.pager)
    ViewPager mViewPager;
//    private DeezerConnect deezerConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewPager.setNestedScrollingEnabled(true);
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mSectionsPagerAdapter = new RecommendationsSectionsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        final int tabPosition = getPersistedItem(RECOMMENDATIONS_TAB_KEY, RECOMMENDATIONS_DEF_VALUE);
        mViewPager.setCurrentItem(tabPosition);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(this);
        // deezer
//        deezerConnect = new DeezerConnect(DeePlayerApp.get(), getString(R.string.app_id));
//        SessionStore sessionStore = new SessionStore();
//        sessionStore.restore(deezerConnect, getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, kMP.musicConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mix, menu);
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
    public void onError(Throwable err) {
        DialogFactory.closeAlertDialog(getSupportFragmentManager());
        //DialogFactory.showSimpleErrorMessage(this, getSupportFragmentManager(), err.getMessage());
    }

    @Override
    public void onAlbumItemInteraction(@NonNull Album album) {
        if (kMP.musicService != null) {
            initMusicService(RecommendationsTypes.Albums);
            kMP.musicService.setData(album);
            kMP.musicService.play();
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
    public void onTrackItemInteraction(@NonNull com.tutorial.deeplayer.app.deeplayer.pojo.Track track) {
        if (kMP.musicService != null) {
            initMusicService(RecommendationsTypes.Tracks);
            kMP.musicService.setData(track);
            kMP.musicService.play();
        }
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
    public void onFlowInteraction(long userId) {
        Log.d(TAG, "try yo play the FLOW");
        if (kMP.musicService != null) {
            initMusicService(RecommendationsTypes.Flow);
            Radio radio = new Radio();
            radio.setId(userId);
            radio.setTitle("User Flow");
            radio.setType("radio");
            radio.setDescription("User Flow radio");
            kMP.musicService.setData(radio);
            kMP.musicService.play();
        }
    }

    @Override
    public void onStopProgress() {
        DialogFactory.closeAlertDialog(getSupportFragmentManager());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (mViewPager != null) {
            final int position = tab.getPosition();
            Log.d(TAG, "tab selected -> " + position);
            mViewPager.setCurrentItem(position);
            setPersistedItem(RECOMMENDATIONS_TAB_KEY, position);
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
