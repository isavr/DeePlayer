package com.tutorial.deeplayer.app.deeplayer.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.OAuthException;
import com.deezer.sdk.player.RadioPlayer;
import com.deezer.sdk.player.event.PlayerState;
import com.deezer.sdk.player.event.RadioPlayerListener;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiOnlyNetworkStateChecker;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.external.RemoteControlClientCompat;
import com.tutorial.deeplayer.app.deeplayer.external.RemoteControlHelper;
import com.tutorial.deeplayer.app.deeplayer.notifications.NotificationMusic;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;

/**
 * Created by ilya.savritsky on 13.08.2015.
 */
public class PlayRadioService extends Service implements AudioManager.OnAudioFocusChangeListener,
        RadioPlayerListener {

    public static final String TAG = PlayRadioService.class.getSimpleName();

    /**
     * String that identifies all broadcasts this Service makes.
     * <p>
     * Since this Service will send LocalBroadcasts to explain
     * what it does (like "playing song" or "paused song"),
     * other classes that might be interested on it must
     * register a BroadcastReceiver to this String.
     */
    public static final String BROADCAST_ACTION = "com.tutorial.deeplayer.app.deeplayer.services.RADIO_SERVICE";

    /**
     * String used to get the current state Extra on the Broadcast Intent
     */
    public static final String BROADCAST_EXTRA_STATE = "x_japan";

    /**
     * String used to get the song ID Extra on the Broadcast Intent
     */
    public static final String BROADCAST_EXTRA_SONG_ID = "tenacious_d";

    // All possible messages this Service will broadcast
    // Ignore the actual values

    /**
     * Broadcast for when some music started playing
     */
    public static final String BROADCAST_EXTRA_PLAYING = "beatles";

    /**
     * Broadcast for when some music just got paused
     */
    public static final String BROADCAST_EXTRA_PAUSED = "santana";

    /**
     * Broadcast for when a paused music got unpaused
     */
    public static final String BROADCAST_EXTRA_UNPAUSED = "iron_maiden";

    /**
     * Broadcast for when current music got played until the end
     */
    public static final String BROADCAST_EXTRA_COMPLETED = "los_hermanos";

    /**
     * Broadcast for when the user skipped to the next song
     */
    public static final String BROADCAST_EXTRA_SKIP_NEXT = "paul_gilbert";

    /**
     * Broadcast for when the user skipped to the previous song
     */
    public static final String BROADCAST_EXTRA_SKIP_PREVIOUS = "john_petrucci";


    private RadioPlayer mRadioPlayer;
    private Radio radio;
    private DeezerConnect deezerConnect;

    //TODO: fix values and names
    // These are the Intent actions that we are prepared to handle. Notice that the fact these
    // constants exist in our class is a mere convenience: what really defines the actions our
    // service can handle are the <action> tags in the <intent-filters> tag for our service in
    // AndroidManifest.xml.
    public static final String BROADCAST_ORDER = " com.tutorial.deeplayer.app.deeplayer.MUSIC_SERVICE";
    public static final String BROADCAST_EXTRA_GET_ORDER = "com.tutorial.deeplayer.app.deeplayer.services.MUSIC_SERVICE";

    public static final String BROADCAST_ORDER_PLAY = "com.tutorial.deeplayer.app.deeplayer.action.PLAY";
    public static final String BROADCAST_ORDER_PAUSE = "com.tutorial.deeplayer.app.deeplayer.action.PAUSE";
    public static final String BROADCAST_ORDER_TOGGLE_PLAYBACK = "dlsadasd";
    public static final String BROADCAST_ORDER_STOP = "com.tutorial.deeplayer.app.deeplayer.action.STOP";
    public static final String BROADCAST_ORDER_SKIP = "com.tutorial.deeplayer.app.deeplayer.action.SKIP";
    public static final String BROADCAST_ORDER_REWIND = "com.tutorial.deeplayer.app.deeplayer.action.REWIND";

    /**
     * Possible states this Service can be on.
     */


    /**
     * Controller that communicates with the lock screen,
     * providing that fancy widget.
     */
    RemoteControlClientCompat lockscreenController = null;

    /**
     * We use this to get the media buttons' Broadcasts and
     * to control the lock screen widget.
     * <p>
     * Component name of the MusicIntentReceiver.
     */
    ComponentName mediaButtonEventReceiver;

    /**
     * Use this to get audio focus:
     * <p>
     * 1. Making sure other music apps don't play
     * at the same time;
     * 2. Guaranteeing the lock screen widget will
     * be controlled by us;
     */
    AudioManager audioManager;

    /**
     * Spawns an on-going notification with our current
     * playing song.
     */
    private NotificationMusic notification = null;

    public void onCreate() {
        super.onCreate();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        deezerConnect = new DeezerConnect(DeePlayerApp.get(), getString(R.string.app_id));
        SessionStore sessionStore = new SessionStore();
        sessionStore.restore(deezerConnect, getApplicationContext());
        if (deezerConnect.isSessionValid()) {
            initPlayer();
        }
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .registerReceiver(localBroadcastReceiver, new IntentFilter(PlayRadioService.BROADCAST_ORDER));

        // Registering the headset broadcaster for info related
        // to user plugging the headset.
        // TODO: headset receiver
//        IntentFilter headsetFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
//        registerReceiver(headsetBroadcastReceiver, headsetFilter);
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

    public void stopRadioPlayer() {
        if (mRadioPlayer == null)
            return;

        mRadioPlayer.stop();
        mRadioPlayer.release();
        mRadioPlayer = null;
        deezerConnect = null;

        Log.w(TAG, "stopMusicPlayer");
    }

    /**
     * Sets radio to play
     *
     * @param radio Radio that will be played from now on
     * @note make sure to call (@link #playRadio()) after this
     */
    public void setRadio(Radio radio) {
        this.radio = radio;
    }

    public void playRadio() {
        Log.d(TAG, "playRadio");
        if (radio != null && mRadioPlayer != null) {
            Log.d(TAG, "Playing radio -> " + radio.getTitle());
            mRadioPlayer.stop();
            mRadioPlayer.playRadio(RadioPlayer.RadioType.RADIO, radio.getId());

            broadcastState(PlayRadioService.BROADCAST_EXTRA_PLAYING);
            updateLockScreenWidget(radio, RemoteControlClient.PLAYSTATE_PLAYING);
        }
    }

    /**
     * Tells if this service is bound to an Activity.
     */
    public boolean musicBound = false;

    public void updateLockScreenWidget(Radio radio, int state) {

        // Only showing if the Setting is... well... set
//        if (! kMP.settings.get("show_lock_widget", true))
//            return;

        if (radio == null)
            return;

        if (!requestAudioFocus()) {
            //Stop the service.
            stopSelf();
            Toast.makeText(getApplicationContext(), "FUCK", Toast.LENGTH_LONG).show();
            return;
        }

        Log.w("service", "audio_focus_granted");

        // The Lock-Screen widget was not created up until now.
        // (both of the null-checks below)
        if (mediaButtonEventReceiver == null)
            mediaButtonEventReceiver = new ComponentName(this, ExternalBroadcastReceiver.class);

        if (lockscreenController == null) {
            Intent audioButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            audioButtonIntent.setComponent(mediaButtonEventReceiver);

            PendingIntent pending = PendingIntent.getBroadcast(this, 0, audioButtonIntent, 0);

            lockscreenController = new RemoteControlClientCompat(pending);

            RemoteControlHelper.registerRemoteControlClient(audioManager, lockscreenController);
            audioManager.registerMediaButtonEventReceiver(mediaButtonEventReceiver);

            Log.w("service", "created control compat");
        }

        // Current state of the Lock-Screen Widget
        lockscreenController.setPlaybackState(state);

        // All buttons the Lock-Screen Widget supports
        // (will be broadcasts)
        lockscreenController.setTransportControlFlags(
                RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
                        RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
                        RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
                        RemoteControlClient.FLAG_KEY_MEDIA_NEXT);

        // Update the current song metadata
        // on the Lock-Screen Widget
        lockscreenController
                // Starts editing (before #apply())
                .editMetadata(true)

                        // Sending all metadata of the current song
                .putString(android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST, "Test Artist")//song.getArtist())
                .putString(android.media.MediaMetadataRetriever.METADATA_KEY_ALBUM, "Test album")
                .putString(android.media.MediaMetadataRetriever.METADATA_KEY_TITLE, radio.getTitle())
                .putLong(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION, mRadioPlayer.getTrackDuration())

                        // TODO: fetch real item artwork
                        //.putBitmap(
                        //        RemoteControlClientCompat.MetadataEditorCompat.METADATA_KEY_ARTWORK,
                        //        mDummyAlbumArt)

                        // Saves (after #editMetadata())
                .apply();

        Log.w("service", "remote control client applied");
    }

    /**
     * Asks the AudioManager for our application to
     * have the audio focus.
     *
     * @return If we have it.
     */
    private boolean requestAudioFocus() {
        //Request audio focus for playback
        int result = audioManager.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        //Check if audio focus was granted. If not, stop the service.
        return (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
    }

    /**
     * Disables the hability to notify things on the
     * status bar.
     *
     * @see #notifyCurrentRadio()
     */
    public void cancelNotification() {
        if (notification == null)
            return;

        notification.cancel();
        notification = null;
    }

    /**
     * Displays a notification on the status bar with the
     * current song and some nice buttons.
     */
    public void notifyCurrentRadio(Track track) {
        //if (! kMP.settings.get("show_notification", true))
        //    return;
        if (radio == null)
            return;

        if (notification == null) {
            notification = new NotificationMusic();
        }

        notification.notifyRadio(this, this, radio, track);
    }

    /**
     * Receives external Broadcasts and gives our MusicService
     * orders based on them.
     * <p>
     * It is the bridge between our application and the external
     * world. It receives Broadcasts and launches Internal Broadcasts.
     * <p>
     * It acts on music events (such as disconnecting headphone)
     * and music controls (the lockscreen widget).
     *
     * @note This class works because we are declaring it in a
     * `receiver` tag in `AndroidManifest.xml`.
     * @note It is static so we can look out for external broadcasts
     * even when the service is offline.
     */
    public static class ExternalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.w(TAG, "external broadcast");

            // Broadcasting orders to our MusicService
            // locally (inside the application)
            LocalBroadcastManager local = LocalBroadcastManager.getInstance(context);

            String action = intent.getAction();

            // TODO: check headphones
            // Headphones disconnected
//            if (action.equals(android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
//
//                // Will only pause the music if the Setting
//                // for it is enabled.
//                if (!kMP.settings.get("pause_headphone_off", true))
//                    return;
//
//                // ADD SETTINGS HERE
//                String text = context.getString(R.string.service_music_play_headphone_off);
//                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
//
//                // send an intent to our MusicService to telling it to pause the audio
//                Intent broadcastIntent = new Intent(PlayRadioService.BROADCAST_ORDER);
//                broadcastIntent.putExtra(PlayRadioService.BROADCAST_EXTRA_GET_ORDER, PlayRadioService.BROADCAST_ORDER_PAUSE);
//
//                local.sendBroadcast(broadcastIntent);
//                Log.w(TAG, "becoming noisy");
//                return;
//            }

            if (action.equals(Intent.ACTION_MEDIA_BUTTON)) {

                // Which media key was pressed
                KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);

                // Not interested on anything other than pressed keys.
                if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                    return;

                String intentValue = null;

                switch (keyEvent.getKeyCode()) {

                    case KeyEvent.KEYCODE_HEADSETHOOK:
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        intentValue = PlayRadioService.BROADCAST_ORDER_TOGGLE_PLAYBACK;
                        Log.w(TAG, "media play pause");
                        break;

                    case KeyEvent.KEYCODE_MEDIA_PLAY:
                        intentValue = PlayRadioService.BROADCAST_ORDER_PLAY;
                        Log.w(TAG, "media play");
                        break;

                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
                        intentValue = PlayRadioService.BROADCAST_ORDER_PAUSE;
                        Log.w(TAG, "media pause");
                        break;

                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        intentValue = PlayRadioService.BROADCAST_ORDER_SKIP;
                        Log.w(TAG, "media next");
                        break;

                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        // TODO: ensure that doing this in rapid succession actually plays the
                        // previous song
                        intentValue = PlayRadioService.BROADCAST_ORDER_REWIND;
                        Log.w(TAG, "media previous");
                        break;
                }

                // Actually sending the Intent
                if (intentValue != null) {
                    Intent broadcastIntent = new Intent(PlayRadioService.BROADCAST_ORDER);
                    broadcastIntent.putExtra(PlayRadioService.BROADCAST_EXTRA_GET_ORDER, intentValue);

                    local.sendBroadcast(broadcastIntent);
                }
            }
        }
    }

    /**
     * The thing that will keep an eye on LocalBroadcasts
     * for the MusicService.
     */
    BroadcastReceiver localBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Getting the information sent by the MusicService
            // (and ignoring it if invalid)
            String order = intent.getStringExtra(PlayRadioService.BROADCAST_EXTRA_GET_ORDER);

            // What?
            if (order == null)
                return;

            if (order.equals(PlayRadioService.BROADCAST_ORDER_PAUSE)) {
                pausePlayer();
            } else if (order.equals(PlayRadioService.BROADCAST_ORDER_PLAY)) {
                unpausePlayer();
            } else if (order.equals(PlayRadioService.BROADCAST_ORDER_TOGGLE_PLAYBACK)) {
                togglePlayback();
            } else if (order.equals(PlayRadioService.BROADCAST_ORDER_SKIP)) {
                next(true);
                //mRadioPlayer.skipToNextTrack();
                //playSong();
            } else if (order.equals(PlayRadioService.BROADCAST_ORDER_REWIND)) {
                previous(true);
                //mRadioPlayer.skipToPreviousTrack();
                //playSong();
            }

            Log.w(TAG, "local broadcast received");
        }
    };

    public void pausePlayer() {
        PlayerState playerState = mRadioPlayer.getPlayerState();
        if (playerState != PlayerState.PAUSED && playerState != PlayerState.PLAYING)
            return;

        mRadioPlayer.pause();

        notification.notifyPaused(true);

        // Updates Lock-Screen Widget
        if (lockscreenController != null) {
            lockscreenController.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
        }

        broadcastState(PlayRadioService.BROADCAST_EXTRA_PAUSED);
    }

    public void unpausePlayer() {
        PlayerState playerState = mRadioPlayer.getPlayerState();
        if (playerState != PlayerState.PAUSED && playerState != PlayerState.PLAYING) {
            return;
        }

        mRadioPlayer.play();

        notification.notifyPaused(false);

        // Updates Lock-Screen Widget
        if (lockscreenController != null)
            lockscreenController.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);

        broadcastState(PlayRadioService.BROADCAST_EXTRA_UNPAUSED);
    }

    /**
     * Jumps to the next song on the list.
     *
     * @note Remember to call `playSong()` to make the MusicPlayer
     * actually play the music.
     */
    public void next(boolean userSkippedSong) {
        PlayerState playerState = mRadioPlayer.getPlayerState();
        if (playerState != PlayerState.PAUSED && playerState != PlayerState.PLAYING) {
            return;
        }

        if (userSkippedSong) {
            broadcastState(PlayRadioService.BROADCAST_EXTRA_SKIP_NEXT);
        }
        mRadioPlayer.skipToNextTrack();

        // Updates Lock-Screen Widget
        if (lockscreenController != null) {
            lockscreenController.setPlaybackState(RemoteControlClient.PLAYSTATE_SKIPPING_FORWARDS);
        }
    }

    /**
     * Jumps to the previous song on the list.
     *
     * @note Remember to call `playSong()` to make the MusicPlayer
     * actually play the music.
     */
    public void previous(boolean userSkippedSong) {
        PlayerState playerState = mRadioPlayer.getPlayerState();
        if (playerState != PlayerState.PAUSED && playerState != PlayerState.PLAYING) {
            return;
        }

        if (userSkippedSong) {
            broadcastState(PlayRadioService.BROADCAST_EXTRA_SKIP_PREVIOUS);
        }
        mRadioPlayer.skipToPreviousTrack();

        // Updates Lock-Screen Widget
        if (lockscreenController != null) {
            lockscreenController.setPlaybackState(RemoteControlClient.PLAYSTATE_SKIPPING_BACKWARDS);
        }
    }


    public void togglePlayback() {
        PlayerState playerState = mRadioPlayer.getPlayerState();
        if (playerState == PlayerState.PAUSED) {
            unpausePlayer();
        } else {
            pausePlayer();
        }
    }

    /**
     * Defines the interaction between an Activity and this Service.
     */
    public class MusicBinder extends Binder {
        public PlayRadioService getService() {
            return PlayRadioService.this;
        }
    }


    /**
     * Token for the interaction between an Activity and this Service.
     */
    private final IBinder musicBind = new MusicBinder();

    /**
     * Called when the Service is finally bound to the app.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind -> " + intent);
        return musicBind;
    }

    /**
     * Called when the Service is unbound - user quitting
     * the app or something.
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    /**
     * Shouts the state of the Music Service.
     *
     * @param state Current state of the Music Service.
     * @note This broadcast is visible only inside this application.
     * @note Will get received by listeners of `ServicePlayMusic.BROADCAST_ACTION`
     */
    private void broadcastState(String state) {
        if (radio == null)
            return;

        Intent broadcastIntent = new Intent(PlayRadioService.BROADCAST_ACTION);

        broadcastIntent.putExtra(PlayRadioService.BROADCAST_EXTRA_STATE, state);
        broadcastIntent.putExtra(PlayRadioService.BROADCAST_EXTRA_SONG_ID, radio.getId());

        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .sendBroadcast(broadcastIntent);

        Log.w(TAG, "sentBroadcast");
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {

            // Yay, gained audio focus! Either from losing it for
            // a long or short periods of time.
            case AudioManager.AUDIOFOCUS_GAIN:
                Log.w(TAG, "audiofocus gain");

                if (mRadioPlayer == null) {
                    initPlayer();
                }

                if (pausedTemporarilyDueToAudioFocus) {
                    pausedTemporarilyDueToAudioFocus = false;
                    unpausePlayer();
                }

                if (loweredVolumeDueToAudioFocus) {
                    loweredVolumeDueToAudioFocus = false;
                    mRadioPlayer.setStereoVolume(0.5f, 0.5f);
                }
                break;

            // Damn, lost the audio focus for a (presumable) long time
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.w(TAG, "audiofocus loss");

                // Giving up everything
                //audioManager.unregisterMediaButtonEventReceiver(mediaButtonEventReceiver);
                //audioManager.abandonAudioFocus(this);

                //pausePlayer();
                stopRadioPlayer();
                break;

            // Just lost audio focus but will get it back shortly
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.w(TAG, "audiofocus loss transient");

                if (!isPaused()) {
                    pausePlayer();
                    pausedTemporarilyDueToAudioFocus = true;
                }
                break;

            // Temporarily lost audio focus but I can keep it playing
            // at a low volume instead of stopping completely
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.w(TAG, "audiofocus loss transient can duck");

                mRadioPlayer.setStereoVolume(0.1f, 0.1f);
                loweredVolumeDueToAudioFocus = true;
                break;
        }
    }

    public int getTrackDuration() {
        if (mRadioPlayer != null) {
            return (int) mRadioPlayer.getTrackDuration();
        } else {
            return 0;
        }
    }

    public boolean isPaused() {
        return mRadioPlayer != null && mRadioPlayer.getPlayerState() == PlayerState.PAUSED;
    }

    public boolean isPlaying() {
        return mRadioPlayer != null && mRadioPlayer.getPlayerState() == PlayerState.PLAYING;
    }

    private boolean pausedTemporarilyDueToAudioFocus = false;
    private boolean loweredVolumeDueToAudioFocus = false;

    @Override
    public void onTooManySkipsException() {

    }

    @Override
    public void onAllTracksEnded() {

    }

    @Override
    public void onPlayTrack(Track track) {
        notifyCurrentRadio(track);
    }

    @Override
    public void onTrackEnded(Track track) {

    }

    @Override
    public void onRequestException(Exception e, Object o) {

    }
}
