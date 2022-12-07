/**
 * @team Journeys
 * @file JourneySaveException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.journey;

public class JourneySaveException extends RuntimeException {

    /**
     * Constructor of JourneySaveException
     * @param message a detail message as error
     */
    public JourneySaveException(String message) {
        super("Error when saving journey : " + message);
    }
}
