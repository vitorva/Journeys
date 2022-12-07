/**
 * @team Journeys
 * @file UserSaveException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.user;

public class UserSaveException extends RuntimeException {

    /**
     * Constructor of UserSaveException
     * @param message a detail message as error
     */
    public UserSaveException(String message) {
        super("Could not save User : " + message);
    }
}