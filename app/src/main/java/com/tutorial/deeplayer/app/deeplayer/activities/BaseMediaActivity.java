package com.tutorial.deeplayer.app.deeplayer.activities;

import android.widget.MediaController;

import com.tutorial.deeplayer.app.deeplayer.controllers.MusicController;
import com.tutorial.deeplayer.app.deeplayer.kMP;

/**
 * Created by ilya.savritsky on 18.08.2015.
 */
public class BaseMediaActivity extends BaseActivity implements MediaController.MediaPlayerControl {
    protected MusicController musicController;

    protected boolean paused = false;
    protected boolean playbackPaused = false;

    /**
     * (Re)Starts the musicController.
     */
    protected void setMusicController() {
        musicController = new MusicController(this);

        // What will happen when the user presses the
        // next/previous buttons?
        musicController.setPrevNextListeners(v -> {
            // Calling method defined on ActivityNowPlaying
            playNext();
        }, v -> {
            // Calling method defined on ActivityNowPlaying
            playPrevious();
        });

        // Binding to our media player
        musicController.setMediaPlayer(this);
//        musicController.setAnchorView(findViewById(R.id.player));
        musicController.setEnabled(true);
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
