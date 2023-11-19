/**
 * @author Bhavya Patel 
 * @author Marc Rizolo
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import util.FileManager;
import java.util.List;


import java.io.IOException;


/**
 * This class is the controller for the login view. It handles the login button action and initializes the view.
 * The initialize method sets the JavaFX and Java version in the label.
 */
public class LoginController {

    @FXML
    private Label label;

    @FXML
    private TextField usernameTextField;

    List<User> users;

    
    /**
     * Handles the action when the login button is clicked.
     * If the username is "admin", it loads the Admin Dashboard scene.
     * If the username is a valid user, it loads the User Dashboard scene and initializes the session with the user's data.
     * If the username is invalid or there is an error loading the data, it displays an error message.
     *
     * @param event The action event triggered by clicking the login button.
     */
    @FXML
    public void handleLoginButtonAction(ActionEvent event) {

        
        Stage stage = (Stage) usernameTextField.getScene().getWindow();
        Parent root = null; // Initialize root to null



        if ("admin".equals(usernameTextField.getText())) {
            try {
                root = FXMLLoader.load(getClass().getResource("/view/AdminDashScene.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }  
        
        else{

            users = FileManager.loadData();

            if (users == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error loading data");
                alert.setContentText("Error loading user data. Please try again.");
                alert.showAndWait();
                return;
            }

            if (users.stream().noneMatch(user -> user.getUsername().equals(usernameTextField.getText()))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid username");
                alert.setContentText("The username you entered does not exist. Please try again.");
                alert.showAndWait();
                return;
            }

            User loginUser = users.stream()
                              .filter(user -> user.getUsername().equals(usernameTextField.getText()))
                              .findFirst()
                              .orElse(null);

            if (loginUser == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid username");
                alert.setContentText("The username you entered does not exist. Please try again.");
                alert.showAndWait();
                return;
            }
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserDashScene.fxml"));
                root = loader.load();
                UserController userController = loader.getController();
                userController.initSession(loginUser); // Pass the existing User object
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
    /**
        * Initializes the LoginController.
        * This method is automatically called after the FXML file has been loaded.
        * It sets the text of the label to display the JavaFX and Java versions.
        */
    @FXML
    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }
}