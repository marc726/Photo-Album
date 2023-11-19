/**
 * @author Marc Rizzolo
 * 
 */

package controller;

import java.io.File;
import java.util.Calendar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.Album;
import model.Photo;
import model.User;
import util.FileManager;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;




public class AlbumController {

    @FXML
    private Pagination pagination;
    private Album album; // The album you're displaying
    private User user; // The user that owns the album
    private List<User> users;

    // Initialize with an album
    public void initData(Album album, List<User> users, User user) {
        this.album = album;
        this.user = user;
        users = FileManager.loadData();
        this.users = users;
        setupPagination();
    }

    private void setupPagination() {
        if (album != null && album.getPhotos() != null && !album.getPhotos().isEmpty()) {
            // Set the page count to the number of photos
            pagination.setPageCount(album.getPhotos().size());
            pagination.setPageFactory(this::createPage);
        } else {
            // Set page count to 1 for albums with no photos
            pagination.setPageCount(1);
            pagination.setPageFactory(pageIndex -> {
                // You can create a default page to show when there are no photos
                VBox box = new VBox();
                ImageView imageView = new ImageView(); // Set a default image or leave it empty
                box.getChildren().add(imageView);
                // You can also add a text label to inform the user that there are no photos
                box.getChildren().add(new Label("No photos in this album."));
                return box;
            });
        }
    }

    private VBox createPage(int pageIndex) {
        VBox box = new VBox();
        Photo photo = album.getPhotos().get(pageIndex);
        ImageView imageView = new ImageView(photo.getImagePath());
        box.getChildren().add(imageView);
        return box;
    }

    @FXML
    private void handleAddPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filters
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpeg", "*.png", "*.bmp", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Show open dialog
        File file = fileChooser.showOpenDialog(pagination.getScene().getWindow());

        // Check if a file was selected
        if (file != null) {
            try {
                // Define the destination path within the application directory
                Path destDir = Paths.get("data/pics");
                Files.createDirectories(destDir); // Create directories if they do not exist
                
                // Define the destination file path
                Path destPath = destDir.resolve(file.getName());
                
                // Copy the file to the destination, replacing it if it already exists
                Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                
                // Convert the destination path to a URI string
                String imagePath = destPath.toUri().toString();
                
                // Create a new Photo object
                Photo newPhoto = new Photo(file.getName(), Calendar.getInstance());
                newPhoto.setImagePath(imagePath); // Set the image path to the new file location
                
                // Add the new photo to the album and refresh the pagination
                album.addPhoto(newPhoto);
                setupPagination();
                
                showAlert("Photo Added", "Photo has been added to the album.");
                updateUsersList(user);
                FileManager.saveData(users);

            } catch (IOException e) {
                showAlert("Error", "An error occurred while adding the photo: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleMovePhoto(ActionEvent event) {
        // TODO: Implement the logic to move a photo to a different album
        //showAlert("Move Photo", "This feature is not implemented yet.");
        //getting selected photo
        int pageIndex = pagination.getCurrentPageIndex();
        Photo selectedPhoto = album.getPhotos().get(pageIndex);

        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to move.");
            return;
        }

        //show diaglog box to select album to move to
        ChoiceDialog<Album> dialog = new ChoiceDialog<Album>(null, user.getAlbums());
        dialog.setTitle("Move Photo");
        dialog.setHeaderText("Select an album to move the photo to:");
        Optional<Album> result = dialog.showAndWait();

        //move photo to selected album
        if (result.isPresent()) {
            Album targetAlbum = result.get();
            pageIndex = pagination.getCurrentPageIndex();
            selectedPhoto = album.getPhotos().remove(pageIndex);
            targetAlbum.addPhoto(selectedPhoto);
            showAlert("Photo Moved", "The Photo has been moved to the album: " + targetAlbum.getAlbumName() + ".");
            setupPagination();
            updateUsersList(user);
            FileManager.saveData(users);
        } else {
            showAlert("No Album Selected", "No album was selected. Photo was not moved.");
        }
    }

    @FXML
    private void handleCopyPhoto(ActionEvent event) {
        // TODO: Implement the logic to copy a photo to a different album
        //showAlert("Copy Photo", "This feature is not implemented yet.");
        // Get the currently displayed photo
        int pageIndex = pagination.getCurrentPageIndex();
        Photo selectedPhoto = album.getPhotos().get(pageIndex);

        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to copy.");
            return;
        }

        // Show a dialog to select the target album
        ChoiceDialog<Album> dialog = new ChoiceDialog<>(null, user.getAlbums());
        dialog.setTitle("Copy Photo");
        dialog.setHeaderText("Select an album to copy the photo to:");
        Optional<Album> result = dialog.showAndWait();

        // Copy the photo to the target album
        if (result.isPresent()) {
            Album targetAlbum = result.get();
            Photo copiedPhoto = new Photo();
            copiedPhoto.setName(selectedPhoto.getName());
            copiedPhoto.setImagePath(selectedPhoto.getImagePath());
            // Set any other properties that need to be copied...
            targetAlbum.addPhoto(copiedPhoto);
            setupPagination();
            updateUsersList(user);
            FileManager.saveData(users);
            showAlert("Photo Copied", "The photo has been copied to the album: " + targetAlbum.getAlbumName() + ".");
        } else {
            showAlert("No Album Selected", "No album was selected. Photo was not copied.");
        }
    }

    @FXML
    private void handleRemovePhoto(ActionEvent event) {
        // TODO: Implement the logic to delete a photo in an album
        //showAlert("Delete Photo", "This feature is not implemented yet.");

        // Get the currently displayed photo
        int pageIndex = pagination.getCurrentPageIndex();
        Photo selectedPhoto = album.getPhotos().get(pageIndex);

        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to remove.");
            return;
        }

        // Remove the photo from the album
        album.getPhotos().remove(selectedPhoto);
        setupPagination();
        updateUsersList(user);
        FileManager.saveData(users);
        showAlert("Photo Removed", "The photo has been removed from the album.");
    }

    @FXML
    private void handleInspectPhoto(ActionEvent event) {
        // TODO: Implement the logic to open a new window and display the selected photo
        // along with its caption, date-time of capture, and all its tags.
        //showAlert("Not implemented", "Not implemented yet");
        int pageIndex = pagination.getCurrentPageIndex();
        Photo selectedPhoto = album.getPhotos().get(pageIndex);

        //load fxml file for photo inspection view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoScene.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get controller of new window and pass the selected photo
        PhotoController photoController = loader.getController();
        photoController.setPhoto(selectedPhoto);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

    @FXML
    private void handleAddTag(ActionEvent event) {
        //retrieve selected photo
        int pageIndex = pagination.getCurrentPageIndex();
        Photo selectedPhoto = album.getPhotos().get(pageIndex);
        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to add a tag to.");
            return;
        }
        
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Tag");
        dialog.setHeaderText("Add a new tag to the photo");

        ComboBox<String> tagNameDropdown = new ComboBox<>();
        tagNameDropdown.getItems().addAll("Person", "Location", "Event", "Food", "Animal", "Other");
        TextField tagValueField = new TextField();

        //populate diaglog's grid plane
        GridPane grid = new GridPane();
        grid.add(new Label("Tag Name:"), 0, 0);
        grid.add(tagNameDropdown, 1, 0);
        grid.add(new Label("Tag Value:"), 0, 1);
        grid.add(tagValueField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        //Convert the result to a pair when the OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(tagNameDropdown.getValue(), tagValueField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(tagNameValue -> {
            Tag newTag = new Tag(tagNameValue.getKey(), tagNameValue.getValue());
            selectedPhoto.addTag(newTag);

            updatePhotoView();
        });
    }
}