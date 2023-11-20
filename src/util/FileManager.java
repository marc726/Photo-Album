/**
 * @author Marc Rizzolo
 */

package util;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.User;


/**
 * The FileManager class provides methods for saving and loading data, as well as managing tag types.
 */
public class FileManager {


    // Define a private static set to store tag types
    private static Set<String> tagTypes = new HashSet<>();
    private static Set<String> restrictedTagTypes = new HashSet<>();

    // Method to get the tag types
    public static Set<String> getTagTypes() {
        return tagTypes;
    }

    // Method to set the tag types
    public static void setTagTypes(Set<String> tags) {
        tagTypes = tags;
    }

   










    /**
     * Saves the given list of users and set of tag types to a file.
     * 
     * @param users    the list of users to be saved
     * @param tagTypes the set of tag types to be saved
     */
    public static void saveData(List<User> users, Set<String> tagTypes, Set<String> restrictedTagTypes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/data.dat"))) {
            oos.writeObject(users);
            oos.writeObject(tagTypes); // Save the tags along with the users
            oos.writeObject(restrictedTagTypes);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }


       
    



    /**
     * Loads the data from the file "data/data.dat" and returns a list of User objects.
     * This method also updates the GlobalTags instance with the loaded tag types.
     * 
     * @return The list of User objects loaded from the file.
     */
    @SuppressWarnings("unchecked")
    public static List<User> loadData() {
        List<User> users = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/data.dat"))) {
            users = (List<User>) ois.readObject();
            Set<String> loadedTagTypes = (Set<String>) ois.readObject(); // Load the tags
            GlobalTags.getInstance().setTagTypes(loadedTagTypes); // Update the GlobalTags instance
            Set<String> loadedRestrictedTagTypes = (Set<String>) ois.readObject();
            GlobalTags.getInstance().setRestrictedTagTypes(loadedRestrictedTagTypes);
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }


    
}

