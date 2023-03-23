package com.bbd.toDoApp.dbconnection;

import java.io.Closeable;
import java.io.IOException;

import java.sql.Timestamp;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bbd.toDoApp.model.Task;
import com.bbd.toDoApp.model.User;
import com.bbd.toDoApp.model.Category;
import javafx.util.Pair;

/**
 * Connecting to the database.
 * @author Khuthadzo Nemauluma
 * @version 1.0
 */
public class Connection implements Closeable {
    private final java.sql.Connection connection;
    private static final String SCHEMA = "todo_app_db";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + SCHEMA;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";
    private static final String USER_TABLE = "users";
    private static final String CATEGORY_TABLE = "categories";
    private static final String TASK_TABLE = "tasks";

    /**
     * Creates a connection to the database.
     * An object of this class can be used to retrieve and persist records in the database.
     * @throws SQLException
     */
    public Connection() throws SQLException { connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);}

    ////////////////////////////////////////
    // Creation of records in the database//
    ////////////////////////////////////////

    /**
     * Persists a new user to the database
     * @param user the new user
     * @param password the password for the new user
     * @return true if the user was created, and false otherwise
     */
    public boolean create(User user, String password, String salt){
        try(Statement statement = connection.createStatement()) {
            StringBuilder query = new StringBuilder();
            query.append(String.format("INSERT INTO %s.%s (Username, User_password,User_salt)\n", SCHEMA, USER_TABLE));
            query.append(String.format("VALUES (\"%s\", \"%s\", \"%s\")", user.getUsername(), password,salt));
            statement.execute(query.toString());
            return true;
        } catch (SQLException e) {
            System.out.println(e);

            return false;
        }
    }

    /**
     * Persist a new task record.
     * @param task the new task record
     * @return true if the record was persisted, and false otherwise
     */
    public boolean create(Task task){
        try(Statement statement = connection.createStatement()) {
            StringBuilder query = new StringBuilder();
            query.append(String.format("INSERT INTO %s.%s (User_ID, Title, Description, Date_created, Date_due, Completed, Category_ID)\n", SCHEMA, TASK_TABLE));
            query.append(String.format("VALUES (%d, \"%s\", \"%s\", \"%s\", \"%s\", 0, %d)",
                    task.getOwnerID(), task.getTitle(), task.getDescription(), task.getCreationDate(), task.getDueDate(),task.getCategoryID()));
            statement.execute(query.toString());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Persists a new category record.
     * @param category the new category
     * @return true if the category is persisted, and false otherwise
     */
    public boolean create(Category category) {
        try(Statement statement = connection.createStatement()) {
            StringBuilder query = new StringBuilder();
            query.append(String.format("INSERT INTO %s.%s (Category_name, User_ID) ", SCHEMA, CATEGORY_TABLE));
            query.append(String.format("VALUES (\"%s\", %d)", category.getName(), category.getOwnerID()));
            statement.execute(query.toString());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    ////////////////////////////////////////
    // Updating of records in the database//
    ////////////////////////////////////////

    /**
     * Updates a user record.
     * The user must have a User_ID matching a record in a table.
     *
     * @param user the user to be updated
     * @return true if success, and false otherwise
     */
    public boolean update(User user){
        try(Statement statement = connection.createStatement()){
            StringBuilder query = new StringBuilder();
            query.append(String.format("UPDATE %s.%s\n", SCHEMA, USER_TABLE));
            query.append(String.format("SET %s.Username = \"%s\"\n", USER_TABLE, user.getUsername()));
            query.append(String.format("WHERE User_ID = %d", user.getID()));
            statement.execute(query.toString());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    /**
     * Updates a task record.
     * The task must have a Task_ID matching a record in a table.
     *
     * @param task the task to be updated
     * @return true if success, and false otherwise
     */
    public boolean update(Task task){
        try(Statement statement = connection.createStatement()){
            StringBuilder query = new StringBuilder();
            query.append(String.format("UPDATE %s.%s\n", SCHEMA, TASK_TABLE));
            query.append(String.format("SET %s.title = \"%s\", %s.Description = \"%s\", %s.Date_due = \"%s\", %s.Completed = %d, %s.Category_ID = %d \n",
                    TASK_TABLE, task.getTitle(), TASK_TABLE, task.getDescription(), TASK_TABLE, task.getDueDate(),
                    TASK_TABLE, task.isCompleted() ? 1 : 0, TASK_TABLE, task.getCategoryID()));
            query.append(String.format("WHERE Task_ID = %d", task.getID()));
            statement.execute(query.toString());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    /**
     * Updates a category record.
     * The user must have a Category_ID matching a record in a table.
     *
//     * @param category the user to be updated
     * @return true if success, and false otherwise
     */
//    public boolean update(Category category){
//        try(Statement statement = connection.createStatement()){
//            StringBuilder query = new StringBuilder();
//            query.append(String.format("UPDATE %s.%s\n", SCHEMA, CATEGORY_TABLE));
//            query.append(String.format("SET %s.Category_name = \"%s\", %s.Description = \"%s\"\n", USER_TABLE, category.getName(), USER_TABLE, category.getDescription()));
//            query.append(String.format("WHERE User_ID = %d", category.getID()));
//            statement.execute(query.toString());
//            return true;
//        } catch (SQLException e) {
//            return false;
//        }
//    }


    //////////////////////////////////////////
    // Deletion of records from the database//
    //////////////////////////////////////////
    public void removeTask(Task task){}
    public void removeCategory(Category category){}

    ////////////////////////////////////////////
    // Retrieval of records from the database///
    ////////////////////////////////////////////
    public Optional<User> retrieveUser(int userID){
        try(Statement statement = connection.createStatement()){
            String query = String.format("SELECT * FROM %s.%s WHERE %s.User_ID = %d", SCHEMA, USER_TABLE, USER_TABLE, userID);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next())
                return Optional.of(getUser(resultSet));
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }

    }
    public Optional<User> retrieveUser(String username){
        try(Statement statement = connection.createStatement()){
            String query = String.format("SELECT * FROM %s.%s WHERE %s.Username = \"%s\"", SCHEMA, USER_TABLE, USER_TABLE, username);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next())
                return Optional.of(getUser(resultSet));
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }

    }
    public Optional<Pair<String,String>> retrieveUserPass(String username){
        try(Statement statement = connection.createStatement()){
            String query = String.format("SELECT User_password,User_salt FROM %s.%s WHERE %s.Username = \"%s\"", SCHEMA, USER_TABLE, USER_TABLE, username);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next())
            {
                Pair<String,String> pair = new Pair<>(resultSet.getString("User_password"),resultSet.getString("User_salt"));
                return Optional.of(pair);

            }
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    public Optional<Integer> retrieveDailyWelcomeTasks(int userID){
        try(Statement statement = connection.createStatement()){
            String query = String.format("SELECT COUNT(*) AS Tasks FROM %s.%s WHERE %s.User_ID = %d and %s.Completed = False", SCHEMA, TASK_TABLE, TASK_TABLE, userID, TASK_TABLE);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next())
                return Optional.of(resultSet.getInt("Tasks"));
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }

    }
    public Optional<Category> retrieveCategory(int categoryID){
        try(Statement statement = connection.createStatement()){
            String query = String.format("SELECT * FROM %s.%s WHERE %s.Category_ID = %d", SCHEMA, CATEGORY_TABLE, CATEGORY_TABLE, categoryID);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next())
                return Optional.of(getCategory(resultSet));
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    public Optional<Task> retrieveTask(int taskID){
        try(Statement statement = connection.createStatement()){
            String query = String.format("SELECT * FROM %s.%s WHERE %s.Task_ID = %d", SCHEMA, TASK_TABLE, TASK_TABLE, taskID);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next())
                return Optional.of(getTask(resultSet));
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    public List<Category> retrieveCategoriesFor(int userID) {
        List<Category> categories = new ArrayList<>();
        try(Statement statement = connection.createStatement()){
            String query = String.format("SELECT * FROM %s.%s WHERE %s.User_ID = %d", SCHEMA, CATEGORY_TABLE, CATEGORY_TABLE, userID);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
                categories.add(getCategory(resultSet));
            return categories;
        } catch (SQLException e) {
            return categories;
        }
    }
    public List<Task> retrieveTasksFor(String username){
        List<Task> tasks = new ArrayList<>();
        try(Statement statement = connection.createStatement()){
            String query = String.format("SELECT * FROM %s.%s INNER JOIN %s.%s WHERE %s.Username = \"%s\"", SCHEMA, TASK_TABLE,SCHEMA, USER_TABLE, USER_TABLE, username);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
                tasks.add(getTask(resultSet));
            return tasks;
        } catch (SQLException e) {
            return tasks;
        }
    }

    public List<Task> retrieveTasksFor(int userID){
        List<Task> tasks = new ArrayList<>();
        try(Statement statement = connection.createStatement()){
            String query = String.format("SELECT * FROM %s.%s WHERE %s.User_ID = %d", SCHEMA, TASK_TABLE, TASK_TABLE, userID);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
                tasks.add(getTask(resultSet));
            return tasks;
        } catch (SQLException e) {
            return tasks;
        }
    }

    public boolean deleteTask(Task task)
    {
        try(Statement statement = connection.createStatement()){
            String query = String.format("DELETE FROM %s.%s WHERE %s.User_ID = %d AND %s.TASK_ID = %d", SCHEMA, TASK_TABLE, TASK_TABLE, task.getOwnerID(),TASK_TABLE, task.getID());
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    /**
     * Creates a User instance from a ResultSet
     * @param resultSet ResultSet object with the information about a user
     * @return a user
     */
    private static User getUser(ResultSet resultSet){
        try {
            int userID = resultSet.getInt("User_ID");
            String username = resultSet.getString("Username");
            return new User(userID, username);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Creates a Task instance from a ResultSet
     * @param resultSet ResultSet object with the information about a task
     * @return a task
     */
    private static Task getTask(ResultSet resultSet){
        try{
            int taskID = resultSet.getInt("Task_ID");
            int userID = resultSet.getInt("User_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            Timestamp creationDate = resultSet.getTimestamp("Date_created");
            Timestamp dueDate = resultSet.getTimestamp("Date_due");
            boolean completed = resultSet.getInt("completed") == 1;
            int categoryID = resultSet.getInt("Category_ID");
            return new Task(taskID, userID, title, description, creationDate, dueDate, completed, categoryID);
        } catch(SQLException e) {
            return null;
        }
    }

    /**
     * Creates a Category instance from a ResultSet
     * @param resultSet ResultSet object with the information about a category
     * @return a category
     */
    private static Category getCategory(ResultSet resultSet){
        try {
            int categoryID = resultSet.getInt("Category_ID");
            String name = resultSet.getString("Category_name");
            int userID = resultSet.getInt("User_ID");
            return new Category(categoryID, name, userID);
        } catch (SQLException e) {
            return null;
        }
    }



    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}