package com.bbd.toDoApp.Frontend;

import com.bbd.toDoApp.dbconnection.Connection;
import com.bbd.toDoApp.model.Task;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.sql.SQLException;

public class taskCompletedValueFactory implements Callback<TableColumn.CellDataFeatures<Task, CheckBox>, ObservableValue<CheckBox>> {
    @Override
    public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Task, CheckBox> param) {
        Task task = param.getValue();
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(task.isCompleted());
        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
            task.setCompleted(new_val);
            try {
                Connection connection = new Connection();
                connection.update(task);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        });
        return new SimpleObjectProperty<>(checkBox);
    }
}