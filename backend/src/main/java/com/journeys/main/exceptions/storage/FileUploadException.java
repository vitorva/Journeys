/**
 * @team Journeys
 * @file FileUploadException.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.storage;

public class FileUploadException extends RuntimeException {

    /**
     * Constructor of FileUploadException
     * @param message a detail message as error
     */
    public FileUploadException(String message) {
        super("Error file upload : " + message);
    }
}