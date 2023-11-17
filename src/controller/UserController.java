package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * UserController manages the actions that can be performed by a logged-in user.
 * This includes album management, photo management, and logging out.
 */
public class UserController{

    @FXML
    private Label welcomeLabel;

    @FXML
    private AnchorPane rootPane;
    
    // Add other FXML elements that this controller will manage

    private User user;

    /**
     * Initialize the user session in the controller.
     * @param user The User object representing the logged-in user.
     */

    public void initSession(User user) {
        System.out.println("Initializing user session...");
        this.user = user;
        updateWelcomeLabel(user);
        // Load user data like albums and photos if needed
    }

    /**
     * Update the welcome label with the user's username.
     */
    private void updateWelcomeLabel(User user) {
        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
    }

    // Add methods to handle user actions like creating albums, opening albums, etc.

    /**
     * Handles the action of logging out from the user's session.
     * @param event The event that triggered the logout action.
     */
    @FXML
    public void handleLogoutButtonAction(ActionEvent event) throws IOException {
        // Use welcomeLabel to get the Stage
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScene.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Other methods...

    /**
     * Handles the action of uploading a photo associated with the logged-in user.
     * @param event The event that triggered the upload action.
     */
    @FXML
    public void handleUploadPhotoButtonAction(ActionEvent event) {
        // Implement the logic for uploading a photo associated with the logged-in user
        // You can use the user object to get the necessary information
        // For example:
        // String userId = user.getUserId();
        // String username = user.getUsername();
        // ...
        // Implement the logic to upload the photo here
    }
}

    
