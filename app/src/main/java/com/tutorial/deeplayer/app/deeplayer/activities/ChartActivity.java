package com.tutorial.deeplayer.app.deeplayer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.adapters.ChartSectionPagerAdapter;
import com.tutorial.deeplayer.app.deeplayer.kMP;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.pojo.Playlist;
import com.tutorial.deeplayer.app.deeplayer.pojo.Track;
import com.tutorial.deeplayer.app.deeplayer.services.MusicService;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.views.PlaylistsView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedAlbumsView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedArtistsView;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedTracksView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChartActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
        RecommendedAlbumsView.OnAlbumItemInteractionListener,
        RecommendedArtistsView.OnArtistItemInteractionListener,
        RecommendedTracksView.OnTrackItemInteractionListener,
        PlaylistsView.OnPlaylistItemInteractionListener {
    public static final String TAG = ChartActivity.class.getSimpleName();
    private static final String CHART_TAB_KEY = "chart_control_val";
    private static final int CHART_DEF_VALUE = 0;

    @Bind(R.id.app_bar)
    android.support.v7.widget.Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    ChartSectionPagerAdapter mSectionsPagerAdapter;

    @Bind(R.id.pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
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
        mSectionsPagerAdapter = new ChartSectionPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        final int tabPosition = getPersistedItem(CHART_TAB_KEY, CHART_DEF_VALUE);
        mViewPager.setCurrentItem(tabPosition);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chart, menu);
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
    public void onAlbumItemInteraction(@NonNull Album album) {

    }

    @Override
    public void onPlaylistItemInteraction(@NonNull Playlist playlist) {

    }

    @Override
    public void onStopProgress() {
        DialogFactory.closeAlertDialog(getSupportFragmentManager());
    }

    @Override
    public void onError(Throwable err) {
        DialogFactory.closeAlertDialog(getSupportFragmentManager());
    }

    @Override
    public void onArtistItemInteraction(@NonNull Artist artist) {

    }

    @Override
    public void onTrackItemInteraction(@NonNull Track track) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (mViewPager != null) {
            final int position = tab.getPosition();
            Log.d(TAG, "tab selected -> " + position);
            mViewPager.setCurrentItem(position);
            setPersistedItem(CHART_TAB_KEY, position);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
