package com.tutorial.deeplayer.app.deeplayer.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.tutorial.deeplayer.app.deeplayer.kMP;
import com.tutorial.deeplayer.app.deeplayer.services.MusicService;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendationsTypes;

/**
 * Created by ilya.savritsky on 09.09.2015.
 */
public class Utils {
    public static void unbindDrawables(View view) {
        try {
            if (view != null) {
                if (view.getBackground() != null) {
                    view.getBackground().setCallback(null);
                    view.setBackgroundDrawable(null);
                }

                if (view instanceof ImageView) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageBitmap(null);
                } else if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    for (int i = 0; i < viewGroup.getChildCount(); i++)
                        unbindDrawables(viewGroup.getChildAt(i));

                    if (!(view instanceof AdapterView))
                        viewGroup.removeAllViews();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initMusicService(RecommendationsTypes type) {
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
            case Playlists: {
                kMP.musicService.initPlayer(MusicService.PlayerType.PLAYLIST);
                break;
            }
            case Radio: {
                kMP.musicService.initPlayer(MusicService.PlayerType.RADIO);
            }
            default: {

            }
        }
    }
}
