package com.bbd.toDoApp.model;

/**
 * Models a category.
 * @author Khuthadzo Nemauluma
 * @version 1.0
 */
public class Category {
    private int ID;
    private String name;
    private int ownerID;

    public Category(String name, int ownerID) {
        this.name = name;
        this.ownerID = ownerID;
    }
    public Category(int ID, String name, int ownerID) {
        this(name, ownerID);
        this.ID = ID;
    }

    public int getID() { return ID;}
    public String getName() { return name;}
    public int getOwnerID() { return ownerID;}

    public void setName(String name) { this.name = name;}
}
