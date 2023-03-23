package com.bbd.toDoApp.Frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class startApplication extends Application {
//    @Override
//    public void startOld(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(startApplication.class.getResource("login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("ToDo App!");
//        stage.setScene(scene);
//        stage.show();
//    }
    private static Scene scene;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(startApplication.class.getResource("viewTasks-view.fxml"));
        scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("ToDo App!");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        //This is how we fetch stuff before the screen is loaded
//        primaryStage.setOnShown((WindowEvent event) -> {
//
//        });
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());

    }

    static void setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(startApplication.class.getResource(fxml));
        scene.setRoot(fxmlLoader.load());
    }


    public static void main(String[] args) {

        launch();
    }
}