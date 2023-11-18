package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Album;
import model.User;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    }
    @FXML
    private void handleViewAlbum(ActionEvent event) {
        
        }
    
    @FXML
    private void handleCreateAlbum() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Album");
        dialog.setHeaderText("Enter album name:");
        dialog.setContentText("Album:");
        Optional<String> result = dialog.showAndWait();
        Album newAlbum = new Album(result.get());
        user.getAlbums().add(newAlbum);
    }

    @FXML
    private void handleRenameAlbum(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename Album");
        dialog.setHeaderText("Enter new album name:");
        dialog.setContentText("Album:");
        Optional<String> result = dialog.showAndWait();
        Album selectedAlbum = (Album) albumContainer.getChildren().stream()
                .filter(node -> node.getStyleClass().contains("selected"))
                .findFirst()
                .get()
                .getUserData();
        selectedAlbum.setAlbumName(result.get());
        albumInfoLabel.setText(selectedAlbum.getAlbumName());
        
    }

    @FXML
    private void handleDeleteAlbum(ActionEvent event) {
        Album selectedAlbum = (Album) albumContainer.getChildren().stream()
                .filter(node -> node.getStyleClass().contains("selected"))
                .findFirst()
                .get()
                .getUserData();
        user.getAlbums().remove(selectedAlbum);
        albumContainer.getChildren().removeIf(node -> node.getStyleClass().contains("selected"));
        albumInfoLabel.setText("");
    }



    
}

        