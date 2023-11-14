package model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Represents a User with a username.
 * 
 * @author Marc Rizzolo
 */


public class User implements Serializable{
    
    static final long serialVersionUID = 1L;
    private String username;
    private ArrayList<Album> albums;
    
    public User (String username){
        this.username = username;
        albums = new ArrayList<Album>();
    }

    //username methods
    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String toString(){
        return this.username;
    }

    //album methods
    public ArrayList<Album> getAlbums() {
		return albums;
	}
}
