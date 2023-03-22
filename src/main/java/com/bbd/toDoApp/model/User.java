package com.bbd.toDoApp.model;

/**
 * Models a user
 * @author Khuthadzo Nemauluma
 * @version 1.0
 */
public class User
{
    private int ID;
    private String username;

    public User(String username){
        this.username = username;
    }
    public User(int ID, String username) {
        this(username);
        this.ID = ID;
    }

    public int getID() { return ID;}
    public String getUsername() { return username;}
    public void setUsername(String username) { this.username = username;}
}