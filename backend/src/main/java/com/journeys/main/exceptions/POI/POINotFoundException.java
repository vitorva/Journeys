/**
 * @team Journeys
 * @file POINotFoundException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.POI;

public class POINotFoundException extends RuntimeException {

    /**
     * Constructor of POINotFoundException
     * @param message a detail message as error
     */
    public POINotFoundException(String message) {
        super("Poi not found: " + message);
    }
}
