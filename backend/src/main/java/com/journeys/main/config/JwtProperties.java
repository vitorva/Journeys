/**
 * @team Journeys
 * @file JwtProperties.java
 * @date January 21st, 2022
 */

package com.journeys.main.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Enables custom configuration in .properties to set
 * basic jwt properties
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "com.journeys.main.jwt")
public class JwtProperties {

    /**
     * name that will be stored in  the client
     */
    String accessToken;

    /**
     * Token duration set to limit One session
     */
    String duration;

    /**
     * Secret key
     */
    String secretKey;
}