/**
 * @team Journeys
 * @file UserAlreadyExistsException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.user;

public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructor of UserAlreadyExistsException
     * @param message a detail message as error
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}