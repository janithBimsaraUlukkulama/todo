package com.example.todo.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.AddNewGroup;
import com.example.todo.MainActivity;
import com.example.todo.Model.TodoGroupModel;
import com.example.todo.R;
import com.example.todo.Utils.DBHelper;

import java.util.List;
import java.util.function.UnaryOperator;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<TodoGroupModel> groupList;
    private MainActivity activity;
    private DBHelper db;
    String Tag = "Group Adapter";

    public GroupAdapter(DBHelper dbHelper, MainActivity mainActivity) {
        this.activity = mainActivity;
        this.db = dbHelper;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_layout, parent, false);
        return new GroupViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        TodoGroupModel item = groupList.get(position);
        holder.checkBox.setText(item.getName());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));

        // if status true add decorator to text
        if (toBoolean(item.getStatus()) == true) {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateGroupStatus(item.getId(), 1);
                } else
                    db.updateGroupStatus(item.getId(), 0);
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Tag, "onClick: ");
                Intent intent = new Intent(getContext(), com.example.todo.anjalee.MainActivity.class);
                intent.putExtra("group",position);

                getContext().startActivity(intent);
            }
        });

        holder.checkBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editGroup(position);
                return false;
            }
        });

    }

    public boolean toBoolean(int num) {
        return num != 0;
    }

    public void setGroup(List<TodoGroupModel> list) {
        this.groupList = list;
        notifyDataSetChanged();
    }

    public void setStatus(int position) {
        TodoGroupModel item = groupList.get(position);
        int status = toBoolean(item.getStatus()) == true ? 0 : 1;

        db.updateGroupStatus(item.getId(), status);
        item.setStatus(status);
        notifyItemChanged(position);

    }

    public void deleteGroup(int position) {
        TodoGroupModel item = groupList.get(position);
        db.deleteGroup(item.getId());
        groupList.remove(position);
        notifyItemRemoved(position);
    }

    public void editGroup(int position) {
        TodoGroupModel item = groupList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("group", item.getName());

        AddNewGroup group = new AddNewGroup();
        group.setArguments(bundle);
        group.show(activity.getSupportFragmentManager(), group.getTag());

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public Context getContext() {
        return activity;
    }


    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.todoGroupCardCheckBox);
        }
    }
}
