/**
 * @team Journeys
 * @file FileUploadException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.storage;

import java.net.MalformedURLException;

public class StorageException extends RuntimeException {

    /**
     * Constructor of StorageException
     * @param message a detail message as error
     * @param e a MalformedURLException
     */

    public StorageException(String message, MalformedURLException e) {
        super("Error stockage : " + message);
    }

    /**
     * Constructor of StorageException
     * @param message a detail message as error
     */
    public StorageException(String message) {
        super("Error stockage : " + message);
    }
}