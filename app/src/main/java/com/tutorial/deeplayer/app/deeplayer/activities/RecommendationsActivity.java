package com.tutorial.deeplayer.app.deeplayer.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.OAuthException;
import com.deezer.sdk.player.AlbumPlayer;
import com.deezer.sdk.player.event.RadioPlayerListener;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiOnlyNetworkStateChecker;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.fragments.AlbumFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.PlayerFragment;
import com.tutorial.deeplayer.app.deeplayer.pojo.Album;
import com.tutorial.deeplayer.app.deeplayer.views.RecommendedAlbumsView;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilya.savritsky on 28.07.2015.
 */
public class RecommendationsActivity extends AppCompatActivity implements RecommendedAlbumsView.OnAlbumItemInteractionListener,
        RadioPlayerListener {
    public static final String TAG = RecommendationsActivity.class.getSimpleName();

    @Bind(R.id.fragment_container)
    View container;

    @Bind(R.id.player)
    View playerContainer;
    private PlayerFragment playerFragment;

    private WeakReference<AlbumPlayer> weakPlayer;

    private AlbumPlayer mAlbumPlayer;
    private DeezerConnect deezerConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix);
        ButterKnife.bind(this);
        addFragment();
        addPlayerFragment();
        playerContainer.setVisibility(View.GONE);

        deezerConnect = new DeezerConnect(DeePlayerApp.get(), getString(R.string.app_id));
        SessionStore sessionStore = new SessionStore();
        sessionStore.restore(deezerConnect, getApplicationContext());
    }

    private void addFragment() {
        AlbumFragment albumFragment = (AlbumFragment) getSupportFragmentManager().findFragmentByTag(AlbumFragment.TAG);
        if (albumFragment == null) {
            albumFragment = new AlbumFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, albumFragment)
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
    public void onTooManySkipsException() {

    }

    @Override
    public void onAllTracksEnded() {

    }

    @Override
    public void onPlayTrack(Track track) {
        playerContainer.setVisibility(View.VISIBLE);
        playerFragment.displayTrack(track);
    }

    @Override
    public void onTrackEnded(Track track) {

    }

    @Override
    public void onRequestException(Exception e, Object o) {

    }

    @Override
    public void onAlbumItemInteraction(@NonNull Album album) {
        try {
            Log.d(TAG, "play album " + album.getTitle());

            if (weakPlayer != null && weakPlayer.get() != null) {
                weakPlayer.get().stop();
            }
            playerContainer.setVisibility(View.VISIBLE);
            if (weakPlayer == null || weakPlayer.get() == null) {
                weakPlayer = new WeakReference<>(new AlbumPlayer(DeePlayerApp.get(), deezerConnect, new WifiOnlyNetworkStateChecker()));
                weakPlayer.get().addPlayerListener(this);
            }
            weakPlayer.get().playAlbum(album.getId());
            playerFragment.setAttachedPlayer(weakPlayer.get());

        } catch (OAuthException e) {
            e.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        }
    }
}
