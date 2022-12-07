/**
 * @team Journeys
 * @file MvcConfig.java
 * @date January 21st, 2022
 */

package com.journeys.main.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Adding ressource handlers & locations to registery
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images-poi/**")
                .addResourceLocations("file:images-poi/");
    }

    /**
     * Add a prefix to every RestController with /api
     * @param configurer the PathMatchConfigurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api",
                HandlerTypePredicate.forAnnotation(RestController.class));
    }

    /**
     * Allow origins for
     * http://myjourneys.ch
     * http://www.myjourneys.ch
     * https://myjourneys.ch
     * https://www.myjourneys.ch
     * localhost
     * and containers present in the same
     * container network
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://myjourneys.ch",
                        "http://www.myjourneys.ch",
                        "https://www.myjourneys.ch",
                        "https://myjourneys.ch",
                        "https://myjourneys.ch",
                        "https://localhost:3000",
                        "https://localhost:8080",
                        "http://journeys-app.journeys-web",
                        "http://journeys-api.journeys-web")
                .allowedMethods("POST", "PATCH", "GET", "DELETE", "PUT")
                .allowCredentials(true);
    }
}