package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;

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
    private LocalDateTime date;
    private String imagePath;
    


    public Photo(String name, LocalDateTime date) {
        this.name = name;
        this.caption = "N/A";
        this.date = date;
        this.tags = new ArrayList<Tag>();
    }

    public Photo() {
        this.name = "";
        this.caption = "N/A";
        this.date = LocalDateTime.now();
        this.tags = new ArrayList<Tag>();
    }


    //helpers
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getCaption(){
        return caption;
    }

    public void setCaption(String caption){
        this.caption = caption;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ArrayList<Tag> getTags(){
        return tags;
    }
    
    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void addTag(Tag tag){
        if (!tags.contains(tag)){
            tags.add(tag);
        }
    }
}