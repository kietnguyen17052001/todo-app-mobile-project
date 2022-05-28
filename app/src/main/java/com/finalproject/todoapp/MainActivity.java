package com.finalproject.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.finalproject.todoapp.model.Task;
import com.finalproject.todoapp.viewmodel.service.TaskApiService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private TaskApiService taskApiService;
    private int userId = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskApiService = new TaskApiService();
        taskApiService.getMyDayTasks(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Task>>() {
                    @Override
                    public void onSuccess(@NonNull List<Task> tasks) {
                        for (Task task : tasks){
                            Log.d("name", task.getName());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("Bug", e.getMessage());
                    }
                });
    }
}