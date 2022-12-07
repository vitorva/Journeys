/**
 * @team Journeys
 * @file UserNotFoundException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.user;

public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor of UserNotFoundException
     * @param message a detail message as error
     */
    public UserNotFoundException(String message) {
        super("User not found: " + message);
    }
}