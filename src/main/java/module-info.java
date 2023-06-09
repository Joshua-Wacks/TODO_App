module com.bbd.toDoApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.bbd.toDoApp.Frontend to javafx.fxml;
    opens com.bbd.toDoApp.model to javafx.fxml;

    exports com.bbd.toDoApp.Frontend;
    exports com.bbd.toDoApp.model;


}