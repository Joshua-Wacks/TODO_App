package com.bbd.toDoApp.Frontend;

import com.bbd.toDoApp.dbconnection.Connection;
import com.bbd.toDoApp.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Pair;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField usernameTxf;
    @FXML
    private PasswordField passwordTxf;
    @FXML
    private Text messageDisplay;

    protected void initialize() throws IOException, SQLException {
        String path = System.getProperty("user.home") + "\\AppData\\Roaming";

        File newDirectory = new File(path, "ToDo");
        if (newDirectory.exists()) {
            path = System.getProperty("user.home") + "\\AppData\\Roaming\\ToDo\\user.txt";
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            if(line != null) {
                Connection conn = new Connection();
                Optional<User> user = conn.retrieveUser(line);
                if (user.isPresent()) {
                    StateObject.setLoggedInUser(user.get());
                    System.out.println("[INFO] User " + user.get().getID() + " logged in...");
                    startApplication.setRoot("viewTasks-view.fxml");
                }
            }
            reader.close();
        } else {
            newDirectory.mkdir();
            path = System.getProperty("user.home") + "\\AppData\\Roaming\\ToDo\\user.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write("");
            writer.close();
        }
    }

    @FXML
    protected void onLoginButtonClick() throws SQLException, IOException {
        //TODO: All the logic for logging a user in
        try {
            Connection conn = new Connection();
            Optional<Pair<String,String>> checkUserPass = conn.retrieveUserPass(usernameTxf.getText());
            if (checkUserPass.isPresent()) {
                String userEnteredPassword = encrypt(passwordTxf.getText(),checkUserPass.get().getValue().getBytes());
                String storedPassword = checkUserPass.get().getKey();
                if (userEnteredPassword.equals(storedPassword)) {
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
                    messageDisplay.setText("Incorrect Password");
                }
            } else {
                messageDisplay.setText("Incorrect Username");
            }
        } catch (SQLException e){
            messageDisplay.setText("An error occurred");
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void switchToSignUp() throws IOException {
        //TODO: All the logic for logging a user in
        System.out.println("[INFO] Switching to sign up Screen...");
        startApplication.setRoot("signup-view.fxml");
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