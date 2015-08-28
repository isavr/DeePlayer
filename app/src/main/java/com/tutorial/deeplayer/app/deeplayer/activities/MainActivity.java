package com.tutorial.deeplayer.app.deeplayer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.kMP;
import com.tutorial.deeplayer.app.deeplayer.services.MusicService;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (kMP.musicService != null) {
            kMP.musicService.updateFavouriteTracksList();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        kMP.initialize(getApplicationContext());
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, kMP.musicConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(kMP.musicConnection);
    }
}
