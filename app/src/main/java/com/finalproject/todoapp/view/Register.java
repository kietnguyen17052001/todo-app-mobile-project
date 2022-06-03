package com.finalproject.todoapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.finalproject.todoapp.MainActivity;
import com.finalproject.todoapp.databinding.ActivityRegisterBinding;
import com.finalproject.todoapp.model.User;
import com.finalproject.todoapp.viewmodel.service.UserApiService;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Register extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private Button btnRegister;
    private EditText etUsername, etPassword, etConfirmPassword, etDisplayName;
    private static final int ACCOUNT = 2;
    private User user;
    private UserApiService userApiService;
    private String username, password, confirmPassword, displayName;

    public void init() {
        btnRegister = binding.btnRegister;
        etUsername = binding.usernameRegister;
        etPassword = binding.passwordRegister;
        etConfirmPassword = binding.confirmPasswordRegister;
        etDisplayName = binding.displaynameRegister;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();
        init();
        user = new User();
        userApiService = new UserApiService();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();
                displayName = etDisplayName.getText().toString();
                if (username.isEmpty() && password.isEmpty() && confirmPassword.isEmpty() && displayName.isEmpty()) {
                    if (password.equals(confirmPassword)) {
                        user.setUsername(username);
                        user.setPassword(password);
                        user.setDisplayName(displayName);
                        userApiService.create(user, ACCOUNT)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new SingleObserver<User>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(@NonNull User user) {
                                        Toast.makeText(Register.this, "Success", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                        setResult(RESULT_OK, intent);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        Log.d("ERROR: ", e.getMessage());
                                        Toast.makeText(Register.this, "Username already exists", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Register.this, "Confirm password is not correct", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Register.this, "Please fill it out completely", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}