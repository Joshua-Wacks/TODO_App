package com.bbd.toDoApp.Frontend;

import com.bbd.toDoApp.Frontend.Objects.TodoItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

//TODO: Add icons to fields
//TODO: Group tasks by their categories
//TODO: Add an option to add images
//TODO: Add an option to add notes
//TODO: Add an option to add a reminder
//TODO: Add an option to add someone else to a task, through the use of a request
//TODO: Add an option to search through tasks/added tasks from someone else
//TODO: be able to edit a task
//TODO: Fetch user info and populate top pane
//TODO: Fetch user created categories and populate left pane

//TODO: when window closes, should close program

public class viewTasksController {
    @FXML
    private Button newTaskBtn;

    @FXML
    private TableView<TodoItem> tasksTbl;

    @FXML//This method is the equivalent of an onLoad method
    protected void initialize() throws MalformedURLException {
        double r=20;
        newTaskBtn.setShape(new Circle(r));
        newTaskBtn.setMinSize(2*r, 2*r);
        newTaskBtn.setMaxSize(2*r, 2*r);

        File file = new File("column.css");
        URL url = file.toURI().toURL();
        System.out.println(url);

//        TableColumn<TodoItem,String> categoryColumn = new TableColumn<>("Category");
        TableColumn<TodoItem,String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<TodoItem,String>("category"));
        tasksTbl.getColumns().add(categoryColumn);

        TableColumn<TodoItem,String> taskColumn = new TableColumn<>("Task");
        taskColumn.setCellValueFactory(new PropertyValueFactory<TodoItem,String>("task"));
        tasksTbl.getColumns().add(taskColumn);

        TableColumn<TodoItem,String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<TodoItem,String>("description"));
        tasksTbl.getColumns().add(descriptionColumn);

        TableColumn<TodoItem,String> createdColumn = new TableColumn<>("Created");
        createdColumn.setCellValueFactory(new PropertyValueFactory<TodoItem,String>("created"));
        tasksTbl.getColumns().add(createdColumn);

        TableColumn<TodoItem,String> dueColumn = new TableColumn<>("Due");
        dueColumn.setCellValueFactory(new PropertyValueFactory<TodoItem,String>("due"));
        tasksTbl.getColumns().add(dueColumn);

        TableColumn<TodoItem,String> completedColumn = new TableColumn<>("Completed");
        completedColumn.setCellValueFactory(new PropertyValueFactory<TodoItem,String>("completed"));
        tasksTbl.getColumns().add(completedColumn);

        addTaskToTable("Task","Desc","Cat","Created","Due");

    }

    public void showCreateTaskScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("createTask-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Create Task");
        stage.setScene(scene);
        stage.show();

    }

    public void addTaskToTable(String task, String desc, String category, String created, String due)
    {
        System.out.println("I RUN");
        TodoItem todoItem = new TodoItem(task,desc,category,created,due,"No");
        tasksTbl.getItems().add(todoItem);
    }

}
