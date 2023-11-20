import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Album;
import model.Photo;
import model.User;
import util.FileManager;
import util.GlobalTags;

public class Photos {

    public static void main(String[] args) {
        Application.launch(PhotosApp.class, args);
    }

    public static class PhotosApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            checkAndInitializeData();

            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScene.fxml"));
            primaryStage.setTitle("Photos Application");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }

        private void checkAndInitializeData() {
        // Check if data.dat exists
        Path dataPath = Paths.get("data/data.dat");
        if (!Files.exists(dataPath)) {
            // Create a new list of users
            List<User> users = new ArrayList<>();

            // Add the 'stock' user
            User stockUser = new User("stock");
            users.add(stockUser);

            // Create an album for the 'stock' user
            Album stockAlbum = new Album("Stock Album");
            stockUser.getAlbums().add(stockAlbum);

            // Scan the data/stock directory for photos
            File stockDir = new File("data/stock");
            File[] stockPhotos = stockDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png") ||
                name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".bmp"));

            if (stockPhotos != null) {
                for (File photoFile : stockPhotos) {
                    // Create a Photo object for each file
                    Photo photo = new Photo(photoFile.getName(), LocalDateTime.now()); // Adjust the date as needed
                    photo.setImagePath(photoFile.getPath());

                    // Add the photo to the stock album
                    stockAlbum.addPhoto(photo);
                }
            }

            // Create default tags
            Set<String> defaultTags = new HashSet<>();
            defaultTags.add("Person");
            defaultTags.add("Place");
            defaultTags.add("Item");
            // Add more tags as needed

            // Set the default tags to GlobalTags
            GlobalTags.getInstance().setTagTypes(defaultTags);

            // Save the users list and the tags to data.dat
            try {
                FileManager.saveData(users, defaultTags);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    }
}
