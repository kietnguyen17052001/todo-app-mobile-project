package com.finalproject.todoapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.finalproject.todoapp.model.User;

public class SessionManagement {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String MY_PREFERENCES = "myPrefs";
    private static final String SESSION_KEY = "sessionKey";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user) {
        editor.putInt(SESSION_KEY, user.getId()).commit();
    }

    public int getSession() {
        return sharedPreferences.getInt(SESSION_KEY, -1);
    }

    public void removeSession() {
        editor.putInt(SESSION_KEY, -1).commit();
    }
}
