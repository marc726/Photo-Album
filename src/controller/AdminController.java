/**
 * @author Bhavya Patel, Marc Rizzolo
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




public class AdminController implements Initializable{

    @FXML
    private ListView<User> users;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    @FXML
    public void handleCreateUserButtonAction(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create User");
        dialog.setHeaderText("Enter a username:");
        dialog.setContentText("Username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            User newUser = new User(username);
            users.getItems().add(newUser);
            users.refresh();
            saveData();

        });
    }

    @FXML
    public void handleDeleteUserButtonAction(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete User");
        dialog.setHeaderText("Enter the username of the user you want to delete:");
        dialog.setContentText("Username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            // TODO Delete the user from the database and update the listview
        });
    }

    @FXML
    public void handleLogoutButtonAction(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScene.fxml"));
        stage.setScene(new Scene(root));
    }

    private void saveData() {
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
        System.out.println("Data loaded successfully");
    } catch (Exception e) {
        System.err.println("Error loading data: " + e.getMessage());
        e.printStackTrace();
    }
}



}
