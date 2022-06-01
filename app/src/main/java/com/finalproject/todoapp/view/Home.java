package com.finalproject.todoapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.finalproject.todoapp.MainActivity;
import com.finalproject.todoapp.databinding.ActivityHomeBinding;
import com.finalproject.todoapp.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {
    private ActivityHomeBinding binding;
    User user;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private final int ACCOUNT = 2, GOOGLE = 1, FACEBOOK = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



        Intent intent = getIntent();
        Integer status = intent.getIntExtra("status", 0);
        if(status == ACCOUNT) {
            if(intent!=null){
                user = (User) intent.getSerializableExtra("user");
                binding.nameHome.setText(user.getDisplayName().toString());
                binding.btnLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = new Intent(Home.this, MainActivity.class);
                        startActivity(intent1);
                        finish();
                        Toast.makeText(Home.this, "LogOut", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else if (status == GOOGLE){
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            gsc = GoogleSignIn.getClient(this, gso);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            if(acct!=null) {
                String name = acct.getDisplayName();
                binding.nameHome.setText(name);
                String url = acct.getPhotoUrl().toString();
                Picasso.get().load(url).into(binding.avatarHome);

                binding.btnLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gsc.signOut();
                        Intent intent1 = new Intent(Home.this, MainActivity.class);
                        startActivity(intent1);
                        finish();
                        Toast.makeText(Home.this, "LogOut", Toast.LENGTH_LONG).show();

                    }
                });
            }
        } else if(status == FACEBOOK){

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
                                binding.nameHome.setText(name);
                                String id = object.getString("id");
                                String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                Picasso.get().load(url).into(binding.avatarHome);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link, picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();

            binding.btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(Home.this, MainActivity.class));
                    finish();
                }
            });
        }


    }
}