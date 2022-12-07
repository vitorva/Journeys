/**
 * @team Journeys
 * @file JourneyDeleteException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.journey;

public class JourneyDeleteException extends RuntimeException {

    /**
     * Constructor of JourneyDeleteException
     * @param message a detail message as error
     */
    public JourneyDeleteException(String message) {
        super("Could not delete: " + message);
    }
}