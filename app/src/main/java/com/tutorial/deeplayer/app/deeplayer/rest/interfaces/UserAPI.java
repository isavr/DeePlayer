package com.tutorial.deeplayer.app.deeplayer.rest.interfaces;

import com.tutorial.deeplayer.app.deeplayer.pojo.User;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by ilya.savritsky on 17.07.2015.
 */
public interface UserAPI {
    @GET("/user/me")
    Observable<User> getUser();

    @GET("/user/{id}")
    Observable<User> getUser(@Path("id") int userId);
}
