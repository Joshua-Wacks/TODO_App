package com.bbd.toDoApp.Frontend.Objects;

//import java.util.String;

public class TodoItem {
    private String task;
    private String description;
    private String category;
    private String created;
    private String due;
    private String completed;

    public TodoItem(String task, String description, String category, String created, String due, String completed) {
        this.task = task;
        this.description = description;
        this.category = category;
        this.created = created;
        this.due = due;
        this.completed = completed;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }
}
