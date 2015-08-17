package com.tutorial.deeplayer.app.deeplayer.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.OAuthException;
import com.deezer.sdk.player.RadioPlayer;
import com.deezer.sdk.player.event.RadioPlayerListener;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiOnlyNetworkStateChecker;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;

/**
 * Created by ilya.savritsky on 11.08.2015.
 */
public class MusicService extends Service implements RadioPlayerListener {
    public final String TAG = MusicService.class.getSimpleName();

    private RadioPlayer mRadioPlayer;
    private Radio radio;
    private DeezerConnect deezerConnect;
    private final IBinder musicBind = new MusicBinder();

    public void onCreate() {
        super.onCreate();
        deezerConnect = new DeezerConnect(DeePlayerApp.get(), getString(R.string.app_id));
        SessionStore sessionStore = new SessionStore();
        sessionStore.restore(deezerConnect, getApplicationContext());
        if (deezerConnect.isSessionValid()) {
            initPlayer();
        }
    }


    private void initPlayer() {
        Log.d(TAG, "initPlayer");
        try {
            mRadioPlayer = new RadioPlayer(DeePlayerApp.get(), deezerConnect, new WifiOnlyNetworkStateChecker());
            mRadioPlayer.addPlayerListener(this);
            // mRadioPlayer.playRadio(RadioPlayer.RadioType.RADIO, radio.getId());
        } catch (OAuthException e) {
            e.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        }
    }

    public void playRadio() {
        Log.d(TAG, "playRadio");
        if (radio != null && mRadioPlayer != null) {
            Log.d(TAG, "Playing radio -> " + radio.getTitle());
            mRadioPlayer.stop();
            mRadioPlayer.playRadio(RadioPlayer.RadioType.RADIO, radio.getId());
        }
    }

    public RadioPlayer getPlayer() {
        return mRadioPlayer;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind -> " + intent);
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind -> " + intent);
        if (mRadioPlayer != null) {
            mRadioPlayer.stop();
            mRadioPlayer.release();
            mRadioPlayer = null;
        }
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind -> " + intent);
        super.onRebind(intent);
    }

    @Override
    public void onTooManySkipsException() {

    }

    @Override
    public void onAllTracksEnded() {

    }

    @Override
    public void onPlayTrack(Track track) {
        // display track
    }

    @Override
    public void onTrackEnded(Track track) {

    }

    @Override
    public void onRequestException(Exception e, Object o) {

    }

    public void setRadio(Radio radio) {
        this.radio = radio;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }

//        public void setRadio(Radio radio) {
//            MusicService.this.setRadio(radio);
//        }
//
//        public void playRadio() {
//            MusicService.this.playRadio();
//        }
    }
}
