package com.finalproject.todoapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.finalproject.todoapp.SessionManagement;
import com.finalproject.todoapp.databinding.ActivityDetailBinding;
import com.finalproject.todoapp.viewmodel.service.TaskApiService;

public class Detail extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private SessionManagement sessionManagement;
    private int userId;
    private TaskApiService taskApiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        sessionManagement = new SessionManagement(Detail.this);
        userId = sessionManagement.getSession();
    }
}