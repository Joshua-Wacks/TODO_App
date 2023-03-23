package com.bbd.toDoApp.Frontend;

import com.bbd.toDoApp.dbconnection.Connection;
import com.bbd.toDoApp.model.Category;
import com.bbd.toDoApp.model.Task;
import com.bbd.toDoApp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.IntStream;

//TODO: check for special characters

public class createTaskController {
    boolean debug = true;
    private static Connection connection;
    private static User user;
    private static List<Category> userCategories;
    @FXML
    private Button addTaskBtn;
    @FXML
    private TextField taskTxf;
    @FXML
    private TextArea descTxa;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    private ChoiceBox<String> dayChoiceBox;
    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;

    @FXML//This method is the equivalent of an onLoad method
    protected void initialize() {
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
        Calendar calendar = Calendar.getInstance();

        ObservableList<String> categories = FXCollections.observableArrayList();
        userCategories = connection.retrieveCategoriesFor(user.getID());
        userCategories.forEach(c -> categories.add(c.getName()));
        categoryChoiceBox.setValue("Default");
        categoryChoiceBox.setItems(categories);

        int month = calendar.get(Calendar.MONTH) +1;
        int year = calendar.get(Calendar.YEAR);
        YearMonth yearMonthObject = YearMonth.of(year, month );
        int daysInMonth = yearMonthObject.lengthOfMonth();
        dayChoiceBox.setValue(calendar.get(Calendar.DAY_OF_MONTH) +"");
        IntStream.rangeClosed(1,daysInMonth).boxed().forEach(x -> dayChoiceBox.getItems().add(x+""));

        DateFormatSymbols dfs = new DateFormatSymbols();
        String monthString = dfs.getMonths()[month-1];
        monthChoiceBox.setValue(monthString);
        String[] monthNames = dfs.getMonths();
        //Have to get all but last element here, for some reason 13 elements
        Arrays.stream(Arrays.copyOfRange(monthNames, 0, 12)).forEach(x -> monthChoiceBox.getItems().add(x));

        yearChoiceBox.setValue(year +"");
        IntStream.rangeClosed(year,year+8).boxed().forEach(x -> yearChoiceBox.getItems().add(x+""));

    }

    public void createTask() throws ParseException, IOException {
        //TODO may need to put more checks in a separate method
        String title = taskTxf.getText();
        if(!testInputFields(title))//It does not pass some check
        {
            return;
        }
        //All fields valid here

        Calendar calendar = Calendar.getInstance();
        String desc,category;
        StringBuilder dueStr;
        Timestamp created,due;
        int catId;

        desc = descTxa.getText();
        category = categoryChoiceBox.getValue();
        created = new Timestamp(System.currentTimeMillis());

        //Making the dueDate timestamp
        dueStr = new StringBuilder(yearChoiceBox.getValue() + "-");
        Date dateConverter = new SimpleDateFormat("MMMM", Locale.ENGLISH).parse(monthChoiceBox.getValue());
        calendar.setTime(dateConverter);
        dueStr.append(calendar.get(Calendar.MONTH));
        dueStr.append("-").append(dayChoiceBox.getValue());
        dueStr.append(" 00:00:00");
        due = Timestamp.valueOf(dueStr.toString());

        if(category.equals("Default"))
        {
            catId = 1;
        }
        else {
            catId = userCategories.stream().filter(c->c.getName().equals(category)).findAny().get().getID();
        }

        Task task = new Task(user.getID(), title,desc,created,due,catId);
        connection.create(task);//persist

        Stage stage = (Stage) addTaskBtn.getScene().getWindow();
        stage.close();
        startApplication.setRoot("viewTasks-view.fxml");//refresh

    }
    private Boolean testInputFields(String task)
    {
        if(task.equals("")){
            taskTxf.setPromptText("ENTER A TASK");
            return false;
        }

        List<Task> userTasks = connection.retrieveTasksFor("TEST");
        Optional<Task> taskExists = userTasks.stream().filter(t -> t.getTitle().equals(task)).findFirst();
        if(taskExists.isPresent()){
            taskTxf.clear();
            taskTxf.setPromptText("TASK NAME ALREADY EXISTS");
            return false;
        }

        return true;
    }


}
