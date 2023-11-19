/**
 * @author Marc Rizzolo
 */

package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import model.Album;
import model.Photo;
import model.User;
import util.FileManager;
import util.GlobalTags;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchController {

    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private TextField tagTypeField1, tagValueField1, tagTypeField2, tagValueField2;
    @FXML
    private ListView<Photo> photoListView;
    @FXML
    private VBox tagSearchVBox;

    private List<User> users; 
    private User user;

    public void initData(List<User> users, User user) {
        this.users = users;
        this.user = user;
        setupPhotoListView();
    }

    private void setupPhotoListView() {
        photoListView.setCellFactory(param -> new ListCell<>() {
            private ImageView imageView = new ImageView();
    
            @Override
            protected void updateItem(Photo photo, boolean empty) {
                super.updateItem(photo, empty);
                if (empty || photo == null) {
                    setGraphic(null);
                } else {
                    String imagePath = photo.getImagePath();
                    Image image = new Image(imagePath, 50, 50, true, true); // Thumbnail size 50x50
                    imageView.setImage(image);
    
                    setGraphic(imageView);
                }
            }
        });
    }
    

    @FXML
    private void handleSearchByDateRange() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            showAlert("Invalid Date Range", "Please select a valid start and end date.");
            return;
        }

        List<Photo> results = searchPhotosByDateRange(startDate, endDate);
        photoListView.setItems(FXCollections.observableArrayList(results));
    }

    private List<Photo> searchPhotosByDateRange(LocalDate startDate, LocalDate endDate) {
        // Convert LocalDate to LocalDateTime at the start and end of the day
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Photo> results = new ArrayList<>();
        for (User user : users) {
            for (Album album : user.getAlbums()) {
                results.addAll(album.getPhotos().stream()
                        .filter(photo -> !photo.getDate().isBefore(startDateTime) && !photo.getDate().isAfter(endDateTime))
                        .collect(Collectors.toList()));
            }
        }
        return results;
    }


    @FXML
    private void handleSearchByTag() {
        String tagType = tagTypeField1.getText();
        String tagValue = tagValueField1.getText();
        if (tagType.isEmpty() || tagValue.isEmpty()) {
            showAlert("Invalid Tag", "Please enter a tag type and value.");
            return;
        }

        List<Photo> results = searchPhotosByTag(tagType, tagValue);
        photoListView.setItems(FXCollections.observableArrayList(results));
    }

    private List<Photo> searchPhotosByTag(String tagType, String tagValue) {
        List<Photo> results = new ArrayList<>();
        for (User user : users) {
            for (Album album : user.getAlbums()) {
                results.addAll(album.getPhotos().stream()
                        .filter(photo -> photo.getTags().stream()
                                .anyMatch(tag -> tag.getTagName().equalsIgnoreCase(tagType) && tag.getTagValue().equalsIgnoreCase(tagValue)))
                        .collect(Collectors.toList()));
            }
        }
        return results;
    }

    @FXML
    private void handleAndSearch() {
        String tagType1 = tagTypeField1.getText();
        String tagValue1 = tagValueField1.getText();
        String tagType2 = tagTypeField2.getText();
        String tagValue2 = tagValueField2.getText();

        if (tagType1.isEmpty() || tagValue1.isEmpty() || tagType2.isEmpty() || tagValue2.isEmpty()) {
            showAlert("Invalid Tag", "Please enter both tag types and values.");
            return;
        }

        List<Photo> results = searchPhotosByTag(tagType1, tagValue1).stream()
                .filter(photo -> photo.getTags().stream()
                        .anyMatch(tag -> tag.getTagName().equalsIgnoreCase(tagType2) && tag.getTagValue().equalsIgnoreCase(tagValue2)))
                .collect(Collectors.toList());

        photoListView.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleOrSearch() {
        String tagType1 = tagTypeField1.getText();
        String tagValue1 = tagValueField1.getText();
        String tagType2 = tagTypeField2.getText();
        String tagValue2 = tagValueField2.getText();

        if (tagType1.isEmpty() || tagValue1.isEmpty() || tagType2.isEmpty() || tagValue2.isEmpty()) {
            showAlert("Invalid Tag", "Please enter both tag types and values.");
            return;
        }

        List<Photo> results = new ArrayList<>();
        results.addAll(searchPhotosByTag(tagType1, tagValue1));
        results.addAll(searchPhotosByTag(tagType2, tagValue2));
        results = results.stream().distinct().collect(Collectors.toList());

        photoListView.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleCreateAlbumFromResults() {
        List<Photo> selectedPhotos = new ArrayList<>(photoListView.getItems());
        if (selectedPhotos.isEmpty()) {
            showAlert("No Photos Selected", "There are no photos to add to the album.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("New Album");
        dialog.setTitle("Create New Album");
        dialog.setHeaderText("Enter the name of the new album:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(albumName -> {
            Album newAlbum = new Album(albumName);
            newAlbum.getPhotos().addAll(selectedPhotos);
            addUserAlbum(newAlbum);
            showAlert("Album Created", "A new album has been created with the selected photos.");
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void addUserAlbum(Album newAlbum) {

        user.getAlbums().add(newAlbum);
        updateUsersList(user);

        FileManager.saveData(users, GlobalTags.getInstance().getTagTypes());
    }

    private void updateUsersList(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserDashScene.fxml"));
            Parent userDashboard = loader.load();
            Scene userDashboardScene = new Scene(userDashboard);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(userDashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Log the error or show an alert
        }
    }

    @FXML
    private void handleClearSearch(ActionEvent event) {
        photoListView.setItems(FXCollections.observableArrayList());  // Clear the ListView
    }
}

