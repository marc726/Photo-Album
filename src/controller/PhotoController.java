package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;


import java.time.format.DateTimeFormatter;

import model.Photo;
import model.Tag;

/**
 * The PhotoController class is responsible for controlling the display of a single photo.
 */
public class PhotoController {
    @FXML
    private ImageView photoImageView;
    @FXML
    private Label captionLabel;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private Label tagsLabel;
    @FXML
    private VBox contentBox;
    @FXML
    private ScrollPane captionScrollPane;

    /**
     * Sets the photo to be displayed in the controller.
     *
     * @param photo The photo object to be displayed.
     */
    public void setPhoto(Photo photo) {
        // Set the photo image
        Image image = new Image(photo.getImagePath());
        photoImageView.setImage(image);

        // Set the photo caption with ScrollPane
        captionLabel.setText(photo.getCaption());
        captionScrollPane.setContent(captionLabel);
        captionScrollPane.setFitToWidth(true); // Ensures the ScrollPane fits the width of the contentBox
        captionScrollPane.setPrefHeight(ScrollPane.USE_COMPUTED_SIZE);
        captionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Only show vertical scrollbar when needed

        // Set the photo date-time of capture
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        dateTimeLabel.setText(photo.getDate().format(formatter));

        // Set the photo tags
        StringBuilder tagsStringBuilder = new StringBuilder();
        for (Tag tag : photo.getTags()) {
            tagsStringBuilder.append(tag).append(", ");
        }
        tagsLabel.setText(tagsStringBuilder.toString());

        // Ensure window size is adjusted when the scene is fully displayed
        Platform.runLater(() -> {
            Stage stage = (Stage) contentBox.getScene().getWindow();
            if (stage != null) {
                String photoFileName = extractFileName(photo.getImagePath());
                stage.setTitle(photoFileName);
                adjustWindowSize(stage , image);
            }
        });
    };
    

    private void adjustWindowSize(Stage stage, Image image) {
    
        // Update the layout to reflect any changes in content
        contentBox.requestLayout();
    
        // Calculate the height of the ScrollPane based on the content
        captionScrollPane.setPrefHeight(ScrollPane.USE_COMPUTED_SIZE);
        captionScrollPane.applyCss();
        captionScrollPane.layout();
    
        // Adjust the stage size based on the content
        stage.sizeToScene();
    
        // Set the minimum size to the current size if necessary
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }
    

    
    
    
    private String extractFileName(String imagePath) {
        // Assuming the path is a file URL, which looks like file:/path/to/file.jpg
        int lastSlashIndex = imagePath.lastIndexOf('/');
        int lastDotIndex = imagePath.lastIndexOf('.');
        if (lastSlashIndex >= 0 && lastDotIndex > lastSlashIndex) {
            return imagePath.substring(lastSlashIndex + 1, lastDotIndex);
        }
        return "Photo"; // Default title if parsing fails
    }

}

