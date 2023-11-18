/**
 * @author Bhavya Patel
 * @author Marc Rizzolo
 * 
 */


package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Album;
import model.User;
import util.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.ListView;


public class UserController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox albumContainer;
    @FXML
    private Label albumInfoLabel;
    @FXML
    private Button createAlbumButton;
    @FXML
    private Button renameAlbumButton;
    @FXML
    private Button deleteAlbumButton;
    @FXML
    private Button viewAlbumButton;
    @FXML
    private Button nextAlbumsButton;
    @FXML
    private Button previousAlbumsButton;
    @FXML
    private TextField albumNameField;

    private User user;
    private List<User> users;

    @FXML
    private ListView<Album> albumListView; 


    public void initSession(User user) {
        this.user = user;
        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        albumListView.setItems(FXCollections.observableArrayList(user.getAlbums()));
        users = FileManager.loadData();
    }
    

    @FXML
    private void handleViewAlbum(ActionEvent event) {
        Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum == null) {
            // Show some error message to the user
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an album to view.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumScene.fxml"));
            Parent albumViewRoot = loader.load();

            // Get the controller for the AlbumViewScene
            AlbumController albumViewController = loader.getController();
            albumViewController.initData(selectedAlbum, users , user); // Method to initialize data in AlbumViewController

            // Create the new scene and display it in a new window or dialog
            Scene albumViewScene = new Scene(albumViewRoot);
            Stage albumStage = new Stage();
            albumStage.setTitle("Album View");
            albumStage.setScene(albumViewScene);

            // Optional: Set the parent stage modality if you want to block input to other windows
            albumStage.initModality(Modality.WINDOW_MODAL);
            albumStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            albumStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions, maybe log the error and show a message to the user
        }
    }





    @FXML
    private void handleCreateAlbum() {
        TextInputDialog dialog = new TextInputDialog("New Album");
        dialog.setTitle("Create New Album");
        dialog.setHeaderText("Create a new album");
        dialog.setContentText("Please enter the name of the album:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            Album newAlbum = new Album(name);
            user.getAlbums().add(newAlbum); // Add new album to user's album list
            albumListView.getItems().add(newAlbum); // Update ListView
        });
        updateUsersList(user);
        FileManager.saveData(users);
    }
    
    
    

    @FXML
    private void handleRenameAlbum(ActionEvent event) {
        Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            TextInputDialog dialog = new TextInputDialog(selectedAlbum.getAlbumName());
            dialog.setTitle("Rename Album");
            dialog.setHeaderText("Rename album");
            dialog.setContentText("Please enter the new name of the album:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                selectedAlbum.setAlbumName(name);
                albumListView.refresh();
            });
            updateUsersList(user);
            FileManager.saveData(users);
        }
        
    }

    @FXML
    private void handleDeleteAlbum(ActionEvent event) {
        Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            user.getAlbums().remove(selectedAlbum); // Remove album from user's album list
            albumListView.getItems().remove(selectedAlbum); // Update ListView
            updateUsersList(user);
            FileManager.saveData(users);
        }
    }

    @FXML
    private void handleLogoutButtonAction(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScene.fxml"));
        stage.setScene(new Scene(root));
    }

    private void updateUsersList(User updatedUser) {
        // Replace the old user object with the updated one in the 'users' list
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }
    }

}

        