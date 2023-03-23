package com.bbd.toDoApp.Frontend;

import com.bbd.toDoApp.dbconnection.Connection;
import com.bbd.toDoApp.model.Category;
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
import java.util.regex.Pattern;

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
    private static Connection connection;
    private static List<Category> userCategories;
    private static int userID = 1;//TODO: should be getting this from context
    @FXML
    private Button newTaskBtn;
    @FXML
    private TableView<Task> tasksTbl;
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

        try {
            connection = new Connection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        TableColumn<Task,String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("category"));
        tasksTbl.getColumns().add(categoryColumn);

        TableColumn<Task,String> taskColumn = new TableColumn<>("Title");
        taskColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("title"));
        tasksTbl.getColumns().add(taskColumn);

        TableColumn<Task,String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("description"));
        tasksTbl.getColumns().add(descriptionColumn);

        TableColumn<Task,String> createdColumn = new TableColumn<>("Created");
        createdColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("creationDateStr"));
        tasksTbl.getColumns().add(createdColumn);

        TableColumn<Task,String> dueColumn = new TableColumn<>("Due");
        dueColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("dueDateStr"));
        tasksTbl.getColumns().add(dueColumn);

        TableColumn<Task,Boolean> completedColumn = new TableColumn<>("Completed");
        completedColumn.setCellValueFactory(new PropertyValueFactory<Task,Boolean>("completed"));
        tasksTbl.getColumns().add(completedColumn);
    }

    private void initCategories() {
        userCategories = connection.retrieveCategoriesFor(userID);
        userCategories.forEach(c -> {
            try {
                addCategoryToView(c.getName());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initTable(){
        List<Task> userTasks = connection.retrieveTasksFor(userID);
        for(Task task: userTasks)
        {
            task.setCategory(userCategories.stream().filter(c->c.getID() == task.getCategoryID()).findAny().get().getName());
            tasksTbl.getItems().add(task);
        }
        tasksTbl.refresh();

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

    public void getCategoryToAdd() throws MalformedURLException {
        String newCat = newCategoryTxf.getText();
        if(newCat.equals(""))
        {
            newCategoryTxf.setPromptText("Enter a Category First");
            newCategoryTxf.setStyle("-fx-prompt-text-fill: red");
            return;
        }

        if(userCategories.stream().anyMatch(c->c.getName().equals(newCat))){
            newCategoryTxf.clear();
            newCategoryTxf.setPromptText("This category already exists");
            newCategoryTxf.setStyle("-fx-prompt-text-fill: red");
            return;
        }

        Pattern regex = Pattern.compile("[^A-Za-z0-9]");
        if(regex.matcher(newCat).find()){
            newCategoryTxf.clear();
            newCategoryTxf.setPromptText("No Special Characters Allowed");
            newCategoryTxf.setStyle("-fx-prompt-text-fill: red");
            return;
        }
        newCategoryTxf.clear();

        persistCategory(newCat);
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

    private void persistCategory(String newCat)
    {
        connection.create(new Category(newCat,userID,""));
    }
    private void addCategoryToView(String cat) throws MalformedURLException {
        Button newCategoryBtn = new Button(cat);
        //TODO fix this path
        File file = new File("C:/Users/bbdnet2493/Documents/grad_projects/Level_ups/Java_level_up/To_do_app_Project/ToDoApp/src/main/resources/StyleSheets/buttonCustomization.css");
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
