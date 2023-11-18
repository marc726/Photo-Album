package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Album;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox albumContainer;
    @FXML
    private Label albumInfoLabel;
    @FXML
    private Button createAlbumButton;
    @FXML
    private Button renameAlbumButton;
    @FXML
    private Button deleteAlbumButton;
    @FXML
    private Button viewAlbumButton;
    @FXML
    private Button nextAlbumsButton;
    @FXML
    private Button previousAlbumsButton;
    @FXML
    private TextField albumNameField;

    private User user;
    private List<Album> albums; // This should be populated with actual album data
    private int albumPageIndex = 0;
    private static final int ALBUMS_PER_PAGE = 3;
    private Album selectedAlbum;

    public void initSession(User user) {
        this.user = user;
        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");

        // Dummy data for demonstration
        albums = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            albums.add(new Album("Album " + i));
        }

        updateAlbumDisplay();
    }

    private void updateAlbumDisplay() {
        albumContainer.getChildren().clear();
        int start = albumPageIndex * ALBUMS_PER_PAGE;
        int end = Math.min(start + ALBUMS_PER_PAGE, albums.size());
        for (int i = start; i < end; i++) {
            Album album = albums.get(i);
            Button albumButton = new Button(album.getAlbumName());
            albumButton.setOnAction(e -> selectAlbum(album));
            albumContainer.getChildren().add(albumButton);
        }
    }

    private void selectAlbum(Album album) {
        selectedAlbum = album;
        albumInfoLabel.setText("Album: " + album.getAlbumName() + " - Photos: " + album.getNumPhotos());
    }

    @FXML
    private void handleCreateAlbum() {
        String albumName = albumNameField.getText();
        if (albumName.isEmpty()) {
            // Show error message
            return;
        }
        Album newAlbum = new Album(albumName);
        albums.add(newAlbum);
        albumNameField.clear();
        updateAlbumDisplay();
    }

    @FXML
    private void handleRenameAlbum() {
        if (selectedAlbum == null) {
            // Show error message
            return;
        }
        String newAlbumName = albumNameField.getText();
        if (newAlbumName.isEmpty()) {
            // Show error message
            return;
        }
        selectedAlbum.setAlbumName(newAlbumName);
        albumNameField.clear();
        updateAlbumDisplay();
    }

    @FXML
    private void handleDeleteAlbum() {
        if (selectedAlbum == null) {
            // Show error message
            return;
        }
        albums.remove(selectedAlbum);
        selectedAlbum = null;
        albumInfoLabel.setText("");
        updateAlbumDisplay();
    }

    @FXML
    private void handleViewAlbum() {
        if (selectedAlbum == null) {
            // Show error message
            return;
        }
        // Open a new window to view the selected album
        // This depends on how you've set up your application
    }

    @FXML
    private void handleNextAlbums() {
        if ((albumPageIndex + 1) * ALBUMS_PER_PAGE < albums.size()) {
            albumPageIndex++;
            updateAlbumDisplay();
        }
    }

    @FXML
    private void handlePreviousAlbums() {
        if (albumPageIndex > 0) {
            albumPageIndex--;
            updateAlbumDisplay();
        }
    }
}

        