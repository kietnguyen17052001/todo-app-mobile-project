package com.finalproject.todoapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.finalproject.todoapp.SessionManagement;
import com.finalproject.todoapp.databinding.ActivityDetailBinding;
import com.finalproject.todoapp.viewmodel.service.TaskApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Detail extends AppCompatActivity implements ListNameDialog.ListNameDialogListener{
    private ActivityDetailBinding binding;
    private SessionManagement sessionManagement;
    private int userId;
    private TaskApiService taskApiService;

    private FloatingActionButton btnAddTaskDetail;
    private RecyclerView tasksRecyclerView;

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
        init();

        btnAddTaskDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputDialog("Create", -1);
            }
        });
    }
    public void openInputDialog(String nameActivity, int pos) {//pos: vị trí recycle view
        ListNameDialog listNameDialog = new ListNameDialog(nameActivity, pos);
        listNameDialog.show(getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void sendListName(String nameActivity, int pos, String listName) {
        if(nameActivity == "create") {
            createNewTask();
        }
        if(nameActivity == "edit"){
            editTask(pos);
        }
        if(nameActivity == "delete"){
            deleteTask(pos);
        }
    }
    public void createNewTask(){

    }
    public void deleteTask(int pos){

    }
    public void editTask(int pos, int idTask){

    }
}