package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.example.todo.Adapter.GroupAdapter;
import com.example.todo.Model.TodoGroupModel;
import com.example.todo.Utils.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private DBHelper dbHelper;
    private List<TodoGroupModel> groupList;
    private GroupAdapter groupAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.todoGroupRecycleView);
        fab = findViewById(R.id.fab);
        dbHelper = new DBHelper(MainActivity.this);
        groupList = new ArrayList<>();
        groupAdapter = new GroupAdapter(dbHelper, MainActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(groupAdapter);

        groupList = dbHelper.getAllGroups();
        Collections.reverse(groupList);
        groupAdapter.setGroup(groupList);

        fab.setOnClickListener((view) -> {
            AddNewGroup.newInstance().show(getSupportFragmentManager(), AddNewGroup.TAG);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new GroupTouchHelper(groupAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        groupList = dbHelper.getAllGroups();
        Collections.reverse(groupList);
        groupAdapter.setGroup(groupList);
        groupAdapter.notifyDataSetChanged();
    }

}