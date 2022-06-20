package com.finalproject.todoapp.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.finalproject.todoapp.R;

public class TaskNameDialog extends AppCompatDialogFragment {
    private EditText etNameField;
    private TaskNameDialog.TaskNameDialogListener listener;

    private String nameActivity;
    private int pos;
    private String taskOldName;

    public TaskNameDialog(String nameActivity, int pos, String taskOldName) {
        this.nameActivity = nameActivity;
        this.pos = pos;
        this.taskOldName = taskOldName;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        etNameField = view.findViewById(R.id.et_name_field);
        if(this.nameActivity == "Rename") {
            etNameField.setText(taskOldName);
        }
        builder.setView(view)
                .setTitle(this.nameActivity)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String listName = etNameField.getText().toString();
                        listener.sendTaskName(nameActivity, pos, listName);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (TaskNameDialog.TaskNameDialogListener) context;
        } catch (ClassCastException e) {
            throw new
                    ClassCastException(context.toString() + "must implement dialog listener");
        }
    }

    public interface TaskNameDialogListener {
        void sendTaskName(String nameActivity, int pos, String listName);
    }
}
