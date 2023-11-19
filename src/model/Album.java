package model;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
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
        this.photos.add(photo);
    }

    public void removePhoto(Photo photo) {
        if (photos.contains(photo)) {
            photos.add(photo);
        }
    }

    public String toString() {
        if (photos.isEmpty()) {
            return "NAME: " + albumName + "\nPHOTO COUNT: 0\nOldest Photo: N/A\nNewest Photo: N/A";
        }
    
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
    
        // Find the oldest photo based on the LocalDateTime attribute
        Optional<Photo> oldestPhoto = photos.stream()
            .min(Comparator.comparing(Photo::getDate));
        // Find the newest photo based on the LocalDateTime attribute
        Optional<Photo> newestPhoto = photos.stream()
            .max(Comparator.comparing(Photo::getDate));
    
        // Format the dates of the oldest and newest photos, or return "N/A" if not available
        String oldestPhotoDate = oldestPhoto.map(photo -> photo.getDate().format(formatter)).orElse("N/A");
        String newestPhotoDate = newestPhoto.map(photo -> photo.getDate().format(formatter)).orElse("N/A");
    
        return "NAME: " + albumName + "\nPHOTO COUNT: " + photos.size() + 
               "\nOldest Photo: " + oldestPhotoDate + 
               "\nNewest Photo: " + newestPhotoDate;
    }
}
