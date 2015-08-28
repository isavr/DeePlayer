package com.tutorial.deeplayer.app.deeplayer.views;

/**
 * Created by ilya.savritsky on 30.07.2015.
 */
public enum RecommendationsTypes {
    Artists,
    Albums,
    Playlists,
    Tracks,
    Flow,
    Unknown;

    public static RecommendationsTypes fromId(int id) {
        switch (id) {
            case 0: {
                return Artists;
            }
            case 1: {
                return Albums;
            }
            case 2: {
                return Playlists;
            }
            case 3: {
                return Tracks;
            }
            case 4: {
                return Flow;
            }
            default: {
                return Unknown;
            }
        }
    }
}
