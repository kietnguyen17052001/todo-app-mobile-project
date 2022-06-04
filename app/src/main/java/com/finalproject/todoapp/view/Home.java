package com.finalproject.todoapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.finalproject.todoapp.MainActivity;
import com.finalproject.todoapp.SessionManagement;
import com.finalproject.todoapp.databinding.ActivityHomeBinding;
import com.finalproject.todoapp.model.User;
import com.finalproject.todoapp.viewmodel.service.UserApiService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Home extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Button btnLogout;
    private static final int GOOGLE = 1, ACCOUNT = 2, FACEBOOK = 4;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount googleSignInAccount;
    private int userId;
    private User user;
    private UserApiService userApiService;
    private SessionManagement sessionManagement;

    // initial value
    public void init() {
        btnLogout = binding.btnLogout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        init();
        userApiService = new UserApiService();
        user = new User();
        sessionManagement = new SessionManagement(Home.this);
        userId = sessionManagement.getSession();
        Log.d("userId", String.valueOf(userId));
        if (userId != -1) {
            userApiService.getUserById(userId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new SingleObserver<User>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull User userResponse) {
                            user = userResponse;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
        } else {
            if (getIntent().getIntExtra("loginTypeId", 0) == GOOGLE) {
                googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
                googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
                if (googleSignInAccount != null) {
                    userApiService.getUserByEmail(googleSignInAccount.getEmail())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new SingleObserver<User>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                }

                                @Override
                                public void onSuccess(@NonNull User userResponse) {
                                    sessionManagement.saveSession(userResponse);
                                    user = userResponse;
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                }
                            });
                }
            } else if (user.getId() == FACEBOOK) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                try {
                                    String name = object.getString("name");
                                    String id = object.getString("id");
                                    user.setUsername(id);
                                    user.setDisplayName(name);
                                    userApiService.create(user, FACEBOOK)
                                            .subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeWith(new SingleObserver<User>() {
                                                @Override
                                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                                }

                                                @Override
                                                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull User user) {
                                                    sessionManagement.saveSession(user);
                                                }

                                                @Override
                                                public void onError(@NonNull Throwable e) {
                                                    userApiService.getUserByUid(user.getUsername())
                                                            .subscribeOn(Schedulers.newThread())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribeWith(new SingleObserver<User>() {
                                                                @Override
                                                                public void onSubscribe(@NonNull Disposable d) {

                                                                }

                                                                @Override
                                                                public void onSuccess(@NonNull User user) {
                                                                    sessionManagement.saveSession(user);
                                                                }

                                                                @Override
                                                                public void onError(@NonNull Throwable e) {

                                                                }
                                                            });
                                                }
                                            });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link, picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getLoginTypeId() == GOOGLE) {
                    googleSignInClient.signOut();
                } else if (user.getLoginTypeId() == FACEBOOK) {
                    LoginManager.getInstance().logOut();
                }
                logout(view);
            }
        });
    }

    public void logout(View view) {
        SessionManagement sessionManagement = new SessionManagement(Home.this);
        sessionManagement.removeSession();
        moveToMainActivity();
    }


    public void moveToMainActivity() {
        Intent intent = new Intent(Home.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}