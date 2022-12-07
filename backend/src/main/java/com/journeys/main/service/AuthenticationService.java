/**
 * @team Journeys
 * @file AuthenticationService.java
 * @date January 21st, 2022
 */

package com.journeys.main.service;

import com.journeys.main.authentication.JourneysAuthenticationProvider;
import com.journeys.main.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JourneysAuthenticationProvider authenticationManager;
    private final UserService userService;

    /**
     * Controller of AuthenticationService
     * @param authenticationProvider
     * @param userService
     */
    @Autowired
    AuthenticationService(JourneysAuthenticationProvider authenticationProvider, UserService userService) {
        this.authenticationManager = authenticationProvider;
        this.userService = userService;
    }

    /**
     * Authenticate a User
     * @param username the username of the User
     * @param password the password of the User
     */
    public void authenticateUser(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    /**
     * Getting details of the current user
     * @return the user
     */
    public User getTheDetailsOfCurrentUser() {
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuthentication != null) {
            if (currentAuthentication instanceof AnonymousAuthenticationToken)
                return null;
            else
                return (User) currentAuthentication.getPrincipal();
        } else
            return null;
    }

    /**
     * Get username by its username
     * @param username the username
     * @return the User
     */
    public User loadByUsername(String username) {
        return userService.getUser(username);
    }
}