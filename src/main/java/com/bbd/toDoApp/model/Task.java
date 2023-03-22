package com.bbd.toDoApp.model;

import java.sql.Timestamp;

/**
 * Models a task.
 * @author Khuthadzo Nemauluma
 * @version 1.0
 */
public class Task {
    private int ID;
    private int ownerID;
    private String title;
    private String description;
    private Timestamp creationDate;
    private Timestamp dueDate;
    private boolean completed;
    private int categoryID;

    public Task(int ownerID, String title, String description, Timestamp creationDate, Timestamp dueDate, int categoryID){
        this.ownerID = ownerID;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.categoryID = categoryID;
    }
    public Task(int ID, int ownerID, String title, String description, Timestamp creationDate, Timestamp dueDate, boolean completed, int categoryID) {
        this(ownerID, title, description, creationDate, dueDate, categoryID);
        this.completed = completed;
        this.ID = ID;
    }

    public int getID() { return ID;}
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
}
