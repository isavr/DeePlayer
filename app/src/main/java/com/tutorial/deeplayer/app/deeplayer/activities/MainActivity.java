package com.tutorial.deeplayer.app.deeplayer.activities;

import android.os.Bundle;

import com.tutorial.deeplayer.app.deeplayer.R;
import com.tutorial.deeplayer.app.deeplayer.kMP;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        kMP.initialize(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        kMP.startMusicService(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        kMP.stopMusicService(getApplicationContext());
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.context_menu_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
