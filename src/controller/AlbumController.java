package controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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

public class AlbumController {

    @FXML
    private ListView<Photo> photoListView;

    private Album album; // The album you're displaying
    private User user; // The user that owns the album
    private List<User> users;

    // Initialize with an album, users list, and user
    public void initData(Album album, List<User> users, User user) {
        this.album = album;
        this.user = user;
        this.users = users;
        setupPhotoListView();
    }



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


    @FXML
    private void handleAddPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpeg", "*.png", "*.bmp", "*.gif");
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
            } catch (Exception e) {
                showAlert("Error", "An error occurred while adding the photo: " + e.getMessage());
            }
        }
    }




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
            showAlert("Photo Moved", "The photo has been moved to the album: " + targetAlbum.getAlbumName() + ".");
        } else {
            showAlert("No Album Selected", "No album was selected. Photo was not moved.");
        }
    }  



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
        showAlert("Photo Removed", "The photo has been removed from the album.");
    }



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


    @FXML
    private void handleAddTagToPhoto(ActionEvent event) {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to add a tag to.");
            return;
        }

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
            while (true) {
                TextInputDialog tagValueDialog = new TextInputDialog();
                tagValueDialog.setTitle("Add Tag");
                tagValueDialog.setHeaderText("Add tag value for " + tagType);
                tagValueDialog.setContentText("Enter tag value (Cancel to stop):");

                Optional<String> tagValueResult = tagValueDialog.showAndWait();
                if (tagValueResult.isPresent()) {
                    String tagValue = tagValueResult.get();
                    Tag newTag = new Tag(tagType, tagValue);
                    selectedPhoto.addTag(newTag);
                } else {
                    break; // Exit the loop if the user cancels
                }
            }
            photoListView.refresh();
            updateUsersList(user);
            FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
        });
    }


    

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
            FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
            photoListView.refresh();
        });
    }


// --------------------------------------------------------------------------------------------
//                                  SLIDESHOW


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
//                                  SEARCH





// --------------------------------------------------------------------------------------------
//                                  HELPERS


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    
    private void updateUsersList(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }
    }


    private void setDateTime(Photo photo) {
        photo.setDate(LocalDateTime.now());
        photoListView.refresh();
    }
}
