package com.bbd.toDoApp.Frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.text.DateFormatSymbols;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.IntStream;

public class createTaskController {
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

        Calendar calendar = Calendar.getInstance();
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
        IntStream.rangeClosed(year,year+100).boxed().forEach(x -> yearChoiceBox.getItems().add(x+""));


    }

    public void createTask()
    {
        //TODO may need to put more checks in a separate method

        Scene scene = (Scene) addTaskBtn.getScene();
        taskTxf = (TextField) scene.lookup("#taskTxf");
        String task = taskTxf.getText();
        if(!testInputFields(taskTxf,task))
        {
            return;
        }
        //At this point we can assume that all the fields are fulled out correctly

        descTxa = (TextArea) scene.lookup("#descTxa");
        //Get all info filled out, persist to db and repopulate



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
