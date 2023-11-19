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




/**
 * The SlideshowController class is responsible for controlling the slideshow functionality
 * and displaying the photos in an album using a pagination control.
 */
public class SlideshowController {
    
    @FXML
    private Pagination pagination;
    private Album album; // The album you're displaying


    
    /**
     * Initializes the data for the SlideshowController.
     * 
     * @param album the album to be used for initialization
     */
    public void initData(Album album) {
        this.album = album;
        setupPagination();
    }

    /**
     * Sets up the pagination for the slideshow.
     * If the album is not null and contains photos, the page count is set to the number of photos
     * and the page factory is set to create pages using the createPage method.
     * If the album is null or does not contain any photos, the page count is set to 1
     * and the page factory is set to create a default page with a message indicating no photos.
     */
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

    /**
     * Creates a VBox container to hold the image view for a slideshow page.
     * The VBox is aligned to the center.
     *
     * @param pageIndex The index of the photo in the album to display.
     * @return The created VBox container.
     */
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
