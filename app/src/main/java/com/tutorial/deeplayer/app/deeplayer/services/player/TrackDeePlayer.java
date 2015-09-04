package com.tutorial.deeplayer.app.deeplayer.services.player;

import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.player.TrackPlayer;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiOnlyNetworkStateChecker;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;

/**
 * Created by ilya.savritsky on 04.09.2015.
 */
public class TrackDeePlayer extends BaseDeePlayer {
    @Override
    protected void initPlayer(DeezerConnect deezerConnect) throws DeezerError, TooManyPlayersExceptions {
        mDeezerPlayer = new TrackPlayer(DeePlayerApp.get(), deezerConnect, new WifiOnlyNetworkStateChecker());
    }

    @Override
    public void playItem(long itemId) {
        super.playItem(itemId);
        ((TrackPlayer)mDeezerPlayer).playTrack(itemId);
    }
}
