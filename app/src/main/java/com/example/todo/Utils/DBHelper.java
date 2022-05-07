package com.example.todo.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todo.Model.TodoGroupModel;

import java.util.ArrayList;
import java.util.List;

import anjalee.Model.ToDoModel;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    private static final String DB_NAME = "todo_db";
    private static final String GROUP_TABLE_NAME = "group_table";
    private static final String GROUP_COL_1 = "id";
    private static final String GROUP_COL_2 = "name";
    private static final String GROUP_COL_3 = "status";

    //    anjalee --------------
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";
    //    anjalee --------------


    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + GROUP_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT , status INTEGER)");

        //    anjalee --------------
        sqLiteDatabase.execSQL(CREATE_TODO_TABLE);
        //    anjalee --------------

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE_NAME);

        //    anjalee --------------
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        //    anjalee --------------

        onCreate(sqLiteDatabase);
    }

//    public void openDatabase() {
//        db = this.getWritableDatabase();
//    }

    public void insertGroup(TodoGroupModel group) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GROUP_COL_2, group.getName());
        values.put(GROUP_COL_3, 0);
        db.insert(GROUP_TABLE_NAME, null, values);
    }

    public void updateGroupName(int id, String name) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GROUP_COL_2, name);
        db.update(GROUP_TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateGroupStatus(int id, int status) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GROUP_COL_3, status);
        db.update(GROUP_TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteGroup(int id) {
        db = this.getWritableDatabase();
        db.delete(GROUP_TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
    }

    @SuppressLint("Range")
    public List<TodoGroupModel> getAllGroups() {

        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<TodoGroupModel> groupList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(GROUP_TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        TodoGroupModel group = new TodoGroupModel();
                        group.setId(cursor.getInt(cursor.getColumnIndex(GROUP_COL_1)));
                        group.setName(cursor.getString(cursor.getColumnIndex(GROUP_COL_2)));
                        group.setStatus(cursor.getInt(cursor.getColumnIndex(GROUP_COL_3)));
                        groupList.add(group);

                    } while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return groupList;
    }

    //    anjalee --------------
    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

    //    anjalee --------------




}
