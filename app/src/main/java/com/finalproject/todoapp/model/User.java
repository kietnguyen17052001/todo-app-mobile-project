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
    @SerializedName("createdAt")
    private Timestamp createdAt;
    @SerializedName("updatedAt")
    private Timestamp updatedAt;

    public User(int id, String displayName, String email, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
}
