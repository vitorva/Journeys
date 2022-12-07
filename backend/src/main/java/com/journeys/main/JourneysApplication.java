/**
 * @team Journeys
 * @file JourneysApplication.java
 * @date January 21st, 2022
 */

package com.journeys.main;

import com.journeys.main.config.FileStorageProperties;
import com.journeys.main.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties(
        {
                JwtProperties.class,
                FileStorageProperties.class
        }
)
public class JourneysApplication extends SpringBootServletInitializer {

    /**
     * Launches SpringApplication
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(JourneysApplication.class, args);
    }
}