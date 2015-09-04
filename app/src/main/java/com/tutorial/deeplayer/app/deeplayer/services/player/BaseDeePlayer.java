package com.tutorial.deeplayer.app.deeplayer.services.player;

import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.player.AbstractPlayerWrapper;
import com.deezer.sdk.player.event.PlayerState;
import com.deezer.sdk.player.event.PlayerWrapperListener;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;

/**
 * Created by ilya.savritsky on 04.09.2015.
 */
public class BaseDeePlayer implements AbstractPlayer {
    protected AbstractPlayerWrapper mDeezerPlayer;

    @Override
    public void setup(DeezerConnect deezerConnect) {
        try {
            initPlayer(deezerConnect);
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        }
    }

    @Override
    public void setStereoVolume(float l, float r) {
        if (mDeezerPlayer != null) {
            mDeezerPlayer.setStereoVolume(l, r);
        }
    }

    protected void initPlayer(DeezerConnect deezerConnect)  throws DeezerError, TooManyPlayersExceptions {
        // Note: override in concrete players
    }

    @Override
    public void skipToNextTrack() {
        if (mDeezerPlayer != null) {
            mDeezerPlayer.skipToNextTrack();
        }
    }

    @Override
    public void skipToPreviousTrack() {
        if (mDeezerPlayer != null) {
            mDeezerPlayer.skipToPreviousTrack();
        }
    }

    @Override
    public void play() {
        if (mDeezerPlayer != null) {
            mDeezerPlayer.play();
        }
    }

    @Override
    public void playItem(long itemId) {
        stop(); // stop if playing something
    }

    @Override
    public void pause() {
        mDeezerPlayer.pause();
    }

    @Override
    public void stop() {
        mDeezerPlayer.stop();
    }

    @Override
    public void release() {
        mDeezerPlayer.release();
        mDeezerPlayer = null;
    }

    @Override
    public void addPlayerListener(PlayerWrapperListener listener) {
        if (mDeezerPlayer != null) {
            mDeezerPlayer.addPlayerListener(listener);
        }
    }

    @Override
    public int getTrackDuration() {
        if (mDeezerPlayer != null) {
            return (int) mDeezerPlayer.getTrackDuration();
        }
        return 0;
    }

    @Override
    public PlayerState getPlayerState() {
        if (mDeezerPlayer != null) {
            return mDeezerPlayer.getPlayerState();
        }
        return PlayerState.RELEASED; // TODO: check?
    }
}
