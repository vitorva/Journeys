/**
 * @team Journeys
 * @file JourneyInternalSaveException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.journey;

public class JourneyInternalSaveException extends RuntimeException {

    /**
     * Constructor of JourneyInternalSaveException
     * @param message a detail message as error
     */
    JourneyInternalSaveException(String message) {
        super("Could not save Journey on our side : " + message);
    }
}