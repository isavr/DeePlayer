package com.tutorial.deeplayer.app.deeplayer.rest.interfaces;

import com.tutorial.deeplayer.app.deeplayer.pojo.*;

import retrofit.http.*;
import rx.Observable;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public interface UserAPI {
    @GET("/user/me")
    Observable<User> getUser();

    @GET("/user/{id}")
    Observable<User> getUser(@Path("id") int userId);


    @GET("/user/me/radios")
    Observable<DataList<Radio>> getUserRadios();

    @GET("/user/me/radios")
    Observable<DataList<Radio>> getUserRadios(@Query("index") int index);

    @POST("/user/me/radios")
    @FormUrlEncoded
    Observable<Boolean> addRadioToFavourite(@Field("radio_id") long radioId);

    @DELETE("/user/me/radios")
    Observable<Boolean> removeRadioFromFavourite(@Query("radio_id") long radioId);


    @GET("/user/me/recommendations/albums")
    Observable<DataList<Album>> getAlbumsRecommendedForUser();

    @GET("/user/me/albums")
    Observable<DataList<Album>> getUserAlbums();

    @GET("/user/me/albums")
    Observable<DataList<Album>> getUserAlbums(@Query("index") int index);

    @POST("/user/me/albums")
    @FormUrlEncoded
    Observable<Boolean> addAlbumToFavourite(@Field("album_id") long albumId);

    @DELETE("/user/me/albums")
    Observable<Boolean> removeAlbumFromFavourite(@Query("album_id") long albumId);


    @GET("/user/me/artists")
    Observable<DataList<Artist>> getUserArtists();

    @GET("/user/me/artists")
    Observable<DataList<Artist>> getUserArtists(@Query("index") int index);

    @GET("/user/me/recommendations/artists")
    Observable<DataList<Artist>> getArtistsRecommendedForUser();

    @POST("/user/me/artists")
    @FormUrlEncoded
    Observable<Boolean> addArtistToFavourite(@Field("artist_id") long artistId);

    @DELETE("/user/me/artists")
    Observable<Boolean> removeArtistFromFavourite(@Query("artist_id") long artistId);


    @GET("/user/me/tracks")
    Observable<DataList<Track>> getUserTracks();

    @GET("/user/me/recommendations/tracks")
    Observable<DataList<Track>> getTracksRecommendedForUser();

    @GET("/user/me/tracks")
    Observable<DataList<Track>> getUserTracks(@Query("index") int index);

    @POST("/user/me/tracks")
    @FormUrlEncoded
    Observable<Boolean> addTrackToFavourite(@Field("track_id") long trackId);

    @DELETE("/user/me/tracks")
    Observable<Boolean> removeTrackFromFavourite(@Query("track_id") long trackId);
}
