module com.example.firstjavafxproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.bbd.toDoApp.Frontend to javafx.fxml;
    opens com.bbd.toDoApp.Frontend.Objects to javafx.fxml;

    exports com.bbd.toDoApp.Frontend;
    exports com.bbd.toDoApp.Frontend.Objects;

}