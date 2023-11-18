/**
 * @author Marc Rizzolo
 * 
 */

package controller;

import java.io.File;
import java.util.Calendar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.Album;
import model.Photo;
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

    // Initialize with an album
    public void initData(Album album) {
        this.album = album;
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
            } catch (IOException e) {
                showAlert("Error", "An error occurred while adding the photo: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleMovePhoto(ActionEvent event) {
        // TODO: Implement the logic to move a photo to a different album
        showAlert("Move Photo", "This feature is not implemented yet.");
    }

    @FXML
    private void handleCopyPhoto(ActionEvent event) {
        // TODO: Implement the logic to copy a photo to a different album
        showAlert("Copy Photo", "This feature is not implemented yet.");
    }

    @FXML
    private void handleRemovePhoto(ActionEvent event) {
        // TODO: Implement the logic to delete a photo in an album
        showAlert("Delete Photo", "This feature is not implemented yet.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}