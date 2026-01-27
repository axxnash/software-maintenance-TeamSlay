/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmaster.util;

import javax.servlet.http.HttpSession;

/**
 * Utility class for session management
 * CR-08: Provides session-related helper methods
 * @author CR-08 Enhancement
 */
public class SessionUtil {
    
    private static final String LAST_ACTIVITY_TIME = "lastActivityTime";
    private static final String SESSION_CREATED_TIME = "sessionCreatedTime";
    private static final long INACTIVITY_TIMEOUT = 30 * 60 * 1000; // 30 minutes
    
    /**
     * Check if session is expired due to inactivity
     * @param session HTTP session
     * @return true if expired, false otherwise
     */
    public static boolean isSessionExpired(HttpSession session) {
        if (session == null) {
            return true;
        }
        
        Long lastActivityTime = (Long) session.getAttribute(LAST_ACTIVITY_TIME);
        if (lastActivityTime == null) {
            return false;
        }
        
        long inactiveTime = System.currentTimeMillis() - lastActivityTime;
        return inactiveTime > INACTIVITY_TIMEOUT;
    }
    
    /**
     * Update session last activity time
     * @param session HTTP session
     */
    public static void updateActivity(HttpSession session) {
        if (session != null) {
            session.setAttribute(LAST_ACTIVITY_TIME, System.currentTimeMillis());
        }
    }
    
    /**
     * Get remaining session time in minutes
     * @param session HTTP session
     * @return Remaining time in minutes
     */
    public static long getRemainingTimeMinutes(HttpSession session) {
        if (session == null) {
            return 0;
        }
        
        Long lastActivityTime = (Long) session.getAttribute(LAST_ACTIVITY_TIME);
        if (lastActivityTime == null) {
            return 30; // Default timeout
        }
        
        long inactiveTime = System.currentTimeMillis() - lastActivityTime;
        long remainingTime = INACTIVITY_TIMEOUT - inactiveTime;
        return Math.max(0, remainingTime / 60000);
    }
    
    /**
     * Get session duration in minutes
     * @param session HTTP session
     * @return Session duration in minutes
     */
    public static long getSessionDurationMinutes(HttpSession session) {
        if (session == null) {
            return 0;
        }
        
        Long createdTime = (Long) session.getAttribute(SESSION_CREATED_TIME);
        if (createdTime == null) {
            return 0;
        }
        
        return (System.currentTimeMillis() - createdTime) / 60000;
    }
    
    /**
     * Safely invalidate session
     * @param session HTTP session
     */
    public static void invalidateSession(HttpSession session) {
        if (session != null) {
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                // Session already invalidated
            }
        }
    }
}
