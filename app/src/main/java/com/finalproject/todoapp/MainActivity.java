package com.finalproject.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
=======
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.finalproject.todoapp.databinding.ActivityMainBinding;
import com.finalproject.todoapp.model.Task;
import com.finalproject.todoapp.model.User;
import com.finalproject.todoapp.viewmodel.service.TaskApiService;
import com.finalproject.todoapp.viewmodel.service.UserApiService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
>>>>>>> 202adbb893cd27a82b6a6e5ddd304347706deb54

public class MainActivity extends AppCompatActivity {
    private UserApiService userApiService;
    private String username = "kietdeptrai", password = "";
    private int newListId = 4;
    private ActivityMainBinding binding;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView ggBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.activity_main);

        ggBtn = findViewById(R.id.btn_gg);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        ggBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
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
        if (requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToLogin();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    void navigateToLogin(){
        finish();
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
=======
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        userApiService = new UserApiService();
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
                            Log.d("name", user.getDisplayName());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
>>>>>>> 202adbb893cd27a82b6a6e5ddd304347706deb54
    }
}