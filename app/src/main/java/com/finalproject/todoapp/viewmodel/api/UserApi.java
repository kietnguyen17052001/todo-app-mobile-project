package com.finalproject.todoapp.viewmodel.api;

import com.finalproject.todoapp.model.User;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @GET("api/users/{email}")
    Single<User> getUserByEmail(@Path("email") String email);

    @POST("api/users")
    Single<User> create(@Body User user);

    @PUT("api/users/{id}")
    Single<User> update(@Path("id") int id, @Body User user);
}
