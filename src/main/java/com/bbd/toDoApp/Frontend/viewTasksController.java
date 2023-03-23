package com.bbd.toDoApp.Frontend;

import com.bbd.toDoApp.Frontend.Objects.TodoItem;
import com.bbd.toDoApp.dbconnection.Connection;
import com.bbd.toDoApp.model.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
//TODO: When adding a new category or task, should check in db does not exist already
//TODO: add default categories for each user

public class viewTasksController {
    @FXML
    private Button newTaskBtn;
    @FXML
    private TableView<TodoItem> tasksTbl;
    @FXML
    private ScrollPane catScrollPane;
    @FXML
    private VBox catVBox;
    @FXML
    private TextField newCategoryTxf;
    @FXML
    private Button addCategoryBtn;

    @FXML//This method is the equivalent of an onLoad method
    protected void initialize() throws MalformedURLException {

        makeButtonCircular();
        createColumnHeadings();
        initCategories();
        initTable();
    }
    private void makeButtonCircular() {
        double r=20;
        newTaskBtn.setShape(new Circle(r));
        newTaskBtn.setMinSize(2*r, 2*r);
        newTaskBtn.setMaxSize(2*r, 2*r);
    }
    private void createColumnHeadings() {
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
    }

    private void initCategories() throws MalformedURLException {
        //TODO: do fetch here
        addCategoryToView("Tasks");
        addCategoryToView("Important");
        addCategoryToView("Planned");

    }

    private void initTable(){
        try {
            Connection connection = new Connection();
            List<Task>  userTasks = connection.retrieveTasksFor("TEST");
            for(Task task: userTasks)
            {
                addTaskToTable(task.getTitle(),task.getDescription(),task.getDescription(),task.getCreationDate(),task.getDueDate());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private String toDate(Timestamp timestamp) {
        return timestamp.toString().split(" ")[0];
    }
    public void addTaskToTable(String task, String desc, String category, Timestamp created, Timestamp due)
    {
        System.out.println(task+desc+category+created+due+"No");
        TodoItem todoItem = new TodoItem(task,desc,category,toDate(created),toDate(due),"No");
        tasksTbl.getItems().add(todoItem);
        tasksTbl.refresh();
    }

    public void getCategoryToAdd() throws MalformedURLException {
        String newCat = newCategoryTxf.getText();
        if(newCat.equals(""))
        {
            newCategoryTxf.setPromptText("Enter a Category First");
            newCategoryTxf.setStyle("-fx-prompt-text-fill: red");
            return;
        }
        //TODO check that cat does not already exist

        addCategoryToView(newCat);
    }

    private void handleCategoryButtonThemes(Button btn)
    {
        if(btn.getId().equals("rich-blue"))
        {
            for(Node n:  catVBox.getChildren()){
                n.setId("rich-blue");
            }
            btn.setId("dark-blue");
        }else{
            btn.setId("rich-blue");
        }
    }

    private void addCategoryToView(String cat) throws MalformedURLException {
        Button newCategoryBtn = new Button(cat);
        //TODO fix this path
        File file = new File("./src/main/resources/StyleSheets/buttonCustomization.css");

        URL url = file.toURI().toURL();
        newCategoryBtn.getStylesheets().add(url.toExternalForm());
        newCategoryBtn.setId("rich-blue");
        newCategoryBtn.setMaxWidth(300);
        newCategoryBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleCategoryButtonThemes(newCategoryBtn);
            }
        });
        catVBox.getChildren().add(newCategoryBtn);
//        catScrollPane.setContent(catScrollPane.getContent()+ newCategory);
    }

}
