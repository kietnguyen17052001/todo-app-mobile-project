package com.finalproject.todoapp.viewmodel.service;

import com.finalproject.todoapp.model.NewList;
import com.finalproject.todoapp.viewmodel.api.NewListApi;

import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewListApiService {
    private static final String BASE_URL = "https://todolistappmobile.herokuapp.com/";
    private NewListApi api;

    public NewListApiService() {
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build().create(NewListApi.class);
    }

    public Single<List<NewList>> getNewListsByUserId(int userId){
        return api.getNewListsByUserId(userId);
    }

    public Single<NewList> create(int userId, NewList newList){
        return api.create(userId, newList);
    }

    public Single<NewList> update(int userId, int newListId, NewList newList){
        return api.update(userId, newListId, newList);
    }

    public void delete(int userId, int newListId){
        api.delete(userId, newListId);
    }
}
