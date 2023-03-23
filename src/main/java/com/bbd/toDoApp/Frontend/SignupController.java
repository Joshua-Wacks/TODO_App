package com.bbd.toDoApp.Frontend;

import com.bbd.toDoApp.dbconnection.Connection;
import com.bbd.toDoApp.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignupController {

    @FXML
    private TextField usernameTxf;
    @FXML
    private PasswordField passwordTxf;
    @FXML
    private PasswordField passwordTxf1;
    @FXML
    private Text messageDisplay;

    @FXML
    protected void onSignUpButtonClick() throws SQLException {
        //TODO: All the logic for logging a user in
        if(Objects.equals(passwordTxf.getText(), passwordTxf1.getText())){
            try {
                Connection conn = new Connection();
                Optional<User> checkUser = conn.retrieveUser(usernameTxf.getText());
                if (checkUser.isPresent()) {
                    messageDisplay.setText("Username already exists");
                } else {
                    if (conn.create(new User(usernameTxf.getText()), encrypt(passwordTxf.getText()))) {
                        Optional<User> user = conn.retrieveUser(usernameTxf.getText());
                        if (user.isPresent()) {
                            startApplication.setLoggedInUser(user.get());
                            System.out.println("[INFO] User " + user.get().getID() + " logged in...");
                            startApplication.setRoot("viewTasks-view.fxml");
                        }
                    } else {
                        messageDisplay.setText("New user could not be created");
                    }
                }
            } catch (SQLException | IOException e){
                messageDisplay.setText("An error occurred");
                throw new RuntimeException(e);
            }
        } else {
            messageDisplay.setText("Passwords do not match");
        }
    }

    @FXML
    protected void switchToLogin() throws IOException {
        //TODO: All the logic for logging a user in
        System.out.println("[INFO] Switching to login Screen...");
        startApplication.setRoot("login-view.fxml");
    }

    private static String encrypt(String input) {
        input = input + "salt";
        byte[] tmp = input.getBytes(StandardCharsets.UTF_8);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(tmp);

        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
