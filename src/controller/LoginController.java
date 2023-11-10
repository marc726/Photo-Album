/**
 * @author Bhavya Patel
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * This class is the controller for the login view. It handles the login button action and initializes the view.
 * The initialize method sets the JavaFX and Java version in the label.
 */
public class LoginController {

    @FXML
    private Label label;

    /**
     * This method handles the login button action.
     *
     * @param event The event triggered by the login button.
     */
    @FXML
    public void handleLoginButtonAction(ActionEvent event) {
        // Placeholder for login logic
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