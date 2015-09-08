package com.tutorial.deeplayer.app.deeplayer.injections;

import android.app.Application;

import com.tutorial.deeplayer.app.deeplayer.activities.MainActivity;
import com.tutorial.deeplayer.app.deeplayer.activities.MixActivity;
import com.tutorial.deeplayer.app.deeplayer.app.DeePlayerApp;
import com.tutorial.deeplayer.app.deeplayer.fragments.*;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteAlbumsFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteArtistsFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteRadiosFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.library.FavouriteTracksFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.AlbumFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.ArtistFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.RecommendationsControlsFragment;
import com.tutorial.deeplayer.app.deeplayer.fragments.recommended.TracksFragment;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;
import com.tutorial.deeplayer.app.deeplayer.viewmodels.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by pt2121 on 2/20/15.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ViewModelModule.class})
//, DataStoreModule.class, DebugInstrumentationModule.class})
public interface Graph {
    void inject(MainActivity mainActivity);

//    void inject(LoginActivity loginActivity);

    void inject(MixActivity mixActivity);

    void inject(MainActivityFragment mainActivityFragment);

    void inject(PlayerFragment playerFragment);

    void inject(RadioFragment radioFragment);

    void inject(LoginFragment loginFragment);

    void inject(AlbumFragment albumFragment);

    void inject(RecommendationsControlsFragment recommendationsControlsFragment);

    void inject(ArtistFragment artistFragment);

    void inject(TracksFragment tracksFragment);

    void inject(RestService restService);

    void inject(DeePlayerApp deePlayerApp);

    void inject(FavouriteAlbumsFragment favouriteAlbumsFragment);

    void inject(FavouriteArtistsFragment favouriteArtistsFragment);

    void inject(FavouriteRadiosFragment favouriteRadiosFragment);

    void inject(FavouriteTracksFragment favouriteTracksFragment);

    final class Initializer {

        public static Graph init(Application application) {
            return DaggerGraph.builder()
                    .applicationModule(new ApplicationModule(application))
                    .build();
        }

    }
}
