package com.finalproject.todoapp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.todoapp.R;
import com.finalproject.todoapp.model.Task;

import java.util.ArrayList;

public class TaskNameAdapter extends RecyclerView.Adapter<TaskNameAdapter.ViewHolder> {

    private ArrayList<Task> listTask;
    private OnCardViewListener onCardViewListener;

    public TaskNameAdapter(ArrayList<Task> listTask, OnCardViewListener context) {
        this.listTask = listTask;
        this.onCardViewListener = context;
    }

    public void setData(ArrayList<Task> listsTask) {
        this.listTask = listsTask;
        notifyDataSetChanged();
    }

    public int getTaskId(int pos) {
        return this.listTask.get(pos).getId();
    }

    public Task getTaskByPos(int pos) {
        return this.listTask.get(pos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_detail, parent,false);
        return new ViewHolder(view, onCardViewListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String taskName = this.listTask.get(position).getName();
        holder.todoCheckbox.setText(taskName);
        boolean isChecked = this.listTask.get(position).isCompleted();
        holder.todoCheckbox.setChecked(isChecked);
    }

    @Override
    public int getItemCount() {
        return this.listTask.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox todoCheckbox;
        public ImageView ivBtnDelete;
        public OnCardViewListener onCardViewListener;

        public ViewHolder(@NonNull View itemView, OnCardViewListener context) {
            super(itemView);

            this.todoCheckbox = itemView.findViewById(R.id.todo_check_box);
            this.ivBtnDelete = itemView.findViewById(R.id.iv_btn_delete);
            this.onCardViewListener = context;

            todoCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    onCardViewListener.onCardViewCLick(pos);
                }
            });

            todoCheckbox.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = getAdapterPosition();
                    onCardViewListener.onCardViewLongCLick(view, pos);
                    return true;
                }
            });

            ivBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    onCardViewListener.onCardViewBtnDeleteClick(pos);
                }
            });
        }
    }

    public interface OnCardViewListener {
        void onCardViewCLick(int pos);
        void onCardViewLongCLick(View view, int pos);
        void onCardViewBtnDeleteClick(int pos);
    }
}
