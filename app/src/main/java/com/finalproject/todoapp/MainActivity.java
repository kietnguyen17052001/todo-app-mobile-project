package com.finalproject.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.finalproject.todoapp.databinding.ActivityMainBinding;
import com.finalproject.todoapp.view.Home;
import com.finalproject.todoapp.view.Register;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import android.util.Log;
import android.widget.Toast;

import com.finalproject.todoapp.model.User;
import com.finalproject.todoapp.viewmodel.service.UserApiService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity{
    private UserApiService userApiService;
    private User user;
    private String username, password;
    private int newListId = 4;
    private ActivityMainBinding binding;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();

        userApiService = new UserApiService();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = binding.username.getText().toString();
                password = binding.password.getText().toString();

                if (username.length() == 0 || password.length() == 0){
                    Toast.makeText(MainActivity.this, "Please enter your username or password", Toast.LENGTH_LONG).show();

                } else {
                    userApiService.getUserByUsernameAndPassword(username, password)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new SingleObserver<User>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onSuccess(@NonNull User user) {
                                    if (user != null){
                                        Intent intent = new Intent(MainActivity.this, Home.class);
                                        intent.putExtra("user", user);
                                        intent.putExtra("status", 2);
                                        startActivity(intent);
                                    }
                                }
                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Log.d("ERROR: ", e.getMessage());
                                    Toast.makeText(MainActivity.this, "User does not exist", Toast.LENGTH_LONG).show();

                                }
                            });
                }
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(MainActivity.this, Register.class);
                startActivity(intentRegister);
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        binding.btnGg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        binding.btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });
    }

    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                toHome();

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
    void toHome() {
        finish();
        Intent intent = new Intent(MainActivity.this, Home.class);
        intent.putExtra("status", 1);
        startActivity(intent);
    }
}