package com.bbd.toDoApp.model;

import java.sql.Timestamp;

/**
 * Models a task.
 * @author Khuthadzo Nemauluma
 * @version 1.0
 */
public class Task {
    private int taskID;
    private int ownerID;
    private String title;
    private String description;
    private Timestamp creationDate;
    private String creationDateStr;
    private Timestamp dueDate;
    private String dueDateStr;
    private boolean completed;
    private int categoryID;
    private String category;

    public Task(int ownerID, String title, String description, Timestamp creationDate, Timestamp dueDate, int categoryID){
        this.ownerID = ownerID;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.categoryID = categoryID;
        this.creationDateStr = creationDate.toString().split(" ")[0];
        this.dueDateStr = dueDate.toString().split(" ")[0];
    }
    public Task(int taskID, int ownerID, String title, String description, Timestamp creationDate, Timestamp dueDate, boolean completed, int categoryID) {
        this(ownerID, title, description, creationDate, dueDate, categoryID);
        this.completed = completed;
        this.taskID = taskID;
    }

    public Task(int taskID, int ownerID, String title, String description, Timestamp creationDate, Timestamp dueDate, boolean completed,int categoryID, String category) {
        this(ownerID, title, description, creationDate, dueDate, categoryID);
        this.completed = completed;
        this.taskID = taskID;
        this.category = category;
    }

    public int getID() { return taskID;}
    public int getOwnerID() { return ownerID;}
    public String getTitle() { return title;}
    public Timestamp getCreationDate() { return creationDate;}
    public Timestamp getDueDate() { return dueDate;}
    public String getDescription() { return description;}
    public boolean isCompleted() { return completed;}
    public int getCategoryID() { return categoryID;}
    public void markAsCompleted() { completed = true;}
    public void setTitle(String title){ this.title = title;}
    public void setDueDate(Timestamp date) { dueDate = date;}
    public void setDescription(String description) { this.description = description;}
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreationDateStr() {
        return creationDateStr;
    }

    public void setCreationDateStr(String creationDateStr) {
        this.creationDateStr = creationDateStr;
    }
    public String getDueDateStr() {
        return dueDateStr;
    }

    public void setDueDateStr(String dueDateStr) {
        this.dueDateStr = dueDateStr;
    }

}
