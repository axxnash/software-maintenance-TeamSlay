/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmaster.rooms;

import java.util.HashMap;

/**
 * Handles room features/amenities
 * Separated from Room class for better maintainability
 * @author Refactored for CR-04
 */
public class RoomFeatures {
    
    private int roomId;
    private HashMap<String, Boolean> features;
    
    /**
     * Default constructor - initializes all standard features as false
     */
    public RoomFeatures() {
        this.features = new HashMap<>();
        initializeDefaultFeatures();
    }
    
    /**
     * Constructor with room ID
     * @param roomId The ID of the room these features belong to
     */
    public RoomFeatures(int roomId) {
        this.roomId = roomId;
        this.features = new HashMap<>();
        initializeDefaultFeatures();
    }
    
    /**
     * Initialize default hotel features
     */
    private void initializeDefaultFeatures() {
        features.put("Balcony", Boolean.FALSE);
        features.put("Breakfast in Bed", Boolean.FALSE);
        features.put("Jacuzzi", Boolean.FALSE);
        features.put("Netflix Enabled TV", Boolean.FALSE);
        features.put("Open Bar", Boolean.FALSE);
        features.put("Room Service", Boolean.FALSE);
        features.put("Wifi", Boolean.FALSE);
    }
    
    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public HashMap<String, Boolean> getFeatures() {
        return features;
    }
    
    public void setFeatures(HashMap<String, Boolean> features) {
        this.features = features;
    }
    
    /**
     * Check if a specific feature is available
     * @param featureName Name of the feature
     * @return true if feature is enabled, false otherwise
     */
    public boolean hasFeature(String featureName) {
        return features.getOrDefault(featureName, false);
    }
    
    /**
     * Enable a specific feature
     * @param featureName Name of the feature to enable
     */
    public void enableFeature(String featureName) {
        features.put(featureName, Boolean.TRUE);
    }
    
    /**
     * Disable a specific feature
     * @param featureName Name of the feature to disable
     */
    public void disableFeature(String featureName) {
        features.put(featureName, Boolean.FALSE);
    }
}
