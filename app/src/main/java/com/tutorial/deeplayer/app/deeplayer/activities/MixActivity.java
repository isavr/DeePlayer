package com.tutorial.deeplayer.app.deeplayer.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.controllers.MusicController;
import com.tutorial.deeplayer.app.deeplayer.fragments.PlayerFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.RadioFragment;
import com.tutorial.deeplayer.app.deeplayer.kMP;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.services.MusicService;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.views.RadioView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MixActivity extends BaseActivity implements RadioView.OnRadioItemInteractionListener,
        MediaController.MediaPlayerControl/*, RadioPlayerListener*/ {
    public static final String TAG = MixActivity.class.getSimpleName();

    @Bind(R.id.fragment_container)
    View container;

    @Bind(R.id.player)
    View playerContainer;

    private PlayerFragment playerFragment;
    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;
    private Radio radioItem;
    private MusicController musicController;

    private boolean paused = false;
    private boolean playbackPaused = false;
    //private RadioPlayer mRadioPlayer;
    //private DeezerConnect deezerConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix);
        ButterKnife.bind(this);
        addFragment();
        addPlayerFragment();
        playerContainer.setVisibility(View.GONE);
        setMusicController();

        if (playbackPaused) {
            setMusicController();
            playbackPaused = false;
        }
        //kMP.initialize(this);
//        if (playIntent == null) {
//            playIntent = new Intent(this, MusicService.class);
//            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
//            startService(playIntent);
//        }
        //deezerConnect = new DeezerConnect(DeePlayerApp.get(), getString(R.string.app_id));
        //SessionStore sessionStore = new SessionStore();
        //sessionStore.restore(deezerConnect, getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            // Ensure that the controller
            // is shown when the user returns to the app
            setMusicController();
            paused = false;
        }
    }

    /**
     * (Re)Starts the musicController.
     */
    private void setMusicController() {
        musicController = new MusicController(MixActivity.this);

        // What will happen when the user presses the
        // next/previous buttons?
        musicController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calling method defined on ActivityNowPlaying
                playNext();
            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Calling method defined on ActivityNowPlaying
                playPrevious();
            }
        });

        // Binding to our media player
        musicController.setMediaPlayer(this);
        musicController
                .setAnchorView(findViewById(R.id.player));
        musicController.setEnabled(true);
    }

    //connect to the service
//    private ServiceConnection musicConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
//            //get service
//            musicService = binder.getService();
//            //TODO: check
//            //pass radio
////            musicService.setRadio(radioItem);
////            musicService.playRadio();
//            musicBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            musicBound = false;
//        }
//    };

    private void addFragment() {
        RadioFragment radioFragment = (RadioFragment) getSupportFragmentManager().findFragmentByTag(RadioFragment.TAG);
        if (radioFragment == null) {
            radioFragment = new RadioFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, radioFragment)
                .commit();
    }


    private void addPlayerFragment() {
        playerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentByTag(PlayerFragment.TAG);
        if (playerFragment == null) {
            playerFragment = new PlayerFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.player, playerFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mix, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStopProgress() {
        DialogFactory.closeAlertDialog(getSupportFragmentManager());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
//        stopService(playIntent);
//        if (musicConnection != null) {
//            unbindService(musicConnection);
//        }
//        musicService = null;
        musicController.hide();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void onTooManySkipsException() {
//
//    }
//
//    @Override
//    public void onAllTracksEnded() {
//
//    }
//
//    @Override
//    public void onPlayTrack(Track track) {
//        playerFragment.displayTrack(track);
//    }
//
//    @Override
//    public void onTrackEnded(Track track) {
//
//    }
//
//    @Override
//    public void onRequestException(Exception e, Object o) {
//
//    }

    @Override
    public void onRadioItemInteraction(@NonNull Radio radio) {
        playerContainer.setVisibility(View.VISIBLE);
//        try {
        Log.d(TAG, "play radio " + radio.getTitle());
        radioItem = radio;
        if (kMP.musicService != null) {
            kMP.musicService.setRadio(radioItem);
            kMP.musicService.playRadio();
        }
//        if (musicService != null && musicBound) {
//            musicService.setRadio(radio);
//            musicService.playRadio();
//            //playerFragment.setAttachedPlayer(musicService.getPlayer());
//        }

//            if (mRadioPlayer != null) {
//                mRadioPlayer.stop();
//            }
//
//            // TODO: update it
//            mRadioPlayer = new RadioPlayer(DeePlayerApp.get(), deezerConnect, new WifiOnlyNetworkStateChecker());
//            mRadioPlayer.addPlayerListener(this);
//            mRadioPlayer.playRadio(RadioPlayer.RadioType.RADIO, radio.getId());
//            playerFragment.setAttachedPlayer(mRadioPlayer);

//        } catch (OAuthException e) {
//            e.printStackTrace();
//        } catch (DeezerError deezerError) {
//            deezerError.printStackTrace();
//        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
//            tooManyPlayersExceptions.printStackTrace();
//        }
    }

    @Override
    public void start() {
        kMP.musicService.unpausePlayer();
    }

    /**
     * Callback to when the user pressed the `pause` button.
     */
    @Override
    public void pause() {
        kMP.musicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        if (kMP.musicService != null && kMP.musicService.musicBound
                && kMP.musicService.isPlaying())
            return kMP.musicService.getTrackDuration();
        else
            return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (kMP.musicService != null && kMP.musicService.musicBound
                && kMP.musicService.isPlaying()) {
            // TODO: // FIXME: 14.08.2015
            return 0;
            //return kMP.musicService.getPosition();
        } else {
            return 0;
        }
    }

    @Override
    public void seekTo(int position) {
        // NOTE: not allowed for radios
        //kMP.musicService.seekTo(position);
    }

    @Override
    public boolean isPlaying() {
        if (kMP.musicService != null && kMP.musicService.musicBound)
            return kMP.musicService.isPlaying();

        return false;
    }

    @Override
    public int getBufferPercentage() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        // TODO Auto-generated method stub
        return 0;
    }

    // Back to the normal methods

    /**
     * Jumps to the next song and starts playing it right now.
     */
    public void playNext() {
        kMP.musicService.next(true);
        //kMP.musicService.playSong();

        //refreshActionBarSubtitle();

        // To prevent the MusicPlayer from behaving
        // unexpectedly when we pause the song playback.
        if (playbackPaused) {
            setMusicController();
            playbackPaused = false;
        }

        musicController.show();
    }

    /**
     * Jumps to the previous song and starts playing it right now.
     */
    public void playPrevious() {
        kMP.musicService.previous(true);
        //kMP.musicService.playRadio();

        //refreshActionBarSubtitle();

        // To prevent the MusicPlayer from behaving
        // unexpectedly when we pause the song playback.
        if (playbackPaused) {
            setMusicController();
            playbackPaused = false;
        }

        musicController.show();
    }
}
