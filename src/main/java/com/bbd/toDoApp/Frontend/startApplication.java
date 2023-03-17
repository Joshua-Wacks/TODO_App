package com.bbd.toDoApp.Frontend;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(startApplication.class.getResource("viewTasks-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ToDo App!");
        stage.setScene(scene);
        //This is how we fetch stuff before the screen is loaded
        stage.setOnShown((WindowEvent event) -> {
            TableView tasksTbl = (TableView) scene.lookup("#tasksTbl");
            //Setting Columns programmatically
//            TableColumn col = new TableColumn("Column1");
//            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(1).toString()));
//            col.getColumns().addAll(col);
        });
        stage.show();


// Create event handler


    }

    public static void main(String[] args) {
        launch();
    }
}