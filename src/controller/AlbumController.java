package controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;


import model.Album;
import model.Photo;
import model.User;
import model.Tag;
import util.FileManager;
import util.GlobalTags;
import util.AlbumChangeListener;

/**
 * The AlbumController class is responsible for managing the user interface and functionality
 * related to displaying and manipulating albums and photos.
 */
public class AlbumController {

    @FXML
    private ListView<Photo> photoListView;

    private Album album; // The album you're displaying
    private User user; // The user that owns the album
    private List<User> users;
    private AlbumChangeListener listener;


    // Initialize with an album, users list, and user
    public void initData(Album album, List<User> users, User user, AlbumChangeListener listener) {
        this.album = album;
        this.user = user;
        this.users = users;
        this.listener = listener;
        setupPhotoListView();
    }


    /**
     * Sets up the photo list view by populating it with photos from the album and customizing the appearance of each photo cell.
     */
    private void setupPhotoListView() {
        photoListView.setItems(FXCollections.observableArrayList(album.getPhotos()));
        photoListView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {
            @Override
            public ListCell<Photo> call(ListView<Photo> listView) {
                return new ListCell<Photo>() {
                    @Override
                    protected void updateItem(Photo photo, boolean empty) {
                        super.updateItem(photo, empty);
                        if (empty || photo == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            VBox vbox = new VBox(5); // 5 is the spacing between elements
                            ImageView imageView = new ImageView(new javafx.scene.image.Image(photo.getImagePath()));
                            imageView.setFitHeight(50); // Set thumbnail size
                            imageView.setFitWidth(50);
                            Label captionLabel = new Label(photo.getCaption());
                            vbox.getChildren().addAll(imageView, captionLabel);
                            setGraphic(vbox);
                        }
                    }
                };
            }
        });
    }

    


// --------------------------------------------------------------------------------------------
//                                  PHOTOS


    /**
     * Handles the event when the "Add Photo" button is clicked.
     * Opens a file chooser dialog to select an image file, then adds the selected photo to the album.
     * Updates the photo list view, shows a success message, and saves the data.
     * If an error occurs, shows an error message with the error details.
     *
     * @param event The action event triggered by clicking the "Add Photo" button.
     */
    @FXML
    private void handleAddPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.bmp", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String imagePath = file.toURI().toString();

                Photo newPhoto = new Photo(file.getName(), LocalDateTime.now());
                newPhoto.setImagePath(imagePath);

                album.addPhoto(newPhoto);
                setupPhotoListView();

                showAlert("Photo Added", "Photo has been added to the album.");
                updateUsersList(user);
                FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
                notifyAlbumChanged();
            } catch (Exception e) {
                showAlert("Error", "An error occurred while adding the photo: " + e.getMessage());
            }
        }
    }




    /**
     * Handles the action of moving a photo to another album.
     * 
     * @param event The event that triggered the action.
     */
    @FXML
    private void handleMovePhoto(ActionEvent event) {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();

        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to move.");
            return;
        }

        ChoiceDialog<Album> dialog = new ChoiceDialog<>(null, user.getAlbums());
        dialog.setTitle("Move Photo");
        dialog.setHeaderText("Select an album to move the photo to:");
        Optional<Album> result = dialog.showAndWait();

        if (result.isPresent()) {
            Album targetAlbum = result.get();
            album.getPhotos().remove(selectedPhoto);
            targetAlbum.addPhoto(selectedPhoto);
            setupPhotoListView();
            updateUsersList(user);
            FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
            notifyAlbumChanged();
            showAlert("Photo Moved", "The photo has been moved to the album: " + targetAlbum.getAlbumName() + ".");
        } else {
            showAlert("No Album Selected", "No album was selected. Photo was not moved.");
        }
    }  



    /**
     * Handles the action of copying a photo to another album.
     * If no photo is selected, a warning message is displayed.
     * The user is prompted to select an album to copy the photo to.
     * If an album is selected, the photo is copied to the selected album.
     * The photo list view and user's list are updated accordingly.
     * The data is saved and a success message is displayed.
     * If no album is selected, a warning message is displayed.
     *
     * @param event The action event that triggered the method.
     */
    @FXML
    private void handleCopyPhoto(ActionEvent event) {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();

        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to copy.");
            return;
        }

        ChoiceDialog<Album> dialog = new ChoiceDialog<>(null, user.getAlbums());
        dialog.setTitle("Copy Photo");
        dialog.setHeaderText("Select an album to copy the photo to:");
        Optional<Album> result = dialog.showAndWait();

        if (result.isPresent()) {
            Album targetAlbum = result.get();
            Photo copiedPhoto = new Photo(selectedPhoto.getName(), LocalDateTime.now());
            copiedPhoto.setImagePath(selectedPhoto.getImagePath());
            targetAlbum.addPhoto(copiedPhoto);
            setupPhotoListView();
            updateUsersList(user);
            FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
            showAlert("Photo Copied", "The photo has been copied to the album: " + targetAlbum.getAlbumName() + ".");
        } else {
            showAlert("No Album Selected", "No album was selected. Photo was not copied.");
        }
        
    }



    /**
     * Handles the action event for removing a photo from the album.
     * If no photo is selected, a warning message is displayed.
     * Otherwise, the selected photo is removed from the album, and the photo list view is updated.
     * The user's list is also updated, and the data is saved to the file system.
     * Finally, a confirmation message is displayed.
     *
     * @param event The action event triggered by the remove photo button.
     */
    @FXML
    private void handleRemovePhoto(ActionEvent event) {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();

        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to remove.");
            return;
        }

        album.getPhotos().remove(selectedPhoto);
        setupPhotoListView();
        updateUsersList(user);
        FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
        notifyAlbumChanged();
        showAlert("Photo Removed", "The photo has been removed from the album.");
    }



    /**
     * Handles the action event for inspecting a photo.
     * Retrieves the selected photo from the photoListView and opens a new window to display the photo.
     * If no photo is selected, a warning message is displayed.
     *
     * @param event The action event triggered by the inspect photo button.
     */
    @FXML
    private void handleInspectPhoto(ActionEvent event) {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();

        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to inspect.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoScene.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        PhotoController photoController = loader.getController();
        photoController.setPhoto(selectedPhoto);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //------------------------------------------------------------------------------------
    //                                 TAGS


    /**
     * Handles the event of adding a tag to a photo.
     * Retrieves the selected photo from the photoListView and prompts the user to choose a tag type.
     * Then, prompts the user to enter a tag value for the selected tag type.
     * Adds the new tag to the selected photo and updates the photoListView.
     * Saves the updated data to the file system.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    private void handleAddTagToPhoto(ActionEvent event) {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to add a tag to.");
            return;
        }
    
        String currentImagePath = selectedPhoto.getImagePath();
        addTagToMatchingPhotos(currentImagePath);
    }
    
    private void addTagToMatchingPhotos(String imagePath) {
        GlobalTags globalTags = GlobalTags.getInstance();
        Set<String> tagTypes = globalTags.getTagTypes();
    
        if (tagTypes.isEmpty()) {
            showAlert("No Tag Types Available", "Please add a tag type first.");
            return;
        }
    
        ChoiceDialog<String> tagTypeDialog = new ChoiceDialog<>(null, tagTypes);
        tagTypeDialog.setTitle("Select Tag Type");
        tagTypeDialog.setHeaderText("Choose a tag type:");
        Optional<String> tagTypeResult = tagTypeDialog.showAndWait();
    
        tagTypeResult.ifPresent(tagType -> {
            TextInputDialog tagValueDialog = new TextInputDialog();
            tagValueDialog.setTitle("Add Tag");
            tagValueDialog.setHeaderText("Add tag value for " + tagType);
            tagValueDialog.setContentText("Enter tag value:");
    
            Optional<String> tagValueResult = tagValueDialog.showAndWait();
            tagValueResult.ifPresent(tagValue -> {
                Tag newTag = new Tag(tagType, tagValue);
                for (User usr : users) {
                    for (Album alb : usr.getAlbums()) {
                        for (Photo photo : alb.getPhotos()) {
                            if (photo.getImagePath().equals(imagePath) && !photo.getTags().contains(newTag)) {
                                photo.addTag(newTag);
                            }
                        }
                    }
                }
                updateUsersList(user);
                FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
                photoListView.refresh();
            });
        });
    }


    @FXML
    private void handleRemoveTagFromPhoto() {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to remove a tag from.");
            return;
        }

        if (selectedPhoto.getTags().isEmpty()) {
            showAlert("No Tags", "This photo has no tags to remove.");
            return;
        }

        String currentImagePath = selectedPhoto.getImagePath();
        removeTagFromMatchingPhotos(currentImagePath);
    }

    private void removeTagFromMatchingPhotos(String imagePath) {
        ChoiceDialog<Tag> dialog = new ChoiceDialog<>(null, new ArrayList<>(getTagsForImage(imagePath)));
        dialog.setTitle("Remove Tag");
        dialog.setHeaderText("Select a tag to remove:");
        Optional<Tag> result = dialog.showAndWait();

        result.ifPresent(tagToRemove -> {
            for (User usr : users) {
                for (Album alb : usr.getAlbums()) {
                    for (Photo photo : alb.getPhotos()) {
                        if (photo.getImagePath().equals(imagePath)) {
                            photo.getTags().remove(tagToRemove);
                        }
                    }
                }
            }
            updateUsersList(user);
            FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
            photoListView.refresh();
        });
    }

    

    /**
     * Handles the action of adding a new tag type.
     * Prompts the user to enter a tag type and adds it to the global tag types.
     * Saves the updated tag types to the file system.
     */
    @FXML
    private void handleAddTagType() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Tag Type");
        dialog.setHeaderText("Create a new tag type");
        dialog.setContentText("Enter tag type:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(tagType -> {
            GlobalTags.getInstance().addTagType(tagType);
            FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
        });
    }


// --------------------------------------------------------------------------------------------
//                                  CAPTIONS


    /**
     * Handles the event when the caption of a photo is changed.
     * If no photo is selected, it displays an alert message.
     * Opens a dialog box to allow the user to enter a new caption for the selected photo.
     * Updates the caption of the selected photo and refreshes the photo list view.
     */
    @FXML
    private void handleChangeCaption() {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to change the caption of.");
            return;
        }
        
        
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change Caption");
        dialog.setHeaderText("Change the caption of the photo");

        // Set the button types.
        ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        TextField captionField = new TextField();
        captionField.setText(selectedPhoto.getCaption()); // Pre-fill with current caption

        GridPane grid = new GridPane();
        grid.add(new Label("Caption:"), 0, 0);
        grid.add(captionField, 1, 0);
        dialog.getDialogPane().setContent(grid);

        // Enable/Disable OK button depending on whether a caption was entered.
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        captionField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return captionField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(caption -> {
            selectedPhoto.setCaption(caption);
            setDateTime(selectedPhoto);
            updateUsersList(user);
            String photoFilePath = selectedPhoto.getImagePath(); // New variable to get the photo's file path
            changeCaptionForAllMatchingPhotos(photoFilePath, caption);
            FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
            photoListView.refresh();
        });
    }


// --------------------------------------------------------------------------------------------
//                                  SLIDESHOW


    /**
     * Handles the action event for starting the slideshow.
     * Loads the SlideshowScene.fxml file, initializes the SlideshowController with the album data,
     * and displays the scene in a new stage.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    private void handleStartSlideshow(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SlideshowScene.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        SlideshowController slideshowController = loader.getController();
        slideshowController.initData(album);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

// --------------------------------------------------------------------------------------------
//                                  HELPERS


    /**
     * Displays an alert dialog with the specified title and content.
     *
     * @param title   the title of the alert dialog
     * @param content the content of the alert dialog
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    
    /**
     * Updates the users list by replacing the user with the same username as the updated user.
     *
     * @param updatedUser The updated user object.
     */
    private void updateUsersList(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }
    }


    /**
     * Sets the date and time for the given photo to the current date and time.
     * Refreshes the photo list view.
     *
     * @param photo The photo for which to set the date and time.
     */
    private void setDateTime(Photo photo) {
        photo.setDate(LocalDateTime.now());
        photoListView.refresh();
    }

    private void notifyAlbumChanged() {
        if (listener != null) {
            listener.onAlbumChanged();
        }
    }

    private void changeCaptionForAllMatchingPhotos(String imagePath, String newCaption) {
        for (User usr : users) {
            for (Album alb : usr.getAlbums()) {
                for (Photo photo : alb.getPhotos()) {
                    if (photo.getImagePath().equals(imagePath)) {
                        photo.setCaption(newCaption);
                    }
                }
            }
        }
        updateUsersList(user);
        FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
        photoListView.refresh();
    }

    private Set<Tag> getTagsForImage(String imagePath) {
        Set<Tag> tags = new HashSet<>();
        for (User usr : users) {
            for (Album alb : usr.getAlbums()) {
                for (Photo photo : alb.getPhotos()) {
                    if (photo.getImagePath().equals(imagePath)) {
                        tags.addAll(photo.getTags());
                    }
                }
            }
        }
        return tags;
    }

}
