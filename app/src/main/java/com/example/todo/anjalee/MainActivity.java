package com.example.todo.anjalee;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Model.TodoGroupModel;
import com.example.todo.R;
import com.example.todo.Utils.DBHelper;
import com.example.todo.anjalee.Adapters.ToDoAdapter;
import com.example.todo.anjalee.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private String groupName;
    private int groupId;

    private static final String TAG = "anjalee Main activity";
    private DBHelper db;

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;

    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.anjalee_activity_main);

        db = new DBHelper(this);
        db.openDatabase();

        Bundle extras = getIntent().getExtras();
        groupName = extras.getString("GROUP_NAME");
        groupId = extras.getInt("GROUP_ID");

        // set label text
        setContentView(R.layout.anjalee_activity_main);
        TextView textView = findViewById(R.id.tasksText);
        textView.setText(groupName);

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = findViewById(R.id.fab);

        taskList = db.getAllTasks(groupId);
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }


    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks(groupId);
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}