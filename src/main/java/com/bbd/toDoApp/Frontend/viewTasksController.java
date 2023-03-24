package com.bbd.toDoApp.Frontend;

import com.bbd.toDoApp.dbconnection.Connection;
import com.bbd.toDoApp.model.Category;
import com.bbd.toDoApp.model.Task;
import com.bbd.toDoApp.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import java.util.Optional;
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
    boolean debug = false;
    private static Connection connection;
    private static List<Category> userCategories;
    private static User user;
    private static Task rowSelected;
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
    @FXML
    private Text welcomeDisplay;
    @FXML
    private Text welcomeDate;
    @FXML
    private Text welcomeTasks;

    @FXML//This method is the equivalent of an onLoad method
    protected void initialize() throws MalformedURLException {
        if(debug)
        {
            user = new User(2,"Test");
        }
        else{
            user = StateObject.getLoggedInUser();

        }

        try {
            connection = new Connection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        makeButtonCircular();
        createColumnHeadings();
        initCategories();
        initTable();
        initWelcomeHeader();
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

        TableColumn<Task,CheckBox> completedColumn = new TableColumn<>("Completed");
        completedColumn.setCellValueFactory(new PropertyValueFactory<Task,CheckBox>("completed"));
        completedColumn.setCellValueFactory( new taskCompletedValueFactory());
        tasksTbl.getColumns().add(completedColumn);

        //Handle the double click of row
        tasksTbl.setRowFactory( tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    rowSelected = row.getItem();

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("editTask-view.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Stage stage = new Stage();
                    stage.setTitle("Edit Task");
                    stage.setScene(scene);
                    stage.show();
                }
            });
            return row ;
        });
    }

    private void initCategories() {
        userCategories = connection.retrieveCategoriesFor(user.getID());
        userCategories.forEach(category -> {
            try {
                addCategoryToView(category);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initTable(){
        List<Task> userTasks = connection.retrieveTasksFor(user.getID());
        for(Task task: userTasks)
        {
            if(task.getCategoryID() == 1)//Dealing with the default category
            {
                task.setCategory("");
            }else{
                task.setCategory(userCategories.stream().filter(c->c.getID() == task.getCategoryID()).findAny().get().getName());
            }
            tasksTbl.getItems().add(task);
        }
        tasksTbl.refresh();

    }

    private void initWelcomeHeader(){
        welcomeDisplay.setText("Hello, " + user.getUsername());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        welcomeDate.setText(formatter.format(calendar.getTime()));

        //retrieveDailyWelcomeTasks
        Optional<Integer> numTasks = connection.retrieveDailyWelcomeTasks(user.getID());
        if(numTasks.isEmpty()){
            welcomeTasks.setText("0");
        } else {
            welcomeTasks.setText(String.valueOf(numTasks.get()));
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

        Pattern regex = Pattern.compile("[^A-Za-z0-9\\s]");
        if(regex.matcher(newCat).find()){
            newCategoryTxf.clear();
            newCategoryTxf.setPromptText("No Special Characters Allowed");
            newCategoryTxf.setStyle("-fx-prompt-text-fill: red");
            return;
        }
        newCategoryTxf.clear();
        Category category = new Category(newCat,user.getID(),"");
        persistCategory(category);
        addCategoryToView(category);
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


    private void persistCategory(Category category)
    {
        connection.create(category);
    }
    private void addCategoryToView(Category cat) throws MalformedURLException {
        Button newCategoryBtn = new Button(cat.getName());
        //TODO fix this path
        File file = new File("./src/main/resources/StyleSheets/buttonCustomization.css");

        URL url = file.toURI().toURL();
        newCategoryBtn.getStylesheets().add(url.toExternalForm());
        newCategoryBtn.setId("rich-blue");
        newCategoryBtn.setMaxWidth(300);
        newCategoryBtn.setOnAction((e)->{
            tasksTbl.getItems().clear();
            connection.retrieveTasksFor(user.getID())
                      .stream()
                      .filter(task->task.getCategoryID() == cat.getID())
                      .forEach(task->{
                          task.setCategory(connection.retrieveCategory(task.getCategoryID()).get().getName());
                          tasksTbl.getItems().add(task);
                      });
        });
        catVBox.getChildren().add(newCategoryBtn);

    }

    public static Task getRowSelected(){return rowSelected;}

    private void logout() throws IOException {
        String path = System.getProperty("user.home") + "\\AppData\\Roaming\\ToDo\\user.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write("");
        writer.close();

        startApplication.setRoot("login-view.fxml");
    }

}
