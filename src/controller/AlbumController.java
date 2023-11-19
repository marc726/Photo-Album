package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import model.Album;
import model.Photo;
import model.User;
import model.Tag;
import util.FileManager;

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

    @FXML
    private void handleAddPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpeg", "*.png", "*.bmp", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                Path destDir = Paths.get("data/pics");
                Files.createDirectories(destDir);
                Path destPath = destDir.resolve(file.getName());
                Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                String imagePath = destPath.toUri().toString();

                Photo newPhoto = new Photo(file.getName(), Calendar.getInstance());
                newPhoto.setImagePath(imagePath);

                album.addPhoto(newPhoto);
                setupPhotoListView();

                showAlert("Photo Added", "Photo has been added to the album.");
                updateUsersList(user);
                FileManager.saveData(users);
            } catch (IOException e) {
                showAlert("Error", "An error occurred while adding the photo: " + e.getMessage());
            }
        }
    }

    // Other event handlers like handleMovePhoto, handleCopyPhoto, handleRemovePhoto, etc.
    // Implement these methods similarly, updating the photo list and refreshing the ListView as needed.

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
            FileManager.saveData(users);
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
            Photo copiedPhoto = new Photo(selectedPhoto.getName(), (Calendar)selectedPhoto.getDate().clone());
            copiedPhoto.setImagePath(selectedPhoto.getImagePath());
            targetAlbum.addPhoto(copiedPhoto);
            setupPhotoListView();
            updateUsersList(user);
            FileManager.saveData(users);
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
        FileManager.saveData(users);
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

    @FXML
    private void handleAddTag(ActionEvent event) {
        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
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

        GridPane grid = new GridPane();
        grid.add(new Label("Tag Name:"), 0, 0);
        grid.add(tagNameDropdown, 1, 0);
        grid.add(new Label("Tag Value:"), 0, 1);
        grid.add(tagValueField, 1, 1);
        dialog.getDialogPane().setContent(grid);

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

            updateUsersList(user);
            FileManager.saveData(users);
        });
    }











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
}
