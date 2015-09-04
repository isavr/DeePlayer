package com.tutorial.deeplayer.app.deeplayer.services.player;

import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.player.event.PlayerState;
import com.deezer.sdk.player.event.PlayerWrapperListener;

/**
 * Created by ilya.savritsky on 03.09.2015.
 */
public interface AbstractPlayer {
    void setup(DeezerConnect deezerConnect);
    void play();
    void playItem(long itemId);
    void pause();
    void stop();
    void release();
    void skipToNextTrack();
    void skipToPreviousTrack();
    void setStereoVolume(final float l, final float r);


    void addPlayerListener(PlayerWrapperListener listener);
    int getTrackDuration();
    PlayerState getPlayerState();
}
