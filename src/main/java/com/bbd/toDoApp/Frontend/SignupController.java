package com.bbd.toDoApp.Frontend;

import com.bbd.toDoApp.dbconnection.Connection;
import com.bbd.toDoApp.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

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
    protected void onSignUpButtonClick() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        //TODO: All the logic for logging a user in
        if(Objects.equals(passwordTxf.getText(), passwordTxf1.getText())){
            try {
                Connection conn = new Connection();
                Optional<User> checkUser = conn.retrieveUser(usernameTxf.getText());
                if (checkUser.isPresent()) {
                    messageDisplay.setText("Username already exists");
                } else {
                    SecureRandom random = new SecureRandom();
                    byte[] salt = new byte[16];
                    random.nextBytes(salt);
                    StringBuilder sb = new StringBuilder();
                    for (byte b : salt) {
                        sb.append(String.format("%02x", b));
                    }
                    String t = sb.toString();

                    boolean result = conn.create(new User(usernameTxf.getText()), encrypt(passwordTxf.getText(),t.getBytes()),t);
                    if (result) {
                        Optional<User> user = conn.retrieveUser(usernameTxf.getText());
                        if (user.isPresent()) {
                            StateObject.setLoggedInUser(user.get());
                            System.out.println("[INFO] User " + user.get().getID() + " logged in...");

                            String path = System.getProperty("user.home") + "\\AppData\\Roaming\\ToDo\\user.txt";
                            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
                            writer.write(user.get().getUsername());
                            writer.close();

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

    private static String encrypt(String input,byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {


        KeySpec spec = new PBEKeySpec(input.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
