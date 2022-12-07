/**
 * @team Journeys
 * @file POIWriteFormatException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.POI;

public class POIWriteFormatException extends RuntimeException {

    /**
     * Constructor of POIWriteFormatException
     * @param message a detail message as error
     */
    public POIWriteFormatException(String message) {
        super("POI write error: " + message);
    }
}