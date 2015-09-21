package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.AlbumFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.ArtistFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.FlowFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.TracksFragment;

import java.util.Locale;

/**
 * Created by ilya.savritsky on 11.09.2015.
 */
public class RecommendationsSectionsPagerAdapter extends FragmentStatePagerAdapter {
    private enum RecommendationsTabs {
        Artists,
        Albums,
        Tracks,
        Flow,
        DEFAULT;

        public static RecommendationsTabs create(int p) {
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
                case 4: {
                    return Flow;
                }
                default: {
                    return DEFAULT;
                }
            }
        }
    }

    private final String[] tabLabels;

    public RecommendationsSectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabLabels = new String[]{
                context.getString(R.string.title_section_artists),
                context.getString(R.string.title_section_albums),
                context.getString(R.string.title_section_tracks),
                context.getString(R.string.title_section_flow),
        };

    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        RecommendationsTabs val = RecommendationsTabs.create(position);

        switch (val) {
            case Artists: {
                return new ArtistFragment();
            }
            case Albums: {
                return new AlbumFragment();
            }
            // TODO: possible Playlists , etc fragments will be added here
            case Tracks: {
                return new TracksFragment();
            }
            case Flow: {
                return new FlowFragment();
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
        return RecommendationsTabs.DEFAULT.ordinal();
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
