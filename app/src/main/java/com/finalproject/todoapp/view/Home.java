package com.finalproject.todoapp.view;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements ListNoteAdapter.OnCardViewListener, ListNameDialog.ListNameDialogListener {
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
        newListApiService = new NewListApiService();
        listNoteAdapter = new ListNoteAdapter(new ArrayList<>(), this);
        rcvTaskList.setAdapter(listNoteAdapter);
        rcvTaskList.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rcvTaskList);
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
                            showListItem();
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
                                    userId = userResponse.getId();
                                    // main
                                    showListItem();

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
                                                    userId = user.getId();
                                                    showListItem();
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
                                                                    userId = user.getId();
                                                                    showListItem();
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
                moveToDetail("MyDay", -1);
            }
        });

        cvImportant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToDetail("Important", -1);
            }
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputDialog("Create", -1, null);
                //createNewList(userId);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar, menu);
        return true;
    }

    // For move back to Main Activity
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

    // Home function
    public void moveToHomeSetting() {
        Intent intent = new Intent(Home.this, HomeSetting.class);
        startActivityForResult(intent, 1000);
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
            case R.id.btn_home_setting:
                moveToHomeSetting();
                return true;
        }
        return true;
    }

    public void openInputDialog(String nameActivity, int pos, String listOldName) {
        ListNameDialog listNameDialog = new ListNameDialog(nameActivity, pos, listOldName);
        listNameDialog.show(getSupportFragmentManager(), "Dialog");
    }

    // get list item by userId
    public void showListItem() {
        newListApiService.getNewListsByUserId(this.userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<NewList>>() {
                    @Override
                    public void onSuccess(@NonNull List<NewList> newLists) {
//                        for (NewList newList : newLists) {
//                            Log.d("Name", newList.getId() + newList.getName());
//                        }
                        listNoteAdapter.setData(new ArrayList<>(newLists));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.getMessage());
                    }
                });
    }

    public void createNewList(String listName) {
        NewList addNewList = new NewList();
        addNewList.setName(listName);
        newListApiService.create(this.userId, addNewList)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NewList>() {
                    @Override
                    public void onSuccess(@NonNull NewList newList) {
                        showListItem();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.getMessage());
                    }
                });
    }

    public void renameList(int pos, String listName) {
        NewList list = listNoteAdapter.getListByPos(pos);
        list.setName(listName);
        newListApiService.update(userId, list)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NewList>() {
                    @Override
                    public void onSuccess(@NonNull NewList newList) {
                        showListItem();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.getMessage());
                    }
                });
    }

    public void deleteList(int pos) {
        int listId = listNoteAdapter.getListId(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setCancelable(true);
        builder.setTitle("WARNING!");
        builder.setMessage("Are you sure want to delete?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newListApiService.delete(userId, listId)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                showListItem();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showListItem();
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onCardViewCLick(int pos) {
        int listId = listNoteAdapter.getListId(pos);
        moveToDetail("NewList", listId);
    }

    @Override
    public void onCardViewMenuClick(View view, int pos) {
        NewList list = listNoteAdapter.getListByPos(pos);
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.home_item_long_click);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.option_edit:
                        openInputDialog("Rename", pos, list.getName());
                        return true;
                    case R.id.option_delete:
                        deleteList(pos);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    @Override
    public void sendListName(String nameActivity, int pos, String listName) {
        if (nameActivity == "Create") {
            createNewList(listName);
        } else {
            renameList(pos, listName);
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                    ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@androidx.annotation.NonNull RecyclerView recyclerView, @androidx.annotation.NonNull RecyclerView.ViewHolder viewHolder, @androidx.annotation.NonNull RecyclerView.ViewHolder target) {
//            listNoteAdapter.onMoveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(@androidx.annotation.NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteList(viewHolder.getAdapterPosition());
        }
    };

    // For task list detail
    public void moveToDetail(String listType, int listId) {
        Intent intent = new Intent(Home.this, Detail.class);
        intent.putExtra("listType", listType);
        intent.putExtra("listId", String.valueOf(listId));
        startActivity(intent);
    }
}