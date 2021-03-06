package com.finalproject.todoapp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.todoapp.R;
import com.finalproject.todoapp.model.NewList;

import java.util.ArrayList;

public class ListNoteAdapter extends RecyclerView.Adapter<ListNoteAdapter.ViewHolder> {

    private ArrayList<NewList> setNewListNote;
    private OnCardViewListener onCardViewListener;

    public ListNoteAdapter(ArrayList<NewList> setNewListNote, OnCardViewListener context) {
        this.setNewListNote = setNewListNote;
        this.onCardViewListener = context;
    }

    public void setData(ArrayList<NewList> lists) {
        this.setNewListNote = lists;
        notifyDataSetChanged();
    }

    public int getListId(int pos) {
        return this.setNewListNote.get(pos).getId();
    }

    public NewList getListByPos(int pos) {
        return this.setNewListNote.get(pos);
    }

    public void onMoveItem(int fromPos, int toPos) {
        NewList fromList = this.setNewListNote.get(fromPos);
        this.setNewListNote.remove(fromList);
        this.setNewListNote.add(toPos, fromList);
        System.out.println("" + fromList + " " + toPos);
        notifyItemMoved(fromPos, toPos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_layout, parent, false);
        return new ViewHolder(view, onCardViewListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNewListName.setText(this.setNewListNote.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return setNewListNote.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNewListName;
        public OnCardViewListener onCardViewListener;
        public ImageButton ibMenu;

        public ViewHolder(View view, OnCardViewListener onCardViewListener) {
            super(view);
            this.tvNewListName = view.findViewById(R.id.tv_newlist_name);
            this.onCardViewListener = onCardViewListener;
            this.ibMenu = view.findViewById(R.id.ib_menu);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int posClick = getAdapterPosition();
                    onCardViewListener.onCardViewCLick(posClick);
                }
            });

            ibMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int posClick = getAdapterPosition();
                    onCardViewListener.onCardViewMenuClick(view, posClick);
                }
            });
        }


    }

    public interface OnCardViewListener {
        void onCardViewCLick(int pos);
        void onCardViewMenuClick(View view, int pos);
    }

}