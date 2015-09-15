package com.tutorial.deeplayer.app.deeplayer.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteAlbumsFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteArtistsFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteRadiosFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteTracksFragment;

import java.util.Locale;

/**
 * Created by ilya.savritsky on 11.09.2015.
 */
public class UserLibrarySectionsPagerAdapter extends FragmentStatePagerAdapter {
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

    private final String[] tabLabels;

    public UserLibrarySectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabLabels = new String[]{
                context.getString(R.string.title_section1),
                context.getString(R.string.title_section2),
                context.getString(R.string.title_section3),
                context.getString(R.string.title_section4),
        };

    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        LibraryTabs val = LibraryTabs.create(position);

        switch (val) {
            case FavouriteTracks: {
                return new FavouriteTracksFragment();
            }
            case FavouriteAlbums: {
                return new FavouriteAlbumsFragment();
            }
            case FavouriteArtists: {
                return new FavouriteArtistsFragment();
            }
            case FavouriteMixes: {
                return new FavouriteRadiosFragment();
            }
            case DEFAULT:
            default: {
                return new FavouriteRadiosFragment();
            }
        }
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return LibraryTabs.DEFAULT.ordinal();
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
