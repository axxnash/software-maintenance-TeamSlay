/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmaster;

import hotelmaster.rooms.RoomFeatures;
import hotelmaster.rooms.RoomPhotos;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Represents a hotel room
 * Refactored for CR-04 to separate concerns:
 * - RoomFeatures: handles room amenities/features
 * - RoomPhotos: handles room images
 * 
 * @author Danny
 */

public class Room {
    
    //Variables for the Room table in the database
    private int roomID;
    private String roomName;
    private String floor;
    private double pricePerNight;
    private int maxGuests;
    private String roomViewURL;
    private String desc;
    
    // Refactored: Features extracted to separate class
    private RoomFeatures roomFeatures;
    
    // Refactored: Photos extracted to separate class
    private RoomPhotos roomPhotos;
    
    // Legacy support - maintain backward compatibility
    @Deprecated
    private Set<String> featuresTest;

    //Empty constructor
    public Room(){
        this.roomFeatures = new RoomFeatures();
        this.roomPhotos = new RoomPhotos();
    }
    
    //Constructor with parameters
    public Room(String roomName, String floor, double pricePerNight, int maxGuests){
        this.roomName = roomName;
        this.floor = floor;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.roomFeatures = new RoomFeatures();
        this.roomPhotos = new RoomPhotos();
        
        setRoomViewURL(roomName);
    }
    
    //Getters and setters
    public int getRoomID() {
        return roomID;
    }
    
    public void setRoomID(int roomID){
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
        setRoomViewURL(roomName);
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }
        
    public String getRoomViewURL(){
        
        return roomViewURL;
    }

    public final void setRoomViewURL(String roomName){
        roomViewURL = roomName.trim().replaceAll("[^a-zA-Z0-9\\-\\s\\.]", "");
        roomViewURL = roomViewURL.replaceAll("[\\-| |\\.]+", "-");
        roomViewURL = roomViewURL.toLowerCase();
    }
    
    // ===== Refactored: Delegated methods for features =====
    
    /**
     * Get room features object
     * @return RoomFeatures object containing all amenities
     */
    public RoomFeatures getRoomFeatures() {
        if (roomFeatures == null) {
            roomFeatures = new RoomFeatures(this.roomID);
        }
        return roomFeatures;
    }
    
    public void setRoomFeatures(RoomFeatures roomFeatures) {
        this.roomFeatures = roomFeatures;
    }
    
    /**
     * Backward compatibility: Get features HashMap
     * @deprecated Use getRoomFeatures() instead
     * @return HashMap of features
     */
    @Deprecated
    public HashMap<String, Boolean> getFeatures() {
        return getRoomFeatures().getFeatures();
    }
    
    /**
     * Backward compatibility: Set features HashMap
     * @deprecated Use setRoomFeatures() instead
     * @param features HashMap of features
     */
    @Deprecated
    public void setFeatures(HashMap<String, Boolean> features) {
        getRoomFeatures().setFeatures(features);
    }
    
    // ===== Refactored: Delegated methods for photos =====
    
    /**
     * Get room photos object
     * @return RoomPhotos object containing all images
     */
    public RoomPhotos getRoomPhotos() {
        if (roomPhotos == null) {
            roomPhotos = new RoomPhotos(this.roomID);
        }
        return roomPhotos;
    }
    
    public void setRoomPhotos(RoomPhotos roomPhotos) {
        this.roomPhotos = roomPhotos;
    }
    
    /**
     * Backward compatibility: Get photos list
     * @deprecated Use getRoomPhotos().getPhotos() instead
     * @return List of photos
     */
    @Deprecated
    public List<Photo> getPhotos(){
        return getRoomPhotos().getPhotos();
    }
    
    /**
     * Backward compatibility: Set photos list
     * @deprecated Use getRoomPhotos().setPhotos() instead
     * @param photos List of photos
     */
    @Deprecated
    public void setPhotos(List<Photo> photos) {
        getRoomPhotos().setPhotos(photos);
    }
    
    /**
     * Backward compatibility: Get primary photo
     * @deprecated Use getRoomPhotos().getPrimaryPhoto() instead
     * @return Primary photo
     */
    @Deprecated
    public Photo getPhoto() {
        return getRoomPhotos().getPrimaryPhoto();
    }
    
    /**
     * Backward compatibility: Set primary photo
     * @deprecated Use getRoomPhotos().setPrimaryPhoto() instead
     * @param photo Primary photo
     */
    @Deprecated
    public void setPhoto(Photo photo) {
        getRoomPhotos().setPrimaryPhoto(photo);
    }
    
    // ===== Legacy support =====
    
    @Deprecated
    public Set<String> getFeaturesTest() {
        return featuresTest;
    }

    @Deprecated
    public void setFeaturesTest(Set<String> featuresTest) {
        this.featuresTest = featuresTest;
    }
    
}
