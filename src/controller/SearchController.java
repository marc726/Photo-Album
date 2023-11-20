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
import javafx.scene.control.ComboBox;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The SearchController class is responsible for handling search operations in the application.
 * It provides methods to search photos by date range, tag, and perform logical AND and OR searches.
 * The class also allows the user to create a new album from the search results.
 */
public class SearchController {

    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private TextField tagTypeField1, tagValueField1, tagTypeField2, tagValueField2;
    @FXML
    private ListView<Photo> photoListView;
    @FXML
    private VBox tagSearchVBox;
    @FXML
    private ComboBox<String> tagTypeComboBox1;
    @FXML
    private ComboBox<String> tagTypeComboBox2;


    private List<User> users; 
    private User user;

    /**
     * Initializes the data for the SearchController.
     * 
     * @param users The list of users.
     * @param user The current user.
     */
    public void initData(List<User> users, User user) {
        this.users = users;
        this.user = user;
        setupPhotoListView();
        populateTagTypeComboBoxes();
    }

    /**
     * Sets up the photo list view by configuring the cell factory to display photos as thumbnails.
     */
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
    

    /**
     * Handles the search by date range functionality.
     * Retrieves the start and end dates from the date pickers,
     * validates the date range, and performs a search for photos
     * within that range. The search results are then displayed
     * in the photo list view.
     */
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

    /**
     * Searches for photos within a specified date range.
     * 
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of photos that fall within the specified date range.
     */
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


    /**
     * Handles the search by tag functionality.
     * Retrieves the tag type and value from the input fields.
     * If the tag type or value is empty, displays an error message.
     * Otherwise, performs a search for photos based on the provided tag type and value.
     * Updates the photo list view with the search results.
     */
    @FXML
    private void handleSearchByTag() {
        String tagType = tagTypeComboBox1.getValue();
        String tagValue = tagValueField1.getText();
        if (tagType == null || tagValue.isEmpty()) {
            showAlert("Invalid Tag", "Please select a tag type and enter a value.");
            return;
        }

        List<Photo> results = searchPhotosByTag(tagType, tagValue);
        photoListView.setItems(FXCollections.observableArrayList(results));
    }

    /**
     * Searches for photos based on a specific tag.
     *
     * @param tagType   the type of tag to search for
     * @param tagValue  the value of the tag to search for
     * @return a list of photos that match the specified tag
     */
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

    /**
     * Handles the "And" search operation by retrieving the tag values from the input fields,
     * performing a search based on the provided tag values, and displaying the results in the photo list view.
     * If any of the tag fields are empty, an alert is shown indicating that both tag types and values should be entered.
     */
    @FXML
    private void handleAndSearch() {
        String tagType1 = tagTypeComboBox1.getValue();
        String tagValue1 = tagValueField1.getText();
        String tagType2 = tagTypeComboBox2.getValue();
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

    /**
     * Handles the "Or" search functionality.
     * Retrieves the tag types and values from the input fields.
     * Performs a search for photos that match either of the tag types and values.
     * Displays the search results in the photo list view.
     * If any of the tag fields are empty, displays an error message.
     */
    @FXML
    private void handleOrSearch() {
        String tagType1 = tagTypeComboBox1.getValue();
        String tagValue1 = tagValueField1.getText();
        String tagType2 = tagTypeComboBox2.getValue();
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

    /**
     * Handles the action of creating a new album from the selected photos.
     * If no photos are selected, a warning message is displayed.
     * Prompts the user to enter the name of the new album.
     * Creates a new album with the entered name and adds the selected photos to it.
     * Adds the new album to the user's album collection.
     * Displays a success message after the album is created.
     */
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

            //check to see if album name exists in current user's album list
            for (Album album : user.getAlbums()) {
                if (album.getAlbumName().equalsIgnoreCase(albumName)) {
                    showAlert("Album Already Exists", "An album with the same name already exists. Please choose a different name.");
                    return;
                }
            }
            
            Album newAlbum = new Album(albumName);
            newAlbum.getPhotos().addAll(selectedPhotos);
            addUserAlbum(newAlbum);
            showAlert("Album Created", "A new album has been created with the selected photos.");
        });
    }

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
     * Adds a new album to the user's collection.
     * 
     * @param newAlbum the album to be added
     */
    private void addUserAlbum(Album newAlbum) {

        user.getAlbums().add(newAlbum);
        updateUsersList(user);

        FileManager.saveData(users, GlobalTags.getInstance().getTagTypes(), GlobalTags.getInstance().getRestrictedTagTypes());
    }

    /**
     * Updates the users list by replacing the user with the specified updated user.
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
     * Handles the action event when the user clicks on the "Back to Dashboard" button.
     * It loads the UserDashScene.fxml file and sets it as the scene for the current stage.
     * If an IOException occurs while loading the file, it prints the stack trace.
     *
     * @param event the action event triggered by the button click
     */
    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserDashScene.fxml"));
            Parent userDashboard = loader.load();
    
            // Retrieve the UserController and call the method that refreshes the album list
            UserController userController = loader.getController();
            userController.initSession(user); // Assume this method sets the user and refreshes the album list
            userController.onAlbumChanged(); // Explicitly call the method to refresh the albums list
    
            Scene userDashboardScene = new Scene(userDashboard);
    
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(userDashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log the error or show an alert
        }
    }
    

    /**
     * Clears the search results in the ListView.
     *
     * @param event the action event that triggered the method
     */
    @FXML
    private void handleClearSearch(ActionEvent event) {
        photoListView.setItems(FXCollections.observableArrayList());  // Clear the ListView
    }

    private void populateTagTypeComboBoxes() {
        Set<String> tagTypes = GlobalTags.getInstance().getTagTypes();
        tagTypeComboBox1.setItems(FXCollections.observableArrayList(tagTypes));
        tagTypeComboBox2.setItems(FXCollections.observableArrayList(tagTypes));
    }
}

