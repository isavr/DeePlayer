package com.tutorial.deeplayer.app.deeplayer.services.player;

import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.player.PlaylistPlayer;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiOnlyNetworkStateChecker;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;

/**
 * Created by ilya.savritsky on 21.09.2015.
 */
public class PlaylistDeePlayer extends BaseDeePlayer {
    @Override
    protected void initPlayer(DeezerConnect deezerConnect) throws DeezerError, TooManyPlayersExceptions {
        mDeezerPlayer = new PlaylistPlayer(DeePlayerApp.get(), deezerConnect, new WifiOnlyNetworkStateChecker());
    }

    @Override
    public void playItem(long itemId) {
        super.playItem(itemId);
        ((PlaylistPlayer) mDeezerPlayer).playPlaylist(itemId);
    }
}
