/**
 * @author Bhavya Patel
 * @author Marc Rizzolo
 * 
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import model.User;




/**
 * The AdminController class is responsible for managing the admin functionality of the application.
 * It handles user creation, user deletion, and data persistence.
 */
public class AdminController implements Initializable{

    @FXML
    private ListView<User> users;

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method is automatically called by the FXMLLoader.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    /**
     * Handles the action when the "Create User" button is clicked.
     * Prompts the user to enter a username and creates a new user with the entered username.
     * If the entered username already exists, displays an error message.
     * Otherwise, adds the new user to the list of users, refreshes the user interface, and saves the data.
     *
     * @param event the action event triggered by clicking the "Create User" button
     */
    @FXML
    public void handleCreateUserButtonAction(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create User");
        dialog.setHeaderText("Enter a username:");
        dialog.setContentText("Username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            if (users.getItems().stream().anyMatch(user -> user.getUsername().equals(username))) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Username already exists");
                alert.setContentText("A user with the same username already exists. Please choose a different username.");
                alert.showAndWait();
            } else {
                User newUser = new User(username);
                users.getItems().add(newUser);
                users.refresh();
                saveData();
            }
        });
    }

    /**
     * Handles the action when the delete user button is clicked.
     * Deletes the user selected from the listview after displaying a confirmation dialog.
     * If the user confirms the deletion, the selected user is removed from the listview,
     * the listview is refreshed, and the data is saved.
     *
     * @param event The action event triggered by the delete user button.
     */
    @FXML
    public void handleDeleteUserButtonAction(ActionEvent event) {
        //delete user selected from listview. add confirmation dialog
        User selectedUser = users.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete User");
            alert.setContentText("Are you sure you want to delete this user?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                users.getItems().remove(selectedUser);
                users.refresh();
                saveData();
            }
        }
    }

    /**
     * Handles the action when the logout button is clicked.
     * This method changes the scene to the login scene.
     *
     * @param event the action event triggered by the logout button
     * @throws Exception if an error occurs during scene transition
     */
    @FXML
    public void handleLogoutButtonAction(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScene.fxml"));
        stage.setScene(new Scene(root));
    }

    /**
     * Saves the data by serializing the list of items in the 'users' object
     * and writing it to a file named "data.dat".
     * 
     * @throws IOException if an I/O error occurs while saving the data
     */
    public void saveData() {
        try {
            FileOutputStream fos = new FileOutputStream("data/data.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(users.getItems()));
            oos.close();
            fos.close();
            System.out.println("Data saved successfully");
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads data from a file and populates the user list.
     * This method reads serialized objects from the "data/data.dat" file and adds them to the user list.
     * If an error occurs during the loading process, an error message is printed along with the stack trace.
     */
    @SuppressWarnings("unchecked")
    private void loadData() {
    try {
        FileInputStream fis = new FileInputStream("data/data.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        List<User> userList = (List<User>) ois.readObject();
        users.getItems().clear();
        users.getItems().addAll(userList);
        ois.close();
        fis.close();
        System.out.println("Data loaded successfully (called from admin)");
    } catch (Exception e) {
        System.err.println("Error loading data: " + e.getMessage());
        e.printStackTrace();
    }
}



}
