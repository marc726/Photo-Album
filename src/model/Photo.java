import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Represents a Photo with attributes.
 * 
 * @author Marc Rizzolo
 */

public class Photo implements Serializable {
    
    static final long serialVersionUID = 1L;
    private ArrayList<Tag> tags = new ArrayList<>();
    private String name;
    private String caption;
    private Calendar date;
    // private Image image;


    public Photo(String name, Calendar date){
        this.name = name;
        this.caption = "";
        //this.image = image;
        this.date = date;
        this.tags = new ArrayList<Tag>();
    }


    //helpers
    public String getName(){
        return name;
    }

    public String getCaption(){
        return caption;
    }

    public void setCaption(String caption){
        this.caption = caption;
    }

    public Calendar getDate(){
        return date;
    }

    public ArrayList<Tag> getTags(){
        return tags;
    }
}