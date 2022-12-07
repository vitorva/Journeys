/**
 * @team Journeys
 * @file ExperienceNotFound.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.experience;

public class ExperienceNotFound extends RuntimeException {

    /**
     * Constructor of ExperienceNotFound
     * @param message a detail message as error
     */
    public ExperienceNotFound(String message) {
        super("Could not found Experience on our side : " + message);
    }
}