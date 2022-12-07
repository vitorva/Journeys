/**
 * @team Journeys
 * @file POIReadFormatException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.POI;

public class POIReadFormatException extends RuntimeException {

    /**
     * Constructor of POIReadFormatException
     * @param message a detail message as error
     */
    public POIReadFormatException(String message) {
        super("POI read error: " + message);
    }
}