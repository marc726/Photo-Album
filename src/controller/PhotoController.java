package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    /**
     * Sets the photo to be displayed in the controller.
     *
     * @param photo The photo object to be displayed.
     */
    public void setPhoto(Photo photo) {
        // Set the photo image
        Image image = new Image(photo.getImagePath());
        photoImageView.setImage(image);

        // Set the photo caption
        captionLabel.setText("Caption: " + photo.getCaption()); // Prefix "Caption: "
        captionLabel.setWrapText(true); // Enable text wrapping
        captionLabel.setMaxWidth(photoImageView.getFitWidth());

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
        // Calculate the actual height of the displayed image
        double aspectRatio = image.getWidth() / image.getHeight();
        double displayedImageHeight = photoImageView.getFitWidth() / aspectRatio;

        // Calculate the preferred height for the caption label based on its width
        // Apply CSS and layout to ensure the label's height is computed based on the text
        captionLabel.applyCss();
        captionLabel.layout();
        double captionHeight = captionLabel.getHeight();

        // Calculate the total height and width required, adding spacing and padding
        double padding = 20; // Assuming padding is 20 on each side
        double totalHeight = displayedImageHeight + captionHeight +
                             dateTimeLabel.getHeight() + tagsLabel.getHeight() +
                             40 + (2 * padding); // Additional spacing between elements + padding

        // Determine the width of the window
        double totalWidth = photoImageView.getFitWidth() + (2 * padding); // Assuming the image width defines the window width

        // Adjust the stage size
        stage.setWidth(totalWidth);
        stage.setHeight(totalHeight);

        // Set the minimum size to the current size
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

