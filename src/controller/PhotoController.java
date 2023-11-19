package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.format.DateTimeFormatter;

import model.Photo;
import model.Tag;

public class PhotoController {
    @FXML
    private ImageView photoImageView;
    @FXML
    private Label captionLabel;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private Label tagsLabel;

    public void setPhoto(Photo photo) {
        // Set the photo image
        Image image = new Image(photo.getImagePath());
        photoImageView.setImage(image);

        // Set the photo caption
        captionLabel.setText(photo.getCaption());

        // Set the photo date-time of capture
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        dateTimeLabel.setText(photo.getDate().format(formatter));

        // Set the photo tags
        StringBuilder tagsStringBuilder = new StringBuilder();
        for (Tag tag : photo.getTags()) {
            tagsStringBuilder.append(tag).append(", ");
        }
        tagsLabel.setText(tagsStringBuilder.toString());
    }
}


