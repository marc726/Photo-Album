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


public class FileManager {

    private static Set<String> tagTypes = new HashSet<>();

    public static Set<String> getTagTypes() {
        return tagTypes;
    }

    public static void setTagTypes(Set<String> tags) {
        tagTypes = tags;
    }
   










    public static void saveData(List<User> users, Set<String> tagTypes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/data.dat"))) {
            oos.writeObject(users);
            oos.writeObject(tagTypes); // Save the tags along with the users
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }


       
    



    @SuppressWarnings("unchecked")
    public static List<User> loadData() {
        List<User> users = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/data.dat"))) {
            users = (List<User>) ois.readObject();
            Set<String> loadedTagTypes = (Set<String>) ois.readObject(); // Load the tags
            GlobalTags.getInstance().setTagTypes(loadedTagTypes); // Update the GlobalTags instance
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }


    
}

