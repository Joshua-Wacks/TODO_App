package com.bbd.toDoApp.Frontend;

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
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.IntStream;

public class createTaskController {
    private static final Calendar calendar = Calendar.getInstance();
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
        //TODO fetch the default and user defined categories here
        ObservableList<String> defaultCategories = FXCollections.observableArrayList();
        defaultCategories.addAll("Tasks","Important","Planned");
        categoryChoiceBox.setValue("Tasks");
        categoryChoiceBox.setItems(defaultCategories);

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
        //TODO Fix this, should not be fetching again
        //TODO do logic like in viewTaskController that is cleaner
        String task = taskTxf.getText();
        if(!testInputFields(taskTxf,task))
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
    private Boolean testInputFields(TextField taskTxf,String task)
    {
        if(task.equals("")){
            taskTxf.setPromptText("ENTER A TASK");
            return false;
        }
        return true;
    }


}
