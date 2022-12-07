/**
 * @team Journeys
 * @file UserIllegalDataException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.user;

public class UserIllegalDataException extends RuntimeException {

    /**
     * Constructor of UserIllegalDataException
     * @param message a detail message as error
     */
    public UserIllegalDataException(String message) {
        super("Illegal data");
    }
}