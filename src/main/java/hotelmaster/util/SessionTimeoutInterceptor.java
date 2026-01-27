/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmaster.util;

import hotelmaster.account.AccountSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Session timeout interceptor for CR-08
 * Monitors session activity and handles timeout scenarios
 * @author CR-08 Enhancement
 */
@Component
public class SessionTimeoutInterceptor implements HandlerInterceptor {
    
    private static final String LAST_ACTIVITY_TIME = "lastActivityTime";
    private static final long INACTIVITY_TIMEOUT = 30 * 60 * 1000; // 30 minutes in milliseconds
    
    @Autowired
    private AccountSession accountSession;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        
        // Skip check for login pages and public resources
        String uri = request.getRequestURI();
        if (uri.contains("/login") || uri.contains("/register") || 
            uri.contains("/resources/") || uri.contains("/getPhoto")) {
            return true;
        }
        
        if (session != null) {
            Long lastActivityTime = (Long) session.getAttribute(LAST_ACTIVITY_TIME);
            long currentTime = System.currentTimeMillis();
            
            if (lastActivityTime != null) {
                long inactiveTime = currentTime - lastActivityTime;
                
                // Check if session has been inactive too long
                if (inactiveTime > INACTIVITY_TIMEOUT) {
                    // Invalidate session and redirect to login
                    session.invalidate();
                    response.sendRedirect(request.getContextPath() + "/login?timeout=true");
                    return false;
                }
            }
            
            // Update last activity time
            session.setAttribute(LAST_ACTIVITY_TIME, currentTime);
        } else {
            // No session exists, check if user is trying to access protected resource
            if (accountSession != null && accountSession.getAccount() != null) {
                response.sendRedirect(request.getContextPath() + "/login?expired=true");
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Optional: Add session info to model if needed
        HttpSession session = request.getSession(false);
        if (session != null && modelAndView != null) {
            Long lastActivityTime = (Long) session.getAttribute(LAST_ACTIVITY_TIME);
            if (lastActivityTime != null) {
                long timeRemaining = INACTIVITY_TIMEOUT - (System.currentTimeMillis() - lastActivityTime);
                modelAndView.addObject("sessionTimeRemaining", timeRemaining / 60000); // in minutes
            }
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Cleanup if needed
    }
}
