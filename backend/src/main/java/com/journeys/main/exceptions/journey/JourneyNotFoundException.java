/**
 * @team Journeys
 * @file JourneyNotFoundException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.journey;

public class JourneyNotFoundException extends RuntimeException {

    /**
     * Constructor of JourneyNotFoundException
     * @param message a detail message as error
     */
    public JourneyNotFoundException(String message) {
        super("Journey not found: " + message);
    }
}
