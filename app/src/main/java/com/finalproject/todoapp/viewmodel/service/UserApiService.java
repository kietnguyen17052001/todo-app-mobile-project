package com.finalproject.todoapp.viewmodel.service;

import com.finalproject.todoapp.model.User;
import com.finalproject.todoapp.viewmodel.api.UserApi;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApiService {
    private static final String BASE_URL = "https://todolistappmobile.herokuapp.com/";
    private UserApi api;

    public UserApiService() {
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build().create(UserApi.class);
    }

    public Single<User> getUserByUsernameAndPassword(String username, String password) {
        return api.getUserByUsernameAndPassword(username, password);
    }

    public Single<User> getUserById(int id) {
        return api.getUserById(id);
    }

    public Single<User> getUserByUid(String uid) {
        return api.getUserByUid(uid);
    }

    public Single<User> getUserByEmail(String email) {
        return api.getUserByEmail(email);
    }

    public Single<User> create(User user, int loginTypeId) {
        return api.create(user, loginTypeId);
    }

    public Single<User> update(int id, User user) {
        return api.update(id, user);
    }
}
