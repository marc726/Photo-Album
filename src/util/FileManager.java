/**
 * @author Marc Rizzolo
 */

package util;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import model.User;

public class FileManager {

    public static void saveData(ArrayList<User> users) {
        try {
            FileOutputStream fileOut = new FileOutputStream("data/data.dat");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(users);
            objectOut.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

