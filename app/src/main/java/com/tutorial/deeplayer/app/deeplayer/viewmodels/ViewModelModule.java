package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ilya.savritsky on 27.07.2015.
 */
@Module
public class ViewModelModule {
    @Provides
    public MainViewModel provideMainViewModel() {
        return new MainViewModel();
    }

    @Provides
    public LoginViewModel provideLoginViewModel() {
        return new LoginViewModel();
    }

    @Provides
    public RadioViewModel provideRadioViewModel() {
        return new RadioViewModel();
    }

    @Provides
    public RecommendedArtistViewModel provideRecommendedArtistViewModel() {
        return new RecommendedArtistViewModel();
    }

    @Provides
    public RecommendedAlbumsViewModel provideRecommendedAlbumViewModel() {
        return new RecommendedAlbumsViewModel();
    }

    @Provides
    public RecommendedTrackViewModel provideRecommendedATrackViewModel() {
        return new RecommendedTrackViewModel();
    }

    @Provides
    public FavouriteAlbumsViewModel provideFavouriteAlbumsViewModel() {
        return new FavouriteAlbumsViewModel();
    }

    @Provides
    public FavouriteArtistViewModel provideFavouriteArtistViewModel() {
        return new FavouriteArtistViewModel();
    }

    @Provides
    public FavouriteRadiosViewModel provideFavouriteRadiosViewModel() {
        return new FavouriteRadiosViewModel();
    }

    @Provides
    public FavouriteTracksViewModel provideFavouriteTracksViewModel() {
        return new FavouriteTracksViewModel();
    }

    @Provides
    public ChartedAlbumsViewModel provideChartedAlbumsViewModel() {
        return new ChartedAlbumsViewModel();
    }

    @Provides
    public ChartedArtistsViewModel provideChartedArtistsViewModel() {
        return new ChartedArtistsViewModel();
    }

    @Provides
    public ChartedTracksViewModel provideChartedTracksViewModel() {
        return new ChartedTracksViewModel();
    }

    @Provides
    public ChartedPlaylistsViewModel provideChartedPlaylistsViewModel() {
        return new ChartedPlaylistsViewModel();
    }
}
