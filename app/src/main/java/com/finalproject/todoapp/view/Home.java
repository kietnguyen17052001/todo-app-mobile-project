package com.finalproject.todoapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.finalproject.todoapp.MainActivity;
import com.finalproject.todoapp.databinding.ActivityHomeBinding;
import com.finalproject.todoapp.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class Home extends AppCompatActivity {
    private ActivityHomeBinding binding;
    User user;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



        Intent intent = getIntent();
        Integer status = intent.getIntExtra("status", 0);
        if(status == 2) {
            if(intent!=null){
                user = (User) intent.getSerializableExtra("user");
                binding.nameHome.setText(user.getDisplayName().toString());
                if (user.getEmail() != null) {
                    binding.emailHome.setText(user.getEmail().toString());
                } else {
                    binding.emailHome.setText("k co email");
                }
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
        } else if (status == 1){
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            gsc = GoogleSignIn.getClient(this, gso);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            if(acct!=null) {
                String name = acct.getDisplayName();
                String email = acct.getEmail();
                binding.nameHome.setText(name);
                binding.emailHome.setText(email);

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
        }


    }
}