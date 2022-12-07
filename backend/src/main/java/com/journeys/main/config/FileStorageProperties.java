/**
 * @team Journeys
 * @file FileStorageProperties.java
 * @date January 21s, 2022
 */

package com.journeys.main.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Enables custom entry in .properties file
 * with root node starting  at file.*
 */
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;

    /**
     * @return the current upload directory
     */
    public String getUploadDir() {
        return uploadDir;
    }

    /**
     * Sets the upload directory with the new upload directory passed in parameter
     * @param uploadDir the new upload directory
     */
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}