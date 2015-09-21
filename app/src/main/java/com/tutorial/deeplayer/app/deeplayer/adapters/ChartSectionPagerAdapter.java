package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.fragments.chart.ChartedAlbumsFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.chart.ChartedArtistsFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.chart.ChartedPlaylistsFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.chart.ChartedTracksFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.FlowFragment;

import java.util.Locale;

/**
 * Created by ilya.savritsky on 17.09.2015.
 */
public class ChartSectionPagerAdapter extends FragmentStatePagerAdapter {
    private enum ChartTabs {
        Artists,
        Albums,
        Tracks,
        Playlists,
        DEFAULT;

        public static ChartTabs create(int p) {
            switch (p) {
                case 0: {
                    return Artists;
                }
                case 1: {
                    return Albums;
                }
                case 2: {
                    return Tracks;
                }
                case 3: {
                    return Playlists;
                }
                default: {
                    return DEFAULT;
                }
            }
        }
    }

    private final String[] tabLabels;

    public ChartSectionPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabLabels = new String[]{
                context.getString(R.string.title_section_artists),
                context.getString(R.string.title_section_albums),
                context.getString(R.string.title_section_tracks),
                context.getString(R.string.title_section_playlists),
        };

    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        ChartTabs val = ChartTabs.create(position);

        switch (val) {
            case Artists: {
                return new ChartedArtistsFragment();
            }
            case Albums: {
                return new ChartedAlbumsFragment();
            }
            case Tracks: {
                return new ChartedTracksFragment();
            }
            case Playlists: {
                return new ChartedPlaylistsFragment();
            }
            case DEFAULT:
            default: {
                return new FlowFragment();
            }
        }
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return ChartTabs.DEFAULT.ordinal();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        if (position < tabLabels.length) {
            return tabLabels[position].toUpperCase(l);
        }
        return null;
    }
}
