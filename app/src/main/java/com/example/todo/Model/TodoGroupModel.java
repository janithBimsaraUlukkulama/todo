package com.example.todo.Model;

public class TodoGroupModel {
    private String name;
    private int id, status;

    public TodoGroupModel(int i, String group1, int i1) {
        this.id= i;
        this.status= i1;
        this.name = group1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
