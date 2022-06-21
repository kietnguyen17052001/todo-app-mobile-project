package com.finalproject.todoapp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.finalproject.todoapp.R;
import com.finalproject.todoapp.SessionManagement;
import com.finalproject.todoapp.databinding.ActivityDetailBinding;
import com.finalproject.todoapp.model.Task;
import com.finalproject.todoapp.model.User;
import com.finalproject.todoapp.viewmodel.service.TaskApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail extends AppCompatActivity implements TaskNameAdapter.OnCardViewListener, TaskNameDialog.TaskNameDialogListener{
    private ActivityDetailBinding binding;
    private SessionManagement sessionManagement;
    private int userId;
    private TaskApiService taskApiService;
    private String listType;
    private String listId;

    private FloatingActionButton btnAddTaskDetail;
    private RecyclerView tasksRecyclerView;

    private TaskNameAdapter taskNameAdapter;

    public void init(){
        btnAddTaskDetail = binding.btnAddTask;
        tasksRecyclerView = binding.tasksRecyclerIew;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManagement = new SessionManagement(Detail.this);
        userId = sessionManagement.getSession();
        taskApiService = new TaskApiService();
        init();

        taskNameAdapter = new TaskNameAdapter(new ArrayList<>(), this);
        tasksRecyclerView.setAdapter(taskNameAdapter);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        listType = intent.getStringExtra("listType");
        listId = intent.getStringExtra("listId");
        showListTask();

        btnAddTaskDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputDialog("Create", -1, null);
            }
        });
    }
    public void openInputDialog(String nameActivity, int pos, String taskName) {//pos: vị trí recycle view
        TaskNameDialog taskNameDialog = new TaskNameDialog(nameActivity, pos, taskName);
        taskNameDialog.show(getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void sendTaskName(String nameActivity, int pos, String taskName) {
        if(nameActivity == "Create") {
            createNewTask(taskName);
        }
        if(nameActivity == "Edit"){
            editTask(pos, taskName);
        }
    }

    public void showListTask() {
        if(listType.equals("MyDay")) {
            taskApiService.getMyDayTasks(userId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Task>>() {
                        @Override
                        public void onSuccess(@NonNull List<Task> tasks) {
//                            for(Task task : tasks) {
//                                Log.d("taskname", task.getName());
//                            }
                            taskNameAdapter.setData(new ArrayList<>(tasks));
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e("error", e.getMessage());
                        }
                    });
        }
        if(listType.equals("Important")) {
            taskApiService.getImportantTasks(userId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Task>>() {
                        @Override
                        public void onSuccess(@NonNull List<Task> tasks) {
//                            for(Task task : tasks) {
//                                Log.d("taskname", task.getName());
//                            }
                            taskNameAdapter.setData(new ArrayList<>(tasks));
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e("error", e.getMessage());
                        }
                    });
        }
        if(listType.equals("NewList")) {
            taskApiService.getNewListTasks(userId, Integer.parseInt(listId))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Task>>() {
                        @Override
                        public void onSuccess(@NonNull List<Task> tasks) {
//                            for(Task task : tasks) {
//                                Log.d("taskname", task.getName());
//                            }
                            taskNameAdapter.setData(new ArrayList<>(tasks));
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e("error", e.getMessage());
                        }
                    });
        }
    }

    public void deleteTask(int pos){
        int taskId = taskNameAdapter.getTaskId(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        builder.setCancelable(true);
        builder.setTitle("WARNING!");
        builder.setMessage("Are you sure want to delete?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                taskApiService.delete(userId, taskId)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                showListTask();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
            }
        });
        builder.show();
    }
    public void editTask(int pos, String newTaskName){
        Task task = taskNameAdapter.getTaskByPos(pos);
        Task updateTask = new Task();
        updateTask.setId(task.getId());
        updateTask.setName(newTaskName);
        updateTask.setDescription(task.getDescription());
        updateTask.setCompleted(task.isCompleted());
        taskApiService.update(userId, updateTask)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Task>() {
                    @Override
                    public void onSuccess(@NonNull Task task) {
                        showListTask();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.getMessage());
                    }
                });
    }

    @Override
    public void onCardViewCLick(int pos) {
        Task task = taskNameAdapter.getTaskByPos(pos);
        Task updateTask = new Task();
        updateTask.setId(task.getId());
        updateTask.setName(task.getName());
        updateTask.setDescription(task.getDescription());
        if(task.isCompleted() == false) {
            updateTask.setCompleted(true);
        }
        else {
            updateTask.setCompleted(false);
        }
        taskApiService.update(userId, updateTask)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Task>() {
                    @Override
                    public void onSuccess(@NonNull Task task) {
                        showListTask();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.getMessage());
                    }
                });
    }

    @Override
    public void onCardViewBtnDeleteClick(int pos) {
        deleteTask(pos);
    }

    @Override
    public void onCardViewLongCLick(View view, int pos) {
        Task task = taskNameAdapter.getTaskByPos(pos);
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.task_menu_long_click);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.task_edit:
                        openInputDialog("Edit", pos, task.getName());
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
}