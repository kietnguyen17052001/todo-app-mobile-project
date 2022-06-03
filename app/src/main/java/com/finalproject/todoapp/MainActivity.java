package com.finalproject.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.todoapp.model.User;
import com.finalproject.todoapp.viewmodel.service.UserApiService;

import java.util.Arrays;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private UserApiService userApiService;
    private User user;
    private static final int GOOGLE = 1, ACCOUNT = 2, FACEBOOK = 4;
    private String username, password;
    private ActivityMainBinding binding;
    private CallbackManager callbackManager;
    private Button btnLoginByAccount;
    private TextView btnRegister;
    private ImageView btnLoginByGoogle, btnLoginByFacebook;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    public void init() {
        btnLoginByAccount = binding.btnLogin;
        btnLoginByGoogle = binding.btnGoogle;
        btnLoginByFacebook = binding.btnFacebook;
        btnRegister = binding.btnRegister;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();
        init();
        user = new User();
        userApiService = new UserApiService();
        btnLoginByAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = binding.username.getText().toString();
                password = binding.password.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
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
                                    if (user != null) {
                                        Intent intent = new Intent(MainActivity.this, Home.class);
                                        intent.putExtra("user", user);
                                        intent.putExtra("status", ACCOUNT);
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

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(MainActivity.this, Register.class);
                startActivity(intentRegister);
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        btnLoginByGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        intent.putExtra("status", FACEBOOK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        btnLoginByFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
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
                String email = task.getResult(ApiException.class).getEmail().toString();
                String name = task.getResult(ApiException.class).getDisplayName().toString();
                user.setEmail(email);
                user.setDisplayName(name);
                userApiService.create(user, GOOGLE)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new SingleObserver<User>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                            }

                            @Override
                            public void onSuccess(@NonNull User user) {
                                Toast.makeText(MainActivity.this, "Tạo mới thành công", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d("ERROR: ", e.getMessage());
                                Toast.makeText(MainActivity.this, "Đã có tài khoản", Toast.LENGTH_LONG).show();
                            }
                        });
                toHome();

            } catch (ApiException e) {
                e.printStackTrace();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    void toHome() {
        finish();
        Intent intent = new Intent(MainActivity.this, Home.class);
        intent.putExtra("status", GOOGLE);
        startActivity(intent);
    }
}