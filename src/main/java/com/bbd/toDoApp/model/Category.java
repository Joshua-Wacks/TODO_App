package main.java.com.bbd.toDoApp.model;

/**
 * Models a category.
 * @author Khuthadzo Nemauluma
 * @version 1.0
 */
public class Category {
    private int ID;
    private String name;
    private int ownerID;
    private String description;

    public Category(String name, int ownerID, String description) {
        this.name = name;
        this.ownerID = ownerID;
        this.description = description;
    }
    public Category(int ID, String name, int ownerID, String description) {
        this(name, ownerID, description);
        this.ID = ID;
    }

    public int getID() { return ID;}
    public String getName() { return name;}
    public int getOwnerID() { return ownerID;}
    public String getDescription() { return description;}

    public void setName(String name) { this.name = name;}
    public void setDescription(String description){ this.description = description;}
}
