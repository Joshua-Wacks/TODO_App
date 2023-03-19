package com.bbd.toDoApp.Frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class viewTasksController {
    public void showCreateTaskScene() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(startApplication.class.getResource("createTask-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        Stage stage = new Stage();
//        stage.setTitle("ToDo App!");
//        stage.setResizable(false);
//        stage.setScene(scene);


        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("createTask-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("ToDo App!");
        stage.setScene(scene);
        stage.show();

    }

}
