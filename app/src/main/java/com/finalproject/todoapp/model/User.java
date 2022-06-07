package com.finalproject.todoapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("displayName")
    private String displayName;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("createdAt")
    private Timestamp createdAt;
    @SerializedName("updatedAt")
    private Timestamp updatedAt;
    @SerializedName("loginTypeId")
    private int loginTypeId;

    public User(int id, String displayName, String email, String username, String password, Timestamp createdAt, Timestamp updatedAt, int loginTypeId) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.loginTypeId = loginTypeId;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getLoginTypeId() {
        return loginTypeId;
    }

    public void setLoginTypeId(int loginTypeId) {
        this.loginTypeId = loginTypeId;
    }
}
