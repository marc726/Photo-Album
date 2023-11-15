/**
 * @author Bhavya Patel
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


    /**
     * This method handles the login button action.
     *
     * @param event The event triggered by the login button.
     */
    
    @FXML
    public void handleLoginButtonAction(ActionEvent event) {
        //get username entered from LoginScene
        //check if username is valid
        //if valid, load AdminDashScene or UserDashScene
        //if not valid, display error message

        Stage stage = (Stage) usernameTextField.getScene().getWindow();
        Parent root;

        if ("admin".equals(usernameTextField.getText())) {
            try {
                root = FXMLLoader.load(getClass().getResource("/view/AdminDashScene.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserDashScene.fxml"));
                root = loader.load(); // Load the FXML file
                UserController userController = loader.getController(); // Get the UserController from the loader
                User user = new User(usernameTextField.getText()); // Create a User object for the session
                userController.initSession(user); // Pass the User object to the UserController
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
     * This method initializes the view and sets the JavaFX and Java version in the label.
     */
    @FXML
    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }
}