/**
 * @author Bhavya Patel
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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

    @FXML
    private TextField passwordTextField;

    /**
     * This method handles the login button action.
     *
     * @param event The event triggered by the login button.
     */
    
    @FXML
    public void handleLoginButtonAction(ActionEvent event) {
        // Placeholder for login logic
        Stage stage = (Stage) usernameTextField.getScene().getWindow();
        Parent root;

        if ("admin".equals(usernameTextField.getText()) && "admin".equals(passwordTextField.getText())) {
            try {
                root = FXMLLoader.load(getClass().getResource("/view/AdminDashScene.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            // Placeholder for regular login logic
            return;
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