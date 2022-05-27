package com.finalproject.todoapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;

public class Task implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("categoryId")
    private int categoryId;
    @SerializedName("newListId")
    private int newListId;
    @SerializedName("createdAt")
    private Timestamp createdAt;
    @SerializedName("updatedAt")
    private Timestamp updatedAt;
    @SerializedName("completed")
    private boolean completed;

    public Task(int id, String name, String description, int categoryId, int newListId, Timestamp createdAt, Timestamp updatedAt, boolean completed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.newListId = newListId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getNewListId() {
        return newListId;
    }

    public void setNewListId(int newListId) {
        this.newListId = newListId;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
