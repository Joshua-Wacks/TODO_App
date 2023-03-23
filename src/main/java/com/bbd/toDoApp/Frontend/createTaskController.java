package com.bbd.toDoApp.Frontend;

import com.bbd.toDoApp.dbconnection.Connection;
import com.bbd.toDoApp.model.Category;
import com.bbd.toDoApp.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.IntStream;

public class createTaskController {
    private static final Calendar calendar = Calendar.getInstance();
    private static Connection connection;
    private static int userID = 1;//TODO: should be getting this from context
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
        try {
            connection = new Connection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<String> categories = FXCollections.observableArrayList();
        List<Category> userCategories = connection.retrieveCategoriesFor(userID);
        userCategories.forEach(c -> categories.add(c.getName()));
        categoryChoiceBox.setValue(categories.get(0));
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
        //At this point we can assume that all the fields are fulled out correctly

        String desc,category;
        StringBuilder dueStr = new StringBuilder();
        Date created,due;

        desc = descTxa.getText();
        category = categoryChoiceBox.getValue();
        created = new Date();
        dueStr.append(dayChoiceBox.getValue());
        dueStr.append("/");
        Date dateConverter = new SimpleDateFormat("MMMM", Locale.ENGLISH).parse(monthChoiceBox.getValue());
        calendar.setTime(dateConverter);
        dueStr.append(calendar.get(Calendar.MONTH));
        dueStr.append("/");
        dueStr.append(yearChoiceBox.getValue());
        dueStr.append("/");
        due = new SimpleDateFormat("dd/MM/yyyy").parse(dueStr.toString());

        //TODO
        //Persist this new task to the DB
        //Re-fetch and populate the table

//        viewTasksController.addTaskToTable(task,desc,category,created,due);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("viewTasks-view.fxml"));
        Parent root = loader.load();
        //TODO persist to DB
//        viewTasksController viewTasksController = loader.getController();
//        viewTasksController.addTaskToTable(task,desc,category,created,due);
//        viewTasksController.addTaskToTable(task,"desc",category,"test","due");

        Stage stage = (Stage) addTaskBtn.getScene().getWindow();
        stage.close();

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
