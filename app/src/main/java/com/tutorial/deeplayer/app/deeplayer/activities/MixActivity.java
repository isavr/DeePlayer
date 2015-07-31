package com.tutorial.deeplayer.app.deeplayer.activities;

import android.content.res.Configuration;
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
import com.deezer.sdk.player.RadioPlayer;
import com.deezer.sdk.player.event.RadioPlayerListener;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiOnlyNetworkStateChecker;
import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.fragments.PlayerFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.RadioFragment;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.views.RadioView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MixActivity extends AppCompatActivity implements RadioView.OnRadioItemInteractionListener, RadioPlayerListener {
    public static final String TAG = MixActivity.class.getSimpleName();

    @Bind(R.id.fragment_container)
    View container;

    @Bind(R.id.player)
    View playerContainer;

    private PlayerFragment playerFragment;

    private RadioPlayer mRadioPlayer;
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
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTooManySkipsException() {

    }

    @Override
    public void onAllTracksEnded() {

    }

    @Override
    public void onPlayTrack(Track track) {
        playerFragment.displayTrack(track);
    }

    @Override
    public void onTrackEnded(Track track) {

    }

    @Override
    public void onRequestException(Exception e, Object o) {

    }

    @Override
    public void onRadioItemInteraction(@NonNull Radio radio) {
        playerContainer.setVisibility(View.VISIBLE);
        try {
            Log.d(TAG, "play radio " + radio.getTitle());

            if (mRadioPlayer != null) {
                mRadioPlayer.stop();
            }

            mRadioPlayer = new RadioPlayer(DeePlayerApp.get(), deezerConnect, new WifiOnlyNetworkStateChecker());
            mRadioPlayer.addPlayerListener(this);
            mRadioPlayer.playRadio(RadioPlayer.RadioType.RADIO, radio.getId());
            playerFragment.setAttachedPlayer(mRadioPlayer);

        } catch (OAuthException e) {
            e.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        }
    }

//    // TODO: move from here to model
//    @Override
//    public void onRadioItemFavouriteStatusChanged(@NonNull Radio radio, boolean isFavourite) {
//        if (isFavourite) {
//            // add to favourites
//            new RestService().fetchResultRadioAddToFavourite(radio.getId()).subscribe(new Observer<Boolean>() {
//                @Override
//                public void onCompleted() {
//                    Log.d(TAG, "completed !!!");
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onNext(Boolean aBoolean) {
//                    Log.d(TAG, "onNext !!!" + aBoolean);
//                }
//            });
//        } else {
//            // remove from favourites
//            new RestService().fetchResultRadioRemoveFromFavourite(radio.getId()).subscribe(new Observer<Boolean>() {
//                @Override
//                public void onCompleted() {
//                    Log.d(TAG, "completed !!!");
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onNext(Boolean aBoolean) {
//                    Log.d(TAG, "onNext !!!" + aBoolean);
//                }
//            });
//        }
//    }
}
