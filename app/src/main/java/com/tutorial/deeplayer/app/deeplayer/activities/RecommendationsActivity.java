package com.tutorial.deeplayer.app.deeplayer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.fragments.*;
import com.tutorial.deeplayer.app.deeplayer.kMP;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.pojo.Artist;
import com.tutorial.deeplayer.app.deeplayer.services.MusicService;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.views.*;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilya.savritsky on 28.07.2015.
 */
public class RecommendationsActivity extends BaseActivity implements RecommendedAlbumsView.OnAlbumItemInteractionListener,
        RecommendedArtistsView.OnArtistItemInteractionListener,
        RecommendedTracksView.OnTrackItemInteractionListener,
        RecommendationsControlsView.OnTypeSelectedListener {
    public static final String TAG = RecommendationsActivity.class.getSimpleName();

    @Bind(R.id.fragment_container)
    View container;

    @Bind(R.id.player)
    View playerContainer;

//    private PlayerFragment playerFragment;

//    private WeakReference<AbstractPlayerWrapper> weakPlayer;

    //    private AlbumPlayer mAlbumPlayer;
    private DeezerConnect deezerConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);
        ButterKnife.bind(this);
        addControlsFragment();
//        addPlayerFragment();
        playerContainer.setVisibility(View.GONE);

        deezerConnect = new DeezerConnect(DeePlayerApp.get(), getString(R.string.app_id));
        SessionStore sessionStore = new SessionStore();
        sessionStore.restore(deezerConnect, getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, kMP.musicConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    private void addFragment(RecommendationsTypes type) {
        Fragment fragment = getFragmentForType(type);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private void initMusicService(RecommendationsTypes type) {
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
            default: {

            }
        }
    }

    private Fragment getFragmentForType(RecommendationsTypes type) {
        switch (type) {
            case Artists: {
                return getArtistFragment();
            }
            case Albums: {
                return getAlbumFragment();
            }
            case Tracks: {
                return getTracksFragment();
            }
            default: {
                return null;
            }
        }
    }

    private RecommendedTracksFragment getTracksFragment() {
        RecommendedTracksFragment recommendedTracksFragment = (RecommendedTracksFragment) getSupportFragmentManager().
                findFragmentByTag(RecommendedTracksFragment.TAG);
        if (recommendedTracksFragment == null) {
            recommendedTracksFragment = new RecommendedTracksFragment();
        }
        return recommendedTracksFragment;
    }

    private ArtistFragment getArtistFragment() {
        ArtistFragment artistFragment = (ArtistFragment) getSupportFragmentManager().findFragmentByTag(ArtistFragment.TAG);
        if (artistFragment == null) {
            artistFragment = new ArtistFragment();
        }
        return artistFragment;
    }

    private AlbumFragment getAlbumFragment() {
        AlbumFragment albumFragment = (AlbumFragment) getSupportFragmentManager().findFragmentByTag(AlbumFragment.TAG);
        if (albumFragment == null) {
            albumFragment = new AlbumFragment();
        }
        return albumFragment;
    }

    private void addControlsFragment() {
        RecommendationsControlsFragment controlsFragment = (RecommendationsControlsFragment)
                getSupportFragmentManager().findFragmentByTag(RecommendationsControlsFragment.TAG);
        if (controlsFragment == null) {
            controlsFragment = new RecommendationsControlsFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.controls, controlsFragment)
                .commit();
    }

//    private void addPlayerFragment() {
//        playerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentByTag(PlayerFragment.TAG);
//        if (playerFragment == null) {
//            playerFragment = new PlayerFragment();
//        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.player, playerFragment)
//                .commit();
//    }

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
    public void onError(Throwable err) {
        DialogFactory.showSimpleErrorMessage(this, getSupportFragmentManager(), err.getMessage());
    }

    @Override
    public void onAlbumItemInteraction(@NonNull Album album) {
        initMusicService(RecommendationsTypes.Artists);
        if (kMP.musicService != null) {
            kMP.musicService.setAlbum(album);
//            kMP.musicService.playAlbum();
        }
    }

    @Override
    public void onArtistItemInteraction(@NonNull Artist artist) {

        if (kMP.musicService != null) {
            initMusicService(RecommendationsTypes.Artists);
            kMP.musicService.setArtist(artist);
            kMP.musicService.playArtist();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(kMP.musicConnection);
    }

    @Override
    public void onTrackItemInteraction(@NonNull com.tutorial.deeplayer.app.deeplayer.pojo.Track track) {
//        try {
//            Log.d(TAG, "play track " + track.getTitle());
//
//            if (weakPlayer != null && weakPlayer.get() != null) {
//                weakPlayer.get().stop();
//            }
//            playerContainer.setVisibility(View.VISIBLE);
//
//            if (weakPlayer == null || weakPlayer.get() == null || !(weakPlayer.get() instanceof TrackPlayer)) {
//                weakPlayer = new WeakReference<>(new TrackPlayer(DeePlayerApp.get(), deezerConnect, new WifiOnlyNetworkStateChecker()));
//                weakPlayer.get().addPlayerListener(this);
//            }
//            ((TrackPlayer) weakPlayer.get()).playTrack(track.getId());
////            playerFragment.setAttachedPlayer(weakPlayer.get());
//
//        } catch (OAuthException e) {
//            e.printStackTrace();
//        } catch (DeezerError deezerError) {
//            deezerError.printStackTrace();
//        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
//            tooManyPlayersExceptions.printStackTrace();
//        }
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
    public void onTypeSelected(RecommendationsTypes type) {
        Log.d(TAG, "Handle type selection");
        switch (type) {
            case Tracks: {
                // Node: Do the same
            }
            case Artists: {
                // Node: Do the same
            }
            case Albums: {
                container.setVisibility(View.VISIBLE);
                addFragment(type);
                break;
            }
            default: {
                container.setVisibility(View.GONE);
            }
        }
    }
}
