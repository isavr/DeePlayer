package com.tutorial.deeplayer.app.deeplayer.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteAlbumsFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteArtistsFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteRadiosFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteTracksFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.BaseFragment;
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

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserLibraryActivity extends BaseActivity implements ActionBar.TabListener,
        RecommendedAlbumsView.OnAlbumItemInteractionListener,
        RecommendedArtistsView.OnArtistItemInteractionListener,
        RecommendedTracksView.OnTrackItemInteractionListener,
        RadioView.OnRadioItemInteractionListener {

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

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @Bind(R.id.pager) ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_library);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewPager.setOnPageChangeListener(null);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.removeAllTabs();
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
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        if (mViewPager != null) {
            mViewPager.setCurrentItem(tab.getPosition());
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private enum LibraryTabs {
        FavouriteTracks,
        FavouriteAlbums,
        FavouriteArtists,
        FavouriteMixes,
        DEFAULT;

        public static LibraryTabs create(int p) {
            switch (p) {
                case 0: {
                    return FavouriteTracks;
                }
                case 1: {
                    return FavouriteAlbums;
                }
                case 2: {
                    return FavouriteArtists;
                }
                case 4: {
                    return FavouriteMixes;
                }
                default: {
                    return DEFAULT;
                }
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            LibraryTabs val = LibraryTabs.create(position);
            switch (val) {
                case FavouriteTracks: {
                    Fragment fr = getSupportFragmentManager().findFragmentByTag(FavouriteTracksFragment.TAG);
                    if (fr == null) {
                        fr = new FavouriteTracksFragment();
                    }
                    return fr;
                }
                case FavouriteAlbums: {
                    Fragment fr = getSupportFragmentManager().findFragmentByTag(FavouriteAlbumsFragment.TAG);
                    if (fr == null) {
                        fr = new FavouriteAlbumsFragment();
                    }
                    return fr;
                }
                case FavouriteArtists: {
                    Fragment fr = getSupportFragmentManager().findFragmentByTag(FavouriteArtistsFragment.TAG);
                    if (fr == null) {
                        fr = new FavouriteArtistsFragment();
                    }
                    return fr;
                }
                case FavouriteMixes: {
                    Fragment fr = getSupportFragmentManager().findFragmentByTag(FavouriteRadiosFragment.TAG);
                    if (fr == null) {
                        fr = new FavouriteRadiosFragment();
                    }
                    return fr;
                }
                case DEFAULT:
                default: {
                    Fragment fr = getSupportFragmentManager().findFragmentByTag(FavouriteRadiosFragment.TAG);
                    if (fr == null) {
                        fr = new FavouriteRadiosFragment();
                    }
                    return fr;
                }
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return LibraryTabs.DEFAULT.ordinal();
        }

//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            if (object != null) {
//                ((BaseFragment)object).onDestroy();
//            }
//            super.destroyItem(container, position, object);
//
//        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0: {
                    return getString(R.string.title_section1).toUpperCase(l);
                }
                case 1: {
                    return getString(R.string.title_section2).toUpperCase(l);
                }
                case 2: {
                    return getString(R.string.title_section3).toUpperCase(l);
                }
                case 3: {
                    return getString(R.string.title_section4).toUpperCase(l);
                }
            }
            return null;
        }
    }
}
