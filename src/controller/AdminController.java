/**
 * @author Bhavya Patel
 * 
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class AdminController {

    @FXML
    public void handleCreateUserButtonAction(ActionEvent event) {
        // Placeholder for creating a user
        
    }

    @FXML
    public void handleDeleteUserButtonAction(ActionEvent event) {
        // Placeholder for deleting a user
    }

    @FXML
    public void handleListUsersButtonAction(ActionEvent event) {
        // Placeholder for listing users
    }

    @FXML
    public void handleLogoutButtonAction(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScene.fxml"));
        stage.setScene(new Scene(root));
    }
}
