package com.bbd.toDoApp.Frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class startApplication extends Application {
//    @Override
//    public void startOld(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(startApplication.class.getResource("login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("ToDo App!");
//        stage.setScene(scene);
//        stage.show();
//    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(startApplication.class.getResource("viewTasks-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("ToDo App!");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        //This is how we fetch stuff before the screen is loaded
//        primaryStage.setOnShown((WindowEvent event) -> {
//
//
//        });
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());

    }

    public static void main(String[] args) {
        launch();
    }
}