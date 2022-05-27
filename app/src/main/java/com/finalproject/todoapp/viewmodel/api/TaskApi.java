package com.finalproject.todoapp.viewmodel.api;

import com.finalproject.todoapp.model.Task;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskApi {
    @GET("api/users/{id}/myday/tasks")
    Single<List<Task>> getMyDayTasks(@Path("id") int id);

    @GET("api/users/{id}/important/tasks")
    Single<List<Task>> getImportantTasks(@Path("id") int id);

    @GET("api/users/{id}/newLists/{newListId}/tasks")
    Single<List<Task>> getNewListTasks(@Path("id") int id, @Path("newListId") int newListId);

    @POST("api/users/{id}/myday/tasks")
    Single<Task> createMyDayTask(@Path("id") int id, @Body Task task);

    @POST("api/users/{id}/important/tasks")
    Single<Task> createImportantTask(@Path("id") int id, @Body Task task);

    @POST("api/users/{id}/newLists/{newListId}/tasks")
    Single<Task> createNewListTask(@Path("id") int id, @Path("newListId") int newListId, @Body Task task);

    @PUT("api/users/{id}/tasks/{taskId}")
    Single<Task> update(@Path("id") int id, @Path("taskId") int taskId, @Body Task task);

    @PUT("api/users/{id}/tasks/{taskId}/completed")
    Single<Task> completed(@Path("id") int id, @Path("taskId") int taskId);

    @DELETE("api/users/{id}/tasks/{taskId}")
    void delete(@Path("id") int id, @Path("taskId") int taskId);
}
