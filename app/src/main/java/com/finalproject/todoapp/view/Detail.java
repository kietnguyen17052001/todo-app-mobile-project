package com.finalproject.todoapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.finalproject.todoapp.SessionManagement;
import com.finalproject.todoapp.databinding.ActivityDetailBinding;
import com.finalproject.todoapp.model.Task;
import com.finalproject.todoapp.model.User;
import com.finalproject.todoapp.viewmodel.service.TaskApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Detail extends AppCompatActivity implements TaskNameDialog.TaskNameDialogListener{
    private ActivityDetailBinding binding;
    private SessionManagement sessionManagement;
    private int userId;
    private TaskApiService taskApiService;

    private FloatingActionButton btnAddTaskDetail;
    private RecyclerView tasksRecyclerView;
    private String listType;
    private String listId;

    public void init(){
        btnAddTaskDetail = binding.btnAddTaskDetail;
        tasksRecyclerView = binding.tasksRecyclerView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        sessionManagement = new SessionManagement(Detail.this);
        userId = sessionManagement.getSession();
        taskApiService = new TaskApiService();
        init();

        Intent intent = getIntent();
        listType = intent.getStringExtra("listType");
        listId = intent.getStringExtra("listId");
        System.out.println("Listtype ne "+ listType);

        btnAddTaskDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputDialog("Create", -1);
            }
        });
    }
    public void openInputDialog(String nameActivity, int pos) {//pos: vị trí recycle view
        TaskNameDialog taskNameDialog = new TaskNameDialog(nameActivity, pos, null);
        taskNameDialog.show(getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void sendTaskName(String nameActivity, int pos, String taskName) {
        if(nameActivity == "Create") {

            createNewTask(taskName);
        }
        if(nameActivity == "edit"){
//            editTask(pos);
        }
        if(nameActivity == "delete"){
            deleteTask(pos);
        }
    }
    public void createNewTask(String taskName){
        if(listType.equals("MyDay")){
            Task newTask = new Task();
//            newTask.setCompleted(false);
//            newTask.setNewListId(Integer.parseInt(listId));
            newTask.setName(taskName);
            System.out.println(newTask.getName());
            System.out.println("Helllooooooooooooooooooooooooooooooooooooooooooooooo");
            taskApiService.createMyDayTask(userId, newTask)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<Task>() {
                        @Override
                        public void onSuccess(@NonNull Task task) {
                            System.out.println("Success");
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            System.out.println("Error");
                            Log.e("Error", e.getMessage());
                        }
                    });
        }
        if(listType == "Important"){

        }
        if(listType == "NewTask"){

        }
    }
    public void deleteTask(int pos){

    }
    public void editTask(int pos, int idTask){

    }
}