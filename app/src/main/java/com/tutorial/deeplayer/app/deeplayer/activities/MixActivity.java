package com.tutorial.deeplayer.app.deeplayer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.fragments.RadioFragment;
import com.tutorial.deeplayer.app.deeplayer.kMP;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.services.MusicService;
import com.tutorial.deeplayer.app.deeplayer.utils.DialogFactory;
import com.tutorial.deeplayer.app.deeplayer.views.RadioView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MixActivity extends BaseMediaActivity implements RadioView.OnRadioItemInteractionListener
        /*, RadioPlayerListener*/ {
    public static final String TAG = MixActivity.class.getSimpleName();

    @Bind(R.id.fragment_container)
    View container;

    @Bind(R.id.player)
    View playerContainer;

//    private PlayerFragment playerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix);
        ButterKnife.bind(this);
        addFragment();
//        addPlayerFragment();
        playerContainer.setVisibility(View.GONE);
        setMusicController();

        if (playbackPaused) {
            setMusicController();
            playbackPaused = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, kMP.musicConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
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
    public void onStopProgress() {
        DialogFactory.closeAlertDialog(getSupportFragmentManager());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        unbindService(kMP.musicConnection);
        musicController.hide();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRadioItemInteraction(@NonNull Radio radio) {
        playerContainer.setVisibility(View.VISIBLE);
//        try {
        Log.d(TAG, "play radio " + radio.getTitle());
        if (kMP.musicService != null) {
            kMP.musicService.initPlayer(MusicService.PlayerType.RADIO);
            kMP.musicService.setData(radio);
            kMP.musicService.playRadio();
        }
    }
}
