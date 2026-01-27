/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmaster.util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

/**
 * Session lifecycle listener for CR-08
 * Monitors session creation, destruction, and tracks active sessions
 * @author CR-08 Enhancement
 */
@Component
public class SessionMonitorListener implements HttpSessionListener {
    
    private static int activeSessions = 0;
    
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        activeSessions++;
        
        // Set initial last activity time
        session.setAttribute("lastActivityTime", System.currentTimeMillis());
        session.setAttribute("sessionCreatedTime", System.currentTimeMillis());
        
        System.out.println("Session created: " + session.getId() + 
                         " | Active sessions: " + activeSessions);
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        activeSessions--;
        
        Long createdTime = (Long) session.getAttribute("sessionCreatedTime");
        if (createdTime != null) {
            long sessionDuration = (System.currentTimeMillis() - createdTime) / 1000; // in seconds
            System.out.println("Session destroyed: " + session.getId() + 
                             " | Duration: " + sessionDuration + "s" +
                             " | Active sessions: " + activeSessions);
        } else {
            System.out.println("Session destroyed: " + session.getId() + 
                             " | Active sessions: " + activeSessions);
        }
        
        // Cleanup session attributes
        try {
            session.removeAttribute("lastActivityTime");
            session.removeAttribute("sessionCreatedTime");
        } catch (IllegalStateException e) {
            // Session already invalidated
        }
    }
    
    /**
     * Get current number of active sessions
     * @return Number of active sessions
     */
    public static int getActiveSessions() {
        return activeSessions;
    }
}
