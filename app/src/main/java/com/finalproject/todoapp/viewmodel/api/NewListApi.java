package com.finalproject.todoapp.viewmodel.api;

import com.finalproject.todoapp.model.NewList;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NewListApi {
    @GET("api/users/{id}/newLists")
    Single<List<NewList>> getNewListsByUserId(@Path("id") int id);

    @POST("api/users/{id}/newLists")
    Single<NewList> create(@Path("id") int id, @Body NewList newList);

    @PUT("api/users/{id}/newLists/{newListId}")
    Single<NewList> update(@Path("id") int id, @Path("newListId") int newListId, @Body NewList newList);

    @DELETE("api/users/{id}/newLists/{newListId}")
    void delete(@Path("id") int id, @Path("newListId") int newListId);
}
