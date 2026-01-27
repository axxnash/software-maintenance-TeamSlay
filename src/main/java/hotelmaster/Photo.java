/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmaster;

import java.util.Comparator;

/**
 * Photo entity representing room images
 * Enhanced for CR-07 with sorting capabilities
 * @author mathe_000
 */
public class Photo {
    
    //variables associated with the room_images table
    private int imageID;
    private int roomID;
    private byte[] image;
    private String title;
    private int primary;
    
    //constructors
    public Photo() { }
    
    public Photo(int imageID, int roomID, byte[] image, String title, int primary) {
        this.imageID = imageID;
        this.roomID = roomID;
        this.image = image;
        this.title = title;
        this.primary = primary;
    }
    
    //Getters and setters

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }
    
    public int getImageID() {
        return imageID;
    }
    
    public void setImageID(int imageID){
        this.imageID = imageID;
    }
    
    public int getRoomID() {
        return roomID;
    }
    
    public void setRoomID(int roomID){
        this.roomID = roomID;
    }
    
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Comparator to sort photos with primary images first
     * CR-07: Improves gallery display consistency
     */
    public static final Comparator<Photo> PRIMARY_FIRST_COMPARATOR = new Comparator<Photo>() {
        @Override
        public int compare(Photo p1, Photo p2) {
            // Primary photos (thumbnail=1) should come first
            if (p1.getPrimary() != p2.getPrimary()) {
                return Integer.compare(p2.getPrimary(), p1.getPrimary()); // Descending order
            }
            // If both are primary or both are not, sort by imageID
            return Integer.compare(p1.getImageID(), p2.getImageID());
        }
    };
    
    /**
     * Check if this is a primary/thumbnail image
     * @return true if primary, false otherwise
     */
    public boolean isPrimary() {
        return this.primary == 1;
    }
    
}
