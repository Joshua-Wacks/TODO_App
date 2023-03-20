package com.bbd.toDoApp.Frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class SignupController {

    @FXML
    protected void onSignUpButtonClick() {
        //TODO: All the logic for logging a user in
    }

    @FXML
    protected void switchToLogin() throws IOException {
        //TODO: All the logic for logging a user in
        System.out.println("[INFO] Switching to login Screen...");
        startApplication.setRoot("login-view.fxml");
    }

}
