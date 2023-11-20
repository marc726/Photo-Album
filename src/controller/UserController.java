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
import util.GlobalTags;
import util.AlbumChangeListener;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.ListView;


/**
 * The UserController class is responsible for controlling the user interface and handling user interactions
 * related to user management and album operations.
 */




 public class UserController implements AlbumChangeListener {

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


    /**
     * Initializes the user session.
     * Sets the user, updates the welcome label, and populates the album list view.
     * Loads user data from the file manager.
     *
     * @param user The user object representing the current user.
     */
    public void initSession(User user) {
        this.user = user;
        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        albumListView.setItems(FXCollections.observableArrayList(user.getAlbums()));
        users = FileManager.loadData();
    }
    

    /**
     * Handles the action event when the user clicks on the "View Album" button.
     * Retrieves the selected album from the album list view and displays it in a new window.
     * If no album is selected, an error message is shown to the user.
     *
     * @param event The action event triggered by clicking the "View Album" button.
     */
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
            albumViewController.initData(selectedAlbum, users , user, this); // Method to initialize data in AlbumViewController

            // Create the new scene and display it in a new window or dialog
            Scene albumViewScene = new Scene(albumViewRoot);
            Stage albumStage = new Stage();
            albumStage.setTitle("Album View");
            albumStage.setScene(albumViewScene);

            albumStage.initModality(Modality.WINDOW_MODAL);
            albumStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            

            albumStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions, maybe log the error and show a message to the user
        }
    }





    /**
     * Handles the action of creating a new album.
     * Prompts the user to enter the name of the album and creates a new album object with the given name.
     * Adds the new album to the user's album list and updates the ListView.
     * Finally, updates the user's list, saves the data, and updates the global tag types.
     */
    @FXML
    private void handleCreateAlbum() {
        TextInputDialog dialog = new TextInputDialog("New Album");
        dialog.setTitle("Create New Album");
        dialog.setHeaderText("Create a new album");
        dialog.setContentText("Please enter the name of the album:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            // Check if album name already exists
            boolean albumExists = user.getAlbums().stream()
                    .anyMatch(album -> album.getAlbumName().equalsIgnoreCase(name));

            if (albumExists) {
                // Show error message to the user
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Album Already Exists");
                alert.setHeaderText(null);
                alert.setContentText("An album with the same name already exists.");
                alert.showAndWait();
            } else {
                Album newAlbum = new Album(name);
                user.getAlbums().add(newAlbum); // Add new album to user's album list
                albumListView.getItems().add(newAlbum); // Update ListView
                updateUsersList(user);
                FileManager.saveData(users, GlobalTags.getInstance().getTagTypes(), GlobalTags.getInstance().getRestrictedTagTypes());
            }
        });
    }
    
    
    

    /**
     * Handles the action of renaming an album.
     * Retrieves the selected album from the album list view and prompts the user to enter a new name for the album.
     * If a new name is provided, updates the album name, refreshes the album list view, and saves the data.
     * 
     * @param event The action event that triggered the method.
     */
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
            FileManager.saveData(users, GlobalTags.getInstance().getTagTypes(), GlobalTags.getInstance().getRestrictedTagTypes());
;
        }
        
    }

    /**
     * Handles the action of deleting an album.
     * Removes the selected album from the user's album list and updates the ListView.
     * Also updates the user's list, saves the data, and updates the global tag types.
     *
     * @param event the action event triggered by the delete button
     */
    @FXML
    private void handleDeleteAlbum(ActionEvent event) {
        Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            user.getAlbums().remove(selectedAlbum); // Remove album from user's album list
            albumListView.getItems().remove(selectedAlbum); // Update ListView
            updateUsersList(user);
            FileManager.saveData(users, GlobalTags.getInstance().getTagTypes(), GlobalTags.getInstance().getRestrictedTagTypes());

        }
    }

    /**
     * Handles the action when the logout button is clicked.
     * This method changes the scene to the login scene.
     *
     * @param event the action event triggered by the logout button
     * @throws Exception if an error occurs while loading the login scene
     */
    @FXML
    private void handleLogoutButtonAction(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScene.fxml"));
        stage.setScene(new Scene(root));
    }




    /**
     * Handles the search action event.
     * Loads the search page, sets up the scene, and initializes data for the search controller.
     * @param event The action event triggered by the search action.
     */
    @FXML
    private void handleSearch(ActionEvent event) {
        try {
            // Load the FXML file for the search page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchDashScene.fxml"));
            Parent searchPage = loader.load();

            // Set up the scene
            Scene searchScene = new Scene(searchPage);

            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene to the stage
            stage.setScene(searchScene);

            // Optionally, initialize data for the search controller
            SearchController searchController = loader.getController();
            searchController.initData(users, user);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IOException (e.g., FXML file not found)
        }
    }

    
  
    /**
     * Updates the 'users' list by replacing the old user object with the updated one.
     * 
     * @param updatedUser The updated user object.
     */
    private void updateUsersList(User updatedUser) {
        // Replace the old user object with the updated one in the 'users' list
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }
    }

    @Override
    public void onAlbumChanged() {
        // Refresh the ListView
        albumListView.getItems().setAll(user.getAlbums());
    }

}

        