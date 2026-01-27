/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmaster.account;

import java.io.Serializable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Session-scoped component for account management
 * Enhanced for CR-08 with session tracking
 * @author Doug
 */

@Component("accountSession")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value=WebApplicationContext.SCOPE_SESSION)
public class AccountSessionImpl implements AccountSession, Serializable {
    private Account account;
    private int counter;
    private Long loginTime;
    private Long lastAccessTime;

    public AccountSessionImpl() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        if (account != null) {
            this.loginTime = System.currentTimeMillis();
        }
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
    
    /**
     * CR-08: Get login timestamp
     * @return Login time in milliseconds
     */
    public Long getLoginTime() {
        return loginTime;
    }
    
    /**
     * CR-08: Set login timestamp
     * @param loginTime Login time in milliseconds
     */
    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }
    
    /**
     * CR-08: Get last access timestamp
     * @return Last access time in milliseconds
     */
    public Long getLastAccessTime() {
        return lastAccessTime;
    }
    
    /**
     * CR-08: Update last access timestamp
     */
    public void updateLastAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }
    
    /**
     * CR-08: Calculate session duration in minutes
     * @return Session duration in minutes
     */
    public long getSessionDurationMinutes() {
        if (loginTime == null) {
            return 0;
        }
        return (System.currentTimeMillis() - loginTime) / 60000;
    }
    
    /**
     * CR-08: Check if user is logged in
     * @return true if account exists, false otherwise
     */
    public boolean isLoggedIn() {
        return account != null;
    }
}
