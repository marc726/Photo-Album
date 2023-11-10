/**
 * @author Bhavya Patel 
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class contains the main method to launch the Photos application.
 * It also contains the inner class PhotosApp which extends Application and sets up the primary stage.
 * The start method loads the LoginScene.fxml file and sets it as the primary stage.
 */
public class Photos {

    public static void main(String[] args) {
        Application.launch(PhotosApp.class, args);
    }

    public static class PhotosApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScene.fxml"));
            primaryStage.setTitle("Photos Application");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }
}