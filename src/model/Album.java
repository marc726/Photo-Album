package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Album with attributes.
 * 
 * @author Marc Rizzolo
 */


public class Album implements Serializable{
    private static final long serialVersionUID = 1L;

    private String albumName;
    private List<Photo> photos;


    public Album(String albumName) {
        this.albumName = albumName;
        this.photos = new ArrayList<Photo>();
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String name) {
        this.albumName = name;
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }

    public int getNumPhotos() {
        return this.photos.size();
    }

    public void addPhoto(Photo photo) {
        if (!photos.contains(photo)) {
            photos.add(photo);
        }
    }

    public void removePhoto(Photo photo) {
        if (photos.contains(photo)) {
            photos.add(photo);
        }
    }

    public String toString() {
        return "NAME: " + albumName + "\nPHOTO COUNT: " + photos.size();
    }
}
