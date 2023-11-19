/**
 * @author Marc Rizzolo
 * 
 */

package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Album;
import model.Photo;




public class SlideshowController {
    
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
        box.setAlignment(Pos.CENTER); // Set the alignment of the VBox to center

        Photo photo = album.getPhotos().get(pageIndex);
        ImageView imageView = new ImageView(new Image(photo.getImagePath()));
        
        imageView.setPreserveRatio(true); // Preserve the aspect ratio
        imageView.setFitHeight(300); // Set the preferred height (adjust as necessary)
        // You can also set imageView.setFitWidth if you want to limit the width

        box.getChildren().add(imageView);

        return box;
    }
}
