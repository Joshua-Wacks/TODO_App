package com.bbd.toDoApp.Frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//TODO: Add icons to fields
//TODO: Group tasks by their categories
//TODO: Add an option to add images
//TODO: Add an option to add notes
//TODO: Add an option to add a reminder
//TODO: Add an option to add someone else to a task, through the use of a request
//TODO: Add an option to search through tasks/added tasks from someone else
//TODO: be able to edit a task

//TODO: when window closes, should close program

public class viewTasksController {
    public void showCreateTaskScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("createTask-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Create Task");
        stage.setScene(scene);
        stage.show();

    }

}
