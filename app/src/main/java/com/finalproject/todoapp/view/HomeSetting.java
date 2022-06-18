package com.finalproject.todoapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.finalproject.todoapp.R;
import com.finalproject.todoapp.databinding.ActivityHomeSettingBinding;

public class HomeSetting extends AppCompatActivity implements ListNameDialog.ListNameDialogListener {

    ActivityHomeSettingBinding binding;

    private TextView tvAboutUs;
    private Switch sDarkMode;

    public void init() {
        tvAboutUs = binding.tvAbout;
        sDarkMode = binding.sDarkmodeSwitch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeSettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        tvAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputDialog("About Us", -1);
            }
        });
    }

    public void openInputDialog(String nameActivity, int pos) {
        ListNameDialog listNameDialog = new ListNameDialog(nameActivity, pos, null);
        listNameDialog.show(getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void sendListName(String nameActivity, int pos, String listName) {

    }
}