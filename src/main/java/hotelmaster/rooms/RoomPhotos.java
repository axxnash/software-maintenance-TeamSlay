/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmaster.rooms;

import hotelmaster.Photo;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles room photos/images
 * Separated from Room class for better maintainability
 * @author Refactored for CR-04
 */
public class RoomPhotos {
    
    private int roomId;
    private List<Photo> photos;
    private Photo primaryPhoto;
    
    /**
     * Default constructor
     */
    public RoomPhotos() {
        this.photos = new ArrayList<>();
    }
    
    /**
     * Constructor with room ID
     * @param roomId The ID of the room these photos belong to
     */
    public RoomPhotos(int roomId) {
        this.roomId = roomId;
        this.photos = new ArrayList<>();
    }
    
    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public List<Photo> getPhotos() {
        return photos;
    }
    
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
    
    public Photo getPrimaryPhoto() {
        return primaryPhoto;
    }
    
    public void setPrimaryPhoto(Photo primaryPhoto) {
        this.primaryPhoto = primaryPhoto;
    }
    
    /**
     * Add a photo to the collection
     * @param photo Photo to add
     */
    public void addPhoto(Photo photo) {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(photo);
    }
    
    /**
     * Remove a photo from the collection
     * @param photo Photo to remove
     */
    public void removePhoto(Photo photo) {
        if (photos != null) {
            photos.remove(photo);
        }
    }
    
    /**
     * Get the number of photos
     * @return Count of photos
     */
    public int getPhotoCount() {
        return photos != null ? photos.size() : 0;
    }
    
    /**
     * Check if room has any photos
     * @return true if photos exist, false otherwise
     */
    public boolean hasPhotos() {
        return photos != null && !photos.isEmpty();
    }
}
