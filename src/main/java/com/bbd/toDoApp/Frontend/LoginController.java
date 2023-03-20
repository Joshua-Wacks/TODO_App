package com.bbd.toDoApp.Frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameTxf;
    @FXML
    private PasswordField passwordTxf;
    @FXML
    private Text messageDisplay;

    @FXML
    protected void onLoginButtonClick() throws IOException {
        //TODO: All the logic for logging a user in
        String tempUser = "User";
        String tempPass = "Password";

        if(usernameTxf.getText().equals(tempUser)){
            if(passwordTxf.getText().equals(tempPass)){
                startApplication.setRoot("viewTasks-view.fxml");
            } else {
                messageDisplay.setText("Incorrect Password");
            }
        } else {
            messageDisplay.setText("Incorrect Username");
        }
    }

    @FXML
    protected void switchToSignUp() throws IOException {
        //TODO: All the logic for logging a user in
        System.out.println("[INFO] Switching to sign up Screen...");
        startApplication.setRoot("signup-view.fxml");
    }
}