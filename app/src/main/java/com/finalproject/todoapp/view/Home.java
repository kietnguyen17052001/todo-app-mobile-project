package com.finalproject.todoapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.finalproject.todoapp.MainActivity;
import com.finalproject.todoapp.R;
import com.finalproject.todoapp.SessionManagement;
import com.finalproject.todoapp.databinding.ActivityHomeBinding;
import com.finalproject.todoapp.model.NewList;
import com.finalproject.todoapp.model.User;
import com.finalproject.todoapp.viewmodel.service.NewListApiService;
import com.finalproject.todoapp.viewmodel.service.UserApiService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Home extends AppCompatActivity implements ListNoteAdapter.OnCardViewListener {
    private ActivityHomeBinding binding;
//    private Button btnLogout;
    private CardView cvMyDay;
    private CardView cvImportant;
    private RecyclerView rcvTaskList;
    private FloatingActionButton btnAddTask;

    private static final int GOOGLE = 1, FACEBOOK = 4;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount googleSignInAccount;
    private int userId;
    private User user;
    private UserApiService userApiService;
    private NewListApiService newListApiService;
    private SessionManagement sessionManagement;

    private ListNoteAdapter listNoteAdapter;

    // initial value
    public void init() {
//        btnLogout = binding.btnLogout;
        cvMyDay = binding.cvMyday;
        cvImportant = binding.cvImportant;
        rcvTaskList = binding.rcvTaskList;
        btnAddTask = binding.btnAddTask;
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
        listNoteAdapter = new ListNoteAdapter(new ArrayList<>(), this);
        rcvTaskList.setAdapter(listNoteAdapter);
        rcvTaskList.setLayoutManager(new LinearLayoutManager(this));
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
                            if (userResponse.getLoginTypeId() == GOOGLE) {
                                googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                                googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), googleSignInOptions);
                            }
                            // main
                            showListItem(userResponse.getId());
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
        } else {
            int loginType = getIntent().getIntExtra("loginTypeId", 0);
            if (loginType == GOOGLE) {
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
                                    // main
                                    showListItem(userResponse.getId());

                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                }
                            });
                }
            } else {
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
                                    user.setUsername(object.getString("id"));
                                    user.setDisplayName(object.getString("name"));
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
                                                    showListItem(user.getId());
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
                                                                    showListItem(user.getId());
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

//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (user.getLoginTypeId() == GOOGLE) {
//                    googleSignInClient.signOut();
//                } else if (user.getLoginTypeId() == FACEBOOK) {
//                    LoginManager.getInstance().logOut();
//                }
//                logout(view);
//            }
//        });

        cvMyDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Change to list of my day");
            }
        });

        cvImportant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Change to list of important");
            }
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("in create");
                createNewList(userId);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_logout_menu:
                if (user.getLoginTypeId() == GOOGLE) {
                    googleSignInClient.signOut();
                } else if (user.getLoginTypeId() == FACEBOOK) {
                    LoginManager.getInstance().logOut();
                }
                logout(null);
                return true;
        }
        return true;
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

    // get list item by userId
    public void showListItem(int userId) {
        newListApiService = new NewListApiService();
        newListApiService.getNewListsByUserId(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<NewList>>() {
                    @Override
                    public void onSuccess(@NonNull List<NewList> newLists) {
                        for (NewList newList : newLists) {
                            Log.d("Name", newList.getName());
                        }
                        listNoteAdapter.setData(new ArrayList<>(newLists));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.getMessage());
                    }
                });
    }

    public void createNewList(int userId) {
        newListApiService = new NewListApiService();
        newListApiService.create(userId, new NewList(
                50, "test1",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<NewList>() {
                        @Override
                        public void onSuccess(@NonNull NewList newList) {
                            Log.d("success", "can create!");
                            showListItem(userId);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e("error", e.getMessage());
                        }
                    });
    }

    @Override
    public void onCardViewCLick(int pos) {
        System.out.println("rcv clicked!");
    }
}