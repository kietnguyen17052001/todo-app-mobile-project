package com.finalproject.todoapp.viewmodel.service;

import com.finalproject.todoapp.model.Task;
import com.finalproject.todoapp.viewmodel.api.TaskApi;

import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskApiService {
    private static final String BASE_URL = "https://todolistappmobile.herokuapp.com/";
    private TaskApi api;

    public TaskApiService() {
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build().create(TaskApi.class);
    }

    Single<List<Task>> getMyDayTasks(int userId) {
        return api.getMyDayTasks(userId);
    }

    Single<List<Task>> getImportantTasks(int userId) {
        return api.getImportantTasks(userId);
    }

    Single<List<Task>> getNewListTasks(int userId, int newListId) {
        return api.getNewListTasks(userId, newListId);
    }

    Single<Task> createMyDayTask(int userId, Task task) {
        return api.createMyDayTask(userId, task);
    }

    Single<Task> createImportantTask(int userId, Task task) {
        return api.createImportantTask(userId, task);
    }

    Single<Task> createNewListTask(int userId, int newListId, Task task) {
        return api.createNewListTask(userId, newListId, task);
    }

    Single<Task> update(int userId, int taskId, Task task) {
        return api.update(userId, taskId, task);
    }

    Single<Task> completed(int userId, int taskId) {
        return api.completed(userId, taskId);
    }

    void delete(int userId, int taskId) {
        api.delete(userId, taskId);
    }
}
