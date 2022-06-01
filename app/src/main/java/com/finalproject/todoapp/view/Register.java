package com.finalproject.todoapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private UserApiService userApiService;
    private ActivityRegisterBinding binding;
<<<<<<< HEAD
    User user;
    String username, password, confirmPassword ,displayName;
=======
    private static final int MY_PERMISSION_REQUEST_CODE_SEND_SMS = 1, ACCOUNT = 2;
    private static final String LOG_TAG = "SendOtpRegister";
    private User user;
    private String username, password, confirmPassword ,displayName, email;
>>>>>>> cc5768bd933362bccddcfd84cf24b9ac959d404b
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();

        user = new User();
        userApiService = new UserApiService();
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = binding.usernameRegister.getText().toString();
                password = binding.passwordRegister.getText().toString();
                confirmPassword = binding.confirmPasswordRegister.getText().toString();
                displayName = binding.displaynameRegister.getText().toString();

                if (checkEmpty(username) && checkEmpty(password) && checkEmpty(confirmPassword) && checkEmpty(displayName)){
                    if (password.equals(confirmPassword)) {
                        user.setUsername(username);
                        user.setPassword(password);
                        user.setDisplayName(displayName);
<<<<<<< HEAD

                        userApiService.create(user, 2)
=======
                        userApiService.create(user, ACCOUNT)
>>>>>>> cc5768bd933362bccddcfd84cf24b9ac959d404b
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

    boolean checkEmpty(String text){
        if (text.length() == 0){
            return false;
        }
        return true;
    }

}