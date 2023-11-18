/**
 * @author Marc Rizzolo
 */

package util;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import model.User;


public class FileManager {


   

    public static void saveData(List<User> users) {
        try {
            FileOutputStream fos = new FileOutputStream("data/data.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();
            fos.close();
            System.out.println("Data saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

       
    



    @SuppressWarnings("unchecked")
    public static List<User> loadData () {
        List<User> users = new ArrayList<User>();
        try {
            FileInputStream fis = new FileInputStream("data/data.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<User> userList = (List<User>) ois.readObject();
            users.clear();
            users.addAll(userList);
            ois.close();
            fis.close();
            System.out.println("Data loaded successfully(called from filemanager)");
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }


    
}

